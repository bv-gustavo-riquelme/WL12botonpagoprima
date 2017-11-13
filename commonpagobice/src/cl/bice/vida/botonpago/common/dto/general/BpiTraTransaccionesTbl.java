package cl.bice.vida.botonpago.common.dto.general;

import cl.bice.vida.botonpago.common.util.RutUtil;

import java.io.Serializable;

import java.sql.Timestamp;

import java.util.Date;


public class BpiTraTransaccionesTbl implements Serializable {

    private Long idTransaccion;
    private Double montoTotal;
    private Date fechainicio;
    private Timestamp fechahorainicio;
    private Double rutPersona;
    private String nombrePersona;
    private Date fechafin;
    private String observaciones;
    private Integer codEstado;
    private Integer codMedioPago;
    private Integer medioNumeroCuotas;
    private Date medioFechaPago;
    private String medioTipoTarjeta;
    private String medioNumeroTarjeta;
    private String medioCodigoRespuesta;
    private String numeroTransaccionBanco;
    private String medioCodigoAutorizacion;
    private String medioOrdenCompra;    
    private Long idEmpresa;
    private Double cargo;
    private Date disponFondos;
    private Integer tipoTransaccion;
    private Long numeroFolioCaja;
    private String codigoEmpresa; //BV=VIDA BH=HIPOTECARIA
    private String codigoOrigenTransaccion; //PEC, APT, BOTON
    private java.util.Date turno;
    private String rutCompleto;

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setRutPersona(Double rutPersona) {
        this.rutPersona = rutPersona;
    }

    public Double getRutPersona() {
        return rutPersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setCodEstado(Integer codEstado) {
        this.codEstado = codEstado;
    }

    public Integer getCodEstado() {
        return codEstado;
    }

    public void setMedioNumeroCuotas(Integer medioNumeroCuotas) {
        this.medioNumeroCuotas = medioNumeroCuotas;
    }

    public Integer getMedioNumeroCuotas() {
        return medioNumeroCuotas;
    }

    public void setMedioFechaPago(Date medioFechaPago) {
        this.medioFechaPago = medioFechaPago;
    }

    public Date getMedioFechaPago() {
        return medioFechaPago;
    }

    public void setMedioTipoTarjeta(String medioTipoTarjeta) {
        this.medioTipoTarjeta = medioTipoTarjeta;
    }

    public String getMedioTipoTarjeta() {
        return (medioTipoTarjeta==null)? null: medioTipoTarjeta.trim();
    }

    public void setMedioNumeroTarjeta(String medioNumeroTarjeta) {
        this.medioNumeroTarjeta = medioNumeroTarjeta;
    }

    public String getMedioNumeroTarjeta() {
        return medioNumeroTarjeta;
    }

    public void setMedioCodigoRespuesta(String medioCodigoRespuesta) {
        this.medioCodigoRespuesta = medioCodigoRespuesta;
    }

    public String getMedioCodigoRespuesta() {
        return medioCodigoRespuesta;
    }


    public void setNumeroTransaccionBanco(String numeroTransaccionBanco) {
        this.numeroTransaccionBanco = numeroTransaccionBanco;
    }

    public String getNumeroTransaccionBanco() {
        return numeroTransaccionBanco;
    }

    public void setMedioCodigoAutorizacion(String medioCodigoAutorizacion) {
        this.medioCodigoAutorizacion = medioCodigoAutorizacion;
    }

    public String getMedioCodigoAutorizacion() {
        return medioCodigoAutorizacion;
    }

    public void setMedioOrdenCompra(String medioOrdenCompra) {
        this.medioOrdenCompra = medioOrdenCompra;
    }

    public String getMedioOrdenCompra() {
        return medioOrdenCompra;
    }

    public void setCodMedioPago(Integer codMedioPago) {
        this.codMedioPago = codMedioPago;
    }

    public Integer getCodMedioPago() {
        return codMedioPago;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setFechahorainicio(Timestamp fechahorainicio) {
        this.fechahorainicio = fechahorainicio;
    }

    public Timestamp getFechahorainicio() {
        return fechahorainicio;
    }

    public void setCargo(Double cargo) {
        this.cargo = cargo;
    }

    public Double getCargo() {
        return cargo;
    }

    public void setDisponFondos(Date disponFondos) {
        this.disponFondos = disponFondos;
    }

    public Date getDisponFondos() {
        return disponFondos;
    }

    public void setTipoTransaccion(Integer tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public Integer getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setNumeroFolioCaja(Long numeroFolioCaja) {
        this.numeroFolioCaja = numeroFolioCaja;
    }

    public Long getNumeroFolioCaja() {
        return numeroFolioCaja;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoOrigenTransaccion(String codigoOrigenTransaccion) {
        this.codigoOrigenTransaccion = codigoOrigenTransaccion;
    }

    public String getCodigoOrigenTransaccion() {
        return codigoOrigenTransaccion;
    }

    public void setTurno(Date turno) {
        this.turno = turno;
    }

    public Date getTurno() {
        return turno;
    }

    public void setRutCompleto(String rutCompleto) {
        this.rutCompleto = rutCompleto;
    }

    public String getRutCompleto() {
        rutCompleto = null;
        if (this.rutPersona != null) {
            rutCompleto = "" + this.rutPersona.intValue() + "-" +  RutUtil.calculaDv(""+this.rutPersona.intValue());
        }
        return rutCompleto;
    }
}

