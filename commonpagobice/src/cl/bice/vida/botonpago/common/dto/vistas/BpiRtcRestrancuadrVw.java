package cl.bice.vida.botonpago.common.dto.vistas;

import java.io.Serializable;

import java.util.Date;

public class BpiRtcRestrancuadrVw implements Serializable {

    private Double codEmpresa;
    private String nombreEmpresa;
    private Date fechaHora;
    private Double idCuadratura;
    private Double transacciones;
    private Double montoTotal;
    private Double transaccionesProb;
    private Double montoTotalProb;
    private String logCuadr;
    

    public BpiRtcRestrancuadrVw() {
        super();
    }

    public Double getCodEmpresa() {
        return this.codEmpresa;
    }

    public Date getFechaHora() {
        return this.fechaHora;
    }

    public Double getIdCuadratura() {
        return this.idCuadratura;
    }

    public String getLogCuadr() {
        return this.logCuadr;
    }

    public Double getMontoTotal() {
        return this.montoTotal;
    }

    public Double getMontoTotalProb() {
        return this.montoTotalProb;
    }

    public String getNombreEmpresa() {
        return this.nombreEmpresa;
    }

    public Double getTransacciones() {
        return this.transacciones;
    }

    public Double getTransaccionesProb() {
        return this.transaccionesProb;
    }
    
    

    public void setCodEmpresa(Double codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setIdCuadratura(Double idCuadratura) {
        this.idCuadratura = idCuadratura;
    }

    public void setLogCuadr(String logCuadr) {
        this.logCuadr = logCuadr;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setMontoTotalProb(Double montoTotalProb) {
        this.montoTotalProb = montoTotalProb;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public void setTransacciones(Double transacciones) {
        this.transacciones = transacciones;
    }

    public void setTransaccionesProb(Double transaccionesProb) {
        this.transaccionesProb = transaccionesProb;
    }
}
