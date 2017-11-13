package cl.bice.vida.botonpago.common.dto.general;


import java.io.Serializable;

import java.util.Date;

import oracle.xml.parser.v2.XMLDocument;

public class BpiCarCartolasTbl implements Serializable {

    private Long idCartola;
    private Long rutPersona;
    private Date fechaHora;
    private String periodo;
    private XMLDocument detallePagina;
    private Integer empresa;

    public BpiCarCartolasTbl() {
        super();
    }

    public XMLDocument getDetallePagina() {
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

    public Long getRutPersona() {
        return this.rutPersona;
    }

    public void setDetallePagina(XMLDocument detallePagina) {
        this.detallePagina = detallePagina;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setIdCartola(Long idCartola) {
        this.idCartola = idCartola;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public void setRutPersona(Long rutPersona) {
        this.rutPersona = rutPersona;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public Integer getEmpresa() {
        return empresa;
    }
}
