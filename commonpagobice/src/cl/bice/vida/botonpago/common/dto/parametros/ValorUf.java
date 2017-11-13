package cl.bice.vida.botonpago.common.dto.parametros;

import java.io.Serializable;

import java.util.Date;

/**
 * Tabla : (BICEVIDA.VALOR_UF)
 */
public class ValorUf  implements Serializable {

    private Date fecha;     //FECHA
    private Double valorUf; //VALOR

    public ValorUf() {
        super();
    }

    public Date getFecha() {
        return this.fecha;
    }

    public Double getValorUf() {
        return this.valorUf;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setValorUf(Double valorUf) {
        this.valorUf = valorUf;
    }

}
