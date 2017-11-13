package cl.bice.vida.botonpago.core.vo;

import cl.bice.vida.botonpago.core.comparator.TransaccionRendicionVOComparator;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;


public class ResultadoProcesoVO implements Serializable {
    private boolean ok = false;
    private String medioPago = "";
    private String estadoValueOk = null;
    private Exception error = null;
    private int numeroTransaccionesProcesadas = 0;
    private int numeroTransaccionesOK  = 0;
    private int numeroTransaccionesError  = 0;
    private int numeroTransaccionesNotFound  = 0;
    private long montoTransaccionesOK  = 0;
    private long montoTransaccionesError  = 0;
    private long montoTransaccionesProcesadas  = 0;
    private long montoTransaccionesNotFound  = 0;
    private Hashtable keys = null;
    
    public ResultadoProcesoVO() {
        keys = new Hashtable();    
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setNumeroTransaccionesProcesadas(int numeroTransaccionesProcesadas) {
        this.numeroTransaccionesProcesadas = numeroTransaccionesProcesadas;
    }

    public int getNumeroTransaccionesProcesadas() {
        return keys.size();
    }

    public void setNumeroTransaccionesOK(int numeroTransaccionesOK) {
        this.numeroTransaccionesOK = numeroTransaccionesOK;
    }

    public int getNumeroTransaccionesOK() {
        return numeroTransaccionesOK;
    }

    public void setNumeroTransaccionesError(int numeroTransaccionesError) {
        this.numeroTransaccionesError = numeroTransaccionesError;
    }

    public int getNumeroTransaccionesError() {
        return numeroTransaccionesError;
    }

    public List<TransaccionRendicionVO> getTransacciones() {
        List lista = new ArrayList(keys.values());
        Collections.sort( lista, new TransaccionRendicionVOComparator() );
        return lista;
    }
    
    /**
     * Agrega una transaccion al detalle de procesamiento
     * @param vo
     */
    public void addTransaccion(TransaccionRendicionVO vo) {
        keys.put("PK"+vo.getNumeroOrdenBice(), vo);        
    }

    public int getCuantasTransacciones() {
        return keys.size();
    }
    /**
     * Quita una transaccion del detalle de procesamiento
     * @param vo
     */
    public void removeTransaccion(TransaccionRendicionVO vo) {
        keys.remove("PK"+vo.getNumeroOrdenBice());
    }
    
    /**
     * Localiza una transaccion
     * @param numeroOrdenBice
     * @return
     */
    public TransaccionRendicionVO getTransaccionById(String numeroOrdenBice) {
        TransaccionRendicionVO trx = (TransaccionRendicionVO) keys.get("PK"+numeroOrdenBice);
        return trx;
    }
    
    public void setError(Exception error) {
        this.error = error;
    }

    public Exception getError() {
        return error;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    public void setMontoTransaccionesOK(long montoTransaccionesOK) {
        this.montoTransaccionesOK = montoTransaccionesOK;
    }

    public long getMontoTransaccionesOK() {
        return montoTransaccionesOK;
    }

    public void setMontoTransaccionesError(long montoTransaccionesError) {
        this.montoTransaccionesError = montoTransaccionesError;
    }

    public long getMontoTransaccionesError() {
        return montoTransaccionesError;
    }

    public void setMontoTransaccionesProcesadas(long montoTransaccionesProcesadas) {
        this.montoTransaccionesProcesadas = montoTransaccionesProcesadas;
    }

    public long getMontoTransaccionesProcesadas() {
        return montoTransaccionesProcesadas;
    }

    public void setNumeroTransaccionesNotFound(int numeroTransaccionesNotFound) {
        this.numeroTransaccionesNotFound = numeroTransaccionesNotFound;
    }

    public int getNumeroTransaccionesNotFound() {        
        return numeroTransaccionesNotFound;
    }

    public void setMontoTransaccionesNotFound(long montoTransaccionesNotFound) {
        this.montoTransaccionesNotFound = montoTransaccionesNotFound;
    }

    public long getMontoTransaccionesNotFound() {
        return montoTransaccionesNotFound;
    }

    public void setEstadoValueOk(String estadoValueOk) {
        this.estadoValueOk = estadoValueOk;
    }

    public String getEstadoValueOk() {
        return estadoValueOk;
    }
}
