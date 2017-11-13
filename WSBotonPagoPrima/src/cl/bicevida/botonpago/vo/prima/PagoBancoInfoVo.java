package cl.bicevida.botonpago.vo.prima;

import cl.bicevida.botonpago.vo.DataBanco;

import java.io.Serializable;


public class PagoBancoInfoVo implements Serializable {

    private String xmlBancoDataPago;
    private String xmlUrlRedirect;
    private String xmlUrlParametroRedirect;
    private String idTransaccionBanco;    
    private String bancoNombre;    
    private String htmlHiddenPago;
    
    private DataBanco[] newHiddenParam;
    
    //private String paramName;
    
    public PagoBancoInfoVo() {
    }

    public void setXmlBancoDataPago(String xmlBancoDataPago) {
        this.xmlBancoDataPago = xmlBancoDataPago;
    }

    public String getXmlBancoDataPago() {
        return xmlBancoDataPago;
    }

    public void setXmlUrlRedirect(String xmlUrlRedirect) {
        this.xmlUrlRedirect = xmlUrlRedirect;
    }

    public String getXmlUrlRedirect() {
        return xmlUrlRedirect;
    }
    
    public void setXmlUrlParametroRedirect(String xmlUrlParametroRedirect) {
        this.xmlUrlParametroRedirect = xmlUrlParametroRedirect;
    }

    public String getXmlUrlParametroRedirect() {
        return xmlUrlParametroRedirect;
    }

    public void setIdTransaccionBanco(String idTransaccionBanco) {
        this.idTransaccionBanco = idTransaccionBanco;
    }

    public String getIdTransaccionBanco() {
        return idTransaccionBanco;
    }

    public void setBancoNombre(String bancoNombre) {
        this.bancoNombre = bancoNombre;
    }

    public String getBancoNombre() {
        return bancoNombre;
    }

    public void setHtmlHiddenPago(String htmlHiddenPago) {
        this.htmlHiddenPago = htmlHiddenPago;
    }

    public String getHtmlHiddenPago() {
        return htmlHiddenPago;
    }


    public void setNewHiddenParam(DataBanco[] newHiddenParam) {
        this.newHiddenParam = newHiddenParam;
    }

    public DataBanco[] getNewHiddenParam() {
        return newHiddenParam;
    }


}
