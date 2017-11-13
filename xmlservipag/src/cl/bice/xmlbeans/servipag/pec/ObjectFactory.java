
package cl.bice.xmlbeans.servipag.pec;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the generated package.
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

    private final static QName _FechaVencimiento_QNAME = new QName("", "FechaVencimiento");
    private final static QName _EmailCliente_QNAME = new QName("", "EmailCliente");
    private final static QName _Identificador_QNAME = new QName("", "Identificador");
    private final static QName _NombreCliente_QNAME = new QName("", "NombreCliente");
    private final static QName _Monto_QNAME = new QName("", "Monto");
    private final static QName _NumeroBoletas_QNAME = new QName("", "NumeroBoletas");
    private final static QName _MontoTotalDeuda_QNAME = new QName("", "MontoTotalDeuda");
    private final static QName _CodigoCanalPago_QNAME = new QName("", "CodigoCanalPago");
    private final static QName _Boleta_QNAME = new QName("", "Boleta");
    private final static QName _FirmaEPS_QNAME = new QName("", "FirmaEPS");
    private final static QName _RutCliente_QNAME = new QName("", "RutCliente");
    private final static QName _IdSubTx_QNAME = new QName("", "IdSubTx");
    private final static QName _Version_QNAME = new QName("", "Version");
    private final static QName _FechaPago_QNAME = new QName("", "FechaPago");
    private final static QName _IdTxPago_QNAME = new QName("", "IdTxPago");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link cl.bice.xmlbeans.servipag.pec.Documentos}
     *
     */
    public Documentos createDocumentos() {
        return new Documentos();
    }

    /**
     * Create an instance of {@link cl.bice.xmlbeans.servipag.pec.Servipag}
     *
     */
    public Servipag createServipag() {
        return new Servipag();
    }

    /**
     * Create an instance of {@link cl.bice.xmlbeans.servipag.pec.Header}
     *
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "FechaVencimiento")
    public JAXBElement<String> createFechaVencimiento(String value) {
        return new JAXBElement<String>(_FechaVencimiento_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "EmailCliente")
    public JAXBElement<String> createEmailCliente(String value) {
        return new JAXBElement<String>(_EmailCliente_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "Identificador")
    public JAXBElement<String> createIdentificador(String value) {
        return new JAXBElement<String>(_Identificador_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "NombreCliente")
    public JAXBElement<String> createNombreCliente(String value) {
        return new JAXBElement<String>(_NombreCliente_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "Monto")
    public JAXBElement<Long> createMonto(Long value) {
        return new JAXBElement<Long>(_Monto_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "NumeroBoletas")
    public JAXBElement<Integer> createNumeroBoletas(Integer value) {
        return new JAXBElement<Integer>(_NumeroBoletas_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "MontoTotalDeuda")
    public JAXBElement<Long> createMontoTotalDeuda(Long value) {
        return new JAXBElement<Long>(_MontoTotalDeuda_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "CodigoCanalPago")
    public JAXBElement<String> createCodigoCanalPago(String value) {
        return new JAXBElement<String>(_CodigoCanalPago_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "Boleta")
    public JAXBElement<String> createBoleta(String value) {
        return new JAXBElement<String>(_Boleta_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "FirmaEPS")
    public JAXBElement<String> createFirmaEPS(String value) {
        return new JAXBElement<String>(_FirmaEPS_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "RutCliente")
    public JAXBElement<String> createRutCliente(String value) {
        return new JAXBElement<String>(_RutCliente_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "IdSubTx")
    public JAXBElement<String> createIdSubTx(String value) {
        return new JAXBElement<String>(_IdSubTx_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "Version")
    public JAXBElement<String> createVersion(String value) {
        return new JAXBElement<String>(_Version_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "FechaPago")
    public JAXBElement<String> createFechaPago(String value) {
        return new JAXBElement<String>(_FechaPago_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "IdTxPago")
    public JAXBElement<String> createIdTxPago(String value) {
        return new JAXBElement<String>(_IdTxPago_QNAME, String.class, null, value);
    }

}
