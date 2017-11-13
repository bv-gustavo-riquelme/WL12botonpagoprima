package cl.bice.vida.botonpago.common.dto.vistas;

import java.io.Serializable;

import java.util.Date;

public class BpiVreRechazosVw  implements Serializable {

    private Date fechaCuadratura;
    private Double idTransaccion;
    private Double codEstado;
    private Double codMedio;
    private Double montoTotal;
    private Date fechaTransaccion;
    private Double rutPersona;
    private String nombrePersona;
    private Date turno;
    private Date fechafin;
    private String observaciones;
    private Long folio;
    private Double numTransaccionCaja;
    private Double numTransaccionMedio;
    private String nombre;
    private Integer empresa;

    public BpiVreRechazosVw() {
        super();
    }

    public Double getCodEstado() {
        return this.codEstado;
    }

    public Double getCodMedio() {
        return this.codMedio;
    }

    public Date getFechaCuadratura() {
        return this.fechaCuadratura;
    }

    public Date getFechaTransaccion() {
        return this.fechaTransaccion;
    }

    public Date getFechafin() {
        return this.fechafin;
    }

    public Long getFolio() {
        return this.folio;
    }

    public Double getIdTransaccion() {
        return this.idTransaccion;
    }

    public Double getMontoTotal() {
        return this.montoTotal;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getNombrePersona() {
        return this.nombrePersona;
    }

    public Double getNumTransaccionCaja() {
        return this.numTransaccionCaja;
    }

    public Double getNumTransaccionMedio() {
        return this.numTransaccionMedio;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Double getRutPersona() {
        return this.rutPersona;
    }

    public Date getTurno() {
        return this.turno;
    }

    public void setCodEstado(Double codEstado) {
        this.codEstado = codEstado;
    }

    public void setCodMedio(Double codMedio) {
        this.codMedio = codMedio;
    }

    public void setFechaCuadratura(Date fechaCuadratura) {
        this.fechaCuadratura = fechaCuadratura;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public void setFolio(Long folio) {
        this.folio = folio;
    }

    public void setIdTransaccion(Double idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public void setNumTransaccionCaja(Double numTransaccionCaja) {
        this.numTransaccionCaja = numTransaccionCaja;
    }

    public void setNumTransaccionMedio(Double numTransaccionMedio) {
        this.numTransaccionMedio = numTransaccionMedio;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setRutPersona(Double rutPersona) {
        this.rutPersona = rutPersona;
    }

    public void setTurno(Date turno) {
        this.turno = turno;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public Integer getEmpresa() {
        return empresa;
    }
}
