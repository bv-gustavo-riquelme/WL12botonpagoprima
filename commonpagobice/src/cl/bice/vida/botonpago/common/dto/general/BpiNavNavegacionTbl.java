package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

public class BpiNavNavegacionTbl  implements Serializable {

    private Double rutPersona;
    private Date fechaHora;
    private Double montoTransaccion;
    private Double idNavegacion;
    private Double idTransaccion;
    private Double codMedio;
    private Integer empresa;

    public BpiNavNavegacionTbl() {
        super();
    }

    public Double getCodMedio() {
        return this.codMedio;
    }

    public Date getFechaHora() {
        return this.fechaHora;
    }

    public Double getIdNavegacion() {
        return this.idNavegacion;
    }

    public Double getIdTransaccion() {
        return this.idTransaccion;
    }

    public Double getMontoTransaccion() {
        return this.montoTransaccion;
    }

    public Double getRutPersona() {
        return this.rutPersona;
    }

    public void setCodMedio(Double codMedio) {
        this.codMedio = codMedio;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setIdNavegacion(Double idNavegacion) {
        this.idNavegacion = idNavegacion;
    }

    public void setIdTransaccion(Double idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setMontoTransaccion(Double montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public void setRutPersona(Double rutPersona) {
        this.rutPersona = rutPersona;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public Integer getEmpresa() {
        return empresa;
    }
}
