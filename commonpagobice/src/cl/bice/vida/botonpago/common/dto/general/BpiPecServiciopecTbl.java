package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

public class BpiPecServiciopecTbl  implements Serializable {

    private Long idNavegacion;
    private Long idTransaccion;
    private Long rutPersona;
    private Date fechaHora;
    private Integer montoTransaccion;
    private Integer codMedio;
    private Integer numTransaccionMedio;

    public BpiPecServiciopecTbl() {
        super();
    }


    public void setIdNavegacion(Long idNavegacion) {
        this.idNavegacion = idNavegacion;
    }

    public Long getIdNavegacion() {
        return idNavegacion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setRutPersona(Long rutPersona) {
        this.rutPersona = rutPersona;
    }

    public Long getRutPersona() {
        return rutPersona;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setMontoTransaccion(Integer montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public Integer getMontoTransaccion() {
        return montoTransaccion;
    }

    public void setCodMedio(Integer codMedio) {
        this.codMedio = codMedio;
    }

    public Integer getCodMedio() {
        return codMedio;
    }

    public void setNumTransaccionMedio(Integer numTransaccionMedio) {
        this.numTransaccionMedio = numTransaccionMedio;
    }

    public Integer getNumTransaccionMedio() {
        return numTransaccionMedio;
    }
}
