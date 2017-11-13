package cl.bice.vida.botonpago.common.vo;

import cl.bice.vida.botonpago.common.util.StringUtil;

import java.io.Serializable;

public class ResultadoConsultaVO implements Serializable {
    private int resultado;
    private String mensajeResultado = "";
    private String numeroTransaccionBanco = "";
    private String numeroCuotasBanco = "";
    private String fechaBanco = "";
    private String horaBanco = "";
    private String numeroOrdenCompra = "";
    private String numeroTarjeta = "";
    private String codigoAutorizacion = "";    
    private String tipoCuotas = "";
    private String monedaPago = "";
    private String medioCodigoRespuesta = "";
    private int codigoEstadoTransaccion;
    private String CodigoEmpresa = "";
    private Integer CodigoMedioPago;
    private Long idEmpresa;    

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public int getResultado() {
        return resultado;
    }

    public void setMensajeResultado(String mensajeResultado) {
        this.mensajeResultado = mensajeResultado;
    }

    public String getMensajeResultado() {
        return mensajeResultado;
    }

    public void setNumeroTransaccionBanco(String numeroTransaccionBanco) {
        this.numeroTransaccionBanco = numeroTransaccionBanco;
    }

    public String getNumeroTransaccionBanco() {
        return numeroTransaccionBanco;
    }

    public void setNumeroCuotasBanco(String numeroCuotasBanco) {
        this.numeroCuotasBanco = numeroCuotasBanco;
    }

    public String getNumeroCuotasBanco() {
        return numeroCuotasBanco;
    }

    public void setNumeroOrdenCompra(String numeroOrdenCompra) {
        this.numeroOrdenCompra = numeroOrdenCompra;
    }

    public String getNumeroOrdenCompra() {
        return numeroOrdenCompra;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setCodigoAutorizacion(String codigoAutorizacion) {
        this.codigoAutorizacion = codigoAutorizacion;
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public void setFechaBanco(String fechaBanco) {
        this.fechaBanco = fechaBanco;
    }

    public String getFechaBanco() {
        return fechaBanco;
    }

    public void setHoraBanco(String horaBanco) {
        this.horaBanco = horaBanco;
    }

    public String getHoraBanco() {
        return horaBanco;
    }

    public void setTipoCuotas(String tipoCuotas) {
        this.tipoCuotas = tipoCuotas;
    }

    public String getTipoCuotas() {
        return tipoCuotas;
    }

    public void setMonedaPago(String monedaPago) {
        this.monedaPago = monedaPago;
    }

    public String getMonedaPago() {
        return monedaPago;
    }

    public void setCodigoEstadoTransaccion(int codigoEstadoTransaccion) {
        this.codigoEstadoTransaccion = codigoEstadoTransaccion;
    }

    public int getCodigoEstadoTransaccion() {
        return codigoEstadoTransaccion;
    }
    
    public String toXML() {
        String xml = "";
        xml = xml +
        "<footer>" +
        "<numerooperacion>" + getNumeroTransaccionBanco() + "</numerooperacion>" + 
        "   <numerocuotas>" + getNumeroCuotasBanco() + "</numerocuotas>" + 
        "   <codigoautorizacion>" +  getCodigoAutorizacion() + "</codigoautorizacion>" + 
        "   <numeroordencompra>" +  getNumeroOrdenCompra() + "</numeroordencompra>" + 
        "   <numerotarjeta>" +  getNumeroTarjeta() + "</numerotarjeta>" + 
        "   <fechabanco>" +  getFechaBanco() + "</fechabanco>" + 
        "   <tipocuota>" + StringUtil.replaceBadCharsFOPDF( getTipoCuotas()) + "</tipocuota>" + 
        "   <moneda>" +  getMonedaPago() + "</moneda>" + 
        "   <horabanco>" +  getHoraBanco() + "</horabanco>" +
        "</footer>";
        xml = StringUtil.replaceString(xml,"null","No aplica");
        return xml;
    }

    public void setMedioCodigoRespuesta(String medioCodigoRespuesta) {
        this.medioCodigoRespuesta = medioCodigoRespuesta;
    }

    public String getMedioCodigoRespuesta() {
        return medioCodigoRespuesta;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.CodigoEmpresa = codigoEmpresa;
    }

    public String getCodigoEmpresa() {
        return CodigoEmpresa;
    }

    public void setCodigoMedioPago(Integer codigoMedioPago) {
        this.CodigoMedioPago = codigoMedioPago;
    }

    public Integer getCodigoMedioPago() {
        return CodigoMedioPago;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }
}
