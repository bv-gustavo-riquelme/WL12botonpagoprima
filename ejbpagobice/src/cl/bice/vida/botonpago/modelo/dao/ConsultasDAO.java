package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.BpiCarCartolasTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiCppComprobpagopolizaTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDnaDetnavegTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDpeDetservpecTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.CartolaPolizas;
import cl.bice.vida.botonpago.common.dto.general.Cartolas;
import cl.bice.vida.botonpago.common.dto.general.DetallePagoPoliza;
import cl.bice.vida.botonpago.common.dto.general.Navegacion;
import cl.bice.vida.botonpago.common.dto.general.PrcProcesoGeneraFileDto;
import cl.bice.vida.botonpago.common.dto.general.PrcProcesoReadFileDto;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionByEmpMedioInPeriod;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionMedioByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.TransaccionByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.TransaccionMedioByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.TransaccionMedioByEmpresaByMedio2;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosHistoricoVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiDttDettraturnoVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiRtcRestrancuadrVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiVreRechazosVw;
import cl.bice.vida.botonpago.modelo.dto.HomologacionConvenioDTO;

import cl.bice.vida.botonpago.modelo.dto.PdfComprobantePagoDTO;
import cl.bice.vida.botonpago.modelo.dto.PersonaDto;

import cl.bice.vida.botonpago.modelo.dto.VWIndPrimaNormalVO;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Date;
import java.util.List;

import oracle.xml.parser.v2.XMLDocument;


public interface ConsultasDAO {
    public Long getCartolaId();     
    public Boolean guardarCartola(Long cartola_id,Integer rut, Date fechaini, Date fechafin, String xml, int empresa);
    public Cartolas newCartola(Long cartola_id,Integer rut, Date fechaini, Date fechafin, int empresa);
    public Boolean updateCartola(Cartolas cartola, String xml);
    public List<BhDividendosHistoricoVw> findCartolaDividendosByRutInPeriod(String rut, Date fechaini, Date fechafin) ;
    public List<CartolaPolizas> findCartolaPolizasByRutInPeriod(int rut, Date fechaini, Date fechafin) ;
    public List<DetallePagoPoliza> findDetallePagoPolizaByFolioRecibo(Long folio);
    public XMLDocument findComprobante(int tipo, Long idcomprobante, Long num_ope, Integer num_div);
    public Long findTransaccionByProdDiv(Long prod, Integer div);
    public BpiDpeDetservpecTbl findComprobantePECByTransaccion(Long id);
    public BpiTraTransaccionesTbl findTransaccionDividendoByNumopeNumdiv(Long num_ope, Integer num_div);
    public XMLDocument findComprobanteCajaDividendo(Integer num_ope, Integer num_div);
    public BpiCppComprobpagopolizaTbl findComprobPagoPolizaById(Long idcomprobante);    
    public BpiTraTransaccionesTbl findTransaccionCodigoEstadoByNumTransaccion(Long numtrx);
    public String GeneraArchivoCuadratura(PrcProcesoGeneraFileDto generafiledto);
    public String LecturaArchivoCuadratura(PrcProcesoReadFileDto lecturafiledto);
    public List<RecaudacionByEmpresa> findLastRecaudacionByEmpresabyRango(Date fechamenor, Date fechamayor);
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyRango(Integer cod_empresa, Integer cod_medio, Date fechamenor, Date fechamayor);
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyRangoByMedio2(Integer cod_empresa, Integer cod_medio, Date fechamenor, Date fechamayor, Integer cod_medio2);
    public List<BpiRtcRestrancuadrVw> findLastRecaudacionByEmpresainPeriodo(Date fechamenor, Date fechamayor, Long empresa);
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainPeriodo(Integer id, Date fechamenor, Date fechamayor);
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainPeriodoByMedio(Integer empresa, Date fechamenor, Date fechamayor, Integer cod_medio);
    public List<TransaccionByEmpresa> findTransaccionesByEmpresabyTransaccion(String trx);
    public List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresaByTrx(Integer id, String trx);
    public List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresaByRut(Integer id, String rut);
    public List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByFecha(Integer cod_empresa, Integer cod_medio, Date fechaini, Date fechfin);
    public List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByTrx(Integer cod_empresa, Integer cod_medio, Integer trx);
    public List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByRut(Integer cod_empresa, Integer cod_medio, Integer rut);
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresaRut(Integer id, Integer cuadratura);
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainIdCudraturaByMedio(Integer empresa, Integer cuadratura, Integer cod_medio);
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyCuad(Integer cod_empresa, Integer cod_medio, Long cuadratura);
    public List<TransaccionByEmpresa> findTransaccionesByEmpresabyRut(String rut);
    public List<BpiRtcRestrancuadrVw> findLastRecaudacionByEmpresaIdCuad(Long cuadratura, Long empresa);
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMedio(Integer cod_empresa, Integer cod_medio, Date fecha);
    public List<BpiVreRechazosVw> findRechazosInPeriod(Date fechaini, Date fechafin);
    public List<BpiDnaDetnavegTbl> findComprobanteByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa);
    public List<BpiDnaDetnavegTbl> findLastNavegacionByRut(Integer rut, Integer empresa);
    public List<BpiDnaDetnavegTbl> findDetalleNavegacion(Integer idnavegacion);
    public List<BpiCarCartolasTbl> findLastCartolaByRut(Integer rut, Integer empresa);
    public List<Navegacion> getCountFindNavegacionByRutInPeriod(Integer rut, Date fechadesde, Date fechahasta, Integer empresa);
    public List<BpiCarCartolasTbl> getCountFindNavCartolasByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa);
    public List<Navegacion> findNavegacionByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa);
    public List<BpiCarCartolasTbl> findNavCartolasByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer rowdesde, Integer rowhasta, Integer empresa);
    public List<BpiVreRechazosVw> findRechazosByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa);
    public boolean isExisteTransaccionComprobante(Long idTransaccion, Double rutPersona);
    public BpiTraTransaccionesTbl findTransaccionCodigoEstadoByNumTransaccionForCuadratura(Long numtrx);
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyCuadByMedio2(Integer cod_empresa, Integer cod_medio, Long cuadratura, Integer cod_medio2);
    public List<TransaccionMedioByEmpresaByMedio2> findTransaccionesRecauByMedio2ByFecha(Timestamp fechaini, Timestamp fechafin, Integer empresa);
    public List<TransaccionMedioByEmpresaByMedio2> findTransaccionesRecauByMedio2ByCuadratura(Integer idcuadratura, Integer empresa);
    public String getNombreProductoSinAcentos(Long codigoProducto);
    public List<BpiTraTransaccionesTbl> findTransaccionesForRegularizacionWebPay(Long idEmpresa, Integer rutCliente, java.util.Date fechaDesde, java.util.Date fechaHasta);
    public List<BpiTraTransaccionesTbl> findTransaccionesForRegularizacionWebPayPool(Long idEmpresa, String texto);
    public List<BpiTraTransaccionesTbl> findTransaccionesYaRegularizadasWebPayPool(Long idEmpresa, Integer rutCliente);
    public PersonaDto findPersonaByRut(Integer rut);
    public PdfComprobantePagoDTO findPdfComprobantebyRut(Integer rut) throws SQLException, Exception; 
    public List<VWIndPrimaNormalVO> findPolizasByRut(Integer rut) throws SQLException, Exception;
    
    /**
     * Recupera el ID de Comercion
     * @param empresa
     * @param mediopago
     * @return
     */
    public Long getIDCOMByEmpresaMedioPago(Long empresa, Long mediopago);
    
    /**
     * Recupera el ID de Comercio
     * @param idTransaccion id transaccion bice vida
     * @return
     */
    public HomologacionConvenioDTO getHomologacionConvenioByIdTrx(Long idTransaccion);
    /**
     * Recupera el ID de Comercio
     * @param idTransaccion id transaccion bice vida
     * @return
     */
    public HomologacionConvenioDTO getHomologacionConvenioByIdComercioTrx(String idComercioTrx);
    
    /**
     *
     * @param idTransaccion
     * @return
     */
    String getNroBoletaByIdTrx(Long idTransaccion);
}
