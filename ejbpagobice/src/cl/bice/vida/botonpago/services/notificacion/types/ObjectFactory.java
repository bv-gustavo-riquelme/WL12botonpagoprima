
package cl.bice.vida.botonpago.services.notificacion.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cl.bice.vida.botonpago.services.notificacion.types package.
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

    private final static QName _ApplicationModelExceptionElement_QNAME =
        new QName("http://webservices.model.integracion.bicevida.cl/", "ApplicationModelExceptionElement");
    private final static QName _InformarRecaudacionResponse_QNAME =
        new QName("http://webservices.model.integracion.bicevida.cl/", "informarRecaudacionResponse");
    private final static QName _InformarRecaudacion_QNAME =
        new QName("http://webservices.model.integracion.bicevida.cl/", "informarRecaudacion");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cl.bice.vida.botonpago.services.notificacion.types
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InformarRecaudacion }
     *
     */
    public InformarRecaudacion createInformarRecaudacion() {
        return new InformarRecaudacion();
    }

    /**
     * Create an instance of {@link InformarRecaudacionResponse }
     *
     */
    public InformarRecaudacionResponse createInformarRecaudacionResponse() {
        return new InformarRecaudacionResponse();
    }

    /**
     * Create an instance of {@link ApplicationModelException }
     *
     */
    public ApplicationModelException createApplicationModelException() {
        return new ApplicationModelException();
    }

    /**
     * Create an instance of {@link InformarRecaudacionIn }
     *
     */
    public InformarRecaudacionIn createInformarRecaudacionIn() {
        return new InformarRecaudacionIn();
    }

    /**
     * Create an instance of {@link InformarRecaudacionOut }
     *
     */
    public InformarRecaudacionOut createInformarRecaudacionOut() {
        return new InformarRecaudacionOut();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplicationModelException }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservices.model.integracion.bicevida.cl/",
                    name = "ApplicationModelExceptionElement")
    public JAXBElement<ApplicationModelException> createApplicationModelExceptionElement(ApplicationModelException value) {
        return new JAXBElement<ApplicationModelException>(_ApplicationModelExceptionElement_QNAME,
                                                          ApplicationModelException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InformarRecaudacionResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservices.model.integracion.bicevida.cl/",
                    name = "informarRecaudacionResponse")
    public JAXBElement<InformarRecaudacionResponse> createInformarRecaudacionResponse(InformarRecaudacionResponse value) {
        return new JAXBElement<InformarRecaudacionResponse>(_InformarRecaudacionResponse_QNAME,
                                                            InformarRecaudacionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InformarRecaudacion }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservices.model.integracion.bicevida.cl/", name = "informarRecaudacion")
    public JAXBElement<InformarRecaudacion> createInformarRecaudacion(InformarRecaudacion value) {
        return new JAXBElement<InformarRecaudacion>(_InformarRecaudacion_QNAME, InformarRecaudacion.class, null, value);
    }

}
