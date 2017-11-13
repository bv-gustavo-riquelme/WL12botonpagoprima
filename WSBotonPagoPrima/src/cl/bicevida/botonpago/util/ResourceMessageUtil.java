package cl.bicevida.botonpago.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
/**
 * Clase que permite recuperar datos del archivo de propiedad especificado en atributo BUNDLE_NAME.
 * @author rhicil
 *
 */
public class ResourceMessageUtil {

    private static final String BUNDLE_NAME = "msgpagopublicobice";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
    public ResourceMessageUtil() {
    }
    
    /**
     * Recupera Propiedades del Resources
     * @param key
     * @return String
     */
    public static String getProperty(String key) {
            try {
                    return RESOURCE_BUNDLE.getString(key);
            } catch (MissingResourceException e) {
                    return '!' + key + '!';
            }
    }    
    
}