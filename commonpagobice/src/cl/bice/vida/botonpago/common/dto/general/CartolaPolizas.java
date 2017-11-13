package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

/**
 * Vista :  (BICEVIDA.BPI_DPP_DETALLEPOLPAGADAS_VW)
 */
public class CartolaPolizas  implements Serializable {
    
    private Long idComprobpago;
    private Long rutCliente;
    private Date fechaEstado;
    private String estado;
    private Double valorUf;
    private Integer idCanalPago;
    private String canalPago;
    private Date fechaPago;
    private Integer tipoComprobante;
    private Long idRamo;
    private Long polizaPol;
    private Long folioRecibo;
    private Integer codigoProducto;
    private String nombreProducto;
    private Double primaBrutaUf;
    private Date fechaVencimiento;
    private Long montoPagadoPesos;    

    public CartolaPolizas() {
        super();
    }
  

    public String getEstado() {
        return this.estado;
    }

    public Date getFechaEstado() {
        return this.fechaEstado;
    }

    public Date getFechaPago() {
        return this.fechaPago;
    }

    public Integer getIdCanalPago() {
        return this.idCanalPago;
    }

    public Long getIdComprobpago() {
        return this.idComprobpago;
    }

    public Long getRutCliente() {
        return this.rutCliente;
    }

    public Integer getTipoComprobante() {
        return this.tipoComprobante;
    }

    public Double getValorUf() {
        return this.valorUf;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaEstado(Date fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setIdCanalPago(Integer idCanalPago) {
        this.idCanalPago = idCanalPago;
    }

    public void setIdComprobpago(Long idComprobpago) {
        this.idComprobpago = idComprobpago;
    }

    public void setRutCliente(Long rutCliente) {
        this.rutCliente = rutCliente;
    }

    public void setTipoComprobante(Integer tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public void setValorUf(Double valorUf) {
        this.valorUf = valorUf;
    }

    public void setIdRamo(Long idRamo) {
        this.idRamo = idRamo;
    }

    public Long getIdRamo() {
        return idRamo;
    }

    public void setPolizaPol(Long polizaPol) {
        this.polizaPol = polizaPol;
    }

    public Long getPolizaPol() {
        return polizaPol;
    }

    public void setFolioRecibo(Long folioRecibo) {
        this.folioRecibo = folioRecibo;
    }

    public Long getFolioRecibo() {
        return folioRecibo;
    }

    public void setCodigoProducto(Integer codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public Integer getCodigoProducto() {
        return codigoProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setPrimaBrutaUf(Double primaBrutaUf) {
        this.primaBrutaUf = primaBrutaUf;
    }

    public Double getPrimaBrutaUf() {
        return primaBrutaUf;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setMontoPagadoPesos(Long montoPagadoPesos) {
        this.montoPagadoPesos = montoPagadoPesos;
    }

    public Long getMontoPagadoPesos() {
        return montoPagadoPesos;
    }

    public void setCanalPago(String canalPago) {
        this.canalPago = canalPago;
    }

    public String getCanalPago() {
        return canalPago;
    }
}
