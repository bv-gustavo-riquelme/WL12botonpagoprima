package cl.bicevida.botonpago.exception;

public class ConsultaDeudaException extends Exception {

    public ConsultaDeudaException() {
        super();
    }

    public ConsultaDeudaException(String message) {
        super(message);
    }

    public ConsultaDeudaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsultaDeudaException(Throwable cause) {
        super(cause);
    }
}