package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

public class RecaudacionMedioByEmpresa implements Serializable {
    private String nombre_medio;
    private Long monto_total;
    private Integer num_transacciones;
    private Integer cod_medio;
    private Integer cod_empresa;
    private Date fecha;
    private Integer id_cuadratura;
    
    public RecaudacionMedioByEmpresa() {
    }

 
    public void setMonto_total(Long monto_total) {
        this.monto_total = monto_total;
    }

    public Long getMonto_total() {
        return monto_total;
    }

    public void setNum_transacciones(Integer num_transacciones) {
        this.num_transacciones = num_transacciones;
    }

    public Integer getNum_transacciones() {
        return num_transacciones;
    }

    public void setNombre_medio(String nombre_medio) {
        this.nombre_medio = nombre_medio;
    }

    public String getNombre_medio() {
        return nombre_medio;
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

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setId_cuadratura(Integer id_cuadratura) {
        this.id_cuadratura = id_cuadratura;
    }

    public Integer getId_cuadratura() {
        return id_cuadratura;
    }
}
