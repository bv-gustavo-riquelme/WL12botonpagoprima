package cl.bice.vida.botonpago.common.dto.vistas;

import java.io.Serializable;

import java.util.Date;

public class BpiDttDettraturnoVw implements Serializable {

    private Long codEmpresa;
    private Long codMedio;
    private Long idTransaccion;
    private Long cuota;
    private Long numProducto;
    private Long montoTotal;
    private String nombrePersona;
    private String nombre;
    private Date fechainicio;
    private Long numTransaccionMedio;
    private Long codProducto;
    private Long rutPersona;
    private Date turno;
    private Long estado;
    private Long idCuadratura;
    private String canal;
    
    private Long folioCaja;

    public BpiDttDettraturnoVw() {
        super();
    }

    public Long getCodEmpresa() {
        return this.codEmpresa;
    }

    public Long getCodMedio() {
        return this.codMedio;
    }

    public Long getCodProducto() {
        return this.codProducto;
    }

    public Long getCuota() {
        return this.cuota;
    }

    public Long getEstado() {
        return this.estado;
    }

    public Date getFechainicio() {
        return this.fechainicio;
    }

    public Long getIdCuadratura() {
        return this.idCuadratura;
    }

    public Long getIdTransaccion() {
        return this.idTransaccion;
    }

    public Long getMontoTotal() {
        return this.montoTotal;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getNombrePersona() {
        return this.nombrePersona;
    }

    public Long getNumProducto() {
        return this.numProducto;
    }

    public Long getNumTransaccionMedio() {
        return this.numTransaccionMedio;
    }

    public Long getRutPersona() {
        return this.rutPersona;
    }

    public Date getTurno() {
        return this.turno;
    }

    public void setCodEmpresa(Long codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public void setCodMedio(Long codMedio) {
        this.codMedio = codMedio;
    }

    public void setCodProducto(Long codProducto) {
        this.codProducto = codProducto;
    }

    public void setCuota(Long cuota) {
        this.cuota = cuota;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public void setIdCuadratura(Long idCuadratura) {
        this.idCuadratura = idCuadratura;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setMontoTotal(Long montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public void setNumProducto(Long numProducto) {
        this.numProducto = numProducto;
    }

    public void setNumTransaccionMedio(Long numTransaccionMedio) {
        this.numTransaccionMedio = numTransaccionMedio;
    }

    public void setRutPersona(Long rutPersona) {
        this.rutPersona = rutPersona;
    }

    public void setTurno(Date turno) {
        this.turno = turno;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getCanal() {
        return canal;
    }

    public void setFolioCaja(Long folioCaja) {
        this.folioCaja = folioCaja;
    }

    public Long getFolioCaja() {
        return folioCaja;
    }
}
