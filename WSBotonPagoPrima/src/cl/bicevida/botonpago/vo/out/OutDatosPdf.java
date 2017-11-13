package cl.bicevida.botonpago.vo.out;

import java.io.Serializable;

public class OutDatosPdf implements Serializable{
    
    @SuppressWarnings("compatibility:-5341350815724839527")
    private static final long serialVersionUID = -5161050140974941941L;


    private String poliza;
    private String url;
    
    public OutDatosPdf() {
        super();
    }

    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }

    public String getPoliza() {
        return poliza;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
