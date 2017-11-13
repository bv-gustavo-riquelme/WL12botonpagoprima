package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;


public class SPActualizarTransaccionDto implements Serializable {
    private long idTrx;
    private int codRet;
    private int nropPagos;
    private int montoPago;
    private String descRet; 
    private long idCom;
    private String idTrxBanco;
    private String indPago;
    private Integer medioNumeroCuotas;
    private Date medioFechaPago;
    private String medioTipoTarjeta;
    private String medioNumeroTarjeta;
    private String medioCodigoRespuesta;
    private String medioCodigoAutorizacion;
    private String medioOrdenCompra;
    private String regularizaWebpay;
    private Boolean esPecV2 = false;
    private String instrumentoCaja;

    public void setIdTrx(long idTrx) {
        this.idTrx = idTrx;
    }

    public long getIdTrx() {
        return idTrx;
    }

    public void setCodRet(int codRet) {
        this.codRet = codRet;
    }

    public int getCodRet() {
        return codRet;
    }

    public void setNropPagos(int nropPagos) {
        this.nropPagos = nropPagos;
    }

    public int getNropPagos() {
        return nropPagos;
    }

    public void setMontoPago(int montoPago) {
        this.montoPago = montoPago;
    }

    public int getMontoPago() {
        return montoPago;
    }

    public void setDescRet(String descRet) {
        this.descRet = descRet;
    }

    public String getDescRet() {
        return descRet;
    }

    public void setIdCom(long idCom) {
        this.idCom = idCom;
    }

    public long getIdCom() {
        return idCom;
    }

    public void setIdTrxBanco(String idTrxBanco) {
        this.idTrxBanco = idTrxBanco;
    }

    public String getIdTrxBanco() {
        return idTrxBanco;
    }

    public void setIndPago(String indPago) {
        this.indPago = indPago;
    }

    public String getIndPago() {
        return indPago;
    }

    public void setMedioNumeroCuotas(Integer medioNumeroCuotas) {
        this.medioNumeroCuotas = medioNumeroCuotas;
    }

    public Integer getMedioNumeroCuotas() {
        return medioNumeroCuotas;
    }

    public void setMedioFechaPago(Date medioFechaPago) {
        this.medioFechaPago = medioFechaPago;
    }

    public Date getMedioFechaPago() {
        return medioFechaPago;
    }

    public void setMedioTipoTarjeta(String medioTipoTarjeta) {
        this.medioTipoTarjeta = medioTipoTarjeta;
    }

    public String getMedioTipoTarjeta() {
        return medioTipoTarjeta;
    }

    public void setMedioNumeroTarjeta(String medioNumeroTarjeta) {
        this.medioNumeroTarjeta = medioNumeroTarjeta;
    }

    public String getMedioNumeroTarjeta() {
        return medioNumeroTarjeta;
    }

    public void setMedioCodigoRespuesta(String medioCodigoRespuesta) {
        this.medioCodigoRespuesta = medioCodigoRespuesta;
    }

    public String getMedioCodigoRespuesta() {
        return medioCodigoRespuesta;
    }

    public void setMedioCodigoAutorizacion(String medioCodigoAutorizacion) {
        this.medioCodigoAutorizacion = medioCodigoAutorizacion;
    }

    public String getMedioCodigoAutorizacion() {
        return medioCodigoAutorizacion;
    }

    public void setMedioOrdenCompra(String medioOrdenCompra) {
        this.medioOrdenCompra = medioOrdenCompra;
    }

    public String getMedioOrdenCompra() {
        return medioOrdenCompra;
    }

    public void setRegularizaWebpay(String regularizaWebpay) {
        this.regularizaWebpay = regularizaWebpay;
    }

    public String getRegularizaWebpay() {
        return regularizaWebpay;
    }

    public void setEsPecV2(Boolean esPecV2) {
        this.esPecV2 = esPecV2;
    }

    public Boolean getEsPecV2() {
        return esPecV2;
    }

    public void setInstrumentoCaja(String instrumentoCaja) {
        this.instrumentoCaja = instrumentoCaja;
    }

    public String getInstrumentoCaja() {
        return instrumentoCaja;
    }
}
