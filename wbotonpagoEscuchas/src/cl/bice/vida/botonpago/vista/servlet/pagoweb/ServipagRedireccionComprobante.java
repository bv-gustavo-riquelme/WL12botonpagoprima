package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.common.util.EncryptSeguridadBiceUtil;
import cl.bice.vida.botonpago.util.Comunes;
import cl.bice.vida.botonpago.util.RequestUtils;
import cl.bice.vida.botonpago.util.ResourcePropertiesUtil;
import cl.bice.xmlbeans.servipag.cambiocontexto.ServipagCambioContextoDocumentCambioContexto;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(name = "ServipagRedireccionComprobante", urlPatterns = { "/faces/ReceptorPago/ReceptorComprobanteServipag" })
public class ServipagRedireccionComprobante extends HttpServlet {
    @SuppressWarnings("compatibility:6863175903481175087")
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ServipagRedireccionComprobante.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    public static void main(String[] args) {
        Signature sig;
        try {
            sig = Signature.getInstance("MD5WithRSA");
            String firmaServipag = "HjhDRucWDa6FVn+vq7Cf5A1vyMBN8MPDoJcHE2SooCiddqGYZhwai3jJffN1UG2dz3sCBZu1onmFqhbGgF+VvWde1Gy1Jm4IfH/hvIB8hIO77ylAge1co7lvHWrkYce7ikyftfp4uRWTHgU+COpo1n/2MDsgzOwvMah2Nec2FIw=";
            logger.info("doPost() - String encriptado de servipag: " + firmaServipag);
            byte[] firmadecode = EncryptSeguridadBiceUtil.base64decode(firmaServipag);

            logger.info("doPost() - confeccionar el string que supuestamente debe benir de forma extacta firma por servipag");
            String idServipag = "36781593";
            String idCliente = "1679566";
            String dataOriginalSend = idServipag+idCliente;
            logger.info("doPost() - Dato a obtener de la firma \"IDTXCLIENTE\": " + dataOriginalSend);

            logger.info("doPost() - Construye claves a partir de base 64");
            // Caso servipag en CARINA
            PublicKey keyPubl =
                EncryptSeguridadBiceUtil.getClavePublicaByBase64(ResourcePropertiesUtil.getProperty("servipag.verising.clave.publica"));
            
            logger.info("doPost() - Verifica con clave publica seteada");
            boolean resultadodesencript =
                EncryptSeguridadBiceUtil.verisingDataSignature(sig, keyPubl, dataOriginalSend.getBytes(), firmadecode);
            logger.info("doPost() - Verificacion entregó: " + resultadodesencript);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("ReceptorComprobanteServipag START");
        doPost(request, response);
        logger.info("ReceptorComprobanteServipag END");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("doPost(HttpServletRequest, HttpServletResponse) - start");
        logger.info("Procesando XML 4");
        RequestUtils.printRequest(request, "ServipagRedireccionComprobante");
        logger.debug("doPost() - start");
        String xmlrespuesta = "";
        String respuesta = "No se realizó el pago, favor cierre la ventana";
        response.setContentType("text/plain");
        String xmlcambiocontexto = request.getParameter("xml");
        String idcomprobantecontexto = null;
        String idcomprobanteservipag = null;


        /*
         * Removemos TimeTolive
         */
        request.getSession().removeAttribute("timetolivepagoweb");

        logger.info("xmlcambiocontexto = " + xmlcambiocontexto);

        try {

            if (xmlcambiocontexto != null) {
                logger.info("xmlcambiocontexto distinto  de null");
                /**
                 * Reemplaza contenido de tag raiz por una maldita cohilicion de
                 * nombres
                 */
                xmlcambiocontexto = Comunes.replaceString(xmlcambiocontexto, "<Servipag>", "<ServipagCambioContexto>");
                xmlcambiocontexto =
                    Comunes.replaceString(xmlcambiocontexto, "</Servipag>", "</ServipagCambioContexto>");

                logger.info("Replace xmlcambiocontexto = " + xmlcambiocontexto);

                /**
                 * Parsea el XML en Documento XMLBEANS
                 */
                logger.info("Parsea xml de xmlcambiocontexto");
                ServipagCambioContextoDocumentCambioContexto cambiocontexto =
                    ServipagCambioContextoDocumentCambioContexto.Factory.parse(xmlcambiocontexto);
                boolean comovalida = cambiocontexto.validate();
                logger.info("Xml xmlcambiocontexto valida = " + comovalida);
                if (comovalida) {

                    String idCliente = cambiocontexto.getServipagCambioContexto().getIdTxCliente();
                    String idServipag = cambiocontexto.getServipagCambioContexto().getIdTrxServipag();
                    String estadopago = cambiocontexto.getServipagCambioContexto().getEstadoPago();

                    idcomprobanteservipag = idCliente;

                    logger.info("IdCliente = " + idCliente);
                    logger.info("IdServipag = " + idServipag);
                    logger.info("Estadopago = " + estadopago);
                    request.getSession().setAttribute("idestadopago", estadopago);

                    logger.debug("Inicializa algoritmo de desencriptacion");
                    Signature sig = Signature.getInstance("MD5WithRSA");

                    logger.debug("recupera el string encriptado de servipag");
                    String firmaServipag = cambiocontexto.getServipagCambioContexto().getFirmaServipag();
                    firmaServipag = firmaServipag.replace(" ", "+");
                    logger.info("String encriptado de servipag: " + firmaServipag);
                    byte[] firmadecode = EncryptSeguridadBiceUtil.base64decode(firmaServipag);

                    logger.debug("confeccionar el string que supuestamente debe benir de forma extacta firma por servipag");
                    String dataOriginalSend = idServipag+idCliente;
                    logger.info("Dato a obtener de la firma IDTRXSERVIPAG + IDTXCLIENTE: " + dataOriginalSend);

                    logger.debug("Construye claves a partir de base 64");
                    // Caso servipag en CARINA
                    PublicKey keyPubl =
                        EncryptSeguridadBiceUtil.getClavePublicaByBase64(ResourcePropertiesUtil.getProperty("servipag.verising.clave.publica"));
                    // Caso de prueba en forma local
                    //PublicKey keyPubl = EncryptSeguridadBiceUtil.getClavePublicaByBase64(ResourceProperties.getProperty("servipag.signature.clave.publica"));

                    logger.debug("Verifica con clave publica seteada");
                    boolean resultadodesencript =
                        EncryptSeguridadBiceUtil.verisingDataSignature(sig, keyPubl, dataOriginalSend.getBytes(), firmadecode);
                    logger.info("Verificacion con clave publica [ " + resultadodesencript + "]");

                    //Evalua resultado de la validacion del Firma
                    if (resultadodesencript == false) {
                        logger.error("doPost() - La verificación del dato encriptado  no es correcta");
                    } else {
                        if("0".equals(estadopago) ) {
                            respuesta = "Pago realizado exitosamente, favor cierre la ventana";        
                        }
                    }
                } else {
                    /**
                     * El XML no es valido
                     */
                    logger.error("doPost() - El Xml enviado no es válido tienes problemas: " + xmlrespuesta);
                }

            } else {
                /**
                 * Sin Datos el XML
                 */
                logger.error("doPost() - El xml con problemas, valor recibido es: " + xmlrespuesta);
            }
        } catch (Exception e) {
            /**
             * Aqui Responder XML Malo a Servipag con confirmacion con problemas
             */
            logger.error("doPost() - se produjo un error: " + e.getMessage(), e);

        } finally {

            /**
              * Evaluamos que comprobante se consultara
              */
            idcomprobantecontexto = (String) request.getSession().getAttribute("idcomprobante");

            if (idcomprobantecontexto == null) {
                idcomprobantecontexto = idcomprobanteservipag;
                logger.info("idcomprobantecontexto es null, y se transforma al idcomprobanteservipag: " + idcomprobanteservipag);
            }

            logger.info("idcomprobantecontexto: " + idcomprobantecontexto);

            logger.debug("Setea atributos");
            
            request.getSession().setAttribute("idtipoconsult", "0");
            request.getSession().setAttribute("idcomprobante", idcomprobantecontexto);
            request.getSession().setAttribute("idnumproducto", null);
            request.getSession().setAttribute("idnumdividendo", null);
            request.getSession().setAttribute("urlvolver", "volverResumenyPagos");
            request.getSession().setAttribute("email", "S");
            request.getSession().setAttribute("contactcenter", "N");
            logger.info("doPost(HttpServletRequest, HttpServletResponse) - end");
            
            response.getOutputStream().println(respuesta);
            response.getOutputStream().close();
            //response.sendRedirect(request.getContextPath() + "/jsp/usuarios/enesperapago.jsf");
        }


        logger.debug("doPost() - end");
    }
}
