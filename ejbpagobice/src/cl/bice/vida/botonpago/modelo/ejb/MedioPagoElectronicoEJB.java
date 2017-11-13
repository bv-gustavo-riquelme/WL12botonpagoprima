package cl.bice.vida.botonpago.modelo.ejb;

import cl.bice.vida.botonpago.common.dto.general.BpiCarCartolasTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDitribFondosAPVTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDlcDetalleLogCuadrTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDnaDetnavegTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiLgclogCuadraturaTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.ConsultaDetalleServicioPec;
import cl.bice.vida.botonpago.common.dto.general.DetalleAPV;
import cl.bice.vida.botonpago.common.dto.general.Navegacion;
import cl.bice.vida.botonpago.common.dto.general.PrcCuadraturaDto;
import cl.bice.vida.botonpago.common.dto.general.PrcProcesoGeneraFileDto;
import cl.bice.vida.botonpago.common.dto.general.PrcProcesoReadFileDto;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionByEmpMedioInPeriod;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionMedioByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.ReporteAgentePolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteGerentePolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteJefeSucursalPolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteJefeZonalPolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteSupervisorPolizas;
import cl.bice.vida.botonpago.common.dto.general.ResumenRequest;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.dto.general.TransaccionByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.TransaccionMedioByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.TransaccionMedioByEmpresaByMedio2;
import cl.bice.vida.botonpago.common.dto.vistas.BpiDttDettraturnoVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiRtcRestrancuadrVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiVreRechazosVw;
import cl.bice.vida.botonpago.modelo.dto.BpiPecConsultaTransaccionDto;
import cl.bice.vida.botonpago.modelo.dto.HomologacionConvenioDTO;
import cl.bice.vida.botonpago.modelo.dto.PersonaCotizadorDto;
import cl.bice.vida.botonpago.modelo.dto.RegimenTributarioDTO;

import java.sql.Timestamp;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import oracle.xml.parser.v2.XMLDocument;


@Remote
public interface MedioPagoElectronicoEJB {
    /*
     * Constantes Publicas
     */
    public static final int CANAL_BOTON_PAGO_PERFILADO = 1;
    public static final int CANAL_BOTON_PAGO_PUBLICO = 2;
    public static final int CANAL_BOTON_PAGO_APV_EXTRAORDINARIO = 3;
    public static final int CANAL_BOTON_PAGO_PUBLICO_PRIMERA_PRIMA = 4;


    String aperturaCaja();

    String cierreCaja();

    String cuadratura(PrcCuadraturaDto cuadraturadto);

    String crearResumenPagosPolizas(Integer rut, String nombre, String confirmacion, int idCanal);

    String crearResumenPagosDividendos(Integer rut, String nombre, String confirmacion, int idCanal);

    String crearRespuestaPagosPendientes(String xmlconsulta);

    String generaXmlResumenPagosConProductosSeleccionados(String xml, ResumenRequest req, int idCanal);

    String generaXmlResumenPagosConProductosSeleccionados(String xml, ResumenRequest req, int idCanal, String email);

    String crearConfirmacionConXmlSeleccionado(String xmlResumenSeleccionado, String email, int idCanal);

    String crearConfirmacionConXmlSeleccionado(String xmlResumenSeleccionado, int idCanal, int tipoTransaccion,
                                               String email);

    BpiDnaDetnavegTbl findComprobanteByTransaccion(Long id);

    XMLDocument findComprobante(int tipo, Long idcomprobante, Long num_ope, Integer num_div);

    Long findTransaccionByProdDiv(Long prod, Integer div);

    BpiTraTransaccionesTbl findTransaccionCodigoEstadoByNumTransaccion(Long numtrx);

    Boolean actualizaTransaccionSP(SPActualizarTransaccionDto dto);

    Boolean updateEstadoTransaccion(Integer idtransaccion, Integer cod_estado);

    Boolean updateCodBancoTransaccion(Integer idtransaccion, Integer cod_banco);

    List<TransaccionByEmpresa> findTransaccionesByEmpresa(Date fechaini, Date fechafin);

    List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresa(Integer id, Date fechaini, Date fechafin);

    List<RecaudacionByEmpresa> findLastRecaudacionByEmpresa(Date fecha);

    List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresa(Integer id, Date fecha);

    Date findUltimoDiaCuadratura();

    List<ReporteGerentePolizas> generateReporteGerentePolizas();

    List<ReporteJefeSucursalPolizas> generateReporteJefeSucursalPolizas(Integer num_jefe_sucursal);

    List<ReporteSupervisorPolizas> generateReporteSupervisorPolizas(Integer numero_supervisor);

    List<ReporteAgentePolizas> generateReporteAgentePolizas(Integer numero_agente, Integer rowdesde, Integer rowhasta);

    List<ReporteJefeZonalPolizas> generateReporteJefeZonalPolizas(Integer num_jefe_zona);

    List<ReporteAgentePolizas> generateReporteAgenteByRutPolizas(Integer rut_agente, Integer rowdesde,
                                                                 Integer rowhasta);

    List<ReporteSupervisorPolizas> generateReporteSupervisorByRutPolizas(Integer rut_supervisor);

    List<ReporteJefeSucursalPolizas> generateReporteJefeSucursalByRutPolizas(Integer rut_jefe_sucursal);

    List<ReporteJefeZonalPolizas> generateReporteJefeZonalByRutPolizas(Integer rut_jefe_zona);

    String crearCartolaPoliza(int rut, String nombre, String email, int rut_usuario, String nombre_usuario,
                              Date fechaini, Date fechafin, int tipo);

    String crearCartolaDividendo(int rut, String nombre, String email, int rut_usuario, String nombre_usuario,
                                 Date fechaini, Date fechafin, int tipo);

    ReporteAgentePolizas getCountFindReporteAgentePolizas(Integer numero_agente);

    ReporteAgentePolizas getCountFindReporteAgenteByRutPolizas(Integer rut_agente);

    String GeneraArchivoCuadratura(PrcProcesoGeneraFileDto generafiledto);

    String LecturaArchivoCuadratura(PrcProcesoReadFileDto lecturafiledto);

    List<RecaudacionByEmpresa> findLastRecaudacionByEmpresabyRango(Date fechamenor, Date fechamayor);

    List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyRango(Integer cod_empresa, Integer cod_medio,
                                                                                 Date fechamenor, Date fechamayor);

    List<BpiRtcRestrancuadrVw> findLastRecaudacionByEmpresainPeriodo(Date fechamenor, Date fechamayor, Long empresa);

    List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainPeriodo(Integer id, Date fechamenor,
                                                                               Date fechamayor);

    List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainPeriodoByMedio(Integer empresa, Date fechamenor,
                                                                                      Date fechamayor,
                                                                                      Integer cod_medio);

    List<TransaccionByEmpresa> findTransaccionesByEmpresabyTransaccion(String trx);

    List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresaByTrx(Integer id, String trx);

    List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresaByRut(Integer id, String rut);

    List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByFecha(Integer cod_empresa, Integer cod_medio,
                                                                     Date fechaini, Date fechfin);

    List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByTrx(Integer cod_empresa, Integer cod_medio, Integer trx);

    List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByRut(Integer cod_empresa, Integer cod_medio, Integer rut);

    List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresaRut(Integer id, Integer cuadratura);

    List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyCuad(Integer cod_empresa, Integer cod_medio,
                                                                                Long cuadratura);

    List<TransaccionByEmpresa> findTransaccionesByEmpresabyRut(String rut);

    List<BpiRtcRestrancuadrVw> findLastRecaudacionByEmpresaIdCuad(Long cuadratura, Long empresa);

    List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMedio(Integer cod_empresa, Integer cod_medio,
                                                                          Date fecha);

    List<BpiVreRechazosVw> findRechazosInPeriod(Date fechaini, Date fechafin);

    List<BpiDnaDetnavegTbl> findComprobanteByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa);

    List<BpiDnaDetnavegTbl> findLastNavegacionByRut(Integer rut, Integer empresa);

    List<BpiDnaDetnavegTbl> findDetalleNavegacion(Integer idnavegacion);

    List<BpiCarCartolasTbl> findLastCartolaByRut(Integer rut, Integer empresa);

    List<Navegacion> getCountFindNavegacionByRutInPeriod(Integer rut, Date fechadesde, Date fechahasta,
                                                         Integer empresa);

    List<BpiCarCartolasTbl> getCountFindNavCartolasByRutInPeriod(Integer rut, Date fechaini, Date fechafin,
                                                                 Integer empresa);

    List<Navegacion> findNavegacionByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa);

    List<BpiCarCartolasTbl> findNavCartolasByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer rowdesde,
                                                         Integer rowhasta, Integer empresa);

    List<BpiVreRechazosVw> findRechazosByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa);

    BpiDnaDetnavegTbl findComprobanteByTransaccion(Long id, Integer codigoPagina);

    Long insertNewCuadratura(Integer idEmpresa, Integer idMedioPago, Date fechaCuadratura);

    Boolean updateCuadratura(Long idCuadratura, Long montoTotal, Long montoInformado, Long numeroTransacciones);

    Boolean insertCuadraturaTransaccion(Long idCuadratura, Long idTransaccion);

    String procesarInformePagoPendientes(String xml);

    ConsultaDetalleServicioPec findDetalleServicioPECByIdTransaccionPagina(Long idtransaccion, Long cod_pagina);

    Boolean actualizaTransaccionSPByFechaPago(java.util.Date fechaPago, SPActualizarTransaccionDto dto);

    Long insertLogCuadratura(BpiLgclogCuadraturaTbl dto);

    Boolean insertLogDetalleCuadratura(BpiDlcDetalleLogCuadrTbl dto);

    BpiTraTransaccionesTbl findTransaccionCodigoEstadoByNumTransaccionForCuadratura(Long numtrx);

    List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainIdCudraturaByMedio(Integer empresa,
                                                                                          Integer cuadratura,
                                                                                          Integer cod_medio);

    List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyRangoByMedio2(Integer cod_empresa,
                                                                                         Integer cod_medio,
                                                                                         Date fechamenor,
                                                                                         Date fechamayor,
                                                                                         Integer cod_medio2);

    List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyCuadByMedio2(Integer cod_empresa,
                                                                                        Integer cod_medio,
                                                                                        Long cuadratura,
                                                                                        Integer cod_medi2);

    List<TransaccionMedioByEmpresaByMedio2> findTransaccionesRecauByMedio2ByFecha(Timestamp fechaini,
                                                                                  Timestamp fechafin, Integer empresa);

    List<TransaccionMedioByEmpresaByMedio2> findTransaccionesRecauByMedio2ByCuadratura(Integer idcuadratura,
                                                                                       Integer empresa);

    DetalleAPV getDetalleProductoAPV(String poliza);

    boolean updateEstadoPolizaAPV(Long rut_trabajador, Long numero_poliza, Long ramo, Long numero_recibo,
                                  Long secuencia);

    String crearResumenPagosAPVAPT(Integer rut, String nombre, String confirmacion, int idCanal);

    int getValorMaximoAporteExtraAPV();

    double getPorcentajeCobroTarjetaAPVAPT();

    String generaXmlResumenPagosConProductosSeleccionadosAPVAPT(String xml, ResumenRequest req, int idCanal,
                                                                int cargoTarjetaCredito);

    DetalleAPV generarIdAporteExtraordinarioAPV(DetalleAPV detalle);

    boolean updateIdAporteComprobantes(DetalleAPV detalle);

    String crearConfirmacionConXmlSeleccionadoAPT(String xmlResumenSeleccionado, int idCanal, int tipoTransaccion,
                                                  int cargoExtra, String email);

    boolean grabarDistribucionFondosAPVProducto(int idAporte, List<BpiDitribFondosAPVTbl> DistribfFondos,
                                                Long CodigoRegimen, String DescrpcionRegimen);

    List<BpiDitribFondosAPVTbl> getDistribucionFondosAPVByTransaccion(int idTransaccion, int poliza, int ramo,
                                                                      long rut);

    boolean updateTransaccionDistribucionFondosAPVProducto(DetalleAPV detalleapv);

    public Boolean updateEmpresaTransaccion(Integer idtransaccion, String empresa);


    //PRIMERAS PRIMAS
    String crearResumenPagosPrimerasPrimas(Integer rut, String nombre, String confirmacion, int idCanal,
                                           Integer numeroPropuesta);

    public String generaXmlResumenPagosConProductosSeleccionadosPrimerasPrimas(String xml, ResumenRequest req,
                                                                               int idCanal);

    public String crearConfirmacionConXmlSeleccionadoPrimeraPrima(String xmlResumenSeleccionado, int idCanal,
                                                                  String email);

    public boolean tienePropuestaRutPrimeraPrima(Integer rut, Integer numeroPropuesta);

    public PersonaCotizadorDto buscaClienteCotizador(Integer rut);

    public List<RegimenTributarioDTO> obtenerListaRegimenTributarioPolizas(Long rutCliente);

    public Boolean actualizaTransaccionSPAsync(SPActualizarTransaccionDto dto);

    /**
     * Regulariza las transacciones pendientes de WebPay
     *
     * @param idEmpresa
     * @param rutCliente
     * @param fechaDesde
     * @param fechaHasta
     * @return
     */
    public List<BpiTraTransaccionesTbl> findTransaccionForRegularizacionWebPay(Long idEmpresa, Integer rutCliente,
                                                                               java.util.Date fechaDesde,
                                                                               java.util.Date fechaHasta);


    public BpiTraTransaccionesTbl actualizaTransaccionSPReturnDTO(SPActualizarTransaccionDto dto);

    public List<BpiTraTransaccionesTbl> findTransaccionForRegularizacionWebPayPool(Long idEmpresa);

    public List<BpiTraTransaccionesTbl> findTransaccionForYaRegularizadasWebPayPool(Long idEmpresa, Integer rutCliente);

    /**
     * Recupera el ID COmercio
     *
     * @param codEmpresa
     * @param codMedioPago
     * @return
     */
    public Long getIdComercioBICEByMedioEmpresa(Long codEmpresa, Long codMedioPago);

    /**
     * Recupera el ID de Comercio
     * @param idTransaccion id transaccion bice vida
     * @return
     */
    public HomologacionConvenioDTO getHomologacionConvenioByIdTrx(Long idTransaccion);

    /**
     * Recupera el ID de Comercio
     * @param idComercioTrx id transaccion bice vida
     * @return
     */
    public HomologacionConvenioDTO getHomologacionConvenioByIdComercioTrx(String idComercioTrx);


    public boolean updateTransaccionRESTful(Long idtransaccion, Integer codEstado);

    public boolean isTransaccionRESTful(Long idTransaccion);

    public boolean notificarPagoTransaccionRESTful(Long idTransaccion);

    public BpiPecConsultaTransaccionDto findDetalleServicioPECV2ByIdTransaccion(Long idtransaccion);

    public Boolean actualizaTransaccionSPPecv2(SPActualizarTransaccionDto dto);

    /**
     * Metodo utilizado para Servipag
     * @param idTransaccion
     * @return
     */
    String getNroBoletaByIdTrx(Long idTransaccion);
}
