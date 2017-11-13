
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for fondoRetorno complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="fondoRetorno">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="costoRemuneracionAnual" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detalleColor" type="{http://ejb.bicevida.cl/}detalleColorRetorno" minOccurs="0"/>
 *         &lt;element name="diasRetiro" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fecCreacion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fecModificacion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fecVigencia" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fondoRecaudacion" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idFondo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="indicadorTipo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="nemotecnico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreFondo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ordenRiesgo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="permanenciaMin" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="riesgo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="serie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="urlCartilla" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuarioCreacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuarioModificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valCompra" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="valVenta" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="vigencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fondoRetorno", propOrder = {
         "costoRemuneracionAnual", "descripcion", "detalleColor", "diasRetiro", "fecCreacion", "fecModificacion",
         "fecVigencia", "fondoRecaudacion", "idFondo", "indicadorTipo", "nemotecnico", "nombreFondo", "ordenRiesgo",
         "permanenciaMin", "riesgo", "serie", "urlCartilla", "usuarioCreacion", "usuarioModificacion", "valCompra",
         "valVenta", "vigencia"
    })
public class FondoRetorno {

    protected Double costoRemuneracionAnual;
    protected String descripcion;
    protected DetalleColorRetorno detalleColor;
    protected Long diasRetiro;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fecCreacion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fecModificacion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fecVigencia;
    protected Long fondoRecaudacion;
    protected Long idFondo;
    protected Long indicadorTipo;
    protected String nemotecnico;
    protected String nombreFondo;
    protected Long ordenRiesgo;
    protected Long permanenciaMin;
    protected Long riesgo;
    protected String serie;
    protected String urlCartilla;
    protected String usuarioCreacion;
    protected String usuarioModificacion;
    protected Long valCompra;
    protected Long valVenta;
    protected String vigencia;

    /**
     * Gets the value of the costoRemuneracionAnual property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getCostoRemuneracionAnual() {
        return costoRemuneracionAnual;
    }

    /**
     * Sets the value of the costoRemuneracionAnual property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setCostoRemuneracionAnual(Double value) {
        this.costoRemuneracionAnual = value;
    }

    /**
     * Gets the value of the descripcion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the detalleColor property.
     *
     * @return
     *     possible object is
     *     {@link DetalleColorRetorno }
     *
     */
    public DetalleColorRetorno getDetalleColor() {
        return detalleColor;
    }

    /**
     * Sets the value of the detalleColor property.
     *
     * @param value
     *     allowed object is
     *     {@link DetalleColorRetorno }
     *
     */
    public void setDetalleColor(DetalleColorRetorno value) {
        this.detalleColor = value;
    }

    /**
     * Gets the value of the diasRetiro property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getDiasRetiro() {
        return diasRetiro;
    }

    /**
     * Sets the value of the diasRetiro property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setDiasRetiro(Long value) {
        this.diasRetiro = value;
    }

    /**
     * Gets the value of the fecCreacion property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getFecCreacion() {
        return fecCreacion;
    }

    /**
     * Sets the value of the fecCreacion property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setFecCreacion(XMLGregorianCalendar value) {
        this.fecCreacion = value;
    }

    /**
     * Gets the value of the fecModificacion property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getFecModificacion() {
        return fecModificacion;
    }

    /**
     * Sets the value of the fecModificacion property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setFecModificacion(XMLGregorianCalendar value) {
        this.fecModificacion = value;
    }

    /**
     * Gets the value of the fecVigencia property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getFecVigencia() {
        return fecVigencia;
    }

    /**
     * Sets the value of the fecVigencia property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setFecVigencia(XMLGregorianCalendar value) {
        this.fecVigencia = value;
    }

    /**
     * Gets the value of the fondoRecaudacion property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getFondoRecaudacion() {
        return fondoRecaudacion;
    }

    /**
     * Sets the value of the fondoRecaudacion property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setFondoRecaudacion(Long value) {
        this.fondoRecaudacion = value;
    }

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
     * Gets the value of the indicadorTipo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getIndicadorTipo() {
        return indicadorTipo;
    }

    /**
     * Sets the value of the indicadorTipo property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setIndicadorTipo(Long value) {
        this.indicadorTipo = value;
    }

    /**
     * Gets the value of the nemotecnico property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNemotecnico() {
        return nemotecnico;
    }

    /**
     * Sets the value of the nemotecnico property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNemotecnico(String value) {
        this.nemotecnico = value;
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

    /**
     * Gets the value of the permanenciaMin property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getPermanenciaMin() {
        return permanenciaMin;
    }

    /**
     * Sets the value of the permanenciaMin property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setPermanenciaMin(Long value) {
        this.permanenciaMin = value;
    }

    /**
     * Gets the value of the riesgo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getRiesgo() {
        return riesgo;
    }

    /**
     * Sets the value of the riesgo property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setRiesgo(Long value) {
        this.riesgo = value;
    }

    /**
     * Gets the value of the serie property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Sets the value of the serie property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSerie(String value) {
        this.serie = value;
    }

    /**
     * Gets the value of the urlCartilla property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUrlCartilla() {
        return urlCartilla;
    }

    /**
     * Sets the value of the urlCartilla property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUrlCartilla(String value) {
        this.urlCartilla = value;
    }

    /**
     * Gets the value of the usuarioCreacion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    /**
     * Sets the value of the usuarioCreacion property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUsuarioCreacion(String value) {
        this.usuarioCreacion = value;
    }

    /**
     * Gets the value of the usuarioModificacion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    /**
     * Sets the value of the usuarioModificacion property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUsuarioModificacion(String value) {
        this.usuarioModificacion = value;
    }

    /**
     * Gets the value of the valCompra property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getValCompra() {
        return valCompra;
    }

    /**
     * Sets the value of the valCompra property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setValCompra(Long value) {
        this.valCompra = value;
    }

    /**
     * Gets the value of the valVenta property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getValVenta() {
        return valVenta;
    }

    /**
     * Sets the value of the valVenta property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setValVenta(Long value) {
        this.valVenta = value;
    }

    /**
     * Gets the value of the vigencia property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVigencia() {
        return vigencia;
    }

    /**
     * Sets the value of the vigencia property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVigencia(String value) {
        this.vigencia = value;
    }

}
