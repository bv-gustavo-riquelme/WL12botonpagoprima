package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.BpiDlcDetalleLogCuadrTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiLgclogCuadraturaTbl;

import java.util.Date;


public interface CuadraturaDAO {
    public Long insertNewCuadratura(Integer idEmpresa, Integer idMedioPago, Date fechaCuadratura);
    public Boolean updateCuadratura(Long idCuadratura, Long montoTotal, Long montoInformado, Long numeroTransacciones);
    public Boolean insertCuadraturaTransaccion(Long idCuadratura, Long idTransaccion);
    public Long insertLogCuadratura(BpiLgclogCuadraturaTbl dto);
    public Boolean insertLogDetalleCuadratura(BpiDlcDetalleLogCuadrTbl dto);
}
