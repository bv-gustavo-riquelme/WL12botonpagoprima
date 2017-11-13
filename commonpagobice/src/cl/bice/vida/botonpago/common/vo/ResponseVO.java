package cl.bice.vida.botonpago.common.vo;

import java.io.Serializable;

public class ResponseVO implements Serializable {
    private Object data;
    private Exception error;

    /**
     * Informa si el resultado de la opcion esta OK
     * @return valor booleano con el resultado de la operacion
     */
    public boolean isTransactionOK() {
        return this.data != null && this.error == null?true:false;
    }
    /**
     * Constructor con data ok
     */
    public ResponseVO(Object data) {
        this.data = data;
    }
    /**
     * Contructor con Excepcion
     */
    public ResponseVO(Exception error) {
        this.error = error;
    }
    
    public boolean isOk() {
        return data!=null&&error==null?true:false;
    }
    
    public String getErrorMessage() {
        return error==null?"-Error no establecido-":error.getMessage();
    }
    
    public Object getData() {
        return data;
    }

    public Exception getError() {
        return error;
    }

}
