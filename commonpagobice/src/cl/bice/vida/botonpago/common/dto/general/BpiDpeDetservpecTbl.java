package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

import oracle.xml.parser.v2.XMLDocument;

public class BpiDpeDetservpecTbl  implements Serializable {

    private BpiPecServiciopecTbl bpiPecServiciopecTbl;

    private Long idNavegacion;
    private Integer entrada;
    private Integer codPagina;
    private Date fechaHora;
    private Long montoTransaccion;
    private XMLDocument detallePagina;

    public BpiDpeDetservpecTbl() {
        super();
    }


    public void setIdNavegacion(Long idNavegacion) {
        this.idNavegacion = idNavegacion;
    }

    public Long getIdNavegacion() {
        return idNavegacion;
    }

    public void setEntrada(Integer entrada) {
        this.entrada = entrada;
    }

    public Integer getEntrada() {
        return entrada;
    }

    public void setCodPagina(Integer codPagina) {
        this.codPagina = codPagina;
    }

    public Integer getCodPagina() {
        return codPagina;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setMontoTransaccion(Long montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public Long getMontoTransaccion() {
        return montoTransaccion;
    }

    public void setDetallePagina(XMLDocument detallePagina) {
        this.detallePagina = detallePagina;
    }

    public XMLDocument getDetallePagina() {
        return detallePagina;
    }
    
    public void set_bpiPecServiciopecTbl(BpiPecServiciopecTbl _bpiPecServiciopecTbl) {
        this.bpiPecServiciopecTbl = _bpiPecServiciopecTbl;
    }

    public BpiPecServiciopecTbl get_bpiPecServiciopecTbl() {
        return bpiPecServiciopecTbl;
    }
}
