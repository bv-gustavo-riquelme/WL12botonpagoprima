package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.AperturaCaja;
import cl.bice.vida.botonpago.common.dto.general.BpiDnaDetnavegTbl;
import cl.bice.vida.botonpago.common.dto.general.CartolaPolizas;
import cl.bice.vida.botonpago.common.dto.general.Comprobantes;
import cl.bice.vida.botonpago.common.dto.general.DetalleAPV;
import cl.bice.vida.botonpago.common.dto.general.DetalleNavegacion;
import cl.bice.vida.botonpago.common.dto.general.DetallePagoPoliza;
import cl.bice.vida.botonpago.common.dto.general.DetalleTransaccionByEmp;
import cl.bice.vida.botonpago.common.dto.general.Navegacion;
import cl.bice.vida.botonpago.common.dto.general.PrcCuadraturaDto;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionMedioByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.dto.general.TransaccionByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.TransaccionMedioByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.Transacciones;
import cl.bice.vida.botonpago.common.dto.parametros.CodEmpByMedio;
import cl.bice.vida.botonpago.common.dto.parametros.MedioPago;
import cl.bice.vida.botonpago.common.dto.parametros.Paginas;
import cl.bice.vida.botonpago.common.dto.parametros.ValorUf;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosHistoricoVw;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosOfflineVw;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosVw;
import cl.bice.vida.botonpago.modelo.dto.PersonaCotizadorDto;

import java.util.Date;
import java.util.List;


public interface PersistenciaGeneralDAO {
    public int getSecuenceValue(String secuencename);
    public AperturaCaja findCajaAbierta();
    public List<BhDividendosVw> findDividendosByRut(String rut);
    public List<BhDividendosOfflineVw> findDividendosOfflineByRut(String rut);
    public List findPolizasByRut(Integer rut);
    public List findAPVAPTByRut(Integer rut);
    public List findPolizasByRutPatPac(Integer rut);
    public List findPolizasOfflineByRut(Integer rut);
    public List<MedioPago> findAllMediosVisibles();
    public ValorUf findValorUfByDate(Date fecha);
    public List<BhDividendosHistoricoVw> findPagosDividendosMesByRut(Integer rut);
    public CodEmpByMedio findCodByEmpInMedio(Integer empresa, Integer medio);
    public Paginas findPaginaById(Integer id);
    public Navegacion findNavegacionById(Long id);
    public Transacciones findTransaccionById(Integer id);
    public Navegacion newNavegacion(int rut, int empresa);
    public void updateTransaccionInNavegacion(Long nav,Long transaccion, Integer monto,Integer medio);    
    public DetalleNavegacion createDetalleNavegacion(Long nav,int entrada,int cod_pagina, Integer monto,String xml, int idCanal);    
    public DetalleNavegacion newDetalleNavegacion(Long nav,int entrada,int cod_pagina, Integer monto, int idCanal);
    public void updateDetNavegacionXML(DetalleNavegacion detalle, String xml);
    public Transacciones newTransaccion(String origenTransaccion, String empresa, int cod_medio, Date turno, int monto, Date fechaini, int rut, String nombre);
    public Transacciones updateTransaccion(Integer idtransaccion,Integer cod_medio, Integer monto, Date turno);
    public void updateDetalleTransaccion(Transacciones transaccion,List detallesByEmp, List comprobantes);
    public List<CartolaPolizas> findPagosPolizasMesByRut(int rut);    
    public List<DetallePagoPoliza> findDetallePagoPolizaByFolioRecibo(Long folio);
    public Boolean insertComprobante(Comprobantes dto);
    public Boolean insertDetalleTransaccionEmp(DetalleTransaccionByEmp dto);
    public BpiDnaDetnavegTbl findComprobanteByTransaccion(Long id);
    public BpiDnaDetnavegTbl findComprobanteByTransaccion(Long id, Integer pagina);
    public String aperturaCaja();
    public String cierreCaja();
    public String cuadratura(PrcCuadraturaDto dto);
    public Boolean actualizaTransaccionSP(SPActualizarTransaccionDto dto);
    public Boolean updateEstadoTransaccion(Integer idtransaccion, Integer cod_estado);
    public Boolean updateCodBancoTransaccion(Integer idtransaccion, Integer cod_banco);
    public List<TransaccionByEmpresa> findTransaccionesByEmpresa(Date fechaini, Date fechafin);
    public List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresa(Integer id, Date fechaini, Date fechafin);
    public List<RecaudacionByEmpresa> findLastRecaudacionByEmpresa(Date fecha);
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresa(Integer id, Date fecha);
    public Date findUltimoDiaCuadratura();
    public Boolean newCartola(Long cartola_id,Integer rut, Date fechaini, Date fechafin, int empresa, String xml);   
    public Boolean actualizaTransaccionSPByFechaPago(java.util.Date fechaPago, SPActualizarTransaccionDto dto);
    public List<MedioPago> findAllMediosdePagoElectronico(Integer empresa);
    public List<MedioPago> findAllMedios();
    public Navegacion findNavegacionByIdTrx(Long idtrx);
    public boolean updateTipoTransaccion(Integer idtransaccion, int tipoTransaccion);
    public DetalleAPV getDetalleProductoAPV(String poliza);
    public boolean updateEstadoPolizaAPV(Long rut_trabajador, Long numero_poliza, Long ramo, Long numero_recibo, Long secuencia);
    public int getValorMaximoAporteExtraAPV();
    public double getPorcentajeCobroTarjetaAPVAPT();
    public DetalleAPV generarIdAporteExtraordinarioAPV(DetalleAPV detalle);
    public boolean updateIdAporteComprobantes(DetalleAPV detalle);    
    public boolean updateTipoTransaccionAPT(Integer idtransaccion, int tipoTransaccion, int cargo);
    public List findAPVByRut(Integer rut);
    public Long getFolioCajaByTurno(java.util.Date turno);
    public Boolean updateEmpresaTransaccion(Integer idtransaccion, String empresa);
    public Long getIdTurnoOpenForInternet();
    public java.util.Date getFechaYHoraServidorOracle();
    
    //PRIMERAS PRIMAS
    public List findPrimeraPrimaByNumppRut(Integer numeroPropuesta, Integer rut);
    public PersonaCotizadorDto findClienteCotizador(Integer rut);
    
    /**
     * Inserta homologacion
     * @return
     */
    public Boolean insertarHomologacionPKConvenio(Long codigoMedioPago, Long idTransaccion, String idConvenioTransaccion, java.util.Date fecha);
    
    public Transacciones updateTransaccion(Integer idtransaccion, String usoTrxUsadaporMDB) ;
    
    
    public String getOrdenCompraServicioRESTFulByTransaccion(Long idTransaccion);
    public boolean updateTransaccionRESTful(Long idtransaccion, Integer codEstado);
    public boolean isTransaccionRESTful(Long idTransaccion);
    public Boolean pagarSPCajasPEC(SPActualizarTransaccionDto dto);
}
