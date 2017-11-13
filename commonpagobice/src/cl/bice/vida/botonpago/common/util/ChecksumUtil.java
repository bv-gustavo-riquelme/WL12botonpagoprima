package cl.bice.vida.botonpago.common.util;

import java.io.FileInputStream;
import java.io.InputStream;

import java.security.MessageDigest;

import java.util.Calendar;
import java.util.Date;


public class ChecksumUtil {

    /**
     * Envia un archivo a un arreglo de bytes
     * codificados en algoritmo MD5
     * @param filename
     * @return
     * @throws Exception
     */
    public static byte[] createChecksumFile(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }


    /**
     * Envia un texto a un arreglo de bytes
     * codificados en algoritmo MD5
     * @param filename
     * @return
     * @throws Exception
     */
    public static byte[] createChecksumText(String texto) throws Exception {
        byte[] buffer = texto.getBytes();
        MessageDigest complete = MessageDigest.getInstance("MD5");
        complete.update(buffer, 0, buffer.length);
        return complete.digest();
    }

    /**
     * Checksum para archivos, que convierte el
     * arreglo de bytes en hexadecimal
     * @param filename
     * @return
     * @throws Exception
     */
    public static String getMD5ChecksumFile(String filename) throws Exception {
        byte[] b = createChecksumFile(filename);
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * Checksum para Texto, que convierte el
     * arreglo de bytes en hexadecimal
     * @param text
     * @return
     * @throws Exception
     */
    public static String getMD5ChecksumText(String text) throws Exception {
        byte[] b = createChecksumText(text);
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
    
    public static String rellenarTextoIzquierda(String texto, int largo, String relleno) {        
        if (texto.length() < largo)  {
            int rell = largo-texto.length();
            for (int p = 0; p < rell; p++) {
                texto = relleno + texto;
            }
        }
        return texto;
    }

    /**
     * Tester main para probar el uso correcto
     * del checksum en MD5
     * @param args
     */
    public static void main(String[] args) {
        try {
            String idOrdenCompra = "000000000000001001020322";            
            String idComercio     = idOrdenCompra.substring(0,15); //15 Primeros caracteres son del comercio
            String idNumProdcutos = idOrdenCompra.substring(15,18); //15 Primeros caracteres son del comercio
            String idTrxBice      = idOrdenCompra.substring(18); //Numero de transaccion
            
            System.out.println("idComercio:" + idComercio);
            System.out.println("idNumProdcutos:" + idNumProdcutos);
            System.out.println("idTrxBice:" + idTrxBice);
            
            String texto = rellenarTextoIzquierda("9556011011100000", 40, "0");
            System.out.println("Texto :" + texto);
            System.out.println("Largo :" + texto.length());
            System.out.println("CheckSum :" + getMD5ChecksumText(texto));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
