
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for retGrabadoDistFondo complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="retGrabadoDistFondo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoRespuesta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idDistribucion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mensajeRespuesta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retGrabadoDistFondo", propOrder = { "codigoRespuesta", "idDistribucion", "mensajeRespuesta" })
public class RetGrabadoDistFondo {

    protected String codigoRespuesta;
    protected String idDistribucion;
    protected String mensajeRespuesta;

    /**
     * Gets the value of the codigoRespuesta property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    /**
     * Sets the value of the codigoRespuesta property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCodigoRespuesta(String value) {
        this.codigoRespuesta = value;
    }

    /**
     * Gets the value of the idDistribucion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdDistribucion() {
        return idDistribucion;
    }

    /**
     * Sets the value of the idDistribucion property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdDistribucion(String value) {
        this.idDistribucion = value;
    }

    /**
     * Gets the value of the mensajeRespuesta property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    /**
     * Sets the value of the mensajeRespuesta property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMensajeRespuesta(String value) {
        this.mensajeRespuesta = value;
    }

}
