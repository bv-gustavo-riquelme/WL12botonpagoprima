package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

/**
 * Tabla : BICEVIDA.BPI_APC_APCAJA_TBL
 */
public class AperturaCaja implements Serializable {

    /**Map transaccionesCollection <-> bicevida.datamodel.Transacciones
     * @associates <{bicevida.datamodel.Transacciones}>
     */
    
    private Date turno;         //BICEVIDA.BPI_APC_APCAJA_TBL.TURNO
    private Date fechaCierre;   //BICEVIDA.BPI_APC_APCAJA_TBL.FECHA_CIERRE
    private Double operaciones; //BICEVIDA.BPI_APC_APCAJA_TBL.OPERACIONES
    private Double montoTotal;  //BICEVIDA.BPI_APC_APCAJA_TBL.MONTO_TOTAL

    public AperturaCaja() {
        super();
    }
     

    public Date getFechaCierre() {
        return this.fechaCierre;
    }

    public Double getMontoTotal() {
        return this.montoTotal;
    }

    public Double getOperaciones() {
        return this.operaciones;
    }

    public Date getTurno() {
        return this.turno;
    }
    
    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setOperaciones(Double operaciones) {
        this.operaciones = operaciones;
    }

    public void setTurno(Date turno) {
        this.turno = turno;
    }

}
