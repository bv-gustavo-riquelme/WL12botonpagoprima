package cl.bicevida.botonpago.components;

import org.apache.log4j.Logger;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class ImpresorTag extends UIComponentTag {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ImpresorTag.class);

    private String valor;
    private String rendered;

    public void release() {
        super.release();
        valor = null;
        rendered = null;
    }
    
    /**
     * Setea propiedades
     * @param component
     */
     protected void setProperties(UIComponent component) {
         super.setProperties(component);
        if (valor != null) {
             if (isValueReference(valor)) {
                 ValueBinding vb = getFacesContext().getApplication().createValueBinding(valor);
                 component.setValueBinding("valor", vb);
             } else {
                 component.getAttributes().put("valor", valor);
             }
         }
         
         if (rendered != null) {
              if (isValueReference(rendered)) {
                  ValueBinding rb = getFacesContext().getApplication().createValueBinding(rendered);
                  component.setValueBinding("rendered", rb);
              } else {
                  component.getAttributes().put("rendered", rendered);
              }
          }
     }   
    
    public String getComponentType() {
        return "impresor";
    }

    public String getRendererType() {
        return null;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void set_rendered(String _rendered) {
        this.rendered = _rendered;
    }

    public String getRendered() {
        return rendered;
    }
}
