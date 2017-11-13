package cl.bice.vida.botonpago.common.exception;

public class BiceException extends Exception {

    public BiceException() {
            super();
    }

    public BiceException(String message, Throwable cause) {
            super(message, cause);
    }

    public BiceException(String message) {
            super(message);
    }

    public BiceException(Throwable cause) {
            super(cause);
    }    
}
