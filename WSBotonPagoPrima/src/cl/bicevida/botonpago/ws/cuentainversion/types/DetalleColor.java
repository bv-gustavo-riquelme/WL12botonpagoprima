
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for detalleColor complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="detalleColor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hexadecimal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valorColorX" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="valorColorY" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="valorColorZ" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleColor", propOrder = { "hexadecimal", "valorColorX", "valorColorY", "valorColorZ" })
public class DetalleColor {

    protected String hexadecimal;
    protected Long valorColorX;
    protected Long valorColorY;
    protected Long valorColorZ;

    /**
     * Gets the value of the hexadecimal property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHexadecimal() {
        return hexadecimal;
    }

    /**
     * Sets the value of the hexadecimal property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setHexadecimal(String value) {
        this.hexadecimal = value;
    }

    /**
     * Gets the value of the valorColorX property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getValorColorX() {
        return valorColorX;
    }

    /**
     * Sets the value of the valorColorX property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setValorColorX(Long value) {
        this.valorColorX = value;
    }

    /**
     * Gets the value of the valorColorY property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getValorColorY() {
        return valorColorY;
    }

    /**
     * Sets the value of the valorColorY property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setValorColorY(Long value) {
        this.valorColorY = value;
    }

    /**
     * Gets the value of the valorColorZ property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getValorColorZ() {
        return valorColorZ;
    }

    /**
     * Sets the value of the valorColorZ property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setValorColorZ(Long value) {
        this.valorColorZ = value;
    }

}
