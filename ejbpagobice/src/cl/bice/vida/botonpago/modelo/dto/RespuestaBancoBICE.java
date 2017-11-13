package cl.bice.vida.botonpago.modelo.dto;

import java.io.Serializable;


public class RespuestaBancoBICE implements Serializable {
    private int estado;
    private String resultado;
    private long idTransaccionCliente; 
    private String idTrxBice;
    private String idEmpresaCliente;
    public RespuestaBancoBICE() {
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }


    public void setIdTransaccionCliente(long idTransaccionCliente) {
        this.idTransaccionCliente = idTransaccionCliente;
    }

    public long getIdTransaccionCliente() {
        return idTransaccionCliente;
    }

    public void setIdTrxBice(String idTrxBice) {
        this.idTrxBice = idTrxBice;
    }

    public String getIdTrxBice() {
        return idTrxBice;
    }

    public void setIdEmpresaCliente(String idEmpresaCliente) {
        this.idEmpresaCliente = idEmpresaCliente;
    }

    public String getIdEmpresaCliente() {
        return idEmpresaCliente;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}
