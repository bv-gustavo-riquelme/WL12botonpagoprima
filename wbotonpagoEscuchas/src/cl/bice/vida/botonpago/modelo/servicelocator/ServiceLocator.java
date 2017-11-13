package cl.bice.vida.botonpago.modelo.servicelocator;

import cl.bice.vida.botonpago.util.ResourcePropertiesUtil;

import cl.bice.vida.botonpago.modelo.ejb.MedioPagoElectronicoEJB;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class ServiceLocator {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ServiceLocator.class);

    // Singleton instance
    private static ServiceLocator serviceLocator = new ServiceLocator();
    
    // Cache of objects in JNDI tree
    private static Hashtable homeCache;
    
    // Initial context
    private static InitialContext defaultContext;
    
    
    /**
    * Private constructor, which initializes all the tables and the default
    * InitialContext.
    */
    private ServiceLocator() {    
        try {
          String typeconection = ResourcePropertiesUtil.getProperty("bice.ejb.oracle.ejb.connection.type");        
          homeCache        = new Hashtable();
          if (typeconection.equalsIgnoreCase("local") ) defaultContext   = new InitialContext();
          if (typeconection.equalsIgnoreCase("remote") || typeconection.equalsIgnoreCase("remoto")) defaultContext = getRemoteContext(ResourcePropertiesUtil.getProperty("bice.ejb.oracle.rmi.context.provider.url"));
        } catch(Exception ex) {
          logger.error("Exception in the constructor of ServiceLocator class : " + ex.getMessage(), ex);
        }
    }
    
    /**
    * Method to access the Singleton instance of the ServiceLocator
    *
    * @return <b>ServiceLocator</b> The instance of this class
    */
    public static ServiceLocator getLocator() {
        return serviceLocator;
    }
    /**
    * Method to return an object in the default JNDI context, with  the supplied
    * JNDI name.
    * @param <b>jndiName</b> The JNDI name
    * @return <b>Object</b> The object in the JNDI tree for this name.
    * @throws <b>UtilityException</b> Exception this method can throw
    */
    public static Object getService(String jndiName) throws ServiceLocatorException {
        logger.debug("getService(String jndiName=" + jndiName + ") - iniciando");
        try {        
          if(!homeCache.containsKey(jndiName)) {
            homeCache.put(jndiName, defaultContext.lookup(jndiName));  
          }
        } catch(NamingException ex) {
           logger.error("getService(String) - NamingException :" + ex.getMessage(), ex);    
          throw new ServiceLocatorException("Exception thrown from getService method of ServiceLocator class : " + ex.getMessage());
        } catch(SecurityException ex) {
          logger.error("getService(String) - SecurityException :" + ex.getMessage(), ex);        
          throw new ServiceLocatorException("Exception thrown from from getService method of ServiceLocator class : " + ex.getMessage());
        }
        Object returnObject = homeCache.get(jndiName);
        logger.debug("getService(String jndiName=" + jndiName + ") - terminado");
        return returnObject;
    }


    /**
     * Recupera el EJB 3.0 de ultima version con cambios
     * centralizados y funcionado perfectamente con patrones
     * de disño y DAO como persistencia de datos y no TOPLINK
     * @return the Local EJB Home corresponding to the homeName
     * @throws NamingException 
     */
    public static MedioPagoElectronicoEJB getMedioPagoElectronicoEJB() throws ServiceLocatorException {
        logger.debug("getMedioPagoElectronicoEJB() - iniciando");                    
        MedioPagoElectronicoEJB ejb = (MedioPagoElectronicoEJB) getService(ResourcePropertiesUtil.getProperty("bice.ejb.oracle.ejb.jndi"));
        logger.debug("getMedioPagoElectronicoEJB() - termina");
        return ejb;    
    }      
    
    
    /**
    * Metodo interno de la clase para recuperar
    * el contecto Inicial de conexion a servidores
    * de negocio EJB3.0, esta recuperacion se realiza
    * a travez de un archivo de recursos.
    * @param ormi
    * @return
    * @throws NamingException
    */
    private static InitialContext getRemoteContext(String url) throws NamingException {
        logger.debug("getContext() - iniciando");
        Hashtable<String, String> h = new Hashtable<>();
        h.put(Context.INITIAL_CONTEXT_FACTORY, ResourcePropertiesUtil.getProperty("bice.ejb.oracle.rmi.context.factory.class") );
        h.put(Context.SECURITY_PRINCIPAL, ResourcePropertiesUtil.getProperty("bice.ejb.oracle.rmi.context.user.name") );
        h.put(Context.SECURITY_CREDENTIALS, ResourcePropertiesUtil.getProperty("bice.ejb.oracle.rmi.context.user.pwd") );
        h.put(Context.PROVIDER_URL, url);
        InitialContext returnInitialContext = new InitialContext(h);
        logger.debug("getContext() - termina");
        return returnInitialContext;                    
    } 
}
