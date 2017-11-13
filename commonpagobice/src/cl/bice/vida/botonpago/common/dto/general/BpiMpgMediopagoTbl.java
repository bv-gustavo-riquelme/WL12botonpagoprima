package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class BpiMpgMediopagoTbl implements Serializable {
    private Integer codMedio;
    private String nombre;
    private String visible;

    public BpiMpgMediopagoTbl() {
        super();
    }

    public Integer getCodMedio() {
        return this.codMedio;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getVisible() {
        return this.visible;
    }

    public void setCodMedio(Integer codMedio) {
        this.codMedio = codMedio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

}

