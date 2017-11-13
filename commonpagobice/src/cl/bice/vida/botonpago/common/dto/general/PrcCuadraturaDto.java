package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.sql.Timestamp;

public class PrcCuadraturaDto implements Serializable {
   
    private String p_archivocuadr;
    private Integer p_mediopago;
    private Integer p_empresa;
    private String p_fechacuadr;
    
    public PrcCuadraturaDto() {
    }
    
    public void setP_archivocuadr(String p_archivocuadr) {
        this.p_archivocuadr = p_archivocuadr;
    }

    public String getP_archivocuadr() {
        return p_archivocuadr;
    }

    public void setP_mediopago(Integer p_mediopago) {
        this.p_mediopago = p_mediopago;
    }

    public Integer getP_mediopago() {
        return p_mediopago;
    }

    public void setP_empresa(Integer p_empresa) {
        this.p_empresa = p_empresa;
    }

    public Integer getP_empresa() {
        return p_empresa;
    }


    public void setP_fechacuadr(String p_fechacuadr) {
        this.p_fechacuadr = p_fechacuadr;
    }

    public String getP_fechacuadr() {
        return p_fechacuadr;
    }
}
