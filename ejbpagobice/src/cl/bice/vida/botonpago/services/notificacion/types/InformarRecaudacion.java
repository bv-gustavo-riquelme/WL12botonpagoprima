
package cl.bice.vida.botonpago.services.notificacion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for informarRecaudacion complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="informarRecaudacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InformarRecaudacionIn_1" type="{http://webservices.model.integracion.bicevida.cl/}InformarRecaudacionIn"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "informarRecaudacion", propOrder = { "informarRecaudacionIn1" })
public class InformarRecaudacion {

    @XmlElement(name = "InformarRecaudacionIn_1", required = true, nillable = true)
    protected InformarRecaudacionIn informarRecaudacionIn1;

    /**
     * Gets the value of the informarRecaudacionIn1 property.
     *
     * @return
     *     possible object is
     *     {@link InformarRecaudacionIn }
     *
     */
    public InformarRecaudacionIn getInformarRecaudacionIn1() {
        return informarRecaudacionIn1;
    }

    /**
     * Sets the value of the informarRecaudacionIn1 property.
     *
     * @param value
     *     allowed object is
     *     {@link InformarRecaudacionIn }
     *
     */
    public void setInformarRecaudacionIn1(InformarRecaudacionIn value) {
        this.informarRecaudacionIn1 = value;
    }

}
