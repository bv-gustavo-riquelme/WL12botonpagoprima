package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

import oracle.xml.parser.v2.XMLDocument;

/**
 * Tabla : (BICEVIDA.BPI_DNA_DETNAVEG_TBL)
 */
public class BpiDnaDetnavegTbl implements Serializable {

    private BpiNavNavegacionTbl bpiNavNavegacionTbl;

    private Integer idNavegacion;
    private Date fechaHora;
    private Double montoTransaccion;
    private XMLDocument detallePagina;
    private Double entrada;
    private Integer cod_pagina;
    private String pagina;
    private Integer id_canal;
    private boolean checkbox = false;

    public BpiDnaDetnavegTbl() {
        super();
    }


    public XMLDocument getDetallePagina() {
        return this.detallePagina;
    }

    public Double getEntrada() {
        return this.entrada;
    }

    public Date getFechaHora() {
        return this.fechaHora;
    }

    public Integer getIdNavegacion() {
        return this.idNavegacion;
    }

    public Double getMontoTransaccion() {
        return this.montoTransaccion;
    }

    public void setDetallePagina(XMLDocument detallePagina) {
        this.detallePagina = detallePagina;
    }

    public void setEntrada(Double entrada) {
        this.entrada = entrada;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setIdNavegacion(Integer idNavegacion) {
        this.idNavegacion = idNavegacion;
    }

    public void setMontoTransaccion(Double montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public void setCod_pagina(Integer cod_pagina) {
        this.cod_pagina = cod_pagina;
    }

    public Integer getCod_pagina() {
        return cod_pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getPagina() {
        return pagina;
    }

    public void setBpiNavNavegacionTbl(BpiNavNavegacionTbl bpiNavNavegacionTbl) {
        this.bpiNavNavegacionTbl = bpiNavNavegacionTbl;
    }

    public BpiNavNavegacionTbl getBpiNavNavegacionTbl() {
        return bpiNavNavegacionTbl;
    }

    public void setId_canal(Integer id_canal) {
        this.id_canal = id_canal;
    }

    public Integer getId_canal() {
        return id_canal;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public boolean isCheckbox() {
        return checkbox;
    }
}
