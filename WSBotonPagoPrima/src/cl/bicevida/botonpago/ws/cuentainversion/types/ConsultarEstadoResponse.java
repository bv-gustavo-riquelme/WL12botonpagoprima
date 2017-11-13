
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultarEstadoResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="consultarEstadoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ejb.bicevida.cl/}retornoConsultaEstado" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarEstadoResponse", propOrder = { "_return" })
public class ConsultarEstadoResponse {

    @XmlElement(name = "return")
    protected RetornoConsultaEstado _return;

    /**
     * Gets the value of the return property.
     *
     * @return
     *     possible object is
     *     {@link RetornoConsultaEstado }
     *
     */
    public RetornoConsultaEstado getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     *
     * @param value
     *     allowed object is
     *     {@link RetornoConsultaEstado }
     *
     */
    public void setReturn(RetornoConsultaEstado value) {
        this._return = value;
    }

}
