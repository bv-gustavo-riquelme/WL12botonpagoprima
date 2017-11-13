package cl.bice.vida.botonpago.common.dto.general;

import cl.bice.vida.botonpago.common.dto.parametros.Empresas;

import cl.bice.vida.botonpago.common.dto.parametros.MedioPago;

import java.io.Serializable;

import java.util.Date;

/**
 * Tabla : (BICEVIDA.BPI_DTE_DETTRANSEMP_TBL)
 */
public class DetalleTransaccionByEmp  implements Serializable {

    /**Map empresas <-> bicevida.datamodel.Empresas
     * @associates <{bicevida.datamodel.Empresas}>
     */
    private Empresas empresas;

    /**Map medioPago <-> bicevida.datamodel.MedioPago
     * @associates <{bicevida.datamodel.MedioPago}>
     */
    private MedioPago medioPago;

    /**Map transacciones <-> bicevida.datamodel.Transacciones
     * @associates <{bicevida.datamodel.Transacciones}>
     */
    private Transacciones transacciones;
    private Integer codEmpresa;
    private Long idTransaccion;
    private Integer montoTotal;
    private Long numTransaccionCaja;
    private Long numTransaccionMedio;
    private Date fechahora;
    private Integer codMedio;

    public DetalleTransaccionByEmp() {
        super();
        this.empresas = new Empresas();
        this.medioPago = new MedioPago();
        this.transacciones = new Transacciones();
    }

    public Integer getCodEmpresa() {
        return this.codEmpresa;
    }

    public Empresas getEmpresas() {
        return (Empresas)this.empresas;
    }

    public Date getFechahora() {
        return this.fechahora;
    }

    public Long getIdTransaccion() {
        return this.idTransaccion;
    }

    public MedioPago getMedioPago() {
        return (MedioPago)this.medioPago;
    }

    public Integer getMontoTotal() {
        return this.montoTotal;
    }

    public Long getNumTransaccionCaja() {
        return this.numTransaccionCaja;
    }

    public Long getNumTransaccionMedio() {
        return this.numTransaccionMedio;
    }

    public Transacciones getTransacciones() {
        return (Transacciones)this.transacciones;
    }

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public void setFechahora(Date fechahora) {
        this.fechahora = fechahora;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public void setMontoTotal(Integer montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setNumTransaccionCaja(Long numTransaccionCaja) {
        this.numTransaccionCaja = numTransaccionCaja;
    }

    public void setNumTransaccionMedio(Long numTransaccionMedio) {
        this.numTransaccionMedio = numTransaccionMedio;
    }

    public void setTransacciones(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public void setCodMedio(Integer codMedio) {
        this.codMedio = codMedio;
    }

    public Integer getCodMedio() {
        return codMedio;
    }
}
