package cl.bice.vida.botonpago.core2.vo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


public class ResultadoProcesoVO implements Serializable {
    private boolean ok;
    private String nombreProceso;
    private Exception error;
    private int numeroTransaccionesProcesadas;
    private int numeroTransaccionesError;
    private List<LectorDinamicoVO> transacciones;
    private List<ResultadoErrorVO> errores;
    private FileStructureVo cargaInfo;
    private String filenameUpload;
    private String filenameDownload;
    private String filenameSaveServerToProcess;
    private long totalBytesFile;
    private String remoteuser;
    
    public ResultadoProcesoVO() {
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

    public void setTransacciones(List<LectorDinamicoVO> transacciones) {
        this.transacciones = transacciones;
    }

    public List<LectorDinamicoVO> getTransacciones() {
        return transacciones;
    }
    
    public void addTransaccion(LectorDinamicoVO vo) {
        if (getTransacciones() == null) transacciones = new ArrayList();
        transacciones.add(vo);
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

    public void setErrores(List<ResultadoErrorVO> errores) {
        this.errores = errores;
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

    public void setCargaInfo(FileStructureVo cargaInfo) {
        this.cargaInfo = cargaInfo;
    }

    public FileStructureVo getCargaInfo() {
        return cargaInfo;
    }

    public void setFilenameUpload(String filenameUpload) {
        this.filenameUpload = filenameUpload;
    }

    public String getFilenameUpload() {
        return filenameUpload;
    }

    public void setFilenameSaveServerToProcess(String filenameSaveServerToProcess) {
        this.filenameSaveServerToProcess = filenameSaveServerToProcess;
    }

    public String getFilenameSaveServerToProcess() {
        return filenameSaveServerToProcess;
    }

    public void setTotalBytesFile(long totalBytesFile) {
        this.totalBytesFile = totalBytesFile;
    }

    public long getTotalBytesFile() {
        return totalBytesFile;
    }

    public void setRemoteuser(String remoteuser) {
        this.remoteuser = remoteuser;
    }

    public String getRemoteuser() {
        return remoteuser;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setFilenameDownload(String filenameDownload) {
        this.filenameDownload = filenameDownload;
    }

    public String getFilenameDownload() {
        return filenameDownload;
    }
}
