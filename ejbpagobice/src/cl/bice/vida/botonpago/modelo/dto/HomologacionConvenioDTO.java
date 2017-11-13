package cl.bice.vida.botonpago.modelo.dto;

import java.io.Serializable;

import java.util.Date;

public class HomologacionConvenioDTO implements Serializable {
    private Long idTransaccion;
    private String idComercioTransaccion;
    private Long codMedioPago;
    private java.util.Date fechaTransaccion;
    public HomologacionConvenioDTO() {
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdComercioTransaccion(String idComercioTransaccion) {
        this.idComercioTransaccion = idComercioTransaccion;
    }

    public String getIdComercioTransaccion() {
        return idComercioTransaccion;
    }

    public void setCodMedioPago(Long codMedioPago) {
        this.codMedioPago = codMedioPago;
    }

    public Long getCodMedioPago() {
        return codMedioPago;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }
}
