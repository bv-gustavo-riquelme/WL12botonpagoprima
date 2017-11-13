package cl.bicevida.botonpago.core;

import cl.bice.vida.botonpago.api.CanastaBotonPago;
import cl.bice.vida.botonpago.common.dto.general.DetalleAPV;
import cl.bice.vida.botonpago.common.dto.general.ResumenRequest;
import cl.bice.vida.botonpago.common.util.StringUtil;
import cl.bice.vida.botonpago.modelo.dao.DAOFactory;

import cl.bicevida.botonpago.exception.PagoPublicoCoreException;
import cl.bice.vida.botonpago.modelo.dto.HomologacionConvenioDTO;
import cl.bice.vida.botonpago.modelo.dto.PdfComprobantePagoDTO;
import cl.bice.vida.botonpago.modelo.dto.ResponsePdfComprobanteVODTO;
import cl.bice.vida.botonpago.modelo.dto.PersonaDto;
import cl.bice.vida.botonpago.modelo.dto.RegimenTributarioDTO;

import cl.bice.vida.botonpago.modelo.dto.VWIndPrimaNormalVO;

import cl.bicevida.botonpago.pdf.GeneradorAcrobatPdf;
import cl.bice.vida.botonpago.servicios.apv.WSApvEJBClient;
import cl.bice.vida.botonpago.servicios.apv.types.InProducto;
import cl.bice.vida.botonpago.servicios.apv.types.ListOfProducto;
import cl.bice.vida.botonpago.servicios.apv.types.Producto;
import cl.bice.vida.botonpago.servicios.cliente.SessionEJBDatosClienteWebServiceService;
//import cl.bice.vida.botonpago.servicios.cliente.types.ResponsePdfComprobanteVODTO;
import cl.bice.vida.botonpago.servicios.cliente.types.ResponseVODTO;


import cl.bice.vida.botonpago.servicios.poliza.SessionPolizaEJBClient;
import cl.bice.vida.botonpago.servicios.poliza.types.IdPoliza;
import cl.bice.vida.botonpago.servicios.poliza.types.InListaProductos;
import cl.bice.vida.botonpago.servicios.poliza.types.ListOfPoliza;
import cl.bice.vida.botonpago.servicios.poliza.types.Login;
import cl.bice.vida.botonpago.servicios.poliza.types.Poliza;

import cl.bicevida.botonpago.pdf.XMLForPDF;
import cl.bicevida.botonpago.util.FacesUtil;
import cl.bicevida.botonpago.util.FechaUtil;
import cl.bicevida.botonpago.util.ResourceEscuchaUtil;
import cl.bicevida.botonpago.util.ResourceMessageUtil;
import cl.bicevida.botonpago.util.ResourcePropertiesUtil;
import cl.bicevida.botonpago.vo.DataBanco;
import cl.bicevida.botonpago.vo.prima.PagoDatosClienteVO;
import cl.bicevida.botonpago.vo.prima.PagoDetalleCuotaVO;
import cl.bicevida.botonpago.vo.prima.PagoPublicoVO;
import cl.bicevida.botonpago.vo.prima.PagoRutasFilesVO;


import cl.bicevida.botonpago.ws.cuentainversion.CuentaDeInversionWebService;
import cl.bicevida.botonpago.ws.cuentainversion.CuentaDeInversionWebService_Service;

import cl.bicevida.botonpago.ws.cuentainversion.types.ObtenerInformacionRegimen;
import cl.bicevida.botonpago.ws.cuentainversion.types.ObtenerInformacionRegimenResponse;

import cl.bicevida.botonpago.ws.cuentainversion.types.RetornoInformacionRegimen;

import com.sun.xml.ws.client.BindingProviderProperties;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import javax.ws.rs.core.Context;

import javax.xml.ws.BindingProvider;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLParseException;
import oracle.xml.parser.v2.XSLException;


import org.apache.batik.dom.util.HashTable;
import org.apache.log4j.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;


public class PagoPublicoCore {
    /**
     * Logger for this class
     */
    private static final Logger logger = 
        Logger.getLogger(PagoPublicoCore.class);


    /**
     * Recupera el nombre de parametro
     * para acceder a la data del banco
     * @param idBanco
     * @return
     */
    public static DataBanco[] getBancoHtmlHiddenParamsNew(
                                                  PagoPublicoVO pago,
                                                  String xmlConfirmacion, 
                                                  String xmlBanco,
                                                  String sessionID) {
        String hidden ="";
        String dataBanco = getXmlBancoRedireccionamiento(xmlConfirmacion);
        DataBanco[] data = new DataBanco[6];
        if (pago.getMedioPago() == 1) {//idBanco
            DataBanco d = new DataBanco(); 
            //hidden = "<input type=\"hidden\" name=\"data\" value='" + dataBanco + "'/>";

            d.setValue("'"+dataBanco+"'");
            d.setName("data");
            d.setType("hidden");
            d.setActionServlet( ResourcePropertiesUtil.getProperty("bice.contexto.banco.chile"));
            data[0] = d;
            //hidden[1] = "data";
           
        }
        if (pago.getMedioPago() == 2) {//idBanco
            //hidden = "<input type=\"hidden\" name=\"TX\" value=\"" + dataBanco + "\"/>";
            DataBanco d = new DataBanco(); 
            d.setValue(dataBanco);
            d.setName("TX");
            d.setType("hidden");
            data[0] = d;
        }
        if (pago.getMedioPago() == 3 || pago.getMedioPago() == 16) {//idBanco) {
            //hidden = "<input type=\"hidden\" name=\"xml\" value='" + dataBanco + "'/>";
            DataBanco d = new DataBanco(); 
            d.setValue("'"+dataBanco+"'");
            d.setName("xml");
            d.setType("hidden");
            data[0] = d;
        }
        if (pago.getMedioPago() == 4) {//idBanco
            DOMParser parser = new DOMParser();
            try {
                parser.parse(new StringReader(xmlBanco));
                XMLDocument doc = parser.getDocument();
                String idcom = getNodeValueByXPath(doc, "/WebPay/IdCom");
                String monto = getNodeValueByXPath(doc, "/WebPay/Monto");
                String intrx = getNodeValueByXPath(doc, "/WebPay/IdTrx");
                String numpr = getNodeValueByXPath(doc, "/WebPay/NumProductos");
                //String urlex = FacesUtil.getServerURL() + FacesUtil.getContextPath() + "/jsp/closewindow.jsp";
                //String urlfr = FacesUtil.getServerURL() + FacesUtil.getContextPath() + "/jsp/closewindow.jsp";
                String tipo = "TR_NORMAL"; //OJO No quitar 00 ya que son los Decimales
                   
                
                for(int i=0; i<= data.length;i++){
                    DataBanco d = new DataBanco();    
                    
                    if(i==0){
                        if(sessionID.length()>=60){
                            d.setValue(sessionID.substring(0,60));
                        }else{
                            d.setValue(sessionID);
                        }
                        d.setName("TBK_ID_SESION");
                        d.setType("hidden");
                        data[0] = d;
                    }else if(i == 1){
                        d.setValue(monto + "00");
                        d.setName("TBK_MONTO");
                        d.setType("hidden");
                        data[1] = d;
                    }else if(i == 2){
                        d.setValue(tipo);
                        d.setName("TBK_TIPO_TRANSACCION");
                        d.setType("hidden");
                        data[2] = d;
                    }else if(i == 3){
                        d.setValue(StringUtil.rellenarTextoIzquierda(intrx, 8, "0") +  StringUtil.rellenarTextoIzquierda(numpr, 3, "0"));
                        d.setName("TBK_ORDEN_COMPRA");
                        d.setType("hidden");    
                        data[3] = d;
                    }else if(i == 4){
                        d.setValue(pago.getTbkURLExitoError());
                        d.setName("TBK_URL_EXITO");
                        d.setType("hidden"); 
                        data[4] = d;
                    }else if(i == 5){
                        d.setValue(pago.getTbkURLExitoError());
                        d.setName("TBK_URL_FRACASO");
                        d.setType("hidden");
                        data[5] = d;
                    }
                }   
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pago.getMedioPago() == 8) {
            //String contexto = FacesUtil.getContextPath();
            String contexto = ResourcePropertiesUtil.getProperty("bice.contextpath");
            
            dataBanco =  StringUtil.replaceString(dataBanco, "$PATH$", ResourceEscuchaUtil.getProperty("bice.mediopago.cgi.bice.bancoestado.contexto.url.pago") +   contexto);
            //hidden = "<input type=\"hidden\" name=\"ent\" value=\"" + dataBanco + "\"/>";
            DataBanco d = new DataBanco();
            d.setValue(dataBanco);
            d.setName("ent");
            d.setType("hidden");
            data[0] = d;
        }
        
        //BANCO BCI 
         if (pago.getMedioPago() == 9) {//idBanco
                 //hidden =  "";
             DataBanco d = new DataBanco();
             d.setValue("");
             d.setName("");
             d.setType("");
             data[0] = d;
         }
         
         //BANCO TBANC
         if (pago.getMedioPago() == 10) {//idBanco
                 //hidden =  "";
             DataBanco d = new DataBanco();
             d.setValue("");
             d.setName("");
             d.setType("");
             data[0] = d;
         }        
         
        //BANCO BICE
        if (pago.getMedioPago() == 12) {
            DOMParser parser = new DOMParser();
            try {
                // CREA CANASTA
                String privateKeyPath = PagoPublicoCore.class.getClassLoader().getResource("/").getPath();

                privateKeyPath = privateKeyPath.replace("classes/", "");
                privateKeyPath += "config/cliente-privado.der";

                logger.info("privateKeyPath -> " + privateKeyPath);

                CanastaBotonPago canasta = new CanastaBotonPago(privateKeyPath); //BotonPagoFactory.crearCanasta();
                  
                //PROCESA XML 
                parser.parse(new StringReader(dataBanco));
                XMLDocument doc = parser.getDocument();
                String idComercio = getNodeValueByXPath(doc, "/BancoBICE/IdConvenio"); 
                String idTransaccion = getNodeValueByXPath(doc, "/BancoBICE/IdTransaccion"); 
                String montoTotal = getNodeValueByXPath(doc, "/BancoBICE/MontoTotal"); 
                canasta.setIdCliente(idComercio);
                canasta.setIdTransaccion(idTransaccion);// 
                canasta.setFechaVencimiento(new java.util.Date());
                canasta.setMontoTotal(Double.parseDouble(montoTotal));
                
                NodeList encontrado = doc.selectNodes("/BancoBICE/detalle/item"); 
                for (int x = 0; x < encontrado.getLength(); x++) {
                    Element elemento = (Element) encontrado.item(x);
                    NodeList objNodes = elemento.getChildNodes();
                    String cantidadItm = "";
                    String descripcItm = "";
                    String montototItm = "";
                    for (int y = 0; y < objNodes.getLength(); y++) {
                        String nombre = objNodes.item(y).getNodeName();
                        NodeList hijos = objNodes.item(y).getChildNodes();
                        String valor = hijos.item(0).getNodeValue();
                        if (nombre.equalsIgnoreCase("cantidad")) cantidadItm = valor;
                        if (nombre.equalsIgnoreCase("descripcion")) descripcItm = valor;
                        if (nombre.equalsIgnoreCase("monto")) montototItm = valor;
                    }
                    //AGREGA DETALLE DE PAGO
                    canasta.agregarLinea(Integer.parseInt(cantidadItm), descripcItm , new Double(montototItm)); 
                }
                
                String biceVidaXML = canasta.getXML();
                String biceVidaFirma = canasta.getControlFirma();
                
                /*hidden+= "<input style='display:none' name='_btn_bice_xml' value=\""+biceVidaXML+"\"/>" + 
                         "<textarea style='display:none' name='_btn_bice_firma'>"+biceVidaFirma+"</textarea>";*/
                
                for(int i=0; i<= data.length;i++){
                    DataBanco d = new DataBanco();    
                    if(i == 0){
                        d.setValue(biceVidaXML);
                        d.setName("_btn_bice_xml");
                        d.setType("text");
                        data[0]=d;
                    }else if(i == 1){
                        d.setValue(biceVidaFirma);
                        d.setName("_btn_bice_firma");
                        d.setType("textarea");        
                        data[1]= d;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("======================= HTTP PARAMETROS REDIRECCIONAMIENTO BANCOS ========================");
        System.out.println(dataBanco);
        System.out.println("======================= HTTP PARAMETROS REDIRECCIONAMIENTO BANCOS ========================");
        logger.info(dataBanco);
        logger.info("getBancoHtmlHiddenParams() - Http Post Banco Parametros:" + hidden);
        //return hidden;
        return data;
    }
    
    public static String getBancoHtmlHiddenParams(int idBanco, 
                                                  String xmlConfirmacion, 
                                                  String xmlBanco) {
        String hidden = "";
        String dataBanco = getXmlBancoRedireccionamiento(xmlConfirmacion);
        if (idBanco == 1) {
            hidden = "<input type=\"hidden\" name=\"data\" value='" + dataBanco + "'/>";
            /*
            try {
                hidden = "<input type=\"hidden\" name=\"data\" value=\"" + Base64.encode(dataBanco.getBytes("UTF8")) + "\"/>";
            } catch (UnsupportedEncodingException e) {
                logger.error("Error al codigicar en base64", e);
            }*/
        }
        if (idBanco == 2) {
            hidden = "<input type=\"hidden\" name=\"TX\" value=\"" + dataBanco + "\"/>";
        }
        if (idBanco == 3 || idBanco == 16) {
            hidden = "<input type=\"hidden\" name=\"xml\" value='" + dataBanco + "'/>";
        }
        if (idBanco == 4) {
            DOMParser parser = new DOMParser();
            try {
                parser.parse(new StringReader(xmlBanco));
                XMLDocument doc = parser.getDocument();
                String idcom = getNodeValueByXPath(doc, "/WebPay/IdCom");
                String monto = getNodeValueByXPath(doc, "/WebPay/Monto");
                String intrx = getNodeValueByXPath(doc, "/WebPay/IdTrx");
                String numpr = getNodeValueByXPath(doc, "/WebPay/NumProductos");
                String urlex = FacesUtil.getServerURL() + FacesUtil.getContextPath() + "/jsp/closewindow.jsp";
                String urlfr = FacesUtil.getServerURL() + FacesUtil.getContextPath() + "/jsp/closewindow.jsp";
                String tipo = "TR_NORMAL"; //OJO No quitar 00 ya que son los Decimales
                    hidden =   "<input type=\"hidden\" name=\"TBK_ID_SESION\" value=\"" + FacesUtil.getSession().getId().substring(0,60) + "\"/>" + 
                        "<input type=\"hidden\" name=\"TBK_MONTO\" value=\"" + monto + "00" + "\"/>" + 
                        "<input type=\"hidden\" name=\"TBK_TIPO_TRANSACCION\" value=\"" +  tipo + "\"/>" + 
                        "<input type=\"hidden\" name=\"TBK_ORDEN_COMPRA\" value=\"" +  StringUtil.rellenarTextoIzquierda(intrx, 8, "0") +  StringUtil.rellenarTextoIzquierda(numpr, 3, "0") +    "\"/>" + 
                        "<input type=\"hidden\" name=\"TBK_URL_EXITO\" value=\"" +  urlex + "\"/>" + 
                        "<input type=\"hidden\" name=\"TBK_URL_FRACASO\" value=\"" +   urlfr + "\"/>";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (idBanco == 8) {
            String contexto = FacesUtil.getContextPath();
            dataBanco =  StringUtil.replaceString(dataBanco, "$PATH$", ResourcePropertiesUtil.getProperty("bice.mediopago.cgi.bice.bancoestado.contexto.url.pago") +   contexto);
            hidden = "<input type=\"hidden\" name=\"ent\" value=\"" + dataBanco + "\"/>";
        }
        
        //BANCO BCI 
         if (idBanco == 9) {
                 hidden =  "";
         }
         
         //BANCO TBANC
         if (idBanco == 10) {
                 hidden =  "";
         }        
         
        //BANCO BICE
        if (idBanco == 12) {
            DOMParser parser = new DOMParser();
            try {
                // CREA CANASTA
                String privateKeyPath = PagoPublicoCore.class.getClassLoader().getResource("/").getPath();

                privateKeyPath = privateKeyPath.replace("classes/", "");
                privateKeyPath += "config/cliente-privado.der";

                logger.info("privateKeyPath -> " + privateKeyPath);

                CanastaBotonPago canasta = new CanastaBotonPago(privateKeyPath); //BotonPagoFactory.crearCanasta();
                  
                //PROCESA XML 
                parser.parse(new StringReader(dataBanco));
                XMLDocument doc = parser.getDocument();
                String idComercio = getNodeValueByXPath(doc, "/BancoBICE/IdConvenio"); 
                String idTransaccion = getNodeValueByXPath(doc, "/BancoBICE/IdTransaccion"); 
                String montoTotal = getNodeValueByXPath(doc, "/BancoBICE/MontoTotal"); 
                canasta.setIdCliente(idComercio);
                canasta.setIdTransaccion(idTransaccion);// 
                canasta.setFechaVencimiento(new java.util.Date());
                canasta.setMontoTotal(Double.parseDouble(montoTotal));
                
                NodeList encontrado = doc.selectNodes("/BancoBICE/detalle/item"); 
                for (int x = 0; x < encontrado.getLength(); x++) {
                    Element elemento = (Element) encontrado.item(x);
                    NodeList objNodes = elemento.getChildNodes();
                    String cantidadItm = "";
                    String descripcItm = "";
                    String montototItm = "";
                    for (int y = 0; y < objNodes.getLength(); y++) {
                        String nombre = objNodes.item(y).getNodeName();
                        NodeList hijos = objNodes.item(y).getChildNodes();
                        String valor = hijos.item(0).getNodeValue();
                        if (nombre.equalsIgnoreCase("cantidad")) cantidadItm = valor;
                        if (nombre.equalsIgnoreCase("descripcion")) descripcItm = valor;
                        if (nombre.equalsIgnoreCase("monto")) montototItm = valor;
                    }
                    //AGREGA DETALLE DE PAGO
                    canasta.agregarLinea(Integer.parseInt(cantidadItm), descripcItm , new Double(montototItm)); 
                }
                
                String biceVidaXML = canasta.getXML();
                String biceVidaFirma = canasta.getControlFirma();
                
                hidden+= "<input style='display:none' name='_btn_bice_xml' value=\""+biceVidaXML+"\"/>" + 
                         "<textarea style='display:none' name='_btn_bice_firma'>"+biceVidaFirma+"</textarea>";
                         
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("======================= HTTP PARAMETROS REDIRECCIONAMIENTO BANCOS ========================");
        System.out.println(dataBanco);
        System.out.println("======================= HTTP PARAMETROS REDIRECCIONAMIENTO BANCOS ========================");
        logger.info(dataBanco);
        logger.info("getBancoHtmlHiddenParams() - Http Post Banco Parametros:" + hidden);
        return hidden;
        
    }

    /**
     * Recupera el nombre de parametro
     * para acceder a la data del banco
     * @param idBanco
     * @return
     */
    public static String getBancoHttpParametro(int idBanco) {
        if (idBanco == 1)
            return "data";
        if (idBanco == 2)
            return "TX";
        if (idBanco == 3 || idBanco == 16)
            return "xml";
        if (idBanco == 4)
            return "TBK_ID_SESION"; //TBK_MONTO, TBK_TIPO_TRANSACCION, TBK_ORDEN_COMPRA, TBK_ID_SESION, TBK_URL_EXITO, TBK_URL_FRACASO
        if (idBanco == 8)
            return "ent";
        if (idBanco == 9)
            return "trx"; 
        if (idBanco == 10)
            return "trx"; 
        if (idBanco == 12)
            return "xml"; 
        return "";
    }

    /**
     * Recupera el nombre de parametro
     * para acceder a la data del banco
     * @param idBanco
     * @return
     */
    public static String getBancoNombre(int idBanco) {
        if (idBanco == 1)
            return "Banco Chile";
        if (idBanco == 2)
            return "Santander";
        if (idBanco == 3 || idBanco == 16)
            return "Servipag";
        if (idBanco == 4)
            return "WebPay";
        if (idBanco == 8)
            return "Banco Estado";
        if (idBanco == 9)
            return "Banco BCI";
        if (idBanco == 10)
            return "Banco TBanc";   
        if (idBanco == 12)
            return "Banco BICE";     
        return "";
    }

    /**
     * Recupera el Id del comprobante segun banco
     * @param idBanco que hizo la operacion
     * @return
     */
    public static String getBancoIdComprobante(int idBanco, String xml) {
        logger.info("getBancoIdComprobante(int idBanco=" + idBanco + 
                     ",xml) - iniciando");
        String idfull = "";
        try {
            //Inicializa el Documento XML en modalidad DOM (Arbol)
            DOMParser parser = new DOMParser();
            parser.parse(new StringReader(xml));
            XMLDocument doc = parser.getDocument();
            if (idBanco == 1)
                idfull = getNodeValueByXPath(doc, "/MPINI/IDTRX");
                //idfull = getNodeValueByXPath(doc, "/MPINISecure/MPINIComer/MPINI/IDTRX");
            if (idBanco == 2)
                idfull = getNodeValueByXPath(doc, "/MPINI/IDTRX");
            if (idBanco == 3 || idBanco == 16)
                idfull = getNodeValueByXPath(doc, "/Servipag/Header/IdTxPago");
            if (idBanco == 4)
                idfull = getNodeValueByXPath(doc, "/WebPay/IdTrx");
            if (idBanco == 8)
                idfull = getNodeValueByXPath(doc, "/INICIO/MULTIPAGO/ID_MPAGO");
            if (idBanco == 9)
                idfull = getNodeValueByXPath(doc, "/BCI/IdTrx");
            if (idBanco == 10)
                idfull = getNodeValueByXPath(doc, "/TBanc/IdTrx");
            if (idBanco == 12)
                idfull = getNodeValueByXPath(doc, "/BancoBICE/IdTransaccion");         

            //Recorta los items
            if (idBanco == 1) {
                //TODO: HOMOLOGACION SOLUCIONADO EL PROBLEMA DE IDTRANSACCION LARGO PARA BANCO CHILE Y SANTANDER
                //MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();
                if (idfull != null) {
                    HomologacionConvenioDTO dtoHomo = DAOFactory.getConsultasDao().getHomologacionConvenioByIdComercioTrx(idfull);
                        //ejb.getHomologacionConvenioByIdComercioTrx(idfull);
                    if (dtoHomo != null) {
                       idfull =  ""+dtoHomo.getIdTransaccion();
                    }
                }
            }
            if (idBanco == 2) {
                //TODO: HOMOLOGACION SOLUCIONADO EL PROBLEMA DE IDTRANSACCION LARGO PARA BANCO CHILE Y SANTANDER
                //MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();
                if (idfull != null) {
                    HomologacionConvenioDTO dtoHomo = DAOFactory.getConsultasDao().getHomologacionConvenioByIdComercioTrx(idfull);
                        //ejb.getHomologacionConvenioByIdComercioTrx(idfull);
                    if (dtoHomo != null) {
                       idfull =  ""+dtoHomo.getIdTransaccion();
                    }
                }
            }
            if (idBanco == 3 || idBanco == 16)
                idfull = idfull + "";
            if (idBanco == 4)
                idfull = idfull + "";
            if (idBanco == 8)
                idfull = idfull + "";
            if (idBanco == 9)
                idfull = idfull + "";
            if (idBanco == 10)
                idfull = idfull + ""; 
            if (idBanco == 12)
                idfull = idfull + ""; 


            return idfull;
        } catch (Exception e) {
            logger.error("getBancoIdComprobante(int, String) - catch (error)", 
                         e);
        }
        logger.info("getBancoIdComprobante(int idBanco=" + idBanco + 
                     ", xml) - termina");
        return null;
    }

    /**
     * Recupera XML de MPINI
     * @param xml
     * @return
     */
    public static String getXmlBancoRedireccionamiento(String xml) {
        logger.info("getXmlBancoRedireccionamiento(String xml) - iniciando");
        try {
            //Inicializa el Documento XML en modalidad DOM (Arbol)
            DOMParser parser = new DOMParser();
            parser.parse(new StringReader(xml));
            XMLDocument doc = parser.getDocument();
            String returnString = getNodeValueByXPath(doc, "/Confirmacion/Convenio/XML");
            return returnString;
        } catch (Exception e) {
            logger.error("getXmlBancoRedireccionamiento(String xml) - error", e);
        }
        return null;
    }

    /**
     * Recupers URL de pago MPNI
     * @param xml
     * @return
     */
    public static String getUrlBancoRedireccionamiento(String xml) {
        try {
            //Inicializa el Documento XML en modalidad DOM (Arbol)
            DOMParser parser = new DOMParser();
            parser.parse(new StringReader(xml));
            XMLDocument doc = parser.getDocument();
            String returnString = getNodeValueByXPath(doc, "/Confirmacion/Convenio/URL");

            String contexto = ResourcePropertiesUtil.getProperty("bice.contextpath");
            logger.info("contexto " + contexto);
            returnString = StringUtil.replaceString(returnString, "$WEBCONTEXT$", contexto);
            logger.info("getUrlBancoRedireccionamiento() -  redireccionado a URL de pago : " + returnString);
            return returnString;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Recupera el requerimiento para confirmacion
     * @return
     */
    public static ResumenRequest getRequerimientoConfirmaPago(PagoPublicoVO pago, 
                                                              int mediopago) {
        logger.info("getRequerimientoConfirmaPago(PagoPublicoVO pago=" + 
                     pago + ") - iniciando");

        ResumenRequest req = new ResumenRequest();

        //Dimensiono vectores de informacion
        Integer[] opciones = new Integer[pago.getCuotas().size()];
        Integer[] totaless = new Integer[pago.getCuotas().size()];
        Integer[] iditemss = new Integer[pago.getCuotas().size()];

        Iterator iter = pago.getCuotas().iterator();
        int counter = 0;
        while (iter.hasNext()) {
            PagoDetalleCuotaVO row = (PagoDetalleCuotaVO)iter.next();
            iditemss[counter] = row.getIdCuota();
            totaless[counter] = row.getTotalCuota().intValue();
            opciones[counter] = row.getValorEnPesos().intValue();
            counter++;
        }

        /**
         * Setea valores resumen
         */
        req.setBanco(mediopago);
        req.setOpciones(opciones);
        req.setTotales(totaless);
        req.setProductosSeleccionados(iditemss);
        req.setTotal_pagar(pago.getTotalPagar().intValue());
        logger.info("getRequerimientoConfirmaPago(PagoPublicoVO pago=" + 
                     pago + ") - termina");
        return req;
    }
    
    
    public static ResumenRequest getRequerimientoConfirmaPagoSeleccionado(PagoPublicoVO pago, int mediopago, Map paramvalore) {
        logger.info("getRequerimientoConfirmaPago(PagoPublicoVO pago=" + pago + ") - iniciando");

        ResumenRequest req = new ResumenRequest();
        List<String> listachekbox = new ArrayList<String>(pago.getHashcuotasl().keySet());
        
        Double totalpagar = 0.0;
        
        //Hashtable hash = pago.getHashcuotas();
        LinkedHashMap hash = pago.getHashcuotasl();
        ObjectMapper mapper = new ObjectMapper();
        Integer isSeleccionado=0;
        if(hash.size() > 0 && hash != null){
            for ( int i = 1; i <= hash.size(); i++){
                //mapeo el json de entrada a un objeto
                PagoDetalleCuotaVO vCuota = mapper.convertValue(hash.get(i), PagoDetalleCuotaVO.class);
                if(vCuota!=null && vCuota.isSelecionado()){
                    totalpagar += vCuota.getValorEnPesos();
                    isSeleccionado++;
                }
                
            }
        }
        
        if(listachekbox != null && listachekbox.size() > 0){
            if(totalpagar != null && totalpagar > 0 ){
                pago.setTotalPagar(totalpagar);
            }
            
            /*Integer[] opciones = new Integer[listachekbox.size()];
            Integer[] totaless = new Integer[listachekbox.size()];
            Integer[] iditemss = new Integer[listachekbox.size()];*/
            
            Integer[] opciones = new Integer[isSeleccionado];
            Integer[] totaless = new Integer[isSeleccionado];
            Integer[] iditemss = new Integer[isSeleccionado];
            
            for (int pos=1; pos<=isSeleccionado; pos++) {
                //PagoDetalleCuotaVO row = mapper.convertValue(hash.get(""+pos), PagoDetalleCuotaVO.class);
                PagoDetalleCuotaVO row = mapper.convertValue(hash.get(pos), PagoDetalleCuotaVO.class);
                if(row != null){
                  // if(row.isSelecionado()){
                        iditemss[pos-1] = row.getIdCuota();
                        totaless[pos-1] = row.getTotalCuota().intValue();
                        opciones[pos-1] = row.getValorEnPesos().intValue();                
                        pago.addCuotaDetalleHash(row.getIdCuota(), row);
                        hash.put(new Integer(row.getIdCuota()), row);
                }
            }           
            //pago.setHashcuotas(hash);
            pago.setHashcuotasl(hash);
            
            
            
            /**
            * Setea valores resumen
            */
            req.setBanco(mediopago);
            req.setOpciones(opciones);
            req.setTotales(totaless);
            req.setProductosSeleccionados(iditemss);
            req.setTotal_pagar(pago.getTotalPagar().intValue());
                
                
        }
        logger.info("getRequerimientoConfirmaPago(PagoPublicoVO pago=" + pago + ") - termina");
        return req;
    }
        
        

    /**
     * TODO: Metodo creado by Alejandro Fernandez
     * Obtiene los datos pendientes de pago para ser mostrados
     * @param xmlinput
     * @return
     */
    public static PagoPublicoVO getXMLParaPagosPendientes(String xmlinput) throws PagoPublicoCoreException {
        logger.info("getXMLParaPagoPublico(String xmlinput) - iniciando");
        PagoPublicoVO retorno = new PagoPublicoVO();
        PagoDatosClienteVO cliente = new PagoDatosClienteVO();
        retorno.setXmloriginal(xmlinput);
        try {
            //Inicializa el Documento XML en modalidad DOM (Arbol)
            DOMParser parser = new DOMParser();
            parser.parse(new StringReader(retorno.getXmloriginal()));
            XMLDocument doc = parser.getDocument();

            //Valores Generales
            retorno.setIdNavegacion(Integer.parseInt(getNodeValueByXPath(doc,  "/ResumenConHeader/InfoNavegacion/IDNavegacion")));
            retorno.setValorUF(Double.parseDouble(getNodeValueByXPath(doc, "/ResumenConHeader/ValorUF")));
            cliente.setRutCliente(Integer.parseInt(getNodeValueByXPath(doc,  "/ResumenConHeader/RutCliente")));
            retorno.setFechaTransaccion(FechaUtil.toDate(getNodeValueByXPath(doc,   "/ResumenConHeader/FechaConsulta").substring(0,  10),  "yyyy-MM-dd"));
            cliente.setNombreCliente(getNodeValueByXPath(doc,   "/ResumenConHeader/NombreCliente"));
            cliente.setFechaUF(FechaUtil.toDate(getNodeValueByXPath(doc, "/ResumenConHeader/FechaUF").substring(0,  10), "yyyy-MM-dd"));
            cliente.setFechaConsulta(FechaUtil.toDate(getNodeValueByXPath(doc,  "/ResumenConHeader/FechaConsulta").substring(0,  10),  "yyyy-MM-dd"));

            //LLamar WebServices para consulta de Informacion de Usuario
            /*01-09-2017 GR: Se quita llamada a web service, dejando solo consulta a base de datos schema personas*/
            PersonaDto persona = DAOFactory.getConsultasDao().findPersonaByRut(cliente.getRutCliente());
            if(persona != null){
                cliente.setCiudad(persona.getCiudad());
                cliente.setComuna(persona.getComuna());
                cliente.setDireccion(persona.getCalle());
                cliente.setRegion(persona.getRegion());
                cliente.setEmail(persona.getEmail());
                cliente.setNombreCliente(persona.getNombre());
            }
            /*cliente.setCiudad("--");
            cliente.setComuna("--");
            cliente.setDireccion("--");
            cliente.setRegion("--");
            cliente.setEmail(null);
                        
           URL url = new URL(ResourcePropertiesUtil.getProperty("bice.webservices.client.bice.clientes.endpoint")+"?wsdl");
           SessionEJBDatosClienteWebServiceService myPort = new SessionEJBDatosClienteWebServiceService(url);
          
           Map<String, Object> requestContext = ((BindingProvider)myPort.getSessionEJBDatosCliente()).getRequestContext();
           //requestContext.put("com.sun.xml.ws.developer.JAXWSProperties.CONNECT_TIMEOUT", 300000);
           requestContext.put("javax.xml.ws.client.connectionTimeout", 300000);
           requestContext.put("javax.xml.ws.client.receiveTimeout", 300000);            
            
            
            ResponseVODTO resp = myPort.getSessionEJBDatosCliente().getDataClienteBDPPublico(Integer.toString(cliente.getRutCliente()), 0);
            if (resp.isExito()) {
                if (resp.getDatocliente().getCiudad() != null) cliente.setCiudad(resp.getDatocliente().getCiudad());
                if (resp.getDatocliente().getComuna() != null) cliente.setComuna(resp.getDatocliente().getComuna());
                if (resp.getDatocliente().getCalle() != null)  cliente.setDireccion(resp.getDatocliente().getCalle());
                if (resp.getDatocliente().getRegion() != null) cliente.setRegion(resp.getDatocliente().getRegion());
                if (resp.getDatocliente().getMail() != null)   cliente.setEmail(resp.getDatocliente().getMail());
                cliente.setNombreCliente(resp.getDatocliente().getNombre());
            } else {
                logger.error("getXMLParaPagoPublico(String) - WebService Usuario (error)",  new Exception(resp.getMngexception()));
                throw new PagoPublicoCoreException(resp.getMessageerror());
            }*/
            
            PdfComprobantePagoDTO dtopdf = new PdfComprobantePagoDTO();
            ResponsePdfComprobanteVODTO respAdc = new ResponsePdfComprobanteVODTO();
            
            dtopdf = DAOFactory.getConsultasDao().findPdfComprobantebyRut(cliente.getRutCliente());            
            if(dtopdf != null){
                respAdc.setExito(true);
                respAdc.setDatoPdfComprobante(dtopdf);
                respAdc.setCoderror(new Integer(0));
                respAdc.setMessageerror(null);
                respAdc.setMngexception(null);   
            }
                        
            
            //Recupera Informacion Adicional
            //ResponsePdfComprobanteVODTO respAdc = myPort.getSessionEJBDatosCliente().getDataPdfComprobanteByRut(Integer.toString(cliente.getRutCliente()));
            if (respAdc.isExito()) {
                if (respAdc.getDatoPdfComprobante().getUrl() != null)   cliente.setUrlPDFs(respAdc.getDatoPdfComprobante().getUrl());
                if (respAdc.getDatoPdfComprobante().getIdpdf() != null) cliente.setIdsPDFs(respAdc.getDatoPdfComprobante().getIdpdf());
            } else {
                logger.error("getXMLParaPagoPublico(String) - WebService Datos Adicionales (error)", new Exception(respAdc.getMngexception()));
                throw new PagoPublicoCoreException(respAdc.getMessageerror());
            }
            
            
            /*ListOfPoliza listapolizas = null;
            SessionPolizaEJBClient myPortPoliza = new SessionPolizaEJBClient();
            myPortPoliza.setEndpoint(ResourcePropertiesUtil.getProperty("bice.webservices.client.bice.polizas.endpoint"));
            logger.info("=====> ENDPOINT SERVICIO: "+ myPortPoliza.getEndpoint() +" <========");
            Login login = new Login();
            Long rutconsulta = new Long(Integer.toString(cliente.getRutCliente()));
            InListaProductos consultaproducto = new InListaProductos();
            consultaproducto.setLogin(login);
            consultaproducto.setRutAsegurado(rutconsulta);                
            listapolizas = myPortPoliza.findProductos(consultaproducto);*/
            //List<VWIndPrimaNormalVO> listaPolizas = DAOFactory.getConsultasDao().findPolizasByRut(rutconsulta.intValue());
            // Consulta Producto APV
             /*ListOfProducto listaapv = null;
             WSApvEJBClient myPortApv = new WSApvEJBClient();
             myPortApv.setEndpoint(ResourcePropertiesUtil.getProperty("bice.webservices.client.bice.apv.endpoint"));
             logger.info("=====> ENDPOINT SERVICIO: "+ myPortApv.getEndpoint() +" <========");
             Long rutconsultax = new Long(Integer.toString(cliente.getRutCliente()));
             InProducto inproducto = new InProducto();
             inproducto.setRut(rutconsultax);
             listaapv = myPortApv.getProductosAPV(inproducto); */    
   
            //RECUPERA REGIN TRIBUTARIO
             //MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();
             //new Long(cliente.getRutCliente())
             List<RegimenTributarioDTO> regimens = obtenerListaRegimenTributarioPolizas(new Long(cliente.getRutCliente()));
            
            //double totalpagar = 0;
            //Recupera informacion de la transacciones para pagar
            NodeList cuotaPorPagar = doc.selectNodes("/ResumenConHeader/ProductosPorPagar/Entrada");
            for (int j = 0; j < cuotaPorPagar.getLength(); j++) {
                Element cuotaPagar = (Element)cuotaPorPagar.item(j);
                int contador = Integer.parseInt(cuotaPagar.getAttribute("contador"));
                
                //Getter XMl Tags
                String seleccionado = getAttributeNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +    contador + "]", "seleccionado");
                String disabled = getAttributeNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +   contador + "]", "disabled");
                String idcuota = getAttributeNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador + "]", "contador");
                String fechaVencimiento =  getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador + "]/Cuota/FechaVencimiento");
                String tipoCuota =  getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]/Producto/TipoCuota");
                String estadoCuota =  getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]/Cuota/Estado");
                String pagadoaFavor =  getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador + "]/Cuota/Pago");
                String codigoProducto =  getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador +  "]/Producto/CodigoProducto");
                String numeroProducto =   getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +   contador + "]/Producto/NumProducto");
                String numeroRamo = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]/InfoCajas/Polizas/Ramo");
                String folio = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]/InfoCajas/Polizas/Folio");
                
                String viaPago = null;
                String secuencia = null;
                if(codigoProducto.equalsIgnoreCase("1060")){ //producto APV
                     viaPago = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]/Cuota/CuotaFija/Detalle/Cargo[1]");
                     secuencia = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador + "]/Cuota/CuotaFija/Detalle/Cargo[2]");
                }
                

                int tipoCuotaPago = Integer.parseInt(tipoCuota);
                String montoPesos = "0";
                String montoUF = "0";
                switch (tipoCuotaPago) {
                case 1:
                    montoPesos = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador + "]/Cuota/CuotaFija/EnPesos");
                    montoUF = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador +  "]/Cuota/CuotaFija/EnUF");
                    break;
                case 2:
                    montoPesos = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador + "]/Cuota/CuotaFija/EnPesos");
                    montoUF =  getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador +  "]/Cuota/CuotaFija/EnUF");
                    break;
                case 3:
                    montoPesos = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador +  "]/Cuota/CuotaVariable/CuotaFija/EnPesos");
                    montoUF = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador + "]/Cuota/CuotaVariable/CuotaFija/EnUF");
                    break;
                case 4:
                    montoPesos = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador +  "]/Cuota/CuotaOpcion/CuotaMin/EnPesos");
                    montoUF = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]/Cuota/CuotaOpcion/CuotaMin/EnUF");
                    break;
                case 5:
                    montoPesos =  getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador +   "]/Cuota/CuotaOpcion/CuotaMin/EnPesos");
                    montoUF = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador +   "]/Cuota/CuotaOpcion/CuotaMin/EnUF");
                    break;
                }

                //VAlores vacios.
                if (pagadoaFavor == null || pagadoaFavor.length() == 0) pagadoaFavor = "0";
                if (montoPesos == null || montoPesos.length() == 0) montoPesos = "0";
                if (montoUF == null || montoUF.length() == 0) montoUF = "0";

                
                String descripcionProducto = null;
                String tipoProducto = null;
                //Caso Seguro individual
                //if(Integer.parseInt(codigoProducto) != 1060){
                    /*IdPoliza idpoliza  = new IdPoliza();
                    idpoliza.setNumero(new Long(numeroProducto));
                    idpoliza.setRamo(new Long(numeroRamo));
                    
                    Poliza poliza = getPolizaByIdPoliza(idpoliza, listapolizas);
                    if (poliza != null){                        
                        descripcionProducto = poliza.getNombre();
                        tipoProducto = poliza.getCategoria();
                    }else{*/
                
                        descripcionProducto = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador + "]/Producto/DescripcionProducto");
                        tipoProducto = "";
                    //}
               // }else{
                    
                
                    //Caso de APV    
                    /*
                    DetalleAPV polizaapv = getAPVByIdPoliza(numeroProducto, numeroRamo, listaapv);
                    if(polizaapv != null){
                        descripcionProducto = polizaapv.getDescripcionProducto();//"APV";
                        tipoProducto = polizaapv.getRegimenTributario();//"REGIMEN A";
                        if(descripcionProducto == null){
                            descripcionProducto = getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]/Producto/DescripcionProducto");
                        }
                        
                        if(tipoProducto == null){
                            tipoProducto="";                                
                        }
                    }else{
                        descripcionProducto =  getNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" + contador + "]/Producto/DescripcionProducto");
                        tipoProducto="";   
                    }
                    */
                //}
                
                if(descripcionProducto != null && tipoProducto != null){
                    PagoDetalleCuotaVO cuota = new PagoDetalleCuotaVO();

                    //Setter Vo
                    cuota.setSelecionado(Boolean.parseBoolean(seleccionado));
                    cuota.setDisabled(Boolean.parseBoolean(disabled));
                    cuota.setIdCuota(Integer.parseInt(idcuota));
                    cuota.setCodigoProducto(Integer.parseInt(codigoProducto));                    
                    
                    cuota.setDescripcionProducto(StringUtil.toTitleCase(descripcionProducto, " ") );
                    cuota.setTipoProducto(StringUtil.toTitleCase(tipoProducto, " "));
                    cuota.setEstado(estadoCuota);
                    cuota.setFechaVencimiento(FechaUtil.toDate(fechaVencimiento.substring(0, 10),   "yyyy-MM-dd"));
                    cuota.setFechaTransaccion(retorno.getFechaTransaccion());
                    cuota.setNumeroProducto(Integer.parseInt(numeroProducto));
                    cuota.setNumeroRamo(Integer.parseInt(numeroRamo));
                    cuota.setFolio_recibo(Integer.parseInt(folio));
                    cuota.setValorEnPesos(Double.parseDouble(montoPesos));
                    cuota.setValorEnUF(Double.parseDouble(montoUF));
                    cuota.setSaldoFavor(Double.parseDouble(pagadoaFavor));
                    cuota.setTotalCuota(new Double(cuota.getValorEnPesos().doubleValue() - 
                                                   cuota.getSaldoFavor().doubleValue()));
                    cuota.setTipoCuota(tipoCuotaPago);
                    if(viaPago != null && secuencia != null){
                        cuota.setViaPago(Integer.parseInt(viaPago));
                        cuota.setSecuencia(Integer.parseInt(secuencia));
                    }
                    
                    
                    if (regimens != null && regimens.size() > 0) {
                        if (cuota.getNumeroRamo() == 0) {
                            
                            cuota.getNumeroProducto();
                            Iterator desta = regimens.iterator();
                            while (desta.hasNext()) {
                                RegimenTributarioDTO regtip = (RegimenTributarioDTO) desta.next();
                                if (regtip.getNumeroPoliza().intValue() == cuota.getNumeroProducto()) {
                                    cuota.setTipoProducto(StringUtil.textoAltaBajas(regtip.getDescripcionRegimen(), true));
                                    if (Integer.parseInt(codigoProducto) == 1060) cuota.setTipoProducto("R\u00e9gimen " + StringUtil.textoAltaBajas(regtip.getDescripcionRegimen(), true));
                                    if (Integer.parseInt(codigoProducto) == 1063) cuota.setTipoProducto("R\u00e9gimen " + StringUtil.textoAltaBajas(regtip.getDescripcionRegimen(), true));
                                    if (Integer.parseInt(codigoProducto) == 1064) cuota.setTipoProducto("R\u00e9gimen " + StringUtil.textoAltaBajas(regtip.getDescripcionRegimen(), true));
                                    if (Integer.parseInt(codigoProducto) == 1065) cuota.setTipoProducto("R\u00e9gimen " + StringUtil.textoAltaBajas(regtip.getDescripcionRegimen(), true));
                                    
                                    break;
                                }
                            }
                        }
                    }
                    //retorno.addCuotaDetalle(cuota);
                    retorno.addCuotaDetalleHash(cuota.getIdCuota(), cuota);
                   
                }
                    
            }

            //Verifica que haya algo que cancelar
            if (retorno.getHashcuotas().size() == 0) {
                throw new PagoPublicoCoreException(ResourceMessageUtil.getProperty("jsf.pagopublico.navegacion.error.nodeudas"));
            }

          
            //Finalizo con contabilidad general
            retorno.setDatosCliente(cliente);

        } catch (Exception e) {
            logger.error("getXMLParaPagoPublico(String) - catch (error)", e);
            throw new PagoPublicoCoreException("No se ha logrado consultar sus datos en nuestra base, reintente mas tarde");
        }

        logger.info("getXMLParaPagoPublico(String xmlinput) - termina");
        return retorno;
    }
    
    /**
     * TODO: Metodo creado by Alejandro Fernandez
     * Setea los datos seleccionados y deshabilitados en PagoPublicoVO
     * @param voinput
     * @return PagoPublicoVO
     */
    public static PagoPublicoVO setDatosSeleccionadosXml(PagoPublicoVO voinput, String xmlinput) throws PagoPublicoCoreException {
        logger.info("setDatosSeleccionadosXml() - iniciando");        
        
        try {
            //Inicializa el Documento XML en modalidad DOM (Arbol)
            DOMParser parser = new DOMParser();
            parser.parse(new StringReader(xmlinput));
            XMLDocument doc = parser.getDocument();

            
            //Recupera informacion de la transacciones para pagar
            NodeList cuotaPorPagar =  doc.selectNodes("/ResumenConHeader/ProductosPorPagar/Entrada");
            for (int j = 0; j < cuotaPorPagar.getLength(); j++) {
                Element cuotaPagar = (Element)cuotaPorPagar.item(j);
                int contador = Integer.parseInt(cuotaPagar.getAttribute("contador"));
                PagoDetalleCuotaVO cuota = (PagoDetalleCuotaVO)voinput.getHashcuotas().get(contador);
                //Getter XMl Tags
                String seleccionado = getAttributeNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]", "seleccionado");
                String disabled = getAttributeNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]", "disabled");
                //Setter Vo
                if(cuota != null){
                    if(seleccionado != null){
                        cuota.setSelecionado(Boolean.parseBoolean(seleccionado));
                    }else{
                        cuota.setSelecionado(false);
                    }
                    if(disabled != null){
                        cuota.setDisabled(Boolean.parseBoolean(disabled));
                    }else{
                        cuota.setDisabled(false);
                    }
                    voinput.addCuotaDetalleHash(contador, cuota);
                }
            }

            
        } catch (SAXException e) {
            logger.error("setDatosSeleccionadosXml() - catch (error)", e);
            throw new PagoPublicoCoreException(ResourceMessageUtil.getProperty("jsf.pagopublico.core.exception.xml"));
        } catch (XSLException e) {
            logger.error("setDatosSeleccionadosXml() - catch (error)", e);
            throw new PagoPublicoCoreException(ResourceMessageUtil.getProperty("jsf.pagopublico.core.exception.xslt"));
        } catch (IOException e) {
            logger.error("setDatosSeleccionadosXml() - catch (error)", e);
            throw new PagoPublicoCoreException(ResourceMessageUtil.getProperty("jsf.pagopublico.core.exception.io"));
        }

        logger.info("setDatosSeleccionadosXml() - termina");
        return voinput;
    }

    /**
     * Genera Comprobante en espera de pago
     * @return
     */
    public static PagoRutasFilesVO generarPDFEnEspera(PagoPublicoVO pago) {
        PagoRutasFilesVO url = null;
        GeneradorAcrobatPdf pdf = new GeneradorAcrobatPdf();
        XMLForPDF xmlForPdf = new XMLForPDF();
        String pathroot = FacesUtil.getServletContext().getRealPath("/")+ File.separator;
        String pathrootPDF = ResourcePropertiesUtil.getProperty("bice.email.replace.folder");
        logger.info("Path PDF [" + pathrootPDF + "]");
        pdf.setXslt(new File(pathroot, "xslt" + File.separator + "comprobante-enespera-fo.xsl"));
        pdf.setDiroutput(new File(pathrootPDF, "pdf"));
        pdf.setFilenamepdfinitial("enesperapago");
        pdf.setXml(xmlForPdf.getXMLForPDF(pago));
        pdf.setContextPathImages(pathroot + "images" + File.separator +  "styles" + File.separator);
        if (pdf.generatePDF(false)) {
            url = new PagoRutasFilesVO();
            url.setPathRelativo( ResourcePropertiesUtil.getProperty("bice.email.replace.to.webpagopublic.desa")+ pdf.getPdfFilename());
            url.setPathAbsoluto( ResourcePropertiesUtil.getProperty("bice.email.replace.to.webpagopublic.desa")+ pdf.getPdfFilename());
        }
        return url;
    }

    /**
     * Genera Comprobante en espera de pago
     * @return
     */
    public static PagoRutasFilesVO generarPDFPagoOK(PagoPublicoVO pago, String realPath) {
        PagoRutasFilesVO url = null;
        GeneradorAcrobatPdf pdf = new GeneradorAcrobatPdf();
        XMLForPDF xmlForPdf = new XMLForPDF();
        //String pathroot = FacesUtil.getServletContext().getRealPath("/") + File.separator;
        String pathroot = realPath + File.separator;
        logger.info("pathroot " + pathroot);
        String pathrootPDF = ResourcePropertiesUtil.getProperty("bice.email.replace.folder");
        //String pathrootPDF = "C:\\Planilla" + File.separator;
        logger.info("Path PDF [" + pathrootPDF + "]");
        pdf.setXslt(new File(pathroot,  "xslt" + File.separator + "comprobante-pagado-fo.xsl"));
        pdf.setDiroutput(new File(pathrootPDF, "pdf"));
        pdf.setFilenamepdfinitial("comprobantepago");
        pdf.setXml(xmlForPdf.getXMLForPDF(pago));
        pdf.setContextPathImages(pathroot + "images" + File.separator +  "styles" + File.separator);
        if (pdf.generatePDF(true)) {
            url = new PagoRutasFilesVO();
            url.setPathRelativo( ResourcePropertiesUtil.getProperty("bice.email.replace.to.webpagopublic.desa")+ pdf.getPdfFilename());
            url.setPathAbsoluto( ResourcePropertiesUtil.getProperty("bice.email.replace.to.webpagopublic.desa")+ pdf.getPdfFilename());
            
            /*url.setPathRelativo( "C:\\Planilla\"PDF\"" + pdf.getPdfFilename());
            url.setPathAbsoluto( "C:\\Planilla\"PDF\"" + pdf.getPdfFilename());*/
        }
        return url;
    }

    /**
     * Recupera Elemento Valor
     * @param doc
     * @param xPath
     * @return
     * @throws XSLException
     */
    public static String getNodeValueByXPath(XMLDocument doc,   String xPath) throws XSLException {
        String find = null;
        NodeList encontrado = doc.selectNodes(xPath);
        if (encontrado != null && encontrado.getLength() > 0) {
            Element elemento = (Element)encontrado.item(0);
            NodeList objNodes = elemento.getChildNodes();
            find = objNodes.item(0).getNodeValue();
        }
       return find;
    }
    
    
    /**
     * Recupera Elemento Valor
     * @param doc
     * @param xPath
     * @return
     * @throws XSLException
     */
    private static String getNodeByXPath(XMLDocument doc, String xPath) throws XSLException {
        String find = null;
        NodeList encontrado = doc.selectNodes(xPath);
        if (encontrado != null && encontrado.getLength() > 0) {
            Element elemento = (Element)encontrado.item(0);
            find = elemento.getNodeValue();
        }
          return find;
    }

    /**
     * Recupera el atributo de un nodo
     * @param doc
     * @param xPath
     * @return
     * @throws XSLException
     */
    private static String getAttributeNodeValueByXPath(XMLDocument doc, 
                                                       String xPath, 
                                                       String atributo) throws XSLException {
        String find = null;
        NodeList encontrado = doc.selectNodes(xPath);
        if (encontrado != null && encontrado.getLength() > 0) {
            Element elemento = (Element)encontrado.item(0);
            find = elemento.getAttribute(atributo);
        }
       return find;
    }

    /**
     * Recupera Elemento Valor
     * @param doc
     * @param xPath
     * @return
     * @throws XSLException
     */
    private static void setNodeValueByXPath(XMLDocument doc, String xPath, 
                                            String newValueNode) throws XSLException {
        NodeList encontrado = doc.selectNodes(xPath);
        if (encontrado != null && encontrado.getLength() > 0) {
            Element elemento = (Element)encontrado.item(0);
            NodeList objNodes = elemento.getChildNodes();
            objNodes.item(0).setNodeValue(newValueNode);
        }
    }

    /**
     * Recupera el valor de contenido de un nodo
     * @param elemento
     * @return
     * @throws XSLException
     */
    private static String getNodeValueByElement(Element elemento) throws XSLException {
        NodeList objNodes = elemento.getChildNodes();
        String returnString = objNodes.item(0).getNodeValue();
        return returnString;
    }



    /**
     * Verifica si una URL Existe
     * @param url
     * @return
     */
    public static boolean exists(URL url) {
        logger.info("exists() - iniciando");
        InputStream is = null;
        DataInputStream dis;
        String s;
        try {
            logger.info("exists() - verificando url si existe");
            is = url.openStream();
            logger.info("exists() - recuperando buffers");
            dis = new DataInputStream(new BufferedInputStream(is));

            logger.info("exists() - iternado lectura de data");
            String html = null;
            int partes = 0;
            while ((s = dis.readLine()) != null) {
                if (partes > 1)
                    break;
                html = html + s;
                partes++;
            }
            logger.info("exists() - evaluando que sean bytes y no html (page not found)");
            if (html.indexOf("<html>") == -1)
                return true;
        } catch (MalformedURLException mue) {
        } catch (IOException ioe) {
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
            }
        }
        logger.info("exists() - terminado");
        return false;

    }
    
    /**
     * Obtiene la poliza asociada al ID
     * @param idpoliza
     * @return
     */
    private static Poliza getPolizaByIdPoliza(IdPoliza idpoliza, ListOfPoliza listapolizas) {
        Poliza poliza = null;

        if(listapolizas != null && listapolizas.getItem() != null && listapolizas.getItem().size() > 0){
            //for(int i = 0; i < listapolizas.getItem().length; i++){
            for(Poliza polizaitem: listapolizas.getItem()) {
                //Poliza polizaitem = listapolizas.getItem()[i];
                if(polizaitem != null && polizaitem.getIdPoliza() != null){
                    IdPoliza polizaitemid = polizaitem.getIdPoliza();
                    if(polizaitemid.getNumero() != null && polizaitemid.getRamo() != null){
                        if(polizaitemid.getNumero().longValue() == idpoliza.getNumero().longValue() && polizaitemid.getRamo().longValue() ==  idpoliza.getRamo().longValue()){
                            poliza = polizaitem;
                            break;
                        }    
                    }                    
                }
            }
        }
        
        return poliza;
    }
    
    /**
     * Obtiene la poliza asociada al ID
     * @return
     */
    private static DetalleAPV getAPVByIdPoliza(String poliza, String ramo, ListOfProducto listaapv) {            
        DetalleAPV detalle = null;
        
        if(listaapv != null && listaapv.getItem() != null && listaapv.getItem().size() > 0){
            //for(int i = 0; i < listaapv.getItem().length; i++){
            for(Producto apvitem : listaapv.getItem()) {
                //Producto apvitem = listaapv.getItem()[i];
                if(apvitem != null && apvitem.getNPoliza() != null && apvitem.getRamSubRamo() != null){
                        Long lpoliza = new Long(poliza);
                        Long lramo = new Long(ramo);
                        if(apvitem.getNPoliza().longValue() == lpoliza.longValue() && apvitem.getRamSubRamo().longValue() ==  lramo.longValue()){
                            //MedioPagoElectronicoEJB ejb = ServiceLocator.getMedioPagoElectronicoEJB();        
                            detalle = DAOFactory.getPersistenciaGeneralDao().getDetalleProductoAPV(poliza);
                            //detalle = ejb.getDetalleProductoAPV(poliza);                            
                            detalle.setRegimenTributario(detalle.getRegimenTributario().replaceAll("Tributario tipo", ""));
                            detalle.setRegimenTributario(detalle.getRegimenTributario().replaceAll("Tributario Tipo", ""));
                            detalle.setRegimenTributario(detalle.getRegimenTributario().replace(")", ""));
                            detalle.setDescripcionProducto(apvitem.getDesclProd());
                            break;
                        }    
                                        
                }
            }
        }
        return detalle;
    }
    
    /*public static List<RegimenTributarioDTO> obtenerListaRegimenTributarioPolizas(Long rutCliente) {
        List<RegimenTributarioDTO> retorno = new ArrayList<RegimenTributarioDTO>();
        CuentaDeInversionClient myPort;
        try {
            myPort = new CuentaDeInversionClient();
            myPort.setEndpoint(ResourceBundleUtil.getProperty("webservice.cuentainversion.endpoint"));
            ObtenerInformacionRegimen in = new ObtenerInformacionRegimen();
            in.setRut(rutCliente);
            ListOfRetornoInformacionRegimen data = myPort.obtenerInformacionRegimen(in);
            if (data != null) {
                List<RetornoInformacionRegimen> regimens  = data.getItem();
                if (regimens != null && !regimens.isEmpty()) {
                    for(RetornoInformacionRegimen reg: regimens) {
                        RegimenTributarioDTO dto = new RegimenTributarioDTO();
                        dto.setDescripcionRegimen(reg.getDescripcionRegimenTributario());
                        dto.setNumeroPoliza(reg.getNumeroPoliza());
                        dto.setNumeroRegimen(reg.getRegimenTributario());
                        retorno.add(dto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }*/
    
    public static List<RegimenTributarioDTO> obtenerListaRegimenTributarioPolizas(Long rutCliente) {
        List<RegimenTributarioDTO> retorno = new ArrayList<RegimenTributarioDTO>();
        //        CuentaDeInversionClient myPort;
        try {
            URL url = new URL(cl.bicevida.botonpago.util.ResourcePropertiesUtil.getProperty("bice.webservices.client.bice.aptcuentainversion.endpointAPT"));
            CuentaDeInversionWebService_Service wsAPV = new CuentaDeInversionWebService_Service(url);
            CuentaDeInversionWebService iCI = wsAPV.getCuentaDeInversionWebServicePort();
            //myPort = new CuentaDeInversionClient();
            
            ObtenerInformacionRegimenResponse resp = new ObtenerInformacionRegimenResponse();
            ObtenerInformacionRegimen in = new ObtenerInformacionRegimen();
            in.setRut(rutCliente);
            
            
            List<RetornoInformacionRegimen> regimens = iCI.obtenerInformacionRegimen(rutCliente);
            
            //ListOfRetornoInformacionRegimen data = myPort.obtenerInformacionRegimen(in);
            if (regimens != null && regimens.size() > 0) {
                //List<RetornoInformacionRegimen> regimens  = data.getItem();
                if (regimens != null && !regimens.isEmpty()) {
                    for(RetornoInformacionRegimen reg: regimens) {
                        RegimenTributarioDTO dto = new RegimenTributarioDTO();
                        dto.setDescripcionRegimen(reg.getDescripcionRegimenTributario());
                        dto.setNumeroPoliza(reg.getNumeroPoliza());
                        dto.setNumeroRegimen(reg.getRegimenTributario());
                        retorno.add(dto);
                        resp.getReturn().add(reg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
    
    public static String getValidaNodePathMedioPago(PagoPublicoVO pago)throws Exception{
        //Inicializa el Documento XML en modalidad DOM (Arbol)
        DOMParser parser = new DOMParser();
        parser.parse(new StringReader(pago.getXmlconfirmacion()));
        XMLDocument doc = parser.getDocument();
        String xPath = "/Confirmacion/Convenio/Codigo";
        String xml = getNodeValueByXPath(doc,xPath);
        return xml;        
    }
    
    public static boolean getValidaProductoSeleccionado(PagoPublicoVO pago) throws Exception {
        boolean flag = true; 
        try {
            
            
            DOMParser parser = new DOMParser();
            parser.parse(new StringReader(pago.getXmlpagopublico()));
            XMLDocument doc = parser.getDocument();
            ObjectMapper mapper = new ObjectMapper();
            //Recupera informacion de la transacciones para pagar
            NodeList cuotaPorPagar =  doc.selectNodes("/ResumenConHeader/ProductosPorPagar/Entrada");
            for (int j = 0; j < cuotaPorPagar.getLength(); j++) {
                Element cuotaPagar = (Element)cuotaPorPagar.item(j);
                int contador = Integer.parseInt(cuotaPagar.getAttribute("contador"));
                //PagoDetalleCuotaVO cuota = (PagoDetalleCuotaVO)pago.getHashcuotas().get(contador);
                PagoDetalleCuotaVO cuota = mapper.convertValue(pago.getHashcuotas().get(contador), PagoDetalleCuotaVO.class);
                //Getter XMl Tags
                String seleccionado = getAttributeNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]", "seleccionado");
                String idCuota = getAttributeNodeValueByXPath(doc, "/ResumenConHeader/ProductosPorPagar/Entrada[" +  contador + "]", "contador");
                
                if(idCuota != null && Integer.parseInt(idCuota) == contador){
                    if(cuota != null && seleccionado != null && !"".equals(seleccionado)){
                        if(cuota.isSelecionado() != Boolean.parseBoolean(seleccionado)){
                            flag = false;
                            break;
                        }
                    }
                }
                
            }
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } catch (XMLParseException e) {
            throw new Exception(e.getMessage());
        } catch (SAXException e) {
            throw new Exception(e.getMessage());
        } catch (XSLException e) {
            throw new Exception(e.getMessage());
        }
        return flag;

    }
    
    public static PagoPublicoVO llenaLinked(PagoPublicoVO pago) throws Exception {
        pago.getHashcuotas().clear();
        pago.getHashcuotasl().clear();
        LinkedHashMap link = new LinkedHashMap();
        if(pago.getHashList().size() < 1){
            throw new Exception("ERROR: No se pudo llenar hash con las polizas");
        }else{
            Collections.sort(pago.getHashList(), new Comparator<PagoDetalleCuotaVO>(){
                 public int compare(PagoDetalleCuotaVO o1, PagoDetalleCuotaVO o2){
                     if(o1.getIdCuota() == o2.getIdCuota())
                         return 0;
                     return o1.getIdCuota() < o2.getIdCuota() ? -1 : 1;
                 }
            });
              
            for(PagoDetalleCuotaVO det: pago.getHashList()){
                pago.getHashcuotasl().put(det.getIdCuota(), det);
                pago.addCuotaDetalleHash(det.getIdCuota(), det);
                logger.info("se lleno la poliza "+det.getNumeroRamo() +"-"+det.getNumeroProducto() + " con el ID: " + det.getIdCuota());
            }
            
            
        }
        
        return pago;
    }
    
    
    
}
