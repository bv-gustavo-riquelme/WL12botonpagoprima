package cl.bice.vida.botonpago.modelo.util;

import java.util.ResourceBundle;


public class ResourceBundleUtil { 

    private static final String RESOURCE_BASENAME = "bicemodelobotonpago";
    private static ResourceBundle rb;

    /**
     * Inicializador estatico del bundle
     */
    static {
        try {
            rb = ResourceBundle.getBundle(RESOURCE_BASENAME);
        } catch(Exception e ) {
            rb = ResourceBundle.getBundle(RESOURCE_BASENAME + ".properties");
        } 
    }

    /**
     * Recupera el valor de una key
     * 
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        String valor = null;
        try {
            valor = rb.getString(key);
        } catch (Exception e) {
            valor = "";
        }        
	return valor;
    }
}
