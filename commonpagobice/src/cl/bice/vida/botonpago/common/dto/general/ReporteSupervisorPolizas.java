package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class ReporteSupervisorPolizas  implements Serializable {
    private Long recaudado;
    private Long por_recaudar;
    private Integer numero_agente;
    private String nom_agente;
    
    public ReporteSupervisorPolizas() {
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

    public void setNom_agente(String nom_agente) {
        this.nom_agente = nom_agente;
    }

    public String getNom_agente() {
        return nom_agente;
    }

    public void setNumero_agente(Integer numero_agente) {
        this.numero_agente = numero_agente;
    }

    public Integer getNumero_agente() {
        return numero_agente;
    }
}
