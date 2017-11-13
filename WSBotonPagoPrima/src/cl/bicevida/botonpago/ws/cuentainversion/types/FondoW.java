
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fondoW complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="fondoW">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="detalleColor" type="{http://ejb.bicevida.cl/}detalleColor" minOccurs="0"/>
 *         &lt;element name="idFondo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="linkPDF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monto" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="nombreFondo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ordenPresentacion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="porcentaje" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="riesgo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fondoW", propOrder = {
         "detalleColor", "idFondo", "linkPDF", "monto", "nombreFondo", "ordenPresentacion", "porcentaje", "riesgo"
    })
public class FondoW {

    protected DetalleColor detalleColor;
    protected int idFondo;
    protected String linkPDF;
    protected double monto;
    protected String nombreFondo;
    protected int ordenPresentacion;
    protected int porcentaje;
    protected int riesgo;

    /**
     * Gets the value of the detalleColor property.
     *
     * @return
     *     possible object is
     *     {@link DetalleColor }
     *
     */
    public DetalleColor getDetalleColor() {
        return detalleColor;
    }

    /**
     * Sets the value of the detalleColor property.
     *
     * @param value
     *     allowed object is
     *     {@link DetalleColor }
     *
     */
    public void setDetalleColor(DetalleColor value) {
        this.detalleColor = value;
    }

    /**
     * Gets the value of the idFondo property.
     *
     */
    public int getIdFondo() {
        return idFondo;
    }

    /**
     * Sets the value of the idFondo property.
     *
     */
    public void setIdFondo(int value) {
        this.idFondo = value;
    }

    /**
     * Gets the value of the linkPDF property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLinkPDF() {
        return linkPDF;
    }

    /**
     * Sets the value of the linkPDF property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLinkPDF(String value) {
        this.linkPDF = value;
    }

    /**
     * Gets the value of the monto property.
     *
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Sets the value of the monto property.
     *
     */
    public void setMonto(double value) {
        this.monto = value;
    }

    /**
     * Gets the value of the nombreFondo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNombreFondo() {
        return nombreFondo;
    }

    /**
     * Sets the value of the nombreFondo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNombreFondo(String value) {
        this.nombreFondo = value;
    }

    /**
     * Gets the value of the ordenPresentacion property.
     *
     */
    public int getOrdenPresentacion() {
        return ordenPresentacion;
    }

    /**
     * Sets the value of the ordenPresentacion property.
     *
     */
    public void setOrdenPresentacion(int value) {
        this.ordenPresentacion = value;
    }

    /**
     * Gets the value of the porcentaje property.
     *
     */
    public int getPorcentaje() {
        return porcentaje;
    }

    /**
     * Sets the value of the porcentaje property.
     *
     */
    public void setPorcentaje(int value) {
        this.porcentaje = value;
    }

    /**
     * Gets the value of the riesgo property.
     *
     */
    public int getRiesgo() {
        return riesgo;
    }

    /**
     * Sets the value of the riesgo property.
     *
     */
    public void setRiesgo(int value) {
        this.riesgo = value;
    }

}
