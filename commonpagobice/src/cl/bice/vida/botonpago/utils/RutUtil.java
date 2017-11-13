package cl.bice.vida.botonpago.utils;

import java.text.DecimalFormat;

import java.util.StringTokenizer;


/**
 * Clase de utilidad con metodos genericos
 * @author rhicil
 *
 */
public class RutUtil {


    /**
     * Validacion de Rut
     * @param rutCompleto
     * @return
     */
    public static boolean verificarRutCompleto(String rutCompleto) {
        boolean valido = false;
        if (rutCompleto == null) return false;
        int poxinit = rutCompleto.indexOf("-");
        int largo = rutCompleto.length()-2;
        if (poxinit == largo) {
            String primeraParte = rutCompleto.substring(0, poxinit);
            String segundaParte = rutCompleto.substring(poxinit+1);
            
            try {
                Long rut = new Long(primeraParte);
                valido = verificarRut(rut, segundaParte);
            } catch (Exception e) {
                valido = false;   
            }
        }
        return valido;
    }
    public static boolean verificarRut(long rut, String dv) {
    long m = 0, s = 1;
    for (; rut != 0; rut /= 10) {
        s = (s + rut % 10 * (9 - m++ % 6)) % 11;
    }
    char[] digito =dv.toCharArray();
        return digito[0] ==(char) (s != 0 ? s + 47 : 75);
    }
    
    public static char obtenerDv(int rut) {
        int s=1;
        int m=0;
        for (; rut != 0; rut /= 10) {
        s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return (char) (s != 0 ? s + 47 : 75);
    }

    /**
     * @param rut
     * @return
     */
    public static String calculaDv(long rut) {
        long s=1;
        long m=0;
        String dv = String.valueOf((char)75);
        for (; rut != 0; rut /= 10) {
          s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        if (s != 0) {
            dv = String.valueOf(((char)(s + 47))); 
        }
        return dv; 
    }
    
    
    /**
     * Funcion para formatear un rut
     * @param rut
     * @param verificador
     * @return
     */
    public static String formatearRut(Long rut, String verificador) {
        DecimalFormat format = new DecimalFormat("##,###,###");
        String bonito = format.format(rut);
        return bonito+"-"+verificador;
    }
    
    /**
     * Funcion para formatear un rut
     * @param rut
     * @param verificador
     * @return
     */
    public static String formatearRut(Integer rut, String verificador) {
        DecimalFormat format = new DecimalFormat("##,###,###");
        String bonito = format.format(rut);
        return bonito+"-"+verificador;
    }


}
