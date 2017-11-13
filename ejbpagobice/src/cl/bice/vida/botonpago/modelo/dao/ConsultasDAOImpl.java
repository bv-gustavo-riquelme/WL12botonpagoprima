package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.BpiCarCartolasTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiComComprobantesTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiCppComprobpagopolizaTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDnaDetnavegTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDpeDetservpecTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiMpgMediopagoTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiNavNavegacionTbl;
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
import cl.bice.vida.botonpago.common.dto.parametros.Empresas;
import cl.bice.vida.botonpago.common.dto.parametros.MedioPago;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosHistoricoVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiDttDettraturnoVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiRtcRestrancuadrVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiVreRechazosVw;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.common.util.StringUtil;
import cl.bice.vida.botonpago.common.util.XmlUtil;
import cl.bice.vida.botonpago.modelo.dto.HomologacionConvenioDTO;
import cl.bice.vida.botonpago.modelo.dto.PdfComprobantePagoDTO;
import cl.bice.vida.botonpago.modelo.dto.PersonaDto;
import cl.bice.vida.botonpago.modelo.dto.ResponsePdfComprobanteVODTO;
import cl.bice.vida.botonpago.modelo.dto.VWIndPrimaNormalVO;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;

import java.io.IOException;
import java.io.StringReader;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import oracle.jdbc.OracleTypes;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;

import org.apache.log4j.Logger;

import org.xml.sax.SAXException;




public class ConsultasDAOImpl extends DataSourceBice implements ConsultasDAO {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ConsultasDAOImpl.class);

    public Long getCartolaId() {
        return null;
    }

    public Boolean guardarCartola(Long cartola_id, Integer rut, Date fechaini, Date fechafin, String xml, int empresa) {
        return null;
    }

    public Cartolas newCartola(Long cartola_id, Integer rut, Date fechaini, Date fechafin, int empresa) {
        return null;
    }

    public Boolean updateCartola(Cartolas cartola, String xml) {
        return null;
    }

    /**
     * Retorna la lista de cartolas
     * por concepto de dividendos en un cierto
     * periodo de fecha y rut
     * @param rut
     * @param fechaini
     * @param fechafin
     * @return
     */
    public List<BhDividendosHistoricoVw> findCartolaDividendosByRutInPeriod(String rut, Date fechaini, Date fechafin) {
        logger.info("findDividendosOfflineByRut() - inicia");
        List dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT * FROM BPI_DIH_DIVIDENDOSHIST_VW " +
                                "WHERE RUT_CLI = ? AND FEC_PAGO BETWEEN ? AND ?" + "  AND COD_EST2 = ?");
            stm.setInt(1, Integer.parseInt("0" + rut));
            stm.setTimestamp(2, new java.sql.Timestamp(fechaini.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechafin.getTime()));
            stm.setInt(4, 2); //Estado igual a 2
            rst = stm.executeQuery();
            while (rst.next()) {
                BhDividendosHistoricoVw dto = new BhDividendosHistoricoVw();
                dto.setAmortizacion(rst.getDouble("AMO_MDA"));
                dto.setBancoPATPAC(rst.getString("BANCO_PATPAC"));
                dto.setCodEmpresa(rst.getInt("COD_EMPRESA"));
                dto.setCodEstado(rst.getInt("COD_EST2"));
                dto.setCodMecanismo(rst.getInt("COD_MECANISMO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setDif_boleta(rst.getInt("DIF_BOL"));
                dto.setDv(rst.getString("DV_CLI"));
                dto.setFechaPATPAC(FechaUtil.toUtilDate(rst.getDate("FECHA_PATPAC")));
                dto.setFecPago(FechaUtil.toUtilDate(rst.getDate("FEC_PAGO")));
                dto.setFecVencimiento(FechaUtil.toUtilDate(rst.getDate("FEC_VENCIMIENTO")));
                dto.setGastosCobranzas(rst.getDouble("GAS_CBZ"));
                dto.setIntereses(rst.getDouble("INT_MDA"));
                dto.setInteresPenal(rst.getDouble("INT_PEN"));
                dto.setNombreSucursalPago(rst.getString("DESC_SUCURSAL"));
                dto.setNumDiv(rst.getLong("NUM_DIV"));
                dto.setNumOpe(rst.getLong("NUM_OPE"));
                dto.setOtrosCargos(rst.getDouble("OTR_CAR"));
                dto.setProducto(rst.getString("NOMBRE"));
                dto.setRut(rst.getInt("RUT_CLI"));
                dto.setSeguroCesantia(rst.getDouble("SEG_CES"));
                dto.setSeguroDesgravamen(rst.getDouble("SEG_DES"));
                dto.setSeguroIncendio(rst.getDouble("SEG_INC"));
                dto.setSrut(rst.getString("RUT_CLI"));
                dto.setSucursalPago(rst.getInt("SUC_PAG"));
                dto.setTotalPagado(rst.getInt("TOT_PGO"));
                dto.setTotMda(rst.getDouble("TOT_MDA"));
                dto.setTotPes(rst.getLong("TOT_PES"));
                dto.setValorUf(rst.getDouble("MMDA_VAL_PES"));
                dto.setEstado(rst.getString("COD_ESTADO"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findDividendosOfflineByRut() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findDividendosOfflineByRut() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findDividendosOfflineByRut() - termina");
        return dtos;
    }

    /**
     * Consulta de cartolas en un determinado
     * periodo y en base a un rut en especifico
     * @param rut
     * @param fechaini
     * @param fechafin
     * @return
     * TODO: Testear
     */
    public List<CartolaPolizas> findCartolaPolizasByRutInPeriod(int rut, Date fechaini, Date fechafin) {
        logger.info("findCartolaPolizasByRutInPeriod() - inicia");
        List<CartolaPolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT ID_COMPROBPAGO, RUT_CLIENTE, FECHA_ESTADO, ESTADO, VALOR_UF, ID_CANAL_PAGO, " +
                                "CANAL_PAGO,FECHA_PAGO, TIPO_COMPROBANTE, ID_RAMO, POLIZA_POL, FOLIO_RECIBO, " +
                                "CODIGO_PRODUCTO, NOMBRE_PRODUCTO, PRIMA_BRUTA_UF, FECHA_VENCIMIENTO, MONTO_PAGADO_PESOS " +
                                "FROM BPI_DPP_DETALLEPOLPAGADAS_VW WHERE RUT_CLIENTE = ? AND FECHA_ESTADO BETWEEN ? AND ?");
            stm.setInt(1, rut);
            stm.setTimestamp(2, new java.sql.Timestamp(fechaini.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechafin.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                CartolaPolizas dto = new CartolaPolizas();
                dto.setCanalPago(rst.getString("CANAL_PAGO"));
                dto.setCodigoProducto(rst.getInt("CODIGO_PRODUCTO"));
                dto.setEstado(rst.getString("ESTADO"));
                dto.setFechaEstado(FechaUtil.toUtilDate(rst.getDate("FECHA_ESTADO")));
                dto.setFechaPago(FechaUtil.toUtilDate(rst.getDate("FECHA_PAGO")));
                dto.setFechaVencimiento(FechaUtil.toUtilDate(rst.getDate("FECHA_VENCIMIENTO")));
                dto.setFolioRecibo(rst.getLong("FOLIO_RECIBO"));
                dto.setIdCanalPago(rst.getInt("ID_CANAL_PAGO"));
                dto.setIdComprobpago(rst.getLong("ID_COMPROBPAGO"));
                dto.setIdRamo(rst.getLong("ID_RAMO"));
                dto.setMontoPagadoPesos(rst.getLong("MONTO_PAGADO_PESOS"));
                dto.setNombreProducto(rst.getString("NOMBRE_PRODUCTO"));
                dto.setPolizaPol(rst.getLong("POLIZA_POL"));
                dto.setPrimaBrutaUf(rst.getDouble("PRIMA_BRUTA_UF"));
                dto.setRutCliente(rst.getLong("RUT_CLIENTE"));
                dto.setTipoComprobante(rst.getInt("TIPO_COMPROBANTE"));
                dto.setValorUf(rst.getDouble("VALOR_UF"));
                dtos.add(dto);
            }
        } catch (Exception e) {
            logger.error("findCartolaPolizasByRutInPeriod() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findCartolaPolizasByRutInPeriod() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findCartolaPolizasByRutInPeriod() - termina");
        return dtos;
    }

    /**
     * Carga el deatlle de las polizas
     * @param folio
     * @return
     */
    public List<DetallePagoPoliza> findDetallePagoPolizaByFolioRecibo(Long folio) {
        logger.info("findDetallePagoPolizaByFolioRecibo() - inicia");
        List<DetallePagoPoliza> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_DPP_DETALLEPAGOPOLIZA_TBL WHERE FOLIO_RECIBO = ?");
            stm.setLong(1, folio);
            rst = stm.executeQuery();
            while (rst.next()) {
                DetallePagoPoliza dto = new DetallePagoPoliza();
                dto.setDescCargo(rst.getString("DESC_CARGO"));
                dto.setDetalle(rst.getInt("DETALLE"));
                dto.setFolioRecibo(folio);
                dto.setMonto(rst.getDouble("MONTO"));
                dtos.add(dto);
            }
        } catch (Exception e) {
            logger.error("findDetallePagoPolizaByFolioRecibo() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findDetallePagoPolizaByFolioRecibo() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findDetallePagoPolizaByFolioRecibo() - termina");
        return dtos;
    }

    /**
     * Busca el comprobante de pago asociado
     * a un pago de un producto.
     * @param tipo Tipo del comprobante a buscar,
     *              1 para comprobantes de polizas
     *              2 para comprobantes internet de dividendos
     *              3 para comprobantes por caja de dividendos
     * @param idcomprobante Solo para casos de polizas este valor debe ir seteado, corresponde al identificador del comprobante
     * @param num_ope Solo para casos de dividendos, corresponde al numero del producto
     * @param num_div Solo para casos de dividendos, corresponde al numero del dividendo
     * @return XMLDocument con xml encontrado o null en caso contrario
     * TODO: Testear
     */
    public XMLDocument findComprobante(int tipo, Long idcomprobante, Long num_ope, Integer num_div) {
        logger.info("findComprobante() - inicia");
        switch (tipo) {
        case 1: //comprobante internet de poliza
            if (num_ope != null && num_div != null) {
                logger.info("findComprobante() - busco transaccion que coincida con numpoliza y cuota (fechavencimiento)");
                Long idtransaccion = findTransaccionByProdDiv(num_ope, num_div);
                logger.info("findComprobante():- idtransaccion: " + idtransaccion);
                //primero si es por boton de pago
                if (idtransaccion != null) {
                    logger.info("findComprobante() - busco comrpobante internet que coincida con transaccion encontrada");
                    BpiDnaDetnavegTbl detnaveg =
                        DAOFactory.getPersistenciaGeneralDao().findComprobanteByTransaccion(idtransaccion);
                    if (detnaveg != null)
                        return detnaveg.getDetallePagina();
                    else {
                        //luego por pec
                        BpiDpeDetservpecTbl detpecnaveg = findComprobantePECByTransaccion(idtransaccion);
                        if (detpecnaveg != null)
                            return detpecnaveg.getDetallePagina();
                        else
                            logger.error("findComprobante() - comprobante no encontrado para transaccion " +
                                         idtransaccion);
                    }
                } else
                    logger.error("findComprobante() - transaccion no encontrada para poliza " + num_ope + "/" +
                                 num_div);
            }
            break;
        case 2: //comprobante internet de dividendos
            logger.info("findComprobante() - busco transaccion internet que coincida con num_ope y num_div");
            BpiTraTransaccionesTbl transaccion = findTransaccionDividendoByNumopeNumdiv(num_ope, num_div);
            Long idtransaccion = transaccion.getIdTransaccion();
            if (idtransaccion != null) {
                logger.info("findComprobante() - busco comrpobante internet que coincida con transaccion encontrada");
                BpiDnaDetnavegTbl detnaveg =
                    DAOFactory.getPersistenciaGeneralDao().findComprobanteByTransaccion(idtransaccion);
                if (detnaveg != null)
                    return detnaveg.getDetallePagina();
                else {
                    //luego por pec
                    BpiDpeDetservpecTbl detpecnaveg = findComprobantePECByTransaccion(idtransaccion);
                    if (detpecnaveg != null)
                        return detpecnaveg.getDetallePagina();
                    else
                        logger.error("findComprobante():- comprobante no encontrado para transaccion " + idtransaccion);
                }
            } else
                logger.error("findComprobante():- transaccion no encontrada para dividendo " + num_ope + "/" + num_div);
            break;
        case 3: //comprobante caja dividendo
            return findComprobanteCajaDividendo(num_ope.intValue(), num_div);
        case 4: //comprobante caja de poliza
            if (idcomprobante != null) {
                logger.info("findComprobante():- busco comprobante que coincida con idcomprobante de un pago de poliza");
                BpiCppComprobpagopolizaTbl comprobante = findComprobPagoPolizaById(idcomprobante);
                if (comprobante != null)
                    return comprobante.getComprobante();
            }
            break;
        default:
            logger.error("findComprobante():- tipo de comprobante no aceptado " + tipo);
            return null;
        }
        logger.info("findComprobante() - termina");
        return null;
    }

    /**
     * Consulta el numero de transaccion
     * sefun el Producto y el Dividendo
     * @param prod
     * @param div
     * @return
     * TODO: Testear
     */
    public Long findTransaccionByProdDiv(Long prod, Integer div) {
        logger.info("findTransaccionByProdDiv() - inicio");
        Long id = new Long(0);
        logger.info("findTransaccionByProdDiv() - llamando a metodo findIdTransaccionByProductoCuotaMaximo()");
        BpiComComprobantesTbl comp = findIdTransaccionByProductoCuotaMaximo(prod, div);
        if (comp != null) {
            id = comp.getIdTransaccion();
            BpiTraTransaccionesTbl trx = findTransaccionCodigoEstadoByNumTransaccion(id);
            if (trx != null) {
                boolean existe = isExisteTransaccionComprobante(id, trx.getRutPersona());
                while (existe == false) {
                    id = id - 1;
                    if (id.longValue() == 0)
                        break;
                    existe = isExisteTransaccionComprobante(id, trx.getRutPersona());
                }
                logger.info("findTransaccionByProdDiv() - IdTransaccion: " + id);
            } else {
                id = new Long(0);
            }
        }
        logger.info("findTransaccionByProdDiv() - termino");
        return id;
    }

    /**
     * Consulta un comprobante de pago
     * seun el numero PEC y transaccion
     * @param id
     * @return
     * TODO: Testear
     */
    public BpiDpeDetservpecTbl findComprobantePECByTransaccion(Long id) {
        logger.info("findComprobantePECByTransaccion() - inicia");
        BpiDpeDetservpecTbl dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT a.ID_NAVEGACION, a.COD_PAGINA, a.FECHA_HORA, a.MONTO_TRANSACCION, a.ENTRADA, " +
                                " a.DETALLE_PAGINA.getClobVal() " +
                                "FROM BPI_DPE_DETSERVPEC_TBL a, BPI_PEC_SERVICIOPEC_TBL b " +
                                "WHERE b.ID_NAVEGACION = a.ID_NAVEGACION " + "  AND b.ID_TRANSACCION = ? " +
                                "  AND a.COD_PAGINA = ?");
            stm.setLong(1, id);
            stm.setInt(2, 5);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new BpiDpeDetservpecTbl();
                dto.setIdNavegacion(rst.getLong("ID_NAVEGACION"));
                dto.setCodPagina(rst.getInt("COD_PAGINA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setMontoTransaccion(rst.getLong("MONTO_TRANSACCION"));
                dto.setEntrada(rst.getInt("ENTRADA"));
                java.sql.Clob clb = rst.getClob(6);
                String xml = StringUtil.clobToString(clb);
                dto.setDetallePagina((XMLDocument) XmlUtil.parse(xml));
            }
        } catch (Exception e) {
            logger.error("findComprobantePECByTransaccion() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findComprobantePECByTransaccion() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findComprobantePECByTransaccion() - termina");
        return dto;
    }

    /**
     * Busca Transacciones Segun Dividendos en numero de
     * operaciones y numero s de vididendos
     * @param num_ope
     * @param num_div
     * @return
     */
    public BpiTraTransaccionesTbl findTransaccionDividendoByNumopeNumdiv(Long num_ope, Integer num_div) {
        logger.info("findTransaccionDividendoByNumopeNumdiv() - inicia");
        BpiTraTransaccionesTbl dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT  t2.* " +
                                "    FROM bpi_com_comprobantes_tbl t1, bpi_tra_transacciones_tbl t2 " +
                                "   WHERE num_producto = ? " + "     AND cuota = ? " +
                                "     AND t2.cod_estado NOT IN (1, 2, 5, 99) " +
                                "     AND t1.id_transaccion = t2.id_transaccion " + "ORDER BY t2.fechainicio DESC ");
            stm.setLong(1, num_ope);
            stm.setInt(2, num_div);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new BpiTraTransaccionesTbl();
                dto.setCodEstado(rst.getInt("COD_ESTADO"));
                dto.setCodMedioPago(rst.getInt("COD_MEDIO"));
                dto.setFechafin(FechaUtil.toUtilDate(rst.getDate("FECHAFIN")));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getDate("FECHAINICIO")));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setObservaciones(rst.getString("OBSERVACIONES"));
                dto.setRutPersona(rst.getDouble("RUT_PERSONA"));
                dto.setMedioCodigoRespuesta(rst.getString("MEDIO_CODIGO_RESP"));
                dto.setMedioFechaPago(FechaUtil.toUtilDate(rst.getTimestamp("MEDIO_FECHA_PAGO")));
                dto.setMedioNumeroCuotas(rst.getInt("MEDIO_NUM_CUOTAS"));
                dto.setMedioNumeroTarjeta(rst.getString("MEDIO_NUM_TARJETA"));
                dto.setMedioTipoTarjeta(rst.getString("MEDIO_TIPO_TARJETA"));
                dto.setNumeroTransaccionBanco(rst.getString("NUM_TRANSACCION_MEDIO"));
                dto.setMedioOrdenCompra(rst.getString("MEDIO_NUM_ORDEN_COMP"));
                dto.setMedioCodigoAutorizacion(rst.getString("MEDIO_CODIGO_AUTORIZA"));
            }

        } catch (Exception e) {
            logger.error("findTransaccionDividendoByNumopeNumdiv() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionDividendoByNumopeNumdiv() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionDividendoByNumopeNumdiv() - termina");
        return dto;
    }

    /**
     * Buscar Comprobante de Caja Dividendo
     * @param num_ope
     * @param num_div
     * @return
     * TODO: Testear
     */
    public XMLDocument findComprobanteCajaDividendo(Integer num_ope, Integer num_div) {
        logger.info("findComprobanteCajaDividendo() - inicia");
        XMLDocument dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("call BPI_CPH_CARTOLAPAGOHISTOR_PKG.bpi_ocd_obtcomprobdividen_fun(?,?)");
            stm.setInt(1, num_ope);
            stm.setInt(2, num_div);
            stm.execute();
            String archivo = stm.getString(1);
            if (archivo != null) {
                DOMParser parser = new DOMParser();
                XMLDocument comprobante = null;
                logger.info("findComprobanteCajaDividendo() - comprobante encontrado, procedo a parsearlo");
                try {
                    parser.parse(new StringReader(archivo));
                    logger.info("findComprobanteCajaDividendo() - comprobante parseado");
                    comprobante = parser.getDocument();
                } catch (SAXException e) {
                    logger.error("findComprobanteCajaDividendo() - SAXException para num_ope " + num_ope +
                                 " y num_div " + num_div, e);
                } catch (IOException e) {
                    logger.error("findComprobanteCajaDividendo() - IOException para num_ope " + num_ope +
                                 " y num_div " + num_div, e);
                } finally {
                    return comprobante;
                }
            }
        } catch (Exception e) {
            logger.error("findComprobanteCajaDividendo() - catch (error)", e);
            dto = null;
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findComprobanteCajaDividendo() - catch (error)", f);
            }
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findComprobanteCajaDividendo() - termina");
        return dto;
    }

    /**
     * Buscra comprobantes de pago segun
     * numero de comprobante de pago poliza
     * @param idcomprobante
     * @return
     */
    public BpiCppComprobpagopolizaTbl findComprobPagoPolizaById(Long idcomprobante) {
        logger.info("findComprobPagoPolizaById() - inicia");
        BpiCppComprobpagopolizaTbl dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT ID_COMPROBPAGO, RUT_CLIENTE, " +
                                "FOLIO_OPERACION, LUGAR_PAGO, NOMBRE_CLIENTE, INSTR_PAGO, COMPROBANTE.getClobVal()   " +
                                "FROM BPI_CPP_COMPROBPAGOPOLIZA_TBL WHERE ID_COMPROBPAGO = ? ");
            stm.setLong(1, idcomprobante);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new BpiCppComprobpagopolizaTbl();
                dto.setFolioOperacion(rst.getLong("FOLIO_OPERACION"));
                dto.setIdComprobpago(rst.getLong("ID_COMPROBPAGO"));
                dto.setInstrPago(rst.getString("INSTR_PAGO"));
                dto.setLugarPago(rst.getString("LUGAR_PAGO"));
                dto.setNombreCliente(rst.getString("NOMBRE_CLIENTE"));
                dto.setRutCliente(rst.getLong("RUT_CLIENTE"));
                java.sql.Clob clb = rst.getClob(7);
                try {
                    String xml = StringUtil.clobToString(clb);
                    dto.setComprobante((XMLDocument) XmlUtil.parse(xml));
                } catch (IOException e) {
                    logger.error("findComprobPagoPolizaById() - IOException :" + e.getMessage(), e);
                } catch (SAXException e) {
                    logger.error("findComprobPagoPolizaById() - SAXException :" + e.getMessage(), e);
                } catch (ParserConfigurationException e) {
                    logger.error("findComprobPagoPolizaById() - ParserConfigurationException :" + e.getMessage(), e);
                }

            }

        } catch (Exception e) {
            logger.error("findComprobPagoPolizaById() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findComprobPagoPolizaById() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findComprobPagoPolizaById() - termina");
        return dto;
    }

    /**
     * Consulta el id de transaccion segun
     * producto y el maximo de la cuota
     * @param numprod
     * @param numcuo
     * @return
     * TODO: Testear
     */
    public BpiComComprobantesTbl findIdTransaccionByProductoCuotaMaximo(Long numprod, Integer numcuo) {
        logger.info("findIdTransaccionByProductoCuotaMaximo() - inicia");
        BpiComComprobantesTbl dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("select a.* from bpi_com_comprobantes_tbl a " +
                                "              where a.id_transaccion In (select max(b.id_transaccion) from bpi_com_comprobantes_tbl b " +
                                "              where b.num_producto=? " + "              and b.cuota = ?) ");
            stm.setLong(1, numprod);
            stm.setInt(2, numcuo);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new BpiComComprobantesTbl();
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setCuota(rst.getInt("CUOTA"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoBase(rst.getInt("MONTO_BASE"));
                dto.setMontoExcedente(rst.getInt("MONTO_EXCEDENTE"));
                dto.setMontoTotal(rst.getInt("MONTO_TOTAL"));
                dto.setNumProducto(rst.getLong("NUM_PRODUCTO"));
            }

        } catch (Exception e) {
            logger.error("findIdTransaccionByProductoCuotaMaximo() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findIdTransaccionByProductoCuotaMaximo() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findIdTransaccionByProductoCuotaMaximo() - termina");
        return dto;
    }

    /**
     * Consulta especifica que determina toda la informacion
     * relevante a pagos en e portal de pago, tanto de vida
     * com de hipetacario y conceptos de pago tales como
     *   PEC
     *   APV
     *   APT
     *   POLIZAS
     *   DIVIDENDOS
     *
     * @param numtrx
     * @return dto con toda la informacion
     */
    public BpiTraTransaccionesTbl findTransaccionCodigoEstadoByNumTransaccion(Long numtrx) {
        logger.info("findTransaccionCodigoEstadoByNumTransaccion() - inicia");
        BpiTraTransaccionesTbl dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_TRA_TRANSACCIONES_TBL  WHERE ID_TRANSACCION = ? ");
            stm.setLong(1, numtrx);
            rst = stm.executeQuery();
            if (rst.next()) {
                dto = new BpiTraTransaccionesTbl();
                dto.setCodEstado(rst.getInt("COD_ESTADO"));
                dto.setCodMedioPago(rst.getInt("COD_MEDIO"));
                dto.setFechafin(FechaUtil.toUtilDate(rst.getTimestamp("FECHAFIN")));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getTimestamp("FECHAINICIO")));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setObservaciones(rst.getString("OBSERVACIONES"));
                dto.setRutPersona(rst.getDouble("RUT_PERSONA"));
                dto.setMedioCodigoRespuesta(rst.getString("MEDIO_CODIGO_RESP"));
                dto.setMedioFechaPago(FechaUtil.toUtilDate(rst.getTimestamp("MEDIO_FECHA_PAGO")));
                dto.setMedioNumeroCuotas(rst.getInt("MEDIO_NUM_CUOTAS"));
                dto.setMedioNumeroTarjeta(rst.getString("MEDIO_NUM_TARJETA"));
                dto.setMedioTipoTarjeta(rst.getString("MEDIO_TIPO_TARJETA"));
                dto.setNumeroTransaccionBanco(rst.getString("NUM_TRANSACCION_MEDIO"));
                dto.setMedioOrdenCompra(rst.getString("MEDIO_NUM_ORDEN_COMP"));
                dto.setMedioCodigoAutorizacion(rst.getString("MEDIO_CODIGO_AUTORIZA"));
                dto.setCargo(rst.getDouble("CARGO"));
                dto.setDisponFondos(rst.getDate("DISPFONDOS"));
                dto.setTipoTransaccion(rst.getInt("TIPOTRX"));
                dto.setNumeroFolioCaja(rst.getLong("FOLIO"));
                dto.setCodigoEmpresa(rst.getString("EMPRESA"));
                dto.setCodigoOrigenTransaccion(rst.getString("ORIGEN_TRANSACCION"));
                java.sql.Date turnoSql = rst.getDate("TURNO");
                if (turnoSql != null)
                    dto.setTurno(new java.util.Date(turnoSql.getTime()));
                if (turnoSql == null)
                    dto.setTurno(null);

            }

            if (dto != null && dto.getIdTransaccion() != null) {
                Navegacion nav = DAOFactory.getPersistenciaGeneralDao().findNavegacionByIdTrx(dto.getIdTransaccion());
                if (nav != null && nav.getEmpresa() != null) {
                    dto.setIdEmpresa(nav.getEmpresa().longValue());
                }
            }

        } catch (Exception e) {
            logger.error("findTransaccionCodigoEstadoByNumTransaccion() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionCodigoEstadoByNumTransaccion() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionCodigoEstadoByNumTransaccion() - termina");
        return dto;
    }

    /**
     * Genera archivo mediante procedimiento almacenado
     * y en su respuesta se almacenan el buffer de data enviado
     * @param generafiledto
     * @return
     * TODO: Testear
     */
    public String GeneraArchivoCuadratura(PrcProcesoGeneraFileDto generafiledto) {
        logger.info("GeneraArchivoCuadratura() - inicia");
        String dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("{ call BPI_PRC_PROCESOGENERAFILE_PRC(?,?,?,?,?) }");
            stm.setString(1, generafiledto.getP_archivocuadr());
            stm.setInt(2, generafiledto.getP_mediopago());
            stm.setInt(3, generafiledto.getP_empresa());
            stm.setTimestamp(4,
                             new Timestamp(FechaUtil.getFecha("dd-MM-yyyy",
                                                              generafiledto.getP_fechacuadr()).getTime()));
            stm.setString(5, generafiledto.getP_buffer());
            stm.registerOutParameter("P_OUTBUFFER", OracleTypes.VARCHAR);
            stm.execute();
            String data = stm.getString("P_OUTBUFFER");
            dto = data;
        } catch (Exception e) {
            logger.error("GeneraArchivoCuadratura() - catch (error)", e);
            dto = null;
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("GeneraArchivoCuadratura() - catch (error)", f);
            }
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("GeneraArchivoCuadratura() - termina");
        return dto;
    }

    /**
     * Lectura de Archivo
     * @param lecturafiledto
     * @return
     * TODO: Testear
     */
    public String LecturaArchivoCuadratura(PrcProcesoReadFileDto lecturafiledto) {
        logger.info("LecturaArchivoCuadratura() - inicia");
        String dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("{ call BPI_PRC_PROCESOREADFILE_PRC(?,?,?,?,?) }");
            stm.setString(1, lecturafiledto.getP_archivocuadr());
            stm.setInt(2, lecturafiledto.getP_mediopago());
            stm.setInt(3, lecturafiledto.getP_empresa());
            stm.setTimestamp(4,
                             new Timestamp(FechaUtil.getFecha("dd-MM-yyyy",
                                                              lecturafiledto.getP_fechacuadr()).getTime()));
            stm.registerOutParameter("P_OUTBUFFER", OracleTypes.VARCHAR);
            stm.execute();
            String data = stm.getString("P_OUTBUFFER");
            dto = data;
        } catch (Exception e) {
            logger.error("LecturaArchivoCuadratura() - catch (error)", e);
            dto = null;
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("LecturaArchivoCuadratura() - catch (error)", f);
            }
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("LecturaArchivoCuadratura() - termina");
        return dto;
    }

    /**
     * Consulta el ultimo rango de recudacion
     * segun fechas de inicio y termino
     * @param fechamenor
     * @param fechamayor
     * @return
     * TODO: Testear
     */
    public List<RecaudacionByEmpresa> findLastRecaudacionByEmpresabyRango(Date fechamenor, Date fechamayor) {
        logger.info("findLastRecaudacionByEmpresabyRango() - inicia");
        List<RecaudacionByEmpresa> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT cod_empresa, SUM(monto_total) as monto_total, SUM(transacciones) as transacciones,  SUM (id_cuadratura) as id_cuadratura " +
                                "FROM bicevida.bpi_cua_cuadratura_tbl  " + "WHERE fecha_hora between ? and ? " +
                                "GROUP BY cod_empresa ");
            stm.setTimestamp(1, new java.sql.Timestamp(fechamenor.getTime()));
            stm.setTimestamp(2, new java.sql.Timestamp(fechamayor.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                RecaudacionByEmpresa dto = new RecaudacionByEmpresa();
                dto.setCod_empresa(rst.getInt("cod_empresa"));
                dto.setMonto_total(rst.getLong("monto_total"));
                dto.setNum_transacciones(rst.getInt("transacciones"));
                dto.setId_cuadratura(rst.getLong("id_cuadratura"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionByEmpresabyRango() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionByEmpresabyRango() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastRecaudacionByEmpresabyRango() - termina");
        return dtos;
    }

    /**
     * Consulta recudacion segun empresa
     * periodo, mediod e pago y rango de fechas
     * @param cod_empresa
     * @param cod_medio
     * @param fechamenor
     * @param fechamayor
     * @return
     * TODO: Testear
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyRango(Integer cod_empresa,
                                                                                        Integer cod_medio,
                                                                                        Date fechamenor,
                                                                                        Date fechamayor) {
        logger.info("findLastRecaudacionByEmpresaMediobyRango() - inicia");
        List<RecaudacionByEmpMedioInPeriod> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            /*stm = cnx.prepareCall("SELECT COD_MEDIO, COD_EMPRESA, ID_TRANSACCION, NUM_PRODUCTO, CUOTA, MONTO_TOTAL,  " +
                            "NOMBRE_PERSONA, NOMBRE, FECHA_HORA, NUM_TRANSACCION_MEDIO, COD_PRODUCTO, RUT_PERSONA, FECHA_PAGO, TURNO, ID_CUADRATURA " +
                            "FROM BPI_DTC_DETTRANCUAD_VW  " +
                            "WHERE ((((COD_EMPRESA = ?)  " +
                            "AND (COD_MEDIO = ?))  " +
                            "AND (FECHA_HORA >= ?))  " +
                            "AND (FECHA_HORA <= ?)) " +
                            "order by ID_TRANSACCION desc ");*/
            stm =
                cnx.prepareCall("SELECT t0.COD_MEDIO, t0.COD_EMPRESA, t0.ID_TRANSACCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL, \n" +
                                "       t0.NOMBRE_PERSONA, t0.NOMBRE, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO, t0.COD_PRODUCTO, t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO, t0.ID_CUADRATURA\n" +
                                "  FROM BPI_DTC_DETTRANCUAD_VW t0\n" + "  WHERE t0.COD_EMPRESA = ?\n" +
                                "  AND t0.COD_MEDIO = ?\n" + "  AND t0.FECHA_HORA BETWEEN ? AND ?  \n" +
                                "  order by t0.ID_TRANSACCION desc");
            stm.setInt(1, cod_empresa);
            stm.setInt(2, cod_medio);
            stm.setTimestamp(3, new java.sql.Timestamp(fechamenor.getTime()));
            stm.setTimestamp(4, new java.sql.Timestamp(fechamayor.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                RecaudacionByEmpMedioInPeriod dto = new RecaudacionByEmpMedioInPeriod();
                dto.setCod_empresa(rst.getInt("COD_EMPRESA"));
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setCuota(rst.getInt("CUOTA"));
                dto.setFecha_hora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setFecha_Pago(FechaUtil.toUtilDate(rst.getDate("FECHA_PAGO")));
                dto.setIdCuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getInt("MONTO_TOTAL"));
                dto.setNombre_persona(rst.getString("NOMBRE_PERSONA"));
                dto.setNombre_producto(rst.getString("NOMBRE"));
                dto.setNumero_producto(rst.getLong("NUM_PRODUCTO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dto.setRut(rst.getString("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionByEmpresaMediobyRango() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionByEmpresaMediobyRango() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastRecaudacionByEmpresaMediobyRango() - termina");
        return dtos;
    }

    /**
     * Consulta recudacion segun empresa
     * periodo, mediod e pago y rango de fechas
     * @param cod_empresa
     * @param cod_medio
     * @param fechamenor
     * @param fechamayor
     * @param cod_medio2
     * @return
     * TODO: Testear
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyRangoByMedio2(Integer cod_empresa,
                                                                                                Integer cod_medio,
                                                                                                Date fechamenor,
                                                                                                Date fechamayor,
                                                                                                Integer cod_medio2) {
        logger.info("findLastRecaudacionByEmpresaMediobyRango() - inicia");
        List<RecaudacionByEmpMedioInPeriod> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();

            String sql =
                "SELECT  t0.COD_MEDIO, t0.COD_EMPRESA, t0.ID_TRANSACCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL,\n" +
                "        t0.NOMBRE_PERSONA, t0.NOMBRE, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO, t0.COD_PRODUCTO, \n" +
                "        t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO, t0.ID_CUADRATURA, t0.COD_MEDIO2\n" +
                "FROM    BPI_DTC_DETTRANCUAD_VW t0\n" + "WHERE   t0.COD_EMPRESA = ?\n" + "AND     t0.COD_MEDIO = ?\n" +
                "AND     t0.COD_MEDIO2 = ?\n" + "AND     t0.FECHA_HORA BETWEEN ? AND ?\n" +
                "ORDER BY t0.ID_TRANSACCION DESC";

            stm = cnx.prepareCall(sql);
            stm.setInt(1, cod_empresa);
            stm.setInt(2, cod_medio);
            stm.setInt(3, cod_medio2);
            stm.setTimestamp(4, new java.sql.Timestamp(fechamenor.getTime()));
            stm.setTimestamp(5, new java.sql.Timestamp(fechamayor.getTime()));

            rst = stm.executeQuery();
            while (rst.next()) {
                RecaudacionByEmpMedioInPeriod dto = new RecaudacionByEmpMedioInPeriod();
                dto.setCod_empresa(rst.getInt("COD_EMPRESA"));
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setCuota(rst.getInt("CUOTA"));
                dto.setFecha_hora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setFecha_Pago(FechaUtil.toUtilDate(rst.getDate("FECHA_PAGO")));
                dto.setIdCuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getInt("MONTO_TOTAL"));
                dto.setNombre_persona(rst.getString("NOMBRE_PERSONA"));
                dto.setNombre_producto(rst.getString("NOMBRE"));
                dto.setNumero_producto(rst.getLong("NUM_PRODUCTO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dto.setRut(rst.getString("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionByEmpresaMediobyRango() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionByEmpresaMediobyRango() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastRecaudacionByEmpresaMediobyRango() - termina");
        return dtos;
    }


    /**
     * Consulta Informacion de recudacion
     * por empresa en un determinado periodo
     * @param fechamenor
     * @param fechamayor
     * @return
     * TODO: Testear
     */
    public List<BpiRtcRestrancuadrVw> findLastRecaudacionByEmpresainPeriodo(Date fechamenor, Date fechamayor,
                                                                            Long empresa) {
        logger.info("findLastRecaudacionByEmpresainPeriodo() - inicia");
        List<BpiRtcRestrancuadrVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;

        String filtro = "";

        if (empresa != 0) {
            filtro = "  AND a.cod_empresa = ?\n";
        }

        try {
            cnx = getConnection();
            //stm = cnx.prepareCall("SELECT * FROM BPI_RTC_RESTRANCUADR_VW WHERE FECHA_HORA BETWEEN ? AND ?");
            stm =
                cnx.prepareCall("SELECT a.cod_empresa, a.nombre_empresa, a.fecha_hora, \n" +
                                "       sum(a.transacciones) as transacciones, sum(a.monto_total) as monto_total, sum(a.transacciones_prob) as transacciones_prob,\n" +
                                "       sum(a.monto_total_prob) as monto_total_prob\n" +
                                "  FROM bpi_rtc_restrancuadr_vw a\n" + "  WHERE a.fecha_hora BETWEEN ? AND ?\n" +
                                filtro + "  group by a.cod_empresa, a.nombre_empresa, a.fecha_hora");
            stm.setTimestamp(1, new java.sql.Timestamp(fechamenor.getTime()));
            stm.setTimestamp(2, new java.sql.Timestamp(fechamayor.getTime()));
            if (empresa.intValue() != 0) {
                stm.setLong(3, empresa);
            }
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiRtcRestrancuadrVw dto = new BpiRtcRestrancuadrVw();
                dto.setCodEmpresa(rst.getDouble("COD_EMPRESA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                //dto.setIdCuadratura(rst.getDouble("ID_CUADRATURA"));
                //dto.setLogCuadr(rst.getString("LOG_CUADR"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setMontoTotalProb(rst.getDouble("MONTO_TOTAL_PROB"));
                dto.setNombreEmpresa(rst.getString("NOMBRE_EMPRESA"));
                dto.setTransacciones(rst.getDouble("TRANSACCIONES"));
                dto.setTransaccionesProb(rst.getDouble("TRANSACCIONES_PROB"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionByEmpresainPeriodo() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionByEmpresainPeriodo() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastRecaudacionByEmpresainPeriodo() - termina");
        return dtos;
    }

    /**
     * Busca la ultima recudacion
     * @param id
     * @param fechamenor
     * @param fechamayor
     * @return
     * TODO: Testear
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainPeriodo(Integer id, Date fechamenor,
                                                                                      Date fechamayor) {
        logger.info("findLastRecaudacionMedioByEmpresainPeriodo() - inicia");
        List<RecaudacionMedioByEmpresa> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            //Consulta todos los medios de pago existentes
            List allmed = DAOFactory.getPersistenciaGeneralDao().findAllMediosdePagoElectronico(id);

            //Consulta la informacion
            cnx = getConnection();
            /*stm = cnx.prepareCall("SELECT t0.ID_CUADRATURA, t0.COD_MEDIO, t1.COD_MEDIO, t0.COD_EMPRESA, t0.MONTO_TOTAL, t0.FECHA_HORA, t1.NOMBRE, t2.num_cuadr as transacciones " +
                                "FROM BPI_CUA_CUADRATURA_TBL t0, BPI_MPG_MEDIOPAGO_TBL t1, BPI_LGC_LOGCUADRATURA_TBL t2 " +
                                "WHERE ((((t0.COD_EMPRESA = ?)  " +
                                "AND (t0.FECHA_HORA >= ?))  " +
                                "AND (t0.FECHA_HORA <= ?))  " +
                                "AND (t1.COD_MEDIO = t0.COD_MEDIO) " +
                                "AND (t2.id_cuadratura = t0.id_cuadratura))");
              */
            /*stm = cnx.prepareCall("SELECT t0.ID_CUADRATURA, t0.COD_MEDIO, t1.COD_MEDIO, t0.COD_EMPRESA, t0.MONTO_TOTAL, t0.FECHA_HORA, t1.NOMBRE, t2.num_cuadr as transacciones\n" +
            "  FROM BPI_CUA_CUADRATURA_TBL t0, BPI_LGC_LOGCUADRATURA_TBL t2, BPI_DLC_DETALLELOGCUADR_TBL t3, BPI_MPG_MEDIOPAGO_TBL t1\n" +
            "  WHERE t0.COD_EMPRESA = ?\n" +
            "  AND t0.FECHA_HORA BETWEEN ? AND ?  \n" +
            "  AND t2.ID_CUADRATURA = t0.ID_CUADRATURA\n" +
            "  AND t3.ID_CUADRATURA = t0.ID_CUADRATURA\n" +
            "  AND t1.COD_MEDIO = t0.COD_MEDIO");*/

            String sql =
                "SELECT t0.COD_MEDIO, t1.COD_MEDIO, t0.COD_EMPRESA, sum(t0.MONTO_TOTAL) as MONTO_TOTAL, t0.FECHA_HORA, t1.NOMBRE, sum(t2.NUM_CUADR) as transacciones\n" +
                "  FROM BPI_CUA_CUADRATURA_TBL t0, BPI_LGC_LOGCUADRATURA_TBL t2, BPI_DLC_DETALLELOGCUADR_TBL t3, BPI_MPG_MEDIOPAGO_TBL t1\n" +
                "  WHERE t0.COD_EMPRESA = ?\n" + "  AND t0.FECHA_HORA BETWEEN ? AND ?  \n" +
                "  AND t2.ID_CUADRATURA = t0.ID_CUADRATURA\n" + "  AND t3.ID_CUADRATURA = t0.ID_CUADRATURA\n" +
                "  AND t1.COD_MEDIO = t0.COD_MEDIO\n" + "  AND t2.NUM_CUADR > 0\n" +
                "  GROUP BY t0.COD_MEDIO, t1.COD_MEDIO, t0.COD_EMPRESA, t0.FECHA_HORA, t1.NOMBRE" +
                "  ORDER BY t0.COD_MEDIO";

            stm = cnx.prepareCall(sql);

            stm.setInt(1, id);
            stm.setTimestamp(2, new java.sql.Timestamp(fechamenor.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechamayor.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                RecaudacionMedioByEmpresa dto = new RecaudacionMedioByEmpresa();
                dto.setCod_empresa(rst.getInt("COD_EMPRESA"));
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setFecha(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                //dto.setId_cuadratura(rst.getInt("ID_CUADRATURA"));
                dto.setMonto_total(rst.getLong("MONTO_TOTAL"));
                dto.setNombre_medio(rst.getString("NOMBRE"));
                dto.setNum_transacciones(rst.getInt("TRANSACCIONES"));
                dtos.add(dto);
            }

            // Consulta Todos los medios de pago
            // y los que no estan los deja con ceros
            // para el detalle de presentacion
            for (Iterator it = allmed.iterator(); it.hasNext();) {
                MedioPago medio = (MedioPago) it.next();
                boolean addmedio = true;
                for (Iterator itreview = dtos.iterator(); itreview.hasNext();) {
                    RecaudacionMedioByEmpresa recau = (RecaudacionMedioByEmpresa) itreview.next();
                    if (recau.getCod_medio().intValue() == medio.getCodMedio().intValue()) {
                        addmedio = false;
                        break;
                    }
                }

                if (addmedio) {
                    RecaudacionMedioByEmpresa dtorec = new RecaudacionMedioByEmpresa();
                    dtorec.setCod_empresa(id);
                    dtorec.setCod_medio(medio.getCodMedio());
                    dtorec.setFecha(fechamenor);
                    dtorec.setId_cuadratura(new Integer(0));
                    dtorec.setMonto_total(new Long(0));
                    dtorec.setNombre_medio(medio.getNombre());
                    dtorec.setNum_transacciones(new Integer(0));
                    dtos.add(dtorec);
                }
            }
        } catch (Exception e) {
            logger.error("findLastRecaudacionMedioByEmpresainPeriodo() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionMedioByEmpresainPeriodo() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastRecaudacionMedioByEmpresainPeriodo() - termina");
        return dtos;

    }

    /**
     * Retorna lista de resumen de recaudaci�n del medio indicado
     * @param empresa
     * @param fechamenor
     * @param fechamayor
     * @param cod_medio
     * @return
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainPeriodoByMedio(Integer empresa,
                                                                                             Date fechamenor,
                                                                                             Date fechamayor,
                                                                                             Integer cod_medio) {
        logger.info("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - inicia");
        List<RecaudacionMedioByEmpresa> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;

        try {
            cnx = getConnection();

            String sql =
                "SELECT  t.cod_banco2medio, t.cod_medio, de.cod_empresa, sum(t.monto_total) as monto_total, c.fecha_hora,   \n" +
                "        me.descripcion as  nombre, count(t.id_transaccion) as transacciones  \n" +
                "FROM    bicevida.bpi_tra_transacciones_tbl t,  \n" +
                "        bicevida.bpi_dte_dettransemp_tbl de,  \n" + "        bicevida.bpi_dcu_detcuad_tbl d,  \n" +
                "        bicevida.bpi_cua_cuadratura_tbl c, \n" + "        bicevida.bpi_bco_bancos_tbl me\n" +
                "WHERE   de.id_transaccion = t.id_transaccion  \n" + "  AND   de.cod_medio = t.cod_medio  \n" +
                "  AND   d.id_transaccion = t.id_transaccion  \n" + "  AND   c.id_cuadratura = d.id_cuadratura    \n" +
                "  AND   c.cod_empresa = de.cod_empresa  \n" + "  AND   c.cod_medio = t.cod_medio   \n" +
                "  AND   me.codigo = t.cod_banco2medio              \n" + "  AND   de.cod_empresa = ?\n" +
                "  AND   c.fecha_hora BETWEEN ? AND ?  \n" + "  AND   t.cod_medio = ?  \n" +
                "  group by t.cod_banco2medio, t.cod_medio, de.cod_empresa, c.fecha_hora, me.descripcion  \n" +
                "  order by t.cod_medio";

            stm = cnx.prepareCall(sql);

            stm.setInt(1, empresa);
            stm.setTimestamp(2, new java.sql.Timestamp(fechamenor.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechamayor.getTime()));
            stm.setInt(4, cod_medio);
            rst = stm.executeQuery();

            while (rst.next()) {
                RecaudacionMedioByEmpresa dto = new RecaudacionMedioByEmpresa();
                dto.setCod_empresa(rst.getInt("cod_empresa"));
                dto.setCod_medio(rst.getInt("cod_banco2medio"));
                dto.setFecha(FechaUtil.toUtilDate(rst.getDate("fecha_hora")));
                dto.setMonto_total(rst.getLong("monto_total"));
                dto.setNombre_medio(rst.getString("nombre"));
                dto.setNum_transacciones(rst.getInt("transacciones"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                logger.error("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - catch (error)", e);
            }
        }
        logger.info("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - termina");
        return dtos;
    }

    /**
     * Busca las transacciones generadas por una
     * empresa y segun numero de transaccion
     * @param trx
     * @return
     */
    public List<TransaccionByEmpresa> findTransaccionesByEmpresabyTransaccion(String trx) {
        logger.info("findTransaccionesByEmpresabyTransaccion() - inicia");
        List dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT emp.nombre, d.cod_empresa, d.cod_empresa as id_transaccion, COUNT (*) as num_transaccion_medio, SUM (d.monto_total) as monto_total  " +
                                "FROM bicevida.bpi_dte_dettransemp_tbl d, bicevida.bpi_tra_transacciones_tbl t, bpi_emp_empresas_tbl emp  " +
                                "WHERE t.id_transaccion = ? " + " AND emp.cod_empresa = d.cod_empresa " +
                                " AND d.id_transaccion = t.id_transaccion and t.cod_estado in (3,6,7) " +
                                "GROUP BY emp.nombre, d.cod_empresa");
            stm.setString(1, trx);
            rst = stm.executeQuery();
            while (rst.next()) {
                TransaccionByEmpresa dto = new TransaccionByEmpresa();
                dto.setCod_empresa(rst.getInt("cod_empresa"));
                dto.setId_transaccion(rst.getInt("id_transaccion"));
                dto.setNum_transacciones(rst.getInt("num_transaccion_medio"));
                dto.setMonto_total(rst.getLong("monto_total"));
                //A�ade la empresa asociada
                Empresas emp = new Empresas();
                emp.setCodEmpresa(rst.getDouble("cod_empresa"));
                emp.setNombre(rst.getString("nombre"));
                dto.setEmpresa(emp);

                dtos.add(dto);
            }
        } catch (Exception e) {
            logger.error("findTransaccionesByEmpresabyTransaccion() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesByEmpresabyTransaccion() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionesByEmpresabyTransaccion() - termina");
        return dtos;

    }

    /**
     * Busca transacciones segun medio de pago
     * emnpresa por el numero de transaccion
     * @param id
     * @param trx
     * @return
     */
    public List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresaByTrx(Integer id, String trx) {
        logger.info("findTransaccionesMedioByEmpresaByTrx() - inicia");
        List<TransaccionMedioByEmpresa> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        String filtro = "";

        /*if(id.intValue() == 2){
            filtro = "AND t.cod_medio not in (4) \n";
        }else{
            filtro = "";
        }*/

        try {
            //Consulta todos los medios de pago existentes
            List allmed = DAOFactory.getPersistenciaGeneralDao().findAllMediosdePagoElectronico(id);

            //Consulta la informacion
            cnx = getConnection();
            String sql =
                "SELECT d.cod_medio as id_transaccion, d.cod_medio, COUNT (*) as num_transaccion_medio,  SUM (d.monto_total) as monto_total " +
                "FROM bicevida.bpi_dte_dettransemp_tbl d, bicevida.bpi_tra_transacciones_tbl t  " +
                " WHERE d.cod_empresa = ? " + " AND t.id_transaccion = ? " +
                " AND d.id_transaccion = t.id_transaccion and t.cod_estado in (3,6,7) " + filtro +
                " GROUP BY d.cod_medio " + " ORDER BY t.cod_medio ";

            stm = cnx.prepareCall(sql);
            stm.setInt(1, id);
            stm.setString(2, trx);
            rst = stm.executeQuery();
            while (rst.next()) {
                TransaccionMedioByEmpresa dto = new TransaccionMedioByEmpresa();
                dto.setCod_empresa(id);
                dto.setId_transaccion(rst.getInt("id_transaccion"));
                dto.setCod_medio(rst.getInt("cod_medio"));
                dto.setNum_transacciones(rst.getInt("num_transaccion_medio"));
                dto.setMonto_total(rst.getLong("monto_total"));
                for (Iterator itreview = allmed.iterator(); itreview.hasNext();) {
                    MedioPago medio = (MedioPago) itreview.next();
                    if (medio.getCodMedio().intValue() == dto.getCod_medio().intValue()) {
                        dto.setMedio(medio);
                        break;
                    }
                }
                dtos.add(dto);
            }

            // Consulta Todos los medios de pago
            // y los que no estan los deja con ceros
            // para el detalle de presentacion
            for (Iterator it = allmed.iterator(); it.hasNext();) {
                MedioPago medio = (MedioPago) it.next();
                boolean addmedio = true;
                for (Iterator itreview = dtos.iterator(); itreview.hasNext();) {
                    TransaccionMedioByEmpresa recau = (TransaccionMedioByEmpresa) itreview.next();
                    if (recau.getCod_medio().intValue() == medio.getCodMedio().intValue()) {
                        addmedio = false;
                        break;
                    }
                }

                if (addmedio) {
                    TransaccionMedioByEmpresa medionew = new TransaccionMedioByEmpresa();
                    medionew.setCod_empresa(id);
                    medionew.setCod_medio(medio.getCodMedio());
                    medionew.setMedio(medio);
                    medionew.setNum_transacciones(new Integer(0));
                    medionew.setMonto_total(new Long(0));
                    dtos.add(medionew);
                }
            }
        } catch (Exception e) {
            logger.error("findTransaccionesMedioByEmpresaByTrx() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesMedioByEmpresaByTrx() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionesMedioByEmpresaByTrx() - termina");
        return dtos;

    }

    /**
     * Consulta transacciones segun medio empresa
     * y rut
     * @param id
     * @param rut
     * @return
     * TODO: Testear
     */
    public List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresaByRut(Integer id, String rut) {
        logger.info("findTransaccionesMedioByEmpresaByRut() - inicia");
        List<TransaccionMedioByEmpresa> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        String filtro = "";

        /*if(id.intValue() == 2){
            filtro = "AND t.cod_medio not in (4) \n";
        }else{
            filtro = "";
        }*/


        try {
            //Consulta todos los medios de pago existentes
            List allmed = DAOFactory.getPersistenciaGeneralDao().findAllMediosdePagoElectronico(id);

            //Consulta la informacion
            cnx = getConnection();
            String sql =
                "SELECT d.cod_medio as id_transaccion, d.cod_medio, COUNT (*) as num_transaccion_medio,  SUM (d.monto_total) as monto_total " +
                "FROM bicevida.bpi_dte_dettransemp_tbl d, bicevida.bpi_tra_transacciones_tbl t  " +
                " WHERE d.cod_empresa = ? " + " AND t.rut_persona = ? " +
                " AND d.id_transaccion = t.id_transaccion and t.cod_estado in (3,6,7) " + filtro +
                " GROUP BY d.cod_medio " + " ORDER BY d.cod_medio ";
            stm = cnx.prepareCall(sql);
            stm.setInt(1, id);
            stm.setString(2, rut);
            rst = stm.executeQuery();
            while (rst.next()) {
                TransaccionMedioByEmpresa dto = new TransaccionMedioByEmpresa();
                dto.setCod_empresa(id);
                dto.setId_transaccion(rst.getInt("id_transaccion"));
                dto.setCod_medio(rst.getInt("cod_medio"));
                dto.setNum_transacciones(rst.getInt("num_transaccion_medio"));
                dto.setMonto_total(rst.getLong("monto_total"));
                for (Iterator itreview = allmed.iterator(); itreview.hasNext();) {
                    MedioPago medio = (MedioPago) itreview.next();
                    if (medio.getCodMedio().intValue() == dto.getCod_medio().intValue()) {
                        dto.setMedio(medio);
                        break;
                    }
                }
                dtos.add(dto);
            }

            // Consulta Todos los medios de pago
            // y los que no estan los deja con ceros
            // para el detalle de presentacion
            for (Iterator it = allmed.iterator(); it.hasNext();) {
                MedioPago medio = (MedioPago) it.next();
                boolean addmedio = true;
                for (Iterator itreview = dtos.iterator(); itreview.hasNext();) {
                    TransaccionMedioByEmpresa recau = (TransaccionMedioByEmpresa) itreview.next();
                    if (recau.getCod_medio().intValue() == medio.getCodMedio().intValue()) {
                        addmedio = false;
                        break;
                    }
                }

                if (addmedio) {
                    TransaccionMedioByEmpresa medionew = new TransaccionMedioByEmpresa();
                    medionew.setCod_empresa(id);
                    medionew.setCod_medio(medio.getCodMedio());
                    medionew.setMedio(medio);
                    medionew.setNum_transacciones(new Integer(0));
                    medionew.setMonto_total(new Long(0));
                    dtos.add(medionew);
                }
            }
        } catch (Exception e) {
            logger.error("findTransaccionesMedioByEmpresaByRut() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesMedioByEmpresaByRut() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionesMedioByEmpresaByRut() - termina");
        return dtos;
    }

    /**
     * Consulta transacciones segun empresa y medio
     * en determina fecha
     * @param cod_empresa
     * @param cod_medio
     * @param fechaini
     * @param fechfin
     * @return
     * TODO: Testear
     */
    public List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByFecha(Integer cod_empresa, Integer cod_medio,
                                                                            Date fechaini, Date fechfin) {
        logger.info("findTransaccionesByEmpresaMedioByFecha() - inicia");
        List<BpiDttDettraturnoVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT t0.COD_EMPRESA, t0.COD_MEDIO, t0.ID_TRANSACCION, t0.CUOTA, " +
                                "       t0.NUM_PRODUCTO, t0.MONTO_TOTAL, t0.NOMBRE_PERSONA, t0.NOMBRE, " +
                                "       t0.FECHAINICIO, t0.NUM_TRANSACCION_MEDIO, t0.COD_PRODUCTO, " +
                                "       t0.RUT_PERSONA, t0.TURNO, t0.ESTADO, t0.ID_CUADRATURA, t0.CANAL, t0.FOLIO_CAJA " +
                                "  FROM BPI_DTT_DETTRATURNO_VW t0   " + "WHERE t0.COD_EMPRESA = ? " +
                                "AND t0.COD_MEDIO = ? " + "AND t0.TURNO BETWEEN ? AND ? " +
                                "order by t0.ID_TRANSACCION desc");
            stm.setInt(1, cod_empresa);
            stm.setInt(2, cod_medio);
            stm.setTimestamp(3, new Timestamp(fechaini.getTime()));
            stm.setTimestamp(4, new Timestamp(fechfin.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiDttDettraturnoVw dto = new BpiDttDettraturnoVw();
                dto.setCodEmpresa(rst.getLong("COD_EMPRESA"));
                dto.setCodMedio(rst.getLong("COD_MEDIO"));
                dto.setCodProducto(rst.getLong("COD_PRODUCTO"));
                dto.setCuota(rst.getLong("CUOTA"));
                dto.setEstado(rst.getLong("ESTADO"));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getTimestamp("FECHAINICIO")));
                dto.setIdCuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getLong("MONTO_TOTAL"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setNumProducto(rst.getLong("NUM_PRODUCTO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dto.setRutPersona(rst.getLong("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getTimestamp("TURNO")));
                dto.setCanal(rst.getString("CANAL"));
                dto.setFolioCaja(rst.getLong("FOLIO_CAJA"));
                dtos.add(dto);
            }
        } catch (Exception e) {
            logger.error("findTransaccionesByEmpresaMedioByFecha() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesByEmpresaMedioByFecha() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionesByEmpresaMedioByFecha() - termina");
        return dtos;
    }

    /**
     * Consulta transacicones por empresa medio de pago
     * y el numero de transaccion
     * @param cod_empresa
     * @param cod_medio
     * @param trx
     * @return
     * TODO: Testear
     */
    public List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByTrx(Integer cod_empresa, Integer cod_medio,
                                                                          Integer trx) {
        logger.info("findTransaccionesByEmpresaMedioByTrx() - inicia");
        List<BpiDttDettraturnoVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT t0.COD_EMPRESA, t0.COD_MEDIO, t0.ID_TRANSACCION, t0.CUOTA, " +
                                "       t0.NUM_PRODUCTO, t0.MONTO_TOTAL, t0.NOMBRE_PERSONA, t0.NOMBRE, " +
                                "       t0.FECHAINICIO, t0.NUM_TRANSACCION_MEDIO, t0.COD_PRODUCTO, " +
                                "       t0.RUT_PERSONA, t0.TURNO, t0.ESTADO, t0.ID_CUADRATURA,  t0.CANAL, t0.FOLIO_CAJA " +
                                "  FROM BPI_DTT_DETTRATURNO_VW t0   " + "WHERE t0.COD_EMPRESA = ? " +
                                "AND t0.COD_MEDIO = ? " + "AND t0.ID_TRANSACCION = ? " +
                                "order by t0.ID_TRANSACCION desc");
            stm.setInt(1, cod_empresa);
            stm.setInt(2, cod_medio);
            stm.setInt(3, trx);
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiDttDettraturnoVw dto = new BpiDttDettraturnoVw();
                dto.setCodEmpresa(rst.getLong("COD_EMPRESA"));
                dto.setCodMedio(rst.getLong("COD_MEDIO"));
                dto.setCodProducto(rst.getLong("COD_PRODUCTO"));
                dto.setCuota(rst.getLong("CUOTA"));
                dto.setEstado(rst.getLong("ESTADO"));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getTimestamp("FECHAINICIO")));
                dto.setIdCuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getLong("MONTO_TOTAL"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setNumProducto(rst.getLong("NUM_PRODUCTO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dto.setRutPersona(rst.getLong("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getTimestamp("TURNO")));
                dto.setCanal(rst.getString("CANAL"));
                dto.setFolioCaja(rst.getLong("FOLIO_CAJA"));
                dtos.add(dto);
            }
        } catch (Exception e) {
            logger.error("findTransaccionesByEmpresaMedioByTrx() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesByEmpresaMedioByTrx() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionesByEmpresaMedioByTrx() - termina");
        return dtos;
    }

    /**
     * Consulta las transacciones de empres ay medio segun
     * rut
     * @param cod_empresa
     * @param cod_medio
     * @param rut
     * @return
     * TODO: Testear
     */
    public List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByRut(Integer cod_empresa, Integer cod_medio,
                                                                          Integer rut) {
        logger.info("findTransaccionesByEmpresaMedioByRut() - inicia");
        List<BpiDttDettraturnoVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT t0.COD_EMPRESA, t0.COD_MEDIO, t0.ID_TRANSACCION, t0.CUOTA, " +
                                "       t0.NUM_PRODUCTO, t0.MONTO_TOTAL, t0.NOMBRE_PERSONA, t0.NOMBRE, " +
                                "       t0.FECHAINICIO, t0.NUM_TRANSACCION_MEDIO, t0.COD_PRODUCTO, " +
                                "       t0.RUT_PERSONA, t0.TURNO, t0.ESTADO, t0.ID_CUADRATURA, t0.CANAL, t0.FOLIO_CAJA " +
                                "  FROM BPI_DTT_DETTRATURNO_VW t0   " + "WHERE t0.COD_EMPRESA = ? " +
                                "AND t0.COD_MEDIO = ? " + "AND t0.RUT_PERSONA = ? " +
                                "order by t0.ID_TRANSACCION desc");
            stm.setInt(1, cod_empresa);
            stm.setInt(2, cod_medio);
            stm.setInt(3, rut);
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiDttDettraturnoVw dto = new BpiDttDettraturnoVw();
                dto.setCodEmpresa(rst.getLong("COD_EMPRESA"));
                dto.setCodMedio(rst.getLong("COD_MEDIO"));
                dto.setCodProducto(rst.getLong("COD_PRODUCTO"));
                dto.setCuota(rst.getLong("CUOTA"));
                dto.setEstado(rst.getLong("ESTADO"));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getTimestamp("FECHAINICIO")));
                dto.setIdCuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getLong("MONTO_TOTAL"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setNumProducto(rst.getLong("NUM_PRODUCTO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dto.setRutPersona(rst.getLong("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getTimestamp("TURNO")));
                dto.setCanal(rst.getString("CANAL"));
                dto.setFolioCaja(rst.getLong("FOLIO_CAJA"));
                dtos.add(dto);
            }
        } catch (Exception e) {
            logger.error("findTransaccionesByEmpresaMedioByRut() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesByEmpresaMedioByRut() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionesByEmpresaMedioByRut() - termina");
        return dtos;
    }

    /**
     * Cunsulta de cuadratura por id de cuadratura
     * @param id
     * @param cuadratura
     * @return
     * TODO: Testear
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresaRut(Integer id, Integer cuadratura) {
        logger.info("findLastRecaudacionMedioByEmpresaRut() - inicia");
        List<RecaudacionMedioByEmpresa> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            //Consulta todos los medios de pago existentes
            List allmed = DAOFactory.getPersistenciaGeneralDao().findAllMediosdePagoElectronico(id);

            //Consulta la informacion
            cnx = getConnection();
            /*stm = cnx.prepareCall("SELECT t0.ID_CUADRATURA, t0.COD_MEDIO, t1.COD_MEDIO, t0.COD_EMPRESA, t0.MONTO_TOTAL, t0.FECHA_HORA, t1.NOMBRE, t2.num_cuadr as transacciones " +
                                "FROM BPI_CUA_CUADRATURA_TBL t0, BPI_MPG_MEDIOPAGO_TBL t1, BPI_LGC_LOGCUADRATURA_TBL t2 " +
                                "WHERE t0.COD_EMPRESA = ?  " +
                                "AND t0.ID_CUADRATURA = ? " +
                                "AND t1.COD_MEDIO = t0.COD_MEDIO " +
                                "AND t2.id_cuadratura = t0.id_cuadratura");

            */

            /*stm = cnx.prepareCall("SELECT t0.ID_CUADRATURA, t0.COD_MEDIO, t1.COD_MEDIO, t0.COD_EMPRESA, t0.MONTO_TOTAL, t0.FECHA_HORA, t1.NOMBRE, t2.num_cuadr as transacciones\n" +
             "  FROM BPI_CUA_CUADRATURA_TBL t0, BPI_LGC_LOGCUADRATURA_TBL t2, BPI_DLC_DETALLELOGCUADR_TBL t3, BPI_MPG_MEDIOPAGO_TBL t1\n" +
             "  WHERE t0.ID_CUADRATURA = ?\n" +
             "  AND t0.COD_EMPRESA = ?\n" +
             "  AND t2.ID_CUADRATURA = t0.ID_CUADRATURA\n" +
             "  AND t3.ID_CUADRATURA = t0.ID_CUADRATURA\n" +
             "  AND t1.COD_MEDIO = t0.COD_MEDIO");*/

            String sql =
                "SELECT t0.COD_MEDIO, t1.COD_MEDIO, t0.COD_EMPRESA, sum(t0.MONTO_TOTAL) as MONTO_TOTAL, t0.FECHA_HORA, t1.NOMBRE, sum(t2.NUM_CUADR) as transacciones\n" +
                "  FROM BPI_CUA_CUADRATURA_TBL t0, BPI_LGC_LOGCUADRATURA_TBL t2, BPI_DLC_DETALLELOGCUADR_TBL t3, BPI_MPG_MEDIOPAGO_TBL t1\n" +
                "  WHERE t0.COD_EMPRESA = ?\n" + "  AND t0.ID_CUADRATURA = ?\n" +
                "  AND t2.ID_CUADRATURA = t0.ID_CUADRATURA\n" + "  AND t3.ID_CUADRATURA = t0.ID_CUADRATURA\n" +
                "  AND t1.COD_MEDIO = t0.COD_MEDIO\n" +
                "  GROUP BY t0.COD_MEDIO, t1.COD_MEDIO, t0.COD_EMPRESA, t0.FECHA_HORA, t1.NOMBRE\n " +
                "  ORDER BY t0.COD_MEDIO";

            stm = cnx.prepareCall(sql);

            stm.setInt(1, id);
            stm.setInt(2, cuadratura);

            rst = stm.executeQuery();
            while (rst.next()) {
                RecaudacionMedioByEmpresa dto = new RecaudacionMedioByEmpresa();
                dto.setCod_empresa(rst.getInt("COD_EMPRESA"));
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setFecha(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                //dto.setId_cuadratura(rst.getInt("ID_CUADRATURA"));
                dto.setMonto_total(rst.getLong("MONTO_TOTAL"));
                dto.setNombre_medio(rst.getString("NOMBRE"));
                dto.setNum_transacciones(rst.getInt("TRANSACCIONES"));
                dtos.add(dto);
            }

            // Consulta Todos los medios de pago
            // y los que no estan los deja con ceros
            // para el detalle de presentacion
            for (Iterator it = allmed.iterator(); it.hasNext();) {
                MedioPago medio = (MedioPago) it.next();
                boolean addmedio = true;
                for (Iterator itreview = dtos.iterator(); itreview.hasNext();) {
                    RecaudacionMedioByEmpresa recau = (RecaudacionMedioByEmpresa) itreview.next();
                    if (recau.getCod_medio().intValue() == medio.getCodMedio().intValue()) {
                        addmedio = false;
                        break;
                    }
                }

                if (addmedio) {
                    RecaudacionMedioByEmpresa dtorec = new RecaudacionMedioByEmpresa();
                    dtorec.setCod_empresa(id);
                    dtorec.setCod_medio(medio.getCodMedio());
                    dtorec.setFecha(new Date(System.currentTimeMillis()));
                    dtorec.setId_cuadratura(new Integer(0));
                    dtorec.setMonto_total(new Long(0));
                    dtorec.setNombre_medio(medio.getNombre());
                    dtorec.setNum_transacciones(new Integer(0));
                    dtos.add(dtorec);
                }
            }
        } catch (Exception e) {
            logger.error("findLastRecaudacionMedioByEmpresaRut() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionMedioByEmpresaRut() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastRecaudacionMedioByEmpresaRut() - termina");
        return dtos;
    }

    /**
     * Retorna lista de resumen de recaudaci�n del medio indicado
     * @param empresa
     * @param cuadratura
     * @param cod_medio
     * @return
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainIdCudraturaByMedio(Integer empresa,
                                                                                                 Integer cuadratura,
                                                                                                 Integer cod_medio) {
        logger.info("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - inicia");
        List<RecaudacionMedioByEmpresa> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;

        try {
            cnx = getConnection();

            String sql =
                "SELECT  t.cod_banco2medio, t.cod_medio, de.cod_empresa, sum(t.monto_total) as monto_total, c.fecha_hora,   \n" +
                "        me.descripcion as  nombre, count(t.id_transaccion) as transacciones, c.id_cuadratura  \n" +
                "FROM    bicevida.bpi_tra_transacciones_tbl t,  \n" +
                "        bicevida.bpi_dte_dettransemp_tbl de,  \n" + "        bicevida.bpi_dcu_detcuad_tbl d,  \n" +
                "        bicevida.bpi_cua_cuadratura_tbl c, \n" + "        bicevida.bpi_bco_bancos_tbl me\n" +
                "WHERE   de.id_transaccion = t.id_transaccion  \n" + "  AND   de.cod_medio = t.cod_medio  \n" +
                "  AND   d.id_transaccion = t.id_transaccion  \n" + "  AND   c.id_cuadratura = d.id_cuadratura    \n" +
                "  AND   c.cod_empresa = de.cod_empresa  \n" + "  AND   c.cod_medio = t.cod_medio   \n" +
                "  AND   me.codigo = t.cod_banco2medio \n" + "  AND   de.cod_empresa = ? \n" +
                "  AND   t.cod_medio = ? \n" + "  AND   d.id_cuadratura = ? \n" +
                "  group by t.cod_banco2medio, t.cod_medio, de.cod_empresa, c.fecha_hora, me.descripcion, c.id_cuadratura  \n" +
                "  order by t.cod_medio";

            stm = cnx.prepareCall(sql);

            stm.setInt(1, empresa);
            stm.setInt(2, cod_medio);
            stm.setInt(3, cuadratura);
            rst = stm.executeQuery();

            while (rst.next()) {
                RecaudacionMedioByEmpresa dto = new RecaudacionMedioByEmpresa();
                dto.setCod_empresa(rst.getInt("cod_empresa"));
                dto.setCod_medio(rst.getInt("cod_banco2medio"));
                dto.setFecha(FechaUtil.toUtilDate(rst.getDate("fecha_hora")));
                dto.setMonto_total(rst.getLong("monto_total"));
                dto.setNombre_medio(rst.getString("nombre"));
                dto.setNum_transacciones(rst.getInt("transacciones"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                logger.error("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - catch (error)", e);
            }
        }
        logger.info("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - termina");
        return dtos;
    }

    /**
     * Otra consulta
     * @param cod_empresa
     * @param cod_medio
     * @param cuadratura
     * @return
     * TODO: Testear
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyCuad(Integer cod_empresa,
                                                                                       Integer cod_medio,
                                                                                       Long cuadratura) {
        logger.info("findLastRecaudacionByEmpresaMediobyCuad() - inicia");
        List<RecaudacionByEmpMedioInPeriod> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            /*stm = cnx.prepareCall("SELECT COD_MEDIO, COD_EMPRESA, ID_TRANSACCION, NUM_PRODUCTO, CUOTA, MONTO_TOTAL,  " +
                                    "NOMBRE_PERSONA, NOMBRE, FECHA_HORA, NUM_TRANSACCION_MEDIO, COD_PRODUCTO, RUT_PERSONA, FECHA_PAGO, TURNO, ID_CUADRATURA " +
                                    "FROM BPI_DTC_DETTRANCUAD_VW  " +
                                    "WHERE COD_EMPRESA = ?  " +
                                    "AND COD_MEDIO = ?  " +
                                    "AND ID_CUADRATURA = ? " +
                                    "order by ID_TRANSACCION desc");*/
            stm =
                cnx.prepareCall("SELECT t0.COD_MEDIO, t0.COD_EMPRESA, t0.ID_TRANSACCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL, \n" +
                                "       t0.NOMBRE_PERSONA, t0.NOMBRE, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO, t0.COD_PRODUCTO, t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO, t0.ID_CUADRATURA\n" +
                                "  FROM BPI_DTC_DETTRANCUAD_VW t0\n" + "  WHERE t0.COD_EMPRESA = ?\n" +
                                "  AND t0.COD_MEDIO = ?\n" + "  AND t0.ID_CUADRATURA = ?\n" +
                                "  order by t0.ID_TRANSACCION desc");

            stm.setInt(1, cod_empresa);
            stm.setInt(2, cod_medio);
            stm.setLong(3, cuadratura);
            rst = stm.executeQuery();
            while (rst.next()) {
                RecaudacionByEmpMedioInPeriod dto = new RecaudacionByEmpMedioInPeriod();
                dto.setCod_empresa(rst.getInt("COD_EMPRESA"));
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setCuota(rst.getInt("CUOTA"));
                dto.setFecha_hora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setFecha_Pago(FechaUtil.toUtilDate(rst.getDate("FECHA_PAGO")));
                dto.setIdCuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getInt("MONTO_TOTAL"));
                dto.setNombre_persona(rst.getString("NOMBRE_PERSONA"));
                dto.setNombre_producto(rst.getString("NOMBRE"));
                dto.setNumero_producto(rst.getLong("NUM_PRODUCTO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dto.setRut(rst.getString("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionByEmpresaMediobyCuad() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionByEmpresaMediobyCuad() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastRecaudacionByEmpresaMediobyCuad() - termina");
        return dtos;

    }

    /**
     * Otra consulta
     * @param cod_empresa
     * @param cod_medio
     * @param cuadratura
     * @param cod_medio2
     * @return
     * TODO: Testear
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyCuadByMedio2(Integer cod_empresa,
                                                                                               Integer cod_medio,
                                                                                               Long cuadratura,
                                                                                               Integer cod_medio2) {
        logger.info("findLastRecaudacionByEmpresaMediobyCuadByMedio2() - inicia");
        List<RecaudacionByEmpMedioInPeriod> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();

            String sql =
                "SELECT  t0.COD_MEDIO, t0.COD_EMPRESA, t0.ID_TRANSACCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL,\n" +
                "        t0.NOMBRE_PERSONA, t0.NOMBRE, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO, t0.COD_PRODUCTO, \n" +
                "        t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO, t0.ID_CUADRATURA, t0.COD_MEDIO2\n" +
                "FROM    BPI_DTC_DETTRANCUAD_VW t0\n" + "WHERE   t0.COD_EMPRESA = ?\n" + "AND     t0.COD_MEDIO = ?\n" +
                "AND     t0.COD_MEDIO2 = ?\n" + "AND     t0.ID_CUADRATURA = ?\n" + "ORDER BY t0.ID_TRANSACCION DESC";

            stm = cnx.prepareCall(sql);

            stm.setInt(1, cod_empresa);
            stm.setInt(2, cod_medio);
            stm.setInt(3, cod_medio2);
            stm.setLong(4, cuadratura);
            rst = stm.executeQuery();
            while (rst.next()) {
                RecaudacionByEmpMedioInPeriod dto = new RecaudacionByEmpMedioInPeriod();
                dto.setCod_empresa(rst.getInt("COD_EMPRESA"));
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setCuota(rst.getInt("CUOTA"));
                dto.setFecha_hora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setFecha_Pago(FechaUtil.toUtilDate(rst.getDate("FECHA_PAGO")));
                dto.setIdCuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getInt("MONTO_TOTAL"));
                dto.setNombre_persona(rst.getString("NOMBRE_PERSONA"));
                dto.setNombre_producto(rst.getString("NOMBRE"));
                dto.setNumero_producto(rst.getLong("NUM_PRODUCTO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dto.setRut(rst.getString("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionByEmpresaMediobyCuadByMedio2() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionByEmpresaMediobyCuadByMedio2() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                logger.error("findLastRecaudacionByEmpresaMediobyCuadByMedio2() - catch (error)", e);
            }
        }
        logger.info("findLastRecaudacionByEmpresaMediobyCuadByMedio2() - termina");
        return dtos;

    }

    /**
     * Otra consulta
     * @param rut
     * @return
     */
    public List<TransaccionByEmpresa> findTransaccionesByEmpresabyRut(String rut) {
        logger.info("findTransaccionesByEmpresabyRut() - inicia");
        List<TransaccionByEmpresa> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT emp.nombre, d.cod_empresa, d.cod_empresa as id_transaccion, COUNT (*) as num_transaccion_medio, SUM (d.monto_total) as monto_total  " +
                                " FROM bicevida.bpi_dte_dettransemp_tbl d, bicevida.bpi_tra_transacciones_tbl t , bpi_emp_empresas_tbl emp " +
                                " WHERE t.rut_persona = ? " + "   AND emp.cod_empresa = d.cod_empresa " +
                                "   AND d.id_transaccion = t.id_transaccion and t.cod_estado in (3,6,7) " +
                                " GROUP BY emp.nombre, d.cod_empresa");
            stm.setString(1, rut);
            rst = stm.executeQuery();
            while (rst.next()) {
                TransaccionByEmpresa dto = new TransaccionByEmpresa();
                dto.setCod_empresa(rst.getInt("cod_empresa"));
                dto.setId_transaccion(rst.getInt("id_transaccion"));
                dto.setNum_transacciones(rst.getInt("num_transaccion_medio"));
                dto.setMonto_total(rst.getLong("monto_total"));
                //A�ade la empresa asociada
                Empresas emp = new Empresas();
                emp.setCodEmpresa(rst.getDouble("cod_empresa"));
                emp.setNombre(rst.getString("nombre"));
                dto.setEmpresa(emp);
                dtos.add(dto);
            }
        } catch (Exception e) {
            logger.error("findTransaccionesByEmpresabyRut() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesByEmpresabyRut() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionesByEmpresabyRut() - termina");
        return dtos;
    }

    /**
     * Otra consultilla
     * @param cuadratura
     * @return
     * TODO: Testear
     */
    public List<BpiRtcRestrancuadrVw> findLastRecaudacionByEmpresaIdCuad(Long cuadratura, Long empresa) {
        logger.info("findLastRecaudacionByEmpresaIdCuad() - inicia");
        List<BpiRtcRestrancuadrVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        String filtro = "";

        if (empresa != 0) {
            filtro = "  AND a.cod_empresa = ?\n";
        }

        try {
            cnx = getConnection();
            //stm = cnx.prepareCall("SELECT * FROM BPI_RTC_RESTRANCUADR_VW WHERE ID_CUADRATURA = ?");
            stm =
                cnx.prepareCall("SELECT a.cod_empresa, a.nombre_empresa, a.fecha_hora, \n" +
                                "       sum(a.transacciones) as transacciones, sum(a.monto_total) as monto_total, sum(a.transacciones_prob) as transacciones_prob,\n" +
                                "       sum(a.monto_total_prob) as monto_total_prob\n" +
                                "  FROM bpi_rtc_restrancuadr_vw a\n" + "  WHERE a.id_cuadratura = ?\n" + filtro +
                                "  group by a.cod_empresa, a.nombre_empresa, a.fecha_hora");
            stm.setLong(1, cuadratura);
            if (empresa.intValue() != 0) {
                stm.setLong(2, empresa);
            }

            rst = stm.executeQuery();
            while (rst.next()) {
                BpiRtcRestrancuadrVw dto = new BpiRtcRestrancuadrVw();
                dto.setCodEmpresa(rst.getDouble("COD_EMPRESA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                //dto.setIdCuadratura(rst.getDouble("ID_CUADRATURA"));
                //dto.setLogCuadr(rst.getString("LOG_CUADR"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setMontoTotalProb(rst.getDouble("MONTO_TOTAL_PROB"));
                dto.setNombreEmpresa(rst.getString("NOMBRE_EMPRESA"));
                dto.setTransacciones(rst.getDouble("TRANSACCIONES"));
                dto.setTransaccionesProb(rst.getDouble("TRANSACCIONES_PROB"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionByEmpresaIdCuad() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionByEmpresaIdCuad() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastRecaudacionByEmpresaIdCuad() - termina");
        return dtos;
    }

    /**
     * Ultima consulta
     * @param cod_empresa
     * @param cod_medio
     * @param fecha
     * @return
     * TODO: Testear
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMedio(Integer cod_empresa, Integer cod_medio,
                                                                                 Date fecha) {
        logger.info("findLastRecaudacionByEmpresaMedio() - inicia");
        List<RecaudacionByEmpMedioInPeriod> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT COD_MEDIO, COD_EMPRESA, ID_TRANSACCION, NUM_PRODUCTO, CUOTA, MONTO_TOTAL,  " +
                                "NOMBRE_PERSONA, NOMBRE, FECHA_HORA, NUM_TRANSACCION_MEDIO, COD_PRODUCTO, RUT_PERSONA, FECHA_PAGO " +
                                "FROM BPI_DTC_DETTRANCUAD_VW  " + "WHERE ((((COD_EMPRESA = ?)  " +
                                "AND (COD_MEDIO = ?))  " + "AND (FECHA_HORA >= ?))  " + "order by ID_TRANSACCION desc");
            stm.setInt(1, cod_empresa);
            stm.setInt(2, cod_medio);
            stm.setTimestamp(3, new java.sql.Timestamp(fecha.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                RecaudacionByEmpMedioInPeriod dto = new RecaudacionByEmpMedioInPeriod();
                dto.setCod_empresa(rst.getInt("COD_EMPRESA"));
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setCuota(rst.getInt("CUOTA"));
                dto.setFecha_hora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setFecha_Pago(FechaUtil.toUtilDate(rst.getDate("FECHA_PAGO")));
                dto.setIdCuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getInt("MONTO_TOTAL"));
                dto.setNombre_persona(rst.getString("NOMBRE_PERSONA"));
                dto.setNombre_producto(rst.getString("NOMBRE"));
                dto.setNumero_producto(rst.getLong("NUM_PRODUCTO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dto.setRut(rst.getString("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastRecaudacionByEmpresaMedio() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionByEmpresaMedio() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastRecaudacionByEmpresaMedio() - termina");
        return dtos;

    }

    /**
     * Consulta
     * @param fechaini
     * @param fechafin
     * @return
     * TODO: Testear
     */
    public List<BpiVreRechazosVw> findRechazosInPeriod(Date fechaini, Date fechafin) {
        logger.info("findRechazosInPeriod() - inicia");
        List<BpiVreRechazosVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_VRE_RECHAZOS_VW WHERE FECHA_CUADRATURA BETWEEN ? AND ? ");
            stm.setTimestamp(1, new java.sql.Timestamp(fechaini.getTime()));
            stm.setTimestamp(2, new java.sql.Timestamp(fechafin.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiVreRechazosVw dto = new BpiVreRechazosVw();
                dto.setCodEstado(rst.getDouble("COD_ESTADO"));
                dto.setCodMedio(rst.getDouble("COD_MEDIO"));
                dto.setEmpresa(rst.getInt("EMPRESA"));
                dto.setFechaCuadratura(FechaUtil.toUtilDate(rst.getDate("FECHA_CUADRATURA")));
                dto.setFechafin(FechaUtil.toUtilDate(rst.getDate("FECHAFIN")));
                dto.setFechaTransaccion(FechaUtil.toUtilDate(rst.getDate("FECHA_TRANSACCION")));
                dto.setFolio(rst.getLong("FOLIO"));
                dto.setIdTransaccion(rst.getDouble("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setNumTransaccionCaja(rst.getDouble("NUM_TRANSACCION_CAJA"));
                dto.setNumTransaccionMedio(rst.getDouble("NUM_TRANSACCION_MEDIO"));
                dto.setObservaciones(rst.getString("OBSERVACIONES"));
                dto.setRutPersona(rst.getDouble("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findRechazosInPeriod() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findRechazosInPeriod() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findRechazosInPeriod() - termina");
        return dtos;
    }

    /**
     * Consulta
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param empresa
     * @return
     * TODO: Testear
     */
    public List<BpiDnaDetnavegTbl> findComprobanteByRutInPeriod(Integer rut, Date fechaini, Date fechafin,
                                                                Integer empresa) {
        logger.info("findComprobanteByRutInPeriod() - inicia");
        List<BpiDnaDetnavegTbl> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            String sql =
                "SELECT  a.RUT_PERSONA AS RUT_PERSONA, a.FECHA_HORA AS FECHA_HORA, a.MONTO_TRANSACCION AS MONTO_TRANSACCION, \n" +
                "        a.ID_NAVEGACION AS ID_NAVEGACION, a.ID_TRANSACCION AS ID_TRANSACCION, a.COD_MEDIO AS COD_MEDIO, \n" +
                "        a.EMPRESA AS EMPRESA,\n" +
                "        b.COD_PAGINA AS COD_PAGINA, b.DETALLE_PAGINA.GETCLOBVAL() AS DETALLE_PAGINA, b.ENTRADA AS ENTRADA, \n" +
                "        b.ID_CANAL AS ID_CANAL\n" + "  FROM  BPI_NAV_NAVEGACION_TBL a, BPI_DNA_DETNAVEG_TBL b\n" +
                "  WHERE a.rut_persona = ?\n" + "  AND   a.id_navegacion = b.id_navegacion\n" +
                "  AND   a.fecha_hora BETWEEN ? AND ?\n" + "  AND   a.empresa = ?\n" + "  AND   b.cod_pagina = 5\n" +
                "  ORDER BY a.id_transaccion desc";

            stm = cnx.prepareCall(sql);
            stm.setInt(1, rut);
            stm.setTimestamp(2, new java.sql.Timestamp(fechaini.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechafin.getTime()));
            stm.setInt(4, empresa); //Vida o Hipotecaria
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiDnaDetnavegTbl dto = new BpiDnaDetnavegTbl();
                BpiNavNavegacionTbl nav = new BpiNavNavegacionTbl();

                nav.setRutPersona(rst.getDouble("RUT_PERSONA"));
                nav.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                nav.setMontoTransaccion(rst.getDouble("MONTO_TRANSACCION"));
                nav.setIdNavegacion(rst.getDouble("ID_NAVEGACION"));
                nav.setIdTransaccion(rst.getDouble("ID_TRANSACCION"));
                nav.setCodMedio(rst.getDouble("COD_MEDIO"));
                nav.setEmpresa(rst.getInt("EMPRESA"));

                dto.setBpiNavNavegacionTbl(nav);
                dto.setIdNavegacion(rst.getInt("ID_NAVEGACION"));
                dto.setCod_pagina(rst.getInt("COD_PAGINA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setMontoTransaccion(rst.getDouble("MONTO_TRANSACCION"));

                java.sql.Clob clb = rst.getClob("DETALLE_PAGINA");
                String xml = StringUtil.clobToString(clb);
                dto.setDetallePagina((XMLDocument) XmlUtil.parse(xml));


                dto.setEntrada(rst.getDouble("ENTRADA"));
                dto.setId_canal(rst.getInt("ID_CANAL"));


                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findComprobanteByRutInPeriod() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findComprobanteByRutInPeriod() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findComprobanteByRutInPeriod() - termina");
        return dtos;
    }

    /**
     * Consulta
     * @param rut
     * @param empresa
     * @return
     */
    public List<BpiDnaDetnavegTbl> findLastNavegacionByRut(Integer rut, Integer empresa) {
        logger.info("findLastNavegacionByRut() - inicia");
        List<BpiDnaDetnavegTbl> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT d.ID_NAVEGACION, d.COD_PAGINA, d.FECHA_HORA, " +
                                "  d.MONTO_TRANSACCION, d.ENTRADA, p.descripcion, d.DETALLE_PAGINA.getClobVal() " +
                                "  FROM bpi_dna_detnaveg_tbl d, bpi_pag_paginas_tbl p " + " WHERE d.id_navegacion = " +
                                "          (SELECT MAX (id_navegacion) " + "             FROM bpi_nav_navegacion_tbl " +
                                "            WHERE (rut_persona = ?) AND empresa = ? " +
                                "              AND (fecha_hora >= ?))  " + "AND d.cod_pagina=p.cod_pagina");

            Integer tiemponavegacion =
                new Integer("0" + ResourceBundleUtil.getProperty("pago.publico.internet.navegacion.actual.minutes"));
            Calendar cal = Calendar.getInstance();
            if (tiemponavegacion.intValue() > 0)
                cal.add(Calendar.MINUTE, -tiemponavegacion.intValue());
            if (tiemponavegacion.intValue() == 0)
                cal.add(Calendar.MINUTE, -20);

            stm.setInt(1, rut);
            stm.setInt(2, empresa);
            stm.setTimestamp(3, new java.sql.Timestamp(cal.getTime().getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiDnaDetnavegTbl dto = new BpiDnaDetnavegTbl();
                dto.setIdNavegacion(rst.getInt("ID_NAVEGACION"));
                dto.setCod_pagina(rst.getInt("COD_PAGINA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getTimestamp("FECHA_HORA")));
                dto.setMontoTransaccion(rst.getDouble("MONTO_TRANSACCION"));
                dto.setEntrada(rst.getDouble("ENTRADA"));
                dto.setPagina(rst.getString("descripcion"));
                java.sql.Clob clb = rst.getClob(7);
                String xml = StringUtil.clobToString(clb);
                dto.setDetallePagina((XMLDocument) XmlUtil.parse(xml));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastNavegacionByRut() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastNavegacionByRut() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastNavegacionByRut() - termina");
        return dtos;
    }

    /**
     * Consulta
     * @param idnavegacion
     * @return
     */
    public List<BpiDnaDetnavegTbl> findDetalleNavegacion(Integer idnavegacion) {
        logger.info("findDetalleNavegacion() - inicia");
        List<BpiDnaDetnavegTbl> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT a.ID_NAVEGACION, a.COD_PAGINA, a.FECHA_HORA,  " +
                                "a.MONTO_TRANSACCION, a.ENTRADA, b.DESCRIPCION, a.DETALLE_PAGINA.getClobVal() " +
                                "FROM BPI_DNA_DETNAVEG_TBL a, BPI_PAG_PAGINAS_TBL b " + "WHERE a.ID_NAVEGACION = ? " +
                                "  AND b.COD_PAGINA = a.COD_PAGINA ");
            stm.setInt(1, idnavegacion);
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiDnaDetnavegTbl dto = new BpiDnaDetnavegTbl();
                dto.setIdNavegacion(rst.getInt("ID_NAVEGACION"));
                dto.setCod_pagina(rst.getInt("COD_PAGINA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getTimestamp("FECHA_HORA")));
                dto.setMontoTransaccion(rst.getDouble("MONTO_TRANSACCION"));
                dto.setEntrada(rst.getDouble("ENTRADA"));
                dto.setPagina(rst.getString("DESCRIPCION"));
                java.sql.Clob clb = rst.getClob(7);
                String xml = StringUtil.clobToString(clb);
                dto.setDetallePagina((XMLDocument) XmlUtil.parse(xml));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findDetalleNavegacion() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findDetalleNavegacion() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findDetalleNavegacion() - termina");
        return dtos;
    }

    /**
     * Consulta
     * @param rut
     * @param empresa
     * @return
     * TODO: Testear
     */
    public List<BpiCarCartolasTbl> findLastCartolaByRut(Integer rut, Integer empresa) {
        logger.info("findLastCartolaByRut() - inicia");
        List<BpiCarCartolasTbl> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT ID_CARTOLA, RUT_PERSONA, FECHA_HORA, PERIODO, EMPRESA, DETALLE_PAGINA.getClobVal() " +
                                "  FROM bpi_car_cartolas_tbl c " + " WHERE c.id_cartola = " +
                                "          (SELECT MAX (id_cartola)  FROM bpi_car_cartolas_tbl   WHERE (rut_persona = ?)   AND (fecha_hora >= ?))  " +
                                "           AND c.empresa = ?");
            stm.setInt(1, rut);
            stm.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            stm.setInt(3, empresa);
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiCarCartolasTbl dto = new BpiCarCartolasTbl();
                dto.setIdCartola(rst.getLong("ID_CARTOLA"));
                dto.setRutPersona(rst.getLong("RUT_PERSONA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setPeriodo(rst.getString("PERIODO"));
                dto.setEmpresa(rst.getInt("EMPRESA"));
                java.sql.Clob clb = rst.getClob(6);
                String xml = StringUtil.clobToString(clb);
                dto.setDetallePagina((XMLDocument) XmlUtil.parse(xml));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findLastCartolaByRut() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastCartolaByRut() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findLastCartolaByRut() - termina");
        return dtos;
    }

    /**
     * Consulta
     * @param rut
     * @param fechadesde
     * @param fechahasta
     * @param empresa
     * @return
     */
    public List<Navegacion> getCountFindNavegacionByRutInPeriod(Integer rut, Date fechadesde, Date fechahasta,
                                                                Integer empresa) {
        logger.info("getCountFindNavegacionByRutInPeriod() - inicia");
        List<Navegacion> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT COUNT (*) AS id_navegacion, 0 as cod_medio " +
                                "  FROM bpi_nav_navegacion_tbl t1 " + " WHERE t1.rut_persona = ? " +
                                "   AND t1.fecha_hora >= ? " + "   AND t1.fecha_hora <= ? " + "   AND t1.empresa = ?");
            stm.setInt(1, rut);
            stm.setTimestamp(2, new java.sql.Timestamp(fechadesde.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechahasta.getTime()));
            stm.setInt(4, empresa);
            rst = stm.executeQuery();
            while (rst.next()) {
                Navegacion dto = new Navegacion();
                dto.setCod_medio(rst.getInt("cod_medio"));
                dto.setIdNavegacion(rst.getLong("id_navegacion"));
                dtos.add(dto);
            }

        } catch (SQLException e) {
            logger.error("getCountFindNavegacionByRutInPeriod() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("getCountFindNavegacionByRutInPeriod() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException e) {
            }
        }
        logger.info("getCountFindNavegacionByRutInPeriod() - termina");
        return dtos;
    }

    /**
     * Consulta
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param empresa
     * @return
     * TODO: Testear
     */
    public List<BpiCarCartolasTbl> getCountFindNavCartolasByRutInPeriod(Integer rut, Date fechaini, Date fechafin,
                                                                        Integer empresa) {
        logger.info("getCountFindNavCartolasByRutInPeriod() - inicia");
        List<BpiCarCartolasTbl> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT   COUNT(*) AS id_cartola " + "                    FROM bpi_car_cartolas_tbl " +
                                "                   WHERE rut_persona = ? " +
                                "                               AND  (fecha_hora >= ? AND fecha_hora <= ?) " +
                                "	          AND  (empresa = ?)");
            stm.setInt(1, rut);
            stm.setTimestamp(2, new java.sql.Timestamp(fechaini.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechafin.getTime()));
            stm.setInt(4, empresa);
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiCarCartolasTbl dto = new BpiCarCartolasTbl();
                dto.setIdCartola(rst.getLong("id_cartola"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("getCountFindNavCartolasByRutInPeriod() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("getCountFindNavCartolasByRutInPeriod() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("getCountFindNavCartolasByRutInPeriod() - termina");
        return dtos;
    }

    /**
     * Consulta
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param rowdesde
     * @param rowhasta
     * @param empresa
     * @return
     */
    public List<Navegacion> findNavegacionByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa) {
        logger.info("findNavegacionByRutInPeriod() - inicia");
        List<Navegacion> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("select a.RUT_PERSONA, a.FECHA_HORA, a.MONTO_TRANSACCION, a.ID_NAVEGACION, a.ID_TRANSACCION, a.COD_MEDIO, a.EMPRESA, c.COD_ESTADO, d.NOMBRE,  max(b.COD_PAGINA) AS ULTIMAPAGINA     " +
                                "from BPI_NAV_NAVEGACION_TBL a LEFT JOIN BPI_DNA_DETNAVEG_TBL b ON b.ID_NAVEGACION = a.ID_NAVEGACION  LEFT JOIN BPI_TRA_TRANSACCIONES_TBL c ON c.ID_TRANSACCION = a.ID_TRANSACCION LEFT JOIN BPI_MPG_MEDIOPAGO_TBL d ON d.COD_MEDIO = a.COD_MEDIO      " +
                                "Where a.RUT_PERSONA = ? " + "  and a.EMPRESA = ? " +
                                "  and a.FECHA_HORA BETWEEN ? AND ? " +
                                "group by a.RUT_PERSONA, a.FECHA_HORA, a.MONTO_TRANSACCION, a.ID_NAVEGACION, a.ID_TRANSACCION, a.COD_MEDIO, a.EMPRESA, c.COD_ESTADO, d.NOMBRE     " +
                                "ORDER BY a.FECHA_HORA desc ");
            stm.setInt(1, rut);
            stm.setInt(2, empresa);
            stm.setTimestamp(3, new java.sql.Timestamp(fechaini.getTime()));
            stm.setTimestamp(4, new java.sql.Timestamp(fechafin.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                Navegacion dto = new Navegacion();
                dto.setRutPersona(rst.getInt("RUT_PERSONA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getTimestamp("FECHA_HORA")));
                dto.setMontoTransaccion(rst.getInt("MONTO_TRANSACCION"));
                dto.setIdNavegacion(rst.getLong("ID_NAVEGACION"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setEmpresa(rst.getInt("EMPRESA"));
                dto.setCodEstado(rst.getInt("COD_ESTADO"));
                dto.setUltimaPagina(rst.getInt("ULTIMAPAGINA"));
                dto.setEmpresa(empresa);

                //SETEA MEDIO
                BpiMpgMediopagoTbl medio = new BpiMpgMediopagoTbl();
                medio.setCodMedio(dto.getCod_medio());
                medio.setNombre(rst.getString("NOMBRE"));
                dto.setMedio_pago(medio);

                //AGREGA
                dtos.add(dto);
            }

        } catch (SQLException e) {
            logger.error("findNavegacionByRutInPeriod() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findNavegacionByRutInPeriod() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException e) {
            }
        }
        logger.info("findNavegacionByRutInPeriod() - termina");
        return dtos;
    }

    /**
     * Consulta
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param rowdesde
     * @param rowhasta
     * @param empresa
     * @return
     * TODO: Testear
     */
    public List<BpiCarCartolasTbl> findNavCartolasByRutInPeriod(Integer rut, Date fechaini, Date fechafin,
                                                                Integer rowdesde, Integer rowhasta, Integer empresa) {
        logger.info("findNavCartolasByRutInPeriod() - inicia");
        List<BpiCarCartolasTbl> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT t.id_cartola, t.rut_persona, t.fecha_hora, t.periodo, t.empresa, t.detalle_pagina.getClobVal() " +
                                "  FROM bpi_car_cartolas_tbl t " + " WHERE t.rut_persona = ? " +
                                "   AND t.fecha_hora BETWEEN ? AND ? " + "   AND t.empresa = ? " +
                                "ORDER BY t.fecha_hora DESC");
            stm.setInt(1, rut);
            stm.setTimestamp(2, new java.sql.Timestamp(fechaini.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechafin.getTime()));
            stm.setInt(4, empresa);
            //stm.setInt(5, rowhasta);
            //stm.setInt(6, rowdesde);
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiCarCartolasTbl dto = new BpiCarCartolasTbl();
                dto.setIdCartola(rst.getLong("ID_CARTOLA"));
                dto.setRutPersona(rst.getLong("RUT_PERSONA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setPeriodo(rst.getString("PERIODO"));
                dto.setEmpresa(rst.getInt("EMPRESA"));
                java.sql.Clob clb = rst.getClob(6);
                if (clb != null) {
                    String xml = StringUtil.clobToString(clb);
                    dto.setDetallePagina((XMLDocument) XmlUtil.parse(xml));
                }
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findNavCartolasByRutInPeriod() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findNavCartolasByRutInPeriod() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findNavCartolasByRutInPeriod() - termina");
        return dtos;
    }

    /**
     * Consukta de datos
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param empresa
     * @return
     * TODO: Testear
     */
    public List<BpiVreRechazosVw> findRechazosByRutInPeriod(Integer rut, Date fechaini, Date fechafin,
                                                            Integer empresa) {
        logger.info("findRechazosByRutInPeriod() - inicia");
        List<BpiVreRechazosVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT * FROM BPI_VRE_RECHAZOS_VW " + " WHERE RUT_PERSONA = ? " +
                                "   AND FECHA_CUADRATURA BETWEEN ? AND ? " + "   AND EMPRESA = ?");
            stm.setInt(1, rut);
            stm.setTimestamp(2, new java.sql.Timestamp(fechaini.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechafin.getTime()));
            stm.setInt(4, empresa);
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiVreRechazosVw dto = new BpiVreRechazosVw();
                dto.setCodEstado(rst.getDouble("COD_ESTADO"));
                dto.setCodMedio(rst.getDouble("COD_MEDIO"));
                dto.setEmpresa(rst.getInt("EMPRESA"));
                dto.setFechaCuadratura(FechaUtil.toUtilDate(rst.getDate("FECHA_CUADRATURA")));
                dto.setFechafin(FechaUtil.toUtilDate(rst.getDate("FECHAFIN")));
                dto.setFechaTransaccion(FechaUtil.toUtilDate(rst.getDate("FECHA_TRANSACCION")));
                dto.setFolio(rst.getLong("FOLIO"));
                dto.setIdTransaccion(rst.getDouble("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setNumTransaccionCaja(rst.getDouble("NUM_TRANSACCION_CAJA"));
                dto.setNumTransaccionMedio(rst.getDouble("NUM_TRANSACCION_MEDIO"));
                dto.setObservaciones(rst.getString("OBSERVACIONES"));
                dto.setRutPersona(rst.getDouble("RUT_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findRechazosByRutInPeriod() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findRechazosByRutInPeriod() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findRechazosByRutInPeriod() - termina");
        return dtos;
    }

    /**
     * Consulta si existe la transaccion
     * @param idTransaccion
     * @return
     */
    public boolean isExisteTransaccionComprobante(Long idTransaccion, Double rutPersona) {
        logger.info("isExisteTransaccionComprobante() - inicia");
        boolean resp = Boolean.FALSE;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT  * FROM bpi_tra_transacciones_tbl " +
                                "     WHERE id_transaccion = ? and RUT_PERSONA = ? AND COD_ESTADO IN(3,7)");
            stm.setLong(1, idTransaccion);
            stm.setLong(2, rutPersona.longValue());
            rst = stm.executeQuery();
            if (rst.next()) {
                resp = Boolean.TRUE;
            }
        } catch (Exception e) {
            logger.error("isExisteTransaccionComprobante() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("isExisteTransaccionComprobante() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("isExisteTransaccionComprobante() - termina");
        return resp;
    }


    public BpiTraTransaccionesTbl findTransaccionCodigoEstadoByNumTransaccionForCuadratura(Long numtrx) {
        logger.info("findTransaccionCodigoEstadoByNumTransaccionForCuadratura() - inicia");
        BpiTraTransaccionesTbl dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_TRA_TRANSACCIONES_TBL  WHERE ID_TRANSACCION = ? ");
            stm.setLong(1, numtrx);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new BpiTraTransaccionesTbl();
                dto.setCodEstado(rst.getInt("COD_ESTADO"));
                dto.setCodMedioPago(rst.getInt("COD_MEDIO"));
                dto.setFechafin(FechaUtil.toUtilDate(rst.getDate("FECHAFIN")));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getDate("FECHAINICIO")));
                dto.setFechahorainicio(rst.getTimestamp("FECHAINICIO"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setObservaciones(rst.getString("OBSERVACIONES"));
                dto.setRutPersona(rst.getDouble("RUT_PERSONA"));
                dto.setMedioCodigoRespuesta(rst.getString("MEDIO_CODIGO_RESP"));
                dto.setMedioFechaPago(FechaUtil.toUtilDate(rst.getTimestamp("MEDIO_FECHA_PAGO")));
                dto.setMedioNumeroCuotas(rst.getInt("MEDIO_NUM_CUOTAS"));
                dto.setMedioNumeroTarjeta(rst.getString("MEDIO_NUM_TARJETA"));
                dto.setMedioTipoTarjeta(rst.getString("MEDIO_TIPO_TARJETA"));
                dto.setNumeroTransaccionBanco(rst.getString("NUM_TRANSACCION_MEDIO"));
                dto.setMedioOrdenCompra(rst.getString("MEDIO_NUM_ORDEN_COMP"));
                dto.setMedioCodigoAutorizacion(rst.getString("MEDIO_CODIGO_AUTORIZA"));
                dto.setCargo(rst.getDouble("CARGO"));
                dto.setDisponFondos(rst.getDate("DISPFONDOS"));
                dto.setTipoTransaccion(rst.getInt("TIPOTRX"));
                dto.setCodigoEmpresa(rst.getString("EMPRESA"));
                dto.setCodigoOrigenTransaccion(rst.getString("ORIGEN_TRANSACCION"));

                //BUSCAR EN LA NAVEGACION EL CODIGO DE EMPRESA
                dto.setIdEmpresa(new Long(0));
            }

            rst.close();
            stm.close();

            //BUSCA EL NUMERO EL NUMERO DE EMPRESA PARA PAGOS NORMALES
            if (dto != null) {
                stm = cnx.prepareCall("SELECT COD_EMPRESA FROM BPI_DTE_DETTRANSEMP_TBL WHERE ID_TRANSACCION = ?");
                stm.setLong(1, numtrx);
                rst = stm.executeQuery();
                if (rst.next()) {
                    dto.setIdEmpresa(rst.getLong("COD_EMPRESA"));
                }
                rst.close();
                stm.close();
            }


        } catch (Exception e) {
            logger.error("findTransaccionCodigoEstadoByNumTransaccionForCuadratura() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionCodigoEstadoByNumTransaccionForCuadratura() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findTransaccionCodigoEstadoByNumTransaccionForCuadratura() - termina");
        return dto;
    }

    public List<TransaccionMedioByEmpresaByMedio2> findTransaccionesRecauByMedio2ByFecha(Timestamp fechaini,
                                                                                         Timestamp fechafin,
                                                                                         Integer empresa) {
        logger.info("findTransaccionesRecauByMedio2ByFecha() - inicia");
        List<TransaccionMedioByEmpresaByMedio2> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();

            String sql =
                "SELECT  tt.COD_EMPRESA, tt.COD_MEDIO, tt.NOMBRE, tt.COD_MEDIO2, tt.DESCRIPCION, tt.RUT_PERSONA, \n" +
                "        tt.NOMBRE_PERSONA, tt.NOM_PRODUCTO, tt.NUM_PRODUCTO, tt.CUOTA, tt.ID_TRANSACCION,\n" +
                "        tt.ID_CUADRATURA, tt.FECHA_PAGO, tt.TURNO, tt.MONTO_TOTAL      \n" + "FROM\n" + "(\n" +
                "    SELECT  t0.ID_TRANSACCION, t0.ID_CUADRATURA, t0.COD_EMPRESA, t0.COD_MEDIO, t1.NOMBRE,\n" +
                "            t0.COD_MEDIO2, t2.DESCRIPCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL,\n" +
                "            t0.NOMBRE_PERSONA, t0.NOMBRE AS NOM_PRODUCTO, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO,\n" +
                "            t0.COD_PRODUCTO, t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO\n" +
                "    FROM    BPI_DTC_DETTRANCUAD_VW t0,\n" + "            BPI_MPG_MEDIOPAGO_TBL t1,\n" +
                "            BPI_BCO_BANCOS_TBL t2\n" + "    WHERE   t1.COD_MEDIO = t0.COD_MEDIO\n" +
                "    AND     t2.CODIGO = t0.COD_MEDIO2\n" + "    AND     t0.COD_MEDIO = 3\n" + "    UNION\n" +
                "    SELECT  t0.ID_TRANSACCION, t0.ID_CUADRATURA, t0.COD_EMPRESA, t0.COD_MEDIO, t1.NOMBRE,\n" +
                "            t0.COD_MEDIO2, t1.NOMBRE AS DESCRIPCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL,\n" +
                "            t0.NOMBRE_PERSONA, t0.NOMBRE AS NOM_PRODUCTO, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO,\n" +
                "            t0.COD_PRODUCTO, t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO\n" +
                "    FROM    BPI_DTC_DETTRANCUAD_VW t0,\n" + "            BPI_MPG_MEDIOPAGO_TBL t1\n" +
                "    WHERE   t1.COD_MEDIO = t0.COD_MEDIO\n" + "    AND     t0.COD_MEDIO2 IS NULL\n" +
                "    AND     t0.COD_MEDIO <> 3\n" + ") tt\n" + "WHERE     tt.COD_EMPRESA = ?\n" +
                "AND   tt.FECHA_HORA BETWEEN ? AND ?\n" +
                "ORDER BY  tt.COD_MEDIO ASC, tt.COD_MEDIO2 ASC, tt.ID_TRANSACCION DESC";

            stm = cnx.prepareCall(sql);
            stm.setInt(1, empresa);
            stm.setTimestamp(2, new java.sql.Timestamp(fechaini.getTime()));
            stm.setTimestamp(3, new java.sql.Timestamp(fechafin.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                TransaccionMedioByEmpresaByMedio2 dto = new TransaccionMedioByEmpresaByMedio2();
                dto.setCodEmpresa(rst.getLong("COD_EMPRESA"));
                dto.setCodMedio(rst.getLong("COD_MEDIO"));
                dto.setNommedio(rst.getString("NOMBRE"));
                dto.setCodMedio2(rst.getLong("COD_MEDIO2"));
                dto.setNommedio2(rst.getString("DESCRIPCION"));
                dto.setRut(rst.getString("RUT_PERSONA"));
                dto.setNombre(rst.getString("NOMBRE_PERSONA"));
                dto.setNomproducto(rst.getString("NOM_PRODUCTO"));
                dto.setProducto(rst.getLong("NUM_PRODUCTO"));
                dto.setIdtransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setIdcuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setFechacuadratura(FechaUtil.toUtilDate(rst.getDate("FECHA_PAGO")));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dto.setMonto_total(rst.getLong("MONTO_TOTAL"));
                dto.setNumeroPropuesta(rst.getInt("CUOTA")); //TODO: Se Agrega N� Propuesta.
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findTransaccionesRecauByMedio2ByFecha() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesRecauByMedio2ByFecha() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                logger.error("findTransaccionesRecauByMedio2ByFecha() - catch (error)", e);
            }
        }
        logger.info("findTransaccionesRecauByMedio2ByFecha() - termina");
        return dtos;
    }

    public List<TransaccionMedioByEmpresaByMedio2> findTransaccionesRecauByMedio2ByCuadratura(Integer idcuadratura,
                                                                                              Integer empresa) {
        logger.info("findTransaccionesRecauByMedio2ByFecha() - inicia");
        List<TransaccionMedioByEmpresaByMedio2> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();

            String sql =
                "SELECT  tt.COD_EMPRESA, tt.COD_MEDIO, tt.NOMBRE, tt.COD_MEDIO2, tt.DESCRIPCION, tt.RUT_PERSONA, \n" +
                "        tt.NOMBRE_PERSONA, tt.NOM_PRODUCTO, tt.NUM_PRODUCTO, tt.CUOTA, tt.ID_TRANSACCION,\n" +
                "        tt.ID_CUADRATURA, tt.FECHA_PAGO, tt.TURNO, tt.MONTO_TOTAL\n" + "FROM\n" + "(\n" +
                "    SELECT  t0.ID_TRANSACCION, t0.ID_CUADRATURA, t0.COD_EMPRESA, t0.COD_MEDIO, t1.NOMBRE,\n" +
                "            t0.COD_MEDIO2, t2.DESCRIPCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL,\n" +
                "            t0.NOMBRE_PERSONA, t0.NOMBRE AS NOM_PRODUCTO, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO,\n" +
                "            t0.COD_PRODUCTO, t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO\n" +
                "    FROM    BPI_DTC_DETTRANCUAD_VW t0,\n" + "            BPI_MPG_MEDIOPAGO_TBL t1,\n" +
                "            BPI_BCO_BANCOS_TBL t2\n" + "    WHERE   t1.COD_MEDIO = t0.COD_MEDIO\n" +
                "    AND     t2.CODIGO = t0.COD_MEDIO2\n" + "    AND     t0.COD_MEDIO = 3\n" + "    UNION\n" +
                "    SELECT  t0.ID_TRANSACCION, t0.ID_CUADRATURA, t0.COD_EMPRESA, t0.COD_MEDIO, t1.NOMBRE,\n" +
                "            t0.COD_MEDIO2, t1.NOMBRE AS DESCRIPCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL,\n" +
                "            t0.NOMBRE_PERSONA, t0.NOMBRE AS NOM_PRODUCTO, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO,\n" +
                "            t0.COD_PRODUCTO, t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO\n" +
                "    FROM    BPI_DTC_DETTRANCUAD_VW t0,\n" + "            BPI_MPG_MEDIOPAGO_TBL t1\n" +
                "    WHERE   t1.COD_MEDIO = t0.COD_MEDIO\n" + "    AND     t0.COD_MEDIO2 IS NULL\n" +
                "    AND     t0.COD_MEDIO <> 3\n" + ") tt\n" + "WHERE     tt.COD_EMPRESA = ?\n" +
                "AND   tt.ID_CUADRATURA = ?\n" +
                "ORDER BY  tt.COD_MEDIO ASC, tt.COD_MEDIO2 ASC, tt.ID_TRANSACCION DESC";

            stm = cnx.prepareCall(sql);
            stm.setInt(1, empresa);
            stm.setInt(2, idcuadratura);
            rst = stm.executeQuery();
            while (rst.next()) {
                TransaccionMedioByEmpresaByMedio2 dto = new TransaccionMedioByEmpresaByMedio2();
                dto.setCodEmpresa(rst.getLong("COD_EMPRESA"));
                dto.setCodMedio(rst.getLong("COD_MEDIO"));
                dto.setNommedio(rst.getString("NOMBRE"));
                dto.setCodMedio2(rst.getLong("COD_MEDIO2"));
                dto.setNommedio2(rst.getString("DESCRIPCION"));
                dto.setRut(rst.getString("RUT_PERSONA"));
                dto.setNombre(rst.getString("NOMBRE_PERSONA"));
                dto.setNomproducto(rst.getString("NOM_PRODUCTO"));
                dto.setProducto(rst.getLong("NUM_PRODUCTO"));
                dto.setIdtransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setIdcuadratura(rst.getLong("ID_CUADRATURA"));
                dto.setFechacuadratura(FechaUtil.toUtilDate(rst.getDate("FECHA_PAGO")));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dto.setMonto_total(rst.getLong("MONTO_TOTAL"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findTransaccionesRecauByMedio2ByFecha() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesRecauByMedio2ByFecha() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                logger.error("findTransaccionesRecauByMedio2ByFecha() - catch (error)", e);
            }
        }
        logger.info("findTransaccionesRecauByMedio2ByFecha() - termina");
        return dtos;
    }


    /**
     * Consulta la descripcion de un codigo
     * producto pero sin acentuaciones
     * @param idTransaccion
     * @return
     */
    public String getNombreProductoSinAcentos(Long codigoProducto) {
        String resp = "";
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("select * from BPI_PRO_PRODUCTOS_TBL where COD_PRODUCTO = " + codigoProducto);
            rst = stm.executeQuery();
            if (rst.next()) {
                resp = rst.getString("NOMBRE_SIN_ACENTO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(rst, stm, cnx);
        }
        return resp;
    }

    /**
     * Recupera la lista de transacciones que no han concluido su pago webpay
     * para ser regularizadas
     *
     * @param idEmpresa
     * @param rutCliente
     * @return
     */
    public List<BpiTraTransaccionesTbl> findTransaccionesForRegularizacionWebPay(Long idEmpresa, Integer rutCliente,
                                                                                 java.util.Date fechaDesde,
                                                                                 java.util.Date fechaHasta) {
        logger.info("findTransaccionesForRegularizacionWebPay() - inicia");
        List<BpiTraTransaccionesTbl> dtos = new ArrayList<BpiTraTransaccionesTbl>();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            if (rutCliente != null) {
                stm =
                    cnx.prepareCall("SELECT t.*   " + "         FROM  bicevida.bpi_tra_transacciones_tbl t   " +
                                    "         WHERE t.cod_estado in (2)   " + "           AND t.cod_medio in(4,11) " +
                                    "           and t.rut_persona = ? " + "           and t.empresa = ? " +
                                    "           and t.fechainicio between ? and ? \n" +
                                    "           AND t.num_transaccion_medio is not null " +
                                    "           and t.medio_num_tarjeta is not null " +
                                    "           and t.observaciones <> ? " + "  order by t.fechainicio desc");
                stm.setInt(1, rutCliente);
                stm.setString(2, idEmpresa.intValue() == 1 ? "BV" : "BH");
                stm.setTimestamp(3, new Timestamp(fechaDesde.getTime()));
                stm.setTimestamp(4, new Timestamp(fechaHasta.getTime()));
                stm.setString(5, "Pago realizado por regularizacion webpay.");
            } else {
                stm =
                    cnx.prepareCall("SELECT t.*   " + "         FROM  bicevida.bpi_tra_transacciones_tbl t   " +
                                    "         WHERE t.cod_estado in (2)   " + "           AND t.cod_medio in(4,11) " +
                                    "           and t.empresa = ?   " +
                                    "           and t.fechainicio between ? and ? \n" +
                                    "           AND t.num_transaccion_medio is not null " +
                                    "           and t.medio_num_tarjeta is not null " +
                                    "           and t.observaciones <> ? " + "  order by t.fechainicio desc");
                stm.setString(1, idEmpresa.intValue() == 1 ? "BV" : "BH");
                stm.setTimestamp(2, new Timestamp(fechaDesde.getTime()));
                stm.setTimestamp(3, new Timestamp(fechaHasta.getTime()));
                stm.setString(4, "Pago realizado por regularizacion webpay.");
            }

            rst = stm.executeQuery();
            while (rst.next()) {
                BpiTraTransaccionesTbl dto = new BpiTraTransaccionesTbl();
                dto.setCodEstado(rst.getInt("COD_ESTADO"));
                dto.setCodMedioPago(rst.getInt("COD_MEDIO"));
                dto.setFechafin(FechaUtil.toUtilDate(rst.getTimestamp("FECHAFIN")));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getTimestamp("FECHAINICIO")));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setObservaciones(rst.getString("OBSERVACIONES"));
                dto.setRutPersona(rst.getDouble("RUT_PERSONA"));
                dto.setMedioCodigoRespuesta(rst.getString("MEDIO_CODIGO_RESP"));
                dto.setMedioFechaPago(FechaUtil.toUtilDate(rst.getTimestamp("MEDIO_FECHA_PAGO")));
                dto.setMedioNumeroCuotas(rst.getInt("MEDIO_NUM_CUOTAS"));
                dto.setMedioNumeroTarjeta(rst.getString("MEDIO_NUM_TARJETA"));
                dto.setMedioTipoTarjeta(rst.getString("MEDIO_TIPO_TARJETA"));
                dto.setNumeroTransaccionBanco(rst.getString("NUM_TRANSACCION_MEDIO"));
                dto.setMedioOrdenCompra(rst.getString("MEDIO_NUM_ORDEN_COMP"));
                dto.setMedioCodigoAutorizacion(rst.getString("MEDIO_CODIGO_AUTORIZA"));
                dto.setCargo(rst.getDouble("CARGO"));
                dto.setDisponFondos(rst.getDate("DISPFONDOS"));
                dto.setTipoTransaccion(rst.getInt("TIPOTRX"));
                dto.setNumeroFolioCaja(rst.getLong("FOLIO"));
                dto.setCodigoEmpresa(rst.getString("EMPRESA"));
                dto.setCodigoOrigenTransaccion(rst.getString("ORIGEN_TRANSACCION"));
                java.sql.Date turnoSql = rst.getDate("TURNO");
                if (turnoSql != null)
                    dto.setTurno(new java.util.Date(turnoSql.getTime()));
                if (turnoSql == null)
                    dto.setTurno(null);
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findTransaccionesForRegularizacionWebPay() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.info("findTransaccionesForRegularizacionWebPay() - termina");
        return dtos;
    }


    /**
     * Recupera la lista de transacciones que no han concluido su pago webpay
     * para ser regularizadas
     *
     * @param idEmpresa
     * @param rutCliente
     * @return
     */
    public List<BpiTraTransaccionesTbl> findTransaccionesForRegularizacionWebPayPool(Long idEmpresa, String texto) {
        logger.info("findTransaccionesForRegularizacionWebPayPool() - inicia");
        List<BpiTraTransaccionesTbl> dtos = new ArrayList<BpiTraTransaccionesTbl>();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();

            stm =
                cnx.prepareCall("SELECT t.*   " + "         FROM  bicevida.bpi_tra_transacciones_tbl t   " +
                                "         WHERE t.cod_medio in(4,11) " + "           AND t.empresa = ?   " +
                                "           AND t.webpay = ? " + "           AND t.observaciones = ? " +
                                "           AND t.folio is null  " +
                                "           AND t.num_transaccion_medio is not null " +
                                "           and t.medio_num_tarjeta is not null " + "  order by t.fechainicio desc");
            stm.setString(1, idEmpresa.intValue() == 1 ? "BV" : "BH");
            stm.setString(2, "S");
            stm.setString(3, "Pago realizado por regularizacion webpay.");


            rst = stm.executeQuery();
            while (rst.next()) {
                BpiTraTransaccionesTbl dto = new BpiTraTransaccionesTbl();
                dto.setCodEstado(rst.getInt("COD_ESTADO"));
                dto.setCodMedioPago(rst.getInt("COD_MEDIO"));
                dto.setFechafin(FechaUtil.toUtilDate(rst.getTimestamp("FECHAFIN")));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getTimestamp("FECHAINICIO")));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setObservaciones(rst.getString("OBSERVACIONES"));
                dto.setRutPersona(rst.getDouble("RUT_PERSONA"));
                dto.setMedioCodigoRespuesta(rst.getString("MEDIO_CODIGO_RESP"));
                dto.setMedioFechaPago(FechaUtil.toUtilDate(rst.getTimestamp("MEDIO_FECHA_PAGO")));
                dto.setMedioNumeroCuotas(rst.getInt("MEDIO_NUM_CUOTAS"));
                dto.setMedioNumeroTarjeta(rst.getString("MEDIO_NUM_TARJETA"));
                dto.setMedioTipoTarjeta(rst.getString("MEDIO_TIPO_TARJETA"));
                dto.setNumeroTransaccionBanco(rst.getString("NUM_TRANSACCION_MEDIO"));
                dto.setMedioOrdenCompra(rst.getString("MEDIO_NUM_ORDEN_COMP"));
                dto.setMedioCodigoAutorizacion(rst.getString("MEDIO_CODIGO_AUTORIZA"));
                dto.setCargo(rst.getDouble("CARGO"));
                dto.setDisponFondos(rst.getDate("DISPFONDOS"));
                dto.setTipoTransaccion(rst.getInt("TIPOTRX"));
                dto.setNumeroFolioCaja(rst.getLong("FOLIO"));
                dto.setCodigoEmpresa(rst.getString("EMPRESA"));
                dto.setCodigoOrigenTransaccion(rst.getString("ORIGEN_TRANSACCION"));
                java.sql.Date turnoSql = rst.getDate("TURNO");
                if (turnoSql != null)
                    dto.setTurno(new java.util.Date(turnoSql.getTime()));
                if (turnoSql == null)
                    dto.setTurno(null);
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findTransaccionesForRegularizacionWebPayPool() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.info("findTransaccionesForRegularizacionWebPayPool() - termina");
        return dtos;
    }


    /**
     * Recupera la lista de transacciones que no han concluido su pago webpay
     * para ser regularizadas
     *
     * @param idEmpresa
     * @param rutCliente
     * @return
     */
    public List<BpiTraTransaccionesTbl> findTransaccionesYaRegularizadasWebPayPool(Long idEmpresa, Integer rutCliente) {
        logger.info("findTransaccionesForRegularizacionWebPayPool() - inicia");
        List<BpiTraTransaccionesTbl> dtos = new ArrayList<BpiTraTransaccionesTbl>();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();

            if (rutCliente == null) {
                stm =
                    cnx.prepareCall("SELECT t.*   " + "         FROM  bicevida.bpi_tra_transacciones_tbl t   " +
                                    "         WHERE t.cod_estado in (3,6) " + "           and t.cod_medio in(4,11) " +
                                    "           and t.empresa = ?   " + "           AND t.webpay = ? " +
                                    "           AND t.folio is not null  " +
                                    "           AND t.num_transaccion_medio is not null " +
                                    "           and t.medio_num_tarjeta is not null " + "  order by t.folio desc");
                stm.setString(1, idEmpresa != null && idEmpresa.intValue() == 1 ? "BV" : "BH");
                stm.setString(2, "S");
            } else {
                stm =
                    cnx.prepareCall("SELECT t.*   " + "         FROM  bicevida.bpi_tra_transacciones_tbl t   " +
                                    "         WHERE t.cod_estado in (3,6) " + "           and t.cod_medio in(4,11) " +
                                    "           and t.empresa = ?   " + "           AND t.webpay = ? " +
                                    "           and t.rut_persona = ? " + "           AND t.folio is not null  " +
                                    "           AND t.num_transaccion_medio is not null " +
                                    "           and t.medio_num_tarjeta is not null " + "  order by t.folio desc");
                stm.setString(1, idEmpresa != null && idEmpresa.intValue() == 1 ? "BV" : "BH");
                stm.setString(2, "S");
                stm.setInt(3, rutCliente);
            }


            rst = stm.executeQuery();
            while (rst.next()) {
                BpiTraTransaccionesTbl dto = new BpiTraTransaccionesTbl();
                dto.setCodEstado(rst.getInt("COD_ESTADO"));
                dto.setCodMedioPago(rst.getInt("COD_MEDIO"));
                dto.setFechafin(FechaUtil.toUtilDate(rst.getTimestamp("FECHAFIN")));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getTimestamp("FECHAINICIO")));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setObservaciones(rst.getString("OBSERVACIONES"));
                dto.setRutPersona(rst.getDouble("RUT_PERSONA"));
                dto.setMedioCodigoRespuesta(rst.getString("MEDIO_CODIGO_RESP"));
                dto.setMedioFechaPago(FechaUtil.toUtilDate(rst.getTimestamp("MEDIO_FECHA_PAGO")));
                dto.setMedioNumeroCuotas(rst.getInt("MEDIO_NUM_CUOTAS"));
                dto.setMedioNumeroTarjeta(rst.getString("MEDIO_NUM_TARJETA"));
                dto.setMedioTipoTarjeta(rst.getString("MEDIO_TIPO_TARJETA"));
                dto.setNumeroTransaccionBanco(rst.getString("NUM_TRANSACCION_MEDIO"));
                dto.setMedioOrdenCompra(rst.getString("MEDIO_NUM_ORDEN_COMP"));
                dto.setMedioCodigoAutorizacion(rst.getString("MEDIO_CODIGO_AUTORIZA"));
                dto.setCargo(rst.getDouble("CARGO"));
                dto.setDisponFondos(rst.getDate("DISPFONDOS"));
                dto.setTipoTransaccion(rst.getInt("TIPOTRX"));
                dto.setNumeroFolioCaja(rst.getLong("FOLIO"));
                dto.setCodigoEmpresa(rst.getString("EMPRESA"));
                dto.setCodigoOrigenTransaccion(rst.getString("ORIGEN_TRANSACCION"));
                java.sql.Date turnoSql = rst.getDate("TURNO");
                if (turnoSql != null)
                    dto.setTurno(new java.util.Date(turnoSql.getTime()));
                if (turnoSql == null)
                    dto.setTurno(null);
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findTransaccionesForRegularizacionWebPayPool() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.info("findTransaccionesForRegularizacionWebPayPool() - termina");
        return dtos;
    }


    /**
     * Recupera el ID de Comercion
     * @param empresa
     * @param mediopago
     * @return
     */
    public Long getIDCOMByEmpresaMedioPago(Long empresa, Long mediopago) {
        logger.info("getIDCOMByEmpresaMedioPago() - inicia");
        Long dtos = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT IDCOM FROM BPI_CEM_CODEMPMEDIO_TBL WHERE COD_EMPRESA = ? AND COD_MEDIO = ?");
            stm.setLong(1, empresa);
            stm.setLong(2, mediopago);
            rst = stm.executeQuery();
            if (rst.next()) {
                dtos = rst.getLong("IDCOM");

            }
        } catch (Exception e) {
            logger.error("findDetallePagoPolizaByFolioRecibo() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.info("getIDCOMByEmpresaMedioPago() - termina");
        return dtos;
    }


    /**
     * Recupera el ID de Comercio
     * @param idTransaccion id transaccion bice vida
     * @return
     */
    public HomologacionConvenioDTO getHomologacionConvenioByIdTrx(Long idTransaccion) {
        logger.info("getHomologacionConvenioByIdTrx() - inicia");
        HomologacionConvenioDTO dtos = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT IDCOMERCIO, ID_TRANSACCION, COD_MEDIO_PAGO, FECHA FROM BPI_HOMOLOGACION_BANCO_CONV WHERE ID_TRANSACCION = ?");
            stm.setLong(1, idTransaccion);
            rst = stm.executeQuery();
            if (rst.next()) {
                dtos = new HomologacionConvenioDTO();
                dtos.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dtos.setIdComercioTransaccion(rst.getString("IDCOMERCIO"));
                dtos.setFechaTransaccion(FechaUtil.toUtilDate(rst.getDate("FECHA")));
                dtos.setCodMedioPago(rst.getLong("COD_MEDIO_PAGO"));

            }
        } catch (Exception e) {
            logger.error("getHomologacionConvenioByIdTrx() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.info("getHomologacionConvenioByIdTrx() - termina");
        return dtos;
    }

    /**
     * Recupera el ID de Comercio
     * @param idTransaccion id transaccion bice vida
     * @return
     */
    public HomologacionConvenioDTO getHomologacionConvenioByIdComercioTrx(String idComercioTrx) {
        logger.info("getHomologacionConvenioByIdComercioTrx() - inicia");
        HomologacionConvenioDTO dtos = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            java.util.Date fechaProceso = new java.util.Date(System.currentTimeMillis());
            fechaProceso = FechaUtil.addDiasFecha(fechaProceso, -60); //RESTA 60 DIAS A LA FECHA DE HOY
            stm =
                cnx.prepareCall("SELECT IDCOMERCIO, ID_TRANSACCION, COD_MEDIO_PAGO, FECHA FROM BPI_HOMOLOGACION_BANCO_CONV WHERE IDCOMERCIO = ? AND FECHA >= ?");
            stm.setString(1, idComercioTrx);
            stm.setDate(2, new java.sql.Date(fechaProceso.getTime()));
            rst = stm.executeQuery();
            if (rst.next()) {
                dtos = new HomologacionConvenioDTO();
                dtos.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dtos.setIdComercioTransaccion(rst.getString("IDCOMERCIO"));
                dtos.setFechaTransaccion(FechaUtil.toUtilDate(rst.getDate("FECHA")));
                dtos.setCodMedioPago(rst.getLong("COD_MEDIO_PAGO"));

            }
        } catch (Exception e) {
            logger.error("getHomologacionConvenioByIdComercioTrx() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.info("getHomologacionConvenioByIdComercioTrx() - termina");
        return dtos;
    }


    public String getNroBoletaByIdTrx(Long idTransaccion) {
        String nroBoleta = "";
        String nroBoletaBD = "";
        logger.info("getNroBoletaByIdTrx() - inicia");
        Connection cnx = null;
        PreparedStatement pstm = null;
        ResultSet rst = null;
        String sql =
            "Select " +
            "  EXTRACTVALUE(XMLType.createXML(EXTRACTVALUE(bdN.DETALLE_PAGINA, '/Confirmacion/Convenio/XML')), 'Servipag/Documentos/Boleta')  BOLETA " +
            "From BPI_NAV_NAVEGACION_TBL bpN Join BPI_DNA_DETNAVEG_TBL bdN " +
            "On bpN.ID_NAVEGACION = bdN.ID_NAVEGACION " + "Where bpN.ID_TRANSACCION = ? " +
            "  AND bdN.MONTO_TRANSACCION>0 " + "Order By bdN.ENTRADA DESC";
        try {
            cnx = getConnection();

            pstm = cnx.prepareStatement(sql);
            pstm.setLong(1, idTransaccion);

            rst = pstm.executeQuery();
            while (rst.next()) {
                nroBoletaBD = rst.getString("BOLETA");
                if (nroBoletaBD != null) {
                    nroBoleta = nroBoletaBD;
                }
            }
        } catch (Exception e) {
            logger.error("getNroBoletaByIdTrx() - catch (error)", e);
        } finally {
            closeConnection(rst, pstm, cnx);
        }

        return nroBoleta;
    }

    public PersonaDto findPersonaByRut(Integer rut) {
        logger.info("findPersonasByRut() - inicia");
        String nombre = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        PersonaDto dto = null;
        try {
            cnx = getConnectionPersonas();
            String sql =
                "SELECT per.nombres ||' '|| per.apellido_paterno ||' '||per.apellido_materno as nombrecompleto, per.email_principal,\n" +
                "dir.ciudad,dir.nombre_comuna, dir.calle,dir.nombre_region\n" +
                "FROM bdp_personas_v per , BDP_DIRECCIONES_PER_FULL_V dir \n" +
                "WHERE per.rut = ? AND dir.rut = ? AND ROWNUM = 1";


            stm = cnx.prepareCall(sql);
            stm.setInt(1, rut);
            stm.setInt(2, rut);
            rst = stm.executeQuery();
            if (rst.next()) {
                dto = new PersonaDto();
                dto.setNombre(rst.getString("NOMBRECOMPLETO"));
                dto.setEmail(rst.getString("EMAIL_PRINCIPAL"));
                dto.setCiudad(rst.getString("CIUDAD"));
                dto.setComuna(rst.getString("NOMBRE_COMUNA"));
                dto.setCalle(rst.getString("CALLE"));
                dto.setRegion(rst.getString("NOMBRE_REGION"));

            }
        } catch (Exception e) {
            logger.error("findPersonasByRut() - catch (error)", e);

            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findPersonasByRut() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.info("findPersonasByRut() - termina");
        return dto;
    }

    @Override
    public PdfComprobantePagoDTO findPdfComprobantebyRut(Integer rut) throws SQLException, Exception {

        System.out.println("BD getPdfComprobantes() - start");
        PdfComprobantePagoDTO dto = new PdfComprobantePagoDTO();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List id = new ArrayList();
        String ruta = null;
        dto.setRut(rut.toString());

        /**
                 * Select de obtención de campos de pdf por rut
                 */
        String queryid =
            "SELECT aa.rut AS rut, aa.campo1 AS id_pdf, aa.campo10 as poliza, aa.campo9 as ramo FROM comprobante_rut aa\n" +
            "WHERE aa.rut = " + dto.getRut();

        /**
                 * Select de ubicación de pdf
                 */
        String querylocation =
            "SELECT a.ruta AS ruta FROM utiles a WHERE a.id = 1"; // id= 1 corresponde a net.bicevida.cl

        /**
                 * Definición de conexión
                 */
        try {


            conn = getConnectionPersistencia();
            System.out.println("OraclePdfComprobantePago() - BD getPdfComprobantes() - conn: " + conn);
            stmt = conn.createStatement();

            System.out.println("OraclePdfComprobantePago() - BD getPdfComprobantes() - stmt: " + stmt);

            /**
                 * Ejecución de obtención id pdf
                 */
            rs = stmt.executeQuery(queryid);
            System.out.println("OraclePdfComprobantePago() - BD getPdfComprobantes() - rs: " + rs);

            while (rs.next()) {
                String pdf = rs.getString("id_pdf");
                String poliza = rs.getString("poliza");
                String ramo = rs.getString("ramo");
                if (poliza == null)
                    poliza = " ";
                if (ramo == null)
                    ramo = " ";
                id.add(pdf + "-" + poliza + "-" + ramo);
            }
            System.out.println("OraclePdfComprobantePago() - BD getPdfComprobantes() - list id: " + id);
            /**
                 * Ejecución de obtención de url
                 */
            rs = stmt.executeQuery(querylocation);
            while (rs.next()) {
                ruta = rs.getString("ruta");
            }
            System.out.println("OraclePdfComprobantePago() - BD getPdfComprobantes() - ruta: " + ruta);

        } catch (SQLException e) {
            conn.close();
            stmt.close();
            rs.close();
            throw new SQLException(e.getMessage());
        } catch (Exception e) {
            conn.close();
            stmt.close();
            rs.close();
            throw new Exception(e.getMessage());
        } finally {
            /**
                     * Desconexion de la BD
                     */
            conn.close();
            stmt.close();
            rs.close();

        }

        /**
                 * Setting de datos en DTO
                 */
        System.out.println("OraclePdfComprobantePago() - BD getPdfComprobantes() - Setting de datos en DTO");
        if (id != null && id.size() != 0) {
            dto.setIdpdf((String[]) id.toArray(new String[id.size()]));
        }

        if (ruta != null) {
            dto.setUrl(ruta);
        }

        System.out.println("OraclePdfComprobantePago() - BD getPdfComprobantes() - end");
        return dto;
    }

    @Override
    public List<VWIndPrimaNormalVO> findPolizasByRut(Integer rut) throws SQLException, Exception {
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rs = null;
        VWIndPrimaNormalVO dto = new VWIndPrimaNormalVO();
        List<VWIndPrimaNormalVO> listdto = new ArrayList<VWIndPrimaNormalVO>();
        try {
            String sql = "SELECT * FROM IND_PRIMA_NORMAL_VW WHERE RUT_CONTRATANTE = ? ";
            
            cnx = getConnection();
            stm = cnx.prepareCall(sql);
            stm.setInt(1, rut);
            rs = stm.executeQuery();
            while (rs.next()) {
                dto = new VWIndPrimaNormalVO();
                dto.setCodEmpresa(rs.getInt("COD_EMPRESA"));
                dto.setCodMecanismo(rs.getInt("COD_MECANISMO"));
                dto.setCodProducto(rs.getInt("COD_PRODUCTO"));
                dto.setDescTipoRecibo(rs.getString("DESC_TIPO_RECIBO"));
                dto.setDvRutContratante(rs.getString("DV_RUT_CONTRATANTE"));
                dto.setFInicioRecibo(rs.getDate("F_INICIO_RECIBO"));
                dto.setFolioRecibo(rs.getInt("FOLIO_RECIBO"));
                dto.setFpago(rs.getInt("ID_FPAGO"));
                dto.setFTerminoRecibo(rs.getDate("F_TERMINO_RECIBO"));
                dto.setIdTipoRecibo(rs.getString("ID_TIPO_RECIBO"));
                dto.setIvaUfRecibo(rs.getDouble("IVA_UF_RECIBO"));
                dto.setIvaPesosRecibo(rs.getLong("IVA_PESOS_RECIBO"));
                dto.setNombre(rs.getString("NOMBRE"));
                dto.setPolizaPol(rs.getInt("POLIZA_POL"));
                dto.setPrimaBrutaUfRecibo(rs.getDouble("PRIMA_BRUTA_UF_RECIBO"));
                dto.setPrimaNetaUfRecibo(rs.getDouble("PRIMA_NETA_UF_RECIBO"));
                dto.setPrimaBrutaPesosRecibo(rs.getLong("PRIMA_BRUTA_PESOS_RECIBO"));
                dto.setPrimaNetaPesosRecibo(rs.getLong("PRIMA_NETA_PESOS_RECIBO"));
                dto.setValorCambioUF(rs.getDouble("VALOR_CAMBIO"));
                dto.setPropuestaPol(rs.getInt("PROPUESTA_POL"));
                dto.setRamo(rs.getInt("RAMO"));
                dto.setRutContratante(rs.getInt("RUT_CONTRATANTE"));
                dto.setViapago(rs.getInt("ID_VIA_PAGO"));
                listdto.add(dto);
                
            }
            
        } catch (SQLException e) {
            cnx.close();
            stm.close();
            rs.close();
            throw new SQLException(e.getMessage());
            
        } catch (Exception e) {
            cnx.close();
            stm.close();
            rs.close();
            throw new Exception(e.getMessage());
        } finally {
            cnx.close();
            stm.close();
            rs.close();

        }

        return listdto;
    }
}
