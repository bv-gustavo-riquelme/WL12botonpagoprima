package cl.bice.vida.botonpago.common.dto.parametros;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


public class Empresas implements Serializable{

    /**Map productosCollection <-> bicevida.datamodel.parametros.Productos
     * @associates <{bicevida.datamodel.parametros.Productos}>
     */
    private List productosCollection;

    /**Map codEmpByMedioCollection <-> bicevida.datamodel.parametros.CodEmpByMedio
     * @associates <{bicevida.datamodel.parametros.CodEmpByMedio}>
     */
    private List codEmpByMedioCollection;
    private Double codEmpresa;
    private String nombre;

    public Empresas() {
        super();
        this.codEmpByMedioCollection = new ArrayList();
        this.productosCollection = new ArrayList();
    }

    public void addCodEmpByMedio(int index, CodEmpByMedio aCodEmpByMedio) {
        this.codEmpByMedioCollection.add(index, aCodEmpByMedio);
        aCodEmpByMedio.setEmpresas(this);
    }

    public void addCodEmpByMedio(CodEmpByMedio aCodEmpByMedio) {
        this.codEmpByMedioCollection.add(aCodEmpByMedio);
        aCodEmpByMedio.setEmpresas(this);
    }

    public void addProductos(int index, Productos aProductos) {
        this.productosCollection.add(index, aProductos);
        aProductos.setEmpresas(this);
    }

    public void addProductos(Productos aProductos) {
        this.productosCollection.add(aProductos);
        aProductos.setEmpresas(this);
    }

    public List<CodEmpByMedio> getCodEmpByMedioCollection() {
        return this.codEmpByMedioCollection;
    }

    public Double getCodEmpresa() {
        return this.codEmpresa;
    }

    public String getNombre() {
        return this.nombre;
    }

    public List<Productos> getProductosCollection() {
        return this.productosCollection;
    }

    public void removeCodEmpByMedio(CodEmpByMedio aCodEmpByMedio) {
        this.codEmpByMedioCollection.remove(aCodEmpByMedio);
    }

    public void removeProductos(Productos aProductos) {
        this.productosCollection.remove(aProductos);
        aProductos.setEmpresas(null);
    }

    public void setCodEmpByMedioCollection(List<CodEmpByMedio> codEmpByMedioCollection) {
        this.codEmpByMedioCollection = codEmpByMedioCollection;
    }

    public void setCodEmpresa(Double codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setProductosCollection(List<Productos> productosCollection) {
        this.productosCollection = productosCollection;
    }

}
