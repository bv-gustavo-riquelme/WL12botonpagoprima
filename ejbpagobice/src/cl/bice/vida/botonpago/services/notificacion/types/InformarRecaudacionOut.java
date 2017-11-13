
package cl.bice.vida.botonpago.services.notificacion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InformarRecaudacionOut complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="InformarRecaudacionOut">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoResultado" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="mensajeResultado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InformarRecaudacionOut", propOrder = { "codigoResultado", "mensajeResultado" })
public class InformarRecaudacionOut {

    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer codigoResultado;
    @XmlElement(required = true, nillable = true)
    protected String mensajeResultado;

    /**
     * Gets the value of the codigoResultado property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCodigoResultado() {
        return codigoResultado;
    }

    /**
     * Sets the value of the codigoResultado property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCodigoResultado(Integer value) {
        this.codigoResultado = value;
    }

    /**
     * Gets the value of the mensajeResultado property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMensajeResultado() {
        return mensajeResultado;
    }

    /**
     * Sets the value of the mensajeResultado property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMensajeResultado(String value) {
        this.mensajeResultado = value;
    }

}
