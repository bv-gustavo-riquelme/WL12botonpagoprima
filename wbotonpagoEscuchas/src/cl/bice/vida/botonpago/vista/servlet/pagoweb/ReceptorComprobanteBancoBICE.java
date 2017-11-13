package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.api.RespuestaBotonPago;
import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.modelo.dto.RespuestaBancoBICE;
import cl.bice.vida.botonpago.modelo.ejb.MedioPagoElectronicoEJB;
import cl.bice.vida.botonpago.modelo.servicelocator.ServiceLocator;
import cl.bice.vida.botonpago.util.RequestUtils;

import cl.botonPago.api.BotonPagoException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XSLException;

import org.apache.log4j.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


@WebServlet(name = "ReceptorComprobanteBancoBICE", urlPatterns = { "/faces/ReceptorPago/ReceptorComprobanteBancoBICE" })
public class ReceptorComprobanteBancoBICE extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(ReceptorComprobanteBancoBICE.class);
    
    private boolean escribirsalida ;
    private String path;

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
     * <boton-pago-bice>
     *  <respuesta>
     *   <id-empresa-cliente></id-empresa-cliente>
     *   <id-transaccion>234234</id-transaccion>
     *   <id-bice>12123</id-bice>
     *   <estado>APROBADO</estado>
     *  </respuesta>
     * </boton-pago-bice>
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, true);
        logger.info("ReceptorComprobanteBancoBICE - doPost() - termino");  
    }
    
    public void processRequest(HttpServletRequest request, HttpServletResponse response, boolean escribirsalida) throws IOException {
        String respuesta = "ERRORDATA";
        try {
            RequestUtils.printRequest(request, "ReceptorComprobanteBancoBICE"); 
            this.escribirsalida = escribirsalida;
            logger.info("ReceptorComprobanteBancoBICE - doPost() - instanciando EJB de negocios");
            MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();
            
            //Recupera parametros
            String xmlRespuesta = request.getParameter("_btn_bice_resp");
            String xmlRespuestaFirma = request.getParameter("_btn_bice_firma");
            
            //VALIDA LA DATA ENVIADA POR EL BANCO
            if (xmlRespuesta != null && xmlRespuestaFirma != null) {
                RespuestaBancoBICE respBICE = validaXmlRespuestaBancoBICE(xmlRespuesta, xmlRespuestaFirma);
               
                                            
                if (respBICE != null && respBICE.getResultado() != null) {
                
                    
                    logger.info(xmlRespuesta);
                    //Instanciacion de EB
                    
                    //Recuperacion de informacion desde XML
                    logger.info("ReceptorComprobanteBancoBICE - doPost() - recuperando infornacion desde el XML");
                    String idTrxBice = Long.toString(respBICE.getIdTransaccionCliente());
                    
                    //Consultado la existencia de la transaccion en el banckend de bice
                    BpiTraTransaccionesTbl orden = ejb.findTransaccionCodigoEstadoByNumTransaccion(new Long(idTrxBice));
                    
                
                    // PAGOS APROBADOS Y RECHAZADOS
                    if (orden != null && !respBICE.getResultado().equalsIgnoreCase("PENDIENTE")) {
                        
                         //==============================
                         // E S T A D O   2, 4, 10 y 99
                         //==============================
                         // 1 = PEC inicio de pago
                         // 2  = No pagado en proceso de pago en linea
                         // 4  = Transaccion rechazada por el banco
                         // 10 = Transaccion no grabada en Banckend
                         // 99 = Transaccion rechazada por el banco
                         //Dar de baja el pago procesandolo directamente con PLSQL de pago, este asu ves debera cambiar a estado 7
                         if (orden.getCodEstado() == 1 || orden.getCodEstado() == 2 || orden.getCodEstado() == 4 || orden.getCodEstado() == 10 || orden.getCodEstado() == 99 ) {
                        
                            String idNumProdcutos = "1"; 
                            String idErrorDescripcion = ""; 
                            String idTrxBancoBICE = respBICE.getIdTrxBice(); 
                     
                            //Determina si hay pago correcto 
                            int estadoPagado = 0; //0=Pagado 1=No Pagado
                            if (respBICE.getResultado().equalsIgnoreCase("APROBADO")) {
                                estadoPagado = 0; //Pagado        
                                idErrorDescripcion = "Transaccion aprobada.";
                            } else {
                                //RECHAZADO
                                estadoPagado = 1; //No Pagado
                            }
                            
                            //Validacion de Monto
                            logger.info("ReceptorComprobanteBancoBICE - doPost() - Verificando concordancia del monto de pago v/s monto informado");                
                            logger.info("ReceptorComprobanteBancoBICE - doPost() - Monto Informado : " + orden.getMontoTotal().doubleValue());
                            logger.info("ReceptorComprobanteBancoBICE - doPost() - Monto Banco BICE : " + orden.getMontoTotal());
                            logger.info("ReceptorComprobanteBancoBICE - doPost() - Seteando valores en DTO para grabacion");
                            SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
                            dto.setIdTrx(Long.parseLong(idTrxBice));
                            dto.setCodRet(estadoPagado); 
                            dto.setNropPagos(Integer.parseInt(idNumProdcutos)); 
                            dto.setMontoPago(orden.getMontoTotal().intValue());
                            dto.setDescRet(idErrorDescripcion); 
                            dto.setIdCom(new Long(respBICE.getIdEmpresaCliente()));
                            dto.setIdTrxBanco(idTrxBancoBICE);
                            dto.setIndPago(estadoPagado==0?"S":"N");
                            dto.setMedioNumeroCuotas(new Integer("0"));
                            dto.setMedioCodigoRespuesta(Long.toString(respBICE.getEstado()));
                            dto.setMedioNumeroTarjeta("");                    
                            dto.setMedioFechaPago(new java.util.Date());
                            dto.setMedioTipoTarjeta("");
                            dto.setMedioCodigoAutorizacion(idTrxBancoBICE);
                            dto.setMedioOrdenCompra(idTrxBice);
                            
                            /**
                            * Llamada al SP para actualizar datos de la transacción
                            */
                            logger.info("ReceptorComprobanteBancoBICE - doPost() - Llamada al SP para actualizar datos de la transacción");                
                            Boolean resultado = ejb.actualizaTransaccionSPAsync(dto);
                            logger.info("ReceptorComprobanteBancoBICE - doPost() - Resultado llamada al SP = " + resultado.booleanValue());
                            if (resultado.booleanValue()) {
                                logger.info("ReceptorComprobanteBancoBICE - doPost() - Seteando valores de session para comprobante de pago");
                                doComprobanteAccion(idTrxBice, request, response);
                                if (estadoPagado == 0 ) respuesta = "OK";
                                if (estadoPagado == 1 ) respuesta = "RECHAZADO";
                            }      
                        } else {
                            /*
                             * Transaccion No existe en nuestro sistema
                             */
                             logger.error("ReceptorComprobanteBancoBICE - Orden de Compra No Existe :" + idTrxBice, new Exception("No Existe Orden de Compra :" + idTrxBice));        
                             respuesta = "NOEXISTE";                    
                        }   
                
                    } else {
                         respuesta= "PENDIENTE";
                        // I M P O R T A N T E
                        // PAGO PENDIENTE SE DEBERA ESPERAR DURANTE LAS 24 HORAS QUE BANCO BICE CONFIRME LA OPERACION YA SEA
                        // POR ESTA VIA O POR ARCHIVO DE RENDICION
                    }
                } else {
                  //DATA NO VALIDA
                   respuesta = "ERRORDATA";
                }
            } else {
              //DATA NO VALIDA
               respuesta = "ERRORDATA";
            }
            
        } catch (Exception e) {
            respuesta = "NOK";
            logger.error("ReceptorComprobanteBancoBICE - Exception() :" + e.getMessage(),e);        
        } finally {
            if (escribirsalida) {
                PrintWriter out;
                response.setContentType("text/html");
                out = response.getWriter();      
                logger.info("ReceptorComprobanteBancoBICE - finally() : " + respuesta + " cerrando response de transaccion"); 
                if (respuesta.equalsIgnoreCase("OK"))         out.write("<html><body><h2>TRANSACCION PAGADA OK</h2></body></html>");
                if (respuesta.equalsIgnoreCase("PENDIENTE"))  out.write("<html><body><h2>TRANSACCION PENDIENTE DE APROBACION (24 HORAS)</h2></body></html>");
                if (respuesta.equalsIgnoreCase("RECHAZADO"))  out.write("<html><body><h2>PAGO RECHAZADO POR BANCO BICE</h2></body></html>");
                if (respuesta.equalsIgnoreCase("ERRORDATA"))  out.write("<html><body><h2>ERROR ENVIO DE RESPUESTA</h2><h4><ul><li>FALTA DE PARAMETROS VIA GET O POST</li><li>ERROR EN CONTENIDO DE LOS PARAMETROS</li><li>FIRMA NO VALIDA O INCORRECTA</li></ul></h4></body></html>");
                out.close();
            }
        }        
        logger.info("ReceptorComprobanteBancoBICE - doPost() - termino");  
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


    /**
     * Respuesta banco BICE con validacion de Firma
     * @param xml
     * @param firma
     * @return
     */
    private RespuestaBancoBICE validaXmlRespuestaBancoBICE(String xml, String firma) {
        RespuestaBancoBICE resp = new RespuestaBancoBICE();
        RespuestaBotonPago respuesta = null;
        try {
            String publicKeyPath = "";
            if(escribirsalida) {
                publicKeyPath = getServletContext().getRealPath("/") + File.separator;
                publicKeyPath = publicKeyPath.replace("classes/", "");
                publicKeyPath += "WEB-INF/config/bice-publico.der";
            } else {
                publicKeyPath = this.getPath();
            }
            
            logger.info("publicKeyPath -> " + publicKeyPath);
            
            
            respuesta = new RespuestaBotonPago(xml, firma, publicKeyPath);//BotonPagoFactory.crearRespuesta(xml, firma);
            if (respuesta != null) {
                resp.setEstado(respuesta.getEstado());
                resp.setIdEmpresaCliente(respuesta.getIdEmpresaCliente());
                resp.setIdTransaccionCliente(respuesta.getIdTrxCliente());
                resp.setIdTrxBice(Long.toString(respuesta.getIdTrxBice()));
                if (resp.getEstado() == RespuestaBotonPago.APROBADO) resp.setResultado("APROBADO");
                if (resp.getEstado() == RespuestaBotonPago.PENDIENTE) resp.setResultado("PENDIENTE");
                if (resp.getEstado() == RespuestaBotonPago.RECHAZADO) resp.setResultado("RECHAZADO");
            }
        } catch (BotonPagoException bte) { // Error de Firma o XML - Significa informaci—n
            bte.printStackTrace();
        }
        return resp;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
