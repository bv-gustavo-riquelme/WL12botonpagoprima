
package cl.bice.xmlbeans.servipag.respuesta;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cl.bice.xmlbeans.servipag.respuesta package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EstadoPago_QNAME = new QName("", "EstadoPago");
    private final static QName _FirmaServipag_QNAME = new QName("", "FirmaServipag");
    private final static QName _IdTxCliente_QNAME = new QName("", "IdTxCliente");
    private final static QName _MensajePago_QNAME = new QName("", "MensajePago");
    private final static QName _IdTrxServipag_QNAME = new QName("", "IdTrxServipag");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cl.bice.xmlbeans.servipag.respuesta
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Servipag }
     *
     */
    public Servipag createServipag() {
        return new Servipag();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "EstadoPago")
    public JAXBElement<String> createEstadoPago(String value) {
        return new JAXBElement<String>(_EstadoPago_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "FirmaServipag")
    public JAXBElement<String> createFirmaServipag(String value) {
        return new JAXBElement<String>(_FirmaServipag_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "IdTxCliente")
    public JAXBElement<String> createIdTxCliente(String value) {
        return new JAXBElement<String>(_IdTxCliente_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "MensajePago")
    public JAXBElement<String> createMensajePago(String value) {
        return new JAXBElement<String>(_MensajePago_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "IdTrxServipag")
    public JAXBElement<String> createIdTrxServipag(String value) {
        return new JAXBElement<String>(_IdTrxServipag_QNAME, String.class, null, value);
    }

}
