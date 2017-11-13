package cl.bice.vida.botonpago.common.dto.parametros;

import java.io.Serializable;

public class Estados  implements Serializable {

    private Integer codEstado;
    private String descripcion;

    public Estados() {
        super();
    }

    public Integer getCodEstado() {
        return this.codEstado;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setCodEstado(Integer codEstado) {
        this.codEstado = codEstado;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
