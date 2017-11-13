package cl.bice.vida.botonpago.core2.vo;

import java.io.Serializable;

public class ResultadoProcesamientoVO implements Serializable {
    private String ActividadResultadoItem;
    private String valorResultadoItem;
    public ResultadoProcesamientoVO() {
    }

    public void setActividadResultadoItem(String actividadResultadoItem) {
        this.ActividadResultadoItem = actividadResultadoItem;
    }

    public String getActividadResultadoItem() {
        return ActividadResultadoItem;
    }

    public void setValorResultadoItem(String valorResultadoItem) {
        this.valorResultadoItem = valorResultadoItem;
    }

    public String getValorResultadoItem() {
        return valorResultadoItem;
    }
}
