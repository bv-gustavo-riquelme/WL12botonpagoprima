package cl.bice.vida.botonpago.common.dto.vistas;

import java.io.Serializable;

import java.util.Date;

/**
 * Vista : IND_PRIMA_NORMAL_PATPAC_VW
 */
public class IndPrimaNormalPatpacVw  implements Serializable {

    private Integer rutContratante;
    private Character dvRutContratante;
    private Integer ramo;
    private Integer polizaPol;
    private Integer propuestaPol;
    private String nombre;
    private Integer folioRecibo;
    private String idTipoRecibo;
    private Double primaBrutaUfRecibo;
    private Double primaNetaUfRecibo;
    private Double ivaUfRecibo;
    private Date fInicioRecibo;
    private Date fTerminoRecibo;
    private String descTipoRecibo;
    private Integer codEmpresa;
    private Integer codMecanismo;
    private Integer codProducto;
    private String idFpago;
    private Date fUfRecibo;
    private Double valorUfRecibo;
    private String idViaPago;


    public void setRutContratante(Integer rutContratante) {
        this.rutContratante = rutContratante;
    }

    public Integer getRutContratante() {
        return rutContratante;
    }

    public void setDvRutContratante(Character dvRutContratante) {
        this.dvRutContratante = dvRutContratante;
    }

    public Character getDvRutContratante() {
        return dvRutContratante;
    }

    public void setRamo(Integer ramo) {
        this.ramo = ramo;
    }

    public Integer getRamo() {
        return ramo;
    }

    public void setPolizaPol(Integer polizaPol) {
        this.polizaPol = polizaPol;
    }

    public Integer getPolizaPol() {
        return polizaPol;
    }

    public void setPropuestaPol(Integer propuestaPol) {
        this.propuestaPol = propuestaPol;
    }

    public Integer getPropuestaPol() {
        return propuestaPol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setFolioRecibo(Integer folioRecibo) {
        this.folioRecibo = folioRecibo;
    }

    public Integer getFolioRecibo() {
        return folioRecibo;
    }

    public void setIdTipoRecibo(String idTipoRecibo) {
        this.idTipoRecibo = idTipoRecibo;
    }

    public String getIdTipoRecibo() {
        return idTipoRecibo;
    }

    public void setPrimaBrutaUfRecibo(Double primaBrutaUfRecibo) {
        this.primaBrutaUfRecibo = primaBrutaUfRecibo;
    }

    public Double getPrimaBrutaUfRecibo() {
        return primaBrutaUfRecibo;
    }

    public void setPrimaNetaUfRecibo(Double primaNetaUfRecibo) {
        this.primaNetaUfRecibo = primaNetaUfRecibo;
    }

    public Double getPrimaNetaUfRecibo() {
        return primaNetaUfRecibo;
    }

    public void setIvaUfRecibo(Double ivaUfRecibo) {
        this.ivaUfRecibo = ivaUfRecibo;
    }

    public Double getIvaUfRecibo() {
        return ivaUfRecibo;
    }

    public void setFInicioRecibo(Date fInicioRecibo) {
        this.fInicioRecibo = fInicioRecibo;
    }

    public Date getFInicioRecibo() {
        return fInicioRecibo;
    }

    public void setFTerminoRecibo(Date fTerminoRecibo) {
        this.fTerminoRecibo = fTerminoRecibo;
    }

    public Date getFTerminoRecibo() {
        return fTerminoRecibo;
    }

    public void setDescTipoRecibo(String descTipoRecibo) {
        this.descTipoRecibo = descTipoRecibo;
    }

    public String getDescTipoRecibo() {
        return descTipoRecibo;
    }

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Integer getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodMecanismo(Integer codMecanismo) {
        this.codMecanismo = codMecanismo;
    }

    public Integer getCodMecanismo() {
        return codMecanismo;
    }

    public void setCodProducto(Integer codProducto) {
        this.codProducto = codProducto;
    }

    public Integer getCodProducto() {
        return codProducto;
    }

    public void setIdFpago(String idFpago) {
        this.idFpago = idFpago;
    }

    public String getIdFpago() {
        return idFpago;
    }

    public void setFUfRecibo(Date fUfRecibo) {
        this.fUfRecibo = fUfRecibo;
    }

    public Date getFUfRecibo() {
        return fUfRecibo;
    }

    public void setValorUfRecibo(Double valorUfRecibo) {
        this.valorUfRecibo = valorUfRecibo;
    }

    public Double getValorUfRecibo() {
        return valorUfRecibo;
    }

    public void setIdViaPago(String idViaPago) {
        this.idViaPago = idViaPago;
    }

    public String getIdViaPago() {
        return idViaPago;
    }
}
