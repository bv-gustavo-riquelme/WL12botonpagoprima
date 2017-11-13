package cl.bice.vida.botonpago.common.dto.parametros;

import java.io.Serializable;

public class TipoCuota  implements Serializable {

    private Double tipoCuota;
    private String descripcion;

    public TipoCuota() {
        super();
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Double getTipoCuota() {
        return this.tipoCuota;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipoCuota(Double tipoCuota) {
        this.tipoCuota = tipoCuota;
    }

}
