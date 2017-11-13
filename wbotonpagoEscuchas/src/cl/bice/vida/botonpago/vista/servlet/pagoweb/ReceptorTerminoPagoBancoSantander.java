package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.util.URLUtil;
import cl.bice.vida.botonpago.modelo.dto.HomologacionConvenioDTO;
import cl.bice.vida.botonpago.modelo.ejb.MedioPagoElectronicoEJB;
import cl.bice.vida.botonpago.modelo.servicelocator.ServiceLocator;
import cl.bice.vida.botonpago.util.RequestUtils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

@WebServlet(name = "ReceptorTerminoPagoBancoSantander", urlPatterns = { "/faces/ReceptorPago/pagoBSantander/salida" })
public class ReceptorTerminoPagoBancoSantander extends HttpServlet {
    @SuppressWarnings("compatibility:-7914723351917888574")
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(ReceptorTerminoPagoBancoSantander.class);
    
    private static final String CONTENT_TYPE = "text/html; charset=ISO-8859-1";
    private static final Long BANCO_SANTANDER = new Long(2);
    private static final Long EMPRESA_BICE_VIDA = new Long(1);    
    private static final Long EMPRESA_BICE_HIPOTECARIA = new Long(2);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        logger.info("doPost(HttpServletRequest, HttpServletResponse) - start");
        String idtrx = null;                
        String idcomercio = null;
        String codret;
        String nropagos;
        String total;
        String indpago;
        String idtrxbanco;
        String mpfin;  
        HomologacionConvenioDTO dtoHomo = null;
        RequestUtils.printRequest(request, "ReceptorTerminoPagoBancoSantander");
        
        /*
         * Removemos TimeTolive
         */
        request.getSession().removeAttribute("timetolivepagoweb");      
        
        /**
         * Obtiene XML mpfin
         */
        logger.info("doPost(HttpServletRequest, HttpServletResponse) - recuperando variable TX desde el HttpRequest");
        mpfin = URLUtil.getStringRequestHttpEncapsulated(request, "TX");
        
        logger.info("doPost(HttpServletRequest, HttpServletResponse) - xml enviado en TX (MPFIN): " + mpfin);
                
        if(mpfin != null && !mpfin.equals("")){
                                
            logger.info("doPost(HttpServletRequest, HttpServletResponse) - xml sin problemas evaluando valores y logica");
            /**
             * Proceso de leer XML, obtener los datos del MPFIN y proceso de los datos
             */
            try {
                    
                    /**
                     * Verifica XML MPFIN
                     */
                    logger.info("doPost(HttpServletRequest, HttpServletResponse) - parseando xml MPFIN");
                    Document document       = DocumentHelper.parseText(mpfin);
                    Node nidtrx             = document.selectSingleNode("//MPFIN/IDTRX");                   
                    Node ncodret            = document.selectSingleNode("//MPFIN/CODRET");
                    Node nnropagos          = document.selectSingleNode("//MPFIN/NROPAGOS");
                    Node ntotal             = document.selectSingleNode("//MPFIN/TOTAL");
                    Node nindpago           = document.selectSingleNode("//MPFIN/INDPAGO");
                    Node nidtrxbanco        = document.selectSingleNode("//MPFIN/IDREG");
                    
            
                    /**
                     * Obtiene datos del XML MPFIN
                     */
                    logger.info("doPost(HttpServletRequest, HttpServletResponse) - recuperando ");
                    idcomercio = "0000000000";
                    idtrx = nidtrx.getText();
                    codret = ncodret.getText();
                    nropagos = nnropagos.getText();
                    total = ntotal.getText();
                    indpago = nindpago.getText();
                    idtrxbanco = nidtrxbanco.getText();             
                   
                
                
                /**
                 * Instancia y carga datos al dto para actualizar transacción
                 */
                 logger.info("doPost(HttpServletRequest, HttpServletResponse) - seteando valores al DTO");
                 SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();                    
                 
                 MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();
                 logger.info("doPost() - ****NUMERO TRANSACCION COMERCIO: " + idtrx );
                //TODO:BUSCAR IDENTIFICAR REAL DE LA TRANSACCION EN TABLA TEMPORAL
                 dtoHomo = ejb.getHomologacionConvenioByIdComercioTrx(idtrx);
                if (dtoHomo == null) {
                    dtoHomo = new HomologacionConvenioDTO();
                    String parteNumver = idcomercio != null ? idtrx.substring(idcomercio.length()):idtrx;
                    dtoHomo.setIdTransaccion(new Long(parteNumver));
                }
                 logger.info("doPost() - ****NUMERO TRANSACCION COMERCIO: " + idtrx + " NUMERO TRANSACCION BICE  :"+ dtoHomo.getIdTransaccion());  
        
                 //RECUPERA INFORMACION DE LA TRANSACCION
                 BpiTraTransaccionesTbl trx = ejb.findTransaccionCodigoEstadoByNumTransaccion(dtoHomo.getIdTransaccion());
                 Long idComercio = ejb.getIdComercioBICEByMedioEmpresa((trx.getCodigoEmpresa()!=null && trx.getCodigoEmpresa().equalsIgnoreCase("BV") ? EMPRESA_BICE_VIDA:EMPRESA_BICE_HIPOTECARIA) ,BANCO_SANTANDER);
                
                
                 dto.setIdTrx(dtoHomo.getIdTransaccion());
                 dto.setCodRet(Integer.parseInt(codret));
                 dto.setNropPagos(Integer.parseInt(nropagos));
                 dto.setMontoPago(Integer.parseInt(total));
                 dto.setDescRet(null);
                 dto.setIdCom(idComercio);
                 dto.setIdTrxBanco(idtrxbanco);
                 dto.setIndPago(indpago);
                 dto.setMedioNumeroCuotas(new Integer("0"));
                 dto.setMedioCodigoRespuesta(codret);
                 dto.setMedioNumeroTarjeta("");
                 dto.setMedioFechaPago(new java.util.Date(System.currentTimeMillis()));
                 dto.setMedioTipoTarjeta("");                 
                 dto.setMedioCodigoAutorizacion("");
                 dto.setMedioOrdenCompra(idtrx);        
                 
                 
            
                /**
                 * Llamada al SP para actualizar datos de la transacción
                 */
                 logger.info("doPost(HttpServletRequest, HttpServletResponse) - llamando EJB de negocios");
                
                 logger.info("doPost(HttpServletRequest, HttpServletResponse) - llamando metodo de negocio actualizaTransaccionSP()");
                 Boolean resultado = ejb.actualizaTransaccionSPAsync(dto);                                  
                 logger.info("doPost(HttpServletRequest, HttpServletResponse) - resultado de metodo actualizaTransaccionSP() : " + resultado.booleanValue());

                /**
                 * Pago realizado exitosamente
                 */
                 logger.info("doPost(HttpServletRequest, HttpServletResponse) - evaluando si el pago fue exitoso");
                 if( codret.equals("0000") && indpago.equals("S") ) {
                    logger.info("doPost(HttpServletRequest, HttpServletResponse) - Pago realizado exitosamente, MPFIN recibido = " + mpfin);
                 } else {
                    logger.error("doPost(HttpServletRequest, HttpServletResponse) - Pago con problemas, MPFIN recibido = " + mpfin); 
                 }

            } catch (Exception e) {
                    logger.error("doPost(HttpServletRequest, HttpServletResponse) - Error en la ejecucion de termino : " + e.getMessage(), e);                      
            }                
        }        
        
        /**
         * Comprobante 
         */
        logger.info("doPost(HttpServletRequest, HttpServletResponse) - evaluando redireccionamiento desde el web a comprobante de pago");
        if (dtoHomo!=null) {
            logger.info("doPost(HttpServletRequest, HttpServletResponse) - redireccionado al comprobante");
            doComprobanteAccion(""+dtoHomo.getIdTransaccion(), request, response);
        } else {
            logger.info("doPost(HttpServletRequest, HttpServletResponse) - nada donde redireccionar, revisar codificacion ");
        }
        logger.info("doPost(HttpServletRequest, HttpServletResponse) - end");
        
        
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Termino de Operacion de Pago</title></head>");
        out.println("<body onload=\"javascript:window.close();\">");
        out.println("</body></html>");
        out.close();
    }
    
    /**
     * Accion para ver el comprobante de pago
     * @throws IOException 
     * @throws ServletException 
     */
    public void doComprobanteAccion(String idTrx, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            logger.info("doComprobanteAccion() - iniciando");
                            
            /**
             * Seteamos atributos
             */             
            request.getSession().setAttribute("idtipoconsult", "0");
            request.getSession().setAttribute("idcomprobante", idTrx);
            request.getSession().setAttribute("urlvolver", "volverResumenyPagos");
            request.getSession().setAttribute("email", "S");
            request.getSession().setAttribute("contactcenter", "N");
            
            
            /**
             * Logger de envio
             */
            logger.info("idtipoconsult : " + request.getSession().getAttribute("idtipoconsult"));
            logger.info("idcomprobante : " + request.getSession().getAttribute("idcomprobante"));
            logger.info("urlvolver     : " + request.getSession().getAttribute("urlvolver"));
            logger.info("email         : " + request.getSession().getAttribute("email"));
            logger.info("contactcenter : " + request.getSession().getAttribute("contactcenter"));

            logger.info("doComprobanteAccion() -  terminado");
    }
}
