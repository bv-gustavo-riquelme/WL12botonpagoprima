package cl.bice.vida.botonpago.common.dto.parametros;

import java.io.Serializable;

public class Paginas  implements Serializable {

    private Integer codPagina;
    private String descripcion;

    public Paginas() {
        super();
    }

    public Integer getCodPagina() {
        return this.codPagina;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setCodPagina(Integer codPagina) {
        this.codPagina = codPagina;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
