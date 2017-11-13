package cl.bice.vida.botonpago.modelo.dto;

import cl.bice.vida.botonpago.common.dto.general.BpiDitribFondosAPVTbl;

import java.util.List;

public class Prueba {
    
    int idAporte;
    //ist<BpiDitribFondosAPVTbl> DistribfFondos;
    Long codigoRegimen; 
    String descripcionRegimen;
    
    public Prueba() {
        super();
    }

    public void setIdAporte(int idAporte) {
        this.idAporte = idAporte;
    }

    public int getIdAporte() {
        return idAporte;
    }

   /* public void setDistribfFondos(List<BpiDitribFondosAPVTbl> DistribfFondos) {
        this.DistribfFondos = DistribfFondos;
    }

    public List<BpiDitribFondosAPVTbl> getDistribfFondos() {
        return DistribfFondos;
    }*/

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
