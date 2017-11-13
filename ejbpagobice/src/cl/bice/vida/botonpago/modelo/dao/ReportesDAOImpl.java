package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.ReporteAgentePolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteGerentePolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteJefeSucursalPolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteJefeZonalPolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteSupervisorPolizas;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class ReportesDAOImpl extends DataSourceBice implements ReportesDAO {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ReportesDAOImpl.class);

    /**
     * Consulta el reporte de generente
     * para las polizas
     * @return
     * TODO: Testear
     */
    public List<ReporteGerentePolizas> generateReporteGerentePolizas() {
        logger.info("generateReporteGerentePolizas() - inicia");
        List<ReporteGerentePolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT   suczon.numero_jefe_zona, suczon.nombre, " + 
                                "         SUM (CASE pol.id_estado_recibo " + 
                                "                 WHEN '01' " + 
                                "                    THEN 0 " + 
                                "                 ELSE pol.prima_pesos_recibo " + 
                                "              END " + 
                                "             ) AS rut_agente, " + 
                                "         SUM (CASE pol.id_estado_recibo " + 
                                "                 WHEN '01' " + 
                                "                    THEN pol.prima_bruta_uf_recibo " + 
                                "                 ELSE 0 " + 
                                "              END " + 
                                "             ) AS numero_agente " + 
                                "    FROM polizas_marketing_vw pol, jefezonal_vw suczon " + 
                                "   WHERE pol.numero_agente = suczon.numero_agente " + 
                                "GROUP BY suczon.numero_jefe_zona, suczon.nombre");
            rst = stm.executeQuery();
            while (rst.next()) {
                ReporteGerentePolizas dto = new ReporteGerentePolizas();            
                dto.setNumero_jefezonal(rst.getInt("numero_jefe_zona"));
                dto.setNombre_jefezonal(rst.getString("nombre"));
                dto.setPor_recaudar(rst.getLong("numero_agente"));
                dto.setRecaudado(rst.getLong("rut_agente"));
                dtos.add(dto);
            }              
        } catch (Exception e) {
            logger.error("generateReporteGerentePolizas() - catch (error)", e);
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("generateReporteGerentePolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("generateReporteGerentePolizas() - termina");
        return dtos;    
    }

    /**
     * Genera el reporte de jefe de sucursal polizas
     * @param num_jefe_sucursal
     * @return
     * TODO: Testear
     */
    public List<ReporteJefeSucursalPolizas> generateReporteJefeSucursalPolizas(Integer num_jefe_sucursal) {
        logger.info("generateReporteGerentePolizas() - inicia");
        List<ReporteJefeSucursalPolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT supjef.numero_supervisor, " + 
                        "       supjef.nombre, " + 
                        "       sum( " + 
                        "           case pol.id_estado_recibo " + 
                        "                 when '01' then " + 
                        "                   0        " + 
                        "                 else " + 
                        "                   pol.prima_pesos_recibo" + 
                        "                 end " + 
                        "           )as rut_agente," + 
                        "       sum( " + 
                        "           case pol.id_estado_recibo " + 
                        "                 when '01' then " + 
                        "                   pol.prima_bruta_uf_recibo        " + 
                        "                 else " + 
                        "                   0 " + 
                        "                 end " + 
                        "           )as numero_agente " + 
                        "    FROM  Polizas_Marketing_VW pol, Supervisores_JefeSuc_VW supjef " + 
                        "    WHERE pol.numero_agente = supjef.numero_agente " +
                        "      AND supjef.numero_jefe_sucursal=? " + 
                        "    GROUP BY supjef.numero_supervisor, supjef.nombre");
            stm.setInt(1, num_jefe_sucursal);
            rst = stm.executeQuery();
            while (rst.next()) {
                ReporteJefeSucursalPolizas dto = new ReporteJefeSucursalPolizas();       
                dto.setNom_supervisor(rst.getString("nombre"));
                dto.setNumero_supervisor(rst.getInt("numero_supervisor"));
                dto.setPor_recaudar(rst.getLong("numero_agente"));
                dto.setRecaudado(rst.getLong("rut_agente"));
                dtos.add(dto);
            }              
        } catch (Exception e) {
            logger.error("generateReporteJefeSucursalPolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("generateReporteJefeSucursalPolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("generateReporteJefeSucursalPolizas() - termina");
        return dtos;  
    }

    /**
     * Genera reporte de supervisor de polizas
     * @param numero_supervisor
     * @return
     * TODO: Testear
     */
    public List<ReporteSupervisorPolizas> generateReporteSupervisorPolizas(Integer numero_supervisor) {
        logger.info("generateReporteSupervisorPolizas() - inicia");
        List<ReporteSupervisorPolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT   agsup.numero_agente, agsup.nombre, " + 
                            "         SUM (CASE pol.id_estado_recibo " + 
                            "                 WHEN '01'" + 
                            "                    THEN 0 " + 
                            "                 ELSE pol.prima_pesos_recibo " + 
                            "              END " + 
                            "             ) AS rut_agente, " + 
                            "         SUM (CASE pol.id_estado_recibo " + 
                            "                 WHEN '01' " + 
                            "                    THEN pol.prima_bruta_uf_recibo " + 
                            "                 ELSE 0 " + 
                            "              END " + 
                            "             ) AS numero_supervisor " + 
                            "    FROM polizas_marketing_vw pol, agentes_supervisores_vw agsup " + 
                            "   WHERE pol.numero_agente = agsup.numero_agente " + 
                            "     AND agsup.numero_supervisor = ? " + 
                            "GROUP BY agsup.numero_agente, agsup.nombre");
            stm.setInt(1, numero_supervisor);
            rst = stm.executeQuery();
            while (rst.next()) {
                ReporteSupervisorPolizas dto = new ReporteSupervisorPolizas();       
                dto.setNom_agente(rst.getString("nombre"));
                dto.setNumero_agente(rst.getInt("numero_agente"));
                dto.setPor_recaudar(rst.getLong("numero_supervisor"));
                dto.setRecaudado(rst.getLong("rut_agente"));
                dtos.add(dto);
            }              
        } catch (Exception e) {
            logger.error("generateReporteSupervisorPolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("generateReporteSupervisorPolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("generateReporteSupervisorPolizas() - termina");
        return dtos;  
    }

    /**
     * Genera reporte de agente por polizas
     * segun numero de agentes, paginado y cantidad de registros
     * @param numero_agente
     * @param rowdesde
     * @param rowhasta
     * @return
     * TODO: Testear
     */
    public List<ReporteAgentePolizas> generateReporteAgentePolizas(Integer numero_agente, Integer rowdesde, Integer rowhasta) {
        logger.info("generateReporteAgentePolizas() - inicia");
        List<ReporteAgentePolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT * " + 
                            "  FROM (SELECT datos.*, ROWNUM rnum " + 
                            "          FROM ( " + 
                            "	SELECT vis.rut_contratante, vis.dv_rut_contratante, vis.contratante, " + 
                            "	       pro.nombre AS id_lproducto,vis.id_ramo, vis.poliza_pol, vis.f_emision_recibo, " + 
                            "	       CASE vis.id_estado_recibo  " + 
                            "	          WHEN '01' THEN vis.prima_bruta_uf_recibo " + 
                            "	          ELSE 0 " + 
                            "	       END AS prima_bruta_uf_recibo, " + 
                            "	       CASE vis.id_estado_recibo  " + 
                            "	          WHEN '01' THEN 0 " + 
                            "	          ELSE vis.prima_pesos_recibo " + 
                            "	       END AS prima_pesos_recibo, " + 
                            "	       CASE vis.id_estado_recibo  " + 
                            "	          WHEN '01' " + 
                            "	             THEN 'POR PAGAR' " + 
                            "	          ELSE 'PAGADO' " + 
                            "	       END AS id_estado_recibo, " + 
                            "	       vis.folio_recibo " + 
                            "	  FROM polizas_marketing_vw vis JOIN bpi_pro_productos_tbl pro " + 
                            "	       ON (   (vis.id_ramo <> 0 AND vis.id_ramo + 2000 = pro.cod_producto) " + 
                            "	           OR (vis.id_ramo = 0 AND vis.id_lproducto + 1000 = pro.cod_producto " + 
                            "	              ) " + 
                            "	          ) " + 
                            "	 WHERE vis.numero_agente = ?) datos " + 
                            "         WHERE ROWNUM <=?) " + 
                            " WHERE rnum >= ?");
            stm.setInt(1, numero_agente);
            stm.setInt(2, rowhasta);
            stm.setInt(3, rowdesde);
            rst = stm.executeQuery();
            while (rst.next()) {
                ReporteAgentePolizas dto = new ReporteAgentePolizas();       
                dto.setContratante(rst.getString("contratante"));
                dto.setDv_contratante(rst.getString("dv_rut_contratante"));
                dto.setEstado(rst.getString("id_estado_recibo"));
                dto.setFecha_recibo(FechaUtil.toUtilDate(rst.getDate("f_emision_recibo")));
                dto.setFolio_recibo(rst.getInt("folio_recibo"));
                dto.setId_ramo(rst.getInt("id_ramo"));
                dto.setPoliza_pol(rst.getInt("poliza_pol"));
                dto.setPrima_bruta(rst.getInt("prima_pesos_recibo"));
                dto.setPrima_bruta_uf(rst.getDouble("prima_bruta_uf_recibo"));
                dto.setProducto(rst.getString("id_lproducto"));
                dto.setRut_contratante(rst.getInt("rut_contratante"));
                dtos.add(dto);
            }              
        } catch (Exception e) {
            logger.error("generateReporteAgentePolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("generateReporteAgentePolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("generateReporteAgentePolizas() - termina");
        return dtos;      
    }

    /**
     * Genera reporte de Jefe de Zona por Polizas
     * segun el numero de jefe de zona
     * @param num_jefe_zona
     * @return
     * TODO: Testear
     */
    public List<ReporteJefeZonalPolizas> generateReporteJefeZonalPolizas(Integer num_jefe_zona) {
        logger.info("generateReporteJefeZonalPolizas() - inicia");
        List<ReporteJefeZonalPolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT suczon.numero_jefe_sucursal, " + 
                                "       suczon.nombre, " + 
                                "       sum( " + 
                                "           case pol.id_estado_recibo " + 
                                "                 when '01' then  " + 
                                "                   0         " + 
                                "                 else  " + 
                                "                   pol.prima_pesos_recibo " + 
                                "                 end " + 
                                "           )as rut_agente, " + 
                                "            " + 
                                "       sum( " + 
                                "           case pol.id_estado_recibo " + 
                                "                 when '01' then  " + 
                                "                   pol.prima_bruta_uf_recibo         " + 
                                "                 else  " + 
                                "                   0 " + 
                                "                 end " + 
                                "           )as numero_agente " + 
                                "                  " + 
                                "    FROM  Polizas_Marketing_VW pol, JefeSuc_JefeZon_VW suczon " + 
                                "    WHERE pol.numero_agente = suczon.numero_agente AND " + 
                                "           suczon.numero_jefe_zona = ? " + 
                                "    GROUP BY suczon.numero_jefe_sucursal, suczon.nombre");
            stm.setInt(1, num_jefe_zona);
            rst = stm.executeQuery();
            while (rst.next()) {
                ReporteJefeZonalPolizas dto = new ReporteJefeZonalPolizas();       
                dto.setNom_jefesuc(rst.getString("nombre"));
                dto.setNumero_jefesuc(rst.getInt("numero_jefe_sucursal"));
                dto.setPor_recaudar(rst.getLong("numero_agente"));
                dto.setRecaudado(rst.getLong("rut_agente"));
                dtos.add(dto);
            }              
        } catch (Exception e) {
            logger.error("generateReporteJefeZonalPolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("generateReporteJefeZonalPolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("generateReporteJefeZonalPolizas() - termina");
        return dtos;   
    }

    /**
     * Consulta reporte por agente y rut de polizas
     * en formato paginado
     * @param rut_agente
     * @param rowdesde
     * @param rowhasta
     * @return
     * TODO: Testear
     */
    public List<ReporteAgentePolizas> generateReporteAgenteByRutPolizas(Integer rut_agente, Integer rowdesde, Integer rowhasta) {
        logger.info("generateReporteAgenteByRutPolizas() - inicia");
        List<ReporteAgentePolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT * " + 
                            "  FROM (SELECT datos.*, ROWNUM rnum " + 
                            "          FROM ( " + 
                            "	SELECT vis.rut_contratante, vis.dv_rut_contratante, vis.contratante, " + 
                            "	       pro.nombre AS id_lproducto,vis.id_ramo, vis.poliza_pol, vis.f_emision_recibo, " + 
                            "	       CASE vis.id_estado_recibo  " + 
                            "	          WHEN '01' THEN vis.prima_bruta_uf_recibo " + 
                            "	          ELSE 0 " + 
                            "	       END AS prima_bruta_uf_recibo, " + 
                            "	       CASE vis.id_estado_recibo  " + 
                            "	          WHEN '01' THEN 0 " + 
                            "	          ELSE vis.prima_pesos_recibo " + 
                            "	       END AS prima_pesos_recibo, " + 
                            "	       CASE vis.id_estado_recibo  " + 
                            "	          WHEN '01' " + 
                            "	             THEN 'POR PAGAR' " + 
                            "	          ELSE 'PAGADO' " + 
                            "	       END AS id_estado_recibo, " + 
                            "	       vis.folio_recibo " + 
                            "	  FROM polizas_marketing_vw vis JOIN bpi_pro_productos_tbl pro " + 
                            "	       ON (   (vis.id_ramo <> 0 AND vis.id_ramo + 2000 = pro.cod_producto) " + 
                            "	           OR (vis.id_ramo = 0 AND vis.id_lproducto + 1000 = pro.cod_producto " + 
                            "	              ) " + 
                            "	          ) " + 
                            "	 WHERE vis.rut_agente = ?) datos " + 
                            "         WHERE ROWNUM <= ?) " + 
                            " WHERE rnum >= ? ");
            stm.setInt(1, rut_agente);
            stm.setInt(2, rowhasta);
            stm.setInt(3, rowdesde);
            rst = stm.executeQuery();
            while (rst.next()) {
                ReporteAgentePolizas dto = new ReporteAgentePolizas();       
                dto.setContratante(rst.getString("contratante"));
                dto.setDv_contratante(rst.getString("dv_rut_contratante"));
                dto.setEstado(rst.getString("id_estado_recibo"));
                dto.setFecha_recibo(FechaUtil.toUtilDate(rst.getDate("f_emision_recibo")));
                dto.setFolio_recibo(rst.getInt("folio_recibo"));
                dto.setId_ramo(rst.getInt("id_ramo"));
                dto.setPoliza_pol(rst.getInt("poliza_pol"));
                dto.setPrima_bruta(rst.getInt("prima_pesos_recibo"));
                dto.setPrima_bruta_uf(rst.getDouble("prima_bruta_uf_recibo"));
                dto.setProducto(rst.getString("id_lproducto"));
                dto.setRut_contratante(rst.getInt("rut_contratante"));
                dtos.add(dto);
            }              
        } catch (Exception e) {
            logger.error("generateReporteAgenteByRutPolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("generateReporteAgenteByRutPolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("generateReporteAgenteByRutPolizas() - termina");
        return dtos;   
    }

    /**
     * Genera reporte de supervisor en base al
     * rut del supervisor para polizas
     * @param rut_supervisor
     * @return
     * TODO: Testear
     */
    public List<ReporteSupervisorPolizas> generateReporteSupervisorByRutPolizas(Integer rut_supervisor) {
        logger.info("generateReporteSupervisorByRutPolizas() - inicia");
        List<ReporteSupervisorPolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT   agsup.numero_agente, agsup.nombre, " + 
                            "         SUM (CASE pol.id_estado_recibo " + 
                            "                 WHEN '01' " + 
                            "                    THEN 0 " + 
                            "                 ELSE pol.prima_pesos_recibo " + 
                            "              END " + 
                            "             ) AS rut_agente, " + 
                            "         SUM (CASE pol.id_estado_recibo " + 
                            "                 WHEN '01' " + 
                            "                    THEN pol.prima_bruta_uf_recibo " + 
                            "                 ELSE 0 " + 
                            "              END " + 
                            "             ) AS numero_supervisor " + 
                            "    FROM polizas_marketing_vw pol, agentes_supervisores_vw agsup " + 
                            "   WHERE pol.numero_agente = agsup.numero_agente " + 
                            "     AND agsup.rut_supervisor = ? " + 
                            "GROUP BY agsup.numero_agente, agsup.nombre ");
            stm.setInt(1, rut_supervisor);
            rst = stm.executeQuery();
            while (rst.next()) {
                ReporteSupervisorPolizas dto = new ReporteSupervisorPolizas();       
                dto.setNom_agente(rst.getString("nombre"));
                dto.setNumero_agente(rst.getInt("numero_agente"));
                dto.setPor_recaudar(rst.getLong("numero_supervisor"));
                dto.setRecaudado(rst.getLong("rut_agente"));
                dtos.add(dto);
            }              
        } catch (Exception e) {
            logger.error("generateReporteSupervisorByRutPolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("generateReporteSupervisorByRutPolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("generateReporteSupervisorByRutPolizas() - termina");
        return dtos;  
    }

    /**
     * Genera reporte de jefe de sucursal en base
     * al rut del jefe de sucursal
     * @param rut_jefe_sucursal
     * @return
     * TODO: Testear
     */
    public List<ReporteJefeSucursalPolizas> generateReporteJefeSucursalByRutPolizas(Integer rut_jefe_sucursal) {
        logger.info("generateReporteJefeSucursalByRutPolizas() - inicia");
        List<ReporteJefeSucursalPolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT supjef.numero_supervisor, " + 
                            "       supjef.nombre, " + 
                            "       sum( " + 
                            "           case pol.id_estado_recibo " + 
                            "                 when '01' then  " + 
                            "                   0         " + 
                            "                 else  " + 
                            "                   pol.prima_pesos_recibo " + 
                            "                 end " + 
                            "           )as rut_agente, " + 
                            "            " + 
                            "       sum( " + 
                            "           case pol.id_estado_recibo " + 
                            "                 when '01' then  " + 
                            "                   pol.prima_bruta_uf_recibo         " + 
                            "                 else  " + 
                            "                   0 " + 
                            "                 end " + 
                            "           )as numero_agente " + 
                            "                  " + 
                            "    FROM  Polizas_Marketing_VW pol, Supervisores_JefeSuc_VW supjef " + 
                            "    WHERE pol.numero_agente = supjef.numero_agente AND " + 
                            "          supjef.rut_jefe_sucursal = ? " + 
                            "    GROUP BY supjef.numero_supervisor, supjef.nombre");
            stm.setInt(1, rut_jefe_sucursal);
            rst = stm.executeQuery();
            while (rst.next()) {
                ReporteJefeSucursalPolizas dto = new ReporteJefeSucursalPolizas();       
                dto.setNom_supervisor(rst.getString("nombre"));
                dto.setNumero_supervisor(rst.getInt("numero_supervisor"));
                dto.setPor_recaudar(rst.getLong("numero_agente"));
                dto.setRecaudado(rst.getLong("rut_agente"));
                dtos.add(dto);
            }              
        } catch (Exception e) {
            logger.error("generateReporteJefeSucursalByRutPolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("generateReporteJefeSucursalByRutPolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("generateReporteJefeSucursalByRutPolizas() - termina");
        return dtos;  

    }

    /**
     * Genera reporte de Jefe de Zona en base al 
     * rut del jefe de zona para las polizas
     * @param rut_jefe_zona
     * @return
     * TODO: Testear
     */
    public List<ReporteJefeZonalPolizas> generateReporteJefeZonalByRutPolizas(Integer rut_jefe_zona) {
        logger.info("generateReporteJefeZonalByRutPolizas() - inicia");
        List<ReporteJefeZonalPolizas> dtos = new ArrayList();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT suczon.numero_jefe_sucursal, " + 
                                "       suczon.nombre, " + 
                                "       sum( " + 
                                "           case pol.id_estado_recibo " + 
                                "                 when '01' then  " + 
                                "                   0         " + 
                                "                 else  " + 
                                "                   pol.prima_pesos_recibo " + 
                                "                 end " + 
                                "           )as rut_agente, " + 
                                "            " + 
                                "       sum( " + 
                                "           case pol.id_estado_recibo " + 
                                "                 when '01' then  " + 
                                "                   pol.prima_bruta_uf_recibo         " + 
                                "                 else  " + 
                                "                   0 " + 
                                "                 end " + 
                                "           )as numero_agente " + 
                                "                  " + 
                                "    FROM  Polizas_Marketing_VW pol, JefeSuc_JefeZon_VW suczon " + 
                                "    WHERE pol.numero_agente = suczon.numero_agente AND " + 
                                "           suczon.rut_jefe_zona = ? " + 
                                "    GROUP BY suczon.numero_jefe_sucursal, suczon.nombre");
            stm.setInt(1, rut_jefe_zona);
            rst = stm.executeQuery();
            while (rst.next()) {
                ReporteJefeZonalPolizas dto = new ReporteJefeZonalPolizas();       
                dto.setNom_jefesuc(rst.getString("nombre"));
                dto.setNumero_jefesuc(rst.getInt("numero_jefe_sucursal"));
                dto.setPor_recaudar(rst.getLong("numero_agente"));
                dto.setRecaudado(rst.getLong("rut_agente"));
                dtos.add(dto);
            }              
        } catch (Exception e) {
            logger.error("generateReporteJefeZonalByRutPolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("generateReporteJefeZonalByRutPolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("generateReporteJefeZonalByRutPolizas() - termina");
        return dtos;   

    }
    
    /**
     * Reporte de consulta de agentes por polizas
     * segun el numero de agente
     * @param numero_agente
     * @return
     * TODO: Testear
     */
    public ReporteAgentePolizas getCountFindReporteAgentePolizas(Integer numero_agente) {
        logger.info("getCountFindReporteAgentePolizas() - inicia");
        ReporteAgentePolizas dtos = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT COUNT (*) as poliza_pol, 0 as id_ramo, 0 as folio_recibo, SYSDATE as f_emision_recibo  " + 
                                "  FROM polizas_marketing_vw vis JOIN bpi_pro_productos_tbl pro " + 
                                "       ON (   (vis.id_ramo <> 0 AND vis.id_ramo + 2000 = pro.cod_producto) " + 
                                "           OR (vis.id_ramo = 0 AND vis.id_lproducto + 1000 = pro.cod_producto " + 
                                "              ) " + 
                                "          ) " + 
                                " WHERE vis.numero_agente = ?");
            stm.setInt(1, numero_agente);
            rst = stm.executeQuery();
            while (rst.next()) {
                dtos = new ReporteAgentePolizas();       
                dtos.setPoliza_pol(rst.getInt("poliza_pol"));
                dtos.setId_ramo(rst.getInt("id_ramo"));
                dtos.setFolio_recibo(rst.getInt("folio_recibo"));
                dtos.setFecha_recibo(FechaUtil.toUtilDate(rst.getDate("f_emision_recibo")));
            }              
        } catch (Exception e) {
            logger.error("getCountFindReporteAgentePolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("getCountFindReporteAgentePolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("getCountFindReporteAgentePolizas() - termina");
        return dtos;      

    }

    /**
     * Consulta cantidad para reporte
     * de agentes segun rut de agente
     * @param rut_agente
     * @return
     */
    public ReporteAgentePolizas getCountFindReporteAgenteByRutPolizas(Integer rut_agente) {
        logger.info("getCountFindReporteAgenteByRutPolizas() - inicia");
        ReporteAgentePolizas dtos = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT COUNT (*) as poliza_pol, 0 as id_ramo, 0 as folio_recibo, SYSDATE as f_emision_recibo " + 
                                    "  FROM polizas_marketing_vw vis JOIN bpi_pro_productos_tbl pro " + 
                                    "       ON (   (vis.id_ramo <> 0 AND vis.id_ramo + 2000 = pro.cod_producto) " + 
                                    "           OR (vis.id_ramo = 0 AND vis.id_lproducto + 1000 = pro.cod_producto " + 
                                    "              ) " + 
                                    "          ) " + 
                                    " WHERE vis.rut_agente = ?");
            stm.setInt(1, rut_agente);
            rst = stm.executeQuery();
            while (rst.next()) {
                dtos = new ReporteAgentePolizas();       
                dtos.setPoliza_pol(rst.getInt("poliza_pol"));
                dtos.setId_ramo(rst.getInt("id_ramo"));
                dtos.setFolio_recibo(rst.getInt("folio_recibo"));
                dtos.setFecha_recibo(FechaUtil.toUtilDate(rst.getDate("f_emision_recibo")));
            }              
        } catch (Exception e) {
            logger.error("getCountFindReporteAgenteByRutPolizas() - catch (error)", e);
            dtos = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("getCountFindReporteAgenteByRutPolizas() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("getCountFindReporteAgenteByRutPolizas() - termina");
        return dtos;       }
}
