
package cl.bice.vida.botonpago.services.notificacion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for InformarRecaudacionIn complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="InformarRecaudacionIn">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="callerSystem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="folioPago" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="turno" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="idTurno" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="callerUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="folioCajaDiario" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="usuarioCaja" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InformarRecaudacionIn", propOrder = {
         "callerSystem", "folioPago", "turno", "idTurno", "callerUser", "folioCajaDiario", "usuarioCaja"
    })
public class InformarRecaudacionIn {

    @XmlElement(required = true, nillable = true)
    protected String callerSystem;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long folioPago;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar turno;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long idTurno;
    @XmlElement(required = true, nillable = true)
    protected String callerUser;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long folioCajaDiario;
    @XmlElement(required = true, nillable = true)
    protected String usuarioCaja;

    /**
     * Gets the value of the callerSystem property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCallerSystem() {
        return callerSystem;
    }

    /**
     * Sets the value of the callerSystem property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCallerSystem(String value) {
        this.callerSystem = value;
    }

    /**
     * Gets the value of the folioPago property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getFolioPago() {
        return folioPago;
    }

    /**
     * Sets the value of the folioPago property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setFolioPago(Long value) {
        this.folioPago = value;
    }

    /**
     * Gets the value of the turno property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getTurno() {
        return turno;
    }

    /**
     * Sets the value of the turno property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setTurno(XMLGregorianCalendar value) {
        this.turno = value;
    }

    /**
     * Gets the value of the idTurno property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getIdTurno() {
        return idTurno;
    }

    /**
     * Sets the value of the idTurno property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setIdTurno(Long value) {
        this.idTurno = value;
    }

    /**
     * Gets the value of the callerUser property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCallerUser() {
        return callerUser;
    }

    /**
     * Sets the value of the callerUser property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCallerUser(String value) {
        this.callerUser = value;
    }

    /**
     * Gets the value of the folioCajaDiario property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getFolioCajaDiario() {
        return folioCajaDiario;
    }

    /**
     * Sets the value of the folioCajaDiario property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setFolioCajaDiario(Long value) {
        this.folioCajaDiario = value;
    }

    /**
     * Gets the value of the usuarioCaja property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUsuarioCaja() {
        return usuarioCaja;
    }

    /**
     * Sets the value of the usuarioCaja property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUsuarioCaja(String value) {
        this.usuarioCaja = value;
    }

}
