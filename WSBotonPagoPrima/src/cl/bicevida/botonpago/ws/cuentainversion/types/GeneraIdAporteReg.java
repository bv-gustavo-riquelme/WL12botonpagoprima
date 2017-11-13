
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for generaIdAporteReg complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="generaIdAporteReg">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ramo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroPoliza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rutCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monto" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="regimen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generaIdAporteReg", propOrder = {
         "ramo", "numeroPoliza", "rutCliente", "monto", "usuario", "regimen" })
public class GeneraIdAporteReg {

    protected String ramo;
    protected String numeroPoliza;
    protected String rutCliente;
    protected double monto;
    protected String usuario;
    protected String regimen;

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
     * Gets the value of the numeroPoliza property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    /**
     * Sets the value of the numeroPoliza property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNumeroPoliza(String value) {
        this.numeroPoliza = value;
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
     * Gets the value of the monto property.
     *
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Sets the value of the monto property.
     *
     */
    public void setMonto(double value) {
        this.monto = value;
    }

    /**
     * Gets the value of the usuario property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }

    /**
     * Gets the value of the regimen property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRegimen() {
        return regimen;
    }

    /**
     * Sets the value of the regimen property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRegimen(String value) {
        this.regimen = value;
    }

}
