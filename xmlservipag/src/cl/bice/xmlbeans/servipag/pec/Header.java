
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
 *         &lt;element ref="{}FirmaEPS"/>
 *         &lt;element ref="{}CodigoCanalPago"/>
 *         &lt;element ref="{}IdTxPago"/>
 *         &lt;element ref="{}FechaPago"/>
 *         &lt;element ref="{}MontoTotalDeuda"/>
 *         &lt;element ref="{}NumeroBoletas"/>
 *         &lt;element ref="{}NombreCliente"/>
 *         &lt;element ref="{}RutCliente"/>
 *         &lt;element ref="{}EmailCliente"/>
 *         &lt;element ref="{}Version"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
         "firmaEPS", "codigoCanalPago", "idTxPago", "fechaPago", "montoTotalDeuda", "numeroBoletas", "nombreCliente",
         "rutCliente", "emailCliente", "version"
    })
@XmlRootElement(name = "Header")
public class Header {

    @XmlElement(name = "FirmaEPS", required = true)
    protected String firmaEPS;
    @XmlElement(name = "CodigoCanalPago", required = true)
    protected String codigoCanalPago;
    @XmlElement(name = "IdTxPago", required = true)
    protected String idTxPago;
    @XmlElement(name = "FechaPago", required = true)
    protected String fechaPago;
    @XmlElement(name = "MontoTotalDeuda")
    protected long montoTotalDeuda;
    @XmlElement(name = "NumeroBoletas")
    protected int numeroBoletas;
    @XmlElement(name = "NombreCliente", required = true)
    protected String nombreCliente;
    @XmlElement(name = "RutCliente", required = true)
    protected String rutCliente;
    @XmlElement(name = "EmailCliente", required = true)
    protected String emailCliente;
    @XmlElement(name = "Version", required = true)
    protected String version;

    /**
     * Gets the value of the firmaEPS property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFirmaEPS() {
        return firmaEPS;
    }

    /**
     * Sets the value of the firmaEPS property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFirmaEPS(String value) {
        this.firmaEPS = value;
    }

    /**
     * Gets the value of the codigoCanalPago property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCodigoCanalPago() {
        return codigoCanalPago;
    }

    /**
     * Sets the value of the codigoCanalPago property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCodigoCanalPago(String value) {
        this.codigoCanalPago = value;
    }

    /**
     * Gets the value of the idTxPago property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdTxPago() {
        return idTxPago;
    }

    /**
     * Sets the value of the idTxPago property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdTxPago(String value) {
        this.idTxPago = value;
    }

    /**
     * Gets the value of the fechaPago property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFechaPago() {
        return fechaPago;
    }

    /**
     * Sets the value of the fechaPago property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFechaPago(String value) {
        this.fechaPago = value;
    }

    /**
     * Gets the value of the montoTotalDeuda property.
     *
     */
    public long getMontoTotalDeuda() {
        return montoTotalDeuda;
    }

    /**
     * Sets the value of the montoTotalDeuda property.
     *
     */
    public void setMontoTotalDeuda(long value) {
        this.montoTotalDeuda = value;
    }

    /**
     * Gets the value of the numeroBoletas property.
     *
     */
    public int getNumeroBoletas() {
        return numeroBoletas;
    }

    /**
     * Sets the value of the numeroBoletas property.
     *
     */
    public void setNumeroBoletas(int value) {
        this.numeroBoletas = value;
    }

    /**
     * Gets the value of the nombreCliente property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Sets the value of the nombreCliente property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNombreCliente(String value) {
        this.nombreCliente = value;
    }

    /**
     * Gets the value of the rutCliente property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRutCliente() {
        return rutCliente;
    }

    /**
     * Sets the value of the rutCliente property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRutCliente(String value) {
        this.rutCliente = value;
    }

    /**
     * Gets the value of the emailCliente property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEmailCliente() {
        return emailCliente;
    }

    /**
     * Sets the value of the emailCliente property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEmailCliente(String value) {
        this.emailCliente = value;
    }

    /**
     * Gets the value of the version property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
