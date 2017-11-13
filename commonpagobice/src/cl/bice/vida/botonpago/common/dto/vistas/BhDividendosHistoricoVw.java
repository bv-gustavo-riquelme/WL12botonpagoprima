package cl.bice.vida.botonpago.common.dto.vistas;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;



public class BhDividendosHistoricoVw implements Serializable {
   
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
    private String estado;
    
    private String nombreSucursalPago;
    private String bancoPATPAC;
    private Date fechaPATPAC;
    
    private Double valorUf;

    /**
     * Tabla : (BICEVIDA.BPI_DIH_DIVIDENDOSHIST_VW)
     */
    public BhDividendosHistoricoVw() {
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
    
    public List getDetalleDividendo(){
    
            List detalle = new ArrayList();
            Object[] data1 = new Object[2];
            data1[0] = "Amortización"; 
            data1[1] = this.getAmortizacion();
            detalle.add(data1);
            Object[] data2 = new Object[2];
            data2[0] = "Intereses"; 
            data2[1] = this.getIntereses();
            detalle.add(data2);
            Object[] data3 = new Object[2];
            data3[0] = "Seguro de Desgravamen"; 
            data3[1] = this.getSeguroDesgravamen();
            detalle.add(data3);
            Object[] data4 = new Object[2];
            data4[0] = "Seguro de Incendio"; 
            data4[1] = this.getSeguroIncendio();
            detalle.add(data4);
            Object[] data5 = new Object[2];
            data5[0] = "Seguro de Cesantía"; 
            data5[1] = this.getSeguroCesantia();
            detalle.add(data5);
            Object[] data6 = new Object[2];
            data6[0] = "Interés Penal"; 
            data6[1] = this.getInteresPenal();
            detalle.add(data6);
            Object[] data7 = new Object[2];
            data7[0] = "Gastos de Cobranza"; 
            data7[1] = this.getGastosCobranzas();
            detalle.add(data7);
            
            
            return detalle;
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

    public void setValorUf(Double valorUf) {
        this.valorUf = valorUf;
    }

    public Double getValorUf() {
        return valorUf;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }
}
