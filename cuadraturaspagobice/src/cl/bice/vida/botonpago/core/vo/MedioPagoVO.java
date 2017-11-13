package cl.bice.vida.botonpago.core.vo;

import java.io.Serializable;

public class MedioPagoVO implements Serializable {
    private String archivoId;
    private String idComercio;
    private String descripcion;

    public void setArchivoId(String archivoId) {
        this.archivoId = archivoId;
    }

    public String getArchivoId() {
        return archivoId;
    }

    public void setIdComercio(String idComercio) {
        this.idComercio = idComercio;
    }

    public String getIdComercio() {
        return idComercio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
