package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class PrcProcesoGeneraFileDto implements Serializable {
    public PrcProcesoGeneraFileDto() {
    }
    
    private String p_archivocuadr;
    private int p_mediopago;
    private int p_empresa;
    private String p_fechacuadr;
    private String p_buffer;

    public void setP_mediopago(int p_mediopago) {
        this.p_mediopago = p_mediopago;
    }

    public int getP_mediopago() {
        return p_mediopago;
    }

    public void setP_empresa(int p_empresa) {
        this.p_empresa = p_empresa;
    }

    public int getP_empresa() {
        return p_empresa;
    }

    public void setP_fechacuadr(String p_fechacuadr) {
        this.p_fechacuadr = p_fechacuadr;
    }

    public String getP_fechacuadr() {
        return p_fechacuadr;
    }

    public void setP_buffer(String p_buffer) {
        this.p_buffer = p_buffer;
    }

    public String getP_buffer() {
        return p_buffer;
    }

    public void setP_archivocuadr(String p_archivocuadr) {
        this.p_archivocuadr = p_archivocuadr;
    }

    public String getP_archivocuadr() {
        return p_archivocuadr;
    }
}
