package cl.bice.vida.botonpago.common.dto.parametros;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


public class MecanismoPago   implements Serializable {

    /**Map productosCollection <-> bicevida.datamodel.parametros.Productos
     * @associates <{bicevida.datamodel.parametros.Productos}>
     */
    private List productosCollection;
    private Double codMecanismo;
    private String descripcion;

    public MecanismoPago() {
        super();
        this.productosCollection = new ArrayList();
    }

    public void addProductos(int index, Productos aProductos) {
        this.productosCollection.add(index, aProductos);
        aProductos.setMecanismoPago(this);
    }

    public void addProductos(Productos aProductos) {
        this.productosCollection.add(aProductos);
        aProductos.setMecanismoPago(this);
    }

    public Double getCodMecanismo() {
        return this.codMecanismo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public List<Productos> getProductosCollection() {
        return this.productosCollection;
    }

    public void removeProductos(Productos aProductos) {
        this.productosCollection.remove(aProductos);
        aProductos.setMecanismoPago(null);
    }

    public void setCodMecanismo(Double codMecanismo) {
        this.codMecanismo = codMecanismo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setProductosCollection(List<Productos> productosCollection) {
        this.productosCollection = productosCollection;
    }

}
