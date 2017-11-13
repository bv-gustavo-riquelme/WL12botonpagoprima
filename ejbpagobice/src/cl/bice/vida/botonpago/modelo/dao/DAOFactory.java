package cl.bice.vida.botonpago.modelo.dao;

import org.apache.log4j.Logger;


public class DAOFactory {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DAOFactory.class);

   
    /**
     * Recupera DAO de consultas Generales.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static PersistenciaGeneralDAO getPersistenciaGeneralDao() {
	return new PersistenciaGeneralDAOImpl();
    }    
   
    /**
     * Recupera DAO consultas cartolas.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static ConsultasDAO getConsultasDao() {
        ConsultasDAO dao = new ConsultasDAOImpl();
        return dao;
    }   
    
    /**
     * Recupera DAO consultas pec.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static ConsultasPecDAO getConsultasPecDao() {
        ConsultasPecDAO dao = new ConsultasPecDAOImpl();
        return dao;
    }      
    
    /**
     * Recupera DAO logica de cartolas.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static LogicaCartolasDAO getLogicaCartolasDao() {
        LogicaCartolasDAO dao = new LogicaCartolasDAOImpl();
        return dao;
    }     
    
    /**
     * Recupera DAO logica de confirmacion.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static LogicaConfirmacionDAO getLogicaConfirmacionDao() {
        LogicaConfirmacionDAO dao = new LogicaConfirmacionDAOImpl();
        return dao;
    }       
    
    /**
     * Recupera DAO logica resumen y pagos.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static LogicaResumenPagosDAO getLogicaResumenPagosDao() {
        LogicaResumenPagosDAO dao = new LogicaResumenPagosDAOImpl();
        return dao;
    }    
    
    /**
     * Recupera DAO Logica Pec.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static LogicaPecDAO getLogicaPecDao() {
        LogicaPecDAO dao = new LogicaPecDAOImpl();
        return dao;
    }  
    
    /**
     * Recupera DAO de Reportes.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static ReportesDAO getReportesDao() {
        ReportesDAO dao = new ReportesDAOImpl();
        return dao;
    }      
    
    /**
     * Recupera DAO de Cuadratura.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static CuadraturaDAO getCuadraturaDao() {
        CuadraturaDAO dao = new CuadraturaDAOImpl();
        return dao;
    }       
    
    /**
     * Recupera DAO de Detalle de Distribucion APV.
     * @return Instancia del DAO
     * @since 1.0
     */
    public static LogicaDistribucionFondosDAO getDistribucionFondosDao() {
        LogicaDistribucionFondosDAO dao = new LogicaDistribucionFondosDAOImpl();
        return dao;
    }      
    
    /**
     * Recupera DAO de Balanceo para JMS CLUSTERIZADA
     * @return
     */
    public static JmsBalancedDAO getJmsBalancedDAO() {
        return new JmsBalancedDAOImpl();
    }
}
