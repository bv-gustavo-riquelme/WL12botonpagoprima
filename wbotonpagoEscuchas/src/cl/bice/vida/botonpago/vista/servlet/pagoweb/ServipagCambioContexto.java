package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.common.util.EncryptSeguridadBiceUtil;
import cl.bice.vida.botonpago.util.Comunes;
import cl.bice.vida.botonpago.util.RequestUtils;
import cl.bice.vida.botonpago.util.ResourcePropertiesUtil;
import cl.bice.xmlbeans.servipag.cambiocontexto.ServipagCambioContextoDocumentCambioContexto;

import java.io.IOException;
import java.io.PrintWriter;

import java.security.PublicKey;
import java.security.Signature;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(name = "ServipagCambioContexto", urlPatterns = { "/faces/ReceptorPago/CambioContextoServipag" }, description = "Cambio Contexto Servipag")
public class ServipagCambioContexto extends HttpServlet {
    @SuppressWarnings("compatibility:4469247533865724698")
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ServipagCambioContexto.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet(HttpServletRequest, HttpServletResponse) CambioContextoServipag - start");
        doPost(request, response);
        logger.info("doGet(HttpServletRequest, HttpServletResponse) CambioContextoServipag - end");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost() - start");
        RequestUtils.printRequest(request, "ServipagCambioContexto");
        PrintWriter out = response.getWriter();
        String xmlrespuesta = "";
        response.setContentType("text/plain");
        String xmlcambiocontexto = request.getParameter("XML");

        /*
        * Removemos TimeTolive
        */
        request.getSession().removeAttribute("timetolivepagoweb");

        logger.info("doPost() - xmlconfirmacion = " + xmlcambiocontexto);
        try {

            if (xmlcambiocontexto != null) {
                logger.info("doPost() - xmlconfirmacion distinto  de null");
                /**
                 * Reemplaza contenido de tag raiz por una maldita cohilicion de
                 * nombres
                 */
                xmlcambiocontexto = Comunes.replaceString(xmlcambiocontexto, "<Servipag>", "<ServipagCambioContexto>");
                xmlcambiocontexto = Comunes.replaceString(xmlcambiocontexto, "</Servipag>", "</ServipagCambioContexto>");

                logger.info("doPost() - Replace xmlconfirmacion = " + xmlcambiocontexto);

                /**
                 * Parsea el XML en Documento XMLBEANS
                 */
                logger.info("doPost() - Parsea xml de confirmacion");
                ServipagCambioContextoDocumentCambioContexto cambiocontexto =
                    ServipagCambioContextoDocumentCambioContexto.Factory.parse(xmlcambiocontexto);
                boolean comovalida = cambiocontexto.validate();
                logger.info("doPost() - Xml confirmacion valida = " + comovalida);
                if (comovalida) {

                    String idCliente = cambiocontexto.getServipagCambioContexto().getIdTxCliente();
                    String idServipag = cambiocontexto.getServipagCambioContexto().getIdTrxServipag();
                    String estadopago = cambiocontexto.getServipagCambioContexto().getEstadoPago();
                    
                    logger.info("doPost() - idCliente = " + idCliente);
                    logger.info("doPost() - idServipag = " + idServipag);
                    logger.info("doPost() - estadopago = " + estadopago);


                    logger.info("doPost() - Inicializa algoritmo de desencriptacion");
                    Signature sig = Signature.getInstance("MD5WithRSA");

                    logger.info("doPost() - recupera el string encriptado de servipag");
                    String firmaServipag = cambiocontexto.getServipagCambioContexto().getFirmaServipag();
                    logger.info("doPost() - String encriptado de servipag: " + firmaServipag);
                    byte[] firmadecode = EncryptSeguridadBiceUtil.base64decode(firmaServipag);

                    logger.info("doPost() - confeccionar el string que supuestamente debe benir de forma extacta firma por servipag");
                    String dataOriginalSend = idCliente;
                    logger.info("doPost() - Dato a obtener de la firma \"IDTXCLIENTE\": " + dataOriginalSend);

                    logger.info("doPost() - Construye claves a partir de base 64");
                    // Caso servipag en CARINA
                    PublicKey keyPubl =
                        EncryptSeguridadBiceUtil.getClavePublicaByBase64(ResourcePropertiesUtil.getProperty("servipag.verising.clave.publica"));
                    // Caso de prueba en forma local
                    //PublicKey keyPubl = EncryptSeguridadBiceUtil.getClavePublicaByBase64(ResourceProperties.getProperty("servipag.signature.clave.publica"));

                    logger.info("doPost() - Verifica con clave publica seteada");
                    boolean resultadodesencript =
                        EncryptSeguridadBiceUtil.verisingDataSignature(sig, keyPubl, dataOriginalSend.getBytes(),
                                                                       firmadecode);
                    logger.info("doPost() - Verificacion entregó: " + resultadodesencript);

                    //Evalua resultado de la validacion del Firma
                    if (resultadodesencript == true) {
                        xmlrespuesta = getXmlRespuestaCambioContexto("1", "OK");
                    } else {
                        xmlrespuesta = getXmlRespuestaCambioContexto("99", "NOOK");
                    }

                } else {
                    /**
                     * El XML no es valido
                     */
                    xmlrespuesta = getXmlRespuestaCambioContexto("2", "El Xml enviado no es válido tienes problemas.");
                    logger.error("doPost() - El Xml enviado no es válido tienes problemas: " + xmlrespuesta);
                }

            } else {
                /**
                 * Sin Datos el XML
                 */
                xmlrespuesta = getXmlRespuestaCambioContexto("3", "No se ha enviado el parametro de confirmacion o no tiene valor alguno.");
                logger.error("doPost() - El xml con problemas, valor recibido es: " + xmlrespuesta);
            }
        } catch (Exception e) {
            /**
             * Aqui Responder XML Malo a Servipag con confirmacion con problemas
             */
            xmlrespuesta = getXmlRespuestaCambioContexto("9", "Problemas en parseo de xml : " + e.getMessage());
            logger.error("doPost() - se produjo un error: " + e.getMessage(), e);

        } finally {

            /**
             * Pinta frespuesta como contenido de XML
             */
            if (xmlrespuesta != null) {
                out.println(xmlrespuesta);
            }
            logger.info("doPost() - out.close()");
            //out.close();

        }
        logger.info("doPost() - end");
    }

    /**
     * Funcion que genera la respuesta al pago de servipag
     *
     * @param codigo
     * @param mensaje
     * @return
     */
    private String getXmlRespuestaCambioContexto(String codigo, String mensaje) {
        return "<Servipag>" + "<CodigoRetorno>" + codigo + "</CodigoRetorno>" + "<MensajeRetorno>" + mensaje +
               "</MensajeRetorno>" + "</Servipag>";
    }
}
