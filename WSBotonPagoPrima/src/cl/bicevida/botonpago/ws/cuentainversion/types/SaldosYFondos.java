
package cl.bicevida.botonpago.ws.cuentainversion.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saldosYFondos complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="saldosYFondos">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fondos" type="{http://ejb.bicevida.cl/}fondoW" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numPoliza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ramo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rut" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saldosYFondos", propOrder = { "fondos", "numPoliza", "ramo", "rut" })
public class SaldosYFondos {

    @XmlElement(nillable = true)
    protected List<FondoW> fondos;
    protected String numPoliza;
    protected String ramo;
    protected String rut;

    /**
     * Gets the value of the fondos property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fondos property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFondos().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FondoW }
     *
     *
     */
    public List<FondoW> getFondos() {
        if (fondos == null) {
            fondos = new ArrayList<FondoW>();
        }
        return this.fondos;
    }

    /**
     * Gets the value of the numPoliza property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNumPoliza() {
        return numPoliza;
    }

    /**
     * Sets the value of the numPoliza property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNumPoliza(String value) {
        this.numPoliza = value;
    }

    /**
     * Gets the value of the ramo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRamo() {
        return ramo;
    }

    /**
     * Sets the value of the ramo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRamo(String value) {
        this.ramo = value;
    }

    /**
     * Gets the value of the rut property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRut() {
        return rut;
    }

    /**
     * Sets the value of the rut property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRut(String value) {
        this.rut = value;
    }

}
