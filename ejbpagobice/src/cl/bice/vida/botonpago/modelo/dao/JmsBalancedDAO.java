package cl.bice.vida.botonpago.modelo.dao;


public interface JmsBalancedDAO {
    
    boolean existProcesoEnEjecucion(String estado, Long idTrx); 
    
    void guardaEjecucion(String serverName, String estado, String descripcion, Long idTrx);
    
    void actualizaEjecucion(String serverName, String estadoInicial, String estadoFinal, String descripcion);
    
}
