package cl.bicevida.botonpago.vo.in;

public class PDFPoliza {
    
    private String[] idPdf;
    private String urlPDF;
    private String rutCliente;
    private Integer poliza;
    private Integer ramo;
    
    public PDFPoliza() {
        super();
    }

    public void setIdPdf(String[] idPdf) {
        this.idPdf = idPdf;
    }

    public String[] getIdPdf() {
        return idPdf;
    }

    public void setUrlPDF(String urlPDF) {
        this.urlPDF = urlPDF;
    }

    public String getUrlPDF() {
        return urlPDF;
    }

    public void setRutCliente(String rutCliente) {
        this.rutCliente = rutCliente;
    }

    public String getRutCliente() {
        return rutCliente;
    }

    public void setPoliza(Integer poliza) {
        this.poliza = poliza;
    }

    public Integer getPoliza() {
        return poliza;
    }

    public void setRamo(Integer ramo) {
        this.ramo = ramo;
    }

    public Integer getRamo() {
        return ramo;
    }

}
