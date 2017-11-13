package cl.bice.vida.botonpago.common.dto.general;

import cl.bice.vida.botonpago.common.dto.parametros.MedioPago;

import java.io.Serializable;

public class TransaccionMedioByEmpresa implements Serializable {
    private Long monto_total;
    private Integer num_transacciones;
    private Integer cod_medio;
    private Integer cod_empresa;
    private MedioPago medio = null;
    private Integer id_transaccion;
    
    public TransaccionMedioByEmpresa() {
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

    public String getNombre_medio() {
        if(medio!=null) return medio.getNombre();
        else return "";
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

    public void setMedio(MedioPago medio) {
        this.medio = medio;
    }

    public MedioPago getMedio() {
        return medio;
    }

    public void setId_transaccion(Integer id_transaccion) {
        this.id_transaccion = id_transaccion;
    }

    public Integer getId_transaccion() {
        return id_transaccion;
    }
}
