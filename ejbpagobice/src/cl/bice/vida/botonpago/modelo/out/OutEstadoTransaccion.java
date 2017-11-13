package cl.bice.vida.botonpago.modelo.out;

public class OutEstadoTransaccion {
    
    private Boolean estadoTransaccion;
    public OutEstadoTransaccion() {
        super();
    }

    public void setEstadoTransaccion(Boolean estadoTransaccion) {
        this.estadoTransaccion = estadoTransaccion;
    }

    public Boolean getEstadoTransaccion() {
        return estadoTransaccion;
    }
}
