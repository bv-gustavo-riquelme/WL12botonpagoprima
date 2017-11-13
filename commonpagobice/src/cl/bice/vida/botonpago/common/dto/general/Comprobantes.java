package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

/**
 * Tabla : (BICEVIDA.BPI_COM_COMPROBANTES_TBL)
 */
public class Comprobantes  implements Serializable {

    private Long idTransaccion;
    private Integer codProducto;
    private Long numProducto;
    private Integer cuota;
    private Integer montoBase;
    private Integer montoExcedente;
    private Integer montoTotal;
    
    public Comprobantes() {
        super();
    }

    public Integer getCodProducto() {
        return this.codProducto;
    }

    public Integer getCuota() {
        return this.cuota;
    }

    public Long getIdTransaccion() {
        return this.idTransaccion;
    }

    public Integer getMontoBase() {
        return this.montoBase;
    }

    public Integer getMontoExcedente() {
        return this.montoExcedente;
    }

    public Integer getMontoTotal() {
        return this.montoTotal;
    }

    public Long getNumProducto() {
        return this.numProducto;
    }

    public void setCodProducto(Integer codProducto) {
        this.codProducto = codProducto;
    }

    public void setCuota(Integer cuota) {
        this.cuota = cuota;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setMontoBase(Integer montoBase) {
        this.montoBase = montoBase;
    }

    public void setMontoExcedente(Integer montoExcedente) {
        this.montoExcedente = montoExcedente;
    }

    public void setMontoTotal(Integer montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setNumProducto(Long numProducto) {
        this.numProducto = numProducto;
    }
}
