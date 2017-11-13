
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for idAporte complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="idAporte">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idAporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monto" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="numPoliza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "idAporte", propOrder = { "idAporte", "monto", "numPoliza", "rutCliente" })
public class IdAporte {

    protected String idAporte;
    protected double monto;
    protected String numPoliza;
    protected String rutCliente;

    /**
     * Gets the value of the idAporte property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdAporte() {
        return idAporte;
    }

    /**
     * Sets the value of the idAporte property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdAporte(String value) {
        this.idAporte = value;
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
