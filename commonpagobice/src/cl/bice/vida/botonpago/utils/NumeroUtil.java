package cl.bice.vida.botonpago.utils;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.Locale;


/**
 * Clase para utilidades numericas
 * @author mmaldonado
 *
 */
public class NumeroUtil {
    /**
     * Logger for this class
     */
    private static DecimalFormatSymbols unusualSymbols;
    
    static {		
            unusualSymbols = new DecimalFormatSymbols( new Locale("ES","","") );
            unusualSymbols.setDecimalSeparator('.');
            unusualSymbols.setGroupingSeparator(',');		
    }
    
    /**
     * formatea numeros
     * @param val
     * @param n
     * @param w
     * @return
     */
    public static String formatDecimales(double val, int decimales) {
        String formato = "####.";
        for (int pg = 1; pg <= decimales; pg++) {
            formato = formato + "0";
        }
        DecimalFormat df1 = new DecimalFormat(formato, new DecimalFormatSymbols(Locale.US));
        return df1.format(val);
    }
    
    /**
     * Funcion de redondeo de
     * numero a una cierta
     * cantidad de decimales
     * @param numero
     * @param decimales
     * @return
     */    
    public static String redondear( double numero, int decimales ) {
        String decimalesrnd = "0.";
        for (int x = 1; x <= decimales; x++) decimalesrnd = decimalesrnd + "0";        
        DecimalFormat df = new DecimalFormat(decimalesrnd) ;
        String numerordn = df.format(numero);
        numerordn = StringUtil.replaceString(numerordn,",",".");
        Double valor = new Double(numerordn);
        return Double.toString(valor);
    }
        
    /**
     * Funcion de redondeo de
     * numero a una cierta
     * cantidad de decimales
     * @param numero
     * @param decimales
     * @return
     */    
    public static Double redondeartodouble( double numero, int decimales ) {
        String decimalesrnd = "0.";
        for (int x = 1; x <= decimales; x++) decimalesrnd = decimalesrnd + "0";        
        DecimalFormat df = new DecimalFormat(decimalesrnd) ;
        String numerordn = df.format(numero);
        numerordn = StringUtil.replaceString(numerordn,",",".");
        Double valor = new Double(numerordn);
        return valor;
    }
    
	
	
    /**
     * Valida que un string contenga un numero
     * @param valor
     * @return boolean
     */
    public static boolean isNumero(String valor) {
            try {
                    Long.parseLong(valor);
                    return true;
            } catch (NumberFormatException e) {
                    return false;
            }			
    }
    /**
     * Retorna un numero en formato decimal
     * @param number
     * @return String
     */
    public static String formatMilesSiDecimal( Object number ){
            DecimalFormat dec = new DecimalFormat();
            String returnString = dec.format(number);
            return returnString;
    }
    /**
     * Retorna un numero en formato de puntos para miles
     * @param number
     * @return String
     */
    public static String formatMilesNoDecimal( Object number ){
        DecimalFormat dec = new DecimalFormat();
        String returnString = dec.format(number);        
        if(returnString.indexOf(",") > 0){
            returnString = returnString.replaceAll(",", ".");
        }
        
        return returnString;
    }
    
    
    /**
     * Funcion para reemplazar separadores de miles
     * @param numero
     * @return String
     */
    public static String removeSeparadoresDeMiles(String numero) {
            String formateado = StringUtil.replaceString(numero,".","");
            return formateado;
    }

    /**
     * Funcion que remueve los separadores de miles y solo
     * deja el monto con el separador decial en caso de existir
     * @param numero
     * @return String
     */
    public static String removeSeparadoresMilesConDecimales(String numero) {
        String formateado = numero;
        String dcimales[] = formateado.split(".");
        String dcimiles[] = formateado.split(",");
        //Utiliza . como separador de miles
        if (dcimales != null && dcimales.length > 2) {
            formateado = StringUtil.replaceString(numero,".","");
        }
        //Utiliza , como separador de miles
        if (dcimiles != null && dcimiles.length > 2) {
             formateado = StringUtil.replaceString(numero,",","");
        }        
        //Utiliza , como sepador decimal por lo cual se reemplaza a un solo .
        if (dcimiles != null && dcimiles.length == 2) {
             formateado = StringUtil.replaceString(numero,",",".");
        }                
        return formateado;
    }    

    /**
     * Funcion que remueve los separadores de miles y solo
     * deja el monto con el separador decial en caso de existir
     * @param numero
     * @return String
     */
    public static String removeSeparadoresMilesSinDecimales(String numero) {
        String formateado = numero;
        String dcimales[] = formateado.split(".");
        String dcimiles[] = formateado.split(",");
        //Utiliza . como separador de miles
        if (dcimales != null && dcimales.length > 2) {
            formateado = StringUtil.replaceString(numero,".","");
        }
        //Utiliza , como separador de miles
        if (dcimiles != null && dcimiles.length > 2) {
            formateado = StringUtil.replaceString(numero,",","");
        }        
        //Utiliza , como sepador decimal por lo cual se reemplaza a un solo .
        if (dcimiles != null && dcimiles.length == 2) {
             formateado = StringUtil.replaceString(numero,",",".");
        }   
        //Buscar el . y quitar todo lo demas
        int pospoint = formateado.indexOf(".");
        if (pospoint > 0) {
            formateado = formateado.substring(0, pospoint);
        }
        return formateado;
    }
    /**
     * Main de pruebas
     * @param args
     */
    public static void main(String[] args) {
        Double uf = new Double("213450.2182374826349732002349872394832");
        String total = redondear(uf.doubleValue(),2);   
        BigDecimal tmp = new BigDecimal(total);
        System.out.println("Valor :" + tmp);
        int entero = Math.round(1342 / 5);
        System.out.println("Parte Entera:"+entero);        
    }
}
