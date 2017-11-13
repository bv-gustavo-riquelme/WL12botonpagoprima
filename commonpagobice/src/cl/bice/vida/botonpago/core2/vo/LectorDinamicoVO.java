package cl.bice.vida.botonpago.core2.vo;

import java.io.Serializable;

import java.util.Hashtable;


public class LectorDinamicoVO implements Serializable {
    private Hashtable valores;
    
    public LectorDinamicoVO() {
    }
    
    public void addField(String nombre, Object valor) {
        if (valores==null) valores = new Hashtable();
        valores.put(nombre, valor);
    }

    public Object getField(String nombre) {
        if (valores==null) valores = new Hashtable();
        return valores.get(nombre);
    }

    public Hashtable getValores() {
        return valores;
    }
}
