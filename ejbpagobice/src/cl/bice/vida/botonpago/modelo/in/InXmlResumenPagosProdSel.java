package cl.bice.vida.botonpago.modelo.in;

import cl.bice.vida.botonpago.common.dto.general.ResumenRequest;

public class InXmlResumenPagosProdSel {
    
    private String xml;
    private ResumenRequest req;
    private int idCanal;
    private int cargoTarjetaCredito;
    private String email;
 
    public InXmlResumenPagosProdSel() {
        super();
    }


    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getXml() {
        return xml;
    }

    public void setReq(ResumenRequest req) {
        this.req = req;
    }

    public ResumenRequest getReq() {
        return req;
    }

    public void setIdCanal(int idCanal) {
        this.idCanal = idCanal;
    }

    public int getIdCanal() {
        return idCanal;
    }

    public void setCargoTarjetaCredito(int cargoTarjetaCredito) {
        this.cargoTarjetaCredito = cargoTarjetaCredito;
    }

    public int getCargoTarjetaCredito() {
        return cargoTarjetaCredito;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
