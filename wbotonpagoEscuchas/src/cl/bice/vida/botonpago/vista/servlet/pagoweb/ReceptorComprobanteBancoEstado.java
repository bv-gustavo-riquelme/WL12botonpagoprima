package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.modelo.ejb.MedioPagoElectronicoEJB;
import cl.bice.vida.botonpago.modelo.servicelocator.ServiceLocator;
import cl.bice.vida.botonpago.util.RequestUtils;
import cl.bice.vida.botonpago.util.ResourcePropertiesUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XSLException;

import org.apache.log4j.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@WebServlet(name = "ReceptorComprobanteBancoEstado", urlPatterns = { "/faces/ReceptorPago/ReceptorComprobanteBancoEstado" })
public class ReceptorComprobanteBancoEstado extends HttpServlet {
    @SuppressWarnings("compatibility:-1976762136768952582")
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ReceptorComprobanteBancoEstado.class);
    
    private String IDCOMBANCOESTADO;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Metodo para procesar el termino de pago
     * Corresponde a XML con data de termino
     * 
     * <INICIO>
     *  <ENCABEZADO>
     *    <ID_SESSION></ID_SESSION>                 --> ID DE SESSION DE USUARIO
     *    <CANT_MPAGO></CANT_MPAGO>                 --> CANTIDAD DE PAGOS REALIZADOS
     *    <TOTAL></TOTAL>                           --> MONTO TOTAL DE LA TRANSACCION
     *  </ENCABEZADO>
     *  <MULTIPAGO>
     *    <ID_MPAGO></ID_MPAGO>                     --> IDENTIFICADOR DE ORDEN DE COMPRA BICE
     *    <RESULTADO>
     *      <RESULT_MPAGO></RESULT_MPAGO>           --> RESULTADO DEL PAGO
     *      <GLOSA_ERR></GLOSA_ERR>                 --> GLOSA CON EL ERROR DEL PAGO
     *      <TRX_MPAGO></TRX_MPAGO>                 --> ID TRANSACCION ENTREGADO POR EL BANCO ESTADO
     *      <FEC_MPAGO></FEC_MPAGO>                 --> FECHA EN QUE SE REALIZO EL PAGO
     *      <HORA_MPAGO></HORA_MPAGO>               --> HORA EN QUE SE REALIZO EL PAGO
     *      <FEC_CNTBL_MPAGO></FEC_CNTBL_MPAGO>     --> FECHA CONTABLE CON LA CUAL SE CARGO EL PAGO
     *    </RESULTADO>
     *  </MULTIPAGO>
     * </INICIO>
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out;
        response.setContentType("text/html");
        out = response.getWriter();
        String respuesta = "NO-DATA-XML";
        String montoTrx = "0";
        String idSession = "";
        try {
            RequestUtils.printRequest(request, "ReceptorComprobanteBancoEstado"); 
            IDCOMBANCOESTADO = ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.bancoestado.vida.idcom");
            logger.info("ReceptorComprobanteBancoEstadoPerfilado() - doPost() - Id de comercio Banco Estado :" + IDCOMBANCOESTADO);
                    
            //Recupera parametros
            String xmlRespuesta = request.getParameter("xml");
                                        
            if (xmlRespuesta != null) {
                logger.info(xmlRespuesta);
                //Instanciacion de EB
                
                logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - instanciando EJB de negocios");
                MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();

                //Parsea el XML                
                DOMParser parser = new DOMParser();
                parser.parse(new StringReader(xmlRespuesta));
                XMLDocument doc = parser.getDocument();            
                

                
                //Recuperacion de informacion desde XML
                logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - recuperando infornacion desde el XML");
                
                idSession = getNodeValueByXPath(doc, "/INICIO/ENCABEZADO/ID_SESSION"); 
                montoTrx = getNodeValueByXPath(doc, "/INICIO/ENCABEZADO/TOTAL"); 
                String idTrxBice = getNodeValueByXPath(doc, "/INICIO/MULTIPAGO/ID_MPAGO"); 
                String idMontoPago = getNodeValueByXPath(doc, "/INICIO/ENCABEZADO/TOTAL"); 
                String idNumProdcutos = getNodeValueByXPath(doc, "/INICIO/ENCABEZADO/CANT_MPAGO"); 
                String idResultadoPago = getNodeValueByXPath(doc, "/INICIO/MULTIPAGO/RESULTADO/RESULT_MPAGO"); 
                String idErrorDescripcion = getNodeValueByXPath(doc, "/INICIO/MULTIPAGO/RESULTADO/GLOSA_ERR"); 
                String idTrxBancoEstado = getNodeValueByXPath(doc, "/INICIO/MULTIPAGO/RESULTADO/TRX_MPAGO"); 
                String idFechaPagoBancoEstado = getNodeValueByXPath(doc, "/INICIO/MULTIPAGO/RESULTADO/FEC_MPAGO"); 
                String idHoraPagoBancoEstado = getNodeValueByXPath(doc, "/INICIO/MULTIPAGO/RESULTADO/HORA_MPAGO"); 
                
                //Consultado la existencia de la transaccion en el banckend de bice                
                BpiTraTransaccionesTbl orden = ejb.findTransaccionCodigoEstadoByNumTransaccion(new Long(idTrxBice));
                
                //Verifica si existe la orden de compra
                if (orden != null) {
            
                    //Determina si hay pago correcto 
                    int estadoPagado = 0; //0=Pagado 1=No Pagado
                    if (idResultadoPago.equalsIgnoreCase("OK")) {
                        estadoPagado = 0; //Pagado        
                        idErrorDescripcion = "Transacción aprobada.";
                    } else {
                        estadoPagado = 1; //No Pagado
                    }
                    
                    //Validacion de Monto
                    logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - Verificando concordancia del monto de pago v/s monto informado");                
                    logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - Monto Informado : " + orden.getMontoTotal().doubleValue());
                    logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - Monto Banco Estado : " + Double.parseDouble(idMontoPago));
                    if (orden.getMontoTotal().doubleValue() != Double.parseDouble(idMontoPago)) {
                        idErrorDescripcion = "El monto informado por Banco Estado es distinto al enviado a pagar"; 
                    }
                    
                    logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - Seteando valores en DTO para grabacion");
                    SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
                    dto.setIdTrx(Long.parseLong(idTrxBice));
                    dto.setCodRet(estadoPagado); 
                    dto.setNropPagos(Integer.parseInt(idNumProdcutos)); 
                    dto.setMontoPago(Integer.parseInt(idMontoPago));
                    dto.setDescRet(idErrorDescripcion); 
                    dto.setIdCom(new Long(IDCOMBANCOESTADO));
                    dto.setIdTrxBanco(idTrxBancoEstado);
                    dto.setIndPago(estadoPagado==0?"S":"N");
                    dto.setDescRet(estadoPagado==0?"Transaccion Pagada OK":"Transaccion Rechazada por el Banco");
                    dto.setMedioNumeroCuotas(new Integer("0"));
                    dto.setMedioCodigoRespuesta(idResultadoPago);
                    dto.setMedioNumeroTarjeta("");
                    dto.setMedioFechaPago(FechaUtil.toDate(idFechaPagoBancoEstado + " "  + idHoraPagoBancoEstado,"yyyyMMdd HH:mm:ss"));
                    dto.setMedioTipoTarjeta("");
                    dto.setMedioCodigoAutorizacion(idTrxBancoEstado);
                    dto.setMedioOrdenCompra(idTrxBice);
                    
                    /**
                    * Llamada al SP para actualizar datos de la transacción
                    */
                    logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - Llamada al SP para actualizar datos de la transacción");                
                    Boolean resultado = ejb.actualizaTransaccionSPAsync(dto);
                    logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - Resultado llamada al SP = " + resultado.booleanValue());
                    if (resultado.booleanValue()) {
                        logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - Seteando valores de session para comprobante de pago");
                        doComprobanteAccion(idTrxBice, request, response);
                        respuesta = "OK";
                    }      
                } else {
                    /*
                     * Transaccion No existe en nuestro sistema
                     */
                     logger.error("ReceptorComprobanteBancoEstadoPerfilado - Orden de Compra No Existe :" + idTrxBice, new Exception("No Existe Orden de Compra :" + idTrxBice));        
                     respuesta = "NOK";                    
                }            
            }
            
        } catch (Exception e) {
            respuesta = "NOK";
            logger.error("ReceptorComprobanteBancoEstadoPerfilado - Exception() :" + e.getMessage(),e);        
        } finally {
            logger.info("ReceptorComprobanteBancoEstadoPerfilado - finally() : " + respuesta + " cerrando response de transaccion");        
            out.write("<INICIO><IDSESSION>" + idSession + "</IDSESSION><RESP_ONLINE>" + respuesta + "</RESP_ONLINE><TOTAL>" + montoTrx + "<TOTAL></INICIO>");
            out.close();
        }        
        logger.info("ReceptorComprobanteBancoEstadoPerfilado - doPost() - termino");        
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
     * Recupera Elemento Valor
     * @param doc
     * @param xPath
     * @return
     * @throws XSLException
     */
    private String getNodeValueByXPath(XMLDocument doc, String xPath) throws XSLException {
        String find = "";
        NodeList encontrado = doc.selectNodes(xPath); 
        if (encontrado != null && encontrado.getLength() > 0) {
            Element elemento = (Element) encontrado.item(0);
            if (elemento != null) {
                NodeList objNodes = elemento.getChildNodes();
                if (objNodes != null) {
                    if (objNodes.item(0) != null) find = objNodes.item(0).getNodeValue();
                }
            }
        }
        logger.info("getNodeValueByXPath(" + xPath + ") = " + find);
        return find;
    }    
}
