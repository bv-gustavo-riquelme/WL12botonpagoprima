package cl.bicevida.botonpago.util;

import org.apache.log4j.Logger;


import java.util.StringTokenizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class FormatearNumero implements Converter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FormatearNumero.class);

    public FormatearNumero() {
    }

    public Object getAsObject(FacesContext facesContext, UIComponent uIComponent, String string) {
        Object returnObject = getAsString(facesContext, uIComponent, string);
        return returnObject;
    }

    /**
     * Convierte Numero 
     * @param facesContext
     * @param uIComponent
     * @param object
     * @return
     */
    public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {
        String valor = (String)object;
        StringTokenizer token = new StringTokenizer(valor, "-");
        if (token.countTokens() == 2) {
            String primeraParte = token.nextToken();
            String digito = token.nextToken();
            
            Double numeroD = new Double(primeraParte);        
            String convertido = NumeroUtil.formatMilesNoDecimal(numeroD);
            valor = convertido + "-" + digito;

        }        
        return valor;
    }
}
