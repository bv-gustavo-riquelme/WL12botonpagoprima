
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for grabarDistribucionFondosAporteExtraordinarioRegResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="grabarDistribucionFondosAporteExtraordinarioRegResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ejb.bicevida.cl/}retGrabadoDistFondo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "grabarDistribucionFondosAporteExtraordinarioRegResponse", propOrder = { "_return" })
public class GrabarDistribucionFondosAporteExtraordinarioRegResponse {

    @XmlElement(name = "return")
    protected RetGrabadoDistFondo _return;

    /**
     * Gets the value of the return property.
     *
     * @return
     *     possible object is
     *     {@link RetGrabadoDistFondo }
     *
     */
    public RetGrabadoDistFondo getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     *
     * @param value
     *     allowed object is
     *     {@link RetGrabadoDistFondo }
     *
     */
    public void setReturn(RetGrabadoDistFondo value) {
        this._return = value;
    }

}
