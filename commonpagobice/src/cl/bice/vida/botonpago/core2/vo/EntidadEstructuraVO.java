package cl.bice.vida.botonpago.core2.vo;

import java.io.Serializable;


public class EntidadEstructuraVO implements Serializable {
    private String archivoId;
    private String databasecode;
    private String descripcion;

    public void setArchivoId(String archivoId) {
        this.archivoId = archivoId;
    }

    public String getArchivoId() {
        return archivoId;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDatabasecode(String databasecode) {
        this.databasecode = databasecode;
    }

    public String getDatabasecode() {
        return databasecode;
    }
}
