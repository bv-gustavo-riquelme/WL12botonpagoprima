
package cl.bice.xmlbeans.servipag.pec;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}IdSubTx"/>
 *         &lt;element ref="{}Identificador"/>
 *         &lt;element ref="{}Boleta"/>
 *         &lt;element ref="{}Monto"/>
 *         &lt;element ref="{}FechaVencimiento"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "idSubTx", "identificador", "boleta", "monto", "fechaVencimiento" })
@XmlRootElement(name = "Documentos")
public class Documentos {

    @XmlElement(name = "IdSubTx", required = true)
    protected String idSubTx;
    @XmlElement(name = "Identificador", required = true)
    protected String identificador;
    @XmlElement(name = "Boleta", required = true)
    protected String boleta;
    @XmlElement(name = "Monto")
    protected long monto;
    @XmlElement(name = "FechaVencimiento", required = true)
    protected String fechaVencimiento;

    /**
     * Gets the value of the idSubTx property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdSubTx() {
        return idSubTx;
    }

    /**
     * Sets the value of the idSubTx property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdSubTx(String value) {
        this.idSubTx = value;
    }

    /**
     * Gets the value of the identificador property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Sets the value of the identificador property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdentificador(String value) {
        this.identificador = value;
    }

    /**
     * Gets the value of the boleta property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBoleta() {
        return boleta;
    }

    /**
     * Sets the value of the boleta property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBoleta(String value) {
        this.boleta = value;
    }

    /**
     * Gets the value of the monto property.
     *
     */
    public long getMonto() {
        return monto;
    }

    /**
     * Sets the value of the monto property.
     *
     */
    public void setMonto(long value) {
        this.monto = value;
    }

    /**
     * Gets the value of the fechaVencimiento property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * Sets the value of the fechaVencimiento property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFechaVencimiento(String value) {
        this.fechaVencimiento = value;
    }

}
