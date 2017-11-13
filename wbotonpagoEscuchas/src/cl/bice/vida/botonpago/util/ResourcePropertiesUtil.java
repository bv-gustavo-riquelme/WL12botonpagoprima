package cl.bice.vida.botonpago.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourcePropertiesUtil {

    private static final String BUNDLE_NAME = "configuracionbice";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private ResourcePropertiesUtil() {
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
