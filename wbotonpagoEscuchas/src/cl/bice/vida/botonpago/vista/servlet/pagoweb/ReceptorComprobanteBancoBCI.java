package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.modelo.ejb.MedioPagoElectronicoEJB;
import cl.bice.vida.botonpago.modelo.servicelocator.ServiceLocator;
import cl.bice.vida.botonpago.util.RequestUtils;
import cl.bice.vida.botonpago.util.ResourcePropertiesUtil;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(name = "ReceptorComprobanteBancoBCI", urlPatterns = { "/faces/ReceptorPago/ReceptorComprobanteBancoBCI" })
public class ReceptorComprobanteBancoBCI extends HttpServlet {
    @SuppressWarnings("compatibility:7133587116040281236")
    private static final long serialVersionUID = 1L;

    private String IDCOMBANCOBCI;

    private static final Logger logger = Logger.getLogger(ReceptorComprobanteBancoBCI.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("ReceptorComprobanteBancoBCI[GET]");
        processRequest(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("ReceptorComprobanteBancoBCI[POST]");
        processRequest(request, response);
    }

    /**
     * Metodo para procesar el termino de pago
     * Corresponde a parametros HTTP
     *
     * PARAMETRO 1 : trx
     * PARAMETRO 2: cod
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out;
        response.setContentType("text/html");
        out = response.getWriter();
        String respuesta = "SI-RESPUESTA";
        String montoTrx = "0";
        String idTrxBice = null;
        String prmCod = null;
        try {
            RequestUtils.printRequest(request, "ReceptorComprobanteBancoBCI"); 
            IDCOMBANCOBCI = ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.bancobci.vida.idcom");
            logger.info("ReceptorComprobanteBancoBCI() - doPost() - Id de comercio Banco BCI :" + IDCOMBANCOBCI);

            //Recupera parametros
            idTrxBice = request.getParameter("trx");
            prmCod = request.getParameter("cod");

            if (idTrxBice != null && prmCod != null) {

                //QUITA LOS 2 ULTIMOS CARACTEREC POR LA CANTIDAD DE PRODCUTOS
                idTrxBice = idTrxBice.substring(0, idTrxBice.length() - 2);

                logger.info("ReceptorComprobanteBancoBCI - doPost() - Valores de parametros recibidos por BCI");
                logger.info("ReceptorComprobanteBancoBCI - doPost() - Parametro TRX = " + idTrxBice);
                logger.info("ReceptorComprobanteBancoBCI - doPost() - Parametro COD = " + prmCod);

                //Instanciacion de EB
                logger.info("ReceptorComprobanteBancoBCI - doPost() - instanciando EJB de negocios");
                MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();

                //Consultado la existencia de la transaccion en el banckend de bice
                BpiTraTransaccionesTbl orden = ejb.findTransaccionCodigoEstadoByNumTransaccion(new Long(idTrxBice));

                //Verifica si existe la orden de compra
                if (orden != null) {
                    //Recuperacion de informacion desde XML
                    logger.info("ReceptorComprobanteBancoBCI - doPost() - recuperando infornacion desde el XML");


                    String idMontoPago = "" + orden.getMontoTotal().intValue();
                    String idNumProdcutos = request.getParameter("trx");
                    idNumProdcutos = idNumProdcutos.substring(idNumProdcutos.length() - 2);
                    logger.info("ReceptorComprobanteBancoBCI - doPost() - Numero de Productos a cancelar : " +
                                idNumProdcutos);
                    String idResultadoPago = prmCod;
                    String idErrorDescripcion = "";
                    String idTrxBancoBCI = idTrxBice;

                    //Determina si hay pago correcto
                    int estadoPagado = 0; //0=Pagado 1=No Pagado
                    if (idResultadoPago.equalsIgnoreCase("021")) {
                        estadoPagado = 0; //Pagado
                        idErrorDescripcion = "Transacción aprobada.";
                    } else {
                        estadoPagado = 1; //No Pagado
                        idErrorDescripcion = "Cargo no aplicado.";
                    }

                    //Validacion de Monto
                    logger.info("ReceptorComprobanteBancoBCI - doPost() - Verificando concordancia del monto de pago v/s monto informado");
                    logger.info("ReceptorComprobanteBancoBCI - doPost() - Monto Informado : " +
                                orden.getMontoTotal().doubleValue());
                    logger.info("ReceptorComprobanteBancoBCI - doPost() - Monto Banco Estado : " +
                                Double.parseDouble(idMontoPago));
                    if (orden.getMontoTotal().doubleValue() != Double.parseDouble(idMontoPago)) {
                        idErrorDescripcion = "El monto informado por Banco Estado es distinto al enviado a pagar";
                    }

                    logger.info("ReceptorComprobanteBancoBCI - doPost() - Seteando valores en DTO para grabacion");
                    SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
                    dto.setIdTrx(Long.parseLong(idTrxBice));
                    dto.setCodRet(estadoPagado);
                    dto.setNropPagos(Integer.parseInt(idNumProdcutos));
                    dto.setMontoPago(Integer.parseInt(idMontoPago));
                    dto.setDescRet(idErrorDescripcion);
                    dto.setIdCom(new Long(IDCOMBANCOBCI));
                    dto.setIdTrxBanco(idTrxBancoBCI);
                    dto.setIndPago(estadoPagado == 0 ? "S" : "N");
                    dto.setMedioNumeroCuotas(new Integer("0"));
                    dto.setMedioCodigoRespuesta(idResultadoPago);
                    dto.setMedioNumeroTarjeta("");
                    dto.setMedioFechaPago(new Date(System.currentTimeMillis()));
                    dto.setMedioTipoTarjeta("");
                    dto.setMedioCodigoAutorizacion(idTrxBancoBCI);
                    dto.setMedioOrdenCompra(idTrxBice);

                    /**
                    * Llamada al SP para actualizar datos de la transacción
                    */
                    logger.info("ReceptorComprobanteBancoBCI - doPost() - Llamada al SP para actualizar datos de la transacción");
                    Boolean resultado = ejb.actualizaTransaccionSPAsync(dto);
                    logger.info("ReceptorComprobanteBancoBCI - doPost() - Resultado llamada al SP = " + resultado.booleanValue());
                    if (resultado.booleanValue()) {
                        logger.info("ReceptorComprobanteBancoBCI - doPost() - Seteando valores de session para comprobante de pago");
                        doComprobanteAccion(idTrxBice, request);
                        respuesta = "OK";
                    }
                } else {
                    /*
                     * Transaccion No existe en nuestro sistema
                     */
                    logger.error("ReceptorComprobanteBancoBCI - Orden de Compra No Existe :" + idTrxBice,
                                 new Exception("No Existe Orden de Compra :" + idTrxBice));
                    respuesta = "NOK";
                }
            } else {
                logger.info("No se obtuvo idTrxBice ["+request.getParameter("trx")+"]prmCod["+request.getParameter("cod")+"]");
            }

        } catch (Exception e) {
            respuesta = "NOK";
            logger.error("ReceptorComprobanteBancoBCI - Exception() :" + e.getMessage(), e);
        } finally {
            logger.info("ReceptorComprobanteBancoBCI - finally() : " + respuesta + " cerrando response de transaccion");
            out.write("<script>window.close();</script>");
            out.close();
        }
        logger.info("ReceptorComprobanteBancoBCI - doPost() - termino");
    }

    /**
     * Accion para ver el comprobante de pago
     * @throws IOException
     * @throws ServletException
     */
    public void doComprobanteAccion(String idTrx, HttpServletRequest request) throws ServletException, IOException {
        /**
         * Seteamos atributos
        */
        request.getSession().setAttribute("idtipoconsult", "0");
        request.getSession().setAttribute("idcomprobante", idTrx);
        request.getSession().setAttribute("urlvolver", "volverResumenyPagos");
        request.getSession().setAttribute("email", "S");
        request.getSession().setAttribute("contactcenter", "N");

    }
}
