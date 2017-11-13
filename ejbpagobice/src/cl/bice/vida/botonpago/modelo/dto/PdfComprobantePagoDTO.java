package cl.bice.vida.botonpago.modelo.dto;

import java.io.Serializable;

public class PdfComprobantePagoDTO implements Serializable{
    public PdfComprobantePagoDTO() {
    }
    
    private String rut;
    private String url;
    private String[] idpdf;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setIdpdf(String[] idpdf) {
        this.idpdf = idpdf;
    }

    public String[] getIdpdf() {
        return idpdf;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getRut() {
        return rut;
    }
}
