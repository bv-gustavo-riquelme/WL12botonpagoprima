package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.util.CheckMacWebPayUtil;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.modelo.ejb.MedioPagoElectronicoEJB;
import cl.bice.vida.botonpago.modelo.servicelocator.ServiceLocator;
import cl.bice.vida.botonpago.util.RequestUtils;
import cl.bice.vida.botonpago.util.ResourcePropertiesUtil;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Calendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(name = "ReceptorComprobanteWebPay", urlPatterns = { "/faces/ReceptorPago/ReceptorComprobanteWebPay" })
public class ReceptorComprobanteWebPay extends HttpServlet {
    @SuppressWarnings("compatibility:-6210828694585383181")
    private static final long serialVersionUID = 1L;


    private static final Logger logger = Logger.getLogger(ReceptorComprobanteWebPay.class);
    private String IDCOMWEBPAY;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    /**
     * Metodo para procesar el termino de pago
     * con WebPay, aqui se analizan las variables
     * para determinar el exito o fracaso de la operacion.
     * Las variables disponibles para lectura son :
     * 
     *      TBK_ORDEN_COMPRA
     *      TBK_CODIGO_COMERCIO
     *      TBK_CODIGO_COMERCIO_ENC
     *      TBK_TIPO_TRANSACCION
     *      TBK_RESPUESTA
     *      TBK_MONTO //ESTE CAMPO CONTIENE 2 CEROS AL FINAL PARA LOS DECIMALES
     *      TBK_CODIGO_AUTORIZACION
     *      TBK_FINAL_NUMERO_TARJETA
     *      TBK_FECHA_CONTABLE //mmdd
     *      TBK_FECHA_TRANSACCION //mmdd
     *      TBK_HORA_TRANSACCION //hhmmss
     *      TBK_ID_SESION
     *      TBK_ID_TRANSACCION 
     *      TBK_TIPO_PAGO
     *      TBK_NUMERO_CUOTAS
     *      TBK_MONTO
     *      TBK_MAC
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("ReceptorComprobanteWebPay - Inicio");
        PrintWriter out;
        response.setContentType("text/html");
        out = response.getWriter();
        String respuesta = "SIN TRANSACCION WEBPAY";
        try {
            RequestUtils.printRequest(request, "ReceptorComprobanteWebPay");         
            //Recupera parametros
            String idOrdenCompra = request.getParameter("TBK_ORDEN_COMPRA");
            String idRespuesta   = request.getParameter("TBK_RESPUESTA");
            String idMontoPago   = request.getParameter("TBK_MONTO");
            String idTipoPago    = request.getParameter("TBK_TIPO_PAGO");
            String idTrxWebPay   = request.getParameter("TBK_ID_TRANSACCION");
            String idNumCuotas   = request.getParameter("TBK_NUMERO_CUOTAS");        
            String idFechaContbl = request.getParameter("TBK_FECHA_CONTABLE");  //mmdd
            String idFinalNumTrj = request.getParameter("TBK_FINAL_NUMERO_TARJETA");
            String idHoraPago    = request.getParameter("TBK_HORA_TRANSACCION"); //hhmmsss
            String idCodigoAut   = request.getParameter("TBK_CODIGO_AUTORIZACION"); //codigo autorizacion
            
            // obteniendo numero de transaccion bice
            String idTrxBice      = idOrdenCompra.substring(0,8); //Numero de transaccion
            logger.info("ReceptorComprobanteWebPay() - doPost() - Numero Transaccion: " + idTrxBice);
            // Obteniendo datos del pago
            MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();
            BpiTraTransaccionesTbl orden = ejb.findTransaccionCodigoEstadoByNumTransaccion(new Long(idTrxBice));
            logger.info("ReceptorComprobanteWebPay() - doPost() - Tipo transaccion: " + orden.getTipoTransaccion().intValue());            
            int tipoTransaccion = orden.getTipoTransaccion().intValue();
            
            logger.info("ReceptorComprobanteWebPay() - doPost() - Validando MAC de Transaccion");            
            boolean macValida = false;
            
            
            logger.info("ReceptorComprobanteWebPay() - doPost() - Empresa ingresada: " + orden.getIdEmpresa());            
            
            //==========================================
            // BICE VIDA
            //==========================================
            if(orden.getIdEmpresa().longValue() == 1){
                if(tipoTransaccion == 2){
                    IDCOMWEBPAY = ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.vida.apt.idcom");
                    logger.info("ReceptorComprobanteWebPay() - doPost() - Id de comercio WebPay vida extraordinaria:" + IDCOMWEBPAY);
                    Boolean validMac = new Boolean(ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.vida.apt.mac.enable"));
                    if (validMac.booleanValue())  macValida = CheckMacWebPayUtil.checkMac(request.getParameterMap(), true, ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.vida.apt.mac.directory.out"), ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.vida.apt.mac.file.validate"));
                    if (!validMac.booleanValue()) macValida = true;    
                }else{
                    IDCOMWEBPAY = ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.vida.idcom");
                    logger.info("ReceptorComprobanteWebPay() - doPost() - Id de comercio WebPay vida normal:" + IDCOMWEBPAY);
                    Boolean validMac = new Boolean(ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.vida.mac.enable"));
                    if (validMac.booleanValue())  macValida = CheckMacWebPayUtil.checkMac(request.getParameterMap(), true, ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.vida.mac.directory.out"), ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.vida.mac.file.validate"));
                    if (!validMac.booleanValue()) macValida = true;    
                }
                
            }
            
            //==========================================
            // BICE HIPOTECARIA
            //==========================================
            if(orden.getIdEmpresa().longValue() == 2){
                IDCOMWEBPAY = ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.hipotecaria.idcom");
                logger.info("ReceptorComprobanteWebPay() - doPost() - Id de comercio WebPay hipotecaria:" + IDCOMWEBPAY);
                Boolean validMac = new Boolean(ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.hipotecaria.mac.enable"));
                if (validMac.booleanValue())  macValida = CheckMacWebPayUtil.checkMac(request.getParameterMap(), true, ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.hipotecaria.mac.directory.out"), ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.webpay.hipotecaria.mac.file.validate"));
                if (!validMac.booleanValue()) macValida = true;    
            }
                        
            //Verifica que haya una transaccion real
            boolean pasa = true;            
            if (idOrdenCompra == null || idOrdenCompra.length() == 0) pasa = false;
            if (idRespuesta == null || idRespuesta.length() == 0) pasa = false;
            if (idTrxWebPay == null || idTrxWebPay.length() == 0) pasa = false;
            
            if (pasa) {
                //Instanciacion de EB
                logger.info("ReceptorComprobanteWebPay - doPost() - instanciando EJB de negocios");
                
                //Quitar Decimales 2 ceros al final
                logger.info("ReceptorComprobanteWebPay - doPost() - quitando decimales del monto");
                idMontoPago = idMontoPago.substring(0, idMontoPago.length()-2);
            
                logger.info("ReceptorComprobanteWebPay - doPost() - Extrayendo valores para grabar en SP");
                
                String idNumProdcutos = idOrdenCompra.substring(8);   //8 Datos de numeros de productos pago                
                
                logger.info("ReceptorComprobanteWebPay - doPost() - Verificando Existencia de Orden de Compra numero :" + idOrdenCompra);                
                //BpiTraTransaccionesTbl orden = ejb.findTransaccionCodigoEstadoByNumTransaccion(new Long(idTrxBice));
                
                //Verifica si existe la orden de compra
                if (orden != null) {
            
                    //Determina si hay pago correcto                    
                    int estadoPagado = 0; //0=Pagado 1=No Pagado
                    //MAC Valida
                    if (macValida) {                    
                        respuesta = "ACEPTADO";                
                        if (idRespuesta.equalsIgnoreCase("0")) {
                            estadoPagado = 0; //Pagado        
                        } else {
                            estadoPagado = 1; //No Pagado
                        }
                    } else {
                        //MAC No Valida
                        estadoPagado = 1; //1=No Pagado
                        idRespuesta = "-99";
                        respuesta = "RECHAZO";  //INTENTO DE SUPLANTACION MAC NO ES VALIDA
                    }
                    
                    // Verifica si existe pago previo con el mismo nro de trx.
                    logger.info("ReceptorComprobanteWebPay - doPost() - Estado de la trx: " + orden.getCodEstado());                    
                    if (orden.getCodEstado() == 3){
                        estadoPagado = 1; //1=No Pagado
                        idRespuesta = "-1";
                        respuesta = "RECHAZO";  //TRX Pagado
                        
                        logger.info("ReceptorComprobanteWebPay - doPost() : " + respuesta + " " + getDescripcionResultado(idRespuesta) + " cerrando response de transaccion");        
                        out.write(respuesta);
                        out.close();
                    }
                    
                    //Validacion de Monto                    
                    if(tipoTransaccion == 2){
                        logger.info("ReceptorComprobanteWebPay - doPost() - Verificando concordancia del monto de pago v/s monto informado");                
                        logger.info("ReceptorComprobanteWebPay - doPost() - Monto Informado : " + orden.getMontoTotal().doubleValue());
                        logger.info("ReceptorComprobanteWebPay - doPost() - Monto Cargo : " + orden.getCargo().doubleValue());
                        logger.info("ReceptorComprobanteWebPay - doPost() - Monto TransBank : " + Double.parseDouble(idMontoPago));
                        if ((orden.getMontoTotal().doubleValue() + orden.getCargo().doubleValue()) != Double.parseDouble(idMontoPago)) {
                            estadoPagado = 1; //1=No Pagado
                            idRespuesta = "-88";
                            respuesta = "RECHAZO";  //MONTO NO CORRECTO A CANCELAR
                            
                            logger.info("ReceptorComprobanteWebPay - doPost() : " + respuesta + " " + getDescripcionResultado(idRespuesta) + " cerrando response de transaccion");        
                            out.write(respuesta);
                            out.close();
                        }
                    }else{
                        logger.info("ReceptorComprobanteWebPay - doPost() - Verificando concordancia del monto de pago v/s monto informado");                
                        logger.info("ReceptorComprobanteWebPay - doPost() - Monto Informado : " + orden.getMontoTotal().doubleValue());
                        logger.info("ReceptorComprobanteWebPay - doPost() - Monto TransBank : " + Double.parseDouble(idMontoPago));
                        if (orden.getMontoTotal().doubleValue() != Double.parseDouble(idMontoPago)) {
                            estadoPagado = 1; //1=No Pagado
                            idRespuesta = "-88";
                            respuesta = "RECHAZO";  //MONTO NO CORRECTO A CANCELAR
                            
                            logger.info("ReceptorComprobanteWebPay - doPost() : " + respuesta + " " + getDescripcionResultado(idRespuesta) + " cerrando response de transaccion");        
                            out.write(respuesta);
                            out.close();
                        }
                    }
                                        
                    logger.info("ReceptorComprobanteWebPay - doPost() - Seteando valores en DTO para grabacion");
                    SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
                    dto.setIdTrx(Long.parseLong(idTrxBice));
                    dto.setCodRet(estadoPagado); 
                    dto.setNropPagos(Integer.parseInt(idNumProdcutos)); 
                    dto.setMontoPago(Integer.parseInt(idMontoPago));
                    dto.setDescRet(getDescripcionResultado(idRespuesta)); 
                    dto.setIdCom(new Long(IDCOMWEBPAY));
                    dto.setIdTrxBanco(idTrxWebPay);
                    dto.setIndPago(estadoPagado==0?"S":"N");
                    dto.setMedioNumeroCuotas(new Integer(idNumCuotas));
                    dto.setMedioCodigoRespuesta(idRespuesta);
                    dto.setMedioNumeroTarjeta(idFinalNumTrj);
                    dto.setMedioFechaPago(FechaUtil.toDate(idFechaContbl + FechaUtil.getAnoFecha(Calendar.getInstance()) + " " + idHoraPago,"MMddyyyy hhmmss"));
                    dto.setMedioTipoTarjeta(idTipoPago);
                    dto.setMedioCodigoAutorizacion(idCodigoAut);
                    dto.setMedioOrdenCompra(idOrdenCompra);
                     
                    if (idRespuesta.equalsIgnoreCase("0")) {
                        /**
                         * Llamada al SPAsync para actualizar datos de la transacción
                         */
                        logger.info("ReceptorComprobanteWebPay - doPost() - Llamada al SP para actualizar datos de la transacción");                
                        Boolean resultado = ejb.actualizaTransaccionSPAsync(dto);
                        logger.info("ReceptorComprobanteWebPay - doPost() - Resultado llamada al SP = " + resultado.booleanValue());
                        
                        if (idRespuesta.equalsIgnoreCase("0")) {
                            if (resultado.booleanValue()) {
                                logger.info("ReceptorComprobanteWebPay - doPost() - Seteando valores de session para comprobante de pago");
                                doComprobanteAccion(idTrxBice, request, response);
                            }
                        }                                        
                    }else{
                        /**
                         * Llamada al SP para actualizar datos de la transacción
                         */
                         logger.info("ReceptorComprobanteWebPay - doPost() - Llamada al SP para actualizar datos de la transacción");                
                         Boolean resultado = ejb.actualizaTransaccionSP(dto);
                         logger.info("ReceptorComprobanteWebPay - doPost() - Resultado llamada al SP = " + resultado.booleanValue());                          
                    }
                    
                } else {
                    /*
                     * Transaccion No existe en nuestro sistema
                     */
                     logger.error("ReceptorComprobanteWebPay - Orden de Compra No Existe :" + idTrxBice, new Exception("No Existe Orden de Compra :" + idTrxBice));        
                     respuesta = "RECHAZO";                    
                }
            }            
            
        } catch (Exception e) {
            respuesta = "RECHAZO";
            logger.error("ReceptorComprobanteWebPay - Exception() :" + e.getMessage(),e);        
        } finally {
            logger.info("ReceptorComprobanteWebPay - finally() : " + respuesta + " cerrando response de transaccion");        
            out.write(respuesta);
            out.close();
        }        
        logger.info("ReceptorComprobanteWebPay - Inicio");
    }
    
    
    /**
     * Accion para ver el comprobante de pago
     * @throws IOException 
     * @throws ServletException 
     */
    public void doComprobanteAccion(String idTrx, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            /**
             * Seteamos atributos
             */             
            request.getSession().setAttribute("idtipoconsult", "0");
            request.getSession().setAttribute("idcomprobante", idTrx);
            request.getSession().setAttribute("urlvolver", "volverResumenyPagos");
            request.getSession().setAttribute("email", "S");
            request.getSession().setAttribute("contactcenter", "N");

    }    
    
    /**
     * Recupera la descripcion del resultado de la operacion con WebPay
     * @param resultado
     * @return
     */
    private String getDescripcionResultado(String resultado) {
        if (resultado.equalsIgnoreCase("0")) return "Transacción aprobada.";
        if (resultado.equalsIgnoreCase("-1")) return "Rechazo de tx. en B24, No autorizada";
        if (resultado.equalsIgnoreCase("-2")) return "Transacción debe reintentarse.";
        if (resultado.equalsIgnoreCase("-3")) return "Error en tx.";
        if (resultado.equalsIgnoreCase("-4")) return "Rechazo de tx. En B24, No autorizada";
        if (resultado.equalsIgnoreCase("-5")) return "Rechazo por error de tasa.";
        if (resultado.equalsIgnoreCase("-6")) return "Excede cupo máximo mensual.";
        if (resultado.equalsIgnoreCase("-7")) return "Excede límite diario por transacción.";        
        if (resultado.equalsIgnoreCase("-99")) return "La Validación MAC de Transaccion no es correcta.";
        if (resultado.equalsIgnoreCase("-88")) return "El monto informado por TransBank es distinto al enviado a pagar.";
        return "";
    }
}
