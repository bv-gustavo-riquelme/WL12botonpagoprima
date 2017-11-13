package cl.bice.vida.botonpago.common.dto.general;

import cl.bice.vida.botonpago.common.dto.parametros.Estados;
import cl.bice.vida.botonpago.common.dto.parametros.MedioPago;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Tabla : (BICEVIDA.BPI_TRA_TRANSACCIONES_TBL)
 */
public class Transacciones  implements Serializable {


    /**Map aperturaCaja <-> bicevida.datamodel.AperturaCaja
     * @associates <{bicevida.datamodel.AperturaCaja}>
     */
    private AperturaCaja aperturaCaja;

    /**Map estados <-> bicevida.datamodel.Estados
     * @associates <{bicevida.datamodel.Estados}>
     */
    private Estados estados;

    /**Map medioPago <-> bicevida.datamodel.MedioPago
     * @associates <{bicevida.datamodel.MedioPago}>
     */
    private MedioPago medioPago;


    /**Map detalleTransaccionByEmpCollection <-> bicevida.datamodel.DetalleTransaccionByEmp
     * @associates <{bicevida.datamodel.DetalleTransaccionByEmp}>
     */
    private List detalleTransaccionByEmpCollection;
    
     /**Map detalleTransaccionByEmpCollection <-> bicevida.datamodel.Comprobantes
      * @associates <{bicevida.datamodel.Comprobantes}>
      */
     private List comprobantesCollection;

    private Integer cod_estado;
    private Integer cod_medioPago;
    private Integer cod_banco2MedioPago;
    private Date turno;
    private Long idTransaccion;
    private Integer montoTotal;
    private Date fechainicio;
    private Integer rutPersona;
    private String nombrePersona;
    private Date fechafin;
    private Long numtransaccionmedio;
    private String tipotransaccion;
    private Date dispfondos;
    private Integer cargo;
    private String trxUsadaporMDB;
    private Long folioCaja;

    public Transacciones() {
        super();
        this.aperturaCaja = new AperturaCaja();        
        this.detalleTransaccionByEmpCollection = new ArrayList();
        this.comprobantesCollection = new ArrayList();
        this.estados = new Estados();
        this.medioPago = new MedioPago();
    }

    public void addDetalleTransaccionByEmp(DetalleTransaccionByEmp aDetalleTransaccionByEmp) {
        this.detalleTransaccionByEmpCollection.add(aDetalleTransaccionByEmp);
        aDetalleTransaccionByEmp.setTransacciones(this);
    }

    public void addDetalleTransaccionByEmp(int index, 
                                           DetalleTransaccionByEmp aDetalleTransaccionByEmp) {
        this.detalleTransaccionByEmpCollection.add(index, 
                                                   aDetalleTransaccionByEmp);
        aDetalleTransaccionByEmp.setTransacciones(this);
    }
        
    public AperturaCaja getAperturaCaja() {
        return (AperturaCaja)this.aperturaCaja;
    }

    
    public List<DetalleTransaccionByEmp> getDetalleTransaccionByEmpCollection() {
        return this.detalleTransaccionByEmpCollection;
    }
    
    public List<Comprobantes> getComprobantesCollection() {
        return this.comprobantesCollection;
    }
    
    
    public Estados getEstados() {
        return (Estados)this.estados;
    }

    public Date getFechafin() {
        return this.fechafin;
    }

    public Date getFechainicio() {
        return this.fechainicio;
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

    public String getNombrePersona() {
        return this.nombrePersona;
    }

    public Integer getRutPersona() {
        return this.rutPersona;
    }

    

    public void removeDetalleTransaccionByEmp(DetalleTransaccionByEmp aDetalleTransaccionByEmp) {
        this.detalleTransaccionByEmpCollection.remove(aDetalleTransaccionByEmp);
    }
    

    public void removeComprobante(Comprobantes aComprobante) {
        this.comprobantesCollection.remove(aComprobante);
    }

    public void setAperturaCaja(AperturaCaja aperturaCaja) {
        this.aperturaCaja = aperturaCaja;
    }

    
    public void setDetalleTransaccionByEmpCollection(List<DetalleTransaccionByEmp> detalleTransaccionByEmpCollection) {
        this.detalleTransaccionByEmpCollection = 
                detalleTransaccionByEmpCollection;
    }
    
    public void setComprobantesCollection(List<Comprobantes> comprobantesCollection) {
        this.comprobantesCollection = 
                comprobantesCollection;
    }

    
    public void setEstados(Estados estados) {
        this.estados = estados;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = (fechainicio==null)?new Date(System.currentTimeMillis()):fechainicio;
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
    
    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public void setRutPersona(Integer rutPersona) {
        this.rutPersona = rutPersona;
    }

    public void setCod_estado(Integer cod_estado) {
        this.cod_estado = cod_estado;
    }

    public Integer getCod_estado() {
        return cod_estado;
    }

    public void setCod_medioPago(Integer cod_medioPago) {
        this.cod_medioPago = cod_medioPago;
    }

    public Integer getCod_medioPago() {
        return cod_medioPago;
    }
    
    public void setCod_banco2MedioPago(Integer cod_banco2MedioPago) {
        this.cod_banco2MedioPago = cod_banco2MedioPago;
    }

    public Integer getCod_banco2MedioPago() {
        return cod_banco2MedioPago;
    }

    public void setTurno(Date turno) {
        this.turno = turno;
    }

    public Date getTurno() {
        return turno;
    }

    public void setNumtransaccionmedio(Long numtransaccionmedio) {
        this.numtransaccionmedio = numtransaccionmedio;
    }

    public Long getNumtransaccionmedio() {
        return numtransaccionmedio;
    }

    public void setTipotransaccion(String tipotransaccion) {
        this.tipotransaccion = tipotransaccion;
    }

    public String getTipotransaccion() {
        return tipotransaccion;
    }

    public void setDispfondos(Date dispfondos) {
        this.dispfondos = dispfondos;
    }

    public Date getDispfondos() {
        return dispfondos;
    }

    public void setCargo(Integer cargo) {
        this.cargo = cargo;
    }

    public Integer getCargo() {
        return cargo;
    }

    public void setTrxUsadaporMDB(String trxUsadaporMDB) {
        this.trxUsadaporMDB = trxUsadaporMDB;
    }

    public String getTrxUsadaporMDB() {
        return trxUsadaporMDB;
    }

    public void setFolioCaja(Long folioCaja) {
        this.folioCaja = folioCaja;
    }

    public Long getFolioCaja() {
        return folioCaja;
    }
}
