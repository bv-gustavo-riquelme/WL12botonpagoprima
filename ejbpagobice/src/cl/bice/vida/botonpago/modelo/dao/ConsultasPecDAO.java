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

import cl.bice.vida.botonpago.modelo.dto.BpiPecConsultaTransaccionDto;

import java.util.Date;
import java.util.List;


public interface ConsultasPecDAO {
    public AperturaCaja findCajaAbierta();
    public BhDividendosVw findOldestDividendoByRut(String rut);
    public BhDividendosOfflineVw findOldestDividendosOfflineByRut(String rut);
    public List<BhDividendosVw> findOlderDividendoByRut(String rut);
    public List<BhDividendosOfflineVw> findOlderDividendosOfflineByRut(String rut);
    public IndPrimaNormalVw findOldestPolizasByRut(Integer rut);
    public IndPrimaNormalOfflineVw findOldestPolizasOfflineByRut(Integer rut);
    public List<IndPrimaNormalVw> findOlderPolizasByRut(Integer rut);
    public List<IndPrimaNormalOfflineVw> findOlderPolizasOfflineByRut(Integer rut);
    public ValorUf findValorUfByDate(Date fecha);
    public ServicioPec findServicioPecByIdtransaccion(Long id);
    public Transacciones findTransaccionById(Long id);
    public ConsultaDetalleServicioPec findXMLConfirmacionPECByIdTransaccion(Long idtransaccion);
    public ConsultaDetalleServicioPec findDetalleServicioPECByIdTransaccionPagina(Long idtransaccion, Long cod_pagina);
    public Transacciones updateTransaccion(Long idtransaccion,Integer cod_estado,Long numtransaccionmedio);
    public void updateDetalleTransaccion(Transacciones transaccion,List<DetalleTransaccionByEmp> detallesByEmp, List<Comprobantes> comprobantes);
    public ServicioPec newServicioPec(long rut, int medio);
    public void updateTransaccionInServicioPec(ServicioPec servicio,Long transaccion, Integer monto);
    public DetalleServicioPec createDetalleServPec(long det, int entrada, int cod_pagina, int monto,String xml);
    public DetalleServicioPec newDetalleServPec(Long det,int entrada,int cod_pagina, Integer monto);
    public void updateDetNavegacionXML(DetalleServicioPec detalle, String xml);  
    public void updateDetNavegacionXML(DetalleServicioPec detalle, String xml, int monto);   
    public Boolean actualizaTransaccionSP(SPActualizarTransaccionDto dto);
    public void SQLUpdateXML(DetalleServicioPec detalle, String xml);  
    public void SQLUpdateXMLSuper(DetalleServicioPec detalle, String xml);  
    public ServicioPec findServicioPecById(Long id);
    public BpiPecConsultaTransaccionDto findDetalleServicioPECV2ByIdTransaccion(Long idtransaccion);
}
