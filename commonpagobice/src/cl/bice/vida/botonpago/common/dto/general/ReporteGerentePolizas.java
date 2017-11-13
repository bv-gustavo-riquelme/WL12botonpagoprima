package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class ReporteGerentePolizas  implements Serializable {
    private Integer numero_jefezonal;
    private String nombre_jefezonal;
    private Long recaudado;
    private Long por_recaudar;
    
    public ReporteGerentePolizas() {
    }

    public void setNombre_jefezonal(String nombre_jefezonal) {
        this.nombre_jefezonal = nombre_jefezonal;
    }

    public String getNombre_jefezonal() {
        return nombre_jefezonal;
    }

    public void setRecaudado(Long recaudado) {
        this.recaudado = recaudado;
    }

    public Long getRecaudado() {
        return recaudado;
    }

    public void setPor_recaudar(Long por_recaudar) {
        this.por_recaudar = por_recaudar;
    }

    public Long getPor_recaudar() {
        return por_recaudar;
    }

    public void setNumero_jefezonal(Integer numero_jefezonal) {
        this.numero_jefezonal = numero_jefezonal;
    }

    public Integer getNumero_jefezonal() {
        return numero_jefezonal;
    }
}
