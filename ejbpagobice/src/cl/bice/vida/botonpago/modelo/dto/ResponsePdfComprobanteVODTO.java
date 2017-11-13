package cl.bice.vida.botonpago.modelo.dto;


import java.io.Serializable;

public class ResponsePdfComprobanteVODTO implements Serializable{
    
    public ResponsePdfComprobanteVODTO() {
    }
    
    private boolean exito;
    private String messageerror;
    private Integer coderror;
    private String mngexception;
    private PdfComprobantePagoDTO datoPdfComprobante;

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public boolean isExito() {
        return exito;
    }

    public void setMessageerror(String messageerror) {
        this.messageerror = messageerror;
    }

    public String getMessageerror() {
        return messageerror;
    }

    public void setCoderror(Integer coderror) {
        this.coderror = coderror;
    }

    public Integer getCoderror() {
        return coderror;
    }

    public void setMngexception(String mngexception) {
        this.mngexception = mngexception;
    }

    public String getMngexception() {
        return mngexception;
    }

    public void setDatoPdfComprobante(PdfComprobantePagoDTO datoPdfComprobante) {
        this.datoPdfComprobante = datoPdfComprobante;
    }

    public PdfComprobantePagoDTO getDatoPdfComprobante() {
        return datoPdfComprobante;
    }
}
