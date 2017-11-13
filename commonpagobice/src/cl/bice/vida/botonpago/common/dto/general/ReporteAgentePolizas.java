package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

public class ReporteAgentePolizas  implements Serializable {
    private Integer rut_contratante;
    private String dv_contratante;
    private String contratante;
    private String producto;
    private Integer poliza_pol;
    private Date fecha_recibo;
    private Double prima_bruta_uf;
    private Integer prima_bruta;
    private String estado;
    private Integer id_ramo;
    private Integer folio_recibo;
    
    public ReporteAgentePolizas() {
    }

    public void setRut_contratante(Integer rut_contratante) {
        this.rut_contratante = rut_contratante;
    }

    public Integer getRut_contratante() {
        return rut_contratante;
    }

    public void setDv_contratante(String dv_contratante) {
        this.dv_contratante = dv_contratante;
    }

    public String getDv_contratante() {
        return dv_contratante;
    }

    public void setContratante(String contratante) {
        this.contratante = contratante;
    }

    public String getContratante() {
        return contratante;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getProducto() {
        return producto;
    }

    public void setPoliza_pol(Integer poliza_pol) {
        this.poliza_pol = poliza_pol;
    }

    public Integer getPoliza_pol() {
        return poliza_pol;
    }

    public void setFecha_recibo(Date fecha_recibo) {
        this.fecha_recibo = fecha_recibo;
    }

    public Date getFecha_recibo() {
        return fecha_recibo;
    }

    public void setPrima_bruta_uf(Double prima_bruta_uf) {
        this.prima_bruta_uf = prima_bruta_uf;
    }

    public Double getPrima_bruta_uf() {
        return prima_bruta_uf;
    }

    public void setPrima_bruta(Integer prima_bruta) {
        this.prima_bruta = prima_bruta;
    }

    public Integer getPrima_bruta() {
        return prima_bruta;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setId_ramo(Integer id_ramo) {
        this.id_ramo = id_ramo;
    }

    public Integer getId_ramo() {
        return id_ramo;
    }

    public void setFolio_recibo(Integer folio_recibo) {
        this.folio_recibo = folio_recibo;
    }

    public Integer getFolio_recibo() {
        return folio_recibo;
    }
}
