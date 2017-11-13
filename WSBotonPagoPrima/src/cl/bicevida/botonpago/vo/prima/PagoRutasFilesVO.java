package cl.bicevida.botonpago.vo.prima;


public class PagoRutasFilesVO {
    private String pathRelativo;
    private String pathAbsoluto;
    private String pathAlternativo;
    public PagoRutasFilesVO() {
    }

    public void setPathRelativo(String pathRelativo) {
        this.pathRelativo = pathRelativo;
    }

    public String getPathRelativo() {
        return pathRelativo;
    }

    public void setPathAbsoluto(String pathAbsoluto) {
        this.pathAbsoluto = pathAbsoluto;
    }

    public String getPathAbsoluto() {
        return pathAbsoluto;
    }

    public void setPathAlternativo(String pathAlternativo) {
        this.pathAlternativo = pathAlternativo;
    }

    public String getPathAlternativo() {
        return pathAlternativo;
    }
}
