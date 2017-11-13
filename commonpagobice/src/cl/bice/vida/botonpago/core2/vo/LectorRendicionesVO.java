package cl.bice.vida.botonpago.core2.vo;

import java.io.Serializable;

import java.util.Date;


public class LectorRendicionesVO implements Serializable {
    private java.util.Date fechaOperacion;
    private Long numeroDividendo;
    private Long numeroOperacion;
    private Double montoPago;
    private String recaudador;
    private String recaudadorDescripcion;

    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public Date getFechaOperacion() {
        return fechaOperacion;
    }

    public void setNumeroDividendo(Long numeroDividendo) {
        this.numeroDividendo = numeroDividendo;
    }

    public Long getNumeroDividendo() {
        return numeroDividendo;
    }

    public void setNumeroOperacion(Long numeroOperacion) {
        this.numeroOperacion = numeroOperacion;
    }

    public Long getNumeroOperacion() {
        return numeroOperacion;
    }

    public void setMontoPago(Double montoPago) {
        this.montoPago = montoPago;
    }

    public Double getMontoPago() {
        return montoPago;
    }

    public void setRecaudador(String recaudador) {
        this.recaudador = recaudador;
    }

    public String getRecaudador() {
        return recaudador;
    }

    public void setRecaudadorDescripcion(String recaudadorDescripcion) {
        this.recaudadorDescripcion = recaudadorDescripcion;
    }

    public String getRecaudadorDescripcion() {
        return recaudadorDescripcion;
    }
}
