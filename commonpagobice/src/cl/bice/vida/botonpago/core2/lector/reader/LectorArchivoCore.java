package cl.bice.vida.botonpago.core2.lector.reader;

import cl.bice.vida.botonpago.core2.escritor.config.WriterConfigCore;
import cl.bice.vida.botonpago.core2.lector.config.ReaderConfigCore;
import cl.bice.vida.botonpago.core2.lector.exception.LecturaException;
import cl.bice.vida.botonpago.core2.vo.EntidadEstructuraVO;
import cl.bice.vida.botonpago.core2.vo.FieldDescripcionVO;
import cl.bice.vida.botonpago.core2.vo.FieldStructureVo;
import cl.bice.vida.botonpago.core2.vo.FileStructureVo;
import cl.bice.vida.botonpago.core2.vo.LectorDinamicoVO;
import cl.bice.vida.botonpago.core2.vo.ResultadoProcesoVO;

import cl.bice.vida.botonpago.utils.FechaUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class LectorArchivoCore {
    
    private static ReaderConfigCore xmlconfig;
        
    /**
     * Carga la configuracion
     * @param fileConfig
     * @return
     */
    public static void loadConfig(String file) {
        xmlconfig = new ReaderConfigCore();
        xmlconfig.loadConfig(file, true);
    }

    /**
     * Carga la configuracion a partir de un xml directamente
     * @param xml
     */
    public static void loadXMLConfig(String xml) {
        xmlconfig = new ReaderConfigCore();
        xmlconfig.loadConfig(xml, false);
    }

    /**
     * Carga la configuracion a partir de un xml directamente
     * @param xml
     */
    public static void loadXMLConfig(String xml, List<FieldDescripcionVO> descripcionCampoAdicional) {
        xmlconfig = new ReaderConfigCore();
        xmlconfig.loadConfig(xml, false, descripcionCampoAdicional);
    }
    
    /**
     * Entrega la lista de medios de pago
     * que se pueden hacer upload de archivo
     * @return
     */
    public static List getArchivosParaCarga() {
        List mediosUp = new ArrayList();
        Iterator medios = xmlconfig.config.values().iterator();  
        while (medios.hasNext()) {            
            FileStructureVo filePrc = (FileStructureVo) medios.next();
            if (filePrc.isUpload()) {
                EntidadEstructuraVO med = new EntidadEstructuraVO();
                med.setArchivoId(filePrc.getNombre());
                med.setDescripcion(filePrc.getDescripcion());
                mediosUp.add(med);
            }            
        }
        return mediosUp;
    }    

    /**
     * Retorna el codigo del medio de pago en
     * la base de datos y esquema de pago bice
     * @param codeMedio
     * @return numero del medio de pago en la base de datos
     */
    public static String getProcesoCodigo(String codeMedio) {
        FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(codeMedio);  
        return filePrc.getDatabasecode();
    }    
    
    
    /**
     * String 
     * @param filenamepath
     * @param medioPago
     * @return
     */
    public static ResultadoProcesoVO doProcesarArchivo(String filenameUpload, long sizebytes, String filenamepath, String nombreProceso, String remoteuser) {
        
        //Investiga el tipo de structura del archivo a procesar
        ResultadoProcesoVO resp = null;
        FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(nombreProceso);  

        if (filePrc != null) {
            //Inicia el procesamiento segun el tipo de archivo        
            if (filePrc.getTipoArchivo().equalsIgnoreCase("LargoFijo")) resp = doProcesarLargoFijo(filenamepath, nombreProceso);
            if (filePrc.getTipoArchivo().equalsIgnoreCase("Delimitado")) resp = doProcesarDelimitado(filenamepath, nombreProceso);
            if (filePrc.getTipoArchivo().equalsIgnoreCase("XML")) resp = doProcesarXml(filenamepath, nombreProceso);
            if (filePrc.getTipoArchivo().equalsIgnoreCase("XLS")) resp = doProcesarXLS(filenamepath, nombreProceso);
            if (resp != null) {
                resp.setCargaInfo(filePrc);
                resp.setFilenameSaveServerToProcess(filenamepath);
                resp.setFilenameUpload(filenameUpload);
                resp.setTotalBytesFile(sizebytes);
                resp.setRemoteuser(remoteuser);
            }
        }
        return resp;
    }
    
    /**
     * Procesa transacciones archivo XLS
     * @param fileDat
     * @return
     */
    private static ResultadoProcesoVO doProcesarXLS(String fileDat, String medioPago) {
        ResultadoProcesoVO resp = new ResultadoProcesoVO();
       try {
            FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(medioPago);
            if (filePrc != null) {
                resp.setNombreProceso(filePrc.getDescripcion());    
                long row = 1;
                
                File file = new File(fileDat);
                InputStream xlsinput = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(xlsinput);
                HSSFSheet sheet = workbook.getSheetAt(0);
                                    
                int registros = sheet.getLastRowNum() ;
                Iterator rows = sheet.rowIterator();
                while(rows.hasNext()){      
                    LectorDinamicoVO trx = new LectorDinamicoVO();
                    String linea = "";
                    HSSFRow lineax = (HSSFRow) rows.next();
                    if (row >= filePrc.getFilaInicio()) {
                        if (filePrc.isUsoUltimoRegistro()==false) {
                            if (lineax.getRowNum() == registros) break;
                        }
                        
                        if (lineax != null) {
                            for (int cl = 0; cl < lineax.getLastCellNum(); cl++) {
                                HSSFCell celda = lineax.getCell((short)cl);
                                String valor = getCellValor(celda);
                                linea = linea + valor + "|";
                            }
                        }
                    
                        filePrc.setDataRow(linea);
                        //Recupera campos obligatorios
                        if (linea !=null  && linea.trim().length() > 0) { 
                            try {                            
                                String errores = "";
                                Enumeration camp = filePrc.getCampos().elements();
                                while (camp.hasMoreElements()){
                                    FieldStructureVo campo = (FieldStructureVo) camp.nextElement();
                                    Object rep = null;
                                    try {
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.String")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Long")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Double")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Boolean")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Float")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Integer")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.util.Date")) rep = filePrc.getValueRow(campo.getNombre());                                                
                                        if (rep == null) {
                                            errores = errores + "campo: '" + campo.getDescripcionCampo() + "' error en su valor;/n";
                                        } else if (rep != null && rep instanceof Exception) {
                                            errores = errores + ((Exception)rep).getMessage();
                                        } else {
                                            trx.addField(campo.getNombre(), rep);
                                        }
                                    } catch (Exception xc) {
                                    errores = errores + "campo: '" + campo.getDescripcionCampo() + "' error en su valor;/n";
                                    }
                                }                            
                                if (errores != null && errores.length() > 0) throw new LecturaException(errores);
                                
                                //Agrega Transaccion al detalle
                                resp.addTransaccion(trx);
                                resp.setNumeroTransaccionesProcesadas(resp.getNumeroTransaccionesProcesadas()+1);
                            } catch (Exception er) {
                            
                                //Agrega Errores al detalle
                                resp.addError(row, er.getMessage());
                                resp.setNumeroTransaccionesError(resp.getNumeroTransaccionesError()+1);
                            }
                        }
                    }
                    row++;                        
                } 
                resp.setOk(true);
                xlsinput.close();
            }
        } catch (Exception e) {
            resp.setError(e);
            resp.setOk(false);
        }
        return resp;
    }    

    
    /**
     * Procesa transacciones archivo largo fijo
     * @param fileDat
     * @return
     */
    private static ResultadoProcesoVO doProcesarLargoFijo(String fileDat, String medioPago) {
        ResultadoProcesoVO resp = new ResultadoProcesoVO();
                
        try {
            FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(medioPago);            
            if (filePrc != null) {
                resp.setNombreProceso(filePrc.getDescripcion());    
                long row = 1;
                long registros = getLineCountFile(fileDat);
                BufferedReader inputBufReader = new BufferedReader(new FileReader(fileDat));
                String linea;
                while ((linea = inputBufReader.readLine()) != null) {
                    LectorDinamicoVO trx = new LectorDinamicoVO();
                    if (row >= filePrc.getFilaInicio() ) {
                        if (filePrc.isUsoUltimoRegistro()==false) {
                            if (row == registros) break;
                        }
                        filePrc.setDataRow(linea); 
                        //Recupera campos obligatorios
                        if (linea !=null  && linea.trim().length() > 0) { 
                            try {
                            
                                String errores = "";
                                Enumeration camp = filePrc.getCampos().elements();
                                while (camp.hasMoreElements()){
                                    FieldStructureVo campo = (FieldStructureVo) camp.nextElement();
                                    Object rep = null;
                                    try {
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.String")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Long")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Double")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Boolean")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Float")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Integer")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.util.Date")) rep = filePrc.getValueRow(campo.getNombre());                                                
                                        if (rep == null) {
                                            errores = errores + "campo: '" + campo.getDescripcionCampo() + "' error en su valor;/n";
                                        } else if (rep != null && rep instanceof Exception) {
                                            errores = errores + ((Exception)rep).getMessage();
                                        } else {
                                            trx.addField(campo.getNombre(), rep);
                                        }
                                    } catch (Exception xc) {
                                    errores = errores + "campo: '" + campo.getDescripcionCampo() + "' error en su valor;/n";
                                    }
                                }                            
                                if (errores != null && errores.length() > 0) throw new LecturaException(errores);
                                
                                //Agrega Transaccion al detalle
                                resp.addTransaccion(trx);
                                resp.setNumeroTransaccionesProcesadas(resp.getNumeroTransaccionesProcesadas()+1);
                            } catch (Exception er) {
                            
                                //Agrega Errores al detalle
                                resp.addError(row, er.getMessage());
                                resp.setNumeroTransaccionesError(resp.getNumeroTransaccionesError()+1);
                            }
                        }
                    }                    
                    row++;                    
                }   
                if (inputBufReader != null) inputBufReader.close();
                resp.setOk(true);
            }
        } catch (Exception e) {
            resp.setError(e);
            resp.setOk(false);
        }
        return resp;
    }
    
    /**
     * Procesa transacciones archivo XML
     * @param fileDat
     * @return
     */
    private static ResultadoProcesoVO doProcesarXml(String fileDat, String medioPago) {
        ResultadoProcesoVO resp = new ResultadoProcesoVO();
       try {
            FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(medioPago);
            if (filePrc != null) {
                resp.setNombreProceso(filePrc.getDescripcion());    
                long row = 1;
                File file = new File(fileDat);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();
                NodeList nodeLst = doc.getElementsByTagName(filePrc.getTagRow());
           
                //Inicia lectura de registros 
                for (int s = 0; s < nodeLst.getLength(); s++) {
                    if (filePrc.isUsoUltimoRegistro()==false) {
                        if (row == nodeLst.getLength()) break;
                    }                
                   Node fstNode = nodeLst.item(s);
                   //Recupera los campos con sus atributos
                   if (fstNode.getNodeType() == Node.ELEMENT_NODE) {      
                                      
                       String linea = xmlToString(fstNode);                                            
                       filePrc.setDataRow(linea); 
                       
                       LectorDinamicoVO trx = new LectorDinamicoVO();
                       //Recupera campos obligatorios
                       if (linea !=null  && linea.trim().length() > 0) { 
                           try {
                           
                               String errores = "";
                               Enumeration camp = filePrc.getCampos().elements();
                               while (camp.hasMoreElements()){
                                   FieldStructureVo campo = (FieldStructureVo) camp.nextElement();
                                   Object rep = null;
                                   try {
                                       if (campo.getTipo().equalsIgnoreCase("java.lang.String")) rep = filePrc.getValueRow(campo.getNombre());
                                       if (campo.getTipo().equalsIgnoreCase("java.lang.Long")) rep = filePrc.getValueRow(campo.getNombre());
                                       if (campo.getTipo().equalsIgnoreCase("java.lang.Double")) rep = filePrc.getValueRow(campo.getNombre());
                                       if (campo.getTipo().equalsIgnoreCase("java.lang.Boolean")) rep = filePrc.getValueRow(campo.getNombre());
                                       if (campo.getTipo().equalsIgnoreCase("java.lang.Float")) rep = filePrc.getValueRow(campo.getNombre());
                                       if (campo.getTipo().equalsIgnoreCase("java.lang.Integer")) rep = filePrc.getValueRow(campo.getNombre());
                                       if (campo.getTipo().equalsIgnoreCase("java.util.Date")) rep = filePrc.getValueRow(campo.getNombre());                                                
                                       if (rep == null) {
                                           errores = errores + "campo: '" + campo.getDescripcionCampo() + "' error en su valor;/n";
                                       } else if (rep != null && rep instanceof Exception) {
                                           errores = errores + ((Exception)rep).getMessage();
                                       } else {
                                           trx.addField(campo.getNombre(), rep);
                                       }
                                   } catch (Exception xc) {
                                   errores = errores + "campo: '" + campo.getDescripcionCampo() + "' error en su valor;/n";
                                   }
                               }                            
                               if (errores != null && errores.length() > 0) throw new LecturaException(errores);
    
                               //Agrega a transacciones
                               resp.addTransaccion(trx);
                               resp.setNumeroTransaccionesProcesadas(resp.getNumeroTransaccionesProcesadas()+1);   
                           } catch (Exception er) {
                               //Agrega Errores al detalle
                               resp.addError(row, er.getMessage());
                               resp.setNumeroTransaccionesError(resp.getNumeroTransaccionesError()+1);
                           }
                       }
                   }                         
                   row++;   
                } 
                resp.setOk(true);
            }
        } catch (Exception e) {
            resp.setError(e);
            resp.setOk(false);
        }
        return resp;
    }    
    
    
    
    /**
     * Procesa transacciones archivo delimitado
     * @param fileDat
     * @return
     */
    private static ResultadoProcesoVO doProcesarDelimitado(String fileDat, String medioPago) {
        ResultadoProcesoVO resp = new ResultadoProcesoVO();
                
        try {
            FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(medioPago);            
            if (filePrc != null) {
                resp.setNombreProceso(filePrc.getDescripcion());    
                long row = 1;        
                long registros = getLineCountFile(fileDat);
                BufferedReader inputBufReader = new BufferedReader(new FileReader(fileDat));
                String linea;
                while ((linea = inputBufReader.readLine()) != null) {
                    linea+=filePrc.getDelimitador();
                    LectorDinamicoVO trx = new LectorDinamicoVO();
                    if (row >= filePrc.getFilaInicio()) {
                        if (filePrc.isUsoUltimoRegistro()==false) {
                            if (row == registros) break;
                        }
                        if (linea !=null  && linea.trim().length() > 0) { 
                            try {
                                //Procesa linea
                                filePrc.setDataRow(linea);
                                String errores = "";
                                Enumeration camp = filePrc.getCampos().elements();
                                while (camp.hasMoreElements()){
                                    FieldStructureVo campo = (FieldStructureVo) camp.nextElement();
                                    Object rep = null;
                                    try {
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.String")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Long")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Double")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Boolean")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Float")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.lang.Integer")) rep = filePrc.getValueRow(campo.getNombre());
                                        if (campo.getTipo().equalsIgnoreCase("java.util.Date")) rep = filePrc.getValueRow(campo.getNombre());                                                
                                        if (rep == null) {
                                            errores = errores + "campo: '" + campo.getDescripcionCampo() + "' error en su valor;/n";
                                        } else if (rep != null && rep instanceof Exception) {
                                            errores = errores + ((Exception)rep).getMessage();
                                        } else {
                                            trx.addField(campo.getNombre(), rep);
                                        }
                                    } catch (Exception xc) {
                                        errores = errores + "campo: '" + campo.getDescripcionCampo() + "' error en su valor;/n";
                                    }
                                }                            
                                if (errores != null && errores.length() > 0) throw new LecturaException(errores);
                                                            
                                //Agrega a transacciones
                                resp.addTransaccion(trx);
                                resp.setNumeroTransaccionesProcesadas(resp.getNumeroTransaccionesProcesadas()+1);
                                
                            } catch (LecturaException er) {
                                //Agrega Errores al detalle
                                resp.addError(row, er.getMessage());
                                resp.setNumeroTransaccionesError(resp.getNumeroTransaccionesError()+1);
                            } catch (Exception erx) {
                                //Agrega Errores al detalle
                                resp.addError(row, "Error en proceso de lectura de registro : " + erx.getStackTrace());
                                resp.setNumeroTransaccionesError(resp.getNumeroTransaccionesError()+1);                            
                            } finally {
                                //Procesguir
                            }
                        }
                    }                    
                    row++;                    
                } 
                if (inputBufReader != null) inputBufReader.close();
                resp.setOk(true);
            }
        } catch (Exception e) {
            resp.setError(e);
            resp.setOk(false);
        }
        return resp;
    }
        
    /**
     * Convierte un Nodo en XML
     * de tipo String para procesamientos
     * @param node
     * @return
     */
    public static String xmlToString(Node node) {
           try {
               Source source = new DOMSource(node);
               StringWriter stringWriter = new StringWriter();
               Result result = new StreamResult(stringWriter);
               TransformerFactory factory = TransformerFactory.newInstance();
               Transformer transformer = factory.newTransformer();
               transformer.transform(source, result);
               return stringWriter.getBuffer().toString();
           } catch (TransformerConfigurationException e) {
               e.printStackTrace();
           } catch (TransformerException e) {
               e.printStackTrace();
           }
           return null;
       }    
    
    /**
     * Recupera el valor de una celda segun un formato de
     * lectura en XLS
     * @param cell
     * @return
     */
    private static String getCellValor(HSSFCell cell) {
        String valor = null;
        if (cell != null) {
          switch (cell.getCellType ())       {
                  case HSSFCell.CELL_TYPE_NUMERIC :               
                       if(HSSFDateUtil.isCellDateFormatted(cell)) {
                           java.util.Date fechax = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                           valor = FechaUtil.getFechaFormateoCustom(fechax, "dd-MM-yyyy HH:mm:ss");
                       } else {
                           valor = Double.toString(cell.getNumericCellValue());
                       }
                       break;
                  case HSSFCell.CELL_TYPE_STRING : 
                          HSSFRichTextString richTextString = cell.getRichStringCellValue ();                       
                          valor =  richTextString.getString();                       
                          break;
                  default : 
                          valor = "";
                          break;
           }
        }
        return valor;
    }

    /**
     * Cuenta el numero de lineas
     * del un archivo 
     * @param filePath
     * @return
     * @throws IOException
     */
    private static int getLineCountFile(String filePath) throws IOException {
        BufferedReader inputStream = new BufferedReader(new FileReader(filePath));
        int lineCount = 0;
        String line;
        while ((line = inputStream.readLine()) != null) {
            lineCount++;
        }
        if (inputStream != null) inputStream.close();
        return lineCount;
    }
}

