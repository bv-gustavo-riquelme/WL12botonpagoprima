package cl.bice.vida.botonpago.modelo.in;

public class InXMLSeleccionadoAPT {

    private String xmlResumenSeleccionado;
    private int idCanal;
    private int tipoTransaccion;
    private int cargoExtra;
    private String email;

    public InXMLSeleccionadoAPT() {
        super();
    }

    public void s(String xmlResumenSeleccionado) {
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

    public void setTipoTransaccion(int tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public int getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setCargoExtra(int cargoExtra) {
        this.cargoExtra = cargoExtra;
    }

    public int getCargoExtra() {
        return cargoExtra;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
