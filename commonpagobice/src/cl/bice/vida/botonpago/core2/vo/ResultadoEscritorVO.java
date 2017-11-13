package cl.bice.vida.botonpago.core2.vo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


public class ResultadoEscritorVO implements Serializable {
    private boolean ok;
    private String nombreProceso;
    private Exception error;
    private int numeroTransaccionesProcesadas;
    private int numeroTransaccionesError;
    private List<ResultadoErrorVO> errores;
    private FileStructureVo descargaInfo;
    private String filenameDownload;
    private long totalBytesFileDownload;
    private String remoteuser;
    
    public ResultadoEscritorVO() {
    }


    public List<ResultadoErrorVO> getErrores() {
        return errores;
    }
    public void addError(Long elinea, String eDescripcion) {
        if (getErrores() == null) errores = new ArrayList();
        ResultadoErrorVO er = new ResultadoErrorVO();
        er.setNumeroLineaFila(elinea);
        er.setDetalleError(eDescripcion);
        errores.add(er);
    }


    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public Exception getError() {
        return error;
    }

    public void setNumeroTransaccionesProcesadas(int numeroTransaccionesProcesadas) {
        this.numeroTransaccionesProcesadas = numeroTransaccionesProcesadas;
    }

    public int getNumeroTransaccionesProcesadas() {
        return numeroTransaccionesProcesadas;
    }

    public void setNumeroTransaccionesError(int numeroTransaccionesError) {
        this.numeroTransaccionesError = numeroTransaccionesError;
    }

    public int getNumeroTransaccionesError() {
        return numeroTransaccionesError;
    }

    public void setErrores(List<ResultadoErrorVO> errores) {
        this.errores = errores;
    }

    public void setDescargaInfo(FileStructureVo descargaInfo) {
        this.descargaInfo = descargaInfo;
    }

    public FileStructureVo getDescargaInfo() {
        return descargaInfo;
    }

    public void setFilenameDownload(String filenameDownload) {
        this.filenameDownload = filenameDownload;
    }

    public String getFilenameDownload() {
        return filenameDownload;
    }

    public void setTotalBytesFileDownload(long totalBytesFileDownload) {
        this.totalBytesFileDownload = totalBytesFileDownload;
    }

    public long getTotalBytesFileDownload() {
        return totalBytesFileDownload;
    }

    public void setRemoteuser(String remoteuser) {
        this.remoteuser = remoteuser;
    }

    public String getRemoteuser() {
        return remoteuser;
    }
}
