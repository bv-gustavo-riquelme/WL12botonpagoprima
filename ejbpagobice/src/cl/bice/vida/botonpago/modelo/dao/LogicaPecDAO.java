package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.AperturaCaja;
import cl.bice.vida.botonpago.common.dto.general.ServicioPec;
import cl.bice.vida.botonpago.common.dto.general.Transacciones;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;

import noNamespace.ConfirmacionDocument;
import noNamespace.InformePagoPendienteDocument;
import noNamespace.InformePagoServipagDocument;
import noNamespace.Producto;
import noNamespace.RespuestaPagosPendientesDocument;

import org.apache.xmlbeans.XmlObject;


public interface LogicaPecDAO {
    /**
     * Crea xml que se usara para responder una consulta por pagos pendientes
     * provenientes de cualquier medio de pago
     * @param xml String con el xml con la consulta realizada al servicio web
     * @return String con el xml con el/los productos que se pueden pagar o con la descripcion del error
     */
         
    public String crearXMLRespuestaPagosPendientes(String medio, int cod_medio, int empresa, int rut, String nombre_persona, ServicioPec servicio);
    public String crearXMLRespuestaServipag(int cod_medio,String medio, int empresa, int rut, String nombre_persona, String xmlsource);  
    public String validateXML(XmlObject xml);
    public Transacciones generaXmlRespuestaPagosPendientes(RespuestaPagosPendientesDocument respuesta, int rut, int empresa, int cod_medio, String medio, String nombre_persona,ServicioPec servicio,ConfirmacionDocument conf);
    public Transacciones setDividendos(RespuestaPagosPendientesDocument respuesta, int rut, int cod_medio,String medio, String nombre_persona, ServicioPec servicio, AperturaCaja turno,ConfirmacionDocument conf);
    public String setDividendosServipag(int rut, int cod_medio,String medio,String nombre_persona, AperturaCaja turno, String xmlorigen);
    public Transacciones setPolizas(RespuestaPagosPendientesDocument respuesta, int rut, int cod_medio, String medio, String nombre_persona, ServicioPec servicio, AperturaCaja turno,ConfirmacionDocument conf);
    public String setPolizasServipag(int rut, int cod_medio, String medio, String nombre_persona, AperturaCaja turno, String xmlorigen);    
    public ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada agregarEntradaConfirmacion(ConfirmacionDocument.Confirmacion.ProductosPorPagar productos, Integer contador, BigInteger codigoProducto, String descripcionProducto,BigInteger numProducto,int codEmpresa, BigInteger enPesos, BigDecimal enUF);
    public void setearProducto(Producto producto,String cod_producto, String descripcion_prod,String num_producto,int cod_empresa);    
    public Transacciones crearTransaccion(String empresa, int rut, int cod_medio, String nombre_persona, ServicioPec servicio, Date fecha_turno, int monto);
    public String crearError(RespuestaPagosPendientesDocument respuesta, Integer cod_error, String error);
    public String crearError(int medio, int empresa, Integer cod_error, String error);
    public String crearErrorProcesarInformePagoPendientes(int medio, Integer cod_error, String error);
    public double setValorUF();
    public String procesarInformePagoPendientes(String xml);     
    public String procesarInformePago(String xml, InformePagoPendienteDocument informe, int cod_medio);
    public String procesarInformePagoPendientesServipag(String xml, InformePagoServipagDocument informe, int cod_medio);
    public String procesarInformePagoServipagOK(InformePagoServipagDocument informe, long idtransaccion, long numtransaccionmedio, int cod_mediotemp);
    public String cerrarTransaccion(String accion, long numtransaccionmedio,long idtransaccion,int nropagos,int cod_medio);
    public String crearRespuestaPagosPendientes(String xml);
}
