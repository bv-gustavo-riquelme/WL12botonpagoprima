package cl.bice.vida.botonpago.core.vo;

import cl.bice.vida.botonpago.common.util.StringUtil;

import java.io.Serializable;

import java.util.Date;

public class TransaccionRendicionVO implements Serializable {
    private java.util.Date fechaOperacion;
    private String numeroOrdenBice;
    private String numeroTransaccionMedio;
    private String rutCliente;
    private String nombreCliente;
    private Double montoPago;
    private Double montoInformado;
    private String codigoAutorizacion;
    private String numeroTarjeta;    
    private Integer numeroCuotas;
    private String tipoTarjeta;
    private String estado;
    private String observaciones;
    private String codBancoPortal;
    
    public TransaccionRendicionVO() {
    }

    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public Date getFechaOperacion() {
        return fechaOperacion;
    }


    public void setNumeroTransaccionMedio(String numeroTransaccionMedio) {
        this.numeroTransaccionMedio = numeroTransaccionMedio;
    }

    public String getNumeroTransaccionMedio() {
        return numeroTransaccionMedio;
    }

    public void setRutCliente(String rutCliente) {
        this.rutCliente = rutCliente;
    }

    public String getRutCliente() {
        return rutCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setMontoPago(Double montoPago) {
        this.montoPago = montoPago;
    }

    public Double getMontoPago() {
        return montoPago;
    }

    public void setCodigoAutorizacion(String codigoAutorizacion) {
        this.codigoAutorizacion = codigoAutorizacion;
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroCuotas(Integer numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public Integer getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setNumeroOrdenBice(String numeroOrdenBice) {
        this.numeroOrdenBice = numeroOrdenBice;
    }

    public String getNumeroOrdenBice() {
        if (numeroOrdenBice != null) numeroOrdenBice = StringUtil.replaceString(numeroOrdenBice," ","");
        return numeroOrdenBice;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setMontoInformado(Double montoInformado) {
        this.montoInformado = montoInformado;
    }

    public Double getMontoInformado() {
        return montoInformado;
    }

    public void setCodBancoPortal(String codBancoPortal) {
        this.codBancoPortal = codBancoPortal;
    }

    public String getCodBancoPortal() {
        return codBancoPortal;
    }
}
