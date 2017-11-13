
package cl.bicevida.botonpago.ws.cuentainversion.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for grabarDistribucionFondosAporteExtraordinarioReg complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="grabarDistribucionFondosAporteExtraordinarioReg">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ramo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroPoliza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rutCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="montoTotal" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="fondos" type="{http://ejb.bicevida.cl/}fondoPorcentaje" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "grabarDistribucionFondosAporteExtraordinarioReg", propOrder = {
         "ramo", "numeroPoliza", "rutCliente", "usuario", "montoTotal", "fondos", "regimen"
    })
public class GrabarDistribucionFondosAporteExtraordinarioReg {

    protected String ramo;
    protected String numeroPoliza;
    protected String rutCliente;
    protected String usuario;
    protected double montoTotal;
    protected List<FondoPorcentaje> fondos;
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
     * Gets the value of the montoTotal property.
     *
     */
    public double getMontoTotal() {
        return montoTotal;
    }

    /**
     * Sets the value of the montoTotal property.
     *
     */
    public void setMontoTotal(double value) {
        this.montoTotal = value;
    }

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
     * {@link FondoPorcentaje }
     *
     *
     */
    public List<FondoPorcentaje> getFondos() {
        if (fondos == null) {
            fondos = new ArrayList<FondoPorcentaje>();
        }
        return this.fondos;
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
