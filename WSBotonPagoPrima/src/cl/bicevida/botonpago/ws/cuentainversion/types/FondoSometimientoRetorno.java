
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fondoSometimientoRetorno complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="fondoSometimientoRetorno">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idFondo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="mostrar" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="nombreFondo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ordenRiesgo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fondoSometimientoRetorno", propOrder = { "idFondo", "mostrar", "nombreFondo", "ordenRiesgo" })
public class FondoSometimientoRetorno {

    protected Long idFondo;
    protected boolean mostrar;
    protected String nombreFondo;
    protected Long ordenRiesgo;

    /**
     * Gets the value of the idFondo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getIdFondo() {
        return idFondo;
    }

    /**
     * Sets the value of the idFondo property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setIdFondo(Long value) {
        this.idFondo = value;
    }

    /**
     * Gets the value of the mostrar property.
     *
     */
    public boolean isMostrar() {
        return mostrar;
    }

    /**
     * Sets the value of the mostrar property.
     *
     */
    public void setMostrar(boolean value) {
        this.mostrar = value;
    }

    /**
     * Gets the value of the nombreFondo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNombreFondo() {
        return nombreFondo;
    }

    /**
     * Sets the value of the nombreFondo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNombreFondo(String value) {
        this.nombreFondo = value;
    }

    /**
     * Gets the value of the ordenRiesgo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getOrdenRiesgo() {
        return ordenRiesgo;
    }

    /**
     * Sets the value of the ordenRiesgo property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setOrdenRiesgo(Long value) {
        this.ordenRiesgo = value;
    }

}
