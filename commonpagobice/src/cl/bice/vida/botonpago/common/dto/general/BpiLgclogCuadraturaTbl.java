package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

public class BpiLgclogCuadraturaTbl implements Serializable {
    private Long idCuadratura;
    private Long codMedio;
    private Long codEmpresa;
    private Date fechaCuadratura;
    private String archivo;
    private Long numeroIntentos;
    private String estado; //OK, NOK
    private Long numeroRegistros;
    private Long numeroCuadrados;
    private Long montoCuadrados;
    private Long numeroNoCuadrados;
    private Long montoNoCuadrados;
    private Long numeroReversados;
    private Long montoReversados;
    private Long numeroNoHubicados;
    private Long numeroNoProcesados;
    private Long montoNoProcesados;

    public void setIdCuadratura(Long idCuadratura) {
        this.idCuadratura = idCuadratura;
    }

    public Long getIdCuadratura() {
        return idCuadratura;
    }

    public void setCodMedio(Long codMedio) {
        this.codMedio = codMedio;
    }

    public Long getCodMedio() {
        return codMedio;
    }

    public void setCodEmpresa(Long codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Long getCodEmpresa() {
        return codEmpresa;
    }

    public void setFechaCuadratura(Date fechaCuadratura) {
        this.fechaCuadratura = fechaCuadratura;
    }

    public Date getFechaCuadratura() {
        return fechaCuadratura;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setNumeroIntentos(Long numeroIntentos) {
        this.numeroIntentos = numeroIntentos;
    }

    public Long getNumeroIntentos() {
        return numeroIntentos;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setNumeroRegistros(Long numeroRegistros) {
        this.numeroRegistros = numeroRegistros;
    }

    public Long getNumeroRegistros() {
        return numeroRegistros;
    }

    public void setNumeroCuadrados(Long numeroCuadrados) {
        this.numeroCuadrados = numeroCuadrados;
    }

    public Long getNumeroCuadrados() {
        return numeroCuadrados;
    }

    public void setMontoCuadrados(Long montoCuadrados) {
        this.montoCuadrados = montoCuadrados;
    }

    public Long getMontoCuadrados() {
        return montoCuadrados;
    }

    public void setNumeroNoCuadrados(Long numeroNoCuadrados) {
        this.numeroNoCuadrados = numeroNoCuadrados;
    }

    public Long getNumeroNoCuadrados() {
        return numeroNoCuadrados;
    }

    public void setMontoNoCuadrados(Long montoNoCuadrados) {
        this.montoNoCuadrados = montoNoCuadrados;
    }

    public Long getMontoNoCuadrados() {
        return montoNoCuadrados;
    }

    public void setNumeroReversados(Long numeroReversados) {
        this.numeroReversados = numeroReversados;
    }

    public Long getNumeroReversados() {
        return numeroReversados;
    }

    public void setMontoReversados(Long montoReversados) {
        this.montoReversados = montoReversados;
    }

    public Long getMontoReversados() {
        return montoReversados;
    }

    public void setNumeroNoHubicados(Long numeroNoHubicados) {
        this.numeroNoHubicados = numeroNoHubicados;
    }

    public Long getNumeroNoHubicados() {
        return numeroNoHubicados;
    }

    public void setNumeroNoProcesados(Long numeroNoProcesados) {
        this.numeroNoProcesados = numeroNoProcesados;
    }

    public Long getNumeroNoProcesados() {
        return numeroNoProcesados;
    }

    public void setMontoNoProcesados(Long montoNoProcesados) {
        this.montoNoProcesados = montoNoProcesados;
    }

    public Long getMontoNoProcesados() {
        return montoNoProcesados;
    }
}
