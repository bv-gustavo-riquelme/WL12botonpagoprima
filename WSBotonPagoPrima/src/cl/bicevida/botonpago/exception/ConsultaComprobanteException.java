package cl.bicevida.botonpago.exception;

public class ConsultaComprobanteException extends Exception {

    public ConsultaComprobanteException() {
        super();
    }

    public ConsultaComprobanteException(String message) {
        super(message);
    }

    public ConsultaComprobanteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsultaComprobanteException(Throwable cause) {
        super(cause);
    }
}