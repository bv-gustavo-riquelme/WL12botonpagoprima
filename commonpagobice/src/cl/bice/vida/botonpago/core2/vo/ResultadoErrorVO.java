package cl.bice.vida.botonpago.core2.vo;

import java.io.Serializable;


public class ResultadoErrorVO implements Serializable {
    private Long numeroLineaFila;
    private String detalleError;

    public void setNumeroLineaFila(Long numeroLineaFila) {
        this.numeroLineaFila = numeroLineaFila;
    }

    public Long getNumeroLineaFila() {
        return numeroLineaFila;
    }

    public void setDetalleError(String detalleError) {
        this.detalleError = detalleError;
    }

    public String getDetalleError() {
        return detalleError;
    }
}
