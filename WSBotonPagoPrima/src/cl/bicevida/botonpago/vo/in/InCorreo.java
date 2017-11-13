package cl.bicevida.botonpago.vo.in;

import cl.bicevida.botonpago.vo.prima.PagoDetalleCuotaVO;

import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

public class InCorreo {
    
    private Hashtable hashcuotas;
    private String urlPdfDownload;
    private String nombreCliente;
    private java.util.Date fechaTransaccion;
    private String emailCustom;
    private List<PagoDetalleCuotaVO> hashList;
    
    public InCorreo() {
        super();
    }


    public void setHashcuotas(Hashtable hashcuotas) {
        this.hashcuotas = hashcuotas;
    }

    public Hashtable getHashcuotas() {
        return hashcuotas;
    }

    public void setUrlPdfDownload(String urlPdfDownload) {
        this.urlPdfDownload = urlPdfDownload;
    }

    public String getUrlPdfDownload() {
        return urlPdfDownload;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setEmailCustom(String emailCustom) {
        this.emailCustom = emailCustom;
    }

    public String getEmailCustom() {
        return emailCustom;
    }


    public void setHashList(List<PagoDetalleCuotaVO> hashList) {
        this.hashList = hashList;
    }

    public List<PagoDetalleCuotaVO> getHashList() {
        return hashList;
    }


}
