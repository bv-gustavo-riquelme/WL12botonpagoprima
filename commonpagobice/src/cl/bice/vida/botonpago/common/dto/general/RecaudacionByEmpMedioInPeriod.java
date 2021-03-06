package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

public class RecaudacionByEmpMedioInPeriod implements Serializable {
    private String rut;
    private String nombre_persona;
    private String nombre_producto;
    private Long numero_producto;
    private Integer cuota;
    private Long idTransaccion;
    private Long numTransaccionMedio;
    private Integer montoTotal;
    private Integer codProducto;
    private Integer cod_medio;
    private Integer cod_empresa;
    private Date fecha_hora;
    private Date fecha_Pago;
    private Date turno;
    private Long idCuadratura;   

    private Integer numero_propuesta;
    
    private Long folioCaja;

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getRut() {
        return rut;
    }

    public void setNombre_persona(String nombre_persona) {
        this.nombre_persona = nombre_persona;
    }

    public String getNombre_persona() {
        return nombre_persona;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNumero_producto(Long numero_producto) {
        this.numero_producto = numero_producto;
    }

    public Long getNumero_producto() {
        return numero_producto;
    }

    public void setCuota(Integer cuota) {
        this.cuota = cuota;
    }

    public Integer getCuota() {
        return cuota;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setNumTransaccionMedio(Long numTransaccionMedio) {
        this.numTransaccionMedio = numTransaccionMedio;
    }

    public Long getNumTransaccionMedio() {
        return numTransaccionMedio;
    }

    public void setMontoTotal(Integer montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Integer getMontoTotal() {
        return montoTotal;
    }

    public void setCodProducto(Integer codProducto) {
        this.codProducto = codProducto;
    }

    public Integer getCodProducto() {
        return codProducto;
    }

    public void setCod_medio(Integer cod_medio) {
        this.cod_medio = cod_medio;
    }

    public Integer getCod_medio() {
        return cod_medio;
    }

    public void setCod_empresa(Integer cod_empresa) {
        this.cod_empresa = cod_empresa;
    }

    public Integer getCod_empresa() {
        return cod_empresa;
    }

    public void setFecha_hora(Date fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public Date getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_Pago(Date fecha_Pago) {
        this.fecha_Pago = fecha_Pago;
    }

    public Date getFecha_Pago() {
        return fecha_Pago;
    }

    public void setTurno(Date turno) {
        this.turno = turno;
    }

    public Date getTurno() {
        return turno;
    }

    public void setIdCuadratura(Long idCuadratura) {
        this.idCuadratura = idCuadratura;
    }

    public Long getIdCuadratura() {
        return idCuadratura;
    }

    public void setNumero_propuesta(Integer numero_propuesta) {
        this.numero_propuesta = numero_propuesta;
    }

    public Integer getNumero_propuesta() {
        return numero_propuesta;
    }

    public void setFolioCaja(Long folioCaja) {
        this.folioCaja = folioCaja;
    }

    public Long getFolioCaja() {
        return folioCaja;
    }
}
