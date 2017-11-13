package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class ReporteJefeZonalPolizas  implements Serializable {
    private Long recaudado;
    private Long por_recaudar;
    private Integer numero_jefesuc;
    private String nom_jefesuc;
    
    public ReporteJefeZonalPolizas() {
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

    public void setNom_jefesuc(String nom_jefesuc) {
        this.nom_jefesuc = nom_jefesuc;
    }

    public String getNom_jefesuc() {
        return nom_jefesuc;
    }

    public void setNumero_jefesuc(Integer numero_jefesuc) {
        this.numero_jefesuc = numero_jefesuc;
    }

    public Integer getNumero_jefesuc() {
        return numero_jefesuc;
    }
}
