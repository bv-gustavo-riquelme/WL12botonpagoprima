package cl.bice.vida.botonpago.modelo.in;

public class InCrearConfimarcionXmlSel {
    
    private String xmlResumenSeleccionado;
    private int idCanal;
    private String email;
    private int tipoTransaccion;

    public InCrearConfimarcionXmlSel() {
        super();
    }

    public void setXmlResumenSeleccionado(String xmlResumenSeleccionado) {
        this.xmlResumenSeleccionado = xmlResumenSeleccionado;
    }

    public String getXmlResumenSeleccionado() {
        return xmlResumenSeleccionado;
    }

    public void setIdCanal(int idCanal) {
        this.idCanal = idCanal;
    }

    public int getIdCanal() {
        return idCanal;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setTipoTransaccion(int tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public int getTipoTransaccion() {
        return tipoTransaccion;
    }
}
