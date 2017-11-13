package cl.bice.vida.botonpago.modelo.dto;

import java.util.Date;

public class VWIndPrimaNormalVO {

    private int rutContratante;
    private String dvRutContratante;
    private int polizaPol;
    private int propuestaPol;
    private int folioRecibo;
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
    private int codEmpresa;
    private int codMecanismo;
    private int codProducto;
    private String nombre;
    private int ramo;
    private int fpago;
    private int viapago;
    private int reajuste;
    private int multa;
    private int interes;
    private int costosCobranza;
    private int secuencia;


    public VWIndPrimaNormalVO() {
        super();
    }


    public void setRutContratante(int rutContratante) {
        this.rutContratante = rutContratante;
    }

    public int getRutContratante() {
        return rutContratante;
    }

    public void setDvRutContratante(String dvRutContratante) {
        this.dvRutContratante = dvRutContratante;
    }

    public String getDvRutContratante() {
        return dvRutContratante;
    }

    public void setPolizaPol(int polizaPol) {
        this.polizaPol = polizaPol;
    }

    public int getPolizaPol() {
        return polizaPol;
    }

    public void setPropuestaPol(int propuestaPol) {
        this.propuestaPol = propuestaPol;
    }

    public int getPropuestaPol() {
        return propuestaPol;
    }

    public void setFolioRecibo(int folioRecibo) {
        this.folioRecibo = folioRecibo;
    }

    public int getFolioRecibo() {
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

    public void setCodEmpresa(int codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public int getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodMecanismo(int codMecanismo) {
        this.codMecanismo = codMecanismo;
    }

    public int getCodMecanismo() {
        return codMecanismo;
    }

    public void setCodProducto(int codProducto) {
        this.codProducto = codProducto;
    }

    public int getCodProducto() {
        return codProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setRamo(int ramo) {
        this.ramo = ramo;
    }

    public int getRamo() {
        return ramo;
    }

    public void setFpago(int fpago) {
        this.fpago = fpago;
    }

    public int getFpago() {
        return fpago;
    }

    public void setViapago(int viapago) {
        this.viapago = viapago;
    }

    public int getViapago() {
        return viapago;
    }

    public void setReajuste(int reajuste) {
        this.reajuste = reajuste;
    }

    public int getReajuste() {
        return reajuste;
    }

    public void setMulta(int multa) {
        this.multa = multa;
    }

    public int getMulta() {
        return multa;
    }

    public void setInteres(int interes) {
        this.interes = interes;
    }

    public int getInteres() {
        return interes;
    }

    public void setCostosCobranza(int costosCobranza) {
        this.costosCobranza = costosCobranza;
    }

    public int getCostosCobranza() {
        return costosCobranza;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public int getSecuencia() {
        return secuencia;
    }
}
