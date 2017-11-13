
package cl.bice.vida.botonpago.services.notificacion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for informarRecaudacionResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="informarRecaudacionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://webservices.model.integracion.bicevida.cl/}InformarRecaudacionOut"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "informarRecaudacionResponse", propOrder = { "_return" })
public class InformarRecaudacionResponse {

    @XmlElement(name = "return", required = true, nillable = true)
    protected InformarRecaudacionOut _return;

    /**
     * Gets the value of the return property.
     *
     * @return
     *     possible object is
     *     {@link InformarRecaudacionOut }
     *
     */
    public InformarRecaudacionOut getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     *
     * @param value
     *     allowed object is
     *     {@link InformarRecaudacionOut }
     *
     */
    public void setReturn(InformarRecaudacionOut value) {
        this._return = value;
    }

}
