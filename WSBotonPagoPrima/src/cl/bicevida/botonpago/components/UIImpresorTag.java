package cl.bice.vida.botonpago.components;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


public class UIImpresorTag extends UIOutput {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(UIImpresorTag.class);
    
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String valor = (String)getAttributes().get("valor");
        Boolean rendered = (Boolean)getAttributes().get("rendered");
        if (valor != null){
            if(rendered != null){
                if(rendered.booleanValue()){
                    writer.write(valor);  
                }
                  
            }
            
        }
    }

}
