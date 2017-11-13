package cl.bice.vida.botonpago.core2.escritor.writer;

import cl.bice.vida.botonpago.core2.comparator.IndexCampoComparator;
import cl.bice.vida.botonpago.core2.escritor.config.WriterConfigCore;
import cl.bice.vida.botonpago.core2.vo.EntidadEstructuraVO;
import cl.bice.vida.botonpago.core2.vo.EscritorDinamicoVO;
import cl.bice.vida.botonpago.core2.vo.FieldStructureVo;
import cl.bice.vida.botonpago.core2.vo.FileStructureVo;
import cl.bice.vida.botonpago.core2.vo.ResultadoEscritorVO;
import cl.bice.vida.botonpago.utils.FechaUtil;
import cl.bice.vida.botonpago.utils.NumeroUtil;
import cl.bice.vida.botonpago.utils.StringUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class EscritorArchivoCore {

    private static WriterConfigCore xmlconfig;

    /**
     * Carga la configuracion
     * @param fileConfig
     * @return
     */
    public static void loadConfig(String file) {
        xmlconfig = new WriterConfigCore();
        xmlconfig.loadConfig(file, true);
    }

    /**
     * Carga la configuracion a partir de un xml directamente
     * @param xml
     */
    public static void loadXMLConfig(String xml) {
        xmlconfig = new WriterConfigCore();
        xmlconfig.loadConfig(xml, false);
    }

    /**
     * Entrega la lista de medios de pago
     * que se pueden hacer upload de archivo
     * @return
     */
    public static List getArchivosParaDescarga() {
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
    public static String getProcesoCodigo(String codigoArchivo) {
        FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(codigoArchivo);
        return filePrc.getDatabasecode();
    }


    /**
     * String
     * @param filenameServerPathSave
     * @param medioPago
     * @return
     */
    public static ResultadoEscritorVO doProcesarArchivo(String filenameDownload, List<EscritorDinamicoVO> data, String nombreProceso, String remoteuser, boolean isWindow) {

        //Investiga el tipo de structura del archivo a procesar
        ResultadoEscritorVO resp = null;
        FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(nombreProceso);

        if (data != null && data.size() > 0) {
            if (filePrc != null) {
                //Inicia el procesamiento segun el tipo de archivo
                if (filePrc.getTipoArchivo().equalsIgnoreCase("LargoFijo")) resp = doProcesarLargoFijo(data, filenameDownload, nombreProceso,isWindow);
                if (filePrc.getTipoArchivo().equalsIgnoreCase("Delimitado")) resp = doProcesarDelimitado(data, filenameDownload, nombreProceso,isWindow);
                if (filePrc.getTipoArchivo().equalsIgnoreCase("XLS")) resp = doProcesarXLS(data, filenameDownload, nombreProceso,isWindow);
                //if (filePrc.getTipoArchivo().equalsIgnoreCase("XML")) resp = doProcesarXml(data, filenameDownload, nombreProceso,isWindow);
        
                if (resp != null) {
                    resp.setDescargaInfo(filePrc);
                    resp.setTotalBytesFileDownload(0);
                    resp.setRemoteuser(remoteuser);
                }
            } else {
                resp = new ResultadoEscritorVO();
                resp.setError(new Exception("No existe definicion de descarga para este tipo de archivo. Revisar configuracion de estructur xml-db de archivos descarga"));
            }
        } else {
            resp = new ResultadoEscritorVO();
            resp.setError(new Exception("No se encontraron registros para ser descargados."));
        }
        return resp;
    }


    /**
     * Procesa transacciones archivo largo fijo
     * @param fileDat
     * @return
     */
    private static ResultadoEscritorVO doProcesarLargoFijo(List<EscritorDinamicoVO> data, String filecreate, String codigoProceso, boolean isWindow) {
        ResultadoEscritorVO resp = new ResultadoEscritorVO();

        try {
            FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(codigoProceso);
            if (filePrc != null) {
                resp.setNombreProceso(filePrc.getDescripcion());
                long row = 1;
                long registros = data.size();

                //ABRE ARCHIVO
                File archivo = new File(filecreate);

                //Lista de campos de estructura
                 IndexCampoComparator orden = new IndexCampoComparator();
                 Object[] fields = filePrc.getCampos().values().toArray();
                 List campos = Arrays.asList(fields);
                 Collections.sort(campos, orden);

                //CREA OUTPUT DE LINEA
                FileWriter out = new FileWriter(archivo, true);
                BufferedWriter outBuff = new BufferedWriter(out);
                Iterator dataread = data.iterator();
                while (dataread.hasNext()) {
                    String linea = "";
                    EscritorDinamicoVO trx = (EscritorDinamicoVO) dataread.next();
                    Iterator camp = campos.iterator();
                    try {
                        while (camp.hasNext()){
                            FieldStructureVo campo = (FieldStructureVo) camp.next();
                            if (campo.getHardvalue() != null && campo.getHardvalue().trim().length() > 0) {
                                linea = linea + escribir(campo.getHardvalue(), campo);
                            } else {
                                linea = linea + escribir(trx.getField(campo.getId()), campo);
                            }
                        }
                        if (isWindow) outBuff.write(linea+"\r\n");
                        if (!isWindow) outBuff.write(linea+"\n");
                        resp.setNumeroTransaccionesProcesadas(resp.getNumeroTransaccionesProcesadas()+1);
                    } catch (Exception er) {
                        //Agrega Errores al detalle
                        resp.addError(row, er.getMessage());
                        resp.setNumeroTransaccionesError(resp.getNumeroTransaccionesError()+1);
                    }
                    row++;
                }
                if (outBuff != null) outBuff.close();
                if (out != null) out.close();
                resp.setFilenameDownload(archivo.getName());
                resp.setTotalBytesFileDownload(archivo.length());
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
    private static ResultadoEscritorVO doProcesarDelimitado(List<EscritorDinamicoVO> data, String filecreate, String codigoProceso, boolean isWindow) {
        ResultadoEscritorVO resp = new ResultadoEscritorVO();

        try {
            FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(codigoProceso);
            if (filePrc != null) {
                resp.setNombreProceso(filePrc.getDescripcion());
                long row = 1;
                long registros = data.size();

                //ABRE ARCHIVO
                File archivo = new File(filecreate);

                //Lista de campos de estructura
                 IndexCampoComparator orden = new IndexCampoComparator();
                 Object[] fields = filePrc.getCampos().values().toArray();
                 List campos = Arrays.asList(fields);
                 Collections.sort(campos, orden);

                //CREA OUTPUT DE LINEA
                FileWriter out = new FileWriter(archivo, true);
                BufferedWriter outBuff = new BufferedWriter(out);
                Iterator dataread = data.iterator();
                while (dataread.hasNext()) {
                    String linea = "";
                    EscritorDinamicoVO trx = (EscritorDinamicoVO) dataread.next();
                    Iterator camp = campos.iterator();
                    try {
                        while (camp.hasNext()){
                            FieldStructureVo campo = (FieldStructureVo) camp.next();
                            if (campo.getHardvalue() != null && campo.getHardvalue().trim().length() > 0) {
                                linea = linea + escribir(campo.getHardvalue(), campo) + filePrc.getDelimitador();
                            } else {
                                linea = linea + escribir(trx.getField(campo.getId()), campo) + filePrc.getDelimitador();
                            }
                        }
                        if (isWindow) outBuff.write(linea+"\r\n"); //NUEVA LINEA WINDOWS
                        if (!isWindow) outBuff.write(linea+"\n"); //NUEVA LINEA UNIX
                        resp.setNumeroTransaccionesProcesadas(resp.getNumeroTransaccionesProcesadas()+1);
                    } catch (Exception er) {
                        //Agrega Errores al detalle
                        resp.addError(row, er.getMessage());
                        resp.setNumeroTransaccionesError(resp.getNumeroTransaccionesError()+1);
                    }
                    row++;
                }
                if (outBuff != null) outBuff.close();
                if (out != null) out.close();
                resp.setFilenameDownload(archivo.getName());
                resp.setTotalBytesFileDownload(archivo.length());
                resp.setOk(true);
            }
        } catch (Exception e) {
            resp.setError(e);
            resp.setOk(false);
        }
        return resp;
    }
    
    /**
    * Procesa transacciones archivo XLS
    * @param fileDat
    * @return
    */
   private static ResultadoEscritorVO doProcesarXLS(List<EscritorDinamicoVO> data, String filecreate, String codigoProceso, boolean isWindow) {
       ResultadoEscritorVO resp = new ResultadoEscritorVO();
        try {
           FileStructureVo filePrc = (FileStructureVo) xmlconfig.config.get(codigoProceso);
           if (filePrc != null) {
               resp.setNombreProceso(filePrc.getDescripcion());
               
               int row = 0;
               
               if (filePrc.getFilaInicio() > 0) row = filePrc.getFilaInicio() - 1;
               
               //Lista de campos de estructura
               IndexCampoComparator orden = new IndexCampoComparator();
               Object[] fields = filePrc.getCampos().values().toArray();
               List campos = Arrays.asList(fields);
               Collections.sort(campos, orden);
               
               //CONSTRUYE ARCHIVO EXCEL CON APACHE POI
               HSSFWorkbook workbook = null;
               FileOutputStream fileDown = null;
               HSSFSheet sheet = null;
               File file = new File(filecreate);
               if (file.exists() && file.length() > 0) {
                   //EXISTE Y LO ABRIMOS PARA AÑADIR DATOS
                   FileInputStream inputStream = new FileInputStream(file); 
                   workbook = new HSSFWorkbook(inputStream);
                   sheet = workbook.getSheet("Export");
                   fileDown = new FileOutputStream(file);
               } else{
                   
                   workbook = new HSSFWorkbook();
                   sheet = workbook.createSheet("Export");
                   file.createNewFile();
                   fileDown = new FileOutputStream(file);
               }

               //ESTILO ESTANDAR PARA LAS FECHAS SEGUN SOLICITADO
               HSSFCellStyle cellStyleFecha = workbook.createCellStyle();
               cellStyleFecha.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
               
               //ESTILO ESTANDAR PARA LAS FECHAS SEGUN SOLICITADO
               HSSFFont fuenteBold = workbook.createFont();
               fuenteBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
               HSSFCellStyle cellStyleFontHead = workbook.createCellStyle();
               cellStyleFontHead.setFont(fuenteBold);
               
               //IMPRIME COLUMNAS O NOMBRES DE CABECERA
               if (filePrc.isUsoNombresCampos()) {
                   Iterator campHead = campos.iterator();
                   HSSFRow filaHead = sheet.createRow(row);
                   int colHead = 0;
                   while (campHead.hasNext()){
                       FieldStructureVo campo = (FieldStructureVo) campHead.next();
                       HSSFCell celdaHead = filaHead.createCell(colHead);
                       celdaHead.setCellStyle(cellStyleFontHead);
                       if (campo.getTagNombre() != null && campo.getTagNombre().length() > 0) {
                           celdaHead.setCellValue(new String(campo.getTagNombre()));
                       } else {
                           celdaHead.setCellValue(new String(campo.getId()));
                       }
                       colHead++;
                   } 
                   row++;
               }
               
               Iterator dataread = data.iterator();
               while (dataread.hasNext()) {
                   EscritorDinamicoVO trx = (EscritorDinamicoVO) dataread.next();
                   Iterator camp = campos.iterator();
                   try {
                       HSSFRow fila = sheet.createRow(row);
                       int col = 0;
                       //IMPRIMIENDO 1UNA LINEA
                       while (camp.hasNext()){
                           String valor = null;
                           FieldStructureVo campo = (FieldStructureVo) camp.next();
                           if (campo.getHardvalue() != null && campo.getHardvalue().trim().length() > 0) {
                               valor = escribir(campo.getHardvalue(), campo);
                           } else {
                               valor = escribir(trx.getField(campo.getId()), campo);
                           }
                           
                           //POSICIONA EN LA COLUMNA
                           col = campo.getIndexTab()-1;
                           
                           //EVALUA EL TIPO DE DATO
                           HSSFCell celda = fila.createCell(col);
                           if (campo.getTipo().equalsIgnoreCase("java.lang.String")) {
                              celda.setCellValue(new String(valor));
                           } else if (campo.getTipo().equalsIgnoreCase("java.lang.Double") || campo.getTipo().equalsIgnoreCase("java.lang.Float") || campo.getTipo().equalsIgnoreCase("java.lang.Long") || campo.getTipo().equalsIgnoreCase("java.lang.Integer")) {
                              celda.setCellValue(new Double(valor));
                           } else if (campo.getTipo().equalsIgnoreCase("java.util.Date")) {
                               Calendar calendario = new GregorianCalendar();
                               calendario.setTime(FechaUtil.getFecha(campo.getFormato(),valor));
                               celda.setCellStyle(cellStyleFecha);
                               celda.setCellValue(calendario);
                           } else {
                               celda.setCellValue(valor);
                           }
                           col++;
                       }
                       resp.setNumeroTransaccionesProcesadas(resp.getNumeroTransaccionesProcesadas()+1);
                   } catch (Exception er) {
                       //Agrega Errores al detalle
                       resp.addError(new Long(row), er.getMessage());
                       resp.setNumeroTransaccionesError(resp.getNumeroTransaccionesError()+1);
                   }
                   row++;
               }
              
               //GRABA 
               workbook.write(fileDown); 
               fileDown.flush();
               fileDown.close();
               resp.setFilenameDownload(file.getName());
               resp.setTotalBytesFileDownload(file.length());
               resp.setOk(true);
                
           }
       } catch (Exception e) {
           resp.setError(e);
           resp.setOk(false);
       }
       return resp;
   }    


    /**
     * Escribe el valor segun
     * sus atributos de confeccion
     * @param valor
     * @param campo
     * @return
     */
    private static String escribir(Object valor, FieldStructureVo campo) throws Exception {
        String valorout = "";
        if (campo != null) {
            if (campo.getHardvalue() == null) {
                if (valor != null) valorout = valor.toString();

                if (campo.getTipo().equalsIgnoreCase("java.lang.String")) {
                    try {
                        String val = (String) valor;
                        if (val == null) val = "";
                        valorout = val;
                    } catch (Exception er) {
                        throw new Exception("Campo :" + campo.getNombre() + " su tipo de datos :"+campo.getTipo()+" no es consistente con el valor pasado, revise codificación.");
                    }
                }
                if (campo.getTipo().equalsIgnoreCase("java.lang.Long")) {
                    try {
                        Long val = (Long) valor;
                        if (val == null) val = new Long(0);
                        String valory = NumeroUtil.formatDecimales(new Double(val.toString()), campo.isSepdecimales()==true?campo.getNumdecimales():0);
                        if (campo.isSepdecimales()==false) {
                            valory = StringUtil.replaceString(valory, ".","");
                        } else {
                            valory = StringUtil.replaceString(valory, ".", campo.getSeparadorDecimal());
                        }
                        valorout = valory;
                     } catch (Exception er) {
                        throw new Exception("Campo :" + campo.getNombre() + " su tipo de datos :"+campo.getTipo()+" no es consistente con el valor pasado, revise codificación.");
                    }
                }
                if (campo.getTipo().equalsIgnoreCase("java.lang.Double")) {
                    try {
                        Double val = new Double(valorout);
                        if (val == null) val = new Double(0);
                        String valory = NumeroUtil.formatDecimales(val, campo.isSepdecimales()==true?campo.getNumdecimales():0);
                        if (campo.isSepdecimales()==false) {
                            valory = StringUtil.replaceString(valory, ".","");
                        } else {
                            valory = StringUtil.replaceString(valory, ".", campo.getSeparadorDecimal());
                        }
                        valorout = valory;
                    } catch (Exception er) {
                        throw new Exception("Campo :" + campo.getNombre() + " su tipo de datos :"+campo.getTipo()+" no es consistente con el valor pasado, revise codificación.");
                    }
                }
                if (campo.getTipo().equalsIgnoreCase("java.lang.Boolean")) {
                    try {
                        Boolean val = (Boolean) valor;
                        if (val == null) val = new Boolean(Boolean.FALSE);
                        valorout = val?"1":"0";
                    } catch (Exception er) {
                        throw new Exception("Campo :" + campo.getNombre() + " su tipo de datos :"+campo.getTipo()+" no es consistente con el valor pasado, revise codificación.");
                    }
                }
                if (campo.getTipo().equalsIgnoreCase("java.lang.Float")) {
                    try {
                        Float val = (Float) valor;
                        if (val == null) val = new Float(0);
                        String valory = NumeroUtil.formatDecimales(val.doubleValue(), campo.isSepdecimales()==true?campo.getNumdecimales():0);
                        if (campo.isSepdecimales()==false) {
                            valory = StringUtil.replaceString(valory, ".","");
                        } else {
                            valory = StringUtil.replaceString(valory, ".", campo.getSeparadorDecimal());
                        }
                        valorout = valory;
                    } catch (Exception er) {
                        throw new Exception("Campo :" + campo.getNombre() + " su tipo de datos :"+campo.getTipo()+" no es consistente con el valor pasado, revise codificación.");
                    }

                }
                if (campo.getTipo().equalsIgnoreCase("java.lang.Integer")) {
                   try {
                        Integer val = (Integer) valor;
                        if (val == null) val = new Integer(0);
                        valorout = val.toString();
                    } catch (Exception er) {
                        throw new Exception("Campo :" + campo.getNombre() + " su tipo de datos :"+campo.getTipo()+" no es consistente con el valor pasado, revise codificación.");
                    }
                }
                if (campo.getTipo().equalsIgnoreCase("java.util.Date")) {
                    try {
                        java.util.Date val = (java.util.Date) valor;
                        if (val != null) {
                            String valorx = FechaUtil.getFechaFormateoCustom(val,campo.getFormato());
                            valorout = valorx;
                        } else {
                            for (int pos = 1; pos <= campo.getLargo(); pos++) {
                                valorout = valorout +  "0";
                            }
                        }
                    } catch (Exception er) {
                        throw new Exception("Campo :" + campo.getNombre() + " su tipo de datos :"+campo.getTipo()+" no es consistente con el valor pasado, revise codificación.");
                    }
                }
            } else {
                valorout = campo.getHardvalue();
            }
            //PROCESAR LARGO DE SALIDA
            if (campo.getLargo() > 0) {
                if (valorout.length() > campo.getLargo()) {
                    valorout = valorout.substring(0, campo.getLargo());
                }
            }
            //PROCESAR RELLENO IZQUIERDA
            if (campo.getRellenarIzquierda() != null && campo.getRellenarIzquierda().length() > 0) {
                valorout = valorout.trim();
                if (valorout.length() < campo.getLargo()) {
                    int falta = campo.getLargo() - valorout.length();
                    for (int pos = 1; pos <= falta; pos++) {
                        valorout = campo.getRellenarIzquierda() + valorout;
                    }
                }
            }
            //PROCESAR RELLENO DERECHA
            if (campo.getRellenarDerecha() != null && campo.getRellenarDerecha().length() > 0) {
                valorout = valorout.trim();
                if (valorout.length() < campo.getLargo()) {
                    int falta = campo.getLargo() - valorout.length();
                    for (int pos = 1; pos <= falta; pos++) {
                        valorout = valorout + campo.getRellenarDerecha();
                    }
                }
            }
        }
        return valorout;
    }
}

