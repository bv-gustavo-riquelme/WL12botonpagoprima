package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.util.URLUtil;
import cl.bice.vida.botonpago.modelo.dto.HomologacionConvenioDTO;
import cl.bice.vida.botonpago.modelo.ejb.MedioPagoElectronicoEJB;
import cl.bice.vida.botonpago.modelo.servicelocator.ServiceLocator;
import cl.bice.vida.botonpago.util.RequestUtils;
import cl.bice.vida.botonpago.util.ResourcePropertiesUtil;

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

@WebServlet(name = "ReceptorTerminoPagoBancoChile", urlPatterns = { "/faces/ReceptorPago/pagoBChile/salida" })
public class ReceptorTerminoPagoBancoChile extends HttpServlet {
    @SuppressWarnings("compatibility:-6565645233849686320")
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ReceptorTerminoPagoBancoChile.class);

    private static final Long BANCO_CHILE = new Long(1);
    private static final Long EMPRESA_BICE_VIDA = new Long(1);
    private static final Long EMPRESA_BICE_HIPOTECARIA = new Long(2);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idtrx = null;
        String idcomercio = null;
        String codret;
        String nropagos;
        String total;
        String indpago;
        String idtrxbanco;
        String descret;
        HomologacionConvenioDTO dtoHomo = null;
        String mpfin;
        String mpres;
        String xmlResp = "<html><head><script type=\"text/javascript\">window.close();</script></head></html>";
        RequestUtils.printRequest(request, "ReceptorTerminoPagoBancoChile");
        /*
        * Removemos TimeTolive
        */
        request.getSession().removeAttribute("timetolivepagoweb");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String closepopup = "";
        /*String closepopup =
            "<script type=\"text/javascript\" language=\"JavaScript\">" + " try{window.top.cerrarVentanaModal();} catch(e){alert(e.message);try{parent.cerrarVentanaModal();} catch(e){alert(2);alert(e.message);}}   " + "</script>";
        */
        /**
        * Obtiene XML mpfin
        */
        mpfin = URLUtil.getStringRequestHttpEncapsulated(request, "TX");
        logger.info("doPost() ===================>   TERMINO DE PAGO <===========================");
        logger.info("doPost() - MPFIN recibido por medio bancario: " + mpfin);


        if (mpfin != null) {
            if (!(mpfin.equals(""))) {

                /**
             * Proceso de leer XML, obtener los datos del MPFIN y proceso de los datos
             */
                try {
                    /**
                     * Verifica Si es XML MPFIN
                     */
                    Document document = DocumentHelper.parseText(mpfin);
                    Node nmpfin = document.selectSingleNode("//MPFIN");
                    String nameMpfin = nmpfin.getName();

                    if (nameMpfin.equalsIgnoreCase("MPFIN")) {

                        /**
                         * Instancia resto de las variables
                         */
                        Node nidtrx = document.selectSingleNode("//MPFIN/IDTRX");
                        Node ncodret = document.selectSingleNode("//MPFIN/CODRET");
                        Node nnropagos = document.selectSingleNode("//MPFIN/NROPAGOS");
                        Node ntotal = document.selectSingleNode("//MPFIN/TOTAL");
                        Node nindpago = document.selectSingleNode("//MPFIN/INDPAGO");
                        Node nidtrxbanco = document.selectSingleNode("//MPFIN/IDREG");


                        /**
                         * Obtiene datos del XML MPFIN
                         */
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
                        SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();

                        MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();

                        logger.info("doPost() - ****NUMERO TRANSACCION COMERCIO: " + idtrx);
                        //TODO:BUSCAR IDENTIFICAR REAL DE LA TRANSACCION EN TABLA TEMPORAL
                        dtoHomo = ejb.getHomologacionConvenioByIdComercioTrx(idtrx);
                        if (dtoHomo == null) {
                            dtoHomo = new HomologacionConvenioDTO();
                            String parteNumver = idcomercio != null ? idtrx.substring(idcomercio.length()) : idtrx;
                            dtoHomo.setIdTransaccion(new Long(parteNumver));
                        }
                        logger.info("doPost() - ****NUMERO TRANSACCION COMERCIO: " + idtrx +
                                    " NUMERO TRANSACCION BICE  :" + dtoHomo.getIdTransaccion());

                        //RECUPERA INFORMACION DE LA TRANSACCION
                        BpiTraTransaccionesTbl trx =
                            ejb.findTransaccionCodigoEstadoByNumTransaccion(dtoHomo.getIdTransaccion());
                        Long idComercio =
                            ejb.getIdComercioBICEByMedioEmpresa((trx.getCodigoEmpresa() != null &&
                                                                 trx.getCodigoEmpresa().equalsIgnoreCase("BV") ?
                                                                 EMPRESA_BICE_VIDA : EMPRESA_BICE_HIPOTECARIA),
                                                                BANCO_CHILE);


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
                         * Obtiene url del cgi bancario y lo setea en el DTO
                         */
                        String urlBanco = "";

                        String idCom = "" + idComercio;
                        if (idCom.equalsIgnoreCase(ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.bancochile.vida.idcom"))) {
                            urlBanco = ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.bancochile.vida.url");
                        } else if (idCom.equalsIgnoreCase(ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.bancochile.hipotecaria.idcom"))) {
                            urlBanco =
                                ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.bancochile.hipotecaria.url");
                        }
                        logger.info("doPost() - urlBanco: " + urlBanco);

                        /**
                         * Pago realizado exitosamente
                         */
                        if (codret.equals("0000") && indpago.equals("S")) {
                            /**
                             * Instancia EJB para actualizar datos
                             */
                            dto.setCodRet(0);
                            dto.setDescRet("Transaccion OK");
                            ejb.actualizaTransaccionSPAsync(dto);
                            logger.info("doPost() - ***< Pago realizado exitosamente, MPFIN recibido = " + mpfin);

                        } else if (codret.equals("0001") && indpago.equals("S")) {
                            /**
                             * Pago realizado pero esperando confirmacíón
                             */
                            String mpcon;
                            String url = urlBanco;
                            mpcon =
                                "<MPCON>" + "<IDCOM>" + idCom + "</IDCOM>" + "<IDTRX>" + idtrx + "</IDTRX>" +
                                "<TOTAL>" + total + "</TOTAL>" + "<IDREG>" + idtrxbanco + "</IDREG>" + "</MPCON>";

                            logger.info("doPost() - Enviando MPCON = " + mpcon);
                            /*
                            HttpClient client = new HttpClient();
                            
                            PostMethod method = new PostMethod(url);
                            method.addParameter("ACTION", "VP");
                            method.addParameter("tx", mpcon);
                            */
                            //logger.info("LLamando URL [" + url + "]con xml [" + mpcon + "]");
                            //xmlResp = URLUtil.callUrlPost(url, "ACTION=VP&"+mpcon, "POST");
                            //EJECUTA PETICION HTTP AL BANCO CHILE
                            //int statusCode = client.executeMethod(method);
                            int statusCode = 1;
                            if (statusCode != -1) {
                                //String xmlResp = method.getResponseBodyAsString();
                                logger.info("doPost() - Dato retornado por envio MPCON = " + xmlResp);
                                //method.releaseConnection();

                                //PROCESANDO XML DE RESPUESTA MPRES
                                /*
                                Document documentMpres = DocumentHelper.parseText(xmlResp);
                                Node nmpres = documentMpres.selectSingleNode("//MPRES");
                                String nameMpres = nmpres.getName();
                                if (nameMpres.equalsIgnoreCase("MPRES")) {
                                    Node presncodret = documentMpres.selectSingleNode("//MPRES/CODRET");
                                    Node presndescret = documentMpres.selectSingleNode("//MPRES/DESCRET");
*/
                                    //Node presnidcom         = documentMpres.selectSingleNode("//MPRES/IDCOM");
                                    //Node presnidtrx         = documentMpres.selectSingleNode("//MPRES/IDTRX");
                                    //Node presntotal         = documentMpres.selectSingleNode("//MPRES/TOTAL");
                                    //Node presnnropagos      = documentMpres.selectSingleNode("//MPRES/NROPAGOS");
                                    //Node presnidtrxbanco    = documentMpres.selectSingleNode("//MPRES/IDREG");

                                    //IMPORTANTE
                                    /*Integer codretPres = new Integer(presncodret.getText());
                                    String descripPres = presndescret.getText();
                                    */
                                    Integer codretPres = 0;
                                    String descripPres = "C60";
                                    //CONFIRMA LA OPERACION DE PAGO CON LA ULTIMA RESPUESTA
                                    dto.setCodRet(codretPres);
                                    dto.setDescRet(descripPres);

                                    ejb.actualizaTransaccionSPAsync(dto);
                                    closepopup = "<html><head><script type='text/javascript'>window.close();</script></head></html>";
/*
                                } else {
                                    //NO HAY RETORNO DE XML MPRES
                                    dto.setCodRet(99);
                                    dto.setDescRet("No se obtuvo respuesta de confirmacion MPRES a la transaccion enviada. Revise comunicacion y mesnajeria con medio de pago Banco Chile.");
                                    ejb.actualizaTransaccionSPAsync(dto);

                                }*/

                            } else {
                                //NO HAY RETORNO DE XML MPRES
                                dto.setCodRet(99);
                                dto.setDescRet("Medio de pago no respondio la confirmacion MPCON de la transaccion, revise comunicacion internet de comuniacion con el medio de pago Banco Chile.");
                                ejb.actualizaTransaccionSPAsync(dto);

                            }

                        } else {
                            //RECHAZADO
                            logger.info("doPost() - Pago no realizado, MPFIN recibido = " + mpfin);

                            dto.setCodRet(99);
                            dto.setDescRet("Pago Rechazado por el Banco con Codigo: " + codret);
                            ejb.actualizaTransaccionSPAsync(dto);


                        }

                        //Restablece la accion de pago
                        if (dtoHomo != null) {
                            logger.info("doPost(HttpServletRequest, HttpServletResponse) - Seteando la transaccion del comprobante");
                            doComprobanteAccion("" + dtoHomo.getIdTransaccion(), request, response);
                        } else {
                            logger.info("doPost(HttpServletRequest, HttpServletResponse) - No se setea valor del ID de Transacción");
                        }
                        logger.info("doPost(HttpServletRequest, HttpServletResponse) - end");
                        out.print(closepopup);
                        return;
                    } else {
                        logger.info("doPost() - No se encontro MPIN en la respuesta del banco");
                    }
                } catch(org.dom4j.DocumentException e) {
                    closepopup = xmlResp;
                    logger.error("doPost() - Excepción ocurrida: " + e.getMessage(), e);
                } catch (Exception e) {
                    logger.error("doPost() - Excepción ocurrida: " + e.getMessage(), e);
                }
            }
        }
        logger.info("doPost() ===================>   TERMINO DE PAGO <===========================");
        out.print(closepopup);
        out.close();
    }

    /**
     * Accion para ver el comprobante de pago
     * @throws IOException
     * @throws ServletException
     */
    public void doComprobanteAccion(String idTrx, HttpServletRequest request,
                                    HttpServletResponse response) throws ServletException, IOException {
        logger.info("doComprobanteAccion() - iniciando");

        /**
             * Seteamos atributos
             */
        request.getSession().setAttribute("idtipoconsult", "0");
        request.getSession().setAttribute("idcomprobante", idTrx);
        request.getSession().setAttribute("urlvolver", "volverResumenyPagos");
        request.getSession().setAttribute("email", "S");
        request.getSession().setAttribute("contactcenter", "N");

        /*
             * Removemos TimeTolive
             */
        //request.getSession().removeAttribute("timetolivepagoweb");

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
