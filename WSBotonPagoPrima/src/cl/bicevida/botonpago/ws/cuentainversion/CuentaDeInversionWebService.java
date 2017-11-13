
package cl.bicevida.botonpago.ws.cuentainversion;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import cl.bicevida.botonpago.ws.cuentainversion.types.EstructuraFondo;
import cl.bicevida.botonpago.ws.cuentainversion.types.FondoPorcentaje;
import cl.bicevida.botonpago.ws.cuentainversion.types.FondoRetorno;
import cl.bicevida.botonpago.ws.cuentainversion.types.FondoSometimientoRetorno;
import cl.bicevida.botonpago.ws.cuentainversion.types.IdAporte;
import cl.bicevida.botonpago.ws.cuentainversion.types.ObjectFactory;
import cl.bicevida.botonpago.ws.cuentainversion.types.RetGrabadoDistFondo;
import cl.bicevida.botonpago.ws.cuentainversion.types.RetornoActivarCuenta;
import cl.bicevida.botonpago.ws.cuentainversion.types.RetornoConsultaEstado;
import cl.bicevida.botonpago.ws.cuentainversion.types.RetornoEliminarSometimiento;
import cl.bicevida.botonpago.ws.cuentainversion.types.RetornoGrabarSometimiento;
import cl.bicevida.botonpago.ws.cuentainversion.types.RetornoInformacionRegimen;
import cl.bicevida.botonpago.ws.cuentainversion.types.SaldosYFondos;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10-b140319.1121
 * Generated source version: 2.2
 *
 */
@WebService(name = "CuentaDeInversionWebService", targetNamespace = "http://ejb.bicevida.cl/")
@XmlSeeAlso({ ObjectFactory.class })
public interface CuentaDeInversionWebService {


    /**
     *
     * @param idProducto
     * @return
     *     returns java.util.List<cl.bicevida.botonpago.ws.cuentainversion.types.FondoRetorno>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "obtenerFondosVigentesV2", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.ObtenerFondosVigentesV2")
    @ResponseWrapper(localName = "obtenerFondosVigentesV2Response", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.ObtenerFondosVigentesV2Response")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/obtenerFondosVigentesV2Request",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/obtenerFondosVigentesV2Response")
    public List<FondoRetorno> obtenerFondosVigentesV2(@WebParam(name = "idProducto", targetNamespace = "")
                                                      Long idProducto);

    /**
     *
     * @param tipoTrabajador
     * @param sucursal
     * @param rutCliente
     * @param formaPago
     * @param fechaEmisionPoliza
     * @param usuario
     * @param folioSolicitud
     * @param tipoRegimenTributario
     * @param claseCotizante
     * @param numeroPropuesta
     * @param ramo
     * @param fechaCaptura
     * @param idsPorcejateFondos
     * @param numeroPoliza
     * @param tipoMonto
     * @param producto
     * @param dvCliente
     * @param fechaSuscripcion
     * @param monto
     * @param institucionOrigen
     * @param idDir
     * @return
     *     returns cl.bicevida.botonpago.ws.cuentainversion.types.RetornoGrabarSometimiento
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "grabarSometimiento", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.GrabarSometimiento")
    @ResponseWrapper(localName = "grabarSometimientoResponse", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.GrabarSometimientoResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/grabarSometimientoRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/grabarSometimientoResponse")
    public RetornoGrabarSometimiento grabarSometimiento(@WebParam(name = "Producto", targetNamespace = "")
                                                        Long producto,
                                                        @WebParam(name = "FechaSuscripcion", targetNamespace = "")
                                                        String fechaSuscripcion,
                                                        @WebParam(name = "RutCliente", targetNamespace = "")
                                                        Long rutCliente,
                                                        @WebParam(name = "DvCliente", targetNamespace = "")
                                                        String dvCliente,
                                                        @WebParam(name = "Sucursal", targetNamespace = "")
                                                        String sucursal,
                                                        @WebParam(name = "FechaEmisionPoliza", targetNamespace = "")
                                                        String fechaEmisionPoliza,
                                                        @WebParam(name = "Ramo", targetNamespace = "") Long ramo,
                                                        @WebParam(name = "NumeroPoliza", targetNamespace = "")
                                                        Long numeroPoliza,
                                                        @WebParam(name = "Usuario", targetNamespace = "")
                                                        String usuario,
                                                        @WebParam(name = "FormaPago", targetNamespace = "")
                                                        Long formaPago,
                                                        @WebParam(name = "TipoRegimenTributario", targetNamespace = "")
                                                        Long tipoRegimenTributario,
                                                        @WebParam(name = "FechaCaptura", targetNamespace = "")
                                                        String fechaCaptura,
                                                        @WebParam(name = "IdsPorcejateFondos", targetNamespace = "")
                                                        String idsPorcejateFondos,
                                                        @WebParam(name = "IdDir", targetNamespace = "") Long idDir,
                                                        @WebParam(name = "NumeroPropuesta", targetNamespace = "")
                                                        Long numeroPropuesta,
                                                        @WebParam(name = "InstitucionOrigen", targetNamespace = "")
                                                        String institucionOrigen,
                                                        @WebParam(name = "Monto", targetNamespace = "") String monto,
                                                        @WebParam(name = "TipoMonto", targetNamespace = "")
                                                        String tipoMonto,
                                                        @WebParam(name = "FolioSolicitud", targetNamespace = "")
                                                        String folioSolicitud,
                                                        @WebParam(name = "TipoTrabajador", targetNamespace = "")
                                                        String tipoTrabajador,
                                                        @WebParam(name = "ClaseCotizante", targetNamespace = "")
                                                        String claseCotizante);

    /**
     *
     * @param rutCliente
     * @param usuario
     * @param numeroPoliza
     * @param ramo
     * @return
     *     returns cl.bicevida.botonpago.ws.cuentainversion.types.RetornoActivarCuenta
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "activarCuenta", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.ActivarCuenta")
    @ResponseWrapper(localName = "activarCuentaResponse", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.ActivarCuentaResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/activarCuentaRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/activarCuentaResponse")
    public RetornoActivarCuenta activarCuenta(@WebParam(name = "numeroPoliza", targetNamespace = "") Long numeroPoliza,
                                              @WebParam(name = "rutCliente", targetNamespace = "") Long rutCliente,
                                              @WebParam(name = "usuario", targetNamespace = "") String usuario,
                                              @WebParam(name = "ramo", targetNamespace = "") Long ramo);

    /**
     *
     * @param idProducto
     * @return
     *     returns java.util.List<cl.bicevida.botonpago.ws.cuentainversion.types.FondoSometimientoRetorno>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "obtenerTodosLosFondosParaSometimiento", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.ObtenerTodosLosFondosParaSometimiento")
    @ResponseWrapper(localName = "obtenerTodosLosFondosParaSometimientoResponse",
                     targetNamespace = "http://ejb.bicevida.cl/",
                     className =
                     "cl.bicevida.botonpago.ws.cuentainversion.types.ObtenerTodosLosFondosParaSometimientoResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/obtenerTodosLosFondosParaSometimientoRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/obtenerTodosLosFondosParaSometimientoResponse")
    public List<FondoSometimientoRetorno> obtenerTodosLosFondosParaSometimiento(@WebParam(name = "idProducto",
                                                                                          targetNamespace = "")
                                                                                Long idProducto);

    /**
     *
     * @param rutCliente
     * @param numeroPoliza
     * @return
     *     returns cl.bicevida.botonpago.ws.cuentainversion.types.RetornoEliminarSometimiento
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "eliminarSometimiento", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.EliminarSometimiento")
    @ResponseWrapper(localName = "eliminarSometimientoResponse", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.EliminarSometimientoResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/eliminarSometimientoRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/eliminarSometimientoResponse")
    public RetornoEliminarSometimiento eliminarSometimiento(@WebParam(name = "RutCliente", targetNamespace = "")
                                                            Long rutCliente,
                                                            @WebParam(name = "NumeroPoliza", targetNamespace = "")
                                                            Long numeroPoliza);

    /**
     *
     * @param rutCliente
     * @param numeroPoliza
     * @return
     *     returns cl.bicevida.botonpago.ws.cuentainversion.types.RetornoEliminarSometimiento
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sePuedeEliminarSometimiento", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.SePuedeEliminarSometimiento")
    @ResponseWrapper(localName = "sePuedeEliminarSometimientoResponse", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.SePuedeEliminarSometimientoResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/sePuedeEliminarSometimientoRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/sePuedeEliminarSometimientoResponse")
    public RetornoEliminarSometimiento sePuedeEliminarSometimiento(@WebParam(name = "RutCliente", targetNamespace = "")
                                                                   Long rutCliente,
                                                                   @WebParam(name = "NumeroPoliza",
                                                                             targetNamespace = "") Long numeroPoliza);

    /**
     *
     * @param rut
     * @return
     *     returns java.util.List<cl.bicevida.botonpago.ws.cuentainversion.types.RetornoInformacionRegimen>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "obtenerInformacionRegimen", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.ObtenerInformacionRegimen")
    @ResponseWrapper(localName = "obtenerInformacionRegimenResponse", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.ObtenerInformacionRegimenResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/obtenerInformacionRegimenRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/obtenerInformacionRegimenResponse")
    public List<RetornoInformacionRegimen> obtenerInformacionRegimen(@WebParam(name = "rut", targetNamespace = "")
                                                                     Long rut);

    /**
     *
     * @param usuario
     * @param idAporte
     * @return
     *     returns cl.bicevida.botonpago.ws.cuentainversion.types.EstructuraFondo
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultaEstructuraFondosPorId", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.ConsultaEstructuraFondosPorId")
    @ResponseWrapper(localName = "consultaEstructuraFondosPorIdResponse", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.ConsultaEstructuraFondosPorIdResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/consultaEstructuraFondosPorIdRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/consultaEstructuraFondosPorIdResponse")
    public EstructuraFondo consultaEstructuraFondosPorId(@WebParam(name = "idAporte", targetNamespace = "")
                                                         String idAporte,
                                                         @WebParam(name = "usuario", targetNamespace = "")
                                                         String usuario);

    /**
     *
     * @param rutCliente
     * @param numeroPoliza
     * @param ramo
     * @return
     *     returns cl.bicevida.botonpago.ws.cuentainversion.types.RetornoConsultaEstado
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultarEstado", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.ConsultarEstado")
    @ResponseWrapper(localName = "consultarEstadoResponse", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.ConsultarEstadoResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/consultarEstadoRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/consultarEstadoResponse")
    public RetornoConsultaEstado consultarEstado(@WebParam(name = "numeroPoliza", targetNamespace = "")
                                                 Long numeroPoliza,
                                                 @WebParam(name = "rutCliente", targetNamespace = "") Long rutCliente,
                                                 @WebParam(name = "ramo", targetNamespace = "") Long ramo);

    /**
     *
     * @param rutCliente
     * @param numeroPoliza
     * @param ramo
     * @return
     *     returns cl.bicevida.botonpago.ws.cuentainversion.types.SaldosYFondos
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultaSaldosYFondos", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.ConsultaSaldosYFondos")
    @ResponseWrapper(localName = "consultaSaldosYFondosResponse", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.ConsultaSaldosYFondosResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/consultaSaldosYFondosRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/consultaSaldosYFondosResponse")
    public SaldosYFondos consultaSaldosYFondos(@WebParam(name = "ramo", targetNamespace = "") String ramo,
                                               @WebParam(name = "numeroPoliza", targetNamespace = "")
                                               String numeroPoliza,
                                               @WebParam(name = "rutCliente", targetNamespace = "") String rutCliente);

    /**
     *
     * @param rutCliente
     * @param usuario
     * @param numeroPoliza
     * @param regimen
     * @param fondos
     * @param ramo
     * @param montoTotal
     * @return
     *     returns cl.bicevida.botonpago.ws.cuentainversion.types.RetGrabadoDistFondo
     * @throws DatoNuloException
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "grabarDistribucionFondosAporteExtraordinarioReg",
                    targetNamespace = "http://ejb.bicevida.cl/",
                    className =
                    "cl.bicevida.botonpago.ws.cuentainversion.types.GrabarDistribucionFondosAporteExtraordinarioReg")
    @ResponseWrapper(localName = "grabarDistribucionFondosAporteExtraordinarioRegResponse",
                     targetNamespace = "http://ejb.bicevida.cl/",
                     className =
                     "cl.bicevida.botonpago.ws.cuentainversion.types.GrabarDistribucionFondosAporteExtraordinarioRegResponse")
    @Action(input =
            "http://ejb.bicevida.cl/CuentaDeInversionWebService/grabarDistribucionFondosAporteExtraordinarioRegRequest",
            output =
            "http://ejb.bicevida.cl/CuentaDeInversionWebService/grabarDistribucionFondosAporteExtraordinarioRegResponse",
            fault = {
            @FaultAction(className = DatoNuloException.class,
                         value =
                         "http://ejb.bicevida.cl/CuentaDeInversionWebService/grabarDistribucionFondosAporteExtraordinarioReg/Fault/DatoNuloException") })
    public RetGrabadoDistFondo grabarDistribucionFondosAporteExtraordinarioReg(@WebParam(name = "ramo",
                                                                                         targetNamespace = "")
                                                                               String ramo,
                                                                               @WebParam(name = "numeroPoliza",
                                                                                         targetNamespace = "")
                                                                               String numeroPoliza,
                                                                               @WebParam(name = "rutCliente",
                                                                                         targetNamespace = "")
                                                                               String rutCliente,
                                                                               @WebParam(name = "usuario",
                                                                                         targetNamespace = "")
                                                                               String usuario,
                                                                               @WebParam(name = "montoTotal",
                                                                                         targetNamespace = "")
                                                                               double montoTotal,
                                                                               @WebParam(name = "fondos",
                                                                                         targetNamespace = "")
                                                                               List<FondoPorcentaje> fondos,
                                                                               @WebParam(name = "regimen",
                                                                                         targetNamespace = "")
                                                                               String regimen) throws DatoNuloException;

    /**
     *
     * @param rutCliente
     * @param usuario
     * @param numeroPoliza
     * @param regimen
     * @param ramo
     * @param monto
     * @return
     *     returns cl.bicevida.botonpago.ws.cuentainversion.types.IdAporte
     * @throws DatoNuloException
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "generaIdAporteReg", targetNamespace = "http://ejb.bicevida.cl/",
                    className = "cl.bicevida.botonpago.ws.cuentainversion.types.GeneraIdAporteReg")
    @ResponseWrapper(localName = "generaIdAporteRegResponse", targetNamespace = "http://ejb.bicevida.cl/",
                     className = "cl.bicevida.botonpago.ws.cuentainversion.types.GeneraIdAporteRegResponse")
    @Action(input = "http://ejb.bicevida.cl/CuentaDeInversionWebService/generaIdAporteRegRequest",
            output = "http://ejb.bicevida.cl/CuentaDeInversionWebService/generaIdAporteRegResponse", fault = {
            @FaultAction(className = DatoNuloException.class,
                         value =
                         "http://ejb.bicevida.cl/CuentaDeInversionWebService/generaIdAporteReg/Fault/DatoNuloException")
        })
    public IdAporte generaIdAporteReg(@WebParam(name = "ramo", targetNamespace = "") String ramo,
                                      @WebParam(name = "numeroPoliza", targetNamespace = "") String numeroPoliza,
                                      @WebParam(name = "rutCliente", targetNamespace = "") String rutCliente,
                                      @WebParam(name = "monto", targetNamespace = "") double monto,
                                      @WebParam(name = "usuario", targetNamespace = "") String usuario,
                                      @WebParam(name = "regimen", targetNamespace = "")
                                      String regimen) throws DatoNuloException;

}
