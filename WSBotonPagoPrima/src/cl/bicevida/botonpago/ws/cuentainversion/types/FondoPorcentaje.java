
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fondoPorcentaje complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="fondoPorcentaje">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idFondo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="porcentaje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fondoPorcentaje", propOrder = { "idFondo", "porcentaje" })
public class FondoPorcentaje {

    protected String idFondo;
    protected String porcentaje;

    /**
     * Gets the value of the idFondo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdFondo() {
        return idFondo;
    }

    /**
     * Sets the value of the idFondo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdFondo(String value) {
        this.idFondo = value;
    }

    /**
     * Gets the value of the porcentaje property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPorcentaje() {
        return porcentaje;
    }

    /**
     * Sets the value of the porcentaje property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPorcentaje(String value) {
        this.porcentaje = value;
    }

}
