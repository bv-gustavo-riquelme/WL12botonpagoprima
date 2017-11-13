
package cl.bice.xmlbeans.servipag.respuesta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}FirmaServipag"/>
 *         &lt;element ref="{}IdTrxServipag"/>
 *         &lt;element ref="{}IdTxCliente"/>
 *         &lt;element ref="{}EstadoPago"/>
 *         &lt;element ref="{}MensajePago"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "firmaServipag", "idTrxServipag", "idTxCliente", "estadoPago", "mensajePago" })
@XmlRootElement(name = "Servipag")
public class Servipag {

    @XmlElement(name = "FirmaServipag", required = true)
    protected String firmaServipag;
    @XmlElement(name = "IdTrxServipag", required = true)
    protected String idTrxServipag;
    @XmlElement(name = "IdTxCliente", required = true)
    protected String idTxCliente;
    @XmlElement(name = "EstadoPago", required = true)
    protected String estadoPago;
    @XmlElement(name = "MensajePago", required = true)
    protected String mensajePago;

    /**
     * Gets the value of the firmaServipag property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFirmaServipag() {
        return firmaServipag;
    }

    /**
     * Sets the value of the firmaServipag property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFirmaServipag(String value) {
        this.firmaServipag = value;
    }

    /**
     * Gets the value of the idTrxServipag property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdTrxServipag() {
        return idTrxServipag;
    }

    /**
     * Sets the value of the idTrxServipag property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdTrxServipag(String value) {
        this.idTrxServipag = value;
    }

    /**
     * Gets the value of the idTxCliente property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdTxCliente() {
        return idTxCliente;
    }

    /**
     * Sets the value of the idTxCliente property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdTxCliente(String value) {
        this.idTxCliente = value;
    }

    /**
     * Gets the value of the estadoPago property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEstadoPago() {
        return estadoPago;
    }

    /**
     * Sets the value of the estadoPago property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEstadoPago(String value) {
        this.estadoPago = value;
    }

    /**
     * Gets the value of the mensajePago property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMensajePago() {
        return mensajePago;
    }

    /**
     * Sets the value of the mensajePago property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMensajePago(String value) {
        this.mensajePago = value;
    }

}
