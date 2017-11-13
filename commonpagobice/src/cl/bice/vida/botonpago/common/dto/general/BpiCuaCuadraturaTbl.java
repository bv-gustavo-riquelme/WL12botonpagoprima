package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

public class BpiCuaCuadraturaTbl  implements Serializable {

    private Date fechaHora;
    private Double codEmpresa;
    private Double codMedio;
    private Double idCuadratura;
    private Double montoTotal;
    private Double montoInformado;
    private Double transacciones;

    public BpiCuaCuadraturaTbl() {
        super();
    }

    public Double getCodEmpresa() {
        return this.codEmpresa;
    }

    public Double getCodMedio() {
        return this.codMedio;
    }

    public Date getFechaHora() {
        return this.fechaHora;
    }

    public Double getIdCuadratura() {
        return this.idCuadratura;
    }

    public Double getMontoInformado() {
        return this.montoInformado;
    }

    public Double getMontoTotal() {
        return this.montoTotal;
    }

    public Double getTransacciones() {
        return this.transacciones;
    }

    public void setCodEmpresa(Double codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public void setCodMedio(Double codMedio) {
        this.codMedio = codMedio;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setIdCuadratura(Double idCuadratura) {
        this.idCuadratura = idCuadratura;
    }

    public void setMontoInformado(Double montoInformado) {
        this.montoInformado = montoInformado;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setTransacciones(Double transacciones) {
        this.transacciones = transacciones;
    }

}
