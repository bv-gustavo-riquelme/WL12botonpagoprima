
package cl.bicevida.botonpago.ws.cuentainversion.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cl.bicevida.botonpago.ws.cuentainversion.types package.
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

    private final static QName _EliminarSometimiento_QNAME =
        new QName("http://ejb.bicevida.cl/", "eliminarSometimiento");
    private final static QName _GeneraIdAporteRegResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "generaIdAporteRegResponse");
    private final static QName _ObtenerInformacionRegimenResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "obtenerInformacionRegimenResponse");
    private final static QName _ObtenerTodosLosFondosParaSometimientoResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "obtenerTodosLosFondosParaSometimientoResponse");
    private final static QName _DatoNuloException_QNAME = new QName("http://ejb.bicevida.cl/", "DatoNuloException");
    private final static QName _ObtenerTodosLosFondosParaSometimiento_QNAME =
        new QName("http://ejb.bicevida.cl/", "obtenerTodosLosFondosParaSometimiento");
    private final static QName _ObtenerInformacionRegimen_QNAME =
        new QName("http://ejb.bicevida.cl/", "obtenerInformacionRegimen");
    private final static QName _GrabarSometimientoResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "grabarSometimientoResponse");
    private final static QName _ConsultaSaldosYFondosResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "consultaSaldosYFondosResponse");
    private final static QName _ObtenerFondosVigentesV2_QNAME =
        new QName("http://ejb.bicevida.cl/", "obtenerFondosVigentesV2");
    private final static QName _GeneraIdAporteReg_QNAME = new QName("http://ejb.bicevida.cl/", "generaIdAporteReg");
    private final static QName _SePuedeEliminarSometimiento_QNAME =
        new QName("http://ejb.bicevida.cl/", "sePuedeEliminarSometimiento");
    private final static QName _GrabarSometimiento_QNAME = new QName("http://ejb.bicevida.cl/", "grabarSometimiento");
    private final static QName _ConsultaSaldosYFondos_QNAME =
        new QName("http://ejb.bicevida.cl/", "consultaSaldosYFondos");
    private final static QName _ActivarCuenta_QNAME = new QName("http://ejb.bicevida.cl/", "activarCuenta");
    private final static QName _ConsultaEstructuraFondosPorId_QNAME =
        new QName("http://ejb.bicevida.cl/", "consultaEstructuraFondosPorId");
    private final static QName _ActivarCuentaResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "activarCuentaResponse");
    private final static QName _SePuedeEliminarSometimientoResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "sePuedeEliminarSometimientoResponse");
    private final static QName _ObtenerFondosVigentesV2Response_QNAME =
        new QName("http://ejb.bicevida.cl/", "obtenerFondosVigentesV2Response");
    private final static QName _ConsultarEstado_QNAME = new QName("http://ejb.bicevida.cl/", "consultarEstado");
    private final static QName _ConsultaEstructuraFondosPorIdResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "consultaEstructuraFondosPorIdResponse");
    private final static QName _GrabarDistribucionFondosAporteExtraordinarioReg_QNAME =
        new QName("http://ejb.bicevida.cl/", "grabarDistribucionFondosAporteExtraordinarioReg");
    private final static QName _EliminarSometimientoResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "eliminarSometimientoResponse");
    private final static QName _ConsultarEstadoResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "consultarEstadoResponse");
    private final static QName _GrabarDistribucionFondosAporteExtraordinarioRegResponse_QNAME =
        new QName("http://ejb.bicevida.cl/", "grabarDistribucionFondosAporteExtraordinarioRegResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cl.bicevida.botonpago.ws.cuentainversion.types
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ObtenerTodosLosFondosParaSometimientoResponse }
     *
     */
    public ObtenerTodosLosFondosParaSometimientoResponse createObtenerTodosLosFondosParaSometimientoResponse() {
        return new ObtenerTodosLosFondosParaSometimientoResponse();
    }

    /**
     * Create an instance of {@link DatoNuloException }
     *
     */
    public DatoNuloException createDatoNuloException() {
        return new DatoNuloException();
    }

    /**
     * Create an instance of {@link ObtenerTodosLosFondosParaSometimiento }
     *
     */
    public ObtenerTodosLosFondosParaSometimiento createObtenerTodosLosFondosParaSometimiento() {
        return new ObtenerTodosLosFondosParaSometimiento();
    }

    /**
     * Create an instance of {@link ObtenerInformacionRegimen }
     *
     */
    public ObtenerInformacionRegimen createObtenerInformacionRegimen() {
        return new ObtenerInformacionRegimen();
    }

    /**
     * Create an instance of {@link GrabarSometimientoResponse }
     *
     */
    public GrabarSometimientoResponse createGrabarSometimientoResponse() {
        return new GrabarSometimientoResponse();
    }

    /**
     * Create an instance of {@link ConsultaSaldosYFondosResponse }
     *
     */
    public ConsultaSaldosYFondosResponse createConsultaSaldosYFondosResponse() {
        return new ConsultaSaldosYFondosResponse();
    }

    /**
     * Create an instance of {@link ObtenerFondosVigentesV2 }
     *
     */
    public ObtenerFondosVigentesV2 createObtenerFondosVigentesV2() {
        return new ObtenerFondosVigentesV2();
    }

    /**
     * Create an instance of {@link GeneraIdAporteReg }
     *
     */
    public GeneraIdAporteReg createGeneraIdAporteReg() {
        return new GeneraIdAporteReg();
    }

    /**
     * Create an instance of {@link EliminarSometimiento }
     *
     */
    public EliminarSometimiento createEliminarSometimiento() {
        return new EliminarSometimiento();
    }

    /**
     * Create an instance of {@link GeneraIdAporteRegResponse }
     *
     */
    public GeneraIdAporteRegResponse createGeneraIdAporteRegResponse() {
        return new GeneraIdAporteRegResponse();
    }

    /**
     * Create an instance of {@link ObtenerInformacionRegimenResponse }
     *
     */
    public ObtenerInformacionRegimenResponse createObtenerInformacionRegimenResponse() {
        return new ObtenerInformacionRegimenResponse();
    }

    /**
     * Create an instance of {@link EliminarSometimientoResponse }
     *
     */
    public EliminarSometimientoResponse createEliminarSometimientoResponse() {
        return new EliminarSometimientoResponse();
    }

    /**
     * Create an instance of {@link GrabarDistribucionFondosAporteExtraordinarioRegResponse }
     *
     */
    public GrabarDistribucionFondosAporteExtraordinarioRegResponse createGrabarDistribucionFondosAporteExtraordinarioRegResponse() {
        return new GrabarDistribucionFondosAporteExtraordinarioRegResponse();
    }

    /**
     * Create an instance of {@link ConsultarEstadoResponse }
     *
     */
    public ConsultarEstadoResponse createConsultarEstadoResponse() {
        return new ConsultarEstadoResponse();
    }

    /**
     * Create an instance of {@link ConsultaSaldosYFondos }
     *
     */
    public ConsultaSaldosYFondos createConsultaSaldosYFondos() {
        return new ConsultaSaldosYFondos();
    }

    /**
     * Create an instance of {@link GrabarSometimiento }
     *
     */
    public GrabarSometimiento createGrabarSometimiento() {
        return new GrabarSometimiento();
    }

    /**
     * Create an instance of {@link SePuedeEliminarSometimiento }
     *
     */
    public SePuedeEliminarSometimiento createSePuedeEliminarSometimiento() {
        return new SePuedeEliminarSometimiento();
    }

    /**
     * Create an instance of {@link ActivarCuenta }
     *
     */
    public ActivarCuenta createActivarCuenta() {
        return new ActivarCuenta();
    }

    /**
     * Create an instance of {@link SePuedeEliminarSometimientoResponse }
     *
     */
    public SePuedeEliminarSometimientoResponse createSePuedeEliminarSometimientoResponse() {
        return new SePuedeEliminarSometimientoResponse();
    }

    /**
     * Create an instance of {@link ActivarCuentaResponse }
     *
     */
    public ActivarCuentaResponse createActivarCuentaResponse() {
        return new ActivarCuentaResponse();
    }

    /**
     * Create an instance of {@link ConsultaEstructuraFondosPorId }
     *
     */
    public ConsultaEstructuraFondosPorId createConsultaEstructuraFondosPorId() {
        return new ConsultaEstructuraFondosPorId();
    }

    /**
     * Create an instance of {@link ObtenerFondosVigentesV2Response }
     *
     */
    public ObtenerFondosVigentesV2Response createObtenerFondosVigentesV2Response() {
        return new ObtenerFondosVigentesV2Response();
    }

    /**
     * Create an instance of {@link ConsultarEstado }
     *
     */
    public ConsultarEstado createConsultarEstado() {
        return new ConsultarEstado();
    }

    /**
     * Create an instance of {@link ConsultaEstructuraFondosPorIdResponse }
     *
     */
    public ConsultaEstructuraFondosPorIdResponse createConsultaEstructuraFondosPorIdResponse() {
        return new ConsultaEstructuraFondosPorIdResponse();
    }

    /**
     * Create an instance of {@link GrabarDistribucionFondosAporteExtraordinarioReg }
     *
     */
    public GrabarDistribucionFondosAporteExtraordinarioReg createGrabarDistribucionFondosAporteExtraordinarioReg() {
        return new GrabarDistribucionFondosAporteExtraordinarioReg();
    }

    /**
     * Create an instance of {@link RetornoInformacionRegimen }
     *
     */
    public RetornoInformacionRegimen createRetornoInformacionRegimen() {
        return new RetornoInformacionRegimen();
    }

    /**
     * Create an instance of {@link SaldosYFondos }
     *
     */
    public SaldosYFondos createSaldosYFondos() {
        return new SaldosYFondos();
    }

    /**
     * Create an instance of {@link RetornoActivarCuenta }
     *
     */
    public RetornoActivarCuenta createRetornoActivarCuenta() {
        return new RetornoActivarCuenta();
    }

    /**
     * Create an instance of {@link RetGrabadoDistFondo }
     *
     */
    public RetGrabadoDistFondo createRetGrabadoDistFondo() {
        return new RetGrabadoDistFondo();
    }

    /**
     * Create an instance of {@link FondoPorcentaje }
     *
     */
    public FondoPorcentaje createFondoPorcentaje() {
        return new FondoPorcentaje();
    }

    /**
     * Create an instance of {@link FondoSometimientoRetorno }
     *
     */
    public FondoSometimientoRetorno createFondoSometimientoRetorno() {
        return new FondoSometimientoRetorno();
    }

    /**
     * Create an instance of {@link EstructuraFondo }
     *
     */
    public EstructuraFondo createEstructuraFondo() {
        return new EstructuraFondo();
    }

    /**
     * Create an instance of {@link RetornoGrabarSometimiento }
     *
     */
    public RetornoGrabarSometimiento createRetornoGrabarSometimiento() {
        return new RetornoGrabarSometimiento();
    }

    /**
     * Create an instance of {@link IdAporte }
     *
     */
    public IdAporte createIdAporte() {
        return new IdAporte();
    }

    /**
     * Create an instance of {@link RetornoEliminarSometimiento }
     *
     */
    public RetornoEliminarSometimiento createRetornoEliminarSometimiento() {
        return new RetornoEliminarSometimiento();
    }

    /**
     * Create an instance of {@link FondoRetorno }
     *
     */
    public FondoRetorno createFondoRetorno() {
        return new FondoRetorno();
    }

    /**
     * Create an instance of {@link DetalleColorRetorno }
     *
     */
    public DetalleColorRetorno createDetalleColorRetorno() {
        return new DetalleColorRetorno();
    }

    /**
     * Create an instance of {@link FondoW }
     *
     */
    public FondoW createFondoW() {
        return new FondoW();
    }

    /**
     * Create an instance of {@link DetalleColor }
     *
     */
    public DetalleColor createDetalleColor() {
        return new DetalleColor();
    }

    /**
     * Create an instance of {@link RetornoConsultaEstado }
     *
     */
    public RetornoConsultaEstado createRetornoConsultaEstado() {
        return new RetornoConsultaEstado();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarSometimiento }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "eliminarSometimiento")
    public JAXBElement<EliminarSometimiento> createEliminarSometimiento(EliminarSometimiento value) {
        return new JAXBElement<EliminarSometimiento>(_EliminarSometimiento_QNAME, EliminarSometimiento.class, null,
                                                     value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GeneraIdAporteRegResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "generaIdAporteRegResponse")
    public JAXBElement<GeneraIdAporteRegResponse> createGeneraIdAporteRegResponse(GeneraIdAporteRegResponse value) {
        return new JAXBElement<GeneraIdAporteRegResponse>(_GeneraIdAporteRegResponse_QNAME,
                                                          GeneraIdAporteRegResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerInformacionRegimenResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "obtenerInformacionRegimenResponse")
    public JAXBElement<ObtenerInformacionRegimenResponse> createObtenerInformacionRegimenResponse(ObtenerInformacionRegimenResponse value) {
        return new JAXBElement<ObtenerInformacionRegimenResponse>(_ObtenerInformacionRegimenResponse_QNAME,
                                                                  ObtenerInformacionRegimenResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerTodosLosFondosParaSometimientoResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "obtenerTodosLosFondosParaSometimientoResponse")
    public JAXBElement<ObtenerTodosLosFondosParaSometimientoResponse> createObtenerTodosLosFondosParaSometimientoResponse(ObtenerTodosLosFondosParaSometimientoResponse value) {
        return new JAXBElement<ObtenerTodosLosFondosParaSometimientoResponse>(_ObtenerTodosLosFondosParaSometimientoResponse_QNAME,
                                                                              ObtenerTodosLosFondosParaSometimientoResponse.class,
                                                                              null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DatoNuloException }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "DatoNuloException")
    public JAXBElement<DatoNuloException> createDatoNuloException(DatoNuloException value) {
        return new JAXBElement<DatoNuloException>(_DatoNuloException_QNAME, DatoNuloException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerTodosLosFondosParaSometimiento }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "obtenerTodosLosFondosParaSometimiento")
    public JAXBElement<ObtenerTodosLosFondosParaSometimiento> createObtenerTodosLosFondosParaSometimiento(ObtenerTodosLosFondosParaSometimiento value) {
        return new JAXBElement<ObtenerTodosLosFondosParaSometimiento>(_ObtenerTodosLosFondosParaSometimiento_QNAME,
                                                                      ObtenerTodosLosFondosParaSometimiento.class, null,
                                                                      value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerInformacionRegimen }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "obtenerInformacionRegimen")
    public JAXBElement<ObtenerInformacionRegimen> createObtenerInformacionRegimen(ObtenerInformacionRegimen value) {
        return new JAXBElement<ObtenerInformacionRegimen>(_ObtenerInformacionRegimen_QNAME,
                                                          ObtenerInformacionRegimen.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarSometimientoResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "grabarSometimientoResponse")
    public JAXBElement<GrabarSometimientoResponse> createGrabarSometimientoResponse(GrabarSometimientoResponse value) {
        return new JAXBElement<GrabarSometimientoResponse>(_GrabarSometimientoResponse_QNAME,
                                                           GrabarSometimientoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultaSaldosYFondosResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "consultaSaldosYFondosResponse")
    public JAXBElement<ConsultaSaldosYFondosResponse> createConsultaSaldosYFondosResponse(ConsultaSaldosYFondosResponse value) {
        return new JAXBElement<ConsultaSaldosYFondosResponse>(_ConsultaSaldosYFondosResponse_QNAME,
                                                              ConsultaSaldosYFondosResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerFondosVigentesV2 }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "obtenerFondosVigentesV2")
    public JAXBElement<ObtenerFondosVigentesV2> createObtenerFondosVigentesV2(ObtenerFondosVigentesV2 value) {
        return new JAXBElement<ObtenerFondosVigentesV2>(_ObtenerFondosVigentesV2_QNAME, ObtenerFondosVigentesV2.class,
                                                        null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GeneraIdAporteReg }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "generaIdAporteReg")
    public JAXBElement<GeneraIdAporteReg> createGeneraIdAporteReg(GeneraIdAporteReg value) {
        return new JAXBElement<GeneraIdAporteReg>(_GeneraIdAporteReg_QNAME, GeneraIdAporteReg.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SePuedeEliminarSometimiento }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "sePuedeEliminarSometimiento")
    public JAXBElement<SePuedeEliminarSometimiento> createSePuedeEliminarSometimiento(SePuedeEliminarSometimiento value) {
        return new JAXBElement<SePuedeEliminarSometimiento>(_SePuedeEliminarSometimiento_QNAME,
                                                            SePuedeEliminarSometimiento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarSometimiento }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "grabarSometimiento")
    public JAXBElement<GrabarSometimiento> createGrabarSometimiento(GrabarSometimiento value) {
        return new JAXBElement<GrabarSometimiento>(_GrabarSometimiento_QNAME, GrabarSometimiento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultaSaldosYFondos }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "consultaSaldosYFondos")
    public JAXBElement<ConsultaSaldosYFondos> createConsultaSaldosYFondos(ConsultaSaldosYFondos value) {
        return new JAXBElement<ConsultaSaldosYFondos>(_ConsultaSaldosYFondos_QNAME, ConsultaSaldosYFondos.class, null,
                                                      value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActivarCuenta }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "activarCuenta")
    public JAXBElement<ActivarCuenta> createActivarCuenta(ActivarCuenta value) {
        return new JAXBElement<ActivarCuenta>(_ActivarCuenta_QNAME, ActivarCuenta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultaEstructuraFondosPorId }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "consultaEstructuraFondosPorId")
    public JAXBElement<ConsultaEstructuraFondosPorId> createConsultaEstructuraFondosPorId(ConsultaEstructuraFondosPorId value) {
        return new JAXBElement<ConsultaEstructuraFondosPorId>(_ConsultaEstructuraFondosPorId_QNAME,
                                                              ConsultaEstructuraFondosPorId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActivarCuentaResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "activarCuentaResponse")
    public JAXBElement<ActivarCuentaResponse> createActivarCuentaResponse(ActivarCuentaResponse value) {
        return new JAXBElement<ActivarCuentaResponse>(_ActivarCuentaResponse_QNAME, ActivarCuentaResponse.class, null,
                                                      value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SePuedeEliminarSometimientoResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "sePuedeEliminarSometimientoResponse")
    public JAXBElement<SePuedeEliminarSometimientoResponse> createSePuedeEliminarSometimientoResponse(SePuedeEliminarSometimientoResponse value) {
        return new JAXBElement<SePuedeEliminarSometimientoResponse>(_SePuedeEliminarSometimientoResponse_QNAME,
                                                                    SePuedeEliminarSometimientoResponse.class, null,
                                                                    value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerFondosVigentesV2Response }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "obtenerFondosVigentesV2Response")
    public JAXBElement<ObtenerFondosVigentesV2Response> createObtenerFondosVigentesV2Response(ObtenerFondosVigentesV2Response value) {
        return new JAXBElement<ObtenerFondosVigentesV2Response>(_ObtenerFondosVigentesV2Response_QNAME,
                                                                ObtenerFondosVigentesV2Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarEstado }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "consultarEstado")
    public JAXBElement<ConsultarEstado> createConsultarEstado(ConsultarEstado value) {
        return new JAXBElement<ConsultarEstado>(_ConsultarEstado_QNAME, ConsultarEstado.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultaEstructuraFondosPorIdResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "consultaEstructuraFondosPorIdResponse")
    public JAXBElement<ConsultaEstructuraFondosPorIdResponse> createConsultaEstructuraFondosPorIdResponse(ConsultaEstructuraFondosPorIdResponse value) {
        return new JAXBElement<ConsultaEstructuraFondosPorIdResponse>(_ConsultaEstructuraFondosPorIdResponse_QNAME,
                                                                      ConsultaEstructuraFondosPorIdResponse.class, null,
                                                                      value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarDistribucionFondosAporteExtraordinarioReg }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "grabarDistribucionFondosAporteExtraordinarioReg")
    public JAXBElement<GrabarDistribucionFondosAporteExtraordinarioReg> createGrabarDistribucionFondosAporteExtraordinarioReg(GrabarDistribucionFondosAporteExtraordinarioReg value) {
        return new JAXBElement<GrabarDistribucionFondosAporteExtraordinarioReg>(_GrabarDistribucionFondosAporteExtraordinarioReg_QNAME,
                                                                                GrabarDistribucionFondosAporteExtraordinarioReg.class,
                                                                                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarSometimientoResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "eliminarSometimientoResponse")
    public JAXBElement<EliminarSometimientoResponse> createEliminarSometimientoResponse(EliminarSometimientoResponse value) {
        return new JAXBElement<EliminarSometimientoResponse>(_EliminarSometimientoResponse_QNAME,
                                                             EliminarSometimientoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarEstadoResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/", name = "consultarEstadoResponse")
    public JAXBElement<ConsultarEstadoResponse> createConsultarEstadoResponse(ConsultarEstadoResponse value) {
        return new JAXBElement<ConsultarEstadoResponse>(_ConsultarEstadoResponse_QNAME, ConsultarEstadoResponse.class,
                                                        null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarDistribucionFondosAporteExtraordinarioRegResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://ejb.bicevida.cl/",
                    name = "grabarDistribucionFondosAporteExtraordinarioRegResponse")
    public JAXBElement<GrabarDistribucionFondosAporteExtraordinarioRegResponse> createGrabarDistribucionFondosAporteExtraordinarioRegResponse(GrabarDistribucionFondosAporteExtraordinarioRegResponse value) {
        return new JAXBElement<GrabarDistribucionFondosAporteExtraordinarioRegResponse>(_GrabarDistribucionFondosAporteExtraordinarioRegResponse_QNAME,
                                                                                        GrabarDistribucionFondosAporteExtraordinarioRegResponse.class,
                                                                                        null, value);
    }

}
