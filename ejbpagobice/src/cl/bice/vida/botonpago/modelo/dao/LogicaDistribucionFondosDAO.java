package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.BpiDitribFondosAPVTbl;
import cl.bice.vida.botonpago.common.dto.general.DetalleAPV;

import java.util.List;


public interface LogicaDistribucionFondosDAO {

    /**
     * 
     * @param idTransaccion
     * @param poliza
     * @param ramo
     * @param rut
     * @return
     */
    public List<BpiDitribFondosAPVTbl> getDistribucionFondosAPVByTransaccion(int idTransaccion, int poliza, int ramo, long rut);

    

    /**
     * Realiza la grabacion de una distribucion independiente que
     * ya exista en la base y asocia sus valores de distribucion
     * @param DistribfFondos
     * @return
     */
    public boolean grabarDistribucionFondosAPVProducto(int idAporte, List<BpiDitribFondosAPVTbl> DistribfFondos, Long CodigoRegimen, String DescrpcionRegimen);
    
    /**
     * 
     * @param detalleapv
     * @return
     */
    public boolean updateTransaccionDistribucionFondosAPVProducto(DetalleAPV detalleapv);
    
   
}
