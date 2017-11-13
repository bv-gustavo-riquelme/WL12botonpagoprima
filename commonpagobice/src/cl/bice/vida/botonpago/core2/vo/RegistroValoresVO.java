package cl.bice.vida.botonpago.core2.vo;

import java.io.Serializable;

public class RegistroValoresVO implements Serializable {
    private String key;
    private Object valor;
    public RegistroValoresVO() {
    }

    public RegistroValoresVO(String key, Object valor) {
        this.key = key;
        this.valor = valor;
    }
    
    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public Object getValor() {
        return valor;
    }
}
