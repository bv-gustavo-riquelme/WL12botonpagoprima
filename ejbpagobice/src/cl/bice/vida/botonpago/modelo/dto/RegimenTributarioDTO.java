package cl.bice.vida.botonpago.modelo.dto;

import java.io.Serializable;

public class RegimenTributarioDTO implements Serializable {
    private String descripcionRegimen;
    private Long numeroPoliza;
    private Long numeroRegimen;
    public RegimenTributarioDTO() {
    }

    public void setDescripcionRegimen(String descripcionRegimen) {
        this.descripcionRegimen = descripcionRegimen;
    }

    public String getDescripcionRegimen() {
        return descripcionRegimen;
    }

    public void setNumeroPoliza(Long numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public Long getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroRegimen(Long numeroRegimen) {
        this.numeroRegimen = numeroRegimen;
    }

    public Long getNumeroRegimen() {
        return numeroRegimen;
    }
}
