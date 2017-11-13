package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class DetalleAPV implements Serializable {

    private String poliza;
    private String descripcionProducto;
    private String regimenTributario;
    private String inicioVigencia;
    
    private Long numeropoliza;
    private Long numeroramo;
    private Long numerofolio;
    private Long rut;
    private Long monto;
    private String usuarioconsulta;
    private Long idaporte;
    private Long idtransaccion;

    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }

    public String getPoliza() {
        return poliza;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setRegimenTributario(String regimenTributario) {
        this.regimenTributario = regimenTributario;
    }

    public String getRegimenTributario() {
        return regimenTributario;
    }

    public void setNumeropoliza(Long numeropoliza) {
        this.numeropoliza = numeropoliza;
    }

    public Long getNumeropoliza() {
        return numeropoliza;
    }

    public void setNumeroramo(Long numeroramo) {
        this.numeroramo = numeroramo;
    }

    public Long getNumeroramo() {
        return numeroramo;
    }

    public void setNumerofolio(Long numerofolio) {
        this.numerofolio = numerofolio;
    }

    public Long getNumerofolio() {
        return numerofolio;
    }

    public void setRut(Long rut) {
        this.rut = rut;
    }

    public Long getRut() {
        return rut;
    }

    public void setMonto(Long monto) {
        this.monto = monto;
    }

    public Long getMonto() {
        return monto;
    }

    public void setUsuarioconsulta(String usuarioconsulta) {
        this.usuarioconsulta = usuarioconsulta;
    }

    public String getUsuarioconsulta() {
        return usuarioconsulta;
    }

    public void setIdaporte(Long idaporte) {
        this.idaporte = idaporte;
    }

    public Long getIdaporte() {
        return idaporte;
    }

    public void setIdtransaccion(Long idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public Long getIdtransaccion() {
        return idtransaccion;
    }

    public void setInicioVigencia(String inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public String getInicioVigencia() {
        return inicioVigencia;
    }
}
