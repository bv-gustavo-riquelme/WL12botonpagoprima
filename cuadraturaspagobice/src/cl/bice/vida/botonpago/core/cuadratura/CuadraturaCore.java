
package cl.bice.vida.botonpago.core.cuadratura;

import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.core.config.ReaderConfigCore;
import cl.bice.vida.botonpago.core.vo.EmpresaVO;
import cl.bice.vida.botonpago.core.vo.FieldStructureVo;
import cl.bice.vida.botonpago.core.vo.FileStructureVo;
import cl.bice.vida.botonpago.core.vo.MedioPagoVO;
import cl.bice.vida.botonpago.core.vo.ResultadoProcesoVO;
import cl.bice.vida.botonpago.core.vo.TransaccionRendicionVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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


public class CuadraturaCore {
    
    private static ReaderConfigCore xmlconfig = null;
    
    /**
     * Carga la configuracion
     * @param fileConfig
     * @return
     */
    public static void loadConfig(String fileConfig) {
        if (xmlconfig == null) xmlconfig = new ReaderConfigCore();        
        xmlconfig.loadConfig(fileConfig);        
    }    

    /**
     * Entrega la lista de empresas
     * @return
     */
    public static List getEmpresas() {
        Collection col = xmlconfig.empresas.values();  
        List emps = new LinkedList(col);
        return emps;
    }    

    /**
     * Indica si la relacion entre el medio y empresa
     * esta habilitados para poder cuadrar informacion
     * @empresa Id de empresa que cargaran la informacion
     * @return Lista de medios disponibles para enviar cuadratura
     */
    public static boolean isMedioEmpresa(String empresa, String medio) {
        MedioPagoVO existe = null;
        EmpresaVO emp = (EmpresaVO) xmlconfig.empresas.get(empresa);
        if (emp!=null) {
            //Busca si el medio esta autorizado para la empresa seleccionada
            existe = (MedioPagoVO) emp.getMedios().get(medio);            
        } 
        return existe!=null?Boolean.TRUE:Boolean.FALSE;
    }    

    /**
     * Recupera todos los medios de pagos permitidos
     * para hacer upload
     * @return
     */
    public static List getAllMedios() {
        List mediosUp = new ArrayList();
        Iterator medios = xmlconfig.config.values().iterator();  
        while (medios.hasNext()) {            
            FileStructureVo filePrc = (FileStructureVo) medios.next();
            if (filePrc.isUpload()) {
                MedioPagoVO med = new MedioPagoVO();
                med.setArchivoId(filePrc.getNombre());
                med.setDescripcion(filePrc.getDescripcion());
                mediosUp.add(med);
            }            
        }
        return mediosUp;
    
    }    
    
    /**
     * Entrega el numero de la empresa
     * @param codeEmp
     * @return numero en base de datos de la empresa
     */
    public static int getEmpresaCode(String codeEmp) {
        EmpresaVO emp = (EmpresaVO) xmlconfig.empresas.get(codeEmp);  
        return emp.getIdDataBase().intValue();
    }
    
    /**
     * Retorna el codigo del medio de pago en
     * la base de datos y esquema de pago bice
     * @param codeMedio
     * @return numero del medio de pago en la base de datos
     */
    public static int getMedioPagoCode(String codeMedio) {
        FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(codeMedio);  
        return filePrc.getDatabasecode().intValue();
    }  
    
    /**
     * Retorna el codigo del medio de pago en
     * la base de datos y esquema de pago bice
     * @param codeMedio
     * @return numero del medio de pago en la base de datos
     */
    public static String getDescripcionMedioPago(String codeMedio) {
        FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(codeMedio);  
        return filePrc.getDescripcion();
    }    
    
    /**
     * Localiza el numero de comercio electroniuco
     * establecido para la relacion empresa - medio de pago
     * @param codeEmpresa
     * @param codeMedio
     * @return
     */
    public static Long getIdComercioEmpresaMedio(String codeEmpresa, String codeMedio) {
        EmpresaVO emp = (EmpresaVO) xmlconfig.empresas.get(codeEmpresa); 
        MedioPagoVO med = (MedioPagoVO) emp.getMedios().get(codeMedio);
        return new Long(med.getIdComercio());
    }    
    
    /**
     * String 
     * @param filenamepath
     * @param medioPago
     * @return
     */
    public static ResultadoProcesoVO doCuadrar(String filenamepath, String medioPago, String empresa) {
        
        //Investiga el tipo de structura del archivo a procesar
        ResultadoProcesoVO res = null;
        FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(medioPago);  

        //Inicia el procesamiento segun el tipo de archivo        
        if ( filePrc.getTipoArchivo().equalsIgnoreCase("LargoFijo") ) res = doProcesarLargoFijo(filenamepath, medioPago);
        if ( filePrc.getTipoArchivo().equalsIgnoreCase("Delimitado") ) res = doProcesarDelimitado(filenamepath, medioPago);
        if ( filePrc.getTipoArchivo().equalsIgnoreCase("XML") ) res = doProcesarXml(filenamepath, medioPago);
        if ( filePrc.getTipoArchivo().equalsIgnoreCase("XLS") ) res = doProcesarXLS(filenamepath, medioPago);
        if (res != null) {
            res.setEstadoValueOk(filePrc.getEstadoOkValue());
        }
        return res;
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
                resp.setMedioPago(filePrc.getDescripcion()); 
                long row = 1;
                
                File file = new File(fileDat);
                InputStream xlsinput = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(xlsinput);
                HSSFSheet sheet = workbook.getSheetAt(0);
                                    
                int registros = sheet.getLastRowNum() ;
                Iterator rows = sheet.rowIterator();
                while(rows.hasNext()){     
                    TransaccionRendicionVO trx = new TransaccionRendicionVO();
                    String linea = "";
                    HSSFRow lineax = (HSSFRow) rows.next();
                    if (row >= filePrc.getFilaInicio()) {
                        if (filePrc.isUsoUltimoRegistro()==false) {
                            if (lineax.getRowNum() == registros) break;
                        }
                        
                        if (lineax != null) {
                            boolean hayvalor = false;
                            for (int cl = 0; cl < lineax.getLastCellNum(); cl++) {
                                HSSFCell celda = lineax.getCell((short)cl);
                                String valor = getCellValor(celda);
                                linea = linea + valor + "|";
                                if (valor != null && valor.length() > 1) hayvalor = true;
                            }
                            if (hayvalor == false) break;
                        }
                    
                        filePrc.setDataRow(linea);
                        
                        
                        //Recupera campos obligatorios
                        if (linea !=null  && linea.trim().length() > 0) { 
                         
                            //Recupera campos obligatorios
                            Double monto = (Double) filePrc.getValueRow("montoPagado");
                            String idOrdenCompra = (String) filePrc.getValueRow("idOrdenCompra");
                            java.util.Date fechaPago = (java.util.Date) filePrc.getValueRow("fechaPago");
                            if (monto == null) throw new Exception("Revise estructura de archivo, imposible obtener el monto del pago!");
                            if (idOrdenCompra == null) throw new Exception("Revise estructura de archivo, imposible obtener el orden de compra!");
                            
                            //Recupera campos opcionales
                            String idTransaccionMedio = (String) filePrc.getValueRow("idTransaccionMedio");
                            String numeroTarjeta = (String) filePrc.getValueRow("numeroTarjeta");
                            String tipoTarjeta = (String) filePrc.getValueRow("tipoTarjeta");
                            String idCodBancoPortal = (String) filePrc.getValueRow("IdCodBancoPortal");
                            String estadoPago = (String) filePrc.getValueRow("estado");
                            
                            
                            //Setea datos de transaccion          
                            trx.setMontoInformado(monto);
                            trx.setMontoPago(monto);
                            trx.setEstado(estadoPago);
                            trx.setFechaOperacion(fechaPago);
                            trx.setNumeroOrdenBice(idOrdenCompra);
                            trx.setNumeroTransaccionMedio(idTransaccionMedio);
                            trx.setNumeroTarjeta(numeroTarjeta);
                            trx.setTipoTarjeta(tipoTarjeta);
                            trx.setCodBancoPortal(idCodBancoPortal);
                            
                            //Agrega Transaccion al detalle y salva el ultimo objeto cargado
                            TransaccionRendicionVO find = resp.getTransaccionById(trx.getNumeroOrdenBice());
                            if (find != null) {
                                    //Quita la transaccion anterior para ser reemplazara por una con monto actualizado
                                    resp.removeTransaccion(find);
                                    
                                    //Suma el monto
                                    trx.setMontoPago(find.getMontoPago() + trx.getMontoPago());
                                    trx.setMontoInformado(find.getMontoInformado() + trx.getMontoInformado());
                                    
                                    //Nueva transaccion pero con el monto sumarizado
                                    resp.addTransaccion(trx);    
                            } else {
                                resp.addTransaccion(trx);
                            }                        
                      
                        } 
                    }
                    row++;
                                        
                } 
                xlsinput.close();
                
                if (resp.getCuantasTransacciones() >= 1) { 
                    resp.setOk(true);
                } else {
                    resp.setError(new Exception("No se han encontrado transacciones para ser procesadas."));
                    resp.setOk(false);
                }
            }
            
           
        } catch (Exception e) {
            resp.setError(new Exception("Revise estructura de archivo:"+e.getMessage()));
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
                resp.setMedioPago(filePrc.getDescripcion());    
                int row = 1;
                int registros = getLineCountFile(fileDat);
                Scanner filescan = new Scanner(new File(fileDat));
                while(filescan.hasNext()){
                    TransaccionRendicionVO trx = new TransaccionRendicionVO();
                    String linea = filescan.nextLine();                        
                    if (row >= filePrc.getFilaInicio() && linea.trim().length() > 5) {
                        if (filePrc.isUsoUltimoRegistro()==false) {
                            if (row == registros) break;
                        }
                        filePrc.setDataRow(linea); 
                        
                        //Recupera campos obligatorios
                        Double monto = (Double) filePrc.getValueRow("montoPagado");
                        if (monto.doubleValue() > 0) {
                            String idOrdenCompra = (String) filePrc.getValueRow("idOrdenCompra");
                            java.util.Date fechaPago = (java.util.Date) filePrc.getValueRow("fechaPago");
                            if (monto == null) throw new Exception("Revise estructura de archivo, imposible obtener el monto del pago!");
                            if (idOrdenCompra == null) throw new Exception("Revise estructura de archivo, imposible obtener el orden de compra!");
                            
                            //Recupera campos opcionales
                            String idTransaccionMedio = (String) filePrc.getValueRow("idTransaccionMedio");
                            String numeroTarjeta = (String) filePrc.getValueRow("numeroTarjeta");
                            String tipoTarjeta = (String) filePrc.getValueRow("tipoTarjeta");
                            String idCodBancoPortal = (String) filePrc.getValueRow("IdCodBancoPortal");
                            String estadoPago = (String) filePrc.getValueRow("estado");
                            
                            //Setea datos de transaccion          
                            trx.setMontoInformado(monto);
                            trx.setMontoPago(monto);
                            trx.setEstado(estadoPago);
                            trx.setFechaOperacion(fechaPago);
                            trx.setNumeroOrdenBice(idOrdenCompra);
                            trx.setNumeroTransaccionMedio(idTransaccionMedio);
                            trx.setNumeroTarjeta(numeroTarjeta);
                            trx.setTipoTarjeta(tipoTarjeta);
                            trx.setCodBancoPortal(idCodBancoPortal);
                            
                            //Agrega Transaccion al detalle y salva el ultimo objeto cargado
                            TransaccionRendicionVO find = resp.getTransaccionById(trx.getNumeroOrdenBice());
                            if (find != null) {
                                    //Quita la transaccion anterior para ser reemplazara por una con monto actualizado
                                    resp.removeTransaccion(find);
                                    
                                    //Suma el monto
                                    trx.setMontoPago(find.getMontoPago() + trx.getMontoPago());
                                    trx.setMontoInformado(find.getMontoInformado() + trx.getMontoInformado());
                                    
                                    //Nueva transaccion pero con el monto sumarizado
                                    resp.addTransaccion(trx);    
                            } else {
                                resp.addTransaccion(trx);
                            }       
                        }
                    }             
                    row++;                    
                }   
                filescan.close();
                if (resp.getCuantasTransacciones() >= 1) {  
                    resp.setOk(true);
                } else {
                    resp.setError(new Exception("No se han encontrado transacciones para ser procesadas."));
                    resp.setOk(false);
                }
            }
        } catch (Exception e) {
            resp.setError(new Exception("Revise estructura de archivo, imposible procesar su contenido."));
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
                resp.setMedioPago(filePrc.getDescripcion());    
                int row = 1;
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
                       
                       TransaccionRendicionVO trx = new TransaccionRendicionVO();
                       //Recupera campos obligatorios
                       Double monto = (Double) filePrc.getValueRow("montoPagado");
                       String idOrdenCompra = (String) filePrc.getValueRow("idOrdenCompra");
                       java.util.Date fechaPago = (java.util.Date) filePrc.getValueRow("fechaPago");
                       if (monto == null) throw new Exception("Revise estructura de archivo, imposible obtener el monto del pago!");
                       if (idOrdenCompra == null) throw new Exception("Revise estructura de archivo, imposible obtener el orden de compra!");
                       if (fechaPago == null) throw new Exception("Revise estructura de archivo, imposible obtener fecha de pago!");
                       
                       
                       //Recupera campos opcionales
                       String idTransaccionMedio = (String) filePrc.getValueRow("idTransaccionMedio");
                       String numeroTarjeta = (String) filePrc.getValueRow("numeroTarjeta");
                       String tipoTarjeta = (String) filePrc.getValueRow("tipoTarjeta");
                       String estadoPago = (String) filePrc.getValueRow("estado");
                       
                       //Setea datos de transaccion
                       trx.setMontoInformado(monto);
                       trx.setMontoPago(monto);
                       trx.setEstado(estadoPago);
                       trx.setFechaOperacion(fechaPago);
                       trx.setNumeroOrdenBice(idOrdenCompra);
                       trx.setNumeroTransaccionMedio(idTransaccionMedio);
                       trx.setNumeroTarjeta(numeroTarjeta);
                       trx.setTipoTarjeta(tipoTarjeta);
                       
                       //Agrega Transaccion al detalle y salva el ultimo objeto cargado
                       TransaccionRendicionVO find = resp.getTransaccionById(trx.getNumeroOrdenBice());
                       if (find != null) {
                               //Quita la transaccion anterior para ser reemplazara por una con monto actualizado
                               resp.removeTransaccion(find);
                               
                               //Suma el monto
                               trx.setMontoPago(find.getMontoPago() + trx.getMontoPago());
                               
                               //Nueva transaccion pero con el monto sumarizado
                               resp.addTransaccion(trx);    
                       } else {
                           resp.addTransaccion(trx);
                       }                       
                       row++;   
                   }                         
                } 
                if (resp.getCuantasTransacciones() >= 1) { 
                    resp.setOk(true);
                } else {
                    resp.setError(new Exception("No se han encontrado transacciones para ser procesadas."));
                    resp.setOk(false);
                }
            }
        } catch (Exception e) {
            resp.setError(new Exception("Revise estructura de archivo, imposible procesar su contenido."));
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
                resp.setMedioPago(filePrc.getDescripcion());    
                int row = 1;        
                int registros = getLineCountFile(fileDat);
                Scanner filescan = new Scanner(new File(fileDat));
                while(filescan.hasNext()){
                    TransaccionRendicionVO trx = new TransaccionRendicionVO();
                    String linea = filescan.nextLine();                        
                    if (row >= filePrc.getFilaInicio() && linea.trim().length() > 5) {
                        if (filePrc.isUsoUltimoRegistro()==false) {
                            if (row == registros) break;
                        }                    
                        filePrc.setDataRow(linea); 
                        //Recupera campos obligatorios
                        Double monto = (Double) filePrc.getValueRow("montoPagado");
                        String idOrdenCompra = (String) filePrc.getValueRow("idOrdenCompra");
                        java.util.Date fechaPago = (java.util.Date) filePrc.getValueRow("fechaPago");
                        if (monto == null) throw new Exception("Revise estructura de archivo, imposible obtener el monto del pago!");
                        if (idOrdenCompra == null) throw new Exception("Revise estructura de archivo, imposible obtener el orden de compra!");
                        if (fechaPago == null) throw new Exception("Revise estructura de archivo, imposible obtener fecha de pago!");
                        
                        
                        //Recupera campos opcionales
                        String idTransaccionMedio = (String) filePrc.getValueRow("idTransaccionMedio");
                        String numeroTarjeta = (String) filePrc.getValueRow("numeroTarjeta");
                        String tipoTarjeta = (String) filePrc.getValueRow("tipoTarjeta");
                        String estadoPago = (String) filePrc.getValueRow("estado");
                        
                        //Setea datos de transaccion
                        trx.setMontoInformado(monto);
                        trx.setMontoPago(monto);
                        trx.setEstado(estadoPago);
                        trx.setFechaOperacion(fechaPago);
                        trx.setNumeroOrdenBice(idOrdenCompra);
                        trx.setNumeroTransaccionMedio(idTransaccionMedio);
                        trx.setNumeroTarjeta(numeroTarjeta);
                        trx.setTipoTarjeta(tipoTarjeta);
                        
                        //Agrega Transaccion al detalle y salva el ultimo objeto cargado
                        TransaccionRendicionVO find = resp.getTransaccionById(trx.getNumeroOrdenBice());
                        if (find != null) {
                                //Quita la transaccion anterior para ser reemplazara por una con monto actualizado
                                resp.removeTransaccion(find);
                                
                                //Suma el monto
                                trx.setMontoPago(find.getMontoPago() + trx.getMontoPago());
                                
                                //Nueva transaccion pero con el monto sumarizado
                                resp.addTransaccion(trx);    
                        } else {
                            resp.addTransaccion(trx);
                        }                       
                    }                    
                    row++;                    
                } 
                filescan.close();
                if (resp.getCuantasTransacciones() >= 1) {  
                    resp.setOk(true);
                } else {
                    resp.setError(new Exception("No se han encontrado transacciones para ser procesadas."));
                    resp.setOk(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setError(new Exception("Revise estructura de archivo, imposible procesar su contenido."));
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
     * Cuenta el numero de lineas
     * del un archivo 
     * @param filePath
     * @return
     * @throws IOException
     */
    private static int getLineCountFile(String filePath) throws IOException {
        Scanner filescan = new Scanner(new File(filePath));
        int lineCount = 0;
        while(filescan.hasNext()){
            String linea = filescan.nextLine();    
            lineCount++;
        }    
        filescan.close();
        return lineCount;
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
                           valor = FechaUtil.getFechaFormateoCustom(fechax, "dd/MM/yyyy HH:mm:ss");
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
}
