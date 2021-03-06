
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultarEstado complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="consultarEstado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numeroPoliza" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="rutCliente" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="ramo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarEstado", propOrder = { "numeroPoliza", "rutCliente", "ramo" })
public class ConsultarEstado {

    protected Long numeroPoliza;
    protected Long rutCliente;
    protected Long ramo;

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
     * Gets the value of the ramo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getRamo() {
        return ramo;
    }

    /**
     * Sets the value of the ramo property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setRamo(Long value) {
        this.ramo = value;
    }

}
