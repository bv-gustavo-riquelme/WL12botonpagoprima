
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for grabarSometimiento complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="grabarSometimiento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Producto" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="FechaSuscripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RutCliente" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="DvCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sucursal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FechaEmisionPoliza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Ramo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="NumeroPoliza" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="Usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FormaPago" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="TipoRegimenTributario" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="FechaCaptura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdsPorcejateFondos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdDir" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="NumeroPropuesta" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="InstitucionOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Monto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TipoMonto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FolioSolicitud" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TipoTrabajador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClaseCotizante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "grabarSometimiento", propOrder = {
         "producto", "fechaSuscripcion", "rutCliente", "dvCliente", "sucursal", "fechaEmisionPoliza", "ramo",
         "numeroPoliza", "usuario", "formaPago", "tipoRegimenTributario", "fechaCaptura", "idsPorcejateFondos", "idDir",
         "numeroPropuesta", "institucionOrigen", "monto", "tipoMonto", "folioSolicitud", "tipoTrabajador",
         "claseCotizante"
    })
public class GrabarSometimiento {

    @XmlElement(name = "Producto")
    protected Long producto;
    @XmlElement(name = "FechaSuscripcion")
    protected String fechaSuscripcion;
    @XmlElement(name = "RutCliente")
    protected Long rutCliente;
    @XmlElement(name = "DvCliente")
    protected String dvCliente;
    @XmlElement(name = "Sucursal")
    protected String sucursal;
    @XmlElement(name = "FechaEmisionPoliza")
    protected String fechaEmisionPoliza;
    @XmlElement(name = "Ramo")
    protected Long ramo;
    @XmlElement(name = "NumeroPoliza")
    protected Long numeroPoliza;
    @XmlElement(name = "Usuario")
    protected String usuario;
    @XmlElement(name = "FormaPago")
    protected Long formaPago;
    @XmlElement(name = "TipoRegimenTributario")
    protected Long tipoRegimenTributario;
    @XmlElement(name = "FechaCaptura")
    protected String fechaCaptura;
    @XmlElement(name = "IdsPorcejateFondos")
    protected String idsPorcejateFondos;
    @XmlElement(name = "IdDir")
    protected Long idDir;
    @XmlElement(name = "NumeroPropuesta")
    protected Long numeroPropuesta;
    @XmlElement(name = "InstitucionOrigen")
    protected String institucionOrigen;
    @XmlElement(name = "Monto")
    protected String monto;
    @XmlElement(name = "TipoMonto")
    protected String tipoMonto;
    @XmlElement(name = "FolioSolicitud")
    protected String folioSolicitud;
    @XmlElement(name = "TipoTrabajador")
    protected String tipoTrabajador;
    @XmlElement(name = "ClaseCotizante")
    protected String claseCotizante;

    /**
     * Gets the value of the producto property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getProducto() {
        return producto;
    }

    /**
     * Sets the value of the producto property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setProducto(Long value) {
        this.producto = value;
    }

    /**
     * Gets the value of the fechaSuscripcion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFechaSuscripcion() {
        return fechaSuscripcion;
    }

    /**
     * Sets the value of the fechaSuscripcion property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFechaSuscripcion(String value) {
        this.fechaSuscripcion = value;
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

    /**
     * Gets the value of the dvCliente property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDvCliente() {
        return dvCliente;
    }

    /**
     * Sets the value of the dvCliente property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDvCliente(String value) {
        this.dvCliente = value;
    }

    /**
     * Gets the value of the sucursal property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSucursal() {
        return sucursal;
    }

    /**
     * Sets the value of the sucursal property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSucursal(String value) {
        this.sucursal = value;
    }

    /**
     * Gets the value of the fechaEmisionPoliza property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFechaEmisionPoliza() {
        return fechaEmisionPoliza;
    }

    /**
     * Sets the value of the fechaEmisionPoliza property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFechaEmisionPoliza(String value) {
        this.fechaEmisionPoliza = value;
    }

    /**
     * Gets the value of the ramo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getRamo() {
        return ramo;
    }

    /**
     * Sets the value of the ramo property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setRamo(Long value) {
        this.ramo = value;
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
     * Gets the value of the formaPago property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getFormaPago() {
        return formaPago;
    }

    /**
     * Sets the value of the formaPago property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setFormaPago(Long value) {
        this.formaPago = value;
    }

    /**
     * Gets the value of the tipoRegimenTributario property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getTipoRegimenTributario() {
        return tipoRegimenTributario;
    }

    /**
     * Sets the value of the tipoRegimenTributario property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setTipoRegimenTributario(Long value) {
        this.tipoRegimenTributario = value;
    }

    /**
     * Gets the value of the fechaCaptura property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * Sets the value of the fechaCaptura property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFechaCaptura(String value) {
        this.fechaCaptura = value;
    }

    /**
     * Gets the value of the idsPorcejateFondos property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdsPorcejateFondos() {
        return idsPorcejateFondos;
    }

    /**
     * Sets the value of the idsPorcejateFondos property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdsPorcejateFondos(String value) {
        this.idsPorcejateFondos = value;
    }

    /**
     * Gets the value of the idDir property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getIdDir() {
        return idDir;
    }

    /**
     * Sets the value of the idDir property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setIdDir(Long value) {
        this.idDir = value;
    }

    /**
     * Gets the value of the numeroPropuesta property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getNumeroPropuesta() {
        return numeroPropuesta;
    }

    /**
     * Sets the value of the numeroPropuesta property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setNumeroPropuesta(Long value) {
        this.numeroPropuesta = value;
    }

    /**
     * Gets the value of the institucionOrigen property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInstitucionOrigen() {
        return institucionOrigen;
    }

    /**
     * Sets the value of the institucionOrigen property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInstitucionOrigen(String value) {
        this.institucionOrigen = value;
    }

    /**
     * Gets the value of the monto property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMonto() {
        return monto;
    }

    /**
     * Sets the value of the monto property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMonto(String value) {
        this.monto = value;
    }

    /**
     * Gets the value of the tipoMonto property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTipoMonto() {
        return tipoMonto;
    }

    /**
     * Sets the value of the tipoMonto property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTipoMonto(String value) {
        this.tipoMonto = value;
    }

    /**
     * Gets the value of the folioSolicitud property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFolioSolicitud() {
        return folioSolicitud;
    }

    /**
     * Sets the value of the folioSolicitud property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFolioSolicitud(String value) {
        this.folioSolicitud = value;
    }

    /**
     * Gets the value of the tipoTrabajador property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTipoTrabajador() {
        return tipoTrabajador;
    }

    /**
     * Sets the value of the tipoTrabajador property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTipoTrabajador(String value) {
        this.tipoTrabajador = value;
    }

    /**
     * Gets the value of the claseCotizante property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClaseCotizante() {
        return claseCotizante;
    }

    /**
     * Sets the value of the claseCotizante property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClaseCotizante(String value) {
        this.claseCotizante = value;
    }

}
