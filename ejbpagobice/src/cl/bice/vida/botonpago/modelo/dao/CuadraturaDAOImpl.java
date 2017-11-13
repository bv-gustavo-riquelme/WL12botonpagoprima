package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.BpiDlcDetalleLogCuadrTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiLgclogCuadraturaTbl;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.Date;

import org.apache.log4j.Logger;


public class CuadraturaDAOImpl extends DataSourceBice implements CuadraturaDAO {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(CuadraturaDAOImpl.class);

    /**
     * Inserta una nueva cuadratura en el sistema
     * de cuadraturas de transacciones
     * @param idEmpresa
     * @param idMedioPago
     * @param fechaCuadratura
     * @return
     */
    public Long insertNewCuadratura(Integer idEmpresa, Integer idMedioPago, Date fechaCuadratura) {
        logger.info("insertNewCuadratura() - iniciando");
        Connection cnx = null;
        Long idCuadratura = null;
        try {
            idCuadratura = new Long(DAOFactory.getPersistenciaGeneralDao().getSecuenceValue("bpi_cua_idcuadratura_seq"));            
            cnx = getConnection();
            PreparedStatement stm = cnx.prepareStatement("insert into bpi_cua_cuadratura_tbl (id_cuadratura, cod_empresa, cod_medio, fecha_hora, monto_total, monto_informado, transacciones) values(?,?,?,?,?,?,?)");
            stm.setLong(1, idCuadratura);
            stm.setInt(2, idEmpresa);
            stm.setInt(3, idMedioPago);
            stm.setDate(4, new java.sql.Date(fechaCuadratura.getTime()));
            stm.setLong(5, 0);
            stm.setLong(6, 0);
            stm.setLong(7, 0);

            // EXECUTE
            stm.execute();
            stm.close();

        } catch (Exception e) {
            logger.error("insertNewCuadratura() - catch (error)", e);
            idCuadratura = null;
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {}
        }
        logger.info("insertNewCuadratura() - terminando");
        return idCuadratura;
    }

    
    /**
     * Actualiza los datos de una cuadratura
     * @param idCuadratura
     * @param montoTotal
     * @param montoInformado
     * @param numeroTransacciones
     * @return
     */
    public Boolean updateCuadratura(Long idCuadratura, Long montoTotal, Long montoInformado, Long numeroTransacciones) {
        logger.info("updateCuadratura() - iniciando");
        Connection cnx = null;
        Boolean resp = Boolean.FALSE;
        try {
            cnx = getConnection();
            PreparedStatement stm = cnx.prepareStatement("UPDATE bpi_cua_cuadratura_tbl SET monto_total = ?, monto_informado = ?, transacciones = ? WHERE id_cuadratura = ?");
            stm.setLong(1, montoTotal);
            stm.setLong(2, montoInformado);
            stm.setLong(3, numeroTransacciones);
            stm.setLong(4, idCuadratura);

            // EXECUTE
            stm.execute();
            stm.close();
            resp = Boolean.TRUE;
            
        } catch (Exception e) {
            logger.error("updateCuadratura() - catch (error)", e);
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {}
        }
        logger.info("updateCuadratura() - terminando");
        return resp;
    }

    /**
     * Actualiza o inserta la realcion cuadratura versus
     * el numero de transacion cuadrada
     * @param idCuadratura
     * @param idTransaccion
     * @return  
     */
    public Boolean insertCuadraturaTransaccion(Long idCuadratura, Long idTransaccion) {
        logger.info("insertCuadraturaTransaccion() - iniciando");
        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rst = null;
        Boolean resp = Boolean.FALSE;
        try {
            cnx = getConnection();
            
            //Elimina relacion en caso de existir una cuadratura para x transaccion       
            stm = cnx.prepareStatement("SELECT * FROM bpi_dcu_detcuad_tbl WHERE id_transaccion = ?");            
            stm.setLong(1, idTransaccion);
            rst = stm.executeQuery();

            //Inserta nueva relacion de cuadratura siempre y cuando no exista la cuadratura para la transaccion
            if (!rst.next()) {
                stm = cnx.prepareStatement("INSERT INTO bpi_dcu_detcuad_tbl (id_cuadratura, id_transaccion) VALUES(?,?)");
                stm.setLong(1, idCuadratura);
                stm.setLong(2, idTransaccion);
    
                // EXECUTE
                stm.execute();                
            }
            rst.close();
            stm.close();
            resp = Boolean.TRUE;
            
        } catch (Exception e) {
            logger.error("insertCuadraturaTransaccion() - catch (error)", e);
        } finally {
            try { if (rst != null) rst.close();} catch (Exception e) {}
            try { if (stm != null) stm.close();} catch (Exception e) {}
            try { if (cnx != null) cnx.close();} catch (Exception e) {}
        }
        logger.info("insertCuadraturaTransaccion() - terminando");
        return resp;
    }
    
    /**
     * Inserta un log de cuadratura
     * @param dto
     * @return
     */
    public Long insertLogCuadratura(BpiLgclogCuadraturaTbl dto) {
        logger.info("insertLogCuadratura() - iniciando");
        Long numeroIntentos = new Long(0);
        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareStatement("SELECT NUM_INTENTOS FROM BPI_LGC_LOGCUADRATURA_TBL WHERE ID_CUADRATURA = ?");
            stm.setLong(1, dto.getIdCuadratura());
            rst = stm.executeQuery();
            if (rst.next()) {
                numeroIntentos = rst.getLong("NUM_INTENTOS");
            } else {
                numeroIntentos = new Long(0);
            }
            rst.close();
            numeroIntentos = numeroIntentos + 1;
            
            //Inserta y deja activo el dispositivo
            if (numeroIntentos.longValue() == 1) {
                stm = cnx.prepareStatement("INSERT INTO BPI_LGC_LOGCUADRATURA_TBL (" +
                                                        "COD_MEDIO,COD_EMPRESA,FECHA_CUADR,ARCHIVO,NUM_INTENTOS,ESTADO," +
                                                        "NUM_REGISTROS,NUM_CUADR,MONTO_CUADR,NUM_NOCUADR,MONTO_NOCUADR,NUM_REVERS,MONTO_REVERS," +
                                                        "NUM_NOUBIC,NUM_NOPROC,MONTO_NOPROC, ID_CUADRATURA) " +
                                                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            } else {
                stm = cnx.prepareStatement("UPDATE BPI_LGC_LOGCUADRATURA_TBL SET " +
                                                            "COD_MEDIO = ?," +
                                                            "COD_EMPRESA = ?," +
                                                            "FECHA_CUADR = ?," +
                                                            "ARCHIVO = ?," +
                                                            "NUM_INTENTOS = ?," +
                                                            "ESTADO = ?," +
                                                            "NUM_REGISTROS = ?," +
                                                            "NUM_CUADR = ?," +
                                                            "MONTO_CUADR = ?," +
                                                            "NUM_NOCUADR = ?," +
                                                            "MONTO_NOCUADR = ?," +
                                                            "NUM_REVERS = ?," +
                                                            "MONTO_REVERS = ?," +
                                                            "NUM_NOUBIC = ?," +
                                                            "NUM_NOPROC = ?," +
                                                            "MONTO_NOPROC = ? WHERE ID_CUADRATURA = ?");                
            }
            // SET PARAMETROS
            stm.setLong(1, dto.getCodMedio());
            stm.setLong(2, dto.getCodEmpresa());
            stm.setTimestamp(3, new Timestamp(dto.getFechaCuadratura().getTime()));
            stm.setString(4, dto.getArchivo());
            stm.setLong(5, numeroIntentos);
            stm.setString(6, dto.getEstado());
            stm.setLong(7, dto.getNumeroRegistros());
            stm.setLong(8, dto.getNumeroCuadrados());
            stm.setLong(9, dto.getMontoCuadrados());
            stm.setLong(10, dto.getNumeroNoCuadrados());
            stm.setLong(11, dto.getMontoNoCuadrados());
            stm.setLong(12, dto.getNumeroReversados());
            stm.setLong(13, dto.getMontoReversados());
            stm.setLong(14, dto.getNumeroNoHubicados());
            stm.setLong(15, dto.getNumeroNoProcesados());
            stm.setLong(16, dto.getMontoNoProcesados());
            stm.setLong(17, dto.getIdCuadratura());
            
           // EXECUTE
            stm.execute();
            stm.close();
            
        } catch (Exception e) {
            logger.error("insertLogCuadratura() - catch (error)", e);
            dto = null;
        } finally {
            try { if (stm != null) stm.close();} catch (Exception e) {}
            try { if (cnx != null) cnx.close();} catch (Exception e) {}
        }
        
        return numeroIntentos;
    }    
    
    /**
     * Inserta un detalle de log de cuadratura
     * @param dto
     * @return
     */
    public Boolean insertLogDetalleCuadratura(BpiDlcDetalleLogCuadrTbl dto) {
        logger.info("insertLogDetalleCuadratura() - iniciando");
        Boolean resp = Boolean.FALSE;
        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareStatement("INSERT INTO BPI_DLC_DETALLELOGCUADR_TBL (" +
                                                    "ID_CUADRATURA,NUM_INTENTO,LOG_CUADR) " +
                                                    "VALUES(?,?,?)");
            // SET PARAMETROS
            stm.setLong(1, dto.getIdCuadratura());
            stm.setLong(2, dto.getNumeroIntento());
            stm.setString(3, dto.getDetalleProceso());
            
           // EXECUTE
            stm.execute();
            stm.close();
            
            resp = Boolean.TRUE;
            
        } catch (Exception e) {
            logger.error("insertLogDetalleCuadratura() - catch (error)", e);
            dto = null;
        } finally {
            try { if (stm != null) stm.close();} catch (Exception e) {}
            try { if (cnx != null) cnx.close();} catch (Exception e) {}
        }        
        return resp;
    }        
}
