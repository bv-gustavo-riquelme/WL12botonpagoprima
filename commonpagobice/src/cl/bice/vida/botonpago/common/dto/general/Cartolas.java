package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

import oracle.xml.parser.v2.XMLDocument;

/**
 * Tabla : BICEVIDA.BPI_CAR_CARTOLAS_TBL
 */
public class Cartolas implements Serializable {

    private Long idCartola;
    private Integer rutPersona;
    private Date fechaHora;
    private String periodo;
    private String detallePagina;
    private Integer empresa;
    
    private XMLDocument  xmldetalle;
    
    public Cartolas() {
        super();
    }

    public String getDetallePagina() {
        return this.detallePagina;
    }

    public Date getFechaHora() {
        return this.fechaHora;
    }

    public Long getIdCartola() {
        return this.idCartola;
    }

    public String getPeriodo() {
        return this.periodo;
    }

    public Integer getRutPersona() {
        return this.rutPersona;
    }

    public void setDetallePagina(String detallePagina) {
        this.detallePagina = detallePagina;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = (fechaHora==null)?new Date(System.currentTimeMillis()):fechaHora;
    }

    public void setIdCartola(Long idCartola) {
        this.idCartola = idCartola;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public void setRutPersona(Integer rutPersona) {
        this.rutPersona = rutPersona;
    }

    public void setXmldetalle(XMLDocument xmldetalle) {
        this.xmldetalle = xmldetalle;
    }

    public XMLDocument getXmldetalle() {
        return xmldetalle;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public Integer getEmpresa() {
        return empresa;
    }
}
