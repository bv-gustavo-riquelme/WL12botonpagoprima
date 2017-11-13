package cl.bice.vida.botonpago.common.dto.general;


import cl.bice.vida.botonpago.common.dto.parametros.Empresas;

import java.io.Serializable;

public class RecaudacionByEmpresa implements Serializable {
    private Integer cod_empresa;
    private Long monto_total;
    private Integer num_transacciones;
    private Empresas empresa;
    private Long id_cuadratura;
    
    public RecaudacionByEmpresa() {
    }

    public void setCod_empresa(Integer cod_empresa) {
        this.cod_empresa = cod_empresa;
    }

    public Integer getCod_empresa() {
        return cod_empresa;
    }

    public String getNombre_empresa() {
        if(empresa!=null) return empresa.getNombre();
        else return "";
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

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setId_cuadratura(Long id_cuadratura) {
        this.id_cuadratura = id_cuadratura;
    }

    public Long getId_cuadratura() {
        return id_cuadratura;
    }
}
