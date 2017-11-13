package cl.bice.vida.botonpago.modelo.jdbc;

import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class DataSourceBice  {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(DataSourceBice.class);

    public String connectiontype=null;
    public String username=null;
    public String password=null;
    public String url=null;
    public String driver=null;
    public String jndidatasource=null;
    public String jndidatasourcePersonas=null;
    public String jndidatasourcePersistencia=null;
    public static boolean loggerinfo = false;

    /**
     * Inicializa valores de configuracion
     */
    public DataSourceBice() {
        logger.debug("CONSTRUCTOR() - iniciando");
        this.setConnectiontype(ResourceBundleUtil.getProperty("jdbc.connection.type"));
        this.setUsername(ResourceBundleUtil.getProperty("jdbc.connection.user"));
        this.setPassword(ResourceBundleUtil.getProperty("jdbc.connection.password"));
        this.setUrl(ResourceBundleUtil.getProperty("jdbc.connection.url.jdbc"));
        this.setDriver(ResourceBundleUtil.getProperty("jdbc.connection.url.driver"));
        this.setJndidatasource(ResourceBundleUtil.getProperty("jdbc.connection.datasource.jndi"));
        this.setJndidatasourcePersonas(ResourceBundleUtil.getProperty("jdbc.connection.datasource.jndi.personas"));
        this.setJndidatasourcePersistencia(ResourceBundleUtil.getProperty("jdbc.connection.datasource.jndi.persistencia"));
        logger.debug("CONSTRUCTOR() - terminado");
    }

    /**
     * <p>Recupera una conexion de datos</p>
     * 
     * @see javax.sql.DataSource#getConnection()
     */
    public Connection getConnection() throws SQLException {
        logger.debug("getConnection() - iniciando");

	//Conexion via Datasource
	if (this.connectiontype.trim().equalsIgnoreCase("datasource")) {
	    InitialContext ic;
	    try {
                logger.debug("Datasource Name [" + this.jndidatasource + "]");
		ic = new InitialContext();
		DataSource ds = (DataSource) ic.lookup(this.jndidatasource);
                Connection returnConnection = ds.getConnection();      
                PreparedStatement stm = returnConnection.prepareStatement("ALTER SESSION SET NLS_DATE_FORMAT = 'DD-MM-YYYY'");
	        stm.execute();
	        stm.close();
                logger.debug("getConnection() - termina");
		return returnConnection;
	    } catch (NamingException e) {
                logger.error("getConnection() - catch (error)", e);
		throw new SQLException("No se logro inicializar conexion JNDI '" + this.jndidatasource + "' de conexion a fuentes de datos!");
	    }	    
	    	    
	} else if (this.connectiontype.trim().equalsIgnoreCase("url")) { //Conexion via URL y Driver 
	    try {
		Class.forName(this.driver).newInstance();
                Connection returnConnection = DriverManager.getConnection(this.url, this.username, this.password);
                logger.debug("getConnection() - termina");
		return returnConnection;
	    } catch (Exception e) {
                logger.error("getConnection() - catch (error)", e);    
		throw new SQLException("No se logoro instanciar driver '" + this.driver + "' de conexion a fuentes de datos!");
	    }		    
	}
	throw new SQLException("No existe implementacion para el tipo de conexion '" + this.connectiontype + "', revise configuracion!");
    }

    public Connection getConnectionPersonas() throws SQLException {
        logger.debug("getConnection() - iniciando");

        //Conexion via Datasource
        if (this.connectiontype.trim().equalsIgnoreCase("datasource")) {
            InitialContext ic;
            try {
                logger.debug("Datasource Name [" + this.jndidatasourcePersonas + "]");
                ic = new InitialContext();
                DataSource ds = (DataSource) ic.lookup(this.jndidatasourcePersonas);
                Connection returnConnection = ds.getConnection();      
                PreparedStatement stm = returnConnection.prepareStatement("ALTER SESSION SET NLS_DATE_FORMAT = 'DD-MM-YYYY'");
                stm.execute();
                stm.close();
                logger.debug("getConnection() - termina");
                return returnConnection;
            } catch (NamingException e) {
                logger.error("getConnection() - catch (error)", e);
                throw new SQLException("No se logro inicializar conexion JNDI '" + this.jndidatasourcePersonas + "' de conexion a fuentes de datos!");
            }       
                    
        } else if (this.connectiontype.trim().equalsIgnoreCase("url")) { //Conexion via URL y Driver 
            try {
                Class.forName(this.driver).newInstance();
                Connection returnConnection = DriverManager.getConnection(this.url, this.username, this.password);
                logger.debug("getConnection() - termina");
                return returnConnection;
            } catch (Exception e) {
                logger.error("getConnection() - catch (error)", e);    
                throw new SQLException("No se logoro instanciar driver '" + this.driver + "' de conexion a fuentes de datos!");
            }               
        }
        throw new SQLException("No existe implementacion para el tipo de conexion '" + this.connectiontype + "', revise configuracion!");
    }

    public Connection getConnectionPersistencia() throws SQLException {
        logger.debug("getConnection() - iniciando");

        //Conexion via Datasource
        if (this.connectiontype.trim().equalsIgnoreCase("datasource")) {
            InitialContext ic;
            try {
                logger.debug("Datasource Name [" + this.jndidatasourcePersistencia + "]");
                ic = new InitialContext();
                DataSource ds = (DataSource) ic.lookup(this.jndidatasourcePersistencia);
                Connection returnConnection = ds.getConnection();      
                PreparedStatement stm = returnConnection.prepareStatement("ALTER SESSION SET NLS_DATE_FORMAT = 'DD-MM-YYYY'");
                stm.execute();
                stm.close();
                logger.debug("getConnection() - termina");
                return returnConnection;
            } catch (NamingException e) {
                logger.error("getConnection() - catch (error)", e);
                throw new SQLException("No se logro inicializar conexion JNDI '" + this.jndidatasourcePersistencia + "' de conexion a fuentes de datos!");
            }       
                    
        } else if (this.connectiontype.trim().equalsIgnoreCase("url")) { //Conexion via URL y Driver 
            try {
                Class.forName(this.driver).newInstance();
                Connection returnConnection = DriverManager.getConnection(this.url, this.username, this.password);
                logger.debug("getConnection() - termina");
                return returnConnection;
            } catch (Exception e) {
                logger.error("getConnection() - catch (error)", e);    
                throw new SQLException("No se logoro instanciar driver '" + this.driver + "' de conexion a fuentes de datos!");
            }               
        }
        throw new SQLException("No existe implementacion para el tipo de conexion '" + this.connectiontype + "', revise configuracion!");
    }

    /**
     * <p>Recupera la conexion de datos utilizando un username y password</p>
     * 
     */
    public Connection getConnection(String username, String password) throws SQLException {
	this.setUsername(username);
	this.setUsername(password);
        Connection returnConnection = this.getConnection();
	return returnConnection;
    }
    public Connection getConnectionPersonas(String username, String password) throws SQLException {
        this.setUsername(username);
        this.setUsername(password);
        Connection returnConnection = this.getConnectionPersonas();
        return returnConnection;
    }
    public Connection getConnectionPersistencia(String username, String password) throws SQLException {
        this.setUsername(username);
        this.setUsername(password);
        Connection returnConnection = this.getConnectionPersistencia();
        return returnConnection;
    }
    
    
    
       

    public String getConnectiontype() {
	return connectiontype;
    }

    public void setConnectiontype(String connectiontype) {
	this.connectiontype = connectiontype;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getDriver() {
	return driver;
    }

    public void setDriver(String driver) {
	this.driver = driver;
    }

    public String getJndidatasource() {
	return jndidatasource;
    }

    public void setJndidatasource(String jndidatasource) {
	this.jndidatasource = jndidatasource;
    }

    public void setJndidatasourcePersonas(String jndidatasourcePersonas) {
        this.jndidatasourcePersonas = jndidatasourcePersonas;
    }

    public String getJndidatasourcePersonas() {
        return jndidatasourcePersonas;
    }

    public void setJndidatasourcePersistencia(String jndidatasourcePersistencia) {
        this.jndidatasourcePersistencia = jndidatasourcePersistencia;
    }

    public String getJndidatasourcePersistencia() {
        return jndidatasourcePersistencia;
    }

    public static void setLoggerinfo(boolean loggerinfo) {
        DataSourceBice.loggerinfo = loggerinfo;
    }

    public static boolean isLoggerinfo() {
        return loggerinfo;
    }

    public void closeConnection(ResultSet rst, CallableStatement stm, Connection cnx) {
        try {
            if (rst != null)rst.close();
            if (stm != null)stm.close();
            if (cnx != null)cnx.close();
        } catch (SQLException f) {
            logger.error("getSecuenceValue() - catch (error)", f);
        }
    }
    
    public void closeConnection(ResultSet rst, PreparedStatement stm, Connection cnx) {
        try {
            if (rst != null)rst.close();
            if (stm != null)stm.close();
            if (cnx != null)cnx.close();
        } catch (SQLException f) {
            logger.error("getSecuenceValue() - catch (error)", f);
        }
    }

}
