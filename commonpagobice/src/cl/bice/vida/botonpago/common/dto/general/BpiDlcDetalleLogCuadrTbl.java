package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class BpiDlcDetalleLogCuadrTbl implements Serializable {

    private Long idCuadratura;
    private Long numeroIntento;
    private String detalleProceso;

    public void setIdCuadratura(Long idCuadratura) {
        this.idCuadratura = idCuadratura;
    }

    public Long getIdCuadratura() {
        return idCuadratura;
    }

    public void setNumeroIntento(Long numeroIntento) {
        this.numeroIntento = numeroIntento;
    }

    public Long getNumeroIntento() {
        return numeroIntento;
    }

    public void setDetalleProceso(String detalleProceso) {
        this.detalleProceso = detalleProceso;
    }

    public String getDetalleProceso() {
        return detalleProceso;
    }
}
