package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class BpiComComprobantesTbl  implements Serializable {

    private Long idTransaccion;
    private Integer codProducto;
    private Long numProducto;
    private Integer cuota;
    private Integer montoBase;
    private Integer montoExcedente;
    private Integer montoTotal;

    public BpiComComprobantesTbl() {
        super();
    }


    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setCodProducto(Integer codProducto) {
        this.codProducto = codProducto;
    }

    public Integer getCodProducto() {
        return codProducto;
    }

    public void setNumProducto(Long numProducto) {
        this.numProducto = numProducto;
    }

    public Long getNumProducto() {
        return numProducto;
    }

    public void setCuota(Integer cuota) {
        this.cuota = cuota;
    }

    public Integer getCuota() {
        return cuota;
    }

    public void setMontoBase(Integer montoBase) {
        this.montoBase = montoBase;
    }

    public Integer getMontoBase() {
        return montoBase;
    }

    public void setMontoExcedente(Integer montoExcedente) {
        this.montoExcedente = montoExcedente;
    }

    public Integer getMontoExcedente() {
        return montoExcedente;
    }

    public void setMontoTotal(Integer montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Integer getMontoTotal() {
        return montoTotal;
    }
}
