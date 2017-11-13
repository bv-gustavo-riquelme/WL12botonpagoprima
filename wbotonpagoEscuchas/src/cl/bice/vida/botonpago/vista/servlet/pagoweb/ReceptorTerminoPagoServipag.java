package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.util.EncryptSeguridadBiceUtil;
import cl.bice.vida.botonpago.modelo.ejb.MedioPagoElectronicoEJB;
import cl.bice.vida.botonpago.modelo.servicelocator.ServiceLocator;
import cl.bice.vida.botonpago.util.Comunes;
import cl.bice.vida.botonpago.util.RequestUtils;
import cl.bice.vida.botonpago.util.ResourcePropertiesUtil;
import cl.bice.xmlbeans.servipag.confirmacion.ServipagConfirmacionDocumentConfirmacion;
import cl.bice.xmlbeans.servipag.respuesta.Servipag;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

@WebServlet(name = "ReceptorTerminoPagoServipag", urlPatterns = { "/faces/ReceptorPago/ReceptorTerminoPagoServipag" })
public class ReceptorTerminoPagoServipag extends HttpServlet {
    @SuppressWarnings("compatibility:2972148656753589383")
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ReceptorTerminoPagoServipag.class);
    private String IDCOMSERVIPAG = "";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Atención: Esta aplicacion solo acepta informacion via POST.");
        
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.print("Atención: Esta aplicacion solo acepta informacion via POST.");
        out.close();
        //doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("ReceptorTerminoPagoServipag XML2 - start");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String xmlconfirmacion = request.getParameter("XML");
        String xmlConfirmacion2 = "";
        String xmlrespuesta = null;
        RequestUtils.printRequest(request, "ReceptorTerminoPagoServipag");
        //logger.info("ReceptorTerminoPagoServipag - XML2 - " + xmlconfirmacion);
        IDCOMSERVIPAG = ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.servipag.vida.idcom");
        logger.info("Id de comercio Servipag :" + IDCOMSERVIPAG);
        xmlConfirmacion2 = xmlconfirmacion;
        request.getSession().removeAttribute("timetolivepagoweb");

        logger.debug("ReceptorTerminoPagoServipag - xmlconfirmacion = " + xmlconfirmacion);
        try {

            if (xmlconfirmacion != null) {
                logger.debug("xmlconfirmacion distinto  de null");
                /**
                 * Reemplaza contenido de tag raiz por una colicion de
                 * nombres
                 */
                xmlconfirmacion = Comunes.replaceString(xmlconfirmacion, "<Servipag>", "<ServipagConfirmacion>");
                xmlconfirmacion = Comunes.replaceString(xmlconfirmacion, "</Servipag>", "</ServipagConfirmacion>");

                logger.debug("Replace xmlconfirmacion = " + xmlconfirmacion);

                /**
                 * Parsea el XML en Documento XMLBEANS
                 */
                logger.debug("doPost() - Parsea xml de confirmacion");
                ServipagConfirmacionDocumentConfirmacion confirmacion =
                    ServipagConfirmacionDocumentConfirmacion.Factory.parse(xmlconfirmacion);
                
                boolean comovalida = confirmacion.validate();

                //Servipag servipag = validaXMLServipag2(xmlConfirmacion2);
                logger.info("Xml confirmacion valida = " + ((confirmacion == null) ? false : true));

                if (comovalida) {
                    String idCliente = confirmacion.getServipagConfirmacion().getIdTxCliente();
                    String idServipag = confirmacion.getServipagConfirmacion().getIdTrxServipag();
                    String montopago = confirmacion.getServipagConfirmacion().getMonto();
                    String identificador = confirmacion.getServipagConfirmacion().getCodigoIdentificador();
                    String fechapago = confirmacion.getServipagConfirmacion().getFechaPago();
                    String boleta = confirmacion.getServipagConfirmacion().getBoleta();
                    String firmaServipag = confirmacion.getServipagConfirmacion().getFirmaServipag().replace(" ", "+");
                    logger.debug("Inicializa algoritmo de desencriptacion");
                    Signature sig = Signature.getInstance("MD5WithRSA");

                    logger.debug("recupera el string encriptado de servipag");
                    //String firmaServipag = confirmacion.getServipagConfirmacion().getFirmaServipag();
                    logger.info("String encriptado de servipag: " + firmaServipag);
                    byte[] firmadecode = EncryptSeguridadBiceUtil.base64decode(firmaServipag);

                    logger.debug("confeccionar el string que supuestamente debe benir de forma extacta firma por servipag");
                    MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();
                    
                    //String dataOriginalSend = idServipag + idCliente + fechapago + montopago;
                    //String boleta = ejb.getNroBoletaByIdTrx(Long.parseLong(idTrxServipag) );

                    String dataOriginalSend = idServipag + idCliente + montopago;
                        //codCanalPago + fechaSolicitada + monto;
                        ;


                    //dataOriginalSend += "1" +servipag.getIdTxCliente() + boleta + monto + fechaSolicitada;
                    
                    logger.info("Dato a obtener de la firma \"IDTRXSERVIPAGIDTXCLIENTEMONTO\": " + dataOriginalSend);

                    logger.debug("Construye claves a partir de base 64");
                    PublicKey keyPubl =
                        EncryptSeguridadBiceUtil.getClavePublicaByBase64(ResourcePropertiesUtil.getProperty("servipag.verising.clave.publica"));

                    logger.debug("Verifica con clave publica seteada");
                    boolean resultadodesencript =
                        EncryptSeguridadBiceUtil.verisingDataSignature(sig, keyPubl, dataOriginalSend.getBytes(),
                                                                       firmadecode);
                    logger.info("Verificacion entregó: " + resultadodesencript);

                    logger.info("idServipag = " + idServipag);
                    logger.info("montopago = " + montopago);
                    logger.info("identificador = " + identificador);
                    logger.info("boleta = " + boleta);

                    String nropagos = boleta.substring((boleta.length() - 2));
                    logger.info("nropagos = " + nropagos);

                    /**
                     * Instancia y carga datos al dto para actualizar
                     * transacción
                     */
                    logger.info("Instancia y carga datos al dto para actualizar transacción");
                    SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
                    dto.setIdTrx(Long.parseLong(idCliente));
                    dto.setCodRet(Integer.parseInt("0")); // Codigo de Retorno
                    dto.setDescRet("Transaccion Pagada OK");
                    // pagado por obligacion [0=pagado, 1=no pagado]
                    dto.setNropPagos(Integer.parseInt(nropagos)); // cuando
                    // confirmacion sabre cuantas boletas pago
                    dto.setMontoPago(Integer.parseInt(montopago));
                    dto.setDescRet(null); // Descripcion de retorno
                    dto.setIdCom(Long.parseLong(IDCOMSERVIPAG)); // Saber
                    // cual sera el id de comercio para servipag
                    dto.setIdTrxBanco(idServipag);
                    dto.setIndPago("S");// S=Si pagado / N=No pagado
                    dto.setMedioNumeroCuotas(new Integer("0"));
                    dto.setMedioCodigoRespuesta("0");
                    dto.setMedioNumeroTarjeta("");
                    dto.setMedioFechaPago(new java.util.Date(System.currentTimeMillis()));
                    dto.setMedioTipoTarjeta("");           
                    dto.setMedioCodigoAutorizacion("");
                    dto.setMedioOrdenCompra(boleta);    

                    if (resultadodesencript) {
                        /**
                         * Llamada al SP para actualizar datos de la transacción
                         */
                        logger.debug("Llamada al SP para actualizar datos de la transacción");

                        Boolean resultado = ejb.actualizaTransaccionSPAsync(dto);
                        logger.info("Resultado llamada al SP = " + resultado);

                        if (resultado.booleanValue() == true) {
                            /**
                             * Responder Ok a la Operacion
                             */
                            xmlrespuesta = getXmlRespuestaConfirmacion("0", "Confirmacion de pago sin problemas.");
                            logger.info("xml de respuesta = " + xmlrespuesta);
                        } else {
                            /**
                             * Imposible pagar documento en Framework de BICE
                            */
                            xmlrespuesta = getXmlRespuestaConfirmacion("1", "No se logro confirmar la operacion de pago en BICE.");
                            logger.info("xml de respuesta = " + xmlrespuesta);
                        }
                    } else {
                        /**
                         * La firma no es valida
                        */
                        xmlrespuesta = getXmlRespuestaConfirmacion("4", "La firma no es valida, tiene problemas.");
                        logger.info("firma con problemas = " + firmaServipag);
                        dto.setCodRet(99);
                        dto.setDescRet("Firma no valida de los datos se rechaza el pago");
                        Boolean resultadxox = ejb.actualizaTransaccionSPAsync(dto);
                    }
                } else {
                    /**
                     * El XML no es valido
                     */
                    xmlrespuesta = getXmlRespuestaConfirmacion("2", "El Xml enviado no es validoo tienes problemas.");
                    logger.info("doPost() - xml de respuesta = " + xmlrespuesta);
                }

            } else {
                /**
                 * Sin Datos el XML
                 */
                xmlrespuesta = getXmlRespuestaConfirmacion("3", "No se ha enviado el parametro de confirmacion o no tiene valor alguno.");
                logger.info("xml de respuesta = " + xmlrespuesta);
            }
        } catch (Exception e) {
            /**
             * Aqui Responder XML Malo a Servipag con confirmacion con problemas
             */
            xmlrespuesta = getXmlRespuestaConfirmacion("9", "Problemas en parseo de xml : " + e.getMessage());
            logger.info("xml de respuesta = " + xmlrespuesta);

        } finally {

            /**
             * Pinta frespuesta como contenido de XML
             */
            if (xmlrespuesta != null) {
                out.println(xmlrespuesta);
            }
            logger.debug("doPost() - out.close()");
            out.close();

        }
        logger.debug("doPost() - end");
    }
    
    private Servipag validaXMLServipag2(String xmlconfirmacion) {
        Servipag servipag = null;
        try {
            MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();

            JAXBContext jaxbContext = JAXBContext.newInstance(Servipag.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xmlconfirmacion);
            servipag = (Servipag) unmarshaller.unmarshal(reader);

            /*
                logger.info("XMLRetornado por servipag -> " + JAXBUtil.convertObjectTOXmlString(servipag) );

                String clavePublicaServipag = ResourceProperties.getProperty("verising.clave.publica");

                String idTrxServipag = servipag.getIdTrxServipag();
                String codCanalPago = idTrxServipag.substring(1, 3);
                String fechaSolicitada = idTrxServipag.substring(3,9);
                String monto = idTrxServipag.substring(9, idTrxServipag.length()-2);
                String secuencia = idTrxServipag.substring(idTrxServipag.length()-2, idTrxServipag.length());
                String firmaServipag = servipag.getFirmaServipag();


                String boleta = ejb.getNroBoletaByIdTrx(Long.parseLong(idTrxServipag) );

                String datafirmar =
                    codCanalPago + fechaSolicitada + monto;


                datafirmar += "1" +servipag.getIdTxCliente() + boleta + monto + fechaSolicitada;

                Signature sig = Signature.getInstance("MD5WithRSA");
                byte[] firmadecode = EncryptSeguridadBiceUtil.base64decode(firmaServipag);

                PublicKey keyPubl = EncryptSeguridadBiceUtil.getClavePublicaByBase64(ResourceProperties.getProperty("servipag.verising.clave.publica"));

                logger.info("doPost() - Verifica con clave publica seteada");
                boolean resultadodesencript = EncryptSeguridadBiceUtil.verisingDataSignature(sig, keyPubl, datafirmar .getBytes(), firmadecode );
                logger.info("doPost() - Verificacion entregó: " + resultadodesencript);


                logger.info("doPost() - idCliente = " + servipag.getIdTxCliente());
                logger.info("doPost() - idServipag = " + servipag.getIdTrxServipag());
                logger.info("doPost() - montopago = " + monto);
                logger.info("doPost() - boleta = " + boleta);

                String nropagos = boleta.substring((boleta.length() - 2));
                logger.info("doPost() - nropagos = " + nropagos);


                logger.info("doPost() - Instancia y carga datos al dto para actualizar transacción");
                SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
                dto.setIdTrx(Long.parseLong(servipag.getIdTxCliente()));
                dto.setCodRet(Integer.parseInt("0")); // Codigo de Retorno
                dto.setDescRet("Transaccion Pagada OK");
                dto.setNropPagos(Integer.parseInt(nropagos));
                dto.setMontoPago(Integer.parseInt(monto));
                dto.setDescRet(null); // Descripcion de retorno
                dto.setIdCom(Long.parseLong(IDCOMSERVIPAG));
                dto.setIdTrxBanco(servipag.getIdTrxServipag());
                dto.setIndPago("S");// S=Si pagado / N=No pagado
                dto.setMedioNumeroCuotas(new Integer("0"));
                dto.setMedioCodigoRespuesta("0");
                dto.setMedioNumeroTarjeta(boleta);
                dto.setMedioFechaPago(new java.util.Date(System.currentTimeMillis()));
                dto.setMedioTipoTarjeta("");
                dto.setMedioCodigoAutorizacion("");
                dto.setMedioOrdenCompra(boleta);


                xmlOK = resultadodesencript;
                */
        } catch (JAXBException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        }


        return servipag;
    }
    
    /**
     * Funcion que genera la respuesta al pago de servipag
     *
     * @param codigo
     * @param mensaje
     * @return
     */
    private String getXmlRespuestaConfirmacion(String codigo, String mensaje) {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "<Servipag>" + "<CodigoRetorno>" + codigo +
               "</CodigoRetorno>" + "<MensajeRetorno>" + mensaje + "</MensajeRetorno>" + "</Servipag>";
    }


    /**
     * Main para generar una data de respuesta valida de servipag
     * para realizar un termino de pago de transaccion.
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException,
                                                  InvalidKeyException, SignatureException {

        ServipagConfirmacionDocumentConfirmacion doc = ServipagConfirmacionDocumentConfirmacion.Factory.newInstance();
        cl.bice.xmlbeans.servipag.confirmacion.ServipagConfirmacionDocumentConfirmacion.ServipagConfirmacion servip =
            ServipagConfirmacionDocumentConfirmacion.ServipagConfirmacion.Factory.newInstance();
        servip.setIdTxCliente("123456789");
        servip.setIdTrxServipag("23764827364876234");
        servip.setMonto("10000");
        servip.setCodigoIdentificador("1");
        servip.setFechaPago("01/02/2012");
        servip.setBoleta("324324234");
        servip.setCodMedioPago("1");
        servip.setFechaContable("01/02/2012");


        //FIRMA CON LLAVE PRIVADA
        Signature sig = Signature.getInstance("MD5WithRSA");
        String dataOriginalSend =
            servip.getIdTrxServipag() + servip.getIdTxCliente() + servip.getFechaPago() + servip.getMonto();
        PrivateKey keyPrivate =
            EncryptSeguridadBiceUtil.getClavePrivadaByBase64("MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIgRvtD/RGTWc5XpYSWA8CXyLFiNoroZJg80DVeqnAaos6cfPlNjitWt2wySqB1RdZRH4/PcOQAxPkSglt+w+WHRgpjHUoVm980e8pms2m2zG5eiNk7hrLCMT9T7BDnHciAbXL/EbuPgYYL7UI/HriG45yg02gu/YL846ZfWBL1zAgMBAAECgYB0guVmZj31LmAYzJafm3GGvIBWbykYuvII8KRAHdmuJgR3JNykYMb/wvefI3EKcbcBejFpnXe9f2z3LJ8j+ZD/fsIvQlmUR+bQzI+moJyE/PWF2uTYJuzUaQPHd85sw0NnjT44/LZKoI5I1LwSEe5ijBddkET8x2Sx7xnGBVqOeQJBAL9HlkR50ynS9piUSwvaRMvIr1xRvX1yw4FmNPRqc2x2B7UrYrK10dpixybsdUuIPQs3pHIPN6oSNFO3eEbD3yUCQQC2G+gePAwDyWaS/mDyw0mjOeCFPHUN1nPGQChT+uZLb+G1TT2BeXZamt88BQCh9s2dq09pBVD6JKKulQP6TzK3AkBfjn+/Taz0I/QVXV/wV+Yud99DG4KyfPa2WjzyLYvkD0Liev4fkT15AAfFpjMivLjiNbXtVTTVvapueCWaFJgRAkBLwlBOQ6y75o1FmY27cIKx5OICi6QmnZCMaSRdy4MC3wiG5BsyQdV62/MChRI+Tu47KYlfp1aIubTz7Ao7l7cBAkBlsN8iU6ewKDOGh+HHoM+dqu+4AZ4n1g4RccZkcwZzPLefY1t4cJFN61UwvYcU/guWTUZ9lisopnkSyj9K22A6");
        byte[] textoFirmado = EncryptSeguridadBiceUtil.firmarData(sig, keyPrivate, dataOriginalSend.getBytes());
        servip.setFirmaServipag(EncryptSeguridadBiceUtil.base64encode(textoFirmado));
        doc.setServipagConfirmacion(servip);
        System.out.println(doc.toString());


        //VERIFICA CON LLAVE PUBLICA
        byte[] firmadecode = EncryptSeguridadBiceUtil.base64decode(servip.getFirmaServipag());
        PublicKey keyPubl =
            EncryptSeguridadBiceUtil.getClavePublicaByBase64("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIEb7Q/0Rk1nOV6WElgPAl8ixYjaK6GSYPNA1XqpwGqLOnHz5TY4rVrdsMkqgdUXWUR+Pz3DkAMT5EoJbfsPlh0YKYx1KFZvfNHvKZrNptsxuXojZO4aywjE/U+wQ5x3IgG1y/xG7j4GGC+1CPx64huOcoNNoLv2C/OOmX1gS9cwIDAQAB");
        boolean resultadodesencript =
            EncryptSeguridadBiceUtil.verisingDataSignature(sig, keyPubl, dataOriginalSend.getBytes(), firmadecode);
        System.out.println("Firmado:" + resultadodesencript);


    }
}
