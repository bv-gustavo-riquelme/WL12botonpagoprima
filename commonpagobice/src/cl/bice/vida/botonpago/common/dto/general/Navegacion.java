package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Tabla : (BICEVIDA.BPI_NAV_NAVEGACION_TBL)
 */
public class Navegacion implements Serializable {
    private Transacciones transacciones;
    private BpiMpgMediopagoTbl medio_pago;     
    private List detalleNavegacionCollection;
    private Integer rutPersona;
    private Date fechaHora;
    private Integer montoTransaccion;
    private Long idNavegacion;
    private Long idTransaccion;
    private Integer cod_medio;
    private Integer empresa;
    private Integer codEstado;
    private Long montoTotalTransaccion;
    private Integer ultimaPagina;

    public Navegacion() {
        super();
        this.detalleNavegacionCollection = new ArrayList();
        this.transacciones = new Transacciones();
    }

    public void addDetalleNavegacion(int index, DetalleNavegacion aDetalleNavegacion) {
        this.detalleNavegacionCollection.add(index, aDetalleNavegacion);
    }

    public void addDetalleNavegacion(DetalleNavegacion aDetalleNavegacion) {
        this.detalleNavegacionCollection.add(aDetalleNavegacion);
    }

    public List<DetalleNavegacion> getDetalleNavegacionCollection() {
        return this.detalleNavegacionCollection;
    }

    public Date getFechaHora() {        
        return this.fechaHora;
    }

    public Long getIdNavegacion() {
        return this.idNavegacion;
    }

    public Integer getMontoTransaccion() {
        return this.montoTransaccion;
    }

    public Integer getRutPersona() {
        return this.rutPersona;
    }

    public Transacciones getTransacciones() {
        return (Transacciones)this.transacciones;
    }

    public void removeDetalleNavegacion(DetalleNavegacion aDetalleNavegacion) {
        this.detalleNavegacionCollection.remove(aDetalleNavegacion);
    }

    public void setDetalleNavegacionCollection(List<DetalleNavegacion> detalleNavegacionCollection) {
        this.detalleNavegacionCollection = detalleNavegacionCollection;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = (fechaHora==null)?new Date(System.currentTimeMillis()):fechaHora;
    }

    public void setIdNavegacion(Long idNavegacion) {
        this.idNavegacion = idNavegacion;
    }

    public void setMontoTransaccion(Integer montoTransaccion) {
        this.montoTransaccion = (montoTransaccion==null)?0:montoTransaccion;
    }

    public void setRutPersona(Integer rutPersona) {
        this.rutPersona = rutPersona;
    }

    public void setTransacciones(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setCod_medio(Integer cod_medio) {
        this.cod_medio = cod_medio;
    }

    public Integer getCod_medio() {
        return cod_medio;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setCodEstado(Integer codEstado) {
        this.codEstado = codEstado;
    }

    public Integer getCodEstado() {
        return codEstado;
    }

    public void setMontoTotalTransaccion(Long montoTotalTransaccion) {
        this.montoTotalTransaccion = montoTotalTransaccion;
    }

    public Long getMontoTotalTransaccion() {
        return montoTotalTransaccion;
    }

    public void setMedio_pago(BpiMpgMediopagoTbl medio_pago) {
        this.medio_pago = medio_pago;
    }

    public BpiMpgMediopagoTbl getMedio_pago() {
        return medio_pago;
    }

    public void setUltimaPagina(Integer ultimaPagina) {
        this.ultimaPagina = ultimaPagina;
    }

    public Integer getUltimaPagina() {
        return ultimaPagina;
    }
}
