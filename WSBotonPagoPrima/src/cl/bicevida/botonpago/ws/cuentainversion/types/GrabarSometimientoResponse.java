
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for grabarSometimientoResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="grabarSometimientoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ejb.bicevida.cl/}retornoGrabarSometimiento" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "grabarSometimientoResponse", propOrder = { "_return" })
public class GrabarSometimientoResponse {

    @XmlElement(name = "return")
    protected RetornoGrabarSometimiento _return;

    /**
     * Gets the value of the return property.
     *
     * @return
     *     possible object is
     *     {@link RetornoGrabarSometimiento }
     *
     */
    public RetornoGrabarSometimiento getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     *
     * @param value
     *     allowed object is
     *     {@link RetornoGrabarSometimiento }
     *
     */
    public void setReturn(RetornoGrabarSometimiento value) {
        this._return = value;
    }

}
