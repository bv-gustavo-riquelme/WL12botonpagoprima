package cl.bice.vida.botonpago.common.dto.parametros;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Tabla : (BICEVIDA.BPI_MPG_MEDIOPAGO_TBL)
 */
public class MedioPago implements Serializable{

    /**Map codEmpByMedioCollection <-> bicevida.datamodel.parametros.CodEmpByMedio
     * @associates <{bicevida.datamodel.parametros.CodEmpByMedio}>
     */
    private List codEmpByMedioCollection;
    private Integer codMedio;
    private String nombre;
    private String visible;

    public MedioPago() {
        super();
        this.codEmpByMedioCollection = new ArrayList();
    }

    public void addCodEmpByMedio(int index, CodEmpByMedio aCodEmpByMedio) {
        this.codEmpByMedioCollection.add(index, aCodEmpByMedio);
        aCodEmpByMedio.setMedioPago(this);
    }

    public void addCodEmpByMedio(CodEmpByMedio aCodEmpByMedio) {
        this.codEmpByMedioCollection.add(aCodEmpByMedio);
        aCodEmpByMedio.setMedioPago(this);
    }

    public List<CodEmpByMedio> getCodEmpByMedioCollection() {
        return this.codEmpByMedioCollection;
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

    public void removeCodEmpByMedio(CodEmpByMedio aCodEmpByMedio) {
        this.codEmpByMedioCollection.remove(aCodEmpByMedio);
    }

    public void setCodEmpByMedioCollection(List<CodEmpByMedio> codEmpByMedioCollection) {
        this.codEmpByMedioCollection = codEmpByMedioCollection;
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
