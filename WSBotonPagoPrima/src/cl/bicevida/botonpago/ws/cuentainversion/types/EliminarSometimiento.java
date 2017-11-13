
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eliminarSometimiento complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="eliminarSometimiento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RutCliente" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="NumeroPoliza" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eliminarSometimiento", propOrder = { "rutCliente", "numeroPoliza" })
public class EliminarSometimiento {

    @XmlElement(name = "RutCliente")
    protected Long rutCliente;
    @XmlElement(name = "NumeroPoliza")
    protected Long numeroPoliza;

    /**
     * Gets the value of the rutCliente property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getRutCliente() {
        return rutCliente;
    }

    /**
     * Sets the value of the rutCliente property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setRutCliente(Long value) {
        this.rutCliente = value;
    }

    /**
     * Gets the value of the numeroPoliza property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getNumeroPoliza() {
        return numeroPoliza;
    }

    /**
     * Sets the value of the numeroPoliza property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setNumeroPoliza(Long value) {
        this.numeroPoliza = value;
    }

}
