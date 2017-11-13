package cl.bicevida.botonpago.vo.out;

public class OutMensaje {
    
    private String mensaje;
    private String codigo;
    public OutMensaje() {
        super();
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
