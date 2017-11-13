package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.List;

public class BpiDitribFondosAPVTbl implements Serializable {
    public BpiDitribFondosAPVTbl() {
    }
    
    private Integer idTransaccion;    
    private Integer poliza;
    private Integer ramo;
    private Integer folio;
    private Double monto;
    private Integer idAporte;
    private Integer idFondo;
    private Integer color;
    private Integer porcentaje;
    private Integer porcentajemostrar;
    private Integer ordenPresentacion;
    private String nombreFondo;
    private Double saldoInicial;
    private String linkPDF;
    private Integer rutCliente;
    //TODO: Detalle Color
    private String hexadecimal;
    private Long valorColorY;    
    private Long valorColorZ;
    private Long valorColorX;


    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Integer getIdTransaccion() {
        return idTransaccion;
    }   

    public void setPoliza(Integer poliza) {
        this.poliza = poliza;
    }

    public Integer getPoliza() {
        return poliza;
    }

    public void setRamo(Integer ramo) {
        this.ramo = ramo;
    }

    public Integer getRamo() {
        return ramo;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setIdAporte(Integer idAporte) {
        this.idAporte = idAporte;
    }

    public Integer getIdAporte() {
        return idAporte;
    }

    public void setIdFondo(Integer idFondo) {
        this.idFondo = idFondo;
    }

    public Integer getIdFondo() {
        return idFondo;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getColor() {
        return color;
    }

    public void setPorcentaje(Integer porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Integer getPorcentaje() {
        return porcentaje;
    }

    public void setOrdenPresentacion(Integer ordenPresentacion) {
        this.ordenPresentacion = ordenPresentacion;
    }

    public Integer getOrdenPresentacion() {
        return ordenPresentacion;
    }

    public void setNombreFondo(String nombreFondo) {
        this.nombreFondo = nombreFondo;
    }

    public String getNombreFondo() {
        return nombreFondo;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getMonto() {
        return monto;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public void setLinkPDF(String linkPDF) {
        this.linkPDF = linkPDF;
    }

    public String getLinkPDF() {
        return linkPDF;
    }

    public void setRutCliente(Integer rutCliente) {
        this.rutCliente = rutCliente;
    }

    public Integer getRutCliente() {
        return rutCliente;
    }

    public void setPorcentajemostrar(Integer porcentajemostrar) {
        this.porcentajemostrar = porcentajemostrar;
    }

    public Integer getPorcentajemostrar() {
        return porcentajemostrar;
    }

    public void setValorColorY(Long valorColorY) {
        this.valorColorY = valorColorY;
    }

    public Long getValorColorY() {
        return valorColorY;
    }

    public void setHexadecimal(String hexadecimal) {
        this.hexadecimal = hexadecimal;
    }

    public String getHexadecimal() {
        return hexadecimal;
    }

    public void setValorColorZ(Long valorColorZ) {
        this.valorColorZ = valorColorZ;
    }

    public Long getValorColorZ() {
        return valorColorZ;
    }

    public void setValorColorX(Long valorColorX) {
        this.valorColorX = valorColorX;
    }

    public Long getValorColorX() {
        return valorColorX;
    }
}
