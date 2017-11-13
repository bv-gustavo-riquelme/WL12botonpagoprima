package cl.bicevida.botonpago.servicelocator;


public class ServiceLocatorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ServiceLocatorException() {
    }

    public ServiceLocatorException(String msg) {
        super(msg);
    }

    public ServiceLocatorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ServiceLocatorException(Throwable cause) {
        super(cause);
    }

}
