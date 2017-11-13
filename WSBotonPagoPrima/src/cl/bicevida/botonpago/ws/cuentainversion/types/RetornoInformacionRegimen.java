
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for retornoInformacionRegimen complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="retornoInformacionRegimen">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descripcionRegimenTributario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroPoliza" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="regimenTributario" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="rutCliente" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retornoInformacionRegimen", propOrder = {
         "descripcionRegimenTributario", "numeroPoliza", "regimenTributario", "rutCliente"
    })
public class RetornoInformacionRegimen {

    protected String descripcionRegimenTributario;
    protected Long numeroPoliza;
    protected Long regimenTributario;
    protected Long rutCliente;

    /**
     * Gets the value of the descripcionRegimenTributario property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescripcionRegimenTributario() {
        return descripcionRegimenTributario;
    }

    /**
     * Sets the value of the descripcionRegimenTributario property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescripcionRegimenTributario(String value) {
        this.descripcionRegimenTributario = value;
    }

    /**
     * Gets the value of the numeroPoliza property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getNumeroPoliza() {
        return numeroPoliza;
    }

    /**
     * Sets the value of the numeroPoliza property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setNumeroPoliza(Long value) {
        this.numeroPoliza = value;
    }

    /**
     * Gets the value of the regimenTributario property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getRegimenTributario() {
        return regimenTributario;
    }

    /**
     * Sets the value of the regimenTributario property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setRegimenTributario(Long value) {
        this.regimenTributario = value;
    }

    /**
     * Gets the value of the rutCliente property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getRutCliente() {
        return rutCliente;
    }

    /**
     * Sets the value of the rutCliente property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setRutCliente(Long value) {
        this.rutCliente = value;
    }

}
