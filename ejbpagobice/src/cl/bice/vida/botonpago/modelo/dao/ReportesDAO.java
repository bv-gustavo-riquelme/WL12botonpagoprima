package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.ReporteAgentePolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteGerentePolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteJefeSucursalPolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteJefeZonalPolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteSupervisorPolizas;

import java.util.List;


public interface ReportesDAO {
    public List<ReporteGerentePolizas> generateReporteGerentePolizas();
    public List<ReporteJefeSucursalPolizas> generateReporteJefeSucursalPolizas(Integer num_jefe_sucursal);
    public List<ReporteSupervisorPolizas> generateReporteSupervisorPolizas(Integer numero_supervisor);
    public List<ReporteAgentePolizas> generateReporteAgentePolizas(Integer numero_agente, Integer rowdesde, Integer rowhasta);
    public List<ReporteJefeZonalPolizas> generateReporteJefeZonalPolizas(Integer num_jefe_zona);
    public List<ReporteAgentePolizas> generateReporteAgenteByRutPolizas(Integer rut_agente, Integer rowdesde, Integer rowhasta);
    public List<ReporteSupervisorPolizas> generateReporteSupervisorByRutPolizas(Integer rut_supervisor);
    public List<ReporteJefeSucursalPolizas> generateReporteJefeSucursalByRutPolizas(Integer rut_jefe_sucursal);
    public List<ReporteJefeZonalPolizas> generateReporteJefeZonalByRutPolizas(Integer rut_jefe_zona);
    public ReporteAgentePolizas getCountFindReporteAgentePolizas(Integer numero_agente);
    public ReporteAgentePolizas getCountFindReporteAgenteByRutPolizas(Integer rut_agente);
}
