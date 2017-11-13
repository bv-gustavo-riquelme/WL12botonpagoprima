package cl.bice.vida.botonpago.common.dto.general;

import cl.bice.vida.botonpago.common.dto.parametros.Empresas;

import java.io.Serializable;


public class TransaccionByEmpresa implements Serializable {
    
    private Integer cod_empresa;
    private Long monto_total;
    private Integer num_transacciones;
    private Empresas empresa = null;
    private Integer id_transaccion;
    
    public TransaccionByEmpresa() {
    
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

    public void setId_transaccion(Integer id_transaccion) {
        this.id_transaccion = id_transaccion;
    }

    public Integer getId_transaccion() {
        return id_transaccion;
    }
}
