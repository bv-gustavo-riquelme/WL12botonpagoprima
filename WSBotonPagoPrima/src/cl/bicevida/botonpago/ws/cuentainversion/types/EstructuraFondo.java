
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for estructuraFondo complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="estructuraFondo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idAporteExtraordinario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numPoliza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ramo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estructuraFondo", propOrder = { "idAporteExtraordinario", "monto", "numPoliza", "ramo" })
public class EstructuraFondo {

    protected String idAporteExtraordinario;
    protected String monto;
    protected String numPoliza;
    protected String ramo;

    /**
     * Gets the value of the idAporteExtraordinario property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdAporteExtraordinario() {
        return idAporteExtraordinario;
    }

    /**
     * Sets the value of the idAporteExtraordinario property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdAporteExtraordinario(String value) {
        this.idAporteExtraordinario = value;
    }

    /**
     * Gets the value of the monto property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMonto() {
        return monto;
    }

    /**
     * Sets the value of the monto property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMonto(String value) {
        this.monto = value;
    }

    /**
     * Gets the value of the numPoliza property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNumPoliza() {
        return numPoliza;
    }

    /**
     * Sets the value of the numPoliza property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNumPoliza(String value) {
        this.numPoliza = value;
    }

    /**
     * Gets the value of the ramo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRamo() {
        return ramo;
    }

    /**
     * Sets the value of the ramo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRamo(String value) {
        this.ramo = value;
    }

}
