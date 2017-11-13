package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class ReporteJefeSucursalPolizas  implements Serializable {
    private Long recaudado;
    private Long por_recaudar;
    private Integer numero_supervisor;
    private String nom_supervisor;
    
    public ReporteJefeSucursalPolizas() {
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

    public void setNom_supervisor(String nom_supervisor) {
        this.nom_supervisor = nom_supervisor;
    }

    public String getNom_supervisor() {
        return nom_supervisor;
    }

    public void setNumero_supervisor(Integer numero_supervisor) {
        this.numero_supervisor = numero_supervisor;
    }

    public Integer getNumero_supervisor() {
        return numero_supervisor;
    }
}
