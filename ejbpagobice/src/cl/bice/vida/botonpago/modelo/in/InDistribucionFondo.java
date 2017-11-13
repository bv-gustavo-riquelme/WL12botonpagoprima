package cl.bice.vida.botonpago.modelo.in;

import cl.bice.vida.botonpago.common.dto.general.BpiDitribFondosAPVTbl;

import java.util.List;

public class InDistribucionFondo {
    
    private int idAporte;
    private List<BpiDitribFondosAPVTbl> DistribfFondos;
    private Long codigoRegimen;
    private String descripcionRegimen;
    
    public InDistribucionFondo() {
        super();
    }

    public void setIdAporte(int idAporte) {
        this.idAporte = idAporte;
    }

    public int getIdAporte() {
        return idAporte;
    }

    public void setDistribfFondos(List<BpiDitribFondosAPVTbl> DistribfFondos) {
        this.DistribfFondos = DistribfFondos;
    }

    public List<BpiDitribFondosAPVTbl> getDistribfFondos() {
        return DistribfFondos;
    }

    public void setCodigoRegimen(Long codigoRegimen) {
        this.codigoRegimen = codigoRegimen;
    }

    public Long getCodigoRegimen() {
        return codigoRegimen;
    }

    public void setDescripcionRegimen(String descripcionRegimen) {
        this.descripcionRegimen = descripcionRegimen;
    }

    public String getDescripcionRegimen() {
        return descripcionRegimen;
    }


}
