package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;


public class JmsBalancedDAOImpl extends DataSourceBice implements JmsBalancedDAO {
    
    private static final Logger LOGGER = Logger.getLogger(JmsBalancedDAOImpl.class);
    
    
    public boolean existProcesoEnEjecucion(String estado, Long idTrx) {
        boolean existe = false;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "Select Count(fecha) TOTAL From BPI_JMS_BALANCED Where ESTADO=? AND ID_TRX = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, estado);
            stm.setLong(2, idTrx);
            rs = stm.executeQuery();
            if(rs.next() ) {
                if(rs.getLong("TOTAL")> 0) {
                    existe = true;
                }    
            }
        } catch(SQLException e) {
            LOGGER.error(e);
        } finally {
            closeConnection(rs, stm, conn);    
        }
        
        return existe;
    }
    
    public void guardaEjecucion(String serverName, String estado, String descripcion, Long idtrx) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
           
            String sql = "Insert Into BPI_JMS_BALANCED (FECHA, SERVER, ESTADO, DESCRIPCION, ID_TRX ) " +
                "Values (SYSDATE,?,?,?,?)";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, serverName);
            stm.setString(2, estado);
            stm.setString(3, descripcion);
            stm.setLong(4, idtrx);
            LOGGER.info("Guardando ejecucion [" + serverName + "][" + estado + "] IdTRX["+idtrx+"] -> " + stm.executeUpdate());
            
        } catch(SQLException e) {
            LOGGER.error(e);
        } finally {
            closeConnection(rs, stm, conn);    
        }
        
    }
    
    public void actualizaEjecucion(String serverName, String estadoInicial, String estadoFinal, String descripcion) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "Update BPI_JMS_BALANCED Set ESTADO = ?, DESCRIPCION = ? Where SERVER=? And ESTADO=?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, estadoFinal);
            stm.setString(2, descripcion);
            stm.setString(3, serverName);
            stm.setString(4, estadoInicial);
            
            LOGGER.info("Actualizando ejecucion [" + serverName + "]Estado Inicial[" + estadoFinal + "]Estado final[" + estadoFinal + "] -> " + stm.executeUpdate());
            
        } catch(SQLException e) {
            LOGGER.error(e);
        } finally {
            closeConnection(rs, stm, conn);    
        }
    }
    
    
}
