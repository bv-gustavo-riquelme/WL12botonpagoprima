package cl.bice.vida.botonpago.common.dto.parametros;

import java.io.Serializable;


public class Productos  implements Serializable {

    /**Map empresas <-> bicevida.datamodel.parametros.Empresas
     * @associates <{bicevida.datamodel.parametros.Empresas}>
     */
    private Empresas empresas;

    /**Map mecanismoPago <-> bicevida.datamodel.parametros.MecanismoPago
     * @associates <{bicevida.datamodel.parametros.MecanismoPago}>
     */
    private MecanismoPago mecanismoPago;
    private Double codProducto;
    private String nombre;
    private String copOperativo;

    public Productos() {
        super();
        this.empresas = new Empresas();
        this.mecanismoPago = new MecanismoPago();
    }

    public Double getCodProducto() {
        return this.codProducto;
    }

    public String getCopOperativo() {
        return this.copOperativo;
    }

    public Empresas getEmpresas() {
        return (Empresas)this.empresas;
    }

    public MecanismoPago getMecanismoPago() {
        return (MecanismoPago)this.mecanismoPago;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setCodProducto(Double codProducto) {
        this.codProducto = codProducto;
    }

    public void setCopOperativo(String copOperativo) {
        this.copOperativo = copOperativo;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public void setMecanismoPago(MecanismoPago mecanismoPago) {
        this.mecanismoPago = mecanismoPago;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
