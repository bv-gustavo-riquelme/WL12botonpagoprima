
package cl.bice.xmlbeans.servipag.pec;

import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element ref="{}Header"/>
 *         &lt;element ref="{}Documentos" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "header", "documentos" })
@XmlRootElement(name = "Servipag")
public class Servipag {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "Documentos", required = true)
    protected List<Documentos> documentos;

    /**
     * Gets the value of the header property.
     *
     * @return
     * possible object is
     * {@link cl.bice.xmlbeans.servipag.pec.Header}
     *
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     *
     * @param value
     * allowed object is
     * {@link cl.bice.xmlbeans.servipag.pec.Header}
     *
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * Gets the value of the documentos property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentos property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getDocumentos().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link cl.bice.xmlbeans.servipag.pec.Documentos}
     *
     *
     */
    public List<Documentos> getDocumentos() {
        if (documentos == null) {
            documentos = new ArrayList<Documentos>();
        }
        return this.documentos;
    }

}
