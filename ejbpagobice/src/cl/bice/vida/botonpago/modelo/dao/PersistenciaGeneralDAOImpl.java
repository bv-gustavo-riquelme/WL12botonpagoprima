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
import cl.bice.vida.botonpago.common.dto.parametros.Empresas;
import cl.bice.vida.botonpago.common.dto.parametros.MedioPago;
import cl.bice.vida.botonpago.common.dto.parametros.Paginas;
import cl.bice.vida.botonpago.common.dto.parametros.ValorUf;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosHistoricoVw;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosOfflineVw;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalOfflineVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalPatpacVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalVw;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.common.util.StringUtil;
import cl.bice.vida.botonpago.common.util.XmlUtil;
import cl.bice.vida.botonpago.modelo.dto.PersonaCotizadorDto;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;
import cl.bice.vida.botonpago.modelo.rest.RESTfulCallUtil;
import cl.bice.vida.botonpago.modelo.rest.RESTfulParametroDto;
import cl.bice.vida.botonpago.modelo.util.DateUtils;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;
import cl.bice.vida.botonpago.modelo.util.RutUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.MimeUtility;

import oracle.jdbc.OracleTypes;

import oracle.xdb.XMLType;

import oracle.xml.parser.v2.XMLDocument;

import org.apache.log4j.Logger;


public class PersistenciaGeneralDAOImpl extends DataSourceBice implements PersistenciaGeneralDAO {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(PersistenciaGeneralDAOImpl.class);

    /*
     * Constantes de la clase
     */
    private final int ESTADO_PAGADO_MEDIO_PAGO = 3;
    private final int ESTADO_RECHAZO_POR_MEDIO_PAGO = 99;

    public PersistenciaGeneralDAOImpl() {
        logger.debug("constructor inicializado");
    }

    /**
     * Recupera un valor de secuencia
     * @param secuencename
     * @return
     */
    public int getSecuenceValue(String secuencename) {
        logger.debug("getSecuenceValue() - inicia");
        int valor = 0;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("select " + secuencename + ".nextval from dual");
            rst = stm.executeQuery();
            while (rst.next()) {
                valor = rst.getInt(1);
            }
        } catch (Exception e) {
            logger.error("getSecuenceValue() - catch (error)", e);
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.debug("getSecuenceValue() - termina");
        return valor;

    }

    /**
     * Consulta si la caja esta abierta
     * @return dto con estado de caja
     */
    public AperturaCaja findCajaAbierta() {
        logger.debug("findCajaAbierta() - inicia");
        AperturaCaja dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_APC_APCAJA_TBL WHERE FECHA_CIERRE IS NULL");
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new AperturaCaja();
                dto.setFechaCierre(FechaUtil.toUtilDate(rst.getDate("FECHA_CIERRE")));
                dto.setMontoTotal(rst.getDouble("MONTO_TOTAL"));
                dto.setOperaciones(rst.getDouble("OPERACIONES"));
                dto.setTurno(rst.getTimestamp("TURNO"));
            }

        } catch (Exception e) {
            logger.error("findCajaAbierta() - catch (error)", e);
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.debug("findCajaAbierta() - termina");
        return dto;
    }

    /**
     * Consulta Dividendos por RUT
     * @param rut
     * @return
     */
    public List<BhDividendosVw> findDividendosByRut(String rut) {
        logger.debug("findDividendosByRut() - inicia");
        List<BhDividendosVw> dtos = new ArrayList<>();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_BHD_BHDIVIDENDOS_VW WHERE RUT_CLI = ?");
            stm.setInt(1, Integer.parseInt("0" + rut));
            rst = stm.executeQuery();
            while (rst.next()) {
                BhDividendosVw dto = new BhDividendosVw();
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
                //dto.setNombreSucursalPago(rst.getString("DESC_SUCURSAL"));
                dto.setNumDiv(rst.getLong("NUM_DIV"));
                dto.setNumOpe(rst.getLong("NUM_OPE"));
                dto.setOtrosCargos(rst.getDouble("OTR_CAR"));
                dto.setPlazo_mes(rst.getInt("PLA_MES"));
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
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findDividendosByRut() - catch (error)", e);

        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.debug("findDividendosByRut() - termina");
        return dtos;
    }

    /**
     * Consulta Dividendos en formato offline
     * @param rut
     * @return
     */
    public List<BhDividendosOfflineVw> findDividendosOfflineByRut(String rut) {
        logger.info("findDividendosOfflineByRut(" + rut + ") - inicia");
        List<BhDividendosOfflineVw> dtos = new ArrayList<>();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM OFF_BHDIVIDENDOS_TBL WHERE RUT_CLI = ?");
            stm.setInt(1, Integer.parseInt("0" + rut));
            rst = stm.executeQuery();
            while (rst.next()) {
                BhDividendosOfflineVw dto = new BhDividendosOfflineVw();
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
                dto.setPlazo_mes(rst.getInt("PLA_MES"));
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
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findDividendosOfflineByRut() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }


        logger.debug("findDividendosOfflineByRut() - termina");
        return dtos;
    }

    /**
     * Consulta de deudas por concepto de Polizas
     * @param rut
     * @return
     */
    public List findPolizasByRut(Integer rut) {
        logger.info("findPolizasByRut(" + rut + ") - inicia");
        List<IndPrimaNormalVw> dtos = new ArrayList<IndPrimaNormalVw>();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        CallableStatement stmVerifica = null;
        ResultSet rstVerifica = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM IND_PRIMA_NORMAL_VW WHERE RUT_CONTRATANTE = ? ");
            stm.setInt(1, rut);
            rst = stm.executeQuery();
            while (rst.next()) {
                boolean agregar = true;
                IndPrimaNormalVw dto = new IndPrimaNormalVw();
                dto.setCodEmpresa(rst.getInt("COD_EMPRESA"));
                dto.setCodMecanismo(rst.getInt("COD_MECANISMO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setDescTipoRecibo(rst.getString("DESC_TIPO_RECIBO"));
                dto.setDvRutContratante(StringUtil.getFirstCharacter(rst.getString("DV_RUT_CONTRATANTE")));
                dto.setFInicioRecibo(FechaUtil.toUtilDate(rst.getDate("F_INICIO_RECIBO")));
                dto.setFolioRecibo(rst.getInt("FOLIO_RECIBO"));
                dto.setFpago(rst.getInt("ID_FPAGO"));
                dto.setFTerminoRecibo(FechaUtil.toUtilDate(rst.getDate("F_TERMINO_RECIBO")));
                dto.setIdTipoRecibo(rst.getString("ID_TIPO_RECIBO"));
                dto.setIvaUfRecibo(rst.getDouble("IVA_UF_RECIBO"));
                dto.setIvaPesosRecibo(rst.getLong("IVA_PESOS_RECIBO"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setPolizaPol(rst.getInt("POLIZA_POL"));
                dto.setPrimaBrutaUfRecibo(rst.getDouble("PRIMA_BRUTA_UF_RECIBO"));
                dto.setPrimaNetaUfRecibo(rst.getDouble("PRIMA_NETA_UF_RECIBO"));
                dto.setPrimaBrutaPesosRecibo(rst.getLong("PRIMA_BRUTA_PESOS_RECIBO"));
                dto.setPrimaNetaPesosRecibo(rst.getLong("PRIMA_NETA_PESOS_RECIBO"));
                dto.setValorCambioUF(rst.getDouble("VALOR_CAMBIO"));
                dto.setPropuestaPol(rst.getInt("PROPUESTA_POL"));
                dto.setRamo(rst.getInt("RAMO"));
                dto.setRutContratante(rst.getInt("RUT_CONTRATANTE"));
                dto.setViapago(rst.getInt("ID_VIA_PAGO"));


                //LUEGO DE ESTE PROCESO QUITAR POLIZAS QUE YA ESTAN PAGADA
                stmVerifica =
                    cnx.prepareCall("SELECT " + "  COUNT(*) AS PAGADO " + "FROM " + "  UCAJAS.HUB_SVI_PRIMAS POL, " +
                                    "  UCAJAS.OPERACIONES OPE " + "WHERE OPE.USUARIO = POL.USUARIO " +
                                    "  AND OPE.TURNO = POL.TURNO " + "  AND OPE.TIPO = 'INGRES' " +
                                    "  AND OPE.ESTADO = 'VIGENT' " + "  AND POL.RUT_CLIENTE = ? " +
                                    "  AND POL.NUMERO_CONTRATO = ? " + "  AND POL.TRX_NUMBER = ? ");
                String ramoPoliza = dto.getRamo() + "-" + dto.getPolizaPol();
                stmVerifica.setInt(1, dto.getRutContratante());
                stmVerifica.setString(2, ramoPoliza);
                stmVerifica.setInt(3, dto.getFolioRecibo());
                rstVerifica = stmVerifica.executeQuery();
                if (rstVerifica.next()) {
                    Integer pagado = rstVerifica.getInt("PAGADO");
                    logger.info("rut :" + dto.getRutContratante());
                    logger.info("ramo poliza :" + ramoPoliza);
                    logger.info("trx_number :" + dto.getFolioRecibo());
                    logger.info("resultado pagado :" + pagado);

                    if (pagado != null && pagado.intValue() > 0)
                        agregar = false;
                }
                if (agregar) {
                    dtos.add(dto);
                }
                closeConnection(rstVerifica, stmVerifica, null);
            }

        } catch (Exception e) {
            logger.error("findPolizasByRut() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rstVerifica, stmVerifica, null);            
            closeConnection(rst, stm, cnx);
        }
        logger.debug("findPolizasByRut() - termina");
        return dtos;
    }

    /**
     * Consulta de deudas por concepto de Polizas
     * @param rut
     * @return
     */
    public List findAPVAPTByRut(Integer rut) {
        logger.info("findAPVAPTByRut(" + rut + ") - inicia");
        List<IndPrimaNormalVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM APV_APORTACION_EXTRAORD_VW WHERE RUT_CONTRATANTE = ?");
            stm.setInt(1, Integer.parseInt("0" + rut));
            rst = stm.executeQuery();
            while (rst.next()) {
                IndPrimaNormalVw dto = new IndPrimaNormalVw();
                dto.setCodEmpresa(rst.getInt("COD_EMPRESA"));
                dto.setCodMecanismo(rst.getInt("COD_MECANISMO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setDvRutContratante(StringUtil.getFirstCharacter(rst.getString("DV_RUT_CONTRATANTE")));
                dto.setFolioRecibo(rst.getInt("FOLIO_RECIBO"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setPolizaPol(rst.getInt("POLIZA_POL"));
                dto.setPropuestaPol(rst.getInt("NUMERO_PROPUESTA"));
                dto.setRamo(rst.getInt("RAMO"));
                dto.setRutContratante(rst.getInt("RUT_CONTRATANTE"));
                dto.setViapago(rst.getInt("ID_VIA_PAGO"));


                //CAMPOS QUE NO SE OBTENDRAN DESDE LA BASE DE DATOS Y QUE SERAN SETEADOS EN VACIOS O CERO
                dto.setFTerminoRecibo(new java.util.Date());
                dto.setPrimaBrutaUfRecibo(new Double(0));
                dto.setPrimaNetaUfRecibo(new Double(0));
                dto.setPrimaBrutaPesosRecibo(new Long(0));
                dto.setPrimaNetaPesosRecibo(new Long(0));

                //dto.setDescTipoRecibo(rst.getString("DESC_TIPO_RECIBO"));
                //dto.setFInicioRecibo(FechaUtil.toUtilDate(rst.getDate("F_INICIO_RECIBO")));
                //dto.setFpago(rst.getInt("ID_FPAGO"));
                //dto.setFTerminoRecibo(FechaUtil.toUtilDate(rst.getDate("F_TERMINO_RECIBO")));
                //dto.setIdTipoRecibo(rst.getString("ID_TIPO_RECIBO"));
                //dto.setIvaUfRecibo(rst.getDouble("IVA_UF_RECIBO"));
                //dto.setIvaPesosRecibo(rst.getLong("IVA_PESOS_RECIBO"));
                //dto.setPrimaBrutaUfRecibo(rst.getDouble("PRIMA_BRUTA_UF_RECIBO"));
                //dto.setPrimaNetaUfRecibo(rst.getDouble("PRIMA_NETA_UF_RECIBO"));
                //dto.setPrimaBrutaPesosRecibo(rst.getLong("PRIMA_BRUTA_PESOS_RECIBO"));
                //dto.setPrimaNetaPesosRecibo(rst.getLong("PRIMA_NETA_PESOS_RECIBO"));
                //dto.setValorCambioUF(rst.getDouble("VALOR_CAMBIO"));

                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findAPVAPTByRut() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }


        logger.debug("findAPVAPTByRut() - termina");
        return dtos;
    }

    /**
     * Consulta de deudas por concepto de Polizas
     * @param rut
     * @return
     */
    public List findAPVByRut(Integer rut) {
        logger.info("findAPVByRut(" + rut + ") - inicia");
        List<IndPrimaNormalVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM APV_VW WHERE RUT_CONTRATANTE = ?");
            stm.setInt(1, Integer.parseInt("0" + rut));
            rst = stm.executeQuery();
            while (rst.next()) {
                IndPrimaNormalVw dto = new IndPrimaNormalVw();
                dto.setCodEmpresa(rst.getInt("COD_EMPRESA"));
                dto.setCodMecanismo(rst.getInt("COD_MECANISMO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setDescTipoRecibo(rst.getString("DESC_TIPO_RECIBO"));
                dto.setDvRutContratante(StringUtil.getFirstCharacter(rst.getString("DV_RUT_CONTRATANTE")));
                dto.setFInicioRecibo(FechaUtil.toUtilDate(rst.getDate("F_INICIO_RECIBO")));
                dto.setFolioRecibo(rst.getInt("FOLIO_RECIBO"));
                dto.setFpago(rst.getInt("ID_FPAGO"));
                dto.setFTerminoRecibo(FechaUtil.toUtilDate(rst.getDate("F_TERMINO_RECIBO")));
                dto.setIdTipoRecibo(rst.getString("ID_TIPO_RECIBO"));
                dto.setIvaUfRecibo(rst.getDouble("IVA_UF_RECIBO"));
                dto.setIvaPesosRecibo(rst.getLong("IVA_PESOS_RECIBO"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setPolizaPol(rst.getInt("POLIZA_POL"));
                dto.setPrimaBrutaUfRecibo(rst.getDouble("PRIMA_BRUTA_UF_RECIBO"));
                dto.setPrimaNetaUfRecibo(rst.getDouble("PRIMA_NETA_UF_RECIBO"));
                dto.setPrimaBrutaPesosRecibo(rst.getLong("PRIMA_BRUTA_PESOS_RECIBO"));
                dto.setPrimaNetaPesosRecibo(rst.getLong("PRIMA_NETA_PESOS_RECIBO"));
                dto.setValorCambioUF(rst.getDouble("VALOR_CAMBIO"));
                dto.setPropuestaPol(rst.getInt("PROPUESTA_POL"));
                dto.setRamo(rst.getInt("RAMO"));
                dto.setRutContratante(rst.getInt("RUT_CONTRATANTE"));
                dto.setViapago(rst.getInt("ID_VIA_PAGO"));
                dto.setReajuste(rst.getInt("REAJUSTE"));
                dto.setMulta(rst.getInt("MULTA"));
                dto.setInteres(rst.getInt("INTERES"));
                dto.setCostosCobranza(rst.getInt("COSTOS_COBRANZA"));
                dto.setSecuencia(rst.getInt("SECUENCIA"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findAPVByRut() - catch (error)", e);

        } finally {
            closeConnection(rst, stm, cnx);

        }
        logger.debug("findAPVByRut() - termina");

        return dtos;
    }

    // TODO: ---- Se debe agregar consulta para vista con los productos APVAPT

    /**
     * Buscador de polizaas
     * @param rut
     * @return
     */
    public List findPolizasByRutPatPac(Integer rut) {
        logger.info("findPolizasByRutPatPac(" + rut + ") - inicia");
        List<IndPrimaNormalPatpacVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM IND_PRIMA_NORMAL_PATPAC_VW WHERE RUT_CONTRATANTE = ?");
            stm.setInt(1, Integer.parseInt("0" + rut));
            rst = stm.executeQuery();
            while (rst.next()) {
                IndPrimaNormalPatpacVw dto = new IndPrimaNormalPatpacVw();
                dto.setCodEmpresa(rst.getInt("COD_EMPRESA"));
                dto.setCodMecanismo(rst.getInt("COD_MECANISMO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setDescTipoRecibo(rst.getString("DESC_TIPO_RECIBO"));
                dto.setDvRutContratante(StringUtil.getFirstCharacter(rst.getString("DV_RUT_CONTRATANTE")));
                dto.setFInicioRecibo(FechaUtil.toUtilDate(rst.getDate("F_INICIO_RECIBO")));
                dto.setFolioRecibo(rst.getInt("FOLIO_RECIBO"));
                dto.setFTerminoRecibo(FechaUtil.toUtilDate(rst.getDate("F_TERMINO_RECIBO")));
                dto.setFUfRecibo(FechaUtil.toUtilDate(rst.getDate("F_UF_RECIBO")));
                dto.setIdFpago(rst.getString("ID_FPAGO"));
                dto.setIdTipoRecibo(rst.getString("ID_TIPO_RECIBO"));
                dto.setIdViaPago(rst.getString("ID_VIA_PAGO"));
                dto.setIvaUfRecibo(rst.getDouble("IVA_UF_RECIBO"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setPolizaPol(rst.getInt("POLIZA_POL"));
                dto.setPrimaBrutaUfRecibo(rst.getDouble("PRIMA_BRUTA_UF_RECIBO"));
                dto.setPrimaNetaUfRecibo(rst.getDouble("PRIMA_NETA_UF_RECIBO"));
                dto.setPropuestaPol(rst.getInt("PROPUESTA_POL"));
                dto.setRamo(rst.getInt("RAMO"));
                dto.setRutContratante(rst.getInt("RUT_CONTRATANTE"));
                dto.setValorUfRecibo(rst.getDouble("VALOR_UF_RECIBO"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findPolizasByRutPatPac() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.debug("findPolizasByRutPatPac() - termina");
        return dtos;
    }

    /**
     * Consulta Polizas OffLine
     * @param rut
     * @return
     */
    public List findPolizasOfflineByRut(Integer rut) {
        logger.info("findPolizasOfflineByRut(" + rut + ") - inicia");
        List<IndPrimaNormalOfflineVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM OFF_PRIMANORMAL_TBL WHERE RUT_CONTRATANTE = ?");
            stm.setInt(1, Integer.parseInt("0" + rut));
            rst = stm.executeQuery();
            while (rst.next()) {
                IndPrimaNormalOfflineVw dto = new IndPrimaNormalOfflineVw();
                dto.setCodEmpresa(rst.getInt("COD_EMPRESA"));
                dto.setCodMecanismo(rst.getInt("COD_MECANISMO"));
                dto.setCodProducto(rst.getInt("COD_PRODUCTO"));
                dto.setDescTipoRecibo(rst.getString("DESC_TIPO_RECIBO"));
                dto.setDvRutContratante(StringUtil.getFirstCharacter(rst.getString("DV_RUT_CONTRATANTE")));
                dto.setFInicioRecibo(FechaUtil.toUtilDate(rst.getDate("F_INICIO_RECIBO")));
                dto.setFolioRecibo(rst.getInt("FOLIO_RECIBO"));
                dto.setFpago(rst.getInt("ID_FPAGO"));
                dto.setFTerminoRecibo(FechaUtil.toUtilDate(rst.getDate("F_TERMINO_RECIBO")));
                dto.setIdTipoRecibo(rst.getString("ID_TIPO_RECIBO"));
                dto.setIvaUfRecibo(rst.getDouble("IVA_UF_RECIBO"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setPolizaPol(rst.getInt("POLIZA_POL"));
                dto.setPrimaBrutaUfRecibo(rst.getDouble("PRIMA_BRUTA_UF_RECIBO"));
                dto.setPrimaNetaUfRecibo(rst.getDouble("PRIMA_NETA_UF_RECIBO"));
                dto.setPropuestaPol(rst.getInt("PROPUESTA_POL"));
                dto.setRamo(rst.getInt("RAMO"));
                dto.setRutContratante(rst.getInt("RUT_CONTRATANTE"));
                dto.setViapago(rst.getInt("ID_VIA_PAGO"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findPolizasOfflineByRut() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.debug("findPolizasOfflineByRut() - termina");
        return dtos;
    }

    /**
     * Busca y entrega lista completa
     * de todos los convenidos de pagos
     * asociados para poder cancelar en linea
     * a traves del boton de pago eletronico.
     * @return lista de convenidos disponibles para cancelar
     */
    public List<MedioPago> findAllMediosVisibles() {
        logger.debug("findAllMediosVisibles() - inicia");
        List<MedioPago> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_MPG_MEDIOPAGO_TBL WHERE VISIBLE = ?");
            stm.setString(1, "Y");
            rst = stm.executeQuery();
            while (rst.next()) {
                MedioPago dto = new MedioPago();
                dto.setCodMedio(rst.getInt("COD_MEDIO"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setVisible(rst.getString("VISIBLE"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findAllMediosVisibles() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.debug("findAllMediosVisibles() - termina");
        return dtos;

    }

    /**
     * Busca y entrega lista completa
     * de todos los convenidos de pagos
     * asociados para poder cancelar en linea
     * a traves del boton de pago eletronico.
     * @return lista de convenidos disponibles para cancelar
     */
    public List<MedioPago> findAllMedios() {
        logger.debug("findAllMediosVisibles() - inicia");
        List<MedioPago> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_MPG_MEDIOPAGO_TBL");
            rst = stm.executeQuery();
            while (rst.next()) {
                MedioPago dto = new MedioPago();
                dto.setCodMedio(rst.getInt("COD_MEDIO"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setVisible(rst.getString("VISIBLE"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findAllMediosVisibles() - catch (error)", e);
            dtos = null;
        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.debug("findAllMediosVisibles() - termina");
        return dtos;

    }

    /**
     * Consulta todos los medios de pagos electronicos disponibles
     * @return
     */
    public List<MedioPago> findAllMediosdePagoElectronico(Integer empresa) {
        logger.debug("findAllMediosdePagoElectronico() - inicia");
        List<MedioPago> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        String filtro = "";
        try {
            cnx = super.getConnection();
            String sql = "SELECT * FROM BPI_MPG_MEDIOPAGO_TBL " + filtro + " ORDER BY COD_MEDIO";
            stm = cnx.prepareCall(sql);
            rst = stm.executeQuery();
            while (rst.next()) {
                MedioPago dto = new MedioPago();
                dto.setCodMedio(rst.getInt("COD_MEDIO"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setVisible(rst.getString("VISIBLE"));
                dtos.add(dto);
            }

        } catch (Exception e) {
            logger.error("findAllMediosdePagoElectronico() - catch (error)", e);
            dtos = null;

        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.debug("findAllMediosdePagoElectronico() - termina");
        return dtos;

    }

    /**
     * Consulta el valor de la UF
     * @param fecha
     * @return
     */
    public ValorUf findValorUfByDate(Date fecha) {
        String fechaString = DateUtils.getFechaFormat(fecha, "dd/MM/yyyy");
        logger.info("findValorUfByDate() - date [" + fechaString + "]");
        ValorUf dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM VALOR_UF WHERE FECHA = TO_DATE('" + fechaString + "','DD/MM/YYYY')");
            //stm.setDate(1, new java.sql.Date(fecha.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new ValorUf();
                dto.setFecha(FechaUtil.toUtilDate(rst.getDate("FECHA")));
                dto.setValorUf(rst.getDouble("VALOR"));
            }

        } catch (Exception e) {
            logger.error("findValorUfByDate() - catch (error)", e);
            dto = null;

        } finally {
            closeConnection(rst, stm, cnx);
        }
        logger.debug("findValorUfByDate() - termina");
        return dto;
    }

    /**
     * Busca lista de dividendos del mes
     * segun el rut adjuntado
     * @param rut
     * @return
     */
    public List<BhDividendosHistoricoVw> findPagosDividendosMesByRut(Integer rut) {
        logger.info("findDividendosOfflineByRut(" + rut + ") - inicia");
        List dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            Date fechaini = FechaUtil.getPrimerDiaMesAnterior();
            Date fechafin = FechaUtil.getPrimerDiaMesSiguiente();
            cnx = getConnection();

            stm =
                cnx.prepareCall("SELECT * FROM BPI_DIH_DIVIDENDOSHIST_VW WHERE RUT_CLI = ? AND FEC_PAGO BETWEEN ? AND ?");
            stm.setInt(1, Integer.parseInt("0" + rut));
            stm.setDate(2, new java.sql.Date(fechaini.getTime()));
            stm.setDate(3, new java.sql.Date(fechafin.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                BhDividendosHistoricoVw dto = new BhDividendosHistoricoVw();
                dto.setEstado(rst.getString("COD_ESTADO"));
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
        logger.debug("findDividendosOfflineByRut() - termina");
        return dtos;
    }

    /**
     * Buscra los codigos de empresa segun medio
     * @param empresa
     * @param medio
     * @return
     */
    public CodEmpByMedio findCodByEmpInMedio(Integer empresa, Integer medio) {
        logger.info("findCodByEmpInMedio(empresa->" + empresa + ",medio->" + medio + ") - inicia");
        CodEmpByMedio dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_CEM_CODEMPMEDIO_TBL WHERE COD_EMPRESA = ? AND COD_MEDIO = ?");
            stm.setInt(1, empresa);
            stm.setInt(2, medio);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new CodEmpByMedio();
                dto.setCodEmpresa(rst.getInt("COD_EMPRESA"));
                dto.setCodMedio(rst.getInt("COD_MEDIO"));
                dto.setIDCOM(rst.getLong("IDCOM"));
                dto.setSRVREC(rst.getInt("SERVEC"));
                dto.setUrl(rst.getString("URL"));
            }

        } catch (Exception e) {
            logger.error("findCodByEmpInMedio() - catch (error)", e);
            dto = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findCodByEmpInMedio() - catch (error)", f);
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
        logger.debug("findCodByEmpInMedio() - termina");
        return dto;
    }

    public Paginas findPaginaById(Integer id) {
        return null;
    }

    /**
     * Busca una navegacion base su ID
     * @param id
     * @return
     */
    public Navegacion findNavegacionById(Long id) {
        logger.info("findNavegacionById(" + id + ") - inicia");
        Navegacion dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_NAV_NAVEGACION_TBL WHERE ID_NAVEGACION = ?");
            stm.setLong(1, id);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new Navegacion();
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setEmpresa(rst.getInt("EMPRESA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setIdNavegacion(rst.getLong("ID_NAVEGACION"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTransaccion(rst.getInt("MONTO_TRANSACCION"));
                dto.setRutPersona(rst.getInt("RUT_PERSONA"));
            }

        } catch (Exception e) {
            logger.error("findNavegacionById() - catch (error)", e);
            dto = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findNavegacionById() - catch (error)", f);
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
        logger.debug("findNavegacionById() - termina");
        return dto;
    }

    /**
     * Busca una navegacion base su idtrx
     * @param idtrx
     * @return
     */
    public Navegacion findNavegacionByIdTrx(Long idtrx) {
        logger.info("findNavegacionByIdTrx(" + idtrx + ") - inicia");
        Navegacion dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_NAV_NAVEGACION_TBL WHERE ID_TRANSACCION = ?");
            stm.setLong(1, idtrx);
            rst = stm.executeQuery();
            while (rst.next()) {
                logger.debug("findNavegacionByIdTrx() - Seteando datos");
                dto = new Navegacion();
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setEmpresa(rst.getInt("EMPRESA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setIdNavegacion(rst.getLong("ID_NAVEGACION"));
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setMontoTransaccion(rst.getInt("MONTO_TRANSACCION"));
                dto.setRutPersona(rst.getInt("RUT_PERSONA"));
                logger.info("findNavegacionByIdTrx() - SFin eteando datos");
            }

        } catch (Exception e) {
            logger.error("findNavegacionByIdTrx() - catch (error)", e);
            dto = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findNavegacionByIdTrx() - catch (error)", f);
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
        logger.debug("findNavegacionByIdTrx() - termina");
        return dto;
    }

    /**
     * Busca una transaccion por su identificacion
     * para actualizadr datos de esta
     * @param id
     * @return
     */
    public Transacciones findTransaccionById(Integer id) {
        logger.info("findTransaccionById(" + id + ") - inicia");
        Transacciones dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_TRA_TRANSACCIONES_TBL WHERE ID_TRANSACCION = ?");
            stm.setInt(1, id);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new Transacciones();
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dto.setCod_estado(rst.getInt("COD_ESTADO"));
                dto.setCod_medioPago(rst.getInt("COD_MEDIO"));
                dto.setMontoTotal(rst.getInt("MONTO_TOTAL"));
                dto.setFechainicio(FechaUtil.toUtilDate(rst.getDate("FECHAINICIO")));
                dto.setRutPersona(rst.getInt("RUT_PERSONA"));
                dto.setNombrePersona(rst.getString("NOMBRE_PERSONA"));
                dto.setTurno(FechaUtil.toUtilDate(rst.getDate("TURNO")));
                dto.setTrxUsadaporMDB(rst.getString("TRX_USADAPOR_MDB"));
                dto.setFolioCaja(rst.getLong("FOLIO"));
            }

        } catch (Exception e) {
            logger.error("findTransaccionById() - catch (error)", e);
            dto = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionById() - catch (error)", f);
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
        logger.debug("findTransaccionById() - termina");
        return dto;

    }

    /**
     * Genera nueva navegacion
     * @param rut
     * @param empresa
     * @return
     */
    public Navegacion newNavegacion(int rut, int empresa) {
        logger.info("newNavegacion() - iniciando");
        Connection cnx = null;
        Navegacion dto = new Navegacion();
        try {

            //Recuepera secuencia
            Long idnavegacion = new Long(Integer.toString(getSecuenceValue("BPI_NAV_IDNAVEGACION_SEQ")));
            logger.info("newNavegacion(rut->" + rut + ",empresa->" + empresa + ") - secuencia idnavegacion :" +
                        idnavegacion);

            //Genera el contenido del DTO a enviar
            dto.setRutPersona(rut);
            dto.setFechaHora(new Date());
            dto.setMontoTransaccion(null);
            dto.setEmpresa(empresa);
            dto.setIdNavegacion(idnavegacion);
            dto.setIdTransaccion(null);
            dto.setCod_medio(null);

            cnx = getConnection();
            //Inserta y deja activo el dispositivo
            PreparedStatement stm =
                cnx.prepareStatement("INSERT INTO BPI_NAV_NAVEGACION_TBL (COD_MEDIO, EMPRESA, FECHA_HORA, ID_NAVEGACION, ID_TRANSACCION, MONTO_TRANSACCION, RUT_PERSONA) VALUES(?,?,?,?,?,?,?)");

            // SET PARAMETROS
            if (dto.getCod_medio() != null)
                stm.setInt(1, dto.getCod_medio());
            if (dto.getEmpresa() != null)
                stm.setInt(2, dto.getEmpresa());
            if (dto.getFechaHora() != null)
                stm.setTimestamp(3, new Timestamp(dto.getFechaHora().getTime()));
            if (dto.getIdNavegacion() != null)
                stm.setLong(4, dto.getIdNavegacion());
            if (dto.getIdTransaccion() != null)
                stm.setLong(5, dto.getIdTransaccion());
            if (dto.getMontoTransaccion() != null)
                stm.setInt(6, dto.getMontoTransaccion());
            if (dto.getRutPersona() != null)
                stm.setInt(7, dto.getRutPersona());

            if (dto.getCod_medio() == null)
                stm.setNull(1, OracleTypes.INTEGER);
            if (dto.getEmpresa() == null)
                stm.setNull(2, OracleTypes.INTEGER);
            if (dto.getFechaHora() == null)
                stm.setNull(3, OracleTypes.DATE);
            if (dto.getIdNavegacion() == null)
                stm.setNull(4, OracleTypes.INTEGER);
            if (dto.getIdTransaccion() == null)
                stm.setNull(5, OracleTypes.NUMBER);
            if (dto.getMontoTransaccion() == null)
                stm.setNull(6, OracleTypes.INTEGER);
            if (dto.getRutPersona() == null)
                stm.setNull(7, OracleTypes.INTEGER);

            logger.info("newNavegacion() - getCod_medio :" + dto.getCod_medio());
            logger.info("newNavegacion() - getEmpresa :" + dto.getEmpresa());
            logger.info("newNavegacion() - getFechaHora :" + dto.getFechaHora());
            logger.info("newNavegacion() - getIdNavegacion :" + dto.getIdNavegacion());
            logger.info("newNavegacion() - getIdTransaccion :" + dto.getIdTransaccion());
            logger.info("newNavegacion() - getMontoTransaccion :" + dto.getMontoTransaccion());
            logger.info("newNavegacion() - getRutPersona :" + dto.getRutPersona());

            // EXECUTE
            stm.execute();
            stm.close();

        } catch (Exception e) {
            logger.error("newNavegacion() - catch (error)", e);
            dto = null;
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        return dto;
    }

    /**
     * Actualiza transaccion de navegacion
     * @param nav
     * @param transaccion
     * @param monto
     * @param medio
     */
    public void updateTransaccionInNavegacion(Long nav, Long transaccion, Integer monto, Integer medio) {
        logger.info("updateTransaccion(nav[" + nav + "] - transaccion[" + transaccion + "] - monto[" + monto +
                    "] - medio[" + medio + "]) - iniciando y buscando navegacion");
        Navegacion dto = findNavegacionById(nav);
        logger.debug("updateTransaccion() - iniciando update");
        Connection cnx = null;
        try {

            //Setea nuevos valores
            dto.setMontoTransaccion(monto);
            dto.setIdTransaccion(transaccion);
            dto.setCod_medio(medio);

            cnx = getConnection();
            PreparedStatement stm =
                cnx.prepareStatement("UPDATE BPI_NAV_NAVEGACION_TBL SET MONTO_TRANSACCION = ?, ID_TRANSACCION = ?, COD_MEDIO = ? WHERE ID_NAVEGACION = ?");

            // SET PARAMETROS
            stm.setInt(1, dto.getMontoTransaccion());
            stm.setLong(2, dto.getIdTransaccion());
            stm.setInt(3, dto.getCod_medio());
            stm.setLong(4, nav);

            logger.info("updateTransaccion() - getMontoTransaccion : " + dto.getMontoTransaccion());
            logger.info("updateTransaccion() - getIdTransaccion : " + dto.getIdTransaccion());
            logger.info("updateTransaccion() - getCod_medio : " + dto.getCod_medio());
            logger.info("updateTransaccion() - idNavegacion : " + nav);

            // EXECUTE
            stm.execute();
            stm.close();

        } catch (Exception e) {
            logger.error("updateTransaccion() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("updateTransaccion() - terminado");
    }

    /**
     * Metodo para crear un detalle de navegacion
     * @param nav
     * @param entrada
     * @param cod_pagina
     * @param monto
     * @param xml
     * @return dto con el detalle de navegacion
     */
    public DetalleNavegacion createDetalleNavegacion(Long nav, int entrada, int cod_pagina, Integer monto, String xml,
                                                     int idCanal) {
        DetalleNavegacion detalle = newDetalleNavegacion(nav, entrada, cod_pagina, monto, idCanal);
        updateDetNavegacionXML(detalle, xml);
        return detalle;
    }

    /**
     * Metodo que genera la navegacion de XML
     * una ves que es consultado
     * @param nav
     * @param entrada
     * @param cod_pagina
     * @param monto
     * @return dto con el detalle de navegacion
     */
    public DetalleNavegacion newDetalleNavegacion(Long nav, int entrada, int cod_pagina, Integer monto, int idCanal) {
        logger.info("newDetalleNavegacion(nav[" + nav + "] - entrada[" + entrada + "] - cod_pagina[" + cod_pagina +
                    "] - monto[" + monto + "] - idCanal[" + idCanal + "]) - iniciando");
        Connection cnx = null;
        DetalleNavegacion dto = new DetalleNavegacion();
        try {
            //Genera el contenido del DTO a enviar
            dto.setIdNavegacion(nav);
            dto.setEntrada(entrada);
            dto.setCodpagina(cod_pagina);
            dto.setFechaHora(null);
            dto.setMontoTransaccion(monto);
            dto.setDetallePagina("<Vacio/>");
            dto.setIdCanal(idCanal);

            cnx = getConnection();
            //Inserta y deja activo el dispositivo
            PreparedStatement stm =
                cnx.prepareStatement("INSERT INTO BPI_DNA_DETNAVEG_TBL (COD_PAGINA, ID_NAVEGACION, FECHA_HORA, MONTO_TRANSACCION, DETALLE_PAGINA, ENTRADA, ID_CANAL) VALUES(?,?,?,?,?,?,?)");

            // SET PARAMETROS
            stm.setInt(1, dto.getCodpagina());
            stm.setLong(2, dto.getIdNavegacion());
            stm.setTimestamp(3, new Timestamp(dto.getFechaHora().getTime()));
            stm.setInt(4, dto.getMontoTransaccion());
            stm.setString(5, dto.getDetallePagina());
            stm.setInt(6, dto.getEntrada());
            stm.setInt(7, dto.getIdCanal());

            logger.info("newDetalleNavegacion() - getCodpagina :" + dto.getCodpagina());
            logger.info("newDetalleNavegacion() - getIdNavegacion :" + dto.getIdNavegacion());
            logger.info("newDetalleNavegacion() - getFechaHora :" + dto.getFechaHora());
            logger.info("newDetalleNavegacion() - getMontoTransaccion :" + dto.getMontoTransaccion());
            logger.info("newDetalleNavegacion() - getDetallePagina :" + dto.getDetallePagina());
            logger.info("newDetalleNavegacion() - getEntrada :" + dto.getEntrada());
            logger.info("newDetalleNavegacion() - getIdCanal :" + dto.getIdCanal());

            // EXECUTE
            stm.execute();
            stm.close();

        } catch (Exception e) {
            logger.error("newDetalleNavegacion() - catch (error)", e);
            dto = null;
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("newDetalleNavegacion() - terminando");
        return dto;
    }

    /**
     * Actualiza el XML
     * @param detalle
     * @param xml
     */
    public void updateDetNavegacionXML(DetalleNavegacion detalle, String xml) {
        logger.debug("updateDetNavegacionXML() - iniciando");
        Connection cnx = null;
        try {
            if (detalle != null) {
                cnx = getConnection();
                XMLType xmlType = XMLType.createXML(cnx, xml);
                Clob xmlClob = xmlType.getClobVal();

                //Inserta y deja activo el dispositivo
                PreparedStatement stm =
                    cnx.prepareStatement("UPDATE BPI_DNA_DETNAVEG_TBL SET DETALLE_PAGINA = XMLType(?) WHERE ID_NAVEGACION = ? AND ENTRADA = ?");

                // SET PARAMETROS
                stm.setClob(1, xmlClob);
                stm.setLong(2, detalle.getIdNavegacion());
                stm.setInt(3, detalle.getEntrada());

                logger.info("updateDetNavegacionXML() - xmlClob : " + xmlClob);
                logger.info("updateDetNavegacionXML() - getIdNavegacion : " + detalle.getIdNavegacion());
                logger.info("updateDetNavegacionXML() - getEntrada : " + detalle.getEntrada());

                // EXECUTE
                stm.execute();
                stm.close();
            }
        } catch (Exception e) {
            logger.error("updateDetNavegacionXML() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("updateDetNavegacionXML() - terminado");
    }

    /**
     * Confecciona una nueva transaccion
     * en base a los datos enviados de la
     * confirmacion del xml y sus productos
     * que se han seleccionado
     * @param cod_medio
     * @param turno
     * @param monto
     * @param fechaini
     * @param rut
     * @param nombre
     * @return
     */
    public Transacciones newTransaccion(String origenTransaccion, String empresa, int cod_medio, Date turno, int monto,
                                        Date fechaini, int rut, String nombre) {
        logger.info("newTransaccion(origenTransaccion[" + origenTransaccion + "] - empresa[" + empresa +
                    "] - cod_medio[" + cod_medio + "] - turno[" + turno + "] - monto[" + monto + "] - fechaini[" +
                    fechaini + "] - rut[" + rut + "] - nombre[" + nombre + "]) - iniciando");
        Connection cnx = null;
        Transacciones dto = new Transacciones();
        try {

            //Recuepera secuencia
            int idtransacciones = getSecuenceValue("BPI_TRA_IDTRANSACCION_SEQ");
            logger.info("newTransaccion() - secuencia idtransaccion :" + idtransacciones);
            logger.info("newTransaccion() - fecha y hora de turno :" +
                        FechaUtil.getFechaFormateoCustom(turno, "yyyy-MM-dd hh:mm:ss"));

            //Genera el contenido del DTO a enviar
            dto.setCod_estado(1);
            dto.setIdTransaccion(new Long(idtransacciones));
            dto.setCod_medioPago(cod_medio);
            dto.setTurno(turno);
            dto.setMontoTotal(monto);
            dto.setFechainicio(fechaini);
            dto.setRutPersona(rut);
            dto.setNombrePersona(nombre);

            cnx = getConnection();
            PreparedStatement stm =
                cnx.prepareStatement("INSERT INTO BPI_TRA_TRANSACCIONES_TBL (ID_TRANSACCION, " +
                                     "COD_ESTADO, COD_MEDIO, MONTO_TOTAL, FECHAINICIO, RUT_PERSONA, " +
                                     "NOMBRE_PERSONA, TURNO, FECHAFIN, OBSERVACIONES, FOLIO, NUM_TRANSACCION_CAJA, " +
                                     "NUM_TRANSACCION_MEDIO, ORIGEN_TRANSACCION, RESULTADO_PAGO, EMPRESA ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            // SET PARAMETROS
            stm.setLong(1, dto.getIdTransaccion()); //PK Llave primaria
            stm.setInt(2, dto.getCod_estado());
            stm.setInt(3, dto.getCod_medioPago());
            stm.setInt(4, dto.getMontoTotal());
            stm.setTimestamp(5, new Timestamp(dto.getFechainicio().getTime()));
            stm.setInt(6, dto.getRutPersona());
            stm.setString(7, dto.getNombrePersona());
            stm.setTimestamp(8, new Timestamp(dto.getTurno().getTime())); //FK --> Fecha apertura de Caja

            //Valores Nulos
            stm.setNull(9, OracleTypes.DATE);
            stm.setNull(10, OracleTypes.VARCHAR);
            stm.setNull(11, OracleTypes.NUMBER);
            stm.setNull(12, OracleTypes.NUMBER);
            stm.setNull(13, OracleTypes.NUMBER);

            //NUEVOS CAMPOS
            stm.setString(14, origenTransaccion);
            stm.setNull(15, OracleTypes.VARCHAR);
            stm.setString(16, empresa);


            logger.info("newTransaccion() - getIdTransaccion:" + dto.getIdTransaccion());
            logger.info("newTransaccion() - getCod_estado:" + dto.getCod_estado());
            logger.info("newTransaccion() - getCod_medioPago:" + dto.getCod_medioPago());
            logger.info("newTransaccion() - getFechainicio:" + dto.getFechainicio());
            logger.info("newTransaccion() - getRutPersona:" + dto.getRutPersona());
            logger.info("newTransaccion() - getNombrePersona:" + dto.getNombrePersona());
            logger.info("newTransaccion() - getTurno:" + dto.getTurno());

            // EXECUTE
            stm.execute();
            stm.close();

        } catch (Exception e) {
            logger.error("newNavegacion() - catch (error)", e);
            dto = null;
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("newTransaccion() - terminado");
        return dto;
    }

    /**
     * Actualiza la transaccion
     * @param idtransaccion
     * @param cod_medio
     * @param monto
     * @param turno
     * @return
     */
    public Transacciones updateTransaccion(Integer idtransaccion, Integer cod_medio, Integer monto, Date turno) {
        Transacciones dto = findTransaccionById(idtransaccion);
        logger.info("updateTransaccion(idtransaccion[" + idtransaccion + "] - cod_medio[" + cod_medio + "] - monto[" +
                    monto + "] - turno[" + turno + "]) - iniciando ");
        Connection cnx = null;
        try {

            //Setea nuevos valores
            dto.setTurno(turno);
            dto.setCod_medioPago(cod_medio);
            dto.setMontoTotal(monto);

            cnx = getConnection();
            PreparedStatement stm =
                cnx.prepareStatement("UPDATE BPI_TRA_TRANSACCIONES_TBL SET TURNO = ?, COD_MEDIO = ?, MONTO_TOTAL = ? WHERE ID_TRANSACCION = ?");

            // SET PARAMETROS
            stm.setTimestamp(1, new Timestamp(dto.getTurno().getTime()));
            stm.setInt(2, dto.getCod_medioPago());
            stm.setInt(3, dto.getMontoTotal());
            stm.setInt(4, idtransaccion);

            logger.info("updateTransaccion() - getCod_medioPago :" + dto.getCod_medioPago());
            logger.info("updateTransaccion() - getMontoTotal :" + dto.getMontoTotal());
            logger.info("updateTransaccion() - idtransaccion :" + idtransaccion);

            // EXECUTE
            stm.execute();
            stm.close();

        } catch (Exception e) {
            logger.error("updateTransaccion() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("updateTransaccion() - terminado");
        return dto;
    }

    /**
     * Actualiza el tipo de transaccion transaccion
     * @param idtransaccion
     * @param tipoTransaccion
     * @return
     */
    public boolean updateTipoTransaccion(Integer idtransaccion, int tipoTransaccion) {
        logger.info("updateTipoTransaccion(idtransaccion[" + idtransaccion + "] - tipoTransaccion[" + tipoTransaccion +
                    "]) - iniciando ");
        boolean ret = false;
        Connection cnx = null;
        try {
            cnx = getConnection();
            PreparedStatement stm =
                cnx.prepareStatement("UPDATE BPI_TRA_TRANSACCIONES_TBL SET TIPOTRX = ? WHERE ID_TRANSACCION = ?");

            // SET PARAMETROS
            stm.setInt(1, tipoTransaccion);
            stm.setInt(2, idtransaccion.intValue());
            logger.debug("updateTipoTransaccion() - tipoTransaccion :" + tipoTransaccion);


            // EXECUTE
            stm.execute();
            stm.close();

            ret = true;

        } catch (Exception e) {
            logger.error("updateTipoTransaccion() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                logger.error("updateTipoTransaccion() - catch (error)", e);
            }
        }
        logger.debug("updateTipoTransaccion() - terminado");
        return ret;
    }

    /**
     * Actualiza el tipo de transaccion y el cargo
     * @param idtransaccion
     * @param tipoTransaccion
     * @param cargo
     * @return
     */
    public boolean updateTipoTransaccionAPT(Integer idtransaccion, int tipoTransaccion, int cargo) {
        logger.info("updateTipoTransaccionAPT(idtransaccion[" + idtransaccion + "] - tipoTransaccion[" +
                    tipoTransaccion + "] - cargo[" + cargo + "]) - iniciando ");
        boolean ret = false;
        Connection cnx = null;
        try {
            cnx = getConnection();
            PreparedStatement stm =
                cnx.prepareStatement("UPDATE BPI_TRA_TRANSACCIONES_TBL SET TIPOTRX = ?, CARGO = ? WHERE ID_TRANSACCION = ?");

            // SET PARAMETROS
            stm.setInt(1, tipoTransaccion);
            stm.setInt(2, cargo);
            stm.setInt(3, idtransaccion.intValue());
            logger.info("updateTipoTransaccionAPT() - tipoTransaccion :" + tipoTransaccion);


            // EXECUTE
            stm.execute();
            stm.close();

            ret = true;

        } catch (Exception e) {
            logger.error("updateTipoTransaccionAPT() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                logger.error("updateTipoTransaccion() - catch (error)", e);
            }
        }
        logger.debug("updateTipoTransaccionAPT() - terminado");
        return ret;
    }

    /**
     * Actualiza el detalle de transaccion
     * @param transaccion
     * @param detallesByEmp
     * @param comprobantes
     */
    public void updateDetalleTransaccion(Transacciones transaccion, List detallesByEmp, List comprobantes) {
        logger.debug("updateDetalleTransaccion() - inicio");
        transaccion.setComprobantesCollection(comprobantes);
        Iterator insertscom = comprobantes.iterator();
        while (insertscom.hasNext()) {
            Comprobantes dto = (Comprobantes) insertscom.next();
            logger.debug("updateDetalleTransaccion() - insertar comprobante : " + dto.getNumProducto());
            boolean resultado = insertComprobante(dto);
        }

        DetalleTransaccionByEmp dto2;
        Iterator insertsdet = detallesByEmp.iterator();
        while (insertsdet.hasNext()) {
            DetalleTransaccionByEmp dto = (DetalleTransaccionByEmp) insertsdet.next();
            logger.debug("Insertar detalleempresa : " + dto.getNumTransaccionCaja());
            boolean resultado = insertDetalleTransaccionEmp(dto);
        }
        logger.debug("updateDetalleTransaccion() - termino");
    }

    /**
     * Consulta las polizas del mes
     * @param rut
     * @return
     */
    public List<CartolaPolizas> findPagosPolizasMesByRut(int rut) {
        logger.info("findPagosPolizasMesByRut(" + rut + ") - inicia");
        List<CartolaPolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            Date fechaini = FechaUtil.getPrimerDiaMesAnterior();
            Date fechafin = FechaUtil.getPrimerDiaMesSiguiente();
            cnx = getConnection();

            stm =
                cnx.prepareCall("SELECT * FROM BPI_DPP_DETALLEPOLPAGADAS_VW WHERE RUT_CLIENTE = ? AND FECHA_ESTADO BETWEEN ? AND ?");
            stm.setInt(1, Integer.parseInt("0" + rut));
            stm.setDate(2, new java.sql.Date(fechaini.getTime()));
            stm.setDate(3, new java.sql.Date(fechafin.getTime()));
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
            logger.error("findPagosPolizasMesByRut() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findPagosPolizasMesByRut() - catch (error)", f);
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
        logger.debug("findPagosPolizasMesByRut() - termina");
        return dtos;
    }

    public List<DetallePagoPoliza> findDetallePagoPolizaByFolioRecibo(Long folio) {
        return null;
    }


    /**
     * Inserta un comprobante
     * @param dto
     * @return
     */
    public Boolean insertComprobante(Comprobantes dto) {
        logger.debug("insertComprobante() - iniciando");
        Connection cnx = null;
        boolean res = false;
        try {
            cnx = getConnection();
            //Inserta y deja activo el dispositivo
            PreparedStatement stm =
                cnx.prepareStatement("INSERT INTO BPI_COM_COMPROBANTES_TBL (ID_TRANSACCION, " +
                                     "COD_PRODUCTO, NUM_PRODUCTO, CUOTA, MONTO_BASE, MONTO_EXCEDENTE, " +
                                     "MONTO_TOTAL) VALUES(?,?,?,?,?,?,?)");

            // SET PARAMETROS
            if (dto.getIdTransaccion() != null)
                stm.setLong(1, dto.getIdTransaccion());
            if (dto.getCodProducto() != null)
                stm.setInt(2, dto.getCodProducto());
            if (dto.getNumProducto() != null)
                stm.setLong(3, dto.getNumProducto());
            if (dto.getCuota() != null)
                stm.setInt(4, dto.getCuota());
            if (dto.getMontoBase() != null)
                stm.setInt(5, dto.getMontoBase());
            if (dto.getMontoExcedente() != null)
                stm.setInt(6, dto.getMontoExcedente());
            if (dto.getMontoTotal() != null)
                stm.setInt(7, dto.getMontoTotal());

            if (dto.getIdTransaccion() == null)
                stm.setNull(1, OracleTypes.NUMBER);
            if (dto.getCodProducto() == null)
                stm.setNull(2, OracleTypes.INTEGER);
            if (dto.getNumProducto() == null)
                stm.setNull(3, OracleTypes.NUMBER);
            if (dto.getCuota() == null)
                stm.setNull(4, OracleTypes.INTEGER);
            if (dto.getMontoBase() == null)
                stm.setNull(5, OracleTypes.INTEGER);
            if (dto.getMontoExcedente() == null)
                stm.setNull(6, OracleTypes.INTEGER);
            if (dto.getMontoTotal() == null)
                stm.setNull(7, OracleTypes.INTEGER);

            // EXECUTE
            stm.execute();
            stm.close();
            res = true;

        } catch (Exception e) {
            logger.error("insertComprobante() - catch (error)", e);
            dto = null;
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        return res;
    }

    /**
     * Inserta un detalle de transaccion
     * @param dto
     * @return
     */
    public Boolean insertDetalleTransaccionEmp(DetalleTransaccionByEmp dto) {
        logger.debug("insertComprobante() - iniciando");
        Connection cnx = null;
        boolean res = false;
        try {
            cnx = getConnection();
            //Inserta y deja activo el dispositivo
            PreparedStatement stm =
                cnx.prepareStatement("INSERT INTO BPI_DTE_DETTRANSEMP_TBL (COD_EMPRESA, ID_TRANSACCION, " +
                                     "MONTO_TOTAL, NUM_TRANSACCION_CAJA, NUM_TRANSACCION_MEDIO, FECHAHORA, COD_MEDIO) VALUES(?,?,?,?,?,?,?)");

            // SET PARAMETROS
            if (dto.getCodEmpresa() != null)
                stm.setInt(1, dto.getCodEmpresa());
            if (dto.getIdTransaccion() != null)
                stm.setLong(2, dto.getIdTransaccion());
            if (dto.getMontoTotal() != null)
                stm.setInt(3, dto.getMontoTotal());
            if (dto.getNumTransaccionCaja() != null)
                stm.setLong(4, dto.getNumTransaccionCaja());
            if (dto.getNumTransaccionMedio() != null)
                stm.setLong(5, dto.getNumTransaccionMedio());
            if (dto.getFechahora() != null)
                stm.setTimestamp(6, new Timestamp(dto.getFechahora().getTime()));
            if (dto.getCodMedio() != null)
                stm.setInt(7, dto.getCodMedio());

            if (dto.getCodEmpresa() == null)
                stm.setNull(1, OracleTypes.INTEGER);
            if (dto.getIdTransaccion() == null)
                stm.setNull(2, OracleTypes.NUMBER);
            if (dto.getMontoTotal() == null)
                stm.setNull(3, OracleTypes.INTEGER);
            if (dto.getNumTransaccionCaja() == null)
                stm.setNull(4, OracleTypes.NUMBER);
            if (dto.getNumTransaccionMedio() == null)
                stm.setNull(5, OracleTypes.NUMBER);
            if (dto.getFechahora() == null)
                stm.setNull(6, OracleTypes.DATE);
            if (dto.getCodMedio() == null)
                stm.setNull(7, OracleTypes.INTEGER);

            // EXECUTE
            stm.execute();
            stm.close();
            res = true;

        } catch (Exception e) {
            logger.error("insertComprobante() - catch (error)", e);
            dto = null;
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        return res;
    }

    /**
     * Busca un comprobante por su ID
     * @param id
     * @return
     */
    public BpiDnaDetnavegTbl findComprobanteByTransaccion(Long id) {
        logger.info("findComprobanteByTransaccion(" + id + ") - inicia");
        BpiDnaDetnavegTbl dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT a.ID_NAVEGACION, a.COD_PAGINA, a.FECHA_HORA,  a.MONTO_TRANSACCION, a.ENTRADA, " +
                                "a.DETALLE_PAGINA.getClobVal() " +
                                "FROM BPI_DNA_DETNAVEG_TBL a, BPI_NAV_NAVEGACION_TBL b " +
                                "WHERE b.ID_NAVEGACION = a.ID_NAVEGACION " + " AND b.ID_TRANSACCION = ? " +
                                " AND a.COD_PAGINA = ?");
            stm.setLong(1, id);
            stm.setInt(2, 5); //PAGINA 5 INDICA QUE EL COMPROBANTE ESTA PAGADO

            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new BpiDnaDetnavegTbl();
                dto.setIdNavegacion(rst.getInt("ID_NAVEGACION"));
                dto.setCod_pagina(rst.getInt("COD_PAGINA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setMontoTransaccion(rst.getDouble("MONTO_TRANSACCION"));
                dto.setEntrada(rst.getDouble("ENTRADA"));
                java.sql.Clob clb = rst.getClob(6);
                try {
                    String xml = StringUtil.clobToString(clb);
                   
                    //dto.setDetallePagina((XMLDocument) XmlUtil.parse(xml));
                    dto.setDetallePagina(XmlUtil.newParser(xml));
                    
                } catch (Exception e) {
                    logger.error("findComprobanteByTransaccion() - Exception :" + e.getMessage(), e);
                }
            }

        } catch (Exception e) {
            logger.error("findComprobanteByTransaccion() - catch (error)", e);
            dto = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findComprobanteByTransaccion() - catch (error)", f);
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
        logger.debug("findComprobanteByTransaccion() - termina");
        return dto;
    }

    /**
     * Apertura de Caja
     * @return
     * TODO: Testear
     */
    public String aperturaCaja() {
        logger.debug("aperturaCaja() - inicia");
        String dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("call BPI_MCJ_MANEJOCAJA_PKG.APERTURATURNO()");
            stm.execute();
            //stm.getResultSet();
            dto = "OK";
        } catch (Exception e) {
            logger.error("aperturaCaja() - catch (error)", e);
            dto = null;
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("aperturaCaja() - catch (error)", f);
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
        logger.debug("aperturaCaja() - termina");
        return dto;
    }

    /**
     * Cierre de Caja
     * @return
     * TODO: Testear
     */
    public String cierreCaja() {
        logger.debug("cierreCaja() - inicia");
        String dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("call BPI_MCJ_MANEJOCAJA_PKG.CIERRETURNO()");
            stm.execute();
            //stm.getResultSet();
            dto = "OK";
        } catch (Exception e) {
            logger.error("cierreCaja() - catch (error)", e);
            dto = null;
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("cierreCaja() - catch (error)", f);
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
        logger.debug("cierreCaja() - termina");
        return dto;
    }

    /**
     * Cuadratura
     * @return
     * TODO: Testear
     */
    public String cuadratura(PrcCuadraturaDto dto) {
        logger.debug("cierreCaja() - inicia");
        String resp = "NOK";
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            //Seteo de las variables del PRC
            java.util.Date fechaCuadratura = FechaUtil.toDate(dto.getP_fechacuadr(), "dd-MM-yyyy");
            logger.info("cuadratura() - Seteo de variables");
            logger.info("cuadratura() - Nombre archivo cuadratura: " + dto.getP_archivocuadr());
            logger.info("cuadratura() - Medio de Pago: " + dto.getP_mediopago().toString());
            logger.info("cuadratura() - Empresa: " + dto.getP_empresa().toString());
            logger.info("cuadratura() - Fecha de la Cuadratura: " + dto.getP_fechacuadr().toString());
            logger.info("cuadratura() - Fecha formateada de la Cuadratura: " + dto.getP_fechacuadr());
            logger.info("cuadratura() - Nombre archivo cuadratura procesado: " + dto.getP_archivocuadr() +
                        FechaUtil.getFechaFormateoCustom(fechaCuadratura, "yyyyMMdd"));

            //Recupera conexion
            cnx = getConnection();
            stm = cnx.prepareCall("call BPI_PRC_PROCESOCUADRATURA_PRC(?,?,?,?)");
            stm.setString(1, dto.getP_archivocuadr());
            stm.setInt(2, dto.getP_mediopago());
            stm.setInt(3, dto.getP_empresa());
            stm.setTimestamp(4, new Timestamp(fechaCuadratura.getTime()));
            //stm.setQueryTimeout(120);
            stm.execute();
            resp = "OK";
        } catch (Exception e) {
            logger.error("cierreCaja() - catch (error)", e);
            dto = null;
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("cierreCaja() - catch (error)", f);
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
        logger.debug("cierreCaja() - termina");
        return resp;
    }

    /**
     * Actualiza los datos de una transaccion
     * este metodo es muy utilizado por el front-end
     * @param dto
     * @return
     */
    public Boolean actualizaTransaccionSP(SPActualizarTransaccionDto dto) {
        logger.debug("actualizaTransaccionSP() - inicia");
        Boolean resp = Boolean.FALSE;
        Connection cnx = null;
        CallableStatement stm = null;
        try {
            cnx = getConnection();

            if (dto.getRegularizaWebpay() != null && dto.getRegularizaWebpay().equalsIgnoreCase("S")) {
                logger.info("actualizaTransaccionSP() -  REGULARIZACION DE PAGO WEBPAY");
                System.out.println("===> REGULARIZACION DE PAGO WEBPAY <====");
            }

            logger.info("actualizaTransaccionSP() - getIdTrx :" + dto.getIdTrx());
            logger.info("actualizaTransaccionSP() - getCodRet :" + dto.getCodRet());
            logger.info("actualizaTransaccionSP() - getNropPagos :" + dto.getNropPagos());
            logger.info("actualizaTransaccionSP() - getMontoPago :" + dto.getMontoPago());
            logger.info("actualizaTransaccionSP() - getDescRet :" + dto.getDescRet());
            logger.info("actualizaTransaccionSP() - getIdTrxBanco :" + dto.getIdTrxBanco());
            logger.info("actualizaTransaccionSP() - getIndPago :" + dto.getIndPago());
            logger.info("actualizaTransaccionSP() - getMedioCodigoRespuesta :" + dto.getMedioCodigoRespuesta());
            logger.info("actualizaTransaccionSP() - getMedioFechaPago :" + dto.getMedioFechaPago());
            logger.info("actualizaTransaccionSP() - getMedioNumeroCuotas :" + dto.getMedioNumeroCuotas());
            logger.info("actualizaTransaccionSP() - getMedioNumeroTarjeta :" + dto.getMedioNumeroTarjeta());
            logger.info("actualizaTransaccionSP() - getMedioTipoTarjeta :" + dto.getMedioTipoTarjeta());
            logger.info("actualizaTransaccionSP() - getMedioCodigoAutorizacion :" + dto.getMedioCodigoAutorizacion());
            logger.info("actualizaTransaccionSP() - getMedioOrdenCompra :" + dto.getMedioOrdenCompra());

            /*
             * Pago no realizado en el banco por lo cual se obta por registrar
             * toda la informacion necesaria del rechazo o causal del rechazo para
             * no continuar con el pago electronico.
             */
            stm =
                cnx.prepareCall("UPDATE BPI_TRA_TRANSACCIONES_TBL SET " + " OBSERVACIONES = ?, " +
                                " NUM_TRANSACCION_MEDIO = ?, " + " MEDIO_NUM_CUOTAS = ?, " +
                                " MEDIO_NUM_TARJETA = ?, " + " MEDIO_TIPO_TARJETA = ?, " + " MEDIO_FECHA_PAGO = ?, " +
                                " MEDIO_CODIGO_RESP = ?, " + " MEDIO_NUM_ORDEN_COMP = ?, " +
                                " MEDIO_CODIGO_AUTORIZA = ?, " + " WEBPAY = ? " + " WHERE ID_TRANSACCION = ?");
            // SET PARAMETROS
            if (dto.getDescRet() != null)
                stm.setString(1, dto.getDescRet());
            if (dto.getIdTrxBanco() != null)
                stm.setString(2, dto.getIdTrxBanco());
            if (dto.getMedioNumeroCuotas() != null)
                stm.setInt(3, dto.getMedioNumeroCuotas());
            if (dto.getMedioNumeroTarjeta() != null)
                stm.setString(4, dto.getMedioNumeroTarjeta());
            if (dto.getMedioTipoTarjeta() != null)
                stm.setString(5, dto.getMedioTipoTarjeta());
            if (dto.getMedioFechaPago() != null)
                stm.setTimestamp(6, new java.sql.Timestamp(dto.getMedioFechaPago().getTime()));
            if (dto.getMedioCodigoRespuesta() != null)
                stm.setString(7, dto.getMedioCodigoRespuesta());
            if (dto.getMedioOrdenCompra() != null)
                stm.setString(8, dto.getMedioOrdenCompra());
            if (dto.getMedioCodigoAutorizacion() != null)
                stm.setString(9, dto.getMedioCodigoAutorizacion());
            if (dto.getRegularizaWebpay() != null && dto.getRegularizaWebpay().equalsIgnoreCase("S"))
                stm.setString(10, "S");

            if (dto.getDescRet() == null)
                stm.setNull(1, OracleTypes.VARCHAR);
            if (dto.getIdTrxBanco() == null)
                stm.setNull(2, OracleTypes.VARCHAR);
            if (dto.getMedioNumeroCuotas() == null)
                stm.setNull(3, OracleTypes.INTEGER);
            if (dto.getMedioNumeroTarjeta() == null)
                stm.setNull(4, OracleTypes.VARCHAR);
            if (dto.getMedioTipoTarjeta() == null)
                stm.setNull(5, OracleTypes.VARCHAR);
            if (dto.getMedioFechaPago() == null)
                stm.setNull(6, OracleTypes.DATE);
            if (dto.getMedioCodigoRespuesta() == null)
                stm.setNull(7, OracleTypes.VARCHAR);
            if (dto.getMedioOrdenCompra() == null)
                stm.setNull(8, OracleTypes.VARCHAR);
            if (dto.getMedioCodigoAutorizacion() == null)
                stm.setNull(9, OracleTypes.VARCHAR);
            if (dto.getRegularizaWebpay() == null || !dto.getRegularizaWebpay().equalsIgnoreCase("S"))
                stm.setNull(10, OracleTypes.VARCHAR);

            //Numero de Transaccion PK
            stm.setLong(11, dto.getIdTrx());
            stm.execute();
            resp = Boolean.TRUE;

            //Solo los pagados siguien el proceso
            //los demas son rechazados en linea por
            //el medio de pago y se registra el detalle del problemas
            //mas el cambio de estado a 99 como rechazado por el medio de pago
            if (dto.getCodRet() == 0) { //ACEPTADO


                /*
                 * Procedimiento Almacenado que se ejecuta cuando la transaccion
                 * se realiza de forma correcta y el pago ha sido debitado
                 */
                logger.info("actualizaTransaccionSP() - ================================================================================");
                logger.info("actualizaTransaccionSP() - Ejecutando procedimiento alamacenado de Pago PLSQL : {call BPI_MCJ_MANEJOCAJA_PKG.ACTUALIZARTRANSACCION()}");
                stm = cnx.prepareCall("{call BPI_MCJ_MANEJOCAJA_PKG.ACTUALIZARTRANSACCION(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                logger.info("call BPI_MCJ_MANEJOCAJA_PKG.ACTUALIZARTRANSACCION(" + dto.getIdTrx() + "," +
                            dto.getCodRet() + "," + dto.getNropPagos() + "," + dto.getMontoPago() + "," +
                            dto.getMedioFechaPago() + "," + dto.getDescRet() + "," + dto.getIdCom() + "," +
                            dto.getIdTrxBanco() + "," + dto.getIndPago() + ")");

                stm.setLong(1, dto.getIdTrx());
                stm.setInt(2, dto.getCodRet());
                stm.setInt(3, dto.getNropPagos());
                stm.setInt(4, dto.getMontoPago());
                stm.setDate(5, new java.sql.Date(dto.getMedioFechaPago().getTime()));
                stm.setString(6, dto.getDescRet());
                stm.setLong(7, dto.getIdCom());
                stm.setLong(8, Long.parseLong(dto.getIdTrxBanco()));
                stm.setString(9, dto.getIndPago());
                stm.execute();


                //INICIALIZA PARAMETROS
                List<RESTfulParametroDto> parametros = new ArrayList<RESTfulParametroDto>();

                //ACTUALIZA VIA BASE DE DATOS
                boolean resultado = updateTransaccionRESTful(new Long("0" + dto.getIdTrx()), ESTADO_PAGADO_MEDIO_PAGO);


                //NOTIFICACION A EMPRESA DESARROLLADORA DE INTERFAZ MOVIL PARA PAGO
                String urlRESTnotifica = ResourceBundleUtil.getProperty("cl.bice.vida.restful.notifica.pago.url");
                String idOrdenCompraCanalSolicitante = getOrdenCompraServicioRESTFulByTransaccion(dto.getIdTrx());
                if (idOrdenCompraCanalSolicitante != null) {
                    parametros = new ArrayList<RESTfulParametroDto>();
                    parametros.add(new RESTfulParametroDto("idOrdenCanal", idOrdenCompraCanalSolicitante));
                    parametros.add(new RESTfulParametroDto("idTransaccion", "" + dto.getIdTrx()));
                    parametros.add(new RESTfulParametroDto("tipoPago", "ONLINE")); //ONLINE RENDICION
                    parametros.add(new RESTfulParametroDto("estadoPago", "PAGADO")); //PAGADO RECHAZADO
                    //parametros.add(new RESTfulParametroDto("claveFuenteOrigen","123456"));

                    //TODO: AQUI LA FIRMA DE LOS DATOS SEGUN CONCATENACION ACORDADA
                    String firma = firmarDatos(dto.getIdTrx() + ";" + "ONLINE" + ";" + "PAGADO" + ";" + "bicevida");
                    firma = firma.replace("\n", "");
                    parametros.add(new RESTfulParametroDto("checksum", firma));
                    System.out.println("=======================================");
                    System.out.println("idOrdenCanal :" + idOrdenCompraCanalSolicitante);
                    System.out.println("idTransaccion :" + dto.getIdTrx());
                    System.out.println("tipoPago : ONLINE");
                    System.out.println("estadoPago : PAGADO");
                    System.out.println("checksum : " + firma);

                    System.out.println("RESTfull llamando OK PAGO :" + urlRESTnotifica);
                    String jsonnotif = RESTfulCallUtil.call(urlRESTnotifica, "POST", null, null, parametros);
                    System.out.println("RESTfull resultado pago (EXITO) XML:" + jsonnotif + " FIRMA:" + firma);
                    System.out.println("=======================================");
                }

                resp = Boolean.TRUE;

            } else { //RECHAZADO
                /*
                 * Transaccion rechaza por el medio de pago
                 */
                stm = cnx.prepareCall("UPDATE BPI_TRA_TRANSACCIONES_TBL SET COD_ESTADO = ?  WHERE ID_TRANSACCION = ?");
                stm.setInt(1, ESTADO_RECHAZO_POR_MEDIO_PAGO);
                stm.setLong(2, dto.getIdTrx());
                stm.execute();

                //INICIALIZA PARAMETROS
                List<RESTfulParametroDto> parametros = new ArrayList<RESTfulParametroDto>();

                //ACTUALIZA VIA BASE DE DATOS
                boolean resultado =
                    updateTransaccionRESTful(new Long("0" + dto.getIdTrx()), ESTADO_RECHAZO_POR_MEDIO_PAGO);

                //NOTIFICACION A EMPRESA DESARROLLADORA DE INTERFAZ MOVIL PARA PAGO
                String urlRESTnotifica = ResourceBundleUtil.getProperty("cl.bice.vida.restful.notifica.pago.url");
                String idOrdenCompraCanalSolicitante = getOrdenCompraServicioRESTFulByTransaccion(dto.getIdTrx());
                if (idOrdenCompraCanalSolicitante != null) {
                    parametros = new ArrayList<RESTfulParametroDto>();
                    parametros.add(new RESTfulParametroDto("idOrdenCanal", idOrdenCompraCanalSolicitante));
                    parametros.add(new RESTfulParametroDto("idTransaccion", "" + dto.getIdTrx()));
                    parametros.add(new RESTfulParametroDto("tipoPago", "ONLINE")); //ONLINE RENDICION
                    parametros.add(new RESTfulParametroDto("estadoPago", "RECHAZADO")); //PAGADO RECHAZADO
                    //parametros.add(new RESTfulParametroDto("claveFuenteOrigen","123456"));


                    //TODO: AQUI LA FIRMA DE LOS DATOS SEGUN CONCATENACION ACORDADA
                    String firma = firmarDatos(dto.getIdTrx() + ";" + "ONLINE" + ";" + "RECHAZADO" + ";" + "bicevida");
                    parametros.add(new RESTfulParametroDto("checksum", firma));

                    System.out.println("idOrdenCanal :" + idOrdenCompraCanalSolicitante);
                    System.out.println("idTransaccion :" + dto.getIdTrx());
                    System.out.println("tipoPago : ONLINE");
                    System.out.println("idOrdenCanal :RECHAZADO");
                    System.out.println("checksum : " + firma);

                    System.out.println("RESTfull llamando RECHAZO :" + urlRESTnotifica);
                    String jsonnotif = RESTfulCallUtil.call(urlRESTnotifica, "POST", null, null, parametros);

                    System.out.println("RESTfull resultado pago (RECHAZO) XML:" + jsonnotif + " FIRMA:" + firma);
                }
                resp = Boolean.TRUE;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("actualizaTransaccionSP() - catch (error)", e);
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("actualizaTransaccionSP() - catch (error)", f);
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
        logger.debug("actualizaTransaccionSP() - termina");
        return resp;
    }
    
    public static void main(String[] args) {
        List<RESTfulParametroDto> parametros = new ArrayList<RESTfulParametroDto>();
        parametros.add(new RESTfulParametroDto("idOrdenCanal", "RX26VH5Y"));
        parametros.add(new RESTfulParametroDto("idTransaccion","1680526"));
        parametros.add(new RESTfulParametroDto("tipoPago", "ONLINE")); //ONLINE RENDICION
        parametros.add(new RESTfulParametroDto("estadoPago", "PAGADO")); //PAGADO RECHAZADO
        //parametros.add(new RESTfulParametroDto("token", "PRUEBA")); //PAGADO RECHAZADO
        
        PersistenciaGeneralDAOImpl dao = new PersistenciaGeneralDAOImpl();
        String firma = dao.firmarDatos("1680227;ONLINE;PAGADO;bicevida");
        firma = firma.replace(" ", "");
        parametros.add(new RESTfulParametroDto("checksum", firma) );
        /*
        parametros.add(new RESTfulParametroDto("checksum",
                                               "_APP_NAME_.MTY4MDIyNztPTkxJTkU7UEFHQURPO2JpY2V2aWRh.MjAxNi0xMS0xMSAxMDo1NjoyOS4xNzM=.d9e2a25b69554de7122bfa8685be4d531472c66db81eeb11830cf65c69a25703"));
        */
        //String json = RESTfulCallUtil.call("http://bicevida-ventaonline-dev.elasticbeanstalk.com/payments/notification.xml", "POST",null,null, parametros);
        /* */
        String json =
            RESTfulCallUtil.call("http://10.140.212.131:3000/payments/notification.xml", "POST", null, null, parametros);
        System.out.println(json);
        
        
    }

    /**
     * Realiza el pago en sistema de CAJAS
     * @param dto
     * @return
     */
    public Boolean pagarSPCajasPEC(SPActualizarTransaccionDto dto) {
        Boolean resp = Boolean.FALSE;
        Connection conn = null;
        CallableStatement stm = null;
        ResultSet rs = null;
        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = getConnection();


            System.out.println("pagarSPCajasPEC() - getIdTrx :" + dto.getIdTrx());
            System.out.println("pagarSPCajasPEC() - getCodRet :" + dto.getCodRet());
            System.out.println("pagarSPCajasPEC() - getNropPagos :" + dto.getNropPagos());
            System.out.println("pagarSPCajasPEC() - getMontoPago :" + dto.getMontoPago());
            System.out.println("pagarSPCajasPEC() - getDescRet :" + dto.getDescRet());
            System.out.println("pagarSPCajasPEC() - getIdTrxBanco :" + dto.getIdTrxBanco());
            System.out.println("pagarSPCajasPEC() - getIndPago :" + dto.getIndPago());
            System.out.println("pagarSPCajasPEC() - getMedioCodigoRespuesta :" + dto.getMedioCodigoRespuesta());
            System.out.println("pagarSPCajasPEC() - getMedioFechaPago :" + dto.getMedioFechaPago());
            System.out.println("pagarSPCajasPEC() - getMedioNumeroCuotas :" + dto.getMedioNumeroCuotas());
            System.out.println("pagarSPCajasPEC() - getMedioNumeroTarjeta :" + dto.getMedioNumeroTarjeta());
            System.out.println("pagarSPCajasPEC() - getMedioTipoTarjeta :" + dto.getMedioTipoTarjeta());
            System.out.println("pagarSPCajasPEC() - getMedioCodigoAutorizacion :" + dto.getMedioCodigoAutorizacion());
            System.out.println("pagarSPCajasPEC() - getMedioOrdenCompra :" + dto.getMedioOrdenCompra());

            /*
             * Pago no realizado en el banco por lo cual se obta por registrar
             * toda la informacion necesaria del rechazo o causal del rechazo para
             * no continuar con el pago electronico.
             */
            stm =
                conn.prepareCall("UPDATE BPI_TRA_TRANSACCIONES_TBL SET " + " OBSERVACIONES = ?, " +
                                 " NUM_TRANSACCION_MEDIO = ?, " + " MEDIO_NUM_CUOTAS = ?, " +
                                 " MEDIO_NUM_TARJETA = ?, " + " MEDIO_TIPO_TARJETA = ?, " + " MEDIO_FECHA_PAGO = ?, " +
                                 " MEDIO_CODIGO_RESP = ?, " + " MEDIO_NUM_ORDEN_COMP = ?, " +
                                 " MEDIO_CODIGO_AUTORIZA = ?, " + " WEBPAY = ? " + " WHERE ID_TRANSACCION = ?");
            // SET PARAMETROS
            if (dto.getDescRet() != null)
                stm.setString(1, dto.getDescRet());
            if (dto.getIdTrxBanco() != null)
                stm.setString(2, dto.getIdTrxBanco());
            if (dto.getMedioNumeroCuotas() != null)
                stm.setInt(3, dto.getMedioNumeroCuotas());
            if (dto.getMedioNumeroTarjeta() != null)
                stm.setString(4, dto.getMedioNumeroTarjeta());
            if (dto.getMedioTipoTarjeta() != null)
                stm.setString(5, dto.getMedioTipoTarjeta());
            if (dto.getMedioFechaPago() != null)
                stm.setTimestamp(6, new java.sql.Timestamp(dto.getMedioFechaPago().getTime()));
            if (dto.getMedioCodigoRespuesta() != null)
                stm.setString(7, dto.getMedioCodigoRespuesta());
            if (dto.getMedioOrdenCompra() != null)
                stm.setString(8, dto.getMedioOrdenCompra());
            if (dto.getMedioCodigoAutorizacion() != null)
                stm.setString(9, dto.getMedioCodigoAutorizacion());
            if (dto.getRegularizaWebpay() != null && dto.getRegularizaWebpay().equalsIgnoreCase("S"))
                stm.setString(10, "S");

            if (dto.getDescRet() == null)
                stm.setNull(1, OracleTypes.VARCHAR);
            if (dto.getIdTrxBanco() == null)
                stm.setNull(2, OracleTypes.VARCHAR);
            if (dto.getMedioNumeroCuotas() == null)
                stm.setNull(3, OracleTypes.INTEGER);
            if (dto.getMedioNumeroTarjeta() == null)
                stm.setNull(4, OracleTypes.VARCHAR);
            if (dto.getMedioTipoTarjeta() == null)
                stm.setNull(5, OracleTypes.VARCHAR);
            if (dto.getMedioFechaPago() == null)
                stm.setNull(6, OracleTypes.DATE);
            if (dto.getMedioCodigoRespuesta() == null)
                stm.setNull(7, OracleTypes.VARCHAR);
            if (dto.getMedioOrdenCompra() == null)
                stm.setNull(8, OracleTypes.VARCHAR);
            if (dto.getMedioCodigoAutorizacion() == null)
                stm.setNull(9, OracleTypes.VARCHAR);
            if (dto.getRegularizaWebpay() == null || !dto.getRegularizaWebpay().equalsIgnoreCase("S"))
                stm.setNull(10, OracleTypes.VARCHAR);

            //Numero de Transaccion PK
            stm.setLong(11, dto.getIdTrx());
            stm.execute();
            resp = Boolean.TRUE;

            //Solo los pagados siguien el proceso
            //los demas son rechazados en linea por
            //el medio de pago y se registra el detalle del problemas
            //mas el cambio de estado a 99 como rechazado por el medio de pago
            if (dto.getCodRet() == 0) { //ACEPTADO


                /*
                 * Procedimiento Almacenado que se ejecuta cuando la transaccion
                 * se realiza de forma correcta y el pago ha sido debitado
                 */
                System.out.println("pagarSPCajasPEC() - ================================================================================");
                System.out.println("pagarSPCajasPEC() - Ejecutando procedimiento alamacenado de Pago PLSQL : {call BPI_MCJ_MANEJOCAJA_PKG.ActualizarTransaccion_PEC()}");
                stm =
                    conn.prepareCall("{call BPI_MCJ_MANEJOCAJA_PKG.ActualizarTransaccion_PEC(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                System.out.println("call BPI_MCJ_MANEJOCAJA_PKG.ActualizarTransaccion_PEC(" + dto.getIdTrx() + "," +
                                   dto.getCodRet() + "," + dto.getNropPagos() + "," + dto.getMontoPago() + "," +
                                   dto.getMedioFechaPago() + "," + dto.getDescRet() + "," + dto.getIdCom() + "," +
                                   dto.getIdTrxBanco() + "," + dto.getIndPago() + ",'" + dto.getInstrumentoCaja() +
                                   "')");

                stm.setLong(1, dto.getIdTrx());
                stm.setInt(2, dto.getCodRet());
                stm.setInt(3, dto.getNropPagos());
                stm.setInt(4, dto.getMontoPago());
                stm.setDate(5, new java.sql.Date(dto.getMedioFechaPago().getTime()));
                stm.setString(6, dto.getDescRet());
                stm.setLong(7, dto.getIdCom());
                stm.setLong(8, Long.parseLong(dto.getIdTrxBanco()));
                stm.setString(9, dto.getIndPago());
                stm.setString(10, dto.getInstrumentoCaja());
                stm.execute();


                resp = Boolean.TRUE;

            } else { //RECHAZADO
                /*
                 * Transaccion rechaza por el medio de pago
                 */
                stm = conn.prepareCall("UPDATE BPI_TRA_TRANSACCIONES_TBL SET COD_ESTADO = ?  WHERE ID_TRANSACCION = ?");
                stm.setInt(1, 99);
                stm.setLong(2, dto.getIdTrx());
                stm.execute();


                resp = Boolean.TRUE;
            }

        } catch (Exception _e) {
            _e.printStackTrace();
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
        return resp;
    }


    /**
     * Firmar Datos
     * @param data
     * @return
     */
    //
    private String firmarDatos(String datos) {

        //DEFINIR COMO FIRMARAN ELLOS PARA TENER EL MISMO DATO
        String clave = "123456";
        String app = "_APP_NAME_";


        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String string = app + "." + getBase64String(datos) + "." + clave + "." + getBase64String(timestamp.toString());
        String string_send = app + "." + getBase64String(datos) + "." + getBase64String(timestamp.toString());

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        md.update(string.getBytes());

        byte[] byteData;
        byteData = md.digest();

        string_send = string_send + "." + getHexFormat(byteData);
        return string_send;
    }

    /**
     * Base 64 a String
     * @param string
     * @return
     */
    private String getBase64String(String string) {
        String encoded = null;
        try {
            encoded = new String(encode(string.getBytes()));
            encoded = encoded.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoded;
    }

    public static byte[] encode(byte[] b) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream b64os = MimeUtility.encode(baos, "base64");
        b64os.write(b);
        b64os.close();
        return baos.toByteArray();
    }

    public static byte[] decode(byte[] b) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        InputStream b64is = MimeUtility.decode(bais, "base64");
        byte[] tmp = new byte[b.length];
        int n = b64is.read(tmp);
        byte[] res = new byte[n];
        System.arraycopy(tmp, 0, res, 0, n);
        return res;
    }

    /**
     * Formateo a hexadecimal
     * @param byteData
     * @return
     */
    private String getHexFormat(byte[] byteData) {
        //convert the byte to hex format
        StringBuffer sb;
        sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


    /**
     * Recuper el orden de compra segun id transaccion
     * @param idTransaccion
     * @return
     */
    public String getOrdenCompraServicioRESTFulByTransaccion(Long idTransaccion) {
        logger.info("getOrdenCompraServicioRESTFulByTransaccion(" + idTransaccion + ") - inicia");
        String resp = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("SELECT ID_ORDEN_CANAL FROM BPI_TRA_RESTFULL_SERV WHERE ID_TRANSACCION = ?");
            stm.setLong(1, idTransaccion);
            rst = stm.executeQuery();
            if (rst.next()) {
                resp = rst.getString("ID_ORDEN_CANAL");
            }

        } catch (Exception e) {
            logger.error("getOrdenCompraServicioRESTFulByTransaccion() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("getOrdenCompraServicioRESTFulByTransaccion() - catch (error)", f);
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
        logger.debug("getOrdenCompraServicioRESTFulByTransaccion() - termina");
        return resp;
    }

    /**
     * Actualiza los datos de una transaccion
     * este metodo es muy utilizado por el front-end
     * @param dto
     * @return
     */
    public Boolean actualizaTransaccionSPByFechaPago(java.util.Date fechaPago, SPActualizarTransaccionDto dto) {
        logger.debug("actualizaTransaccionSPByFechaPago() - inicia");
        Boolean resp = Boolean.FALSE;
        Connection cnx = null;
        CallableStatement stm = null;
        try {
            cnx = getConnection();
            logger.info("actualizaTransaccionSPByFechaPago() - getIdTrx :" + dto.getIdTrx());
            logger.info("actualizaTransaccionSPByFechaPago() - getCodRet :" + dto.getCodRet());
            logger.info("actualizaTransaccionSPByFechaPago() - getNropPagos :" + dto.getNropPagos());
            logger.info("actualizaTransaccionSPByFechaPago() - getMontoPago :" + dto.getMontoPago());
            logger.info("actualizaTransaccionSPByFechaPago() - getDescRet :" + dto.getDescRet());
            logger.info("actualizaTransaccionSPByFechaPago() - getIdTrxBanco :" + dto.getIdTrxBanco());
            logger.info("actualizaTransaccionSPByFechaPago() - getIndPago :" + dto.getIndPago());
            logger.info("actualizaTransaccionSPByFechaPago() - getMedioCodigoRespuesta :" +
                        dto.getMedioCodigoRespuesta());
            logger.info("actualizaTransaccionSPByFechaPago() - getMedioFechaPago :" + dto.getMedioFechaPago());
            logger.info("actualizaTransaccionSPByFechaPago() - getMedioNumeroCuotas :" + dto.getMedioNumeroCuotas());
            logger.info("actualizaTransaccionSPByFechaPago() - getMedioNumeroTarjeta :" + dto.getMedioNumeroTarjeta());
            logger.info("actualizaTransaccionSPByFechaPago() - getMedioTipoTarjeta :" + dto.getMedioTipoTarjeta());
            logger.info("actualizaTransaccionSPByFechaPago() - getMedioCodigoAutorizacion :" +
                        dto.getMedioCodigoAutorizacion());
            logger.info("actualizaTransaccionSPByFechaPago() - getMedioOrdenCompra :" + dto.getMedioOrdenCompra());

            /*
             * Pago no realizado en el banco por lo cual se obta por registrar
             * toda la informacion necesaria del rechazo o causal del rechazo para
             * no continuar con el pago electronico.
             */
            stm =
                cnx.prepareCall("UPDATE BPI_TRA_TRANSACCIONES_TBL SET " + " OBSERVACIONES = ?, " +
                                " NUM_TRANSACCION_MEDIO = ?, " + " MEDIO_NUM_CUOTAS = ?, " +
                                " MEDIO_NUM_TARJETA = ?, " + " MEDIO_TIPO_TARJETA = ?, " + " MEDIO_FECHA_PAGO = ?, " +
                                " MEDIO_CODIGO_RESP = ?, " + " MEDIO_NUM_ORDEN_COMP = ?, " +
                                " MEDIO_CODIGO_AUTORIZA = ? " + " WHERE ID_TRANSACCION = ?");
            // SET PARAMETROS
            if (dto.getDescRet() != null)
                stm.setString(1, dto.getDescRet());
            if (dto.getIdTrxBanco() != null)
                stm.setString(2, dto.getIdTrxBanco());
            if (dto.getMedioNumeroCuotas() != null)
                stm.setInt(3, dto.getMedioNumeroCuotas());
            if (dto.getMedioNumeroTarjeta() != null)
                stm.setString(4, dto.getMedioNumeroTarjeta());
            if (dto.getMedioTipoTarjeta() != null)
                stm.setString(5, dto.getMedioTipoTarjeta());
            if (dto.getMedioFechaPago() != null)
                stm.setTimestamp(6, new java.sql.Timestamp(dto.getMedioFechaPago().getTime()));
            if (dto.getMedioCodigoRespuesta() != null)
                stm.setString(7, dto.getMedioCodigoRespuesta());
            if (dto.getMedioOrdenCompra() != null)
                stm.setString(8, dto.getMedioOrdenCompra());
            if (dto.getMedioCodigoAutorizacion() != null)
                stm.setString(9, dto.getMedioCodigoAutorizacion());

            if (dto.getDescRet() == null)
                stm.setNull(1, OracleTypes.VARCHAR);
            if (dto.getIdTrxBanco() == null)
                stm.setNull(2, OracleTypes.VARCHAR);
            if (dto.getMedioNumeroCuotas() == null)
                stm.setNull(3, OracleTypes.INTEGER);
            if (dto.getMedioNumeroTarjeta() == null)
                stm.setNull(4, OracleTypes.VARCHAR);
            if (dto.getMedioTipoTarjeta() == null)
                stm.setNull(5, OracleTypes.VARCHAR);
            if (dto.getMedioFechaPago() == null)
                stm.setNull(6, OracleTypes.DATE);
            if (dto.getMedioCodigoRespuesta() == null)
                stm.setNull(7, OracleTypes.VARCHAR);
            if (dto.getMedioOrdenCompra() == null)
                stm.setNull(8, OracleTypes.VARCHAR);
            if (dto.getMedioCodigoAutorizacion() == null)
                stm.setNull(9, OracleTypes.VARCHAR);

            //Numero de Transaccion PK
            stm.setLong(10, dto.getIdTrx());
            stm.execute();
            resp = Boolean.TRUE;

            //Solo los pagados siguien el proceso
            //los demas son rechazados en linea por
            //el medio de pago y se registra el detalle del problemas
            //mas el cambio de estado a 99 como rechazado por el medio de pago
            if (dto.getCodRet() == 0) {
                /*
                 * Procedimiento Almacenado que se ejecuta cuando la transaccion
                 * se realiza de forma correcta y el pago ha sido debitado
                 */
                logger.info("actualizaTransaccionSPByFechaPago() - (Cuadratura) Ejecutando procedimiento alamacenado de Pago PLSQL : {call BPI_MCJ_MANEJOCAJA_PKG.ActualizarTransaccionByFecha()}");
                stm =
                    cnx.prepareCall("{call BPI_MCJ_MANEJOCAJA_PKG.ActualizarTransaccionByFecha(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                stm.setLong(1, dto.getIdTrx());
                stm.setInt(2, dto.getCodRet());
                stm.setInt(3, dto.getNropPagos());
                stm.setInt(4, dto.getMontoPago());
                stm.setString(5, dto.getDescRet());
                stm.setLong(6, dto.getIdCom());
                stm.setLong(7, Long.parseLong(dto.getIdTrxBanco()));
                stm.setString(8, dto.getIndPago());
                stm.setDate(9, new java.sql.Date(fechaPago.getTime()));
                stm.execute();
                resp = Boolean.TRUE;
            } else {
                /*
                 * Transaccion rechaza por el medio de pago
                 */
                stm = cnx.prepareCall("UPDATE BPI_TRA_TRANSACCIONES_TBL SET COD_ESTADO = ?  WHERE ID_TRANSACCION = ?");
                stm.setInt(1, ESTADO_RECHAZO_POR_MEDIO_PAGO);
                stm.setLong(2, dto.getIdTrx());
                stm.execute();
                resp = Boolean.TRUE;
            }

        } catch (Exception e) {
            logger.error("actualizaTransaccionSPByFechaPago() - catch (error)", e);
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("actualizaTransaccionSPByFechaPago() - catch (error)", f);
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
        logger.debug("actualizaTransaccionSPByFechaPago() - termina");
        return resp;
    }

    /**
     * Actualiza el estado de una transaccion
     * @param idtransaccion
     * @param cod_estado
     * @return
     */
    public Boolean updateEstadoTransaccion(Integer idtransaccion, Integer cod_estado) {
        logger.info("updateEstadoTransaccion(idtransaccion[" + idtransaccion + " - cod_estado[" + cod_estado +
                    "]) - iniciando");
        Boolean resp = Boolean.FALSE;
        Transacciones dto = findTransaccionById(idtransaccion);
        logger.debug("updateEstadoTransaccion() - consulta de informacion obtenido, procediendo a actualizar el dato");
        Connection cnx = null;
        try {

            if (dto != null) {
                //Setea nuevos valores
                dto.setCod_estado(cod_estado);

                cnx = getConnection();
                PreparedStatement stm =
                    cnx.prepareStatement("UPDATE BPI_TRA_TRANSACCIONES_TBL SET COD_ESTADO = ? WHERE ID_TRANSACCION = ? ");

                // SET PARAMETROS
                stm.setInt(1, dto.getCod_estado());
                stm.setLong(2, dto.getIdTransaccion());

                logger.debug("updateEstadoTransaccion() - getCod_estado :" + dto.getCod_estado());
                logger.debug("updateEstadoTransaccion() - getIdTransaccion :" + dto.getIdTransaccion());

                // EXECUTE
                stm.execute();
                stm.close();

                resp = Boolean.TRUE;
            }
        } catch (Exception e) {
            logger.error("updateEstadoTransaccion() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("updateEstadoTransaccion() - terminado");
        return resp;
    }

    /**
     * Actualiza el Codigo del banco si es pagado v�a 2 medios de pago
     * @param idtransaccion
     * @param cod_banco
     * @return
     */
    public Boolean updateCodBancoTransaccion(Integer idtransaccion, Integer cod_banco) {
        logger.info("updateEstadoTransaccion(idtransaccion[" + idtransaccion + "] - cod_banco[" + cod_banco +
                    "]) - iniciando");
        Boolean resp = Boolean.FALSE;
        Transacciones dto = findTransaccionById(idtransaccion);
        logger.debug("updateEstadoTransaccion() - consulta de informacion obtenido, procediendo a actualizar el dato");
        Connection cnx = null;
        try {

            if (dto != null) {
                //Setea nuevos valores
                dto.setCod_banco2MedioPago(cod_banco);

                cnx = getConnection();
                PreparedStatement stm =
                    cnx.prepareStatement("UPDATE BPI_TRA_TRANSACCIONES_TBL SET COD_BANCO2MEDIO = ? WHERE ID_TRANSACCION = ? ");

                // SET PARAMETROS
                stm.setInt(1, dto.getCod_banco2MedioPago());
                stm.setLong(2, dto.getIdTransaccion());

                logger.debug("updateEstadoTransaccion() - getCod_banco2MedioPago :" + dto.getCod_banco2MedioPago());
                logger.debug("updateEstadoTransaccion() - getIdTransaccion :" + dto.getIdTransaccion());

                // EXECUTE
                stm.execute();
                stm.close();

                resp = Boolean.TRUE;
            }
        } catch (Exception e) {
            logger.error("updateEstadoTransaccion() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("updateEstadoTransaccion() - terminado");
        return resp;
    }

    /**
     * Busca Transacciones por Empresa
     * @param fechaini fecha de inicio
     * @param fechafin fecha de termino
     * @return lista de transacciones por empresa
     */
    public List<TransaccionByEmpresa> findTransaccionesByEmpresa(Date fechaini, Date fechafin) {
        logger.debug("findTransaccionesByEmpresa() - inicia");
        List dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT emp.nombre, d.cod_empresa, d.cod_empresa as id_transaccion, COUNT (*) as num_transaccion_medio, SUM (d.monto_total) as monto_total " +
                                " FROM bicevida.bpi_dte_dettransemp_tbl d, bicevida.bpi_tra_transacciones_tbl t, bpi_emp_empresas_tbl emp " +
                                " WHERE t.turno BETWEEN ? and ? " + " AND emp.cod_empresa = d.cod_empresa " +
                                " AND d.id_transaccion = t.id_transaccion and t.cod_estado in (3,6,7) " +
                                " GROUP BY emp.nombre, d.cod_empresa");
            stm.setTimestamp(1, new Timestamp(fechaini.getTime()));
            stm.setTimestamp(2, new Timestamp(fechafin.getTime()));
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
            logger.error("findTransaccionesByEmpresa() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesByEmpresa() - catch (error)", f);
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
        logger.debug("findTransaccionesByEmpresa() - termina");
        return dtos;
    }

    /**
     * Consulta transacciones segun
     * medio sd epago y empresa en un
     * determinado rango de fechas
     * @param id
     * @param fechaini
     * @param fechafin
     * @return
     */
    public List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresa(Integer id, Date fechaini, Date fechafin) {
        logger.debug("findTransaccionesMedioByEmpresa() - inicia");
        List dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        String filtro = "";

        //String fechainiciotmp = FechaUtil.getFechaFormateoCustom(fechaini, "dd/MM/yyyy hh:mm:ss");
        //String fechatermintmp = FechaUtil.getFechaFormateoCustom(fechafin, "dd/MM/yyyy hh:mm:ss");
        /*if(id.intValue() == 2){
            filtro = "AND t.cod_medio not in (4) \n";
        }else{
            filtro = "";
        }*/


        try {
            //Consulta todos los medios de pago existentes
            List allmed = findAllMediosdePagoElectronico(id);

            //Consulta la informacion
            cnx = getConnection();
            String sql =
                "SELECT t.cod_medio as id_transaccion, t.cod_medio, COUNT (*) as num_transaccion_medio,  SUM (t.monto_total) as monto_total\n" +
                "                     FROM bicevida.bpi_tra_transacciones_tbl t  , bicevida.bpi_dte_dettransemp_tbl d\n" +
                "                     WHERE d.id_transaccion = t.id_transaccion\n" +
                "                     and t.cod_estado in (3,6,7)  \n" +
                "                     AND t.turno BETWEEN ? and ? \n" + "                     AND d.cod_empresa = ?\n" +
                "                     " + filtro + "                     GROUP BY t.cod_medio \n" +
                "                     ORDER BY t.cod_medio";

            stm = cnx.prepareCall(sql);

            stm.setTimestamp(1, new Timestamp(fechaini.getTime()));
            stm.setTimestamp(2, new Timestamp(fechafin.getTime()));
            stm.setInt(3, id);

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
                if (dto.getMedio() != null)
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
            logger.error("findTransaccionesMedioByEmpresa() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findTransaccionesMedioByEmpresa() - catch (error)", f);
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
        logger.debug("findTransaccionesMedioByEmpresa() - termina");
        return dtos;
    }

    /**
     * Busca la ultima recudacion por empresa
     * y segun fecha de consulta
     * @param fecha
     * @return
     */
    public List<RecaudacionByEmpresa> findLastRecaudacionByEmpresa(Date fecha) {
        logger.debug("findLastRecaudacionByEmpresa() - inicia");
        List dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT cod_empresa, SUM(monto_total) as monto_total, SUM(transacciones) as transacciones,  SUM (id_cuadratura) as id_cuadratura " +
                                "FROM bicevida.bpi_cua_cuadratura_tbl " + "WHERE fecha_hora >= ? " +
                                "GROUP BY cod_empresa");
            stm.setDate(1, new java.sql.Date(fecha.getTime()));
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
            logger.error("findLastRecaudacionByEmpresa() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionByEmpresa() - catch (error)", f);
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
        logger.debug("findLastRecaudacionByEmpresa() - termina");
        return dtos;
    }

    /**
     * Consulta lista de ultima recudacion por
     * medio de pago y empresa segun fecha
     * @param id
     * @param fecha
     * @return
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresa(Integer id, Date fecha) {
        logger.info("findLastRecaudacionMedioByEmpresa(id[" + id + "] - fecha[" + fecha + "]) - inicia");
        List<RecaudacionMedioByEmpresa> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            //Consulta todos los medios de pago existentes
            List allmed = findAllMediosVisibles();

            //Consulta la informacion
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT a.FECHA_HORA, a.COD_EMPRESA, a.COD_MEDIO, a.ID_CUADRATURA, a.MONTO_TOTAL, " +
                                " a.MONTO_INFORMADO, a.TRANSACCIONES, b.NOMBRE " +
                                " FROM BPI_CUA_CUADRATURA_TBL a, BPI_MPG_MEDIOPAGO_TBL b" + " WHERE a.COD_EMRESA = ? " +
                                "   AND a.FECHA_HORA >= ?" + "   AND b.COD_MEDIO = a.COD_MEDIO");
            stm.setInt(1, id);
            stm.setDate(2, new java.sql.Date(fecha.getTime()));
            rst = stm.executeQuery();
            while (rst.next()) {
                RecaudacionMedioByEmpresa dto = new RecaudacionMedioByEmpresa();
                dto.setCod_empresa(rst.getInt("COD_EMPRESA"));
                dto.setCod_medio(rst.getInt("COD_MEDIO"));
                dto.setFecha(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setId_cuadratura(rst.getInt("ID_CUADRATURA"));
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
                    dtorec.setFecha(fecha);
                    dtorec.setId_cuadratura(new Integer(0));
                    dtorec.setMonto_total(new Long(0));
                    dtorec.setNombre_medio(medio.getNombre());
                    dtorec.setNum_transacciones(new Integer(0));
                    dtos.add(dtorec);
                }
            }
        } catch (Exception e) {
            logger.error("findLastRecaudacionMedioByEmpresa() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findLastRecaudacionMedioByEmpresa() - catch (error)", f);
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
        logger.debug("findLastRecaudacionMedioByEmpresa() - termina");
        return dtos;

    }

    /**
     * Consulta el Ultimo dia de Cuadratura
     * @return
     * TODO: Testear
     */
    public Date findUltimoDiaCuadratura() {
        logger.debug("findUltimoDiaCuadratura() - inicia");
        Date resp = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT MAX(FECHA_HORA) AS FECHA_HORA, 1 AS ID_CUADRATURA FROM BPI_CUA_CUADRATURA_TBL");
            rst = stm.executeQuery();
            while (rst.next()) {
                resp = FechaUtil.toUtilDate(rst.getDate("FECHA_HORA"));
            }

        } catch (Exception e) {
            logger.error("findUltimoDiaCuadratura() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findUltimoDiaCuadratura() - catch (error)", f);
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
        logger.debug("findUltimoDiaCuadratura() - termina");
        return resp;
    }


    /**
     * Crea una nueva cartola
     * @param cartola_id
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param empresa
     * @return
     */
    public Boolean newCartola(Long cartola_id, Integer rut, Date fechaini, Date fechafin, int empresa, String xml) {
        logger.debug("newCartola() - iniciando ");
        Connection cnx = null;
        Boolean resp = Boolean.FALSE;
        try {

            //Recupera conexion
            cnx = getConnection();
            XMLType xmlType = XMLType.createXML(cnx, xml);
            Clob xmlClob = xmlType.getClobVal();

            //Inserta y deja activo el dispositivo
            PreparedStatement stm =
                cnx.prepareStatement("INSERT INTO BPI_CAR_CARTOLAS_TBL " +
                                     "(ID_CARTOLA, RUT_PERSONA, FECHA_HORA, PERIODO, EMPRESA, DETALLE_PAGINA) " +
                                     "VALUES(?,?,?,?,?,XMLType(?))");

            // SET PARAMETROS
            stm.setLong(1, cartola_id);
            stm.setInt(2, rut);
            stm.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stm.setString(4,
                          FechaUtil.getFechaFormateoCustom(fechaini, "dd/MM/yyyy") + " al " +
                          FechaUtil.getFechaFormateoCustom(fechafin, "dd/MM/yyyy"));
            stm.setInt(5, empresa);
            stm.setClob(6, xmlClob);

            // EXECUTE
            stm.execute();
            stm.close();
            resp = Boolean.TRUE;
        } catch (Exception e) {
            logger.error("newCartola() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("newCartola() - terminado");
        return resp;
    }

    /**
     * Recupera el XML en base al codigo de pagina
     * que se defina segun el flujo de pago
     * @param id
     * @param pagina
     * @return
     */
    public BpiDnaDetnavegTbl findComprobanteByTransaccion(Long id, Integer pagina) {
        logger.info("findComprobanteByTransaccion(id[" + id + "] - pagina[" + pagina + "]) - inicia");
        BpiDnaDetnavegTbl dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("SELECT a.ID_NAVEGACION, a.COD_PAGINA, a.FECHA_HORA,  a.MONTO_TRANSACCION, a.ENTRADA, " +
                                "a.DETALLE_PAGINA.getClobVal() " +
                                "FROM BPI_DNA_DETNAVEG_TBL a, BPI_NAV_NAVEGACION_TBL b " +
                                "WHERE b.ID_NAVEGACION = a.ID_NAVEGACION " + " AND b.ID_TRANSACCION = ? " +
                                " AND a.COD_PAGINA = ?");
            stm.setLong(1, id);
            stm.setInt(2, pagina);

            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new BpiDnaDetnavegTbl();
                dto.setIdNavegacion(rst.getInt("ID_NAVEGACION"));
                dto.setCod_pagina(rst.getInt("COD_PAGINA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setMontoTransaccion(rst.getDouble("MONTO_TRANSACCION"));
                dto.setEntrada(rst.getDouble("ENTRADA"));
                java.sql.Clob clb = rst.getClob(6);
                try {
                    String xml = StringUtil.clobToString(clb);
                    dto.setDetallePagina((XMLDocument) XmlUtil.parse(xml));
                } catch (Exception e) {
                    logger.error("findComprobanteByTransaccion() - ParserConfigurationException :" + e.getMessage(), e);
                }
            }

        } catch (Exception e) {
            logger.error("findComprobanteByTransaccion() - catch (error)", e);
            dto = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findComprobanteByTransaccion() - catch (error)", f);
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
        logger.debug("findComprobanteByTransaccion() - termina");
        return dto;
    }


    public DetalleAPV getDetalleProductoAPV(String poliza) {
        logger.info("getRegimenTributarioAPV(poliza[" + poliza + "]) - inicia");
        DetalleAPV data = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;

        //Timestamp time = new Timestamp(new Date().getTime());

        try {
            cnx = getConnection();
            stm =
                cnx.prepareCall("select\n" + "contrato_plan_apvc.numero_poliza,\n" +
                                "contrato_plan_apvc.nombre_plan,\n" + "tipo_regimen_trib.descripcion\n" + "from\n" +
                                "apvc_ahorro.contrato_plan_apvc, apvc_ahorro.adhesion, apvc_ahorro.cuenta_plan_trab, apvc_ahorro.tipo_regimen_trib\n" +
                                "where\n" + "contrato_plan_apvc.numero_poliza = ? and\n" +
                                "contrato_plan_apvc.codigo_prefijo_poliza = 0 and\n" +
                                "contrato_plan_apvc.id_contrato = adhesion.id_contrato and\n" +
                                "adhesion.id_adhesion = cuenta_plan_trab.id_adhesion and\n" +
                                "cuenta_plan_trab.tipo_reg_trib_v = tipo_regimen_trib.tipo_reg_trib");

            stm.setString(1, poliza);
            rst = stm.executeQuery();
            while (rst.next()) {
                data = new DetalleAPV();
                data.setPoliza(rst.getString("numero_poliza"));
                data.setDescripcionProducto(rst.getString("nombre_plan"));
                data.setRegimenTributario(rst.getString("descripcion"));
            }

        } catch (Exception e) {
            logger.error("getRegimenTributarioAPV() - catch (error)", e);
            data = null;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findComprobanteByTransaccion() - catch (error)", f);
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
        logger.debug("findComprobanteByTransaccion() - termina");
        return data;
    }

    public boolean updateEstadoPolizaAPV(Long rut_trabajador, Long numero_poliza, Long ramo, Long numero_recibo,
                                         Long secuencia) {
        logger.info("updateEstadoPolizaAPV(rut_trabajador[" + rut_trabajador + "] - ramo[" + ramo +
                    "] - numero_recibo[" + numero_recibo + "] - secuencia[" + secuencia + "]) - iniciando");
        boolean ret = false;
        Connection cnx = null;

        String sql =
            "update apvc_ahorro.ahorro_intereses set estado_recibo = 'PEN' \n" + "where rut_trabajador = ?\n" +
            "and numero_poliza = ?\n" + "and ramo = ?\n" + "and numero_recibo = ?\n" + "and secuencia = ?";

        try {

            cnx = getConnection();
            PreparedStatement stm = cnx.prepareStatement(sql);
            stm.setLong(1, rut_trabajador.longValue());
            stm.setLong(2, numero_poliza.longValue());
            stm.setLong(3, ramo.longValue());
            stm.setLong(4, numero_recibo.longValue());
            stm.setLong(5, secuencia.longValue());

            // EXECUTE
            stm.execute();
            stm.close();
            ret = true;

        } catch (Exception e) {
            logger.error("updateEstadoPolizaAPV() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                logger.error("updateEstadoPolizaAPV() - catch (error)", e);
            }
        }
        logger.debug("updateEstadoPolizaAPV() - terminado");

        return ret;
    }


    public int getValorMaximoAporteExtraAPV() {
        logger.debug("getValorMaximoAporteExtraAPV() - inicia");
        int data = 5000000;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;

        String sql = "SELECT * FROM BPI_PAA_PARAMETROAPVAPT_TBL WHERE NOMBRE = 'MONTOMAXAPV'";

        try {
            cnx = getConnection();
            stm = cnx.prepareCall(sql);
            rst = stm.executeQuery();
            while (rst.next()) {
                String datastr = rst.getString("VALOR");
                if (datastr != null) {
                    data = Integer.parseInt(datastr);
                }
            }

        } catch (Exception e) {
            logger.error("getValorMaximoAporteExtraAPV() - catch (error)", e);
            data = 5000000;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("getValorMaximoAporteExtraAPV() - catch (error)", f);
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
        logger.debug("getValorMaximoAporteExtraAPV() - termina");
        return data;
    }

    public double getPorcentajeCobroTarjetaAPVAPT() {
        logger.debug("getPorcentajeCobroTarjetaAPVAPT() - inicia");
        double data = 0.0;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;

        String sql = "SELECT * FROM BPI_PAA_PARAMETROAPVAPT_TBL WHERE NOMBRE = 'COBROTARJETA'";

        try {
            cnx = getConnection();
            stm = cnx.prepareCall(sql);
            rst = stm.executeQuery();
            while (rst.next()) {
                String datastr = rst.getString("VALOR");
                if (datastr != null) {
                    data = Double.parseDouble(datastr);
                }
            }

        } catch (Exception e) {
            logger.error("getPorcentajeCobroTarjetaAPVAPT() - catch (error)", e);
            data = 0.0;
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("getPorcentajeCobroTarjetaAPVAPT() - catch (error)", f);
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
        logger.debug("getPorcentajeCobroTarjetaAPVAPT() - termina");
        return data;
    }


    public DetalleAPV generarIdAporteExtraordinarioAPV(DetalleAPV detalle) {
        logger.debug("generarAporteExtraordinarioAPV() - inicia");
        DetalleAPV data = null;
        Connection cnx = null;
        CallableStatement stm = null;

        String sqlpkg = "{call UCAJAS.APV_PKG.GENERA_ID_APORTACION(?, ?, ?, ?, ?, ?)}";

        try {
            cnx = getConnection();
            stm = cnx.prepareCall(sqlpkg);
            stm.setLong(1, detalle.getNumeroramo());
            stm.setLong(2, detalle.getNumeropoliza());
            stm.setLong(3, detalle.getRut());
            stm.setLong(4, detalle.getMonto());
            stm.setString(5, detalle.getUsuarioconsulta());
            stm.registerOutParameter(6, Types.NUMERIC);

            stm.execute();
            Long idiaporte = stm.getLong(6);
            if (idiaporte.longValue() > 0) {
                detalle.setIdaporte(idiaporte);
            } else {
                detalle = null;
            }
        } catch (Exception e) {
            logger.error("generarAporteExtraordinarioAPV() - catch (error)", e);
            data = null;
            try {

                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("generarAporteExtraordinarioAPV() - catch (error)", f);
            }
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                data = null;
                logger.error("generarAporteExtraordinarioAPV() - catch (error)", e);
            }
        }

        if (detalle != null && detalle.getIdaporte().longValue() > 0) {
            if (!updateIdAporteComprobantes(detalle)) {
                detalle = null;
            }
        }

        logger.debug("generarAporteExtraordinarioAPV() - termina");
        return detalle;
    }


    /**
     * APT
     * @param detalle
     * @return
     */
    public boolean updateIdAporteComprobantes(DetalleAPV detalle) {
        logger.debug("generarAporteExtraordinarioAPV() - inicia");
        boolean ret = false;
        Connection cnx = null;
        CallableStatement stm = null;

        String sql =
            "UPDATE BPI_COM_COMPROBANTES_TBL SET " + "IDAPORTE = ? " + "WHERE ID_TRANSACCION = ? " +
            "AND NUM_PRODUCTO = ? " + "AND CUOTA = ? ";

        try {
            cnx = getConnection();
            stm = cnx.prepareCall(sql);
            stm.setLong(1, detalle.getIdaporte());
            stm.setLong(2, detalle.getIdtransaccion());
            stm.setLong(3, detalle.getNumeropoliza());
            stm.setLong(4, detalle.getNumerofolio());
            stm.execute();
            ret = true;

        } catch (Exception e) {
            logger.error("generarAporteExtraordinarioAPV() - catch (error)", e);
            try {

                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("generarAporteExtraordinarioAPV() - catch (error)", f);
            }
        } finally {
            try {
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
                logger.error("generarAporteExtraordinarioAPV() - catch (error)", e);
            }
        }
        logger.debug("generarAporteExtraordinarioAPV() - termina");
        return ret;
    }

    /**
     * Recupera el Folio caja con el cual
     * se aperturo el turno
     * @param turno
     * @return
     */
    public Long getFolioCajaByTurno(java.util.Date turno) {
        Long folio = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = getConnection();

            // construct the SQL statement
            final String SQL = "SELECT FOLIO FROM UCAJAS.FOLIOCAJA WHERE FECHA = ?";

            // prepare statement
            stmt = conn.prepareStatement(SQL);
            stmt.setDate(1, new java.sql.Date(turno.getTime()));
            rs = stmt.executeQuery();
            if (rs.next()) {
                folio = rs.getLong("FOLIO");
            }

        } catch (Exception _e) {
            _e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
        return folio;
    }

    /**
     * Recupera el Folio caja con el cual
     * se aperturo el turno
     * @param turno
     * @return
     */
    public java.util.Date getFechaYHoraServidorOracle() {
        java.util.Date fecha = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = getConnection();

            // construct the SQL statement
            final String SQL = "SELECT sysdate FROM DUAL";

            // prepare statement
            stmt = conn.prepareStatement(SQL);
            rs = stmt.executeQuery();
            if (rs.next()) {
                fecha = new java.util.Date(rs.getTimestamp("sysdate").getTime());
            }

        } catch (Exception _e) {
            _e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
        return fecha;
    }


    /**
     * Actualiza el estado de una transaccion
     * @param idtransaccion
     * @param cod_estado
     * @return
     */
    public Boolean updateEmpresaTransaccion(Integer idtransaccion, String empresa) {
        logger.info("updateEmpresaTransaccion(idtransaccion[" + idtransaccion + "] - empresa[" + empresa +
                    "]) - iniciando");
        Boolean resp = Boolean.FALSE;
        logger.debug("updateEmpresaTransaccion() - consulta de informacion obtenido, procediendo a actualizar el dato");
        Connection cnx = null;
        try {

            cnx = getConnection();
            PreparedStatement stm =
                cnx.prepareStatement("UPDATE BPI_TRA_TRANSACCIONES_TBL SET EMPRESA = ? WHERE ID_TRANSACCION = ? ");

            // SET PARAMETROS
            stm.setString(1, empresa);
            stm.setLong(2, new Long("0" + idtransaccion));

            logger.debug("updateEmpresaTransaccion() - empresa :" + empresa);
            logger.debug("updateEmpresaTransaccion() - idtransaccion :" + idtransaccion);

            // EXECUTE
            stm.execute();
            stm.close();

            resp = Boolean.TRUE;

        } catch (Exception e) {
            logger.error("updateEmpresaTransaccion() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("updateEmpresaTransaccion() - terminado");
        return resp;
    }


    /**
     * Recupera el ID del turno abierto para internet
     * @return
     */
    public Long getIdTurnoOpenForInternet() {
        logger.debug("getIdTurnoOpenForInternet() - iniciando");
        Long resp = new Long(0);
        ResultSet rs = null;
        PreparedStatement stm = null;
        Connection cnx = null;
        try {

            cnx = getConnection();
            stm = cnx.prepareStatement("SELECT ID_TURNO FROM TURNOS WHERE USUARIO = 'INTERNET' AND ABIERTO = 'S'");
            rs = stm.executeQuery();
            if (rs.next()) {
                resp = rs.getLong("ID_TURNO");
            }

        } catch (Exception e) {
            logger.error("updateEmpresaTransaccion() - catch (error)", e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
            }
            try {
                if (stm != null)
                    stm.close();
            } catch (Exception e) {
            }
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("updateEmpresaTransaccion() - terminado");
        return resp;
    }

    /**
     * NUEVA Consulta de deudas por concepto de PRIMERA PRIMA
     * @param numeroPropuesta
     * @param rut
     * @return
     */
    public List findPrimeraPrimaByNumppRut(Integer numeroPropuesta, Integer rut) {
        logger.info("findPrimeraPrimaByNumppRut(numeroPropuesta[" + numeroPropuesta + "] - rut[" + rut + "]) - inicia");
        List<IndPrimaNormalVw> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        CallableStatement stmVerifica = null;
        ResultSet rstVerifica = null;
        try {
            cnx = super.getConnection();
            //stm = cnx.prepareCall("SELECT * FROM IND_PRIMA_NORMAL_VW WHERE RUT_CONTRATANTE = ?");
            //stm.setInt(1, Integer.parseInt("0"+rut));
            stm = cnx.prepareCall("SELECT * FROM VW_PRIMERA_PRIMA WHERE NUMERO_PROPUESTA = ? AND RUT_CONTRATANTE = ?");
            stm.setInt(1, numeroPropuesta);
            stm.setInt(2, rut);
            rst = stm.executeQuery();
            while (rst.next()) {
                boolean agregar = true;
                IndPrimaNormalVw dto = new IndPrimaNormalVw();
                dto.setCodEmpresa(1); //DATO DURO BV
                dto.setCodMecanismo(2); //CUOTA FIJA
                dto.setCodProducto(0); //NO POSEE TIPO PRODUCTO
                dto.setDescTipoRecibo(rst.getString("TIPO"));
                dto.setDvRutContratante(StringUtil.getFirstCharacter(rst.getString("DV_CONTRATANTE")));
                dto.setFInicioRecibo(new Date(System.currentTimeMillis()));
                dto.setFolioRecibo(rst.getInt("NUMERO_PROPUESTA"));
                dto.setFpago(0);
                dto.setFTerminoRecibo(new Date(System.currentTimeMillis()));
                dto.setIdTipoRecibo(rst.getString("ID"));
                dto.setIvaUfRecibo(0.0);
                dto.setIvaPesosRecibo(new Long(0));
                dto.setIvaPesosRecibo(rst.getLong("MONTO_PAGO"));
                dto.setNombre(rst.getString("TIPO")); //DESCRIPCION DEL PRODUCTO
                //dto.setPolizaPol(rst.getInt("NUMERO_POLIZA_TII"));
                dto.setPolizaPol(rst.getInt("NUMERO_POLIZA_TII"));
                dto.setPrimaBrutaUfRecibo(0.0);
                dto.setPrimaNetaUfRecibo(0.0);
                dto.setPrimaBrutaPesosRecibo(rst.getLong("MONTO_PAGO"));
                dto.setPrimaNetaPesosRecibo(rst.getLong("MONTO_PAGO"));
                dto.setValorCambioUF(0.0);
                dto.setPropuestaPol(rst.getInt("NUMERO_PROPUESTA"));
                dto.setRamo(rst.getInt("RAMO_TII"));
                dto.setRutContratante(rst.getInt("RUT_CONTRATANTE"));
                dto.setViapago(0);
                dtos.add(dto);

            }


            if (dtos != null && dtos.size() == 0) {
                stm = cnx.prepareCall("SELECT * FROM VW_PRIMERA_PRIMA WHERE NUMERO_PROPUESTA = ?");
                stm.setInt(1, numeroPropuesta);
                rst = stm.executeQuery();
                while (rst.next()) {
                    boolean agregar = true;
                    if (rst.getString("RUT_CONTRATANTE") == null) {
                        IndPrimaNormalVw dto = new IndPrimaNormalVw();
                        dto.setCodEmpresa(1); //DATO DURO BV
                        dto.setCodMecanismo(2); //CUOTA FIJA
                        dto.setCodProducto(0); //NO POSEE TIPO PRODUCTO
                        dto.setDescTipoRecibo(rst.getString("TIPO"));
                        dto.setDvRutContratante(RutUtil.calculaDv(rut).charAt(0));
                        dto.setFInicioRecibo(new Date(System.currentTimeMillis()));
                        //dto.setFolioRecibo(rst.getInt("NUMERO_POLIZA_TII"));
                        dto.setFolioRecibo(rst.getInt("NUMERO_PROPUESTA"));
                        dto.setFpago(0);
                        dto.setFTerminoRecibo(new Date(System.currentTimeMillis()));
                        dto.setIdTipoRecibo(rst.getString("ID"));
                        dto.setIvaUfRecibo(0.0);
                        dto.setIvaPesosRecibo(new Long(0));
                        dto.setIvaPesosRecibo(rst.getLong("MONTO_PAGO"));
                        dto.setNombre(rst.getString("TIPO")); //DESCRIPCION DEL PRODUCTO
                        dto.setPolizaPol(rst.getInt("NUMERO_POLIZA_TII"));
                        //dto.setPolizaPol(rst.getInt("NUMERO_PROPUESTA"));
                        dto.setPrimaBrutaUfRecibo(0.0);
                        dto.setPrimaNetaUfRecibo(0.0);
                        dto.setPrimaBrutaPesosRecibo(rst.getLong("MONTO_PAGO"));
                        dto.setPrimaNetaPesosRecibo(rst.getLong("MONTO_PAGO"));
                        dto.setValorCambioUF(0.0);
                        dto.setPropuestaPol(rst.getInt("NUMERO_PROPUESTA"));
                        dto.setRamo(rst.getInt("RAMO_TII"));
                        dto.setRutContratante(rut);
                        dto.setViapago(0);
                        dtos.add(dto);
                    }

                }
            }

        } catch (Exception e) {
            logger.error("findPrimeraPrimaByNumppRut() - catch (error)", e);
            dtos = null;
            try {
                if (rstVerifica != null)
                    rstVerifica.close();
                if (stmVerifica != null)
                    stmVerifica.close();
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findPrimeraPrimaByNumppRut() - catch (error)", f);
            }
        } finally {
            try {
                if (rstVerifica != null)
                    rstVerifica.close();
                if (stmVerifica != null)
                    stmVerifica.close();
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("findPrimeraPrimaByNumppRut() - termina");
        return dtos;
    }


    /**
     * NUEVA Consulta de Cliente en COTIZADOR
     * @param rut
     * @return
     */
    public PersonaCotizadorDto findClienteCotizador(Integer rut) {
        logger.info("findClienteCotizador(rut[" + rut + "]) - inicia");

        PersonaCotizadorDto dto = null;

        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        CallableStatement stmVerifica = null;
        ResultSet rstVerifica = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM VW_CLIENTE_COTIZADORSA WHERE RUT_CLIENTE = ?");
            stm.setString(1, ("" + rut + "-" + RutUtil.calculaDv(rut)).toUpperCase());
            rst = stm.executeQuery();

            if (rst.next()) {
                dto = new PersonaCotizadorDto();
                dto.setRut(rst.getString("RUT_CLIENTE"));
                dto.setNombre(rst.getString("NOMBRE"));
                dto.setApellidoPaterno(rst.getString("APELLIDO_PATERNO"));
                dto.setApellidoMaterno(rst.getString("APELLIDO_MATERNO"));
                dto.setEstadoCivil(rst.getInt("ESTADO_CIVIL"));
                dto.setSexo(rst.getString("GENERO"));
                dto.setFechaNacimiento(FechaUtil.toUtilDate(rst.getDate("FECHA_NACIMIENTO")));

            }

        } catch (Exception e) {
            logger.error("findClienteCotizador() - catch (error)", e);
            dto = null;
            try {
                if (rstVerifica != null)
                    rstVerifica.close();
                if (stmVerifica != null)
                    stmVerifica.close();
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("findClienteCotizador() - catch (error)", f);
            }
        } finally {
            try {
                if (rstVerifica != null)
                    rstVerifica.close();
                if (stmVerifica != null)
                    stmVerifica.close();
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("findClienteCotizador() - termina");
        return dto;
    }


    /**
     * Inserta homologacion
     * @return
     */
    public Boolean insertarHomologacionPKConvenio(Long codigoMedioPago, Long idTransaccion,
                                                  String idConvenioTransaccion, java.util.Date fecha) {
        logger.info("insertarHomologacionPKConvenio(codigoMedioPago[" + codigoMedioPago + "] - idTransaccion[" +
                    idTransaccion + "] - idConvenioTransaccion[" + idConvenioTransaccion + "] - fecha[" + fecha +
                    "]) - iniciando");
        Connection cnx = null;
        Boolean resultado = Boolean.FALSE;
        try {


            cnx = getConnection();
            //Inserta y deja activo el dispositivo
            PreparedStatement stm =
                cnx.prepareStatement("INSERT INTO BPI_HOMOLOGACION_BANCO_CONV (IDCOMERCIO, ID_TRANSACCION, COD_MEDIO_PAGO, FECHA) VALUES(?,?,?,?)");

            // SET PARAMETROS
            stm.setString(1, idConvenioTransaccion);
            stm.setLong(2, idTransaccion);
            stm.setLong(3, codigoMedioPago);
            stm.setDate(4,
                        (fecha != null ? new java.sql.Date(fecha.getTime()) :
                         new java.sql.Date(System.currentTimeMillis())));


            // EXECUTE
            stm.execute();
            stm.close();
            resultado = Boolean.TRUE;

        } catch (Exception e) {
            logger.error("newNavegacion() - catch (error)", e);

        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        return resultado;
    }


    /**
     * Actualiza la transaccion
     * @param idtransaccion
     * @param usotransaccion
     * @return
     */
    public Transacciones updateTransaccion(Integer idtransaccion, String usoTrxUsadaporMDB) {
        Transacciones dto = findTransaccionById(idtransaccion);
        logger.info("updateTransaccion(idtransaccion[" + idtransaccion + "] - usoTrxUsadaporMDB[" + usoTrxUsadaporMDB +
                    "]) - iniciando ");
        Connection cnx = null;
        try {

            cnx = getConnection();
            PreparedStatement stm =
                cnx.prepareStatement("UPDATE BPI_TRA_TRANSACCIONES_TBL SET TRX_USADAPOR_MDB = ? WHERE ID_TRANSACCION = ?");

            // SET PARAMETROS
            stm.setString(1, usoTrxUsadaporMDB);
            stm.setInt(2, idtransaccion);

            // EXECUTE
            stm.execute();
            stm.close();

        } catch (Exception e) {
            logger.error("updateTransaccion() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("updateTransaccion() - terminado");
        return dto;
    }


    /**
     * Actualiza la transaccion
     * @param idtransaccion
     * @param usotransaccion
     * @return
     */
    public boolean updateTransaccionRESTful(Long idtransaccion, Integer codEstado) {
        boolean res = false;
        logger.info("updateTransaccionRESTful(idtransaccion[" + idtransaccion + "] - codEstado[" + codEstado +
                    "]) - iniciando ");
        Connection cnx = null;
        try {

            cnx = getConnection();
            PreparedStatement stm =
                cnx.prepareStatement("UPDATE BPI_TRA_RESTFULL_SERV SET COD_ESTADO = ? WHERE ID_TRANSACCION = ?");

            // SET PARAMETROS
            stm.setInt(1, codEstado);
            stm.setLong(2, idtransaccion);

            // EXECUTE
            stm.execute();
            stm.close();

            res = true;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateTransaccionRESTful() - catch (error)", e);
        } finally {
            try {
                if (cnx != null)
                    cnx.close();
            } catch (Exception e) {
            }
        }
        logger.debug("updateTransaccionRESTful() - terminado");
        return res;
    }


    /**
     * Consulta si es de tipo RESTFul la transaccion de pago
     * @param rut
     * @return
     */
    public boolean isTransaccionRESTful(Long idTransaccion) {
        boolean res = false;
        logger.info("isTransaccionRESTful(idTransaccion[" + idTransaccion + "]) - inicia");
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        CallableStatement stmVerifica = null;
        ResultSet rstVerifica = null;
        try {
            cnx = super.getConnection();
            stm = cnx.prepareCall("SELECT * FROM BPI_TRA_RESTFULL_SERV WHERE ID_TRANSACCION = ?");
            stm.setLong(1, idTransaccion);
            rst = stm.executeQuery();

            if (rst.next()) {
                res = true;
            }

        } catch (Exception e) {
            logger.error("isTransaccionRESTful() - catch (error)", e);
            try {
                if (rst != null)
                    rst.close();
                if (stm != null)
                    stm.close();
                if (cnx != null)
                    cnx.close();
            } catch (SQLException f) {
                logger.error("isTransaccionRESTful() - catch (error)", f);
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
        logger.debug("isTransaccionRESTful() - termina");
        return res;
    }

}
