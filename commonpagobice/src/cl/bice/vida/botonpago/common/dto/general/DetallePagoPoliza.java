package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;


public class DetallePagoPoliza  implements Serializable {

    private Long folioRecibo;
    private Integer detalle;
    private String descCargo;
    private Double monto;

    public DetallePagoPoliza() {
        super();
    }

    public String getDescCargo() {
        return this.descCargo;
    }

    public Integer getDetalle() {
        return this.detalle;
    }

    public Long getFolioRecibo() {
        return this.folioRecibo;
    }

    public Double getMonto() {
        return this.monto;
    }


    public void setDescCargo(String descCargo) {
        this.descCargo = descCargo;
    }

    public void setDetalle(Integer detalle) {
        this.detalle = detalle;
    }

    public void setFolioRecibo(Long folioRecibo) {
        this.folioRecibo = folioRecibo;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
