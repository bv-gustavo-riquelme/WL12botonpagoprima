
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for obtenerInformacionRegimen complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="obtenerInformacionRegimen">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rut" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerInformacionRegimen", propOrder = { "rut" })
public class ObtenerInformacionRegimen {

    protected Long rut;

    /**
     * Gets the value of the rut property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getRut() {
        return rut;
    }

    /**
     * Sets the value of the rut property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setRut(Long value) {
        this.rut = value;
    }

}
