package cl.bice.vida.botonpago.common.util;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileWriter;

import java.io.IOException;

import java.io.InputStreamReader;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

public class CheckMacWebPayUtil {
    /**
    * Lo4j en esta clase
    */     
    private static final Logger logger = Logger.getLogger(CheckMacWebPayUtil.class);
    
    /**
     * Chequeador de MAC para WebPay
     * Utilizando archivo de comando de validacion
     * que debe estar instalado y con permisos de
     * ejecucion en el servidor donde recide la CGI.
     * @param parametros
     * @return
     */
    public static boolean checkMac(Map parametros, boolean debugParams, String fileDirectoryOut, String fileValidateMac) {        
        logger.info("checkMac() - iniciando");
        boolean respuesta = Boolean.FALSE;
        logger.info("checkMac() - revidando parametros");
        if (debugParams) {
            Set set = parametros.entrySet();        
            Iterator iter = set.iterator();        
            while (iter.hasNext()) {
                Entry n = (Entry) iter.next();
                String clave = n.getKey().toString();
                String[] valor = (String[])n.getValue();
                logger.info("checkMac() - parametro " + clave + " = " + valor[0]);
            }        
        }
        
        //Montando Archivos y Directorios
        File out = new File(fileDirectoryOut.trim());
        File validate = new File(fileValidateMac.trim());
        String[] idTransaccion = (String[]) parametros.get("TBK_ID_TRANSACCION");
        
        logger.info("checkMac() - verificando parametro de identificacion transaccion");
        if (idTransaccion != null && idTransaccion[0] != null) {            
            logger.info("checkMac() - verificando existencia de directorio y archivo de validacion MAC");
            if (validate.exists()) {
                logger.info("checkMac() - excribiendo archivo de trasaccion :" + idTransaccion[0]+".txt");
                File outValue = new File(out, idTransaccion[0]+".txt");
                FileWriter wrt;
                try {              
                    logger.info("checkMac() - excribiendo valores");
                    wrt = new FileWriter(outValue, false);
                    if (parametros.get("TBK_ORDEN_COMPRA")!=null) wrt.write("TBK_ORDEN_COMPRA" + "=" + ((String[]) parametros.get("TBK_ORDEN_COMPRA"))[0] + "&");
                    if (parametros.get("TBK_TIPO_TRANSACCION")!=null) wrt.write("TBK_TIPO_TRANSACCION" + "=" + ((String[]) parametros.get("TBK_TIPO_TRANSACCION"))[0] + "&");
                    if (parametros.get("TBK_RESPUESTA")!=null) wrt.write("TBK_RESPUESTA" + "=" + ((String[]) parametros.get("TBK_RESPUESTA"))[0] + "&");
                    if (parametros.get("TBK_MONTO")!=null) wrt.write("TBK_MONTO" + "=" + ((String[]) parametros.get("TBK_MONTO"))[0] + "&");
                    if (parametros.get("TBK_CODIGO_AUTORIZACION")!=null) wrt.write("TBK_CODIGO_AUTORIZACION" + "=" + ((String[]) parametros.get("TBK_CODIGO_AUTORIZACION"))[0] + "&");
                    if (parametros.get("TBK_FINAL_NUMERO_TARJETA")!=null) wrt.write("TBK_FINAL_NUMERO_TARJETA" + "=" + ((String[]) parametros.get("TBK_FINAL_NUMERO_TARJETA"))[0] + "&");
                    if (parametros.get("TBK_FECHA_CONTABLE")!=null) wrt.write("TBK_FECHA_CONTABLE" + "=" + ((String[]) parametros.get("TBK_FECHA_CONTABLE"))[0] + "&");
                    if (parametros.get("TBK_FECHA_TRANSACCION")!=null) wrt.write("TBK_FECHA_TRANSACCION" + "=" + ((String[]) parametros.get("TBK_FECHA_TRANSACCION"))[0] + "&");
                    if (parametros.get("TBK_HORA_TRANSACCION")!=null) wrt.write("TBK_HORA_TRANSACCION" + "=" + ((String[]) parametros.get("TBK_HORA_TRANSACCION"))[0] + "&");
                    if (parametros.get("TBK_ID_SESION")!=null) wrt.write("TBK_ID_SESION" + "=" + ((String[]) parametros.get("TBK_ID_SESION"))[0] + "&");
                    if (parametros.get("TBK_ID_TRANSACCION")!=null) wrt.write("TBK_ID_TRANSACCION" + "=" + ((String[]) parametros.get("TBK_ID_TRANSACCION"))[0] + "&");
                    if (parametros.get("TBK_TIPO_PAGO")!=null) wrt.write("TBK_TIPO_PAGO" + "=" + ((String[]) parametros.get("TBK_TIPO_PAGO"))[0] + "&");
                    //if (parametros.get("TBK_NUMERO_CUOTAS")!=null) wrt.write("TBK_NUMERO_CUOTAS" + "=" + ((String[]) parametros.get("TBK_NUMERO_CUOTAS"))[0] + "&");
                    if (parametros.get("TBK_TIPO_PAGO")!=null && parametros.get("TBK_TIPO_PAGO") == "VD"){
                        if (parametros.get("TBK_NUMERO_CUOTAS")!=null) wrt.write("TBK_NUMERO_CUOTAS" + "=" + "00" + "&");
                    }else{
                        if (parametros.get("TBK_TIPO_PAGO")!=null && parametros.get("TBK_TIPO_PAGO") == "VN"){
                            if (parametros.get("TBK_NUMERO_CUOTAS")!=null) wrt.write("TBK_NUMERO_CUOTAS" + "=" + "00" + "&");
                        }else{
                            if (parametros.get("TBK_NUMERO_CUOTAS")!=null) wrt.write("TBK_NUMERO_CUOTAS" + "=" + ((String[]) parametros.get("TBK_NUMERO_CUOTAS"))[0] + "&");
                        }                        
                    }                    
                    if (parametros.get("TBK_TASA_INTERES_MAX")!=null) wrt.write("TBK_TASA_INTERES_MAX" + "=" + ((String[]) parametros.get("TBK_TASA_INTERES_MAX"))[0] + "&");
                    if (parametros.get("TBK_VCI")!=null) wrt.write("TBK_VCI" + "=" + ((String[]) parametros.get("TBK_VCI"))[0] + "&");
                    if (parametros.get("TBK_MAC")!=null) wrt.write("TBK_MAC" + "=" + ((String[]) parametros.get("TBK_MAC"))[0] + "");
                    wrt.close();
                    
                    //Validando
                    logger.info("checkMac() - validando el archivo con la MAC");                
                    Runtime rt = Runtime.getRuntime();
                    logger.info("checkMac() - ejecutando comando :" + validate.getAbsolutePath()  + " " + outValue.getAbsolutePath());                
                    Process pr = rt.exec(validate.getAbsolutePath()  + " " + outValue.getAbsolutePath());        
                    BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));        
                    String line=null;   
                    String resultado = null;
                    while((line=input.readLine()) != null) {
                        resultado = line;
                    }        
                    logger.info("checkMac() - resultado de validacion :" + resultado);                
                    if (!resultado.equalsIgnoreCase("INVALIDO")) respuesta = true;
                    
                } catch (Exception e) {
                     logger.error("checkMac() - error al crear archivo de validacion MAC y validacion  :" + e.getMessage(), e);
                }
            } else {
                logger.error("checkMac() - la cgi de validacion no existe en la ruta solicitada :" + fileValidateMac);
            }
        }
        
        logger.info("checkMac() - terminado");
        return respuesta;
    }
    
    /**
     * Main
     * @param args
     */
    public static void main(String[] args) {
        CheckMacWebPayUtil test = new CheckMacWebPayUtil();
    }
}
