package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.BpiDitribFondosAPVTbl;
import cl.bice.vida.botonpago.common.dto.general.DetalleAPV;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;


public class LogicaDistribucionFondosDAOImpl extends DataSourceBice implements LogicaDistribucionFondosDAO{

    /**
     * Logger for this class de esta
     */
    private static final Logger logger = Logger.getLogger(LogicaDistribucionFondosDAOImpl.class);



    /**
     * 
     * @param idTransaccion
     * @param poliza
     * @param ramo
     * @param rut
     * @return
     */
    public List<BpiDitribFondosAPVTbl> getDistribucionFondosAPVByTransaccion(int idTransaccion, int poliza, int ramo, long rut) {
        logger.info("getDistribucionFondosAPV() - inicia");
        List<BpiDitribFondosAPVTbl> distribucion = new ArrayList<BpiDitribFondosAPVTbl>();
        Connection cnx = null;
        CallableStatement stm = null;
        ResultSet rst = null;

        String sql = "" +
        "SELECT  COM.ID_TRANSACCION as ID_TRANSACCION, DIS.POLIZA as POLIZA, DIS.RAMO as RAMO, \n" + 
        "        DIS.FOLIORECIBO as FOLIORECIBO, DIS.MONTO as MONTO, DIS.IDFONDO as IDFONDO, \n" + 
        "        DIS.COLOR as COLOR, DIS.PORCENTAJE as PORCENTAJE, DIS.ORDENPRESENTACION as ORDENPRESENTACION, \n" + 
        "        DIS.NOMBREFONDO as NOMBREFONDO, DIS.IDAPORTE as IDAPORTE, \n" + 
        "        DIS.SALDOINICIAL as SALDOINICIAL, DIS.RUT as RUT \n" + 
        "FROM    BPI_COM_COMPROBANTES_TBL COM, BPI_DFA_DISTRIBFONDOSAPV_TBL DIS \n" + 
        "WHERE   DIS.IDAPORTE = COM.IDAPORTE \n" + 
        "AND     DIS.IDTRANSACCION = COM.ID_TRANSACCION \n" + 
        "AND     DIS.POLIZA = COM.NUM_PRODUCTO \n" + 
        "AND     DIS.RAMO = COM.CUOTA \n" + 
        "AND     COM.ID_TRANSACCION = ? \n" + 
        "AND     COM.NUM_PRODUCTO = ? \n" + 
        "AND     DIS.RUT = ? ";

        try {
            cnx = getConnection();
            stm = cnx.prepareCall(sql);
            stm.setInt(1, idTransaccion);            
            stm.setInt(2, poliza);
            stm.setLong(3, rut);
            rst = stm.executeQuery();
            while (rst.next()) {
                BpiDitribFondosAPVTbl dto = new BpiDitribFondosAPVTbl();
                dto.setIdTransaccion(rst.getInt("ID_TRANSACCION"));                
                dto.setPoliza(rst.getInt("POLIZA"));
                dto.setRamo(rst.getInt("RAMO"));
                dto.setFolio(rst.getInt("FOLIORECIBO"));
                dto.setMonto(rst.getDouble("MONTO"));
                dto.setIdFondo(rst.getInt("IDFONDO"));
                dto.setColor(rst.getInt("COLOR"));
                dto.setPorcentaje(rst.getInt("PORCENTAJE"));
                dto.setOrdenPresentacion(rst.getInt("ORDENPRESENTACION"));
                dto.setNombreFondo(rst.getString("NOMBREFONDO"));
                dto.setIdAporte(rst.getInt("IDAPORTE"));
                dto.setSaldoInicial(rst.getDouble("SALDOINICIAL"));
                dto.setRutCliente(rst.getInt("RUT"));
                distribucion.add(dto);
            }

        } catch (Exception e) {
            logger.error("getDistribucionFondosAPV() - catch (error)", e);
             try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                logger.error("getDistribucionFondosAPV() - catch (error)", f);
            }
        } finally {
            try {
                if (rst != null)rst.close();
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {
                logger.error("getDistribucionFondosAPV() - catch (error)", e);
            }
        }
        logger.info("getDistribucionFondosAPV() - termina");


        return distribucion;
    }

   

    /**
     * Itera la lista de fondosd y los graba para su distribucion
     * como logica de proceso de almacenamiento.
     * @param DistribfFondos
     * @return
     */
    //APT AQUI HAY QUE APLICARSE PARA LA GRABACION DE LA DISTRIBUCION DEL APORTE EN DB
    public boolean grabarDistribucionFondosAPVProducto(int idAporte, List<BpiDitribFondosAPVTbl> DistribfFondos, Long CodigoRegimen, String DescrpcionRegimen) {
        logger.info("grabarDistribucionFondosAPVProducto() - inicia");
        boolean ret = false;
        Connection cnx = null;
        PreparedStatement stm = null;
        PreparedStatement verifica = null;

        try {

            //RECUPERA CONEXION
            cnx = getConnection();
            String sql = "";
            Iterator itr = DistribfFondos.iterator();
            while(itr.hasNext()){
                BpiDitribFondosAPVTbl dto = (BpiDitribFondosAPVTbl)itr.next();
            
                dto.setIdAporte(new Integer(idAporte));
                verifica = cnx.prepareStatement("SELECT * FROM BPI_DFA_DISTRIBFONDOSAPV_TBL WHERE  RUT = ?  AND POLIZA = ? AND RAMO = ? AND IDFONDO = ? AND IDAPORTE = ? AND IDTRANSACCION = 0");
                verifica.setInt(1, dto.getRutCliente());
                verifica.setInt(2, dto.getPoliza());
                verifica.setInt(3, dto.getRamo());
                verifica.setInt(4, dto.getIdFondo());
                verifica.setInt(5, idAporte);
                ResultSet res =  verifica.executeQuery();

                if (!res.next()) {
                    sql = "INSERT INTO BPI_DFA_DISTRIBFONDOSAPV_TBL ( POLIZA, RAMO, FOLIORECIBO, MONTO, IDFONDO, COLOR, PORCENTAJE, ORDENPRESENTACION, NOMBREFONDO, SALDOINICIAL, " +
                    "IDAPORTE, RUT, CODIGOREGIMEN, DESCRIPCIONREGIMEN, IDTRANSACCION, VALORCOLORHEXADECIMAL, VALORCOLORY, VALORCOLORZ, VALORCOLORX)  " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    stm = cnx.prepareCall(sql);
                    // SET PARAMETROS
                    if (dto.getPoliza() != null)            stm.setInt(1, dto.getPoliza());
                    if (dto.getRamo() != null)              stm.setInt(2, dto.getRamo());
                    if (dto.getFolio() != null)             stm.setInt(3, dto.getFolio());
                    if (dto.getMonto() != null)             stm.setDouble(4, dto.getMonto());
                    if (dto.getIdFondo() != null)           stm.setInt(5, dto.getIdFondo());
                    if (dto.getColor() != null)             stm.setInt(6, dto.getColor());
                    if (dto.getPorcentaje() != null)        stm.setInt(7, dto.getPorcentaje());
                    if (dto.getOrdenPresentacion() != null) stm.setInt(8, dto.getOrdenPresentacion());
                    if (dto.getNombreFondo() != null)       stm.setString(9, dto.getNombreFondo());
                    if (dto.getSaldoInicial() != null)      stm.setDouble(10, dto.getSaldoInicial());
                    if (dto.getIdAporte() != null)          stm.setInt(11, dto.getIdAporte());
                    if (dto.getRutCliente() != null)        stm.setInt(12, dto.getRutCliente());                    
                    if (CodigoRegimen != null)              stm.setLong(13, CodigoRegimen);
                    if (DescrpcionRegimen != null)          stm.setString(14, DescrpcionRegimen);
                    //TODO: NUEVOS VALORES DE COLOR.
                    if (dto.getHexadecimal() != null)       stm.setString(16, dto.getHexadecimal());
                    if (dto.getValorColorX() != null)       stm.setLong(17, dto.getValorColorX());
                    if (dto.getValorColorY() != null)       stm.setLong(18, dto.getValorColorY());
                    if (dto.getValorColorZ() != null)       stm.setLong(19, dto.getValorColorZ());
                    

                    if (dto.getPoliza() == null)            stm.setNull(1, OracleTypes.INTEGER);
                    if (dto.getRamo() == null)              stm.setNull(2, OracleTypes.INTEGER);
                    if (dto.getFolio() == null)             stm.setNull(3, OracleTypes.INTEGER);
                    if (dto.getMonto() == null)             stm.setNull(4, OracleTypes.DOUBLE);
                    if (dto.getIdFondo() == null)           stm.setNull(5, OracleTypes.INTEGER);
                    if (dto.getColor() == null)             stm.setNull(6, OracleTypes.INTEGER);
                    if (dto.getPorcentaje() == null)        stm.setNull(7, OracleTypes.INTEGER);
                    if (dto.getOrdenPresentacion() == null) stm.setNull(8, OracleTypes.INTEGER);
                    if (dto.getNombreFondo() == null)       stm.setNull(9, OracleTypes.VARCHAR);
                    if (dto.getSaldoInicial() == null)      stm.setNull(10, OracleTypes.DOUBLE);
                    if (dto.getIdAporte() == null)          stm.setNull(11, OracleTypes.INTEGER);
                    if (dto.getRutCliente() == null)        stm.setNull(12, OracleTypes.INTEGER);
                    if (CodigoRegimen == null)              stm.setNull(13, OracleTypes.INTEGER);
                    if (DescrpcionRegimen == null)          stm.setNull(14, OracleTypes.VARCHAR);
                    
                    stm.setInt(15, 0); // El id de transaccion se inicializa en cero para la distribución temporal co0n esa poliza
                    
                    //TODO: NUEVOS VALORES DE COLOR.
                    if (dto.getHexadecimal() == null)       stm.setNull(16, OracleTypes.VARCHAR);
                    if (dto.getValorColorX() == null)       stm.setNull(17, OracleTypes.NUMBER);
                    if (dto.getValorColorY() == null)       stm.setNull(18, OracleTypes.NUMBER);
                    if (dto.getValorColorZ() == null)       stm.setNull(19, OracleTypes.NUMBER);                    
                    
                } else {
                    sql = "UPDATE BPI_DFA_DISTRIBFONDOSAPV_TBL SET MONTO = ?, PORCENTAJE = ?, IDAPORTE = ? , CODIGOREGIMEN = ?, DESCRIPCIONREGIMEN = ? " +
                    " WHERE POLIZA = ? " +
                    " AND RAMO = ? " +
                    " AND IDFONDO = ? " +
                    " AND RUT = ? " +
                    " AND IDAPORTE = ? " +
                    " AND IDTRANSACCION = 0";
                    stm = cnx.prepareCall(sql);
                    stm.setDouble(1, dto.getMonto());
                    stm.setInt(2, dto.getPorcentaje());
                    stm.setInt(3, dto.getIdAporte());
                    stm.setLong(4, CodigoRegimen);
                    stm.setLong(5, idAporte);
                    stm.setString(5, DescrpcionRegimen);

                    //PK
                    stm.setInt(6, dto.getPoliza());
                    stm.setInt(7, dto.getRamo());
                    stm.setInt(8, dto.getIdFondo());
                    stm.setInt(9, dto.getRutCliente());
                }
                res.close();
                verifica.close();

                //EJECUTA INSERT O UPDATE
                stm.execute();
            }

            ret = true;
        } catch (Exception e) {
            ret = false;
            logger.error("grabarDistribucionFondosAPVProducto() - catch (error)", e);
             try {
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                ret = false;
                logger.error("grabarDistribucionFondosAPVProducto() - catch (error)", f);
            }
        } finally {
            try {
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {
                ret = false;
                logger.error("grabarDistribucionFondosAPVProducto() - catch (error)", e);
            }
        }
        logger.info("grabarDistribucionFondosAPVProducto() - termina");

        return ret;
    }
    
    /**
     * APT
     * @param detalleapv
     * @return
     */
    public boolean updateTransaccionDistribucionFondosAPVProducto(DetalleAPV detalleapv) {
        logger.info("updateTransaccionDistribucionFondosAPVProducto() - inicia");
        boolean ret = false;
        Connection cnx = null;
        PreparedStatement stm = null;

        try {
            String sql = "UPDATE BPI_DFA_DISTRIBFONDOSAPV_TBL SET IDTRANSACCION = ? " +
            "WHERE POLIZA = ? " +
            "AND RAMO = ? " +
            "AND IDAPORTE = ? " +
            "AND RUT = ? " +
            "AND IDTRANSACCION = 0";
            //RECUPERA CONEXION
            cnx = getConnection();
            stm = cnx.prepareCall(sql);
            stm.setLong(1, detalleapv.getIdtransaccion());
            stm.setLong(2, detalleapv.getNumeropoliza());
            stm.setLong(3, detalleapv.getNumeroramo());
            stm.setLong(4, detalleapv.getIdaporte());
            stm.setLong(5, detalleapv.getRut());
            
            stm.execute();
            
            ret = true;
        } catch (Exception e) {
            ret = false;
            logger.error("updateTransaccionDistribucionFondosAPVProducto() - catch (error)", e);
             try {
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (SQLException f) {
                ret = false;
                logger.error("updateTransaccionDistribucionFondosAPVProducto() - catch (error)", f);
            }
        } finally {
            try {
                if (stm != null)stm.close();
                if (cnx != null)cnx.close();
            } catch (Exception e) {
                ret = false;
                logger.error("updateTransaccionDistribucionFondosAPVProducto() - catch (error)", e);
            }
        }
        logger.info("updateTransaccionDistribucionFondosAPVProducto() - termina");

        return ret;
    }
    
}
