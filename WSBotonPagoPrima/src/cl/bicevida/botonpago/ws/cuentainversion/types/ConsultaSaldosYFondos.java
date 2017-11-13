
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultaSaldosYFondos complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="consultaSaldosYFondos">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ramo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroPoliza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rutCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaSaldosYFondos", propOrder = { "ramo", "numeroPoliza", "rutCliente" })
public class ConsultaSaldosYFondos {

    protected String ramo;
    protected String numeroPoliza;
    protected String rutCliente;

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

    /**
     * Gets the value of the numeroPoliza property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    /**
     * Sets the value of the numeroPoliza property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNumeroPoliza(String value) {
        this.numeroPoliza = value;
    }

    /**
     * Gets the value of the rutCliente property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRutCliente() {
        return rutCliente;
    }

    /**
     * Sets the value of the rutCliente property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRutCliente(String value) {
        this.rutCliente = value;
    }

}
