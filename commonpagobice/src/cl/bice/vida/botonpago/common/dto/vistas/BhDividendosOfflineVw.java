package cl.bice.vida.botonpago.common.dto.vistas;


import java.io.Serializable;

import java.util.Date;

/**
 * Tabla :  (BICEVIDA.OFF_BHDIVIDENDOS_TBL)
 */
public class BhDividendosOfflineVw implements Serializable {
   
    private Integer rut;
    private String srut;
    private String dv;
    private Long numOpe;
    private Long numDiv;
    private Date fecVencimiento;
    private Long totPes;
    private Double totMda;
    private Integer codProducto;
    private Integer codMecanismo;
    private String producto;
    private Integer codEmpresa;
    private Integer codEstado;
    private Double amortizacion;
    private Double intereses;
    private Double interesPenal;
    private Double gastosCobranzas;
    private Double seguroCesantia;
    private Double seguroIncendio;
    private Double seguroDesgravamen;
    private Double otrosCargos;
    
    private Date fecPago;
    private Integer totalPagado;
    private Integer sucursalPago;
    private Integer dif_boleta;
    private Integer plazo_mes;
    
    private String nombreSucursalPago;
    private String bancoPATPAC;
    private Date fechaPATPAC;

    public BhDividendosOfflineVw() {
        super();
    }

    public String getDv() {
        return this.dv;
    }

    public Date getFecVencimiento() {
        return this.fecVencimiento;
    }


    public Long getNumDiv() {
        return this.numDiv;
    }

    public Long getNumOpe() {
        return this.numOpe;
    }

    public Integer getRut() {
        return this.rut;
    }

    public Double getTotMda() {
        return this.totMda;
    }

    public Long getTotPes() {
        return this.totPes;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public void setFecVencimiento(Date fecVencimiento) {
        this.fecVencimiento = fecVencimiento;
    }


    public void setNumDiv(Long numDiv) {
        this.numDiv = numDiv;
    }

    public void setNumOpe(Long numOpe) {
        this.numOpe = numOpe;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setRut(Integer rut) {
        this.rut = rut;
    }

    public void setTotMda(Double totMda) {
        this.totMda = totMda;
    }

    public void setTotPes(Long totPes) {
        this.totPes = totPes;
    }

    public void setCodProducto(Integer codProducto) {
        this.codProducto = codProducto;
    }

    public Integer getCodProducto() {
        return codProducto;
    }

    public void setCodMecanismo(Integer codMecanismo) {
        this.codMecanismo = codMecanismo;
    }

    public Integer getCodMecanismo() {
        return codMecanismo;
    }
 

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Integer getCodEmpresa() {
        return codEmpresa;
    }

    public String getProducto() {
        return producto;
    }

    public void setSrut(String srut) {
        this.srut = srut;
    }

    public String getSrut() {
        return srut;
    }

    public void setCodEstado(Integer codEstado) {
        this.codEstado = codEstado;
    }

    public Integer getCodEstado() {
        return codEstado;
    }

    public void setIntereses(Double intereses) {
        this.intereses = intereses;
    }

    public Double getIntereses() {
        return intereses;
    }

    public void setInteresPenal(Double interesPenal) {
        this.interesPenal = interesPenal;
    }

    public Double getInteresPenal() {
        return interesPenal;
    }

    public void setGastosCobranzas(Double gastosCobranzas) {
        this.gastosCobranzas = gastosCobranzas;
    }

    public Double getGastosCobranzas() {
        return gastosCobranzas;
    }

    public void setSeguroCesantia(Double seguroCesantia) {
        this.seguroCesantia = seguroCesantia;
    }

    public Double getSeguroCesantia() {
        return seguroCesantia;
    }

    public void setSeguroIncendio(Double seguroIncendio) {
        this.seguroIncendio = seguroIncendio;
    }

    public Double getSeguroIncendio() {
        return seguroIncendio;
    }

    public void setSeguroDesgravamen(Double seguroDesgravamen) {
        this.seguroDesgravamen = seguroDesgravamen;
    }

    public Double getSeguroDesgravamen() {
        return seguroDesgravamen;
    }

    public void setOtrosCargos(Double otrosCargos) {
        this.otrosCargos = otrosCargos;
    }

    public Double getOtrosCargos() {
        return otrosCargos;
    }

    public void setAmortizacion(Double amortizacion) {
        this.amortizacion = amortizacion;
    }

    public Double getAmortizacion() {
        return amortizacion;
    }
    
    public void setFecPago(Date fecPago) {
        this.fecPago = fecPago;
    }

    public Date getFecPago() {
        return fecPago;
    }

    public void setTotalPagado(Integer totalPagado) {
        this.totalPagado = totalPagado;
    }

    public Integer getTotalPagado() {
        return totalPagado;
    }

    public void setSucursalPago(Integer sucursalPago) {
        this.sucursalPago = sucursalPago;
    }

    public Integer getSucursalPago() {
        return sucursalPago;
    }

    public void setDif_boleta(Integer dif_boleta) {
        this.dif_boleta = dif_boleta;
    }

    public Integer getDif_boleta() {
        return dif_boleta;
    }
    
    public BhDividendosVw toBhDividendosVw(){
        BhDividendosVw obj = new BhDividendosVw();
        
        obj.setAmortizacion(this.getAmortizacion());
        obj.setCodEmpresa(this.getCodEmpresa());
        obj.setCodEstado(this.getCodEstado());
        obj.setCodMecanismo(this.getCodMecanismo());
        obj.setCodProducto(this.getCodProducto());
        obj.setDif_boleta(this.getDif_boleta());
        obj.setDv(this.getDv());
        obj.setFecPago(this.getFecPago());
        obj.setFecVencimiento(this.getFecVencimiento());
        obj.setGastosCobranzas(this.getGastosCobranzas());
        obj.setIntereses(this.getIntereses());
        obj.setInteresPenal(this.getInteresPenal());
        obj.setNumDiv(this.getNumDiv());
        obj.setNumOpe(this.getNumOpe());
        obj.setOtrosCargos(this.getOtrosCargos());
        obj.setProducto(this.getProducto());
        obj.setRut(this.getRut());
        obj.setSeguroCesantia(this.getSeguroCesantia());
        obj.setSeguroDesgravamen(this.getSeguroDesgravamen());
        obj.setSeguroIncendio(this.getSeguroIncendio());
        obj.setSrut(this.getSrut());
        obj.setSucursalPago(this.getSucursalPago());
        obj.setTotalPagado(this.getTotalPagado());
        obj.setTotMda(this.getTotMda());
        obj.setTotPes(this.getTotPes());
        obj.setPlazo_mes(this.getPlazo_mes());
        obj.setNombreSucursalPago(this.getNombreSucursalPago());
        obj.setBancoPATPAC(this.getBancoPATPAC());
        obj.setFechaPATPAC(this.getFechaPATPAC());
        return obj;        
    }

    public void setPlazo_mes(Integer plazo_mes) {
        this.plazo_mes = plazo_mes;
    }

    public Integer getPlazo_mes() {
        return plazo_mes;
    }

    public void setNombreSucursalPago(String nombreSucursalPago) {
        this.nombreSucursalPago = nombreSucursalPago;
    }

    public String getNombreSucursalPago() {
        return nombreSucursalPago;
    }

    public void setBancoPATPAC(String bancoPATPAC) {
        this.bancoPATPAC = bancoPATPAC;
    }

    public String getBancoPATPAC() {
        return bancoPATPAC;
    }

    public void setFechaPATPAC(Date fechaPATPAC) {
        this.fechaPATPAC = fechaPATPAC;
    }

    public Date getFechaPATPAC() {
        return fechaPATPAC;
    }
}
