package cl.bice.vida.botonpago.core2.lector.exception;

public class LecturaException extends Exception {
    public LecturaException() {
            super();
    }

    public LecturaException(String message, Throwable cause) {
            super(message, cause);
    }

    public LecturaException(String message) {
            super(message);
    }

    public LecturaException(Throwable cause) {
            super(cause);
    } 
}
