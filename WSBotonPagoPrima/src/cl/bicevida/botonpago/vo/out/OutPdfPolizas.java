package cl.bicevida.botonpago.vo.out;

public class OutPdfPolizas {
    
    private String linkPdfByRamoPoliza;
    private String mensajeRespuesta;
    private Integer codigo;
    
    public OutPdfPolizas() {
        super();
    }

    public void setLinkPdfByRamoPoliza(String linkPdfByRamoPoliza) {
        this.linkPdfByRamoPoliza = linkPdfByRamoPoliza;
    }

    public String getLinkPdfByRamoPoliza() {
        return linkPdfByRamoPoliza;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }
}
