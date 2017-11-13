package cl.bice.vida.botonpago.common.vo;

import cl.bice.vida.botonpago.common.dto.general.BpiDitribFondosAPVTbl;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

public class PagoDetalleCuotaVO implements Serializable {
    private int idCuota;
    private Date fechaTransaccion;
    private Date fechaVencimiento;
    private String estado;
    private Double valorEnPesos;
    private Double valorEnUF;
    private String descripcionProducto;
    private String tipoProducto;
    private int codigoProducto;
    private int numeroProducto;
    private Double totalCuota;
    private Double saldoFavor;
    private int tipoCuota;
    private List<BpiDitribFondosAPVTbl> distribucionFondos;
    
    
    public PagoDetalleCuotaVO() {
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setValorEnPesos(Double valorEnPesos) {
        this.valorEnPesos = valorEnPesos;
    }

    public Double getValorEnPesos() {
        return valorEnPesos;
    }

    public void setValorEnUF(Double valorEnUF) {
        this.valorEnUF = valorEnUF;
    }

    public Double getValorEnUF() {
        return valorEnUF;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setNumeroProducto(int numeroProducto) {
        this.numeroProducto = numeroProducto;
    }

    public int getNumeroProducto() {
        return numeroProducto;
    }

    public void setTotalCuota(Double totalCuota) {
        this.totalCuota = totalCuota;
    }

    public Double getTotalCuota() {
        return totalCuota;
    }

    public void setSaldoFavor(Double saldoFavor) {
        this.saldoFavor = saldoFavor;
    }

    public Double getSaldoFavor() {
        return saldoFavor;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setTipoCuota(int tipoCuota) {
        this.tipoCuota = tipoCuota;
    }

    public int getTipoCuota() {
        return tipoCuota;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setDistribucionFondos(List<BpiDitribFondosAPVTbl> distribucionFondos) {
        this.distribucionFondos = distribucionFondos;
    }

    public List<BpiDitribFondosAPVTbl> getDistribucionFondos() {
        return distribucionFondos;
    }
}

