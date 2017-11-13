package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.AperturaCaja;
import cl.bice.vida.botonpago.common.dto.general.Comprobantes;
import cl.bice.vida.botonpago.common.dto.general.ConsultaDetalleServicioPec;
import cl.bice.vida.botonpago.common.dto.general.DetalleServicioPec;
import cl.bice.vida.botonpago.common.dto.general.DetalleTransaccionByEmp;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.dto.general.ServicioPec;
import cl.bice.vida.botonpago.common.dto.general.Transacciones;
import cl.bice.vida.botonpago.common.dto.parametros.ValorUf;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosOfflineVw;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalOfflineVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalVw;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.common.util.StringUtil;
import cl.bice.vida.botonpago.common.util.XmlUtil;
import cl.bice.vida.botonpago.modelo.dto.BpiPecConsultaTransaccionDto;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import oracle.jdbc.OracleTypes;

import oracle.xdb.XMLType;

import oracle.xml.parser.v2.XMLDocument;

import org.apache.log4j.Logger;


public class ConsultasPecDAOImpl extends DataSourceBice implements ConsultasPecDAO {

    /*
     * Constantes de la clase
     */
    private final int ESTADO_RECHAZO_POR_MEDIO_PAGO = 99;
    
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(PersistenciaGeneralDAOImpl.class);

    public ConsultasPecDAOImpl() {
    }

    public AperturaCaja findCajaAbierta() {
        return null;
    }

    public BhDividendosVw findOldestDividendoByRut(String rut) {
        return null;
    }

    public BhDividendosOfflineVw findOldestDividendosOfflineByRut(String rut) {
        return null;
    }

    public List<BhDividendosVw> findOlderDividendoByRut(String rut) {
        while(rut.length()<9) rut = "0"+rut;
        
        List<BhDividendosVw> results = DAOFactory.getPersistenciaGeneralDao().findDividendosByRut(rut);
        
        Iterator resulti = results.iterator();
        Collection resultsd = new ArrayList();
        BhDividendosVw divant = null;
        while(resulti.hasNext()) {
            BhDividendosVw dividendo = (BhDividendosVw)resulti.next();
            if(divant!=null && divant.getNumOpe().intValue()==dividendo.getNumOpe().intValue()) {
                resultsd.add(dividendo);
            } else{
                divant = dividendo;
            }
        }
        
        results.removeAll(resultsd);
        return results;
        
    }

    public List<BhDividendosOfflineVw> findOlderDividendosOfflineByRut(String rut) {
        while(rut.length()<9) rut = "0"+rut;
        List<BhDividendosOfflineVw> results = DAOFactory.getPersistenciaGeneralDao().findDividendosOfflineByRut(rut);
        Iterator resulti = results.iterator();
        Collection resultsd = new ArrayList();
        BhDividendosOfflineVw divant = null;
        while(resulti.hasNext()) {
            BhDividendosOfflineVw dividendo = (BhDividendosOfflineVw)resulti.next();
            if(divant!=null && divant.getNumOpe().intValue()==dividendo.getNumOpe().intValue()) {
                resultsd.add(dividendo);
            } else{
                divant = dividendo;
            }
        }
        
        results.removeAll(resultsd);
        
        return results;
    }

    public IndPrimaNormalVw findOldestPolizasByRut(Integer rut) {
        return null;
    }

    public IndPrimaNormalOfflineVw findOldestPolizasOfflineByRut(Integer rut) {
        return null;
    }

    public List<IndPrimaNormalVw> findOlderPolizasByRut(Integer rut) {    
        
        List<IndPrimaNormalVw> polizas = DAOFactory.getPersistenciaGeneralDao().findPolizasByRut(rut);
        
        Iterator resulti = polizas.iterator();
        Collection resultsd = new ArrayList();
        IndPrimaNormalVw polant = null;
        while(resulti.hasNext()) {
            IndPrimaNormalVw poliza = (IndPrimaNormalVw)resulti.next();
            if(polant!=null && polant.getRamo().intValue()==poliza.getRamo().intValue() && polant.getPolizaPol().intValue()==poliza.getPolizaPol().intValue()) {
                resultsd.add(poliza);
            } else{
                polant = poliza;
            }
        }        
        polizas.removeAll(resultsd);        
        return polizas;
    
    }

    public List<IndPrimaNormalOfflineVw> findOlderPolizasOfflineByRut(Integer rut) {
    
        List<IndPrimaNormalOfflineVw> polizas = DAOFactory.getPersistenciaGeneralDao().findPolizasOfflineByRut(rut);
        Iterator resulti = polizas.iterator();
        Collection resultsd = new ArrayList();
        IndPrimaNormalVw polant = null;
        while(resulti.hasNext()) {
            IndPrimaNormalVw poliza = (IndPrimaNormalVw)resulti.next();
            if(polant!=null && polant.getRamo().intValue()==poliza.getRamo().intValue() && polant.getPolizaPol().intValue()==poliza.getPolizaPol().intValue()) {
                resultsd.add(poliza);
            } else{
                polant = poliza;
            }
        }        
        polizas.removeAll(resultsd);        
        return polizas;
    }

    public ValorUf findValorUfByDate(Date fecha) {
        return null;
    }

    public ServicioPec findServicioPecByIdtransaccion(Long id) {
        logger.info("findServicioPecByIdtransaccion() - inicia");
        ServicioPec dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT * FROM BPI_PEC_SERVICIOPEC_TBL WHERE ID_TRANSACCION = ?");
            stm.setLong(1, id);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new ServicioPec(); 
                dto.setIdNavegacion(rst.getLong("ID_NAVEGACION"));                
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));                
                dto.setRutPersona(rst.getLong("RUT_PERSONA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setMontoTransaccion(rst.getInt("MONTO_TRANSACCION"));
                dto.setCodMedio(rst.getInt("COD_MEDIO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));                
                
            }            

        } catch (Exception e) {
            logger.error("findServicioPecByIdtransaccion() - catch (error)", e);
            dto = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("findServicioPecByIdtransaccion() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {logger.error("findServicioPecByIdtransaccion() - catch (error)", e);}
        }
        logger.info("findServicioPecByIdtransaccion() - termina");
        return dto;
    }

    public Transacciones findTransaccionById(Long id) {
        return null;
    }

    public ConsultaDetalleServicioPec findXMLConfirmacionPECByIdTransaccion(Long idtransaccion) {
        logger.info("ConsultaDetalleServicioPec() - inicia");
        ConsultaDetalleServicioPec dto = null;
        ServicioPec dtopec = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            String sql = "SELECT A.ID_NAVEGACION, A.ID_TRANSACCION, A.RUT_PERSONA, A.MONTO_TRANSACCION AS MONTO_TRANSACCION_NAV, \n" + 
            "       A.COD_MEDIO, A.NUM_TRANSACCION_MEDIO, B.ENTRADA, B.COD_PAGINA, B.FECHA_HORA, \n" + 
            "       B.MONTO_TRANSACCION, B.DETALLE_PAGINA.GETCLOBVAL() \n" + 
            "FROM BICEVIDA.BPI_PEC_SERVICIOPEC_TBL A, BICEVIDA.BPI_DPE_DETSERVPEC_TBL B\n" + 
            "WHERE B.COD_PAGINA = 3\n" + 
            "AND A.ID_NAVEGACION = B.ID_NAVEGACION\n" + 
            "AND A.ID_TRANSACCION = ?";
            stm = cnx.prepareCall(sql);
            stm.setLong(1, idtransaccion);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new ConsultaDetalleServicioPec();
                dtopec = new ServicioPec();
                
                dtopec.setCodMedio(rst.getInt("COD_MEDIO"));
                dtopec.setFechaHora(FechaUtil.toUtilDate(rst.getTimestamp("FECHA_HORA")));
                dtopec.setIdNavegacion(rst.getLong("ID_NAVEGACION"));
                dtopec.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dtopec.setMontoTransaccion(rst.getInt("MONTO_TRANSACCION_NAV"));
                dtopec.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dtopec.setRutPersona(rst.getLong("RUT_PERSONA"));
                
                dto.setCodPagina(rst.getInt("COD_PAGINA"));
                dto.setEntrada(rst.getInt("ENTRADA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getTimestamp("FECHA_HORA")));
                dto.setIdNavegacion(rst.getLong("ID_NAVEGACION"));
                dto.setMontoTransaccion(rst.getInt("MONTO_TRANSACCION"));
                dto.setServicioPEC(dtopec);
                java.sql.Clob clb = rst.getClob(11);    
                String xml = StringUtil.clobToString(clb);                      
                dto.setXmldetalle((XMLDocument)XmlUtil.parse(xml));
            }

        } catch (Exception e) {
            logger.error("ConsultaDetalleServicioPec() - catch (error)", e);
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("ConsultaDetalleServicioPec() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {logger.error("ConsultaDetalleServicioPec() - catch (error)", e);}
        }
        logger.info("ConsultaDetalleServicioPec() - termina");
        return dto;
    }
    
    public ConsultaDetalleServicioPec findDetalleServicioPECByIdTransaccionPagina(Long idtransaccion, Long cod_pagina) {
        logger.info("ConsultaDetalleServicioPec() - inicia");
        ConsultaDetalleServicioPec dto = null;
        ServicioPec dtopec = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            String sql = "SELECT A.ID_NAVEGACION, A.ID_TRANSACCION, A.RUT_PERSONA, A.MONTO_TRANSACCION AS MONTO_TRANSACCION_NAV, \n" + 
            "       A.COD_MEDIO, A.NUM_TRANSACCION_MEDIO, B.ENTRADA, B.COD_PAGINA, B.FECHA_HORA, \n" + 
            "       B.MONTO_TRANSACCION, B.DETALLE_PAGINA.GETCLOBVAL() \n" + 
            "FROM BICEVIDA.BPI_PEC_SERVICIOPEC_TBL A, BICEVIDA.BPI_DPE_DETSERVPEC_TBL B\n" + 
            "WHERE B.COD_PAGINA = ?\n" + 
            "AND A.ID_NAVEGACION = B.ID_NAVEGACION\n" + 
            "AND A.ID_TRANSACCION = ?";
            stm = cnx.prepareCall(sql);
            stm.setLong(1, cod_pagina);
            stm.setLong(2, idtransaccion);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new ConsultaDetalleServicioPec();
                dtopec = new ServicioPec();
                
                dtopec.setCodMedio(rst.getInt("COD_MEDIO"));
                dtopec.setFechaHora(FechaUtil.toUtilDate(rst.getTimestamp("FECHA_HORA")));
                dtopec.setIdNavegacion(rst.getLong("ID_NAVEGACION"));
                dtopec.setIdTransaccion(rst.getLong("ID_TRANSACCION"));
                dtopec.setMontoTransaccion(rst.getInt("MONTO_TRANSACCION_NAV"));
                dtopec.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                dtopec.setRutPersona(rst.getLong("RUT_PERSONA"));
                
                dto.setCodPagina(rst.getInt("COD_PAGINA"));
                dto.setEntrada(rst.getInt("ENTRADA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getTimestamp("FECHA_HORA")));
                dto.setIdNavegacion(rst.getLong("ID_NAVEGACION"));
                dto.setMontoTransaccion(rst.getInt("MONTO_TRANSACCION"));
                dto.setServicioPEC(dtopec);
                java.sql.Clob clb = rst.getClob(11);    
                String xml = StringUtil.clobToString(clb);                      
                dto.setXmldetalle((XMLDocument)XmlUtil.parse(xml));
            }

        } catch (Exception e) {
            logger.error("ConsultaDetalleServicioPec() - catch (error)", e);
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("ConsultaDetalleServicioPec() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {logger.error("ConsultaDetalleServicioPec() - catch (error)", e);}
        }
        logger.info("ConsultaDetalleServicioPec() - termina");
        return dto;
    }
    
    
    /**
     * Actualiza el estado de una transaccion t el numero de transaccion del medio
     * @param idtransaccion
     * @param cod_estado
     * @param numtransaccionmedio
     * @return
     */
     public Transacciones updateTransaccion(Long idtransaccion, 
                                            Integer cod_estado, 
                                            Long numtransaccionmedio) {
        logger.info("updateTransaccion() - iniciando");        
        Transacciones dto = DAOFactory.getPersistenciaGeneralDao().findTransaccionById(idtransaccion.intValue());
        logger.info("updateTransaccion() - consulta de informacion obtenido, procediendo a actualizar el dato");
        Connection cnx = null;
        try {
        
            //Setea nuevos valores
            dto.setCod_estado(cod_estado);
            dto.setNumtransaccionmedio(numtransaccionmedio);

            cnx = getConnection();
            PreparedStatement stm = cnx.prepareStatement("UPDATE BPI_TRA_TRANSACCIONES_TBL SET COD_ESTADO = ?, NUM_TRANSACCION_MEDIO = ? WHERE ID_TRANSACCION = ?");

            // SET PARAMETROS
            stm.setInt(1, dto.getCod_estado());
            stm.setLong(2, dto.getNumtransaccionmedio());
            stm.setLong(3, dto.getIdTransaccion());
            
            logger.info("updateTransaccion() - getCod_estado :" + dto.getCod_estado());
            logger.info("updateTransaccion() - getIdTransaccion :" + dto.getIdTransaccion());

            // EXECUTE
            stm.execute();
            stm.close();            
            
        } catch (Exception e) {
            logger.error("updateTransaccion() - catch (error)", e);
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {logger.error("updateTransaccion() - catch (error)", e);}
        }
        logger.info("updateTransaccion() - terminado");    
        return dto;
    }
    
    /**
     * Actualiza el estado de una transaccion t el numero de transaccion del medio
     * @param transaccion     
     * @return
     */
     public Transacciones updateTransaccionByDTO(Transacciones transaccion) {
        logger.info("updateTransaccionByDTO() - iniciando");        
        //Transacciones dto = DAOFactory.getPersistenciaGeneralDao().findTransaccionById(idtransaccion.intValue());
        logger.info("updateTransaccionByDTO() - consulta de informacion obtenido, procediendo a actualizar el dato");
        Connection cnx = null;
        try {
        
            //Setea nuevos valores
           

            cnx = getConnection();
            PreparedStatement stm = cnx.prepareStatement("UPDATE BPI_TRA_TRANSACCIONES_TBL SET COD_ESTADO = ?, NUM_TRANSACCION_MEDIO = ?, MONTO_TOTAL = ? WHERE ID_TRANSACCION = ?");

            // SET PARAMETROS
            stm.setInt(1, transaccion.getCod_estado());
            stm.setLong(2, transaccion.getNumtransaccionmedio());
            stm.setLong(3, transaccion.getMontoTotal());
            stm.setLong(4, transaccion.getIdTransaccion());
            
            logger.info("updateTransaccionByDTO() - getCod_estado :" + transaccion.getCod_estado());
            logger.info("updateTransaccionByDTO() - getMontoTotal :" + transaccion.getMontoTotal());
            logger.info("updateTransaccionByDTO() - getIdTransaccion :" + transaccion.getIdTransaccion());

            // EXECUTE
            stm.execute();
            stm.close();            
            
        } catch (Exception e) {
            logger.error("updateTransaccionByDTO() - catch (error)", e);
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {logger.error("updateTransaccion() - catch (error)", e);}
        }
        logger.info("updateTransaccionByDTO() - terminado");    
        return transaccion;
    }



    public void updateDetalleTransaccion(Transacciones transaccion,
                                         List<DetalleTransaccionByEmp> detallesByEmp, 
                                         List<Comprobantes> comprobantes) {
        logger.info("updateDetalleTransaccion() - inicio");
        transaccion.setComprobantesCollection(comprobantes);
        Iterator insertscom = comprobantes.iterator();
        while (insertscom.hasNext()) {
            Comprobantes dto = (Comprobantes) insertscom.next();
            logger.info("updateDetalleTransaccion() - insertar comprobante : " + dto.getNumProducto());
            boolean resultado = insertComprobante(dto);
        }

        DetalleTransaccionByEmp dto2;
        Iterator insertsdet = detallesByEmp.iterator();
        while (insertsdet.hasNext()) {
            DetalleTransaccionByEmp dto = (DetalleTransaccionByEmp) insertsdet.next();
            logger.info("updateDetalleTransaccion() - insertar detalleempresa : " + dto.getNumTransaccionCaja());
            boolean resultado = insertDetalleTransaccionEmp(dto);
        }
        
        /*
         * update transaccion
         */
         logger.info("updateDetalleTransaccion() - update transacción");
         updateTransaccionByDTO(transaccion);        
        
        logger.info("updateDetalleTransaccion() - termino");
    }

    /**
     * Genera nueva navegacion del servicio PEC
     * @param rut
     * @param medio
     * @return
     */
    public ServicioPec newServicioPec(long rut, int medio) {
        logger.info("newServicioPec() - iniciando");
        Connection cnx = null;
        ServicioPec dto = new ServicioPec();
        try {
        
            //Recuepera secuencia
            Long idnavegacion = new Long(Integer.toString(getSecuenceValue("bpi_pec_idnavegacion_seq")));
            logger.info("newServicioPec() - secuencia idnavegacion :" + idnavegacion);
          
            //Genera el contenido del DTO a enviar
            dto.setIdNavegacion(idnavegacion);
            dto.setIdTransaccion(null);
            dto.setRutPersona(rut);
            dto.setFechaHora(new Date());
            dto.setMontoTransaccion(null);
            dto.setCodMedio(medio);
            dto.setNumTransaccionMedio(null);
            
            cnx = getConnection();
            //Inserta y deja activo el dispositivo
            PreparedStatement stm = cnx.prepareStatement("INSERT INTO BPI_PEC_SERVICIOPEC_TBL A (A.ID_NAVEGACION, A.ID_TRANSACCION, A.RUT_PERSONA, A.FECHA_HORA, A.MONTO_TRANSACCION, A.COD_MEDIO, A.NUM_TRANSACCION_MEDIO ) VALUES (?, ?, ?, ?, ?, ?, ?)");

            // SET PARAMETROS
            
            if (dto.getIdNavegacion() != null)          stm.setLong(1, dto.getIdNavegacion());
            if (dto.getIdTransaccion() != null)         stm.setLong(2, dto.getIdTransaccion());
            if (dto.getRutPersona() != null)            stm.setLong(3, dto.getRutPersona());
            if (dto.getFechaHora() != null)             stm.setTimestamp(4, new Timestamp(dto.getFechaHora().getTime()));
            if (dto.getMontoTransaccion() != null)      stm.setInt(5, dto.getMontoTransaccion());
            if (dto.getCodMedio() != null)              stm.setInt(6, dto.getCodMedio()); 
            if (dto.getNumTransaccionMedio() != null)   stm.setLong(7, dto.getNumTransaccionMedio()); 
                        
            if (dto.getIdNavegacion() == null)     stm.setNull(1, OracleTypes.NUMBER);  
            if (dto.getIdTransaccion() == null)       stm.setNull(2, OracleTypes.NUMBER); 
            if (dto.getRutPersona() == null)       stm.setNull(3, OracleTypes.NUMBER); 
            if (dto.getFechaHora() == null)             stm.setNull(4, OracleTypes.DATE); 
            if (dto.getMontoTransaccion() == null)         stm.setNull(5, OracleTypes.NUMBER); 
            if (dto.getCodMedio() == null)    stm.setNull(6, OracleTypes.NUMBER); 
            if (dto.getNumTransaccionMedio() == null)        stm.setNull(7, OracleTypes.NUMBER);        
           
            logger.info("newNavegacion() - getIdNavegacion :" + dto.getIdNavegacion());
            logger.info("newServicioPec() - getIdTransaccion :" + dto.getIdTransaccion());
            logger.info("newServicioPec() - getRutPersona :" + dto.getRutPersona());
            logger.info("newServicioPec() - getFechaHora :" + dto.getFechaHora());
            logger.info("newServicioPec() - getMontoTransaccion :" + dto.getMontoTransaccion());
            logger.info("newServicioPec() - getCodMedio :" + dto.getCodMedio());
            logger.info("newServicioPec() - getNumTransaccionMedio :" + dto.getNumTransaccionMedio());
            
            // EXECUTE
            stm.execute();
            stm.close();
            
        } catch (Exception e) {
            logger.error("newNavegacion() - catch (error)", e);
            dto = null;
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {}
        }
        return dto;       
    }

    /**
     * Actualiza la transacción en la navegación
     * @param servicio
     * @param transaccion
     * @param monto
     * @throws Exception
     */
    public void updateTransaccionInServicioPec(ServicioPec servicio,Long transaccion, Integer monto) {
        logger.info("updateTransaccionInServicioPec() - iniciando y buscando navegacion");        
        ServicioPec dto = findServicioPecById(servicio.getIdNavegacion());
        logger.info("updateTransaccionInServicioPec() - iniciando update");
        Connection cnx = null;
        try {
        
            //Setea nuevos valores
            dto.setMontoTransaccion(monto);
            dto.setIdTransaccion(transaccion);
            dto.setCodMedio(servicio.getCodMedio());

            cnx = getConnection();
            PreparedStatement stm = cnx.prepareStatement("UPDATE BPI_PEC_SERVICIOPEC_TBL SET MONTO_TRANSACCION = ?, ID_TRANSACCION = ?, COD_MEDIO = ? WHERE ID_NAVEGACION = ?");

            // SET PARAMETROS
            stm.setInt(1, dto.getMontoTransaccion());
            stm.setLong(2, dto.getIdTransaccion());
            stm.setInt(3, dto.getCodMedio());
            stm.setLong(4, servicio.getIdNavegacion());
            
            logger.info("updateTransaccionInServicioPec() - getMontoTransaccion : " + dto.getMontoTransaccion());
            logger.info("updateTransaccionInServicioPec() - getIdTransaccion : " + dto.getIdTransaccion());
            logger.info("updateTransaccionInServicioPec() - getCod_medio : " + servicio.getCodMedio());
            logger.info("updateTransaccionInServicioPec() - idNavegacion : " + servicio.getIdNavegacion());

            // EXECUTE
            stm.execute();
            stm.close();
            
        } catch (Exception e) {
            logger.error("updateTransaccionInServicioPec() - catch (error)", e);
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {logger.error("updateTransaccionInServicioPec() - catch(errror)", e);}
        }
        logger.info("updateTransaccionInServicioPec() - terminado");              
    }


    /**
     * Metodo para crear un detalle de navegacion PEC
     * @param nav
     * @param entrada
     * @param cod_pagina
     * @param monto
     * @param xml
     * @return dto con detalle de navegación PEC
     */
    public DetalleServicioPec createDetalleServPec(long nav, int entrada, int cod_pagina, int monto, String xml) {    
        DetalleServicioPec detalle = newDetalleServPec(nav,entrada,cod_pagina, monto);
        updateDetNavegacionXML(detalle,xml);          
        return detalle;
    }

    /**
     * Metodo que genera la navegacion de XML Servicio PEC
     * una ves que es consultado
     * @param nav
     * @param entrada
     * @param cod_pagina
     * @param monto
     * @param idCanal
     * @return dto de newDetalleServPec
     */
    public DetalleServicioPec newDetalleServPec(Long nav,int entrada,int cod_pagina, Integer monto) {    
        logger.info("newDetalleServPec() - iniciando");
        Connection cnx = null;
        DetalleServicioPec dto = new DetalleServicioPec();
        try {
            //Genera el contenido del DTO a enviar
            dto.setCodPagina(cod_pagina);
            dto.setDetallePagina("<Vacio/>");
            dto.setEntrada(entrada);
            dto.setFechaHora(new Date());
            dto.setIdNavegacion(nav);
            dto.setMontoTransaccion(monto);                        
            
            
            cnx = getConnection();
            //Inserta y deja activo el dispositivo
            PreparedStatement stm = cnx.prepareStatement("INSERT INTO BPI_DPE_DETSERVPEC_TBL (ID_NAVEGACION, ENTRADA, COD_PAGINA, FECHA_HORA, MONTO_TRANSACCION, DETALLE_PAGINA) VALUES(?,?,?,?,?,?)");

            // SET PARAMETROS
            stm.setLong(1, dto.getIdNavegacion());
            stm.setInt(2, dto.getEntrada());
            stm.setInt(3, dto.getCodPagina()); 
            stm.setTimestamp(4, new Timestamp(dto.getFechaHora().getTime()));
            stm.setInt(5, dto.getMontoTransaccion());
            stm.setString(6, dto.getDetallePagina());
            
            
            logger.info("newDetalleServPec() - getCodpagina :" + dto.getCodPagina());
            logger.info("newDetalleServPec() - getIdNavegacion :" + dto.getIdNavegacion());
            logger.info("newDetalleServPec() - getFechaHora :" + dto.getFechaHora());
            logger.info("newDetalleServPec() - getMontoTransaccion :" + dto.getMontoTransaccion());
            logger.info("newDetalleServPec() - getDetallePagina :" + dto.getDetallePagina());
            logger.info("newDetalleServPec() - getEntrada :" + dto.getEntrada());
            
            // EXECUTE
            stm.execute();
            stm.close();
            
        } catch (Exception e) {
            logger.error("newDetalleServPec() - catch (error)", e);
            dto = null;
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {logger.error("newDetalleServPec() - catch(errror)", e);}
        }
        logger.info("newDetalleServPec() - terminando");
        return dto;
    }

    public void updateDetNavegacionXML(DetalleServicioPec detalle, 
                              String xml) {
        logger.info("updateDetNavegacionPecXML() - iniciando");
        Connection cnx = null;
        try {
            if (detalle != null) {
                cnx = getConnection();
                XMLType xmlType = XMLType.createXML(cnx, xml);
                Clob xmlClob = xmlType.getClobVal();
                
                //Inserta y deja activo el dispositivo
                PreparedStatement stm = cnx.prepareStatement("UPDATE BPI_DPE_DETSERVPEC_TBL SET DETALLE_PAGINA = XMLType(?), MONTO_TRANSACCION=? WHERE ID_NAVEGACION = ? AND ENTRADA = ?");
    
                // SET PARAMETROS
                stm.setClob(1, xmlClob);
                stm.setLong(2, detalle.getMontoTransaccion());
                stm.setLong(3, detalle.getIdNavegacion());
                stm.setInt(4, detalle.getEntrada());
                
                logger.info("updateDetNavegacionPecXML() - xmlClob : " + xmlClob);
                logger.info("updateDetNavegacionPecXML() - getIdNavegacion : " + detalle.getIdNavegacion());
                logger.info("updateDetNavegacionPecXML() - getEntrada : " + detalle.getEntrada());
                
                // EXECUTE
                stm.execute();
                stm.close();
            }            
        } catch (Exception e) {
            logger.error("updateDetNavegacionPecXML() - catch (error)", e);
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {logger.error("updateDetNavegacionXML() - catch (error)", e);}
        }
        logger.info("updateDetNavegacionPecXML() - terminado");
    }

    public void updateDetNavegacionXML(DetalleServicioPec detalle, String xml, 
                                       int monto) {
    }

    /**
     * Actualiza los datos de una transaccion
     * este metodo es muy utilizado por el front-end
     * @param dto
     * @return
     */
    public Boolean actualizaTransaccionSP(SPActualizarTransaccionDto dto) {
        logger.info("actualizaTransaccionSP() - inicia");
        Boolean resp = Boolean.FALSE;
        Connection cnx = null;
        CallableStatement stm = null;
        try {
            cnx = getConnection();
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
             stm = cnx.prepareCall("UPDATE BPI_TRA_TRANSACCIONES_TBL SET " +
                                   " OBSERVACIONES = ?, " +
                                   " NUM_TRANSACCION_MEDIO = ?, " +
                                   " MEDIO_NUM_CUOTAS = ?, " +
                                   " MEDIO_NUM_TARJETA = ?, " +
                                   " MEDIO_TIPO_TARJETA = ?, " +
                                   " MEDIO_FECHA_PAGO = ?, " +
                                   " MEDIO_CODIGO_RESP = ?, " +
                                   " MEDIO_NUM_ORDEN_COMP = ?, " +
                                   " MEDIO_CODIGO_AUTORIZA = ? " +
                                   " WHERE ID_TRANSACCION = ?");
             // SET PARAMETROS                 
             if (dto.getDescRet() != null )                 stm.setString(1, dto.getDescRet());
             if (dto.getIdTrxBanco() != null )              stm.setString(2, dto.getIdTrxBanco());
             if (dto.getMedioNumeroCuotas() != null )       stm.setInt(3, dto.getMedioNumeroCuotas());
             if (dto.getMedioNumeroTarjeta() != null )      stm.setString(4, dto.getMedioNumeroTarjeta());
             if (dto.getMedioTipoTarjeta() != null )        stm.setString(5, dto.getMedioTipoTarjeta());
             if (dto.getMedioFechaPago() != null )          stm.setTimestamp(6, new java.sql.Timestamp(dto.getMedioFechaPago().getTime()));
             if (dto.getMedioCodigoRespuesta() != null )    stm.setString(7, dto.getMedioCodigoRespuesta());
             if (dto.getMedioOrdenCompra() != null )        stm.setString(8, dto.getMedioOrdenCompra());
             if (dto.getMedioCodigoAutorizacion() != null ) stm.setString(9, dto.getMedioCodigoAutorizacion());
             
             if (dto.getDescRet() == null )                 stm.setNull(1, OracleTypes.VARCHAR); 
             if (dto.getIdTrxBanco() == null )              stm.setNull(2, OracleTypes.VARCHAR); 
             if (dto.getMedioNumeroCuotas() == null )       stm.setNull(3, OracleTypes.INTEGER); 
             if (dto.getMedioNumeroTarjeta() == null )      stm.setNull(4, OracleTypes.VARCHAR); 
             if (dto.getMedioTipoTarjeta() == null )        stm.setNull(5, OracleTypes.VARCHAR); 
             if (dto.getMedioFechaPago() == null )          stm.setNull(6, OracleTypes.DATE); 
             if (dto.getMedioCodigoRespuesta() == null )    stm.setNull(7, OracleTypes.VARCHAR); 
             if (dto.getMedioOrdenCompra() == null )        stm.setNull(8, OracleTypes.VARCHAR); 
             if (dto.getMedioCodigoAutorizacion() == null ) stm.setNull(9, OracleTypes.VARCHAR); 
             
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
                logger.info("actualizaTransaccionSP() - Ejecutando procedimiento alamacenado de Pago PLSQL : {call BPI_MCJ_MANEJOCAJA_PKG.ACTUALIZARTRANSACCION(?, ?, ?, ?, ?, ?, ?, ?)}");
                stm = cnx.prepareCall("{call BPI_MCJ_MANEJOCAJA_PKG.ACTUALIZARTRANSACCION(?, ?, ?, ?, ?, ?, ?, ?)}");
                stm.setLong(1, dto.getIdTrx());
                stm.setInt(2, dto.getCodRet());
                stm.setInt(3, dto.getNropPagos());
                stm.setInt(4, dto.getMontoPago());
                stm.setString(5, dto.getDescRet());
                stm.setLong(6, dto.getIdCom());
                if(dto.getIdTrxBanco() != null){
                    stm.setLong(7, Long.parseLong(dto.getIdTrxBanco()));
                }else{
                    stm.setLong(7, 0);
                }
                stm.setString(8, dto.getIndPago());           
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
            logger.error("actualizaTransaccionSP() - catch (error)", e);
            e.printStackTrace();
            try {
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("actualizaTransaccionSP() - catch (error)", f);
            }
        } finally {
            try {
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {}
        }
        logger.info("actualizaTransaccionSP() - termina");
        return resp;
    }

    public void SQLUpdateXML(DetalleServicioPec detalle, String xml) {
    }

    public void SQLUpdateXMLSuper(DetalleServicioPec detalle, String xml) {
    }

    public ServicioPec findServicioPecById(Long id) {
        logger.info("findNavegacionById() - inicia");
        ServicioPec dto = null;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();            
            stm = cnx.prepareCall("SELECT * FROM BPI_PEC_SERVICIOPEC_TBL WHERE ID_NAVEGACION = ?");
            stm.setLong(1, id);
            rst = stm.executeQuery();
            while (rst.next()) {
                dto = new ServicioPec(); 
                dto.setIdNavegacion(rst.getLong("ID_NAVEGACION"));                
                dto.setIdTransaccion(rst.getLong("ID_TRANSACCION"));                
                dto.setRutPersona(rst.getLong("RUT_PERSONA"));
                dto.setFechaHora(FechaUtil.toUtilDate(rst.getDate("FECHA_HORA")));
                dto.setMontoTransaccion(rst.getInt("MONTO_TRANSACCION"));
                dto.setCodMedio(rst.getInt("COD_MEDIO"));
                dto.setNumTransaccionMedio(rst.getLong("NUM_TRANSACCION_MEDIO"));
                
            }            

        } catch (Exception e) {
            logger.error("findNavegacionById() - catch (error)", e);
            dto = null;
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("findNavegacionById() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {logger.error("findNavegacionById() - catch (error)", e);}
        }
        logger.info("findNavegacionById() - termina");
        return dto;
    }    
    
    /**
     * Recupera un valor de secuencia
     * @param secuencename
     * @return
     */
    public int getSecuenceValue(String secuencename) {
        logger.info("getSecuenceValue() - inicia");
        int valor = 0;
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;
        try {
            cnx = getConnection();
            stm = cnx.prepareCall("select " + secuencename  + ".nextval from dual");
            rst = stm.executeQuery();
            while (rst.next()) {
               valor = rst.getInt(1);
            }            

        } catch (Exception e) {
            logger.error("getSecuenceValue() - catch (error)", e);
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("getSecuenceValue() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) { logger.error("getSecuenceValue() - catch (error)", e);}
        }
        logger.info("getSecuenceValue() - termina");
        return valor;    
                
    }
    
    /**
     * Inserta un comprobante
     * @param dto
     * @return
     */
    public Boolean insertComprobante(Comprobantes dto) {
        logger.info("insertComprobante() - iniciando");
        Connection cnx = null;
        boolean res = false;
        try {
            cnx = getConnection();
            //Inserta y deja activo el dispositivo
            PreparedStatement stm = cnx.prepareStatement("INSERT INTO BPI_COM_COMPROBANTES_TBL (ID_TRANSACCION, " +
                    "COD_PRODUCTO, NUM_PRODUCTO, CUOTA, MONTO_BASE, MONTO_EXCEDENTE, " +
                    "MONTO_TOTAL) VALUES(?,?,?,?,?,?,?)");

            // SET PARAMETROS
            if (dto.getIdTransaccion() != null)     stm.setLong(1, dto.getIdTransaccion());
            if (dto.getCodProducto() != null)       stm.setInt(2, dto.getCodProducto());
            if (dto.getNumProducto() != null)       stm.setLong(3, dto.getNumProducto());
            if (dto.getCuota() != null)             stm.setInt(4, dto.getCuota());
            if (dto.getMontoBase() != null)         stm.setInt(5, dto.getMontoBase());
            if (dto.getMontoExcedente() != null)    stm.setInt(6, dto.getMontoExcedente());
            if (dto.getMontoTotal() != null)        stm.setInt(7, dto.getMontoTotal());

            if (dto.getIdTransaccion() == null)     stm.setNull(1, OracleTypes.NUMBER);
            if (dto.getCodProducto() == null)       stm.setNull(2, OracleTypes.INTEGER);
            if (dto.getNumProducto() == null)       stm.setNull(3, OracleTypes.NUMBER);
            if (dto.getCuota() == null)             stm.setNull(4, OracleTypes.INTEGER);
            if (dto.getMontoBase() == null)         stm.setNull(5, OracleTypes.INTEGER);
            if (dto.getMontoExcedente() == null)    stm.setNull(6, OracleTypes.INTEGER);
            if (dto.getMontoTotal() == null)        stm.setNull(7, OracleTypes.INTEGER);

            // EXECUTE
            stm.execute();
            stm.close();
            res = true;

        } catch (Exception e) {
            logger.error("insertComprobante() - catch (error)", e);
            dto = null;
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {}
        }
        return res;
    }
    
    /**
     * Inserta un detalle de transaccion
     * @param dto
     * @return
     */
    public Boolean insertDetalleTransaccionEmp(DetalleTransaccionByEmp dto) {
        logger.info("insertDetalleTransaccionEmp() - iniciando");
        Connection cnx = null;
        boolean res = false;
        try {
            cnx = getConnection();
            //Inserta y deja activo el dispositivo
            PreparedStatement stm = cnx.prepareStatement("INSERT INTO BPI_DTE_DETTRANSEMP_TBL (COD_EMPRESA, ID_TRANSACCION, " +
                                "MONTO_TOTAL, NUM_TRANSACCION_CAJA, NUM_TRANSACCION_MEDIO, FECHAHORA, COD_MEDIO) VALUES(?,?,?,?,?,?,?)");

            // SET PARAMETROS
            if (dto.getCodEmpresa() != null)            stm.setInt(1, dto.getCodEmpresa());
            if (dto.getIdTransaccion() != null)         stm.setLong(2, dto.getIdTransaccion());
            if (dto.getMontoTotal() != null)            stm.setInt(3, dto.getMontoTotal());
            if (dto.getNumTransaccionCaja() != null)    stm.setLong(4, dto.getNumTransaccionCaja());
            if (dto.getNumTransaccionMedio() != null)   stm.setLong(5, dto.getNumTransaccionMedio());
            if (dto.getFechahora() != null)             stm.setTimestamp(6, new Timestamp(dto.getFechahora().getTime()));
            if (dto.getCodMedio() != null)              stm.setInt(7, dto.getCodMedio());

            if (dto.getCodEmpresa() == null)            stm.setNull(1, OracleTypes.INTEGER);
            if (dto.getIdTransaccion() == null)         stm.setNull(2, OracleTypes.NUMBER);
            if (dto.getMontoTotal() == null)            stm.setNull(3, OracleTypes.INTEGER);
            if (dto.getNumTransaccionCaja() == null)    stm.setNull(4, OracleTypes.NUMBER);
            if (dto.getNumTransaccionMedio() == null)   stm.setNull(5, OracleTypes.NUMBER);
            if (dto.getFechahora() == null)             stm.setNull(6, OracleTypes.DATE);
            if (dto.getCodMedio() == null)              stm.setNull(7, OracleTypes.INTEGER);

            // EXECUTE
            stm.execute();
            stm.close();
            res = true;

        } catch (Exception e) {
            logger.error("insertDetalleTransaccionEmp() - catch (error)", e);
            dto = null;
        } finally {
            try { if (cnx != null) cnx.close();} catch (Exception e) {logger.error("insertDetalleTransaccionEmp() - catch (error)", e);}
        }
        return res;
    }
    
    /**
         * Consulta servicio PEC
         * @param idtransaccion
         * @return
         */
        public BpiPecConsultaTransaccionDto findDetalleServicioPECV2ByIdTransaccion(Long idtransaccion) {
            logger.info("findDetalleServicioPECV2ByIdTransaccion() - inicia");
            BpiPecConsultaTransaccionDto dto = null;
            Connection cnx = null;
            CallableStatement stm = null;
            ResultSet rst = null;
            try {
                cnx = getConnection();
                String sql = "SELECT ID_CONSULTA,\n" + 
                "  ID_TRANSACCION_BOTON,\n" + 
                "  ID_NAVEGACION_BOTON,\n" + 
                "  ID_LINEA_NEGOCIO,\n" + 
                "  FECHA_DOCUMENTO,\n" + 
                "  NUMERO_DOCUMENTO,\n" + 
                "  NUMERO_CONTRATO,\n" + 
                "  MONTO_MINIMO,\n" + 
                "  MONTO_MAXIMO,\n" + 
                "  ID_ESTADO,\n" + 
                "  MONTO_PAGADO,\n" + 
                "  FECHA_HORA_PAGO,\n" + 
                "  DESCRIPCION_PRODUCTO,\n" + 
                "  CODIGO_AUTORIZACION_MEDIO\n" + 
                " FROM BPI_PEC_CONSULTA_TRANSACCION\n" + 
                " WHERE ID_TRANSACCION_BOTON = ?";
                stm = cnx.prepareCall(sql);
                stm.setLong(1, idtransaccion);
                rst = stm.executeQuery();
                while (rst.next()) {
                    dto = new BpiPecConsultaTransaccionDto();
                    dto.setIdConsulta(rst.getLong("ID_CONSULTA"));
                    dto.setIdTransaccionBoton(rst.getLong("ID_TRANSACCION_BOTON"));
                    dto.setIdNavegacionBoton(rst.getLong("ID_NAVEGACION_BOTON"));
                    dto.setIdLineaNegocio(rst.getInt("ID_LINEA_NEGOCIO"));
                    dto.setFechaDocumento(FechaUtil.toUtilDate(rst.getTimestamp("FECHA_DOCUMENTO")));
                    dto.setNumeroDocumento(rst.getString("NUMERO_DOCUMENTO"));
                    dto.setNumeroContrato(rst.getString("NUMERO_CONTRATO"));
                    dto.setMontoMinimo(rst.getDouble("MONTO_MINIMO"));
                    dto.setMontoMaximo(rst.getDouble("MONTO_MAXIMO"));
                    dto.setIdEstado(rst.getInt("ID_ESTADO"));
                    dto.setMontoPagado(rst.getDouble("MONTO_PAGADO"));
                    dto.setFechaHoraPago(FechaUtil.toUtilDate(rst.getTimestamp("FECHA_HORA_PAGO")));
                    dto.setDescripcionProducto(rst.getString("DESCRIPCION_PRODUCTO"));
                    dto.setCodigoAutorizacionMedio(rst.getString("CODIGO_AUTORIZACION_MEDIO"));
                 
                }
                rst.close();
                stm.close();
                
                if (dto != null && dto.getIdLineaNegocio() != null) {
                    //BUSCAR INSTRUMENTO DE CAJA
                     stm = cnx.prepareCall("SELECT INSTRUMENTO_CAJA FROM BPI_PEC_LINEA_NEGOCIO where ID_LINEA_NEGOCIO = ?");
                     stm.setInt(1, dto.getIdLineaNegocio());
                     rst = stm.executeQuery();
                     if (rst.next()) {
                         dto.setInstrumentoCaja(rst.getString("INSTRUMENTO_CAJA")); 
                     }
                }

            } catch (Exception e) {
                logger.error("findDetalleServicioPECV2ByIdTransaccion() - catch (error)", e);
                try {
                    if (rst != null)rst.close();
                    if (stm != null)stm.close();
                    if (cnx != null)cnx.close();
                } catch (SQLException f) {
                    logger.error("findDetalleServicioPECV2ByIdTransaccion() - catch (error)", f);
                }
            } finally {
                try {
                    if (rst != null)rst.close();
                    if (stm != null)stm.close();
                    if (cnx != null)cnx.close();
                } catch (Exception e) {logger.error("findDetalleServicioPECV2ByIdTransaccion() - catch (error)", e);}
            }
            logger.info("findDetalleServicioPECV2ByIdTransaccion() - termina");
            return dto;
        }
}
