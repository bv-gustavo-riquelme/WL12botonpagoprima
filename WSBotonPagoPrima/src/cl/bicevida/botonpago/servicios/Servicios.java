package cl.bicevida.botonpago.servicios;

import cl.bice.vida.botonpago.common.dto.general.BpiDnaDetnavegTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.ResumenRequest;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.common.util.RutUtil;
import cl.bice.vida.botonpago.common.util.StringUtil;

import cl.bice.vida.botonpago.common.vo.ResultadoConsultaVO;
import cl.bice.vida.botonpago.modelo.dao.DAOFactory;
import cl.bice.vida.botonpago.modelo.dto.PersonaDto;
import cl.bice.vida.botonpago.servicios.cliente.SessionEJBDatosClienteClient;


import cl.bice.vida.botonpago.servicios.cliente.SessionEJBDatosClienteWebService;
import cl.bice.vida.botonpago.servicios.cliente.SessionEJBDatosClienteWebServiceService;
import cl.bice.vida.botonpago.servicios.cliente.types.ResponseVODTO;

import cl.bicevida.botonpago.core.PagoPublicoCore;
import cl.bicevida.botonpago.exception.ConsultaComprobanteException;
import cl.bicevida.botonpago.exception.ConsultaDeudaException;
import cl.bicevida.botonpago.exception.PagoPublicoCoreException;
import cl.bicevida.botonpago.servicelocator.ServiceLocatorException;
import cl.bicevida.botonpago.util.EmailUtil;
import cl.bicevida.botonpago.util.LinkPDF;
import cl.bicevida.botonpago.util.ResourceMessageUtil;
import cl.bicevida.botonpago.util.ResourcePropertiesUtil;
import cl.bicevida.botonpago.util.ValidadorRut;
import cl.bicevida.botonpago.vo.in.InCorreo;
import cl.bicevida.botonpago.vo.out.OutMensaje;
import cl.bicevida.botonpago.vo.out.OutPdfPolizas;
import cl.bicevida.botonpago.vo.out.PagoPublicoOut;
import cl.bicevida.botonpago.vo.prima.PagoBancoInfoVo;
import cl.bicevida.botonpago.vo.prima.PagoDetalleCuotaVO;
import cl.bicevida.botonpago.vo.prima.PagoPublicoVO;
import cl.bicevida.botonpago.vo.prima.PagoRutasFilesVO;

import com.sun.xml.ws.client.BindingProviderProperties;

import java.io.InputStream;

import java.io.StringReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import javax.xml.bind.JAXBContext;
import javax.xml.ws.BindingProvider;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;

import org.apache.log4j.Logger;

import org.codehaus.jackson.map.ObjectMapper;

@Path("/prima")
@Produces(MediaType.APPLICATION_JSON)

public class Servicios {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(Servicios.class);
    public static final int TPOTRX_NORMAL = 1;
    public static final int CANAL_BOTON_PAGO_PERFILADO = 1;
    public static final int CANAL_BOTON_PAGO_PUBLICO = 2;
    public static final int CANAL_BOTON_PAGO_APV_EXTRAORDINARIO = 3;
    public static final int CANAL_BOTON_PAGO_PUBLICO_PRIMERA_PRIMA = 4;

    @Context
    ServletContext context;
    @Context
    HttpServletRequest request;

    public Servicios() {
        super();
    }

    @Path("/consultaDeuda")
    @GET
    public PagoPublicoOut consultaDeuda(@QueryParam(value = "rut") String rutParam) {
        //public PagoPublicoVO consultaDeuda(@QueryParam(value="rut") String rutParam){
        ValidadorRut validador = new ValidadorRut();
        PagoPublicoVO pagoConsolidado = new PagoPublicoVO();
        PagoPublicoOut pagoOut = new PagoPublicoOut();
        try {

            //Valida el RUT
            validador.validaRut(rutParam);
            //Quita los separadores
            String rutper = rutParam;
            rutper = StringUtil.replaceString(rutper, ".", "");
            rutper = StringUtil.replaceString(rutper, ",", "");
            Integer rut = Integer.parseInt(RutUtil.extractRut(rutper));


            //Busca deudas
            pagoConsolidado = findDeudaCliente(rut);
            List hash = new ArrayList(pagoConsolidado.getHashcuotas().values());
            pagoConsolidado.setHashList(hash);
            pagoOut.setPagoPublico(pagoConsolidado);
            if (pagoConsolidado.getDatosCliente() != null && pagoConsolidado.getHashcuotas() != null &&
                pagoConsolidado.getHashcuotas().size() > 0) {
                try {

                    pagoConsolidado.setPdfConsolidado(LinkPDF.getHtmlPDFs(pagoConsolidado));
                    pagoConsolidado.setPdfPolizas(LinkPDF.obtieneDataHashTable(pagoConsolidado));
                    pagoConsolidado.setMensajeRespuesta("OK");
                    pagoConsolidado.setCodRespuesta(0);
                    pagoConsolidado.setEstado(Boolean.TRUE);
                    pagoConsolidado.setMethodSend("POST");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return pagoOut;
        } catch (Exception e) {
            pagoConsolidado.getHashcuotasl().clear();
            pagoConsolidado.getHashList().clear();
            pagoConsolidado.getHashcuotas().clear();
            pagoConsolidado.setEstado(Boolean.FALSE);
            pagoConsolidado.setMensajeRespuesta(e.getMessage());
            pagoConsolidado.setCodRespuesta(01); //error general
            pagoOut.setPagoPublico(pagoConsolidado);
            return pagoOut;
        }

    }

    @Path("/confirmacionProductos")
    @POST
    public PagoPublicoOut confirmacionProductos(PagoPublicoOut pagoPublicoOut) {
        //public PagoPublicoVO confirmacionProductos(PagoPublicoVO pagoPublico){
        PagoPublicoVO pago = new PagoPublicoVO();
        PagoPublicoOut pagoOut = new PagoPublicoOut();
        try {
            pago = confirmacionProducto(pagoPublicoOut.getPagoPublico());
            pago.setMethodSend("POST");
            pagoOut.setPagoPublico(pago);
            return pagoOut;
        } catch (Exception e) {
            pago.getHashcuotasl().clear();
            pago.getHashList().clear();
            pago.getHashcuotas().clear();
            pago.setMensajeRespuesta("Ha ocurrido el siguiente error: " + e.getMessage());
            pago.setCodRespuesta(1);
            pago.setEstado(false);
            pagoOut.setPagoPublico(pago);
            return pagoOut;
        }

    }


    @Path("/confirmacionMedioPago")
    @POST
    public PagoPublicoOut confirmacionMedioPago(PagoPublicoOut pagoPublicoOut) {
        //public PagoPublicoVO confirmacionMedioPago(PagoPublicoVO pagoPublico){
        PagoPublicoVO pago = new PagoPublicoVO();
        PagoPublicoOut pagoOut = new PagoPublicoOut();
        try {

            pago = doConfirmacionPago(pagoPublicoOut.getPagoPublico());
            pago.setMethodSend("POST");
            pagoOut.setPagoPublico(pago);
            return pagoOut;
        } catch (Exception e) {
            pago.getHashcuotasl().clear();
            pago.getHashList().clear();
            pago.getHashcuotas().clear();
            pago.setMensajeRespuesta("Ha ocurrido el siguiente error: " + e.getMessage());
            pago.setCodRespuesta(1);
            pago.setEstado(false);
            pagoOut.setPagoPublico(pago);
            return pagoOut;
        }
        //return pago;
    }

    @Path("/verificaComprobante")
    @POST
    public PagoPublicoOut verificaTrx(PagoPublicoOut pagoPublicoOut) {
        //public PagoPublicoVO verificaTrx(PagoPublicoVO pagoPublico){
        PagoPublicoVO pago = new PagoPublicoVO();
        PagoPublicoOut pagoOut = new PagoPublicoOut();
        try {
            pago = doVerificar(pagoPublicoOut.getPagoPublico());
            pago.setMethodSend("POST");
            pagoOut.setPagoPublico(pago);
            return pagoOut;
        } catch (Exception e) {
            pago.getHashcuotasl().clear();
            pago.getHashList().clear();
            pago.getHashcuotas().clear();
            pago.setMensajeRespuesta("Ha ocurrido el siguiente error: " + e.getMessage());
            pago.setCodRespuesta(1);
            pago.setEstado(false);
            pagoOut.setPagoPublico(pago);
            return pagoOut;
        }
        //return pago;
    }


    @Path("/enviarEmail")
    @POST
    public OutMensaje enviarEmail(InCorreo inCorreo) {
        OutMensaje salida = new OutMensaje();
        PagoPublicoVO pago = new PagoPublicoVO();
        pago.setHashcuotas(new Hashtable());
        pago.setHashList(inCorreo.getHashList());
        
        try {
            PagoPublicoCore.llenaLinked(pago);
            salida =
                /*doEnviarPorEmail(inCorreo.getHashcuotas(), inCorreo.getUrlPdfDownload(), inCorreo.getNombreCliente(),
                                 inCorreo.getFechaTransaccion(), inCorreo.getEmailCustom());*/
                doEnviarPorEmail(pago.getHashcuotasl(), inCorreo.getUrlPdfDownload(), inCorreo.getNombreCliente(),
                                 inCorreo.getFechaTransaccion(), inCorreo.getEmailCustom());
            return salida;
        } catch (Exception e) {
            salida.setMensaje("Ha ocurrido un error: " + e.getMessage());
            salida.setCodigo("01");
            return salida;
        }

    }


    /**
     * Busca deuda de clientes por rut
     * */
    private PagoPublicoVO findDeudaCliente(Integer rut) throws Exception {
        logger.info("findDeudaCliente(Integer rut=" + rut + ") - iniciando");

        String xmlRespuesta;
        PagoPublicoVO pagoConsolidado = new PagoPublicoVO();

        try {
            xmlRespuesta = consultaDeudas(rut);
            logger.info("xmlrespuesta: " + xmlRespuesta);
            if (xmlRespuesta != null) {
                if (xmlRespuesta.startsWith("NOK")) {
                    String mensajerror = xmlRespuesta;
                    if (xmlRespuesta.startsWith("NOK"))
                        mensajerror = xmlRespuesta.substring(4);
                    pagoConsolidado.setMensajeRespuesta(mensajerror);
                    pagoConsolidado.setEstado(false);
                    pagoConsolidado.setCodRespuesta(01); //codigo error general
                    return pagoConsolidado;

                } else {
                    //Consulta informacion de usuario deudas y datos basicos
                    pagoConsolidado = PagoPublicoCore.getXMLParaPagosPendientes(xmlRespuesta);
                    pagoConsolidado.setMensajeRespuesta("OK");
                    pagoConsolidado.setCodRespuesta(0);
                    pagoConsolidado.setEstado(Boolean.TRUE);
                    pagoConsolidado.setMethodSend("POST");
                    pagoConsolidado.setFechaAccesoUsuario(StringUtil.toTitleCase(FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()),
                                                                                                                  "dd' de 'MMMM' del 'yyyy HH:mm"),
                                                                                 " ") + " horas");
                    pagoConsolidado.setTipoTransaccion(TPOTRX_NORMAL);
                }
            } else {
                pagoConsolidado.setEstado(Boolean.FALSE);
                pagoConsolidado.setMensajeRespuesta(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.ejb.nodata"));
                pagoConsolidado.setCodRespuesta(01); //error general
                logger.info("xmlrespuesta: null");

                return pagoConsolidado;
            }
        } catch (PagoPublicoCoreException ec) {
            String msg = "";
            if (ec.getCause() != null) {
                msg = ec.getCause().getMessage();
            } else {
                msg = ec.getMessage();
            }
            throw new Exception(msg);

        } catch (ConsultaDeudaException ed) {
            String msg = "";
            if (ed.getCause() != null) {
                msg = ed.getCause().getMessage();
            } else {
                msg = ed.getMessage();
            }
            throw new Exception(msg);

        }
        logger.info("findDeudaCliente(String rut=" + rut + ") - termina");
        return pagoConsolidado;
    }

    /**
     * Consulta las deudas
     * @param rut
     * @return
     */
    private String consultaDeudas(Integer rut) throws ConsultaDeudaException {
        logger.info("consultaDeudas(Integer rut=" + rut + ") - iniciando");
        String xml = null;
        //PersonaDto persona = null;
        String nombreusuario = null;
        try {

            PersonaDto persona = DAOFactory.getConsultasDao().findPersonaByRut(rut);
            if (persona != null) {
                nombreusuario = persona.getNombre();
            }

            logger.info("nombreusuario: " + nombreusuario);
            //Consulta la informacion de deudas
            xml =
                DAOFactory.getLogicaResumenPagosDao().crearResumenPagosPolizas(rut, nombreusuario, null,
                                                                               CANAL_BOTON_PAGO_PUBLICO); // numero 2 corresponde a prima
            logger.info("xml Servicios" + xml);
            logger.info("consultaDeudas(Integer rut=" + rut + ") - termina");
        } catch (ServiceLocatorException e) {
            logger.error(e.getMessage());
            throw new ConsultaDeudaException(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.ejb"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ConsultaDeudaException(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.logica"));
        }
        logger.info("xml " + xml);
        return xml;
    }

    /**
     * confirma la seleccion de los productos
     * */
    private PagoPublicoVO confirmacionProducto(PagoPublicoVO pagoPublico) throws Exception {

        logger.info("confirmacionPago(int mediopago=" + pagoPublico.getMedioPago() + ") - iniciando");
        pagoPublico.setMensajeRespuesta("OK");
        pagoPublico.setCodRespuesta(0);
        pagoPublico.setEstado(Boolean.TRUE);

        if (pagoPublico.getIdNavegacion() == 0) {
            throw new Exception("ERROR: No existe ID navegacion");
        } else if (pagoPublico.getXmloriginal() == null || "".equals(pagoPublico.getXmloriginal())) {
            throw new Exception("ERROR: xmlOriginal vacio");
        } else if (pagoPublico.getMedioPago() == 0) {
            throw new Exception("ERROR: Debe seleccionar medio de pago");
        } else if (pagoPublico.getHashList().size() < 1) {
            throw new Exception("ERROR: Lista de polizas viene vacia");
        }

        logger.info("confirmacionPago() - NUMERO NAVEGACION BICE:" + pagoPublico.getIdTransaccion());
        try {
            Map paramvalore = null;
            pagoPublico = PagoPublicoCore.llenaLinked(pagoPublico);

            ResumenRequest req =
                PagoPublicoCore.getRequerimientoConfirmaPagoSeleccionado(pagoPublico, pagoPublico.getMedioPago(),
                                                                         paramvalore);
            pagoPublico.setIdBancoPago(pagoPublico.getMedioPago());
            if (req != null) {
                if (req.getOpciones().length < 11) {
                    Boolean dummyejb =
                        new Boolean(ResourcePropertiesUtil.getProperty("bice.offline.website.dummy.ejb.enable"));
                    String xmlResumenPagoProductosSeleccionados = null;
                    if (dummyejb.booleanValue() == false) {
                        logger.info("confirmacionPago() - NUMERO BANCO SELECCIONADO " + req.getBanco());
                        //xmlResumenPagoProductosSeleccionados = ejb.generaXmlResumenPagosConProductosSeleccionados(pagoPublico.getXmloriginal(), req, ejb.CANAL_BOTON_PAGO_PUBLICO);
                        pagoPublico.getXmloriginal().replaceAll("\r\n", "");
                        pagoPublico.getXmloriginal().replaceAll("\"\"", "");
                        pagoPublico.getXmloriginal().replaceAll("\n", "");
                        xmlResumenPagoProductosSeleccionados =
                            DAOFactory.getLogicaConfirmacionDao().generaXmlResumenPagosConProductosSeleccionados(pagoPublico.getXmloriginal(),
                                                                                                                 req,
                                                                                                                 CANAL_BOTON_PAGO_PUBLICO);
                    } else {
                        xmlResumenPagoProductosSeleccionados = pagoPublico.getXmloriginal();
                    }

                    //Valida OK el XML
                    if (xmlResumenPagoProductosSeleccionados != null &&
                        !xmlResumenPagoProductosSeleccionados.startsWith("NOK")) {
                        //Graba XML en session serializable
                        pagoPublico.setXmlpagopublico(xmlResumenPagoProductosSeleccionados);
                        //pagoPublico.setXmlconfirmacion(xmlConfirmacion);

                        //Recupera el XML segun lo seleccionado mas las data del banco
                        String xmlConfDatabanco = null;
                        if (dummyejb.booleanValue() ==
                            false) {
                            //xmlConfDatabanco = ejb.crearConfirmacionConXmlSeleccionado(xmlResumenPagoProductosSeleccionados, ejb.CANAL_BOTON_PAGO_PUBLICO, Constantes.TPOTRX_NORMAL, pagoPublico.getDatosCliente().getEmail());
                            xmlConfDatabanco =
     DAOFactory.getLogicaConfirmacionDao().crearConfirmacionConXmlSeleccionado("B",
                                                                               xmlResumenPagoProductosSeleccionados,
                                                                               CANAL_BOTON_PAGO_PUBLICO, TPOTRX_NORMAL,
                                                                               pagoPublico.getDatosCliente().getEmail());
                        } else {
                            xmlConfDatabanco = xmlResumenPagoProductosSeleccionados;
                        }


                        if (xmlConfDatabanco != null && !xmlConfDatabanco.startsWith("NOK")) {

                            HttpSession session = request.getSession();
                            logger.info("Session ID: " + session.getId());
                            //Setea y asocia datas de pago
                            pagoPublico.setXmlconfirmacion(xmlConfDatabanco);
                            pagoPublico.setXmlconfirmacionDataBancos(xmlConfDatabanco);
                            String xmlBanco = PagoPublicoCore.getXmlBancoRedireccionamiento(xmlConfDatabanco);
                            PagoBancoInfoVo info = new PagoBancoInfoVo();
                            //info.setHtmlHiddenPago(PagoPublicoCore.getBancoHtmlHiddenParams(pagoPublico.getMedioPago(), xmlConfDatabanco, xmlBanco));
                            info.setHtmlHiddenPago("");
                            info.setNewHiddenParam(PagoPublicoCore.getBancoHtmlHiddenParamsNew(pagoPublico,
                                                                                               xmlConfDatabanco,
                                                                                               xmlBanco,
                                                                                               session.getId()));
                            info.setBancoNombre(PagoPublicoCore.getBancoNombre(pagoPublico.getMedioPago()));
                            info.setXmlUrlRedirect(PagoPublicoCore.getUrlBancoRedireccionamiento(xmlConfDatabanco));
                            info.setIdTransaccionBanco(PagoPublicoCore.getBancoIdComprobante(pagoPublico.getMedioPago(),
                                                                                             xmlBanco));

                            pagoPublico.setBanco(info);
                            pagoPublico.setFormaPago(info.getBancoNombre());

                            pagoPublico =
                                PagoPublicoCore.setDatosSeleccionadosXml(pagoPublico, pagoPublico.getXmlpagopublico());

                            /*pagoPublico.setUrlcgi(pagoPublico.getBanco().getXmlUrlRedirect());
                            //htmldata --> corresponde a newHiddenParam
                            pagoPublico.setBancoId(pagoPublico.getIdBancoPago());


                            String medio = PagoPublicoCore.getValidaNodePathMedioPago(pagoPublico);
                            if (Integer.parseInt(medio) != pagoPublico.getMedioPago()) {
                                throw new Exception("Confirmaci\u00f3n de medio de pago no corresponde");
                            } 

                            String idcomprobante = pagoPublico.getBanco().getIdTransaccionBanco();
                            logger.info("doConfirmacionPago() - numero de transaccion : " + idcomprobante);
                            // mbean.setNumeroTransaccion(idcomprobante);

                            DAOFactory.getPersistenciaGeneralDao().updateEstadoTransaccion(new Integer(idcomprobante),
                                                                                           new Integer(2));
                            //update de estado en apv_ahorro tabla ahorro_intereses
                            this.updateEstadoPolizasAPV(pagoPublico.getDatosCliente().getRutCliente(),
                                                        pagoPublico.getHashcuotasl());*/


                        } else {
                            String mensajerror = xmlConfDatabanco;
                            if (xmlConfDatabanco.startsWith("NOK"))
                                mensajerror = xmlConfDatabanco.substring(4);

                            pagoPublico.setMensajeRespuesta(mensajerror);
                            pagoPublico.setCodRespuesta(1);
                            pagoPublico.setEstado(Boolean.FALSE);
                            return pagoPublico;

                        }
                    } else {
                        String mensajerror = xmlResumenPagoProductosSeleccionados;
                        if (xmlResumenPagoProductosSeleccionados.startsWith("NOK"))
                            mensajerror = xmlResumenPagoProductosSeleccionados.substring(4);
                        throw new Exception(mensajerror);
                        /*pagoPublico.setMensajeRespuesta(mensajerror);
                        pagoPublico.setCodRespuesta(1);
                        pagoPublico.setEstado(Boolean.FALSE);
                        return pagoPublico;*/
                    }
                } else {
                    pagoPublico = PagoPublicoCore.setDatosSeleccionadosXml(pagoPublico, pagoPublico.getXmloriginal());
                    pagoPublico.setTotalPagar(0.0);
                    pagoPublico.setMensajeRespuesta(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.seleccioneproducto.10"));
                    pagoPublico.setCodRespuesta(1);
                    pagoPublico.setEstado(Boolean.FALSE);
                    return pagoPublico;
                }
            } else {
                pagoPublico = PagoPublicoCore.setDatosSeleccionadosXml(pagoPublico, pagoPublico.getXmloriginal());
                pagoPublico.setTotalPagar(0.0);
                pagoPublico.setMensajeRespuesta(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.seleccioneproducto"));
                pagoPublico.setCodRespuesta(1);
                pagoPublico.setEstado(Boolean.FALSE);
                return pagoPublico;
            }
        } catch (ServiceLocatorException e) {
            throw new Exception(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.ejb"));

        } catch (Exception e) {
            throw new Exception(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.logica"));
        }
        return pagoPublico;
    }

    /**
     * Confirma el medio de pago
     * */
    private PagoPublicoVO doConfirmacionPago(PagoPublicoVO pagoPublico) throws Exception {
        logger.info("doConfirmacion() - iniciando");
        pagoPublico.setMensajeRespuesta("OK");
        pagoPublico.setCodRespuesta(0);
        pagoPublico.setEstado(Boolean.TRUE);
        
        if (pagoPublico.getAccion() != null && "volver".equals(pagoPublico.getAccion())) {
            //VUELVE A BUSCAR
            pagoPublico.setXmloriginal(null);
            pagoPublico.setXmlconfirmacion(null);
            pagoPublico.setXmlconfirmacionDataBancos(null);
            pagoPublico.setXmlpagopublico(null);
            pagoPublico = findDeudaCliente(Integer.valueOf(pagoPublico.getDatosCliente().getRutCliente()));
            return pagoPublico;
        }
        if (pagoPublico.getIdNavegacion() == 0) {

            throw new Exception("ERROR: No existe ID navegacion");

        } else if (pagoPublico.getXmloriginal() == null || "".equals(pagoPublico.getXmloriginal())) {

            throw new Exception("ERROR: xmlOriginal vacio");
        }
        pagoPublico = PagoPublicoCore.llenaLinked(pagoPublico);
        pagoPublico.setUrlcgi(pagoPublico.getBanco().getXmlUrlRedirect());
        //htmldata --> corresponde a newHiddenParam
        pagoPublico.setBancoId(pagoPublico.getIdBancoPago());


        try {
            String medio = PagoPublicoCore.getValidaNodePathMedioPago(pagoPublico);
            if (Integer.parseInt(medio) != pagoPublico.getMedioPago()) {
                throw new Exception("Confirmaci\u00f3n de medio de pago no corresponde");
            } else if (!PagoPublicoCore.getValidaProductoSeleccionado(pagoPublico)) {
                throw new Exception("Los productos seleccionado, verifique que sean los productos a pagar");
            }

            String idcomprobante = pagoPublico.getBanco().getIdTransaccionBanco();
            logger.info("doConfirmacionPago() - numero de transaccion : " + idcomprobante);
            // mbean.setNumeroTransaccion(idcomprobante);

            DAOFactory.getPersistenciaGeneralDao().updateEstadoTransaccion(new Integer(idcomprobante), new Integer(2));
            //update de estado en apv_ahorro tabla ahorro_intereses
            //this.updateEstadoPolizasAPV(pagoPublico.getDatosCliente().getRutCliente(), pagoPublico.getHashcuotas());
            this.updateEstadoPolizasAPV(pagoPublico.getDatosCliente().getRutCliente(), pagoPublico.getHashcuotasl());
        } catch (ServiceLocatorException e) {
            if (e.getMessage() != null && !"".equals(e.getMessage())) {
                throw new Exception(e.getMessage());
            } else
                throw new Exception(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.ejb"));
        } catch (Exception e) {
            if (e.getMessage() != null && !"".equals(e.getMessage())) {
                throw new Exception(e.getMessage());
            } else
                throw new Exception(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.logica"));


        }

        logger.info("doConfirmacion() - termina");
        return pagoPublico;
    }


    //private boolean updateEstadoPolizasAPV(int rut, Hashtable hashcuotas) {
    private boolean updateEstadoPolizasAPV(int rut, LinkedHashMap hashcuotas) {
        int numcuotas = hashcuotas.size();
        boolean ret = false;
        for (int counter = 1; counter <= numcuotas; counter++) {
            PagoDetalleCuotaVO vCuota = (PagoDetalleCuotaVO) hashcuotas.get(counter);
            if (vCuota != null && vCuota.getNumeroProducto() != 0) {
                if (vCuota.isSelecionado()) {
                    if (vCuota.getCodigoProducto() == 1060 || vCuota.getCodigoProducto() == 1063 ||
                        vCuota.getCodigoProducto() ==
                        1064) { //producto APV
                        DAOFactory.getPersistenciaGeneralDao().updateEstadoPolizaAPV(new Long(rut),
                                                                                     new Long(vCuota.getNumeroProducto()),
                                                                                     new Long(vCuota.getNumeroRamo()),
                                                                                     new Long(vCuota.getFolio_recibo()),
                                                                                     new Long(vCuota.getSecuencia()));
                        ret = true;
                    }
                }
            }
        }

        return ret;
    }


    /**
     * Verifica que la transaccion este pagada
     * **/
    private PagoPublicoVO doVerificar(PagoPublicoVO pagoPublico) throws Exception {
        logger.info("doVerificar() - iniciando");
        logger.info("doVerificar() - consultando si la transacion esta pagada");
        pagoPublico.setMensajeRespuesta("OK");
        pagoPublico.setCodRespuesta(0);
        pagoPublico.setEstado(Boolean.TRUE);
        try {
            if (pagoPublico.getAccion() != null && "volver".equals(pagoPublico.getAccion())) {
                //VUELVE A BUSCAR
                pagoPublico.setXmloriginal(null);
                pagoPublico.setXmlconfirmacion(null);
                pagoPublico.setXmlconfirmacionDataBancos(null);
                pagoPublico.setXmlpagopublico(null);
                pagoPublico = findDeudaCliente(Integer.valueOf(pagoPublico.getDatosCliente().getRutCliente()));
                return pagoPublico;
            }
            if (pagoPublico.getIdNavegacion() == 0) {
                throw new Exception("ERROR: No existe ID navegacion");
            } else if (pagoPublico.getXmloriginal() == null || "".equals(pagoPublico.getXmloriginal())) {
                throw new Exception("ERROR: xmlOriginal vacio");
            } else if (pagoPublico.getBanco().getIdTransaccionBanco() == null ||
                       "".equals(pagoPublico.getBanco().getIdTransaccionBanco())) {
                throw new Exception("ERROR: Falta el id de Transaccion del banco");
            }
            pagoPublico = PagoPublicoCore.llenaLinked(pagoPublico);
            
            ResultadoConsultaVO resultado = verificaComprobantePagado(pagoPublico.getBanco().getIdTransaccionBanco());
            pagoPublico.setResultadoPagoBanco(resultado);

            if (resultado.getResultado() == 2) {

                logger.info("doVerificar() - comprobante encontrado y pagado - IDNavegacion[" +
                            pagoPublico.getIdNavegacion() + "] idtrxbanco[" +
                            pagoPublico.getBanco().getIdTransaccionBanco() + "] Monto Total[" +
                            pagoPublico.getTotalPagar() + "]");

                URL realPath = context.getResource("/");
                logger.info("realpath " + realPath);


                //GENERA EL PDF DE PAGO EXITOSO
                PagoRutasFilesVO voPag =
                    PagoPublicoCore.generarPDFPagoOK(pagoPublico, realPath.toString().replace("file:", ""));
                if (voPag != null) {
                    pagoPublico.setUrlPdfDownload(voPag.getPathRelativo());
                    pagoPublico.setAbsoluteServerPathPdfDownload(voPag.getPathAbsoluto());
                    if (voPag.getPathRelativo() != null && voPag.getPathAbsoluto() != null) {
                        autoSendEmail(pagoPublico);
                    }

                } else {
                    pagoPublico.setMensajeRespuesta(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.pdf.pago"));
                    pagoPublico.setCodRespuesta(1);
                    pagoPublico.setEstado(false);
                    return pagoPublico;
                }

            }

            logger.info("doVerificar() - resultado.getResultado() " + resultado.getResultado());

            if (resultado.getResultado() == 1) {

                if (pagoPublico.getIdBancoPago() == 4 || pagoPublico.getIdBancoPago() == 11) {
                    logger.info("doVerificar() - Rechazado por medio de pago WEBPAY: " + pagoPublico.getIdBancoPago());

                    BpiTraTransaccionesTbl trx =
                        DAOFactory.getConsultasDao().findTransaccionCodigoEstadoByNumTransaccion(new Long(pagoPublico.getBanco().getIdTransaccionBanco()));


                    if (trx.getIdEmpresa() == 1) {
                        pagoPublico.setCodRespuesta(1);
                        pagoPublico.setEstado(false);
                        pagoPublico.setMensajeRespuesta(ResourceMessageUtil.getProperty("jsf.pagopublico.mensajes.informativos.rechazo.webpay.pago") +
                                                        " " + resultado.getNumeroOrdenCompra() +
                                                        ResourceMessageUtil.getProperty("jsf.pagopublico.mensajes.informativos.rechazo.webpay.pago2"));

                    } else {
                        pagoPublico.setCodRespuesta(1);
                        pagoPublico.setEstado(false);
                        pagoPublico.setMensajeRespuesta(ResourceMessageUtil.getProperty("jsf.pagopublico.mensajes.informativos.rechazo.webpay.pago") +
                                                        " " + resultado.getNumeroOrdenCompra() +
                                                        ResourceMessageUtil.getProperty("jsf.pagopublico.mensajes.informativos.rechazo.webpay.pagobh"));
                    }

                } else {
                    logger.info("doVerificar() - Rechazado por medio de pago: " + pagoPublico.getIdBancoPago());
                    pagoPublico.setMensajeRespuesta(ResourceMessageUtil.getProperty("jsf.pagopublico.mensajes.informativos.rechazo.banco"));
                    pagoPublico.setCodRespuesta(1);
                    pagoPublico.setEstado(false);
                    return pagoPublico;
                }
                return pagoPublico;
            }
            if (resultado.getResultado() == 0) {
                logger.info("doVerificar() - no pagado aun...");
                pagoPublico.setMensajeRespuesta(ResourceMessageUtil.getProperty("jsf.pagopublico.mensajes.informativos.nopagadoahun"));
                pagoPublico.setCodRespuesta(1);
                pagoPublico.setEstado(false);
                return pagoPublico;
            }

            /* NO VA TIME OUT
            }else{
                //logger.info("doRefrescar() - Timeout es de: " + time);
                this.renderpoll = false;
                this.message =  this.messageTimeout();
            }*/


        } catch (ConsultaComprobanteException e) {
            logger.error("doRefrescar() - Ha ucurrido un error: ", e);
            String msg = "";
            if (e.getCause() != null) {
                msg = e.getCause().getMessage();
            } else {
                msg = e.getMessage();
            }
            throw new Exception(msg);

        } catch (MalformedURLException e) {
            throw new Exception(e.getMessage());

        }
        logger.info("doRefrescar() - termina");
        return pagoPublico;
    }


    /**
     * Verifica el estado del comprobande de pago
     * **/
    private ResultadoConsultaVO verificaComprobantePagado(String idTransaccion) throws ConsultaComprobanteException {

        ResultadoConsultaVO resultado = new ResultadoConsultaVO();
        resultado.setResultado(0);
        logger.info("verificaComprobantePagado() - inicia");
        try {
            BpiTraTransaccionesTbl trx =
                DAOFactory.getConsultasDao().findTransaccionCodigoEstadoByNumTransaccion(new Long(idTransaccion));
            if (trx != null) {
                switch (trx.getCodEstado().intValue()) {
                case 2: // '\002'
                    resultado.setResultado(0);
                    resultado.setMensajeResultado("Aun no se realiza el pago a traves del banco...");
                    break;

                case 99: // 'c'
                    resultado.setResultado(1);
                    resultado.setMensajeResultado(trx.getObservaciones());
                    //this.setNumeroOrdenCompra(trx.getMedioOrdenCompra() );
                    break;

                default:
                    resultado.setNumeroCuotasBanco(trx.getMedioNumeroCuotas() != null ?
                                                   Integer.toString(trx.getMedioNumeroCuotas().intValue()) : "-");
                    resultado.setNumeroTransaccionBanco(trx.getNumeroTransaccionBanco());
                    resultado.setNumeroOrdenCompra(trx.getMedioOrdenCompra());
                    resultado.setNumeroTarjeta(trx.getMedioNumeroTarjeta());
                    resultado.setCodigoAutorizacion(trx.getMedioCodigoAutorizacion());
                    resultado.setMedioCodigoRespuesta(trx.getMedioCodigoRespuesta());
                    if (trx.getMedioFechaPago() != null) {
                        resultado.setFechaBanco(FechaUtil.getFechaFormateoCustom(trx.getMedioFechaPago(),
                                                                                 "dd/MM/yyyy"));
                        resultado.setHoraBanco(FechaUtil.getFechaFormateoCustom(trx.getMedioFechaPago(), "HH:mm:ss"));
                    } else {
                        resultado.setFechaBanco(FechaUtil.getFechaFormateoCustom(trx.getFechafin(), "dd/MM/yyyy"));
                        resultado.setHoraBanco(FechaUtil.getFechaFormateoCustom(trx.getFechafin(), "HH:mm:ss"));
                    }
                    resultado.setMonedaPago("Pesos");
                    //resultado.setTipoCuotas("Sin cuotas");
                    resultado.setTipoCuotas("Venta debito");
                    if (trx.getMedioTipoTarjeta() != null) {
                        if (trx.getMedioTipoTarjeta().equalsIgnoreCase("VN"))
                            resultado.setTipoCuotas("Sin cuotas");
                        if (trx.getMedioTipoTarjeta().equalsIgnoreCase("VC"))
                            resultado.setTipoCuotas("Cuotas Normales");
                        if (trx.getMedioTipoTarjeta().equalsIgnoreCase("SI"))
                            resultado.setTipoCuotas("Sin Inter\351s");
                        if (trx.getMedioTipoTarjeta().equalsIgnoreCase("CI"))
                            resultado.setTipoCuotas("Cuotas Comercio");
                    }
                    BpiDnaDetnavegTbl xmlDTO =
                        DAOFactory.getPersistenciaGeneralDao().findComprobanteByTransaccion(new Long(idTransaccion));

                    if (xmlDTO != null) {
                        oracle.xml.parser.v2.XMLDocument xmlDoc = xmlDTO.getDetallePagina();
                        if (xmlDoc != null) {
                            resultado.setResultado(2);
                            resultado.setMensajeResultado("Pago realizado sin problemas");
                        }
                    }
                    break;
                }
                if (resultado.getResultado() == 2)
                    switch (trx.getCodMedioPago().intValue()) {
                    case 1: // '\001'
                        resultado.setCodigoAutorizacion("No aplica");
                        resultado.setNumeroCuotasBanco("No aplica");
                        resultado.setNumeroTarjeta("No aplica");
                        resultado.setTipoCuotas("No aplica");
                        break;

                    case 2: // '\002'
                        resultado.setCodigoAutorizacion("No aplica");
                        resultado.setNumeroCuotasBanco("No aplica");
                        resultado.setNumeroTarjeta("No aplica");
                        resultado.setTipoCuotas("No aplica");
                        break;

                    case 3: // '\003'
                        resultado.setCodigoAutorizacion("No aplica");
                        resultado.setNumeroCuotasBanco("No aplica");
                        resultado.setNumeroTarjeta("No aplica");
                        resultado.setTipoCuotas("No aplica");
                        break;
                    }
            }
        } catch (ServiceLocatorException e) {
            throw new ConsultaComprobanteException(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.ejb"));
        } catch (Exception e) {
            throw new ConsultaComprobanteException(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.logica"));
        }
        logger.info("verificaComprobantePagado() - termina");
        return resultado;
    }

    /**
     * Autoenvia Email una vez que finaliza el pago
     */
    private void autoSendEmail(PagoPublicoVO pagoPublico) {
        boolean isAutoSendMail = false;

        logger.info("autoSendEmail() - iniciando");
        if (isAutoSendMail == false) {
            logger.info("autoSendEmail() - verificando si hay auto envio de email");
            Boolean autoEnvioEnable =
                new Boolean(ResourcePropertiesUtil.getProperty("bice.webservices.client.email.autosend.fin.pago"));
            if (autoEnvioEnable.booleanValue()) {
                //String polizas = this.listPolizas(pagoPublico.getHashcuotas());
                String polizas = this.listPolizas(pagoPublico.getHashcuotasl());
                String urlAttach = (new StringBuilder()).append(pagoPublico.getUrlPdfDownload()).toString();
                String emailUser = pagoPublico.getDatosCliente().getEmail();
                logger.info("autoSendEmail() - Hay que auto enviar correo con el comprobante a :" + emailUser);
                logger.info("autoSendEmail() - El PDF adjutado en el correo es :" + urlAttach);
                if (emailUser != null) {
                    /*
                     * Verifica que el email sea valido
                     */
                    if (EmailUtil.isValidEmailAddress(emailUser)) {
                        logger.info("autoSendEmail() - Email valido, enviando correo...");
                        EmailUtil.enviarEmailWS(true, emailUser,
                                                ResourcePropertiesUtil.getProperty("bice.webservices.client.email.from"),
                                                ResourcePropertiesUtil.getProperty("bice.webservices.client.email.cc"),
                                                ResourcePropertiesUtil.getProperty("bice.webservices.client.email.cco"),
                                                ResourcePropertiesUtil.getProperty("bice.webservices.client.email.subject.pagado"),
                                                pagoPublico.getDatosCliente().getNombreCliente(),
                                                FechaUtil.getFechaFormateoStandar(pagoPublico.getFechaTransaccion()),
                                                polizas, urlAttach);
                        logger.info("autoSendEmail() - Email Enviado");
                    }
                }
            }
            //FINALIZA PARA QUE NO VUELVA A ENVIAR EN CASO DE REFRESCAR.
            isAutoSendMail = true;
        }
        logger.info("autoSendEmail() - terminado");
        return;
    }

    //private String listPolizas(Hashtable cuotas) {
    private String listPolizas(LinkedHashMap cuotas) {
        String polizas = "";
        int numcuotas = cuotas.size();

        ObjectMapper mapper = new ObjectMapper();
        for (int counter = 1; counter <= numcuotas; counter++) {
            //PagoDetalleCuotaVO vCuota = (PagoDetalleCuotaVO)cuotas.get(counter);
            PagoDetalleCuotaVO vCuota = mapper.convertValue(cuotas.get(counter), PagoDetalleCuotaVO.class);
            if (vCuota != null && vCuota.getNumeroProducto() != 0) {
                /*
                 * Busqueda del comprobante
                 */
                if (vCuota.isSelecionado()) {
                    polizas =
                        polizas + vCuota.getDescripcionProducto() + " - N&deg; " + vCuota.getNumeroProducto() + ", ";
                }
            }
        }

        if (polizas.length() > 0) {
            polizas = polizas.substring(0, polizas.length() - 2);
        }

        return polizas;
    }

    /* private OutMensaje doEnviarPorEmail(Hashtable cuotas, String urlPdf, String nombreCliente,
                                        java.util.Date fechaTransaccion, String emailCustom) {*/

    private OutMensaje doEnviarPorEmail(LinkedHashMap cuotas, String urlPdf, String nombreCliente,
                                        java.util.Date fechaTransaccion, String emailCustom) {
        logger.info("doEnviarEmailCustomEsperaPago() - iniciando");

        //String polizas = this.listPolizas(pagoPublico.getHashcuotas());
        String polizas = this.listPolizas(cuotas);
        //String urlAttach = (new StringBuilder()).append(pagoPublico.getUrlPdfDownload()).toString();
        String urlAttach = (new StringBuilder()).append(urlPdf).toString();
        OutMensaje salida = new OutMensaje();

        if (emailCustom != null) {
            if (EmailUtil.isValidEmailAddress(emailCustom)) {

                EmailUtil.enviarEmailWS(true, emailCustom,
                                        ResourcePropertiesUtil.getProperty("bice.webservices.client.email.from"),
                                        ResourcePropertiesUtil.getProperty("bice.webservices.client.email.cc"),
                                        ResourcePropertiesUtil.getProperty("bice.webservices.client.email.cco"),
                                        ResourcePropertiesUtil.getProperty("bice.webservices.client.email.subject.pagado"),
                                        //pagoPublico.getDatosCliente().getNombreCliente(),
                                        nombreCliente,
                    //FechaUtil.getFechaFormateoStandar(pagoPublico.getFechaTransaccion()),
                    FechaUtil.getFechaFormateoStandar(fechaTransaccion), polizas, urlAttach);

                salida.setMensaje(ResourceMessageUtil.getProperty("jsf.pagopublico.mensajes.informativos.emialenviado"));
                salida.setCodigo("00");
            } else {
                salida.setMensaje(ResourceMessageUtil.getProperty("jsf.pagopublico.mensajes.informativos.email.error.format"));
                salida.setCodigo("01");
            }
        } else {
            salida.setMensaje(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.noemail"));
            salida.setCodigo("01");
        }
        logger.info("doEnviarEmailCustomEsperaPago() - terminando");

        return salida;
    }

}
