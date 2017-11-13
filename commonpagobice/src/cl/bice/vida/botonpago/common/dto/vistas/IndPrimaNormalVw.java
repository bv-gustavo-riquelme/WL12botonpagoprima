package cl.bice.vida.botonpago.common.dto.vistas;

import java.io.Serializable;

import java.util.Date;

/**
 * Vista : (BICEVIDA.IND_PRIMA_NORMAL_VW)
 */
public class IndPrimaNormalVw  implements Serializable {

    private Integer rutContratante;
    private Character dvRutContratante;
    private Integer polizaPol;
    private Integer propuestaPol;
    private Integer folioRecibo;
    private String idTipoRecibo;
    private Double primaBrutaUfRecibo;
    private Double primaNetaUfRecibo;
    private Double ivaUfRecibo;
    private Long primaBrutaPesosRecibo;
    private Long primaNetaPesosRecibo;
    private Long ivaPesosRecibo;
    private Double valorCambioUF;
    private Date fInicioRecibo; //fecha
    private Date fTerminoRecibo; //fecha
    private String descTipoRecibo;
    private Integer codEmpresa;
    private Integer codMecanismo;
    private Integer codProducto;
    private String nombre;
    private Integer ramo;
    private Integer fpago;
    private Integer viapago;
    private Integer reajuste;
    private Integer multa;
    private Integer interes;
    private Integer costosCobranza;
    private Integer secuencia;
    
    // TODO: ----- Se deben agregar los nuevos parametros para APV
    
    public IndPrimaNormalVw() {
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

    public void setViapago(Integer viapago) {
        this.viapago = viapago;
    }

    public Integer getViapago() {
        return viapago;
    }

    public void setReajuste(Integer reajuste) {
        this.reajuste = reajuste;
    }

    public Integer getReajuste() {
        return reajuste;
    }

    public void setMulta(Integer multa) {
        this.multa = multa;
    }

    public Integer getMulta() {
        return multa;
    }

    public void setInteres(Integer interes) {
        this.interes = interes;
    }

    public Integer getInteres() {
        return interes;
    }

    public void setCostosCobranza(Integer costosCobranza) {
        this.costosCobranza = costosCobranza;
    }

    public Integer getCostosCobranza() {
        return costosCobranza;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setPrimaBrutaPesosRecibo(Long primaBrutaPesosRecibo) {
        this.primaBrutaPesosRecibo = primaBrutaPesosRecibo;
    }

    public Long getPrimaBrutaPesosRecibo() {
        return primaBrutaPesosRecibo;
    }

    public void setPrimaNetaPesosRecibo(Long primaNetaPesosRecibo) {
        this.primaNetaPesosRecibo = primaNetaPesosRecibo;
    }

    public Long getPrimaNetaPesosRecibo() {
        return primaNetaPesosRecibo;
    }

    public void setIvaPesosRecibo(Long ivaPesosRecibo) {
        this.ivaPesosRecibo = ivaPesosRecibo;
    }

    public Long getIvaPesosRecibo() {
        return ivaPesosRecibo;
    }


    public void setValorCambioUF(Double valorCambioUF) {
        this.valorCambioUF = valorCambioUF;
    }

    public Double getValorCambioUF() {
        return valorCambioUF;
    }
}
