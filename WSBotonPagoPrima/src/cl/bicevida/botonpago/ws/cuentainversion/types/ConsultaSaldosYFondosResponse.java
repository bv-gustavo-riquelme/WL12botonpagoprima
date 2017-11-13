
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultaSaldosYFondosResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="consultaSaldosYFondosResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ejb.bicevida.cl/}saldosYFondos" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaSaldosYFondosResponse", propOrder = { "_return" })
public class ConsultaSaldosYFondosResponse {

    @XmlElement(name = "return")
    protected SaldosYFondos _return;

    /**
     * Gets the value of the return property.
     *
     * @return
     *     possible object is
     *     {@link SaldosYFondos }
     *
     */
    public SaldosYFondos getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     *
     * @param value
     *     allowed object is
     *     {@link SaldosYFondos }
     *
     */
    public void setReturn(SaldosYFondos value) {
        this._return = value;
    }

}
