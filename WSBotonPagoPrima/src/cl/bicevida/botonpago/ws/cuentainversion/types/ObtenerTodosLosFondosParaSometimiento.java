
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for obtenerTodosLosFondosParaSometimiento complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="obtenerTodosLosFondosParaSometimiento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idProducto" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerTodosLosFondosParaSometimiento", propOrder = { "idProducto" })
public class ObtenerTodosLosFondosParaSometimiento {

    protected Long idProducto;

    /**
     * Gets the value of the idProducto property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getIdProducto() {
        return idProducto;
    }

    /**
     * Sets the value of the idProducto property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setIdProducto(Long value) {
        this.idProducto = value;
    }

}
