package cl.bicevida.botonpago.exception;

public class PagoPublicoCoreException extends Exception {

    public PagoPublicoCoreException() {
        super();
    }

    public PagoPublicoCoreException(String message) {
        super(message);
    }

    public PagoPublicoCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public PagoPublicoCoreException(Throwable cause) {
        super(cause);
    }
}
