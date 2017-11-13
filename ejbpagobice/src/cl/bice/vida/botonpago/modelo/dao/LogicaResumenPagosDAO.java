package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.AperturaCaja;
import cl.bice.vida.botonpago.common.exception.BiceException;

import java.math.BigDecimal;

import java.text.ParseException;

import java.util.Date;
import java.util.List;

import noNamespace.ConfirmacionDocument;
import noNamespace.ResumenConHeaderDocument;

import noNamespace.ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar;

import org.apache.xmlbeans.XmlObject;


public interface LogicaResumenPagosDAO {
    public String crearResumenPagos(int rut, String nombre, String confirmacion);
    public String crearResumenPagosPolizas(int rut, String nombre, String confirmacion, int idCanal);
    public String crearResumenPagosDividendos(int rut, String nombre, String confirmacion, int idCanal);
    public void validateXML(XmlObject xml) throws BiceException;
    public ResumenConHeaderDocument generaXmlResumenPagos(int rut, String nombre,ConfirmacionDocument conf);
    public ResumenConHeaderDocument generaXmlResumenPagosPolizas(int rut, String nombre,ConfirmacionDocument conf) throws BiceException, ParseException;
    public ResumenConHeaderDocument generaXmlResumenPagosDividendos(int rut, String nombre,ConfirmacionDocument conf) throws BiceException, ParseException;
    public void setIdentificacionMensaje(ResumenConHeaderDocument.ResumenConHeader newResumenConHeader);
    public void setInfoNavegacion(ResumenConHeaderDocument.ResumenConHeader resumen, int rut, int empresa);
    public BigDecimal setValorUF(Date fecha) throws BiceException;
    public void setProductosPorPagar(ProductosPorPagar pp, int rut, BigDecimal valor_uf, AperturaCaja turno,ConfirmacionDocument.Confirmacion.ProductosPorPagar seleccion);
    public void setProductosPorPagarPolizas(ProductosPorPagar pp, int rut, BigDecimal valor_uf, AperturaCaja turno,ConfirmacionDocument.Confirmacion.ProductosPorPagar seleccion);
    public void setProductosPorPagarDividendos(ProductosPorPagar pp, int rut, BigDecimal valor_uf, AperturaCaja turno,ConfirmacionDocument.Confirmacion.ProductosPorPagar seleccion);
    public int setDividendos(ProductosPorPagar pp, BigDecimal valor_uf,List dividendos,List seleccion);
    public int SetPolizas(ProductosPorPagar pp, BigDecimal valor_uf, List polizas,List seleccion);
    public void setEstadoCuota(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota cuota);
    public void setEstadoCuotaPolizas(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota cuota);
    public void setEstadoCuotaDividendos(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota cuota);
    public void setProductosPagados(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, int rut);
    public void setProductosPagadosPolizas(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, int rut);
    public void setProductosPagadosDividendos(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, int rut);
    public int SetPolizasMes(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, List polizas);
    public int setDividendosMes(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, List dividendos);
    public void setConvenios(ResumenConHeaderDocument.ResumenConHeader newResumenConHeader,int seleccionado) throws BiceException;
    public String crearResumenPagosAPVAPT(int rut, String nombre, String confirmacion, int idCanal);
    
    //PRIMERAS PRIMAS
    public String crearResumenPagosPrimerasPrimas(int rut, String nombre, String confirmacion, int idCanal, Integer numeroPropuesta);
    public void setProductosPagadosPrimeraPrima(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, int rut, int numeroPropuesta);
    public void setProductosPorPagarPrimeraPrima(ProductosPorPagar pp, int rut, BigDecimal valor_uf, AperturaCaja turno,ConfirmacionDocument.Confirmacion.ProductosPorPagar seleccion, int numeroPropuesta);
    public int SetPrimeraPrima(ProductosPorPagar pp, BigDecimal valor_uf, List polizas,List seleccion);
}
