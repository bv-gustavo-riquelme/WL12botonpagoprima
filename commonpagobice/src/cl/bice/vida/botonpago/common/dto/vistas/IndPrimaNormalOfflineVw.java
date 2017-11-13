package cl.bice.vida.botonpago.common.dto.vistas;

import java.io.Serializable;

import java.util.Date;

/**
 * Vista : (BICEVIDA.OFF_PRIMANORMAL_TBL)
 */
public class IndPrimaNormalOfflineVw  implements Serializable {

    private Integer rutContratante;
    private Character dvRutContratante;
    private Integer polizaPol;
    private Integer propuestaPol;
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
    private String nombre;
    private Integer ramo;
    private Integer fpago;
    private Integer viapago;

    public IndPrimaNormalOfflineVw() {
        super();
    }

    public String getDescTipoRecibo() {
        return this.descTipoRecibo;
    }

    public Character getDvRutContratante() {
        return this.dvRutContratante;
    }

    public Date getFInicioRecibo() {
        return this.fInicioRecibo;
    }

    public Date getFTerminoRecibo() {
        return this.fTerminoRecibo;
    }

    public Integer getFolioRecibo() {
        return this.folioRecibo;
    }

    public String getIdTipoRecibo() {
        return this.idTipoRecibo;
    }

    public Double getIvaUfRecibo() {
        return this.ivaUfRecibo;
    }

    public Integer getPolizaPol() {
        return this.polizaPol;
    }

    public Double getPrimaBrutaUfRecibo() {
        return this.primaBrutaUfRecibo;
    }

    public Double getPrimaNetaUfRecibo() {
        return this.primaNetaUfRecibo;
    }

    public Integer getPropuestaPol() {
        return this.propuestaPol;
    }

    public Integer getRutContratante() {
        return this.rutContratante;
    }

    public void setDescTipoRecibo(String descTipoRecibo) {
        this.descTipoRecibo = descTipoRecibo;
    }

    public void setDvRutContratante(Character dvRutContratante) {
        this.dvRutContratante = dvRutContratante;
    }

    public void setFInicioRecibo(Date fInicioRecibo) {
        this.fInicioRecibo = fInicioRecibo;
    }

    public void setFTerminoRecibo(Date fTerminoRecibo) {
        this.fTerminoRecibo = fTerminoRecibo;
    }

    public void setFolioRecibo(Integer folioRecibo) {
        this.folioRecibo = folioRecibo;
    }


    public void setIdTipoRecibo(String idTipoRecibo) {
        this.idTipoRecibo = idTipoRecibo;
    }

    public void setIvaUfRecibo(Double ivaUfRecibo) {
        this.ivaUfRecibo = ivaUfRecibo;
    }

    public void setPolizaPol(Integer polizaPol) {
        this.polizaPol = polizaPol;
    }

    public void setPrimaBrutaUfRecibo(Double primaBrutaUfRecibo) {
        this.primaBrutaUfRecibo = primaBrutaUfRecibo;
    }

    public void setPrimaNetaUfRecibo(Double primaNetaUfRecibo) {
        this.primaNetaUfRecibo = primaNetaUfRecibo;
    }

    public void setPropuestaPol(Integer propuestaPol) {
        this.propuestaPol = propuestaPol;
    }

    public void setRutContratante(Integer rutContratante) {
        this.rutContratante = rutContratante;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setRamo(Integer ramo) {
        this.ramo = ramo;
    }

    public Integer getRamo() {
        return ramo;
    }

    public void setFpago(Integer fpago) {
        this.fpago = fpago;
    }

    public Integer getFpago() {
        return fpago;
    }

    public IndPrimaNormalVw toIndPrimaNormalVw(){
        IndPrimaNormalVw obj = new IndPrimaNormalVw();
        
        obj.setCodEmpresa(this.getCodEmpresa());
        obj.setCodMecanismo(this.getCodMecanismo());
        obj.setCodProducto(this.getCodProducto());
        obj.setDescTipoRecibo(this.getDescTipoRecibo());
        obj.setDvRutContratante(this.getDvRutContratante());
        obj.setFInicioRecibo(this.getFInicioRecibo());
        obj.setFolioRecibo(this.getFolioRecibo());
        obj.setFpago(this.getFpago());
        obj.setFTerminoRecibo(this.getFTerminoRecibo());
        obj.setIdTipoRecibo(this.getIdTipoRecibo());
        obj.setIvaUfRecibo(this.getIvaUfRecibo());
        obj.setNombre(this.getNombre());
        obj.setPolizaPol(this.getPolizaPol());
        obj.setPrimaBrutaUfRecibo(this.getPrimaBrutaUfRecibo());
        obj.setPrimaNetaUfRecibo(this.getPrimaNetaUfRecibo());
        obj.setPropuestaPol(this.getPropuestaPol());
        obj.setRamo(this.getRamo());
        obj.setRutContratante(this.getRutContratante());
        obj.setViapago(this.getViapago());
                
        return obj;
    }

    public void setViapago(Integer viapago) {
        this.viapago = viapago;
    }

    public Integer getViapago() {
        return viapago;
    }
}
