package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.AperturaCaja;
import cl.bice.vida.botonpago.common.dto.general.CartolaPolizas;
import cl.bice.vida.botonpago.common.dto.general.DetallePagoPoliza;
import cl.bice.vida.botonpago.common.dto.general.Navegacion;
import cl.bice.vida.botonpago.common.dto.parametros.MedioPago;
import cl.bice.vida.botonpago.common.dto.parametros.ValorUf;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosHistoricoVw;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosOfflineVw;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalOfflineVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalVw;
import cl.bice.vida.botonpago.common.exception.BiceException;
import cl.bice.vida.botonpago.common.util.NumeroUtil;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import noNamespace.ConfirmacionDocument;
import noNamespace.DetalleCargos;
import noNamespace.EstadoCaja;
import noNamespace.EstadoInternet;
import noNamespace.Infocajas;
import noNamespace.MontoCuota;
import noNamespace.ResumenConHeaderDocument;

import noNamespace.ResumenConHeaderDocument.ResumenConHeader.Convenios;
import noNamespace.ResumenConHeaderDocument.ResumenConHeader.IdentificacionMensaje;
import noNamespace.ResumenConHeaderDocument.ResumenConHeader.InfoNavegacion;
import noNamespace.ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar;
import noNamespace.ResumenConHeaderDocument.ResumenConHeader.Titulos;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;


public class LogicaResumenPagosDAOImpl extends DataSourceBice implements LogicaResumenPagosDAO {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(LogicaResumenPagosDAOImpl.class);

    /**
     * Variables Privadas de la clase
     */
    private int contador = 0;
    private int monto_pagar = 0;
    private Boolean[] tipos_cuotas_sel = new Boolean[4];
    private Boolean tipos_cuotas_pat_pac = new Boolean(false);
    private String[] nombre_titulos = new String[4];


    public String crearResumenPagos(int rut, String nombre, String confirmacion) {
        return null;
    }

    /**
     * Metodod para generar el XML de resumen
     * y pagos desde aplicaciones web
     * @param rut
     * @param nombre
     * @param confirmacion
     * @return string xml con el resumen y pagos
     */
    public String crearResumenPagosPolizas(int rut, String nombre, String confirmacion, int idCanal) {
        logger.info("crearResumenPagosPolizas() - inicio");
        for (int i = 0; i < tipos_cuotas_sel.length; i++)
            tipos_cuotas_sel[i] = false;
        tipos_cuotas_pat_pac = false;
        contador = 0;
        monto_pagar = 0;

        nombre_titulos[0] = "Normal";
        nombre_titulos[1] = "Min";
        nombre_titulos[2] = "Max";
        nombre_titulos[3] = "Prima Contratada";
        
        
        String resumenxml = "NOK|" + ResourceBundleUtil.getProperty("mensaje.error.resumenypagos.general.bicevida");
        logger.info("resumenxml : "+ resumenxml);
        try {
            logger.info("crearResumenPagosPolizas() - verificando confirmacion");
            ConfirmacionDocument conf = null;
            if (confirmacion != null) {
                logger.info("crearResumenPagosPolizas() - confirmacion distinto de null");
                conf = ConfirmacionDocument.Factory.parse(confirmacion);
                logger.info("crearResumenPagosPolizas() - validando xml");
                validateXML(conf);
            }
            logger.info("crearResumenPagosPolizas() - generando resumen y pagos");
            ResumenConHeaderDocument resumen = generaXmlResumenPagosPolizas(rut, nombre, conf);
            logger.info("crearResumenPagosPolizas() - validando resumen y pago" + resumen);
            validateXML(resumen);
            
            logger.info("crearResumenPagosPolizas() - convirtiendo resumen a String" + resumen);
            resumenxml = resumen.toString();
            logger.info("crearResumenPagosPolizas() - creando detalle de navegacion" + resumenxml);
            DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(resumen.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                           resumen.getResumenConHeader().getInfoNavegacion().getEntrada().intValue(),
                                                                           1,
                                                                           resumen.getResumenConHeader().getTotalPagar().intValue(),
                                                                           resumenxml, idCanal);
            logger.info("despues del crear el detalle");
        } catch (BiceException e) {
            logger.warn("crearResumenPagosPolizas() - BiceException : NOK :" + e.getMessage());
            resumenxml = "NOK|" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("crearResumenPagosPolizas() - error : NOK :" + e.getMessage(), e);
            resumenxml = "NOK|" + ResourceBundleUtil.getProperty("mensaje.error.resumenypagos.general.bicevida");
            e.printStackTrace();
        } finally {
            logger.info("crearResumenPagosPolizas() - termino");
            return resumenxml;
        }
    }

    /**
     * Metodod para generar el XML de resumen
     * y pagos desde aplicaciones web
     * @param rut
     * @param nombre
     * @param confirmacion
     * @return string xml con el resumen y pagos
     */
    public String crearResumenPagosAPVAPT(int rut, String nombre, String confirmacion, int idCanal) {
        logger.debug("crearResumenPagosAPVAPT() - inicio");
        for (int i = 0; i < tipos_cuotas_sel.length; i++)
            tipos_cuotas_sel[i] = false;
        tipos_cuotas_pat_pac = false;
        contador = 0;
        monto_pagar = 0;

        nombre_titulos[0] = "Normal";
        nombre_titulos[1] = "Min";
        nombre_titulos[2] = "Max";
        nombre_titulos[3] = "Prima Contratada";

        String resumenxml = "NOK|" + ResourceBundleUtil.getProperty("mensaje.error.resumenypagos.general.bicevida");
        logger.info("resumenxml " + resumenxml);
        try {
            logger.debug("crearResumenPagosAPVAPT() - verificando confirmacion");
            ConfirmacionDocument conf = null;
            if (confirmacion != null) {
                logger.info("crearResumenPagosAPVAPT() - confirmacion distinto de null");
                conf = ConfirmacionDocument.Factory.parse(confirmacion);
                logger.info("crearResumenPagosPolizas() - validando xml");
                validateXML(conf);
            }
            logger.debug("crearResumenPagosAPVAPT() - generando resumen y pagos");
            ResumenConHeaderDocument resumen = generaXmlResumenPagosAPVAPT(rut, nombre, conf);
            logger.debug("crearResumenPagosAPVAPT() - validando resumen y pago");
            validateXML(resumen);
            logger.debug("crearResumenPagosAPVAPT() - convirtiendo resumen a String");
            resumenxml = resumen.toString();
            logger.info("crearResumenPagosAPVAPT() - creando detalle de navegacion IDNavegacion["+resumen.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue()+"]Entrada["+resumen.getResumenConHeader().getInfoNavegacion().getEntrada().intValue()+"]Monto[" + resumen.getResumenConHeader().getTotalPagar().intValue() + "]");
            DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(resumen.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                           resumen.getResumenConHeader().getInfoNavegacion().getEntrada().intValue(),
                                                                           1,
                                                                           resumen.getResumenConHeader().getTotalPagar().intValue(),
                                                                           resumenxml, idCanal);
        } catch (BiceException e) {
            e.printStackTrace();
            logger.warn("crearResumenPagosAPVAPT() - BiceException : NOK :" + e.getMessage());
            resumenxml = "NOK|" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("crearResumenPagosAPVAPT() - error : NOK :" + e.getMessage(), e);
            resumenxml = "NOK|" + ResourceBundleUtil.getProperty("mensaje.error.resumenypagos.general.bicevida");
            e.printStackTrace();
        } finally {
            logger.debug("crearResumenPagosAPVAPT() - termino");
            return resumenxml;
        }
    }

    /**
     * Consulta Dividendos segun el rut
     * del cliente que desea cancelar
     * @param rut
     * @param nombre
     * @param confirmacion
     * @return xml con la informacion de las polizadas a pagar
     */
    public String crearResumenPagosDividendos(int rut, String nombre, String confirmacion, int idCanal) {
        logger.debug("crearResumenPagosDividendos() - inicio");
        logger.debug("crearResumenPagosDividendos() - inicializacion de variables usadas para la generacion de resumen y pagos");
        for (int i = 0; i < tipos_cuotas_sel.length; i++)
            tipos_cuotas_sel[i] = false;
        contador = 0;
        monto_pagar = 0;

        nombre_titulos[0] = "Normal";
        nombre_titulos[1] = "Min";
        nombre_titulos[2] = "Max";
        nombre_titulos[3] = "Prima Contratada";

        String resumenxml = "NOK|Error: Inesperado";
        try {
            logger.debug("crearResumenPagosDividendos() - verificando confirmacion");
            ConfirmacionDocument conf = null;
            if (confirmacion != null) {
                logger.debug("crearResumenPagosDividendos() - confirmacion distinto de null");
                conf = ConfirmacionDocument.Factory.parse(confirmacion);
                logger.debug("crearResumenPagos() - validando xml");
                validateXML(conf);
            }
            logger.debug("crearResumenPagosDividendos() - generando resumen y pagos");
            ResumenConHeaderDocument resumen = generaXmlResumenPagosDividendos(rut, nombre, conf);
            logger.debug("crearResumenPagosDividendos() - validando resumen y pago");
            validateXML(resumen);
            logger.debug("crearResumenPagosDividendos() - convirtiendo resumen a String");
            resumenxml = resumen.toString();
            logger.info("crearResumenPagosDividendos() - creando detalle de navegacion");
            logger.info("crearResumenPagosDividendos() - creando detalle de navegacion IDNavegacion["+resumen.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue()+"]Entrada["+resumen.getResumenConHeader().getInfoNavegacion().getEntrada().intValue()+"]Monto[" + resumen.getResumenConHeader().getTotalPagar().intValue() + "]");
            DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(resumen.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                           resumen.getResumenConHeader().getInfoNavegacion().getEntrada().intValue(),
                                                                           1,
                                                                           resumen.getResumenConHeader().getTotalPagar().intValue(),
                                                                           resumenxml, idCanal);
        } catch (BiceException e) {
            e.printStackTrace();
            logger.warn("crearResumenPagosPolizas() - BiceException : NOK :" + e.getMessage());
            resumenxml = "NOK|" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("crearResumenPagosPolizas() - error : NOK :" + e.getMessage(), e);
            resumenxml = "NOK|" + ResourceBundleUtil.getProperty("mensaje.error.resumenypagos.general");
        } finally {
            logger.debug("crearResumenPagosDividendos() - termino");
            return resumenxml;
        }

    }

    /**
     * Valida la estructura del mensaje
     * xml que se esta enviando
     * @param xml
     * @throws BiceException
     */
    public void validateXML(XmlObject xml) throws BiceException {
        Boolean validar = new Boolean(ResourceBundleUtil.getProperty("xml.validation.enable"));
        if (validar.booleanValue() == false)
            return;
        logger.info("validateXML() - inicio");
        Collection errorList = new ArrayList();
        XmlOptions xo = new XmlOptions();
        logger.info("validateXML() - seteando lista de error");
        xo.setErrorListener(errorList);
        if (!xml.validate(xo)) {
            logger.info(xml.toString());
            StringBuffer sb = new StringBuffer();
            for (Iterator it = errorList.iterator(); it.hasNext();) {
                sb.append(it.next() + "\n");
            }
            logger.info("validateXML() - XML con errores : " + sb.toString());
            String txtMsg = "XML con errores:" + sb.toString();
            logger.info("validateXML() - termino con error");
            throw new BiceException(txtMsg);
        }
        logger.info("validateXML() - termino");
    }

    public ResumenConHeaderDocument generaXmlResumenPagos(int rut, String nombre, ConfirmacionDocument conf) {
        return null;
    }

    /**
     * Metod que genera el XML a enviar
     * pro concepto de resumen y pagos
     * @param rut
     * @param nombre
     * @param conf
     * @return
     */
    public ResumenConHeaderDocument generaXmlResumenPagosPolizas(int rut, String nombre,
                                                                 ConfirmacionDocument conf) throws BiceException,
                                                                                                   ParseException {
        logger.info("generaXmlResumenPagosPolizas() - inicio");
        Calendar fecha = Calendar.getInstance();
        //SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM");
        //SimpleDateFormat formato1 = new SimpleDateFormat("yyyy-MM-dd");
        //String fechas = formato.format(fecha.getTime());
        //fechas = fechas + "-05";
        //Date fecha_uf = formato1.parse(fechas);
        Date fecha_uf = fecha.getTime();
        logger.info("generaXmlResumenPagosPolizas() - recuperando ResumenConHeaderDocument");
        ResumenConHeaderDocument resumen = ResumenConHeaderDocument.Factory.newInstance();    
        logger.info("generaXmlResumenPagosPolizas() - ejecutando addNewResumenConHeader()");
        ResumenConHeaderDocument.ResumenConHeader newResumenConHeader = resumen.addNewResumenConHeader();
        Long idNavegacion = 0L;
        logger.info("generaXmlResumenPagosPolizas() - SETANDO LA INFORMACION GENERAL");
        if (conf != null) {
            logger.debug("generaXmlResumenPagosPolizas() - confirmacion distinto de null");
            logger.debug("generaXmlResumenPagosPolizas() - addNewInfoNavegacion");
            newResumenConHeader.addNewInfoNavegacion();
            logger.info("generaXmlResumenPagosPolizas() - seteando valores de nevegacion - IDNavegacion["+ conf.getConfirmacion().getInfoNavegacion().getIDNavegacion() +"]");
            idNavegacion = conf.getConfirmacion().getInfoNavegacion().getIDNavegacion().longValue();
            newResumenConHeader.getInfoNavegacion().setIDNavegacion(conf.getConfirmacion().getInfoNavegacion().getIDNavegacion());
            newResumenConHeader.getInfoNavegacion().setEntrada(conf.getConfirmacion().getInfoNavegacion().getEntrada().add(new BigInteger("1")));
        } else {
            logger.info("generaXmlResumenPagosPolizas() - confirmacion en null");
            setInfoNavegacion(newResumenConHeader, rut, 1);
        }

        logger.info("generaXmlResumenPagosPolizas() - IDNavegacion["+idNavegacion+"] datos del clientes (rut["+rut+"], nombre["+nombre+"], fecha consulta["+fecha+"])");
        newResumenConHeader.setRutCliente(rut);
        newResumenConHeader.setNombreCliente(nombre);
        setIdentificacionMensaje(newResumenConHeader);
        newResumenConHeader.setFechaConsulta(fecha);

        BigDecimal valor_uf;
        logger.debug("generaXmlResumenPagosPolizas() - seteando el valor de la UF");
        if (conf == null ||
            conf.getConfirmacion().getFechaConsulta().DATE != newResumenConHeader.getFechaConsulta().DATE) {
            valor_uf = setValorUF(fecha_uf);
        } else {
            valor_uf = conf.getConfirmacion().getValorUF();
        }

        Calendar fecha_uf_calendar = Calendar.getInstance();
        fecha_uf_calendar.setTime(fecha_uf);
        newResumenConHeader.setFechaUF(fecha_uf_calendar);
        newResumenConHeader.setValorUF(valor_uf);

        logger.info("generaXmlResumenPagosPolizas() - buscando si la caja esta habierta para setear el turno");
        AperturaCaja turno = DAOFactory.getPersistenciaGeneralDao().findCajaAbierta();
        if (turno != null) {
            fecha.setTime(turno.getTurno());
            newResumenConHeader.setTurno(fecha);
        }

        logger.debug("generaXmlResumenPagosPolizas() - Copia ProductosPorPagar");
        ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp;
        pp = newResumenConHeader.addNewProductosPorPagar();
        logger.debug("generaXmlResumenPagosPolizas() - seteando productos por pagar setProductosPorPagar()");
        if (conf != null)
            setProductosPorPagarPolizas(pp, rut, valor_uf, turno, conf.getConfirmacion().getProductosPorPagar());
        else
            setProductosPorPagarPolizas(pp, rut, valor_uf, turno, null);

        logger.debug("generaXmlResumenPagosPolizas() - Copia ProductosPagados");
        ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pPagados;
        pPagados = newResumenConHeader.addNewProductosPagados();

        setProductosPagadosPolizas(pPagados, rut);


        logger.debug("generaXmlResumenPagosPolizas() - titulos que se van a desplegar");
        logger.debug("generaXmlResumenPagosPolizas() - seteando titulos que se van a desplegar");
        Titulos titulos;
        titulos = newResumenConHeader.addNewTitulos();

        int tit = 0;
        for (int i = 0; i < tipos_cuotas_sel.length; i++) {
            if (tipos_cuotas_sel[i]) {
                titulos.addTitulo(nombre_titulos[i]);
                tit++;
            }
        }

        if (tit == 0) {
            logger.info("generaXmlResumenPagos() - throw sale del metodo por :" +
                        ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos"));
            String message = ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos.bicevida");
            throw new BiceException(message);
            /*if(tipos_cuotas_pat_pac){
                logger.info("generaXmlResumenPagosPolizas() - throw sale del metodo por :" + ResourceBundleUtil.getProperty("cl.bice.vida.error.patpac"));
                String message = ResourceBundleUtil.getProperty("cl.bice.vida.error.patpac");
                throw new BiceException(message);
            }else{
                logger.info("generaXmlResumenPagos() - throw sale del metodo por :" + ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos"));
                String message = ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos.bicevida");
                throw new BiceException(message);
            }*/
        }

        
        int convenio_seleccionado = 0;
        if (conf != null)
            convenio_seleccionado = conf.getConfirmacion().getConvenio().getCodigo().intValue();
        setConvenios(resumen.getResumenConHeader(), convenio_seleccionado);
        
        logger.debug("generaXmlResumenPagosPolizas() - IDNavegacion["+idNavegacion+"] Convenio seleccionado["+convenio_seleccionado+"]");
        
        if (conf == null)
            newResumenConHeader.setTotalPagar(new BigInteger("0"));
        else
            newResumenConHeader.setTotalPagar(conf.getConfirmacion().getProductosPorPagar().getTotalPorPagar().getEnPesos());

        logger.debug("generaXmlResumenPagosPolizas() - IDNavegacion["+idNavegacion+"] Monto total a pagar["+newResumenConHeader.getTotalPagar()+"]");
        
        resumen.setResumenConHeader(newResumenConHeader);
        logger.debug("generaXmlResumenPagosPolizas() - inicio");
        return resumen;
    }

    /**
     * Metod que genera el XML a enviar
     * pro concepto de resumen y pagos
     * @param rut
     * @param nombre
     * @param conf
     * @return
     */
    public ResumenConHeaderDocument generaXmlResumenPagosAPVAPT(int rut, String nombre,
                                                                ConfirmacionDocument conf) throws BiceException,
                                                                                                  ParseException {
        logger.debug("generaXmlResumenPagosPolizas() - inicio");
        Calendar fecha = Calendar.getInstance();

        Date fecha_uf = fecha.getTime();
        logger.debug("generaXmlResumenPagosPolizas() - recuperando ResumenConHeaderDocument");
        ResumenConHeaderDocument resumen = ResumenConHeaderDocument.Factory.newInstance();
        logger.debug("generaXmlResumenPagosPolizas() - ejecutando addNewResumenConHeader()");
        ResumenConHeaderDocument.ResumenConHeader newResumenConHeader = resumen.addNewResumenConHeader();
        Long idNavegacion = 0L;
        logger.info("generaXmlResumenPagosPolizas() - SETANDO LA INFORMACION GENERAL");
        if (conf != null) {
            logger.debug("generaXmlResumenPagosPolizas() - confirmacion distinto de null");
            logger.debug("generaXmlResumenPagosPolizas() - addNewInfoNavegacion");
            newResumenConHeader.addNewInfoNavegacion();
            idNavegacion = conf.getConfirmacion().getInfoNavegacion().getIDNavegacion().longValue();
            logger.info("generaXmlResumenPagosPolizas() - seteando valores de nevegacion - IDNavegacion["+idNavegacion+"]");
            newResumenConHeader.getInfoNavegacion().setIDNavegacion(conf.getConfirmacion().getInfoNavegacion().getIDNavegacion());
            newResumenConHeader.getInfoNavegacion().setEntrada(conf.getConfirmacion().getInfoNavegacion().getEntrada().add(new BigInteger("1")));
        } else {
            logger.info("generaXmlResumenPagosPolizas() - confirmacion en null");
            setInfoNavegacion(newResumenConHeader, rut, 1);
            logger.info("generaXmlResumenPagosPolizas() - terminando navegacion");
        }

        logger.info("generaXmlResumenPagosPolizas() - seteando datos del clientes (rut["+rut+"], nombre["+nombre+"], fecha consulta["+fecha+"])");
        newResumenConHeader.setRutCliente(rut);
        newResumenConHeader.setNombreCliente(nombre);
        setIdentificacionMensaje(newResumenConHeader);
        newResumenConHeader.setFechaConsulta(fecha);

        BigDecimal valor_uf;
        logger.info("generaXmlResumenPagosPolizas() - seteando el valor de la UF");
        if (conf == null ||
            conf.getConfirmacion().getFechaConsulta().DATE != newResumenConHeader.getFechaConsulta().DATE) {
            valor_uf = setValorUF(fecha_uf);
        } else {
            valor_uf = conf.getConfirmacion().getValorUF();
        }

        Calendar fecha_uf_calendar = Calendar.getInstance();
        fecha_uf_calendar.setTime(fecha_uf);
        newResumenConHeader.setFechaUF(fecha_uf_calendar);
        newResumenConHeader.setValorUF(valor_uf);

        logger.debug("generaXmlResumenPagosPolizas() - buscando si la caja esta habierta para setear el turno");
        AperturaCaja turno = DAOFactory.getPersistenciaGeneralDao().findCajaAbierta();
        if (turno != null) {
            fecha.setTime(turno.getTurno());
            newResumenConHeader.setTurno(fecha);
        }

        logger.debug("generaXmlResumenPagosPolizas() - Copia ProductosPorPagar");
        ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp;
        pp = newResumenConHeader.addNewProductosPorPagar();
        logger.debug("generaXmlResumenPagosPolizas() - seteando productos por pagar setProductosPorPagar()");
        if (conf != null)
            setProductosPorPagarAPVAPT(pp, rut, valor_uf, turno, conf.getConfirmacion().getProductosPorPagar());
        else
            setProductosPorPagarAPVAPT(pp, rut, valor_uf, turno, null);

        logger.debug("generaXmlResumenPagosPolizas() - Copia ProductosPagados");
        ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pPagados;
        pPagados = newResumenConHeader.addNewProductosPagados();

        //setProductosPagadosPolizas(pPagados, rut);


        logger.debug("generaXmlResumenPagosPolizas() - titulos que se van a desplegar");
        logger.debug("generaXmlResumenPagosPolizas() - seteando titulos que se van a desplegar");
        Titulos titulos;
        titulos = newResumenConHeader.addNewTitulos();

        int tit = 0;
        for (int i = 0; i < tipos_cuotas_sel.length; i++) {
            if (tipos_cuotas_sel[i]) {
                titulos.addTitulo(nombre_titulos[i]);
                tit++;
            }
        }

        if (tit == 0) {
            logger.info("generaXmlResumenPagos() - throw sale del metodo por :" +
                        ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos"));
            String message = ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos.bicevida");
            throw new BiceException(message);
            /*if(tipos_cuotas_pat_pac){
                logger.info("generaXmlResumenPagosPolizas() - throw sale del metodo por :" + ResourceBundleUtil.getProperty("cl.bice.vida.error.patpac"));
                String message = ResourceBundleUtil.getProperty("cl.bice.vida.error.patpac");
                throw new BiceException(message);
            }else{
                logger.info("generaXmlResumenPagos() - throw sale del metodo por :" + ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos"));
                String message = ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos.bicevida");
                throw new BiceException(message);
            }*/
        }

        
        int convenio_seleccionado = 0;
        if (conf != null)
            convenio_seleccionado = conf.getConfirmacion().getConvenio().getCodigo().intValue();
        setConvenios(resumen.getResumenConHeader(), convenio_seleccionado);
        
        logger.info("generaXmlResumenPagosPolizas() - IDNavegacion["+idNavegacion+"] - convenio seleccionado ["+convenio_seleccionado+"]");
        
        if (conf == null)
            newResumenConHeader.setTotalPagar(new BigInteger("0"));
        else
            newResumenConHeader.setTotalPagar(conf.getConfirmacion().getProductosPorPagar().getTotalPorPagar().getEnPesos());

        logger.info("generaXmlResumenPagosPolizas() - IDNavegacion["+idNavegacion+"] - monto total a pagar ["+ newResumenConHeader.getTotalPagar() +"]");
        resumen.setResumenConHeader(newResumenConHeader);
        logger.debug("generaXmlResumenPagosPolizas() - inicio");
        return resumen;
    }

    /**
     * Confeccion el resumen y pagos
     * de dividendos
     * @param rut
     * @param nombre
     * @param conf
     * @return
     */
    public ResumenConHeaderDocument generaXmlResumenPagosDividendos(int rut, String nombre,
                                                                    ConfirmacionDocument conf) throws BiceException,
                                                                                                      ParseException {
        logger.debug("generaXmlResumenPagosDividendos() - inicio");
        Calendar fecha = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fechas = formato.format(fecha.getTime());
        Date fecha_uf = formato.parse(fechas);
        logger.debug("generaXmlResumenPagosDividendos() - recuperando ResumenConHeaderDocument");
        ResumenConHeaderDocument resumen = ResumenConHeaderDocument.Factory.newInstance();
        logger.debug("generaXmlResumenPagosDividendos() - ejecutando addNewResumenConHeader()");
        ResumenConHeaderDocument.ResumenConHeader newResumenConHeader = resumen.addNewResumenConHeader();
        Long idNavegacion = 0L;
        logger.debug("generaXmlResumenPagos() - SETANDO LA INFORMACION GENERAL");
        if (conf != null) {
            logger.debug("generaXmlResumenPagosDividendos() - confirmacion distinto de null");
            logger.debug("generaXmlResumenPagosDividendos() - addNewInfoNavegacion");
            newResumenConHeader.addNewInfoNavegacion();
            idNavegacion = conf.getConfirmacion().getInfoNavegacion().getIDNavegacion().longValue();
            logger.info("generaXmlResumenPagosDividendos() - seteando valores de nevegacion - IDNavegacion[" + idNavegacion + "]");
            newResumenConHeader.getInfoNavegacion().setIDNavegacion(conf.getConfirmacion().getInfoNavegacion().getIDNavegacion());
            newResumenConHeader.getInfoNavegacion().setEntrada(conf.getConfirmacion().getInfoNavegacion().getEntrada().add(new BigInteger("1")));
        } else {
            logger.info("generaXmlResumenPagosDividendos() - confirmacion en null");
            setInfoNavegacion(newResumenConHeader, rut, 2);
        }

        logger.debug("generaXmlResumenPagosDividendos() - seteando datos del clientes (rut, nombre, fecha consulta)");
        newResumenConHeader.setRutCliente(rut);
        newResumenConHeader.setNombreCliente(nombre);
        setIdentificacionMensaje(newResumenConHeader);
        newResumenConHeader.setFechaConsulta(fecha);

        BigDecimal valor_uf;
        logger.debug("generaXmlResumenPagosDividendos() - seteando el valor de la UF");
        if (conf == null ||
            conf.getConfirmacion().getFechaConsulta().DATE != newResumenConHeader.getFechaConsulta().DATE) {
            valor_uf = setValorUF(fecha_uf);
        } else {
            valor_uf = conf.getConfirmacion().getValorUF();
        }
        newResumenConHeader.setFechaUF(fecha);
        newResumenConHeader.setValorUF(valor_uf);

        logger.debug("generaXmlResumenPagosDividendos() - buscando si la caja esta abierta para setear el turno");
        AperturaCaja turno = DAOFactory.getPersistenciaGeneralDao().findCajaAbierta();
        if (turno != null) {
            fecha.setTime(turno.getTurno());
            newResumenConHeader.setTurno(fecha);
        }

        logger.debug("generaXmlResumenPagosDividendos() - Copia ProductosPorPagar");
        ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp;
        pp = newResumenConHeader.addNewProductosPorPagar();
        logger.debug("generaXmlResumenPagosDividendos() - seteando productos por pagar setProductosPorPagar()");
        if (conf != null)
            setProductosPorPagarDividendos(pp, rut, valor_uf, turno, conf.getConfirmacion().getProductosPorPagar());
        else
            setProductosPorPagarDividendos(pp, rut, valor_uf, turno, null);

        logger.debug("generaXmlResumenPagosDividendos() - Copia ProductosPagados");
        ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pPagados;
        pPagados = newResumenConHeader.addNewProductosPagados();

        setProductosPagadosDividendos(pPagados, rut);


        logger.debug("generaXmlResumenPagosDividendos() - titulos que se van a desplegar");
        logger.debug("generaXmlResumenPagosDividendos() - seteando titulos que se van a desplegar");
        Titulos titulos;
        titulos = newResumenConHeader.addNewTitulos();

        int tit = 0;
        for (int i = 0; i < tipos_cuotas_sel.length; i++) {
            if (tipos_cuotas_sel[i]) {
                titulos.addTitulo(nombre_titulos[i]);
                tit++;
            }
        }
        if (tit == 0) {
            logger.info("generaXmlResumenPagos() - throw sale del metodo por :" +
                        ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos"));
            String message = ResourceBundleUtil.getProperty("cl.bice.vida.error.sinpagos.bicehipotecaria");
            throw new BiceException(message);
        }

        
        int convenio_seleccionado = 0;
        if (conf != null)
            convenio_seleccionado = conf.getConfirmacion().getConvenio().getCodigo().intValue();
        setConvenios(resumen.getResumenConHeader(), convenio_seleccionado);

        logger.info("generaXmlResumenPagosDividendos() - IDNavegacion["+idNavegacion +"], convenio seleccionado["+convenio_seleccionado+"]");
        
        
        if (conf == null)
            newResumenConHeader.setTotalPagar(new BigInteger("0"));
        else
            newResumenConHeader.setTotalPagar(conf.getConfirmacion().getProductosPorPagar().getTotalPorPagar().getEnPesos());

        logger.info("generaXmlResumenPagosDividendos() - IDNavegacion["+idNavegacion +"] monto total a pagar["+newResumenConHeader.getTotalPagar()+"]");
        
        
        resumen.setResumenConHeader(newResumenConHeader);
        logger.debug("generaXmlResumenPagosDividendos() - inicio");
        return resumen;
    }

    /**
     * Funcion para identificar el mensaje
     * que se esta enviando
     * @param newResumenConHeader
     */
    public void setIdentificacionMensaje(ResumenConHeaderDocument.ResumenConHeader newResumenConHeader) {
        logger.debug("setIdentificacionMensaje() - inicio");
        IdentificacionMensaje idm;
        if (newResumenConHeader.getIdentificacionMensaje() == null)
            idm = newResumenConHeader.addNewIdentificacionMensaje();
        else
            idm = (IdentificacionMensaje) newResumenConHeader.getIdentificacionMensaje().copy();
        idm.setCodigo("RESPAG");
        idm.setVersion("1.0");
        idm.setDe("PAGWEB");
        idm.setFechaCreacion(java.util.Calendar.getInstance());
        idm.setAccion("PAGAR");
        logger.debug("setIdentificacionMensaje() - termino");
    }

    /**
     * Setea informacion de navegacion
     * @param resumen
     * @param rut
     * @param empresa
     */
    public void setInfoNavegacion(ResumenConHeaderDocument.ResumenConHeader resumen, int rut, int empresa) {
        try {
            logger.info("setInfoNavegacion() - inicio");
            InfoNavegacion info = resumen.addNewInfoNavegacion();
            logger.info("setInfoNavegacion() - CREANDO LA NAVEGACION RUT:" + rut + " EMPR:" + empresa + " OBT:" + info);
            Navegacion naver;
            logger.info("setInfoNavegacion() - recuperando navegacion desde DB");
            naver = DAOFactory.getPersistenciaGeneralDao().newNavegacion(rut, empresa);
            if (naver != null) {
                logger.info("setInfoNavegacion() - NAVEGACION OK, RECUPERANDO DATOS..");
                info.setIDNavegacion(new BigInteger(naver.getIdNavegacion().toString()));
                logger.info("setInfoNavegacion() - OK, PROCESANDO CONTINUACION.");
            }
            info.setEntrada(new BigInteger("1"));
            logger.info("setInfoNavegacion() - termino");
        } catch (Exception er) {
            System.out.println("==> ERROR NAVEGACION : " + er.getMessage());
            er.printStackTrace();
        }
    }

    /**
     * Establecer el valor de UF
     * @param fecha
     * @return
     */
    public BigDecimal setValorUF(Date fecha) throws BiceException {
        BigDecimal resp = null;
        logger.debug("setValorUF() - inicio");
        ValorUf valor_uf = DAOFactory.getPersistenciaGeneralDao().findValorUfByDate(fecha);
        if (valor_uf != null) {
            logger.debug("setValorUF() - valor distinto de null, redondeando el valor : " + valor_uf.getValorUf());
            String numero = NumeroUtil.redondear(valor_uf.getValorUf(), 2);
            logger.debug("setValorUF() - termino correctamente con nuevo valor :" + numero);
            resp = new BigDecimal(numero);
            logger.debug("setValorUF() - termino valor generado:" + resp);
        } else {
            logger.error("setValorUF() - Error : No se logra encontrar el valor de la UF del dia");
            throw new BiceException("No se logra encontrar el valor de la UF del dia " + fecha);
        }
        logger.debug("setValorUF() - termino");
        return resp;
    }

    public void setProductosPorPagar(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp, int rut,
                                     BigDecimal valor_uf, AperturaCaja turno,
                                     ConfirmacionDocument.Confirmacion.ProductosPorPagar seleccion) {
    }

    /**
     * Setea los productos a pagar por conceto de polizas
     * @param pp
     * @param rut
     * @param valor_uf
     * @param turno
     * @param seleccion
     */
    public void setProductosPorPagarPolizas(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp, int rut,
                                            BigDecimal valor_uf, AperturaCaja turno,
                                            ConfirmacionDocument.Confirmacion.ProductosPorPagar seleccion) {
        logger.debug("setProductosPorPagarPolizas() - inicio");
        int deuda = 0;
        List polizassel = null;
        List dividendossel = null;

        if (seleccion != null) {
            logger.debug("setProductosPorPagarPolizas() - seleccion distinto de null entra el ciclo");
            polizassel = new ArrayList();
            logger.debug("setProductosPorPagarPolizas() - iniciando iteracion de productos");
            for (int i = 0; i < seleccion.getEntradaArray().length; i++) {
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = seleccion.getEntradaArray(i);
                Object[] data = new Object[4];
                if (entrada.getProducto().getCodEmpresa() == 1) {
                    data[0] = entrada.getProducto().getNumProducto().longValue();
                    data[1] = entrada.getInfoCajas().getPolizas().getRamo().intValue();
                    data[2] = entrada.getInfoCajas().getPolizas().getFolio().intValue();
                    data[3] = entrada.getCuota().getMonto().getEnPesos().intValue();
                    polizassel.add(data);
                }
            }
            if (polizassel.size() == 0)
                polizassel = null;
        }

        if (turno != null) {
            logger.debug("setProductosPorPagarPolizas() - turno distinto de null busco polizas findPolizasByRut()");
            List<IndPrimaNormalVw> polizas = DAOFactory.getPersistenciaGeneralDao().findPolizasByRut(rut);

            /**
             * LO DE APV YA NO VA, POR QUE LA LISTA DE RESUMEN Y PAGOS DEVUELVE TODO.
            if (polizas == null) polizas = new ArrayList();
            List<IndPrimaNormalVw> dtosApv = DAOFactory.getPersistenciaGeneralDao().findAPVByRut(rut);
            if (dtosApv == null) dtosApv = new ArrayList();
            Iterator pols = dtosApv.iterator();
            while (pols.hasNext()) {
                IndPrimaNormalVw pVO = (IndPrimaNormalVw) pols.next();

                boolean existe = false;
                Iterator firs = polizas.iterator();
                while (firs.hasNext()) {
                    IndPrimaNormalVw oVO = (IndPrimaNormalVw)  firs.next();
                    if (oVO.getRamo().intValue() == pVO.getRamo().intValue() &&
                        oVO.getPolizaPol().intValue() == pVO.getPolizaPol().intValue() &&
                        oVO.getFolioRecibo().intValue() == pVO.getFolioRecibo().intValue()) {
                        existe = true;
                        break;
                    }
                }

                if (existe == false) {
                    polizas.add(pVO);
                }
            }
            */

            if (polizas != null && polizas.size() > 0) {
                logger.debug("setProductosPorPagarPolizas() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada)");
                logger.debug("setProductosPorPagarPolizas() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada) - SetPolizas()");
                tipos_cuotas_sel[3] = true;
                deuda += SetPolizas(pp, valor_uf, polizas, polizassel);
            }

            /*List polizaspatpac = DAOFactory.getPersistenciaGeneralDao().findPolizasByRutPatPac(rut);
            if (polizaspatpac != null && polizaspatpac.size() > 0) {
                tipos_cuotas_pat_pac = true;
            }*/


        } else {
            logger.info("setProductosPorPagarPolizas() - turno es igual a null, buscando polizas Offline - findPolizasOfflineByRut()");
            List polizas = DAOFactory.getPersistenciaGeneralDao().findPolizasOfflineByRut(rut);
            if (polizas != null && polizas.size() > 0) {
                logger.info("setProductosPorPagarPolizas() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada)");
                tipos_cuotas_sel[3] = true;
                logger.info("setProductosPorPagarPolizas() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada) - SetPolizas()");
                deuda += SetPolizas(pp, valor_uf, polizas, polizassel);
            }
        }

        pp.setTotalPorPagar(new BigInteger(Integer.toString(monto_pagar)));
        pp.setTotalDeuda(new BigInteger(Integer.toString(deuda)));
        logger.debug("setProductosPorPagarPolizas() - termino");

    }


    /**
     * Setea los productos a pagar por conceto de polizas
     * @param pp
     * @param rut
     * @param valor_uf
     * @param turno
     * @param seleccion
     */
    public void setProductosPorPagarAPVAPT(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp, int rut,
                                           BigDecimal valor_uf, AperturaCaja turno,
                                           ConfirmacionDocument.Confirmacion.ProductosPorPagar seleccion) {
        logger.debug("setProductosPorPagarPolizas() - inicio");
        int deuda = 0;
        List polizassel = null;
        List dividendossel = null;

        if (seleccion != null) {
            logger.debug("setProductosPorPagarPolizas() - seleccion distinto de null entra el ciclo");
            polizassel = new ArrayList();
            logger.debug("setProductosPorPagarPolizas() - iniciando iteracion de productos");
            for (int i = 0; i < seleccion.getEntradaArray().length; i++) {
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = seleccion.getEntradaArray(i);
                Object[] data = new Object[4];
                if (entrada.getProducto().getCodEmpresa() == 1) {
                    data[0] = entrada.getProducto().getNumProducto().longValue();
                    data[1] = entrada.getInfoCajas().getPolizas().getRamo().intValue();
                    data[2] = entrada.getInfoCajas().getPolizas().getFolio().intValue();
                    data[3] = entrada.getCuota().getMonto().getEnPesos().intValue();
                    polizassel.add(data);
                }
            }
            if (polizassel.size() == 0)
                polizassel = null;
        }

        if (turno != null) {
            logger.debug("setProductosPorPagarPolizas() - turno distinto de null busco polizas findPolizasByRut()");
            List polizas = DAOFactory.getPersistenciaGeneralDao().findAPVAPTByRut(rut);
            if (polizas != null && polizas.size() > 0) {
                logger.debug("setProductosPorPagarPolizas() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada)");
                logger.debug("setProductosPorPagarPolizas() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada) - SetPolizas()");
                tipos_cuotas_sel[3] = true;
                deuda += SetAPVAPT(pp, valor_uf, polizas, polizassel);
            }

            /*List polizaspatpac = DAOFactory.getPersistenciaGeneralDao().findPolizasByRutPatPac(rut);
            if (polizaspatpac != null && polizaspatpac.size() > 0) {
                tipos_cuotas_pat_pac = true;
            }*/


        } else {
            logger.info("setProductosPorPagarPolizas() - turno es igual a null, buscando polizas Offline - findPolizasOfflineByRut()");
            List polizas = DAOFactory.getPersistenciaGeneralDao().findPolizasOfflineByRut(rut);
            if (polizas != null && polizas.size() > 0) {
                logger.info("setProductosPorPagarPolizas() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada)");
                tipos_cuotas_sel[3] = true;
                logger.info("setProductosPorPagarPolizas() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada) - SetPolizas()");
                deuda += SetAPVAPT(pp, valor_uf, polizas, polizassel);
            }
        }

        pp.setTotalPorPagar(new BigInteger(Integer.toString(monto_pagar)));
        pp.setTotalDeuda(new BigInteger("0"));
        logger.debug("setProductosPorPagarPolizas() - termino");

    }

    /**
     * Setea productos a pagar con concepto de
     * de dividendos
     * @param pp
     * @param rut
     * @param valor_uf
     * @param turno
     * @param seleccion
     */
    public void setProductosPorPagarDividendos(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp, int rut,
                                               BigDecimal valor_uf, AperturaCaja turno,
                                               ConfirmacionDocument.Confirmacion.ProductosPorPagar seleccion) {
        logger.debug("setProductosPorPagarDividendos() - inicio");
        int deuda = 0;
        List polizassel = null;
        List dividendossel = null;

        if (seleccion != null) {
            logger.debug("setProductosPorPagarDividendos() - seleccion distinto de null entra el ciclo");
            dividendossel = new ArrayList();
            logger.debug("setProductosPorPagarDividendos() - iniciando iteracion de productos");
            for (int i = 0; i < seleccion.getEntradaArray().length; i++) {
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = seleccion.getEntradaArray(i);
                Object[] data = new Object[4];
                if (entrada.getProducto().getCodEmpresa() != 1) {
                    data[0] = entrada.getProducto().getNumProducto().longValue();
                    data[1] = entrada.getInfoCajas().getCreditos().getNumDividendo().intValue();
                    data[2] = entrada.getCuota().getMonto().getEnUF().doubleValue();
                    data[3] = entrada.getCuota().getMonto().getEnPesos().intValue();
                    dividendossel.add(data);
                }
            }
            if (dividendossel.size() == 0)
                dividendossel = null;
        }

        if (turno != null) {
            logger.debug("setProductosPorPagarDividendos() - consultando dividendos findDividendosByRut()");
            List dividendos = DAOFactory.getPersistenciaGeneralDao().findDividendosByRut(Integer.toString(rut));
            if (dividendos != null && dividendos.size() > 0) {
                logger.debug("setProductosPorPagarDividendos() - seteo valor de dividendo encontrados - setDividendos()");
                deuda += setDividendos(pp, valor_uf, dividendos, dividendossel);
            }
        } else {

            logger.info("setProductosPorPagarDividendos() - buscando dividendos findDividendosOfflineByRut()");
            List dividendos = DAOFactory.getPersistenciaGeneralDao().findDividendosOfflineByRut(Integer.toString(rut));
            if (dividendos != null && dividendos.size() > 0) {
                logger.debug("setProductosPorPagarDividendos() - seteo valor de dividendo encontrados - setDividendos()");
                deuda += setDividendos(pp, valor_uf, dividendos, dividendossel);
            }
        }

        pp.setTotalPorPagar(new BigInteger(Integer.toString(monto_pagar)));
        pp.setTotalDeuda(new BigInteger(Integer.toString(deuda)));
        logger.debug("setProductosPorPagarDividendos() - termino");
    }

    /**
     * Genera los dividendos
     * @param pp
     * @param valor_uf
     * @param dividendos
     * @param seleccion
     * @return monto de la deudad
     */
    public int setDividendos(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp, BigDecimal valor_uf,
                             List dividendos, List seleccion) {
        logger.debug("setDividendos() - inicio");
        long num_producto = 0;
        int deuda = 0;

        for (int i = 0; i < dividendos.size(); i++) {
            int porpagar = 0;
            ++contador;
            ProductosPorPagar.Entrada entrada = pp.addNewEntrada();

            entrada.setContador(new BigDecimal(contador).unscaledValue());

            logger.debug("setDividendos() - buscando dividendos");
            BhDividendosVw dividendo;
            if (dividendos.get(i) instanceof BhDividendosVw) {
                logger.debug("setDividendos() - recuperando dividendos de vista BhDividendosVw");
                dividendo = (BhDividendosVw) dividendos.get(i);
            } else {
                logger.debug("setDividendos() - recuperando dividendos de vista BhDividendosOfflineVw");
                dividendo = ((BhDividendosOfflineVw) dividendos.get(i)).toBhDividendosVw();
            }

            logger.debug("setDividendos() - Seteo cuando un dividendo ha sido seleccionado o no");
            logger.debug("setDividendos() - Dependiendo de eso y del valor anterior es que se ve la necesidad de habilitar");
            logger.debug("setDividendos() - o no el divideno para ser pagado");
            entrada.setSeleccionado(false);
            entrada.setDisabled(true);
            Object[] data = null;
            if (seleccion != null) {
                for (int j = 0; j < seleccion.size(); j++) {
                    data = (Object[]) seleccion.get(j);
                    if (((Long) data[0]).longValue() == dividendo.getNumOpe().longValue() &&
                        ((Integer) data[1]).intValue() == dividendo.getNumDiv().intValue()) {
                        entrada.setSeleccionado(true);
                        entrada.setDisabled(false);
                        num_producto = 0;
                        break;
                    } else
                        data = null;
                }
            }
            if (data == null && num_producto == 0) {
                num_producto = dividendo.getNumOpe().longValue();
                entrada.setDisabled(false);
            } else if (num_producto != 0 && num_producto != dividendo.getNumOpe().longValue()) {
                num_producto = dividendo.getNumOpe().longValue();
                entrada.setDisabled(false);

            }


            logger.debug("setDividendos() - INFORMACION DEL PRODUCTO");
            logger.debug("setDividendos() - seteando informacion del producto");
            ProductosPorPagar.Entrada.Producto producto = entrada.addNewProducto();
            producto.setCodigoProducto(new BigDecimal(dividendo.getCodProducto()).unscaledValue());

            if (dividendo.getCodProducto().intValue() == 3021 || dividendo.getCodProducto().intValue() == 3022) {
                producto.setDescripcionProducto(dividendo.getProducto() + " " + dividendo.getNumDiv());
            } else {
                producto.setDescripcionProducto(dividendo.getProducto() + " " + dividendo.getNumDiv() + "/" +
                                                dividendo.getPlazo_mes());
            }

            //producto.setDescripcionProducto(dividendo.getProducto()+" "+dividendo.getNumDiv()+"/"+dividendo.getPlazo_mes());
            producto.setNumProducto(new BigDecimal(dividendo.getNumOpe()).unscaledValue());
            producto.setCodEmpresa(dividendo.getCodEmpresa());
            producto.setTipoCuota(dividendo.getCodMecanismo());

            logger.debug("setDividendos() - INFORMACION UTIL PARA CAJAS");
            logger.debug("setDividendos() - seteando informacion util para cajas");
            Infocajas.Creditos infocredito = entrada.addNewInfoCajas().addNewCreditos();
            infocredito.setNumDividendo(new BigInteger(dividendo.getNumDiv().toString()));

            logger.debug("setDividendos() - CUOTA    ");
            logger.debug("setDividendos() - seteando cuota");
            ProductosPorPagar.Entrada.Cuota cuota = entrada.addNewCuota();

            Calendar calendario = Calendar.getInstance();
            calendario.setTime(dividendo.getFecVencimiento());

            cuota.setFechaVencimiento(calendario);
            logger.debug("setDividendos() - Medio por el cual se realizo el pago o se va a realizar, caso PAC o PAT");
            cuota.setMedioPago("");


            if (dividendo.getSucursalPago() != null &&
                (dividendo.getSucursalPago() ==
                 Integer.parseInt(ResourceBundleUtil.getProperty("dividendo.codigo.pac")) ||
                 dividendo.getSucursalPago() ==
                 Integer.parseInt(ResourceBundleUtil.getProperty("dividendo.codigo.pat")))) {
                ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota.InfoPA infoPA =
                    cuota.addNewInfoPA();
                logger.debug("setDividendos() - buscando valor con findValorUfByDate()");
                ValorUf valoruf =
                    DAOFactory.getPersistenciaGeneralDao().findValorUfByDate(cuota.getFechaVencimiento().getTime());
                if (valoruf != null) {
                    String val = NumeroUtil.redondear(valoruf.getValorUf(), 2);
                    infoPA.setValorUF(new BigDecimal(val));
                } else
                    infoPA.setValorUF(new BigDecimal("0"));
                calendario.setTime(dividendo.getFechaPATPAC());
                infoPA.setFechaPago(calendario);
                if (dividendo.getSucursalPago() ==
                    Integer.parseInt(ResourceBundleUtil.getProperty("dividendo.codigo.pac")))
                    cuota.setMedioPago("PAC");
                else
                    cuota.setMedioPago("PAT");
            }

            long valor_cuota;
            DetalleCargos detalle;
            if (dividendo.getCodMecanismo() == 1) {
                logger.info("setDividendos() - PRIMA NORMAL");
                logger.debug("setDividendos() - seteo que se debe mostrar el titulo en la posicion 0 (Normal)");
                logger.debug("setDividendos() - buscando valor con findValorUfByDate()");
                tipos_cuotas_sel[0] = true;

                logger.info("setDividendos() - Valor total MDA en UF: " + dividendo.getTotMda());
                valor_cuota = Math.round(dividendo.getTotMda() * valor_uf.doubleValue());
                logger.info("setDividendos() - Valor total MDA en $: " + valor_cuota);


                MontoCuota cuota_fija = cuota.addNewCuotaFija();
                cuota_fija.setEnUF(new BigDecimal(dividendo.getTotMda().toString()));
                BigDecimal valor = new BigDecimal(valor_cuota);
                cuota_fija.setEnPesos(valor.unscaledValue());

                logger.debug("setDividendos() - aumentando el valor del monto pagado");
                if (data != null) {
                    monto_pagar += valor.intValue();
                    porpagar = valor.intValue();
                }

                logger.debug("setDividendos() - instancio detalle de los cargos");
                detalle = cuota_fija.addNewDetalle();
            } else {
                logger.info("setDividendos() - LIBRE O FULL ELECCION");
                logger.debug("setDividendosv() - seteo que se debe mostrar el titulo en la posicion 1 (Min)");
                tipos_cuotas_sel[1] = true;

                logger.info("setDividendos() - Valor total MDA en UF: " + dividendo.getTotMda());
                valor_cuota = Math.round(dividendo.getTotMda() * valor_uf.doubleValue());
                logger.info("setDividendos() - Valor total MDA en $: " + valor_cuota);


                logger.debug("setDividendos() - CUOTA OPCION");
                ProductosPorPagar.Entrada.Cuota.CuotaOpcion cuotaOpcion = cuota.addNewCuotaOpcion();

                logger.debug("setDividendos() - CUOTA MIN");
                ProductosPorPagar.Entrada.Cuota.CuotaOpcion.CuotaMin cuotaOpcionMin = cuotaOpcion.addNewCuotaMin();
                BigDecimal valor = new BigDecimal(valor_cuota);
                cuotaOpcionMin.setEnPesos(valor.unscaledValue());
                cuotaOpcionMin.setEnUF(new BigDecimal(dividendo.getTotMda().toString()));

                if (data != null) {
                    if (dividendo.getCodMecanismo() == 4) {
                        logger.info("setDividendos() - Libre Eleccion");
                        if (dividendo.getTotMda().doubleValue() == ((Double) data[2]).doubleValue()) {
                            monto_pagar += valor.intValue();
                            porpagar = valor.intValue();
                            cuotaOpcionMin.setSeleccionado(true);
                        }
                    } else {
                        logger.info("setDividendos() - Full Eleccion");
                        if (valor_cuota <= (Integer) data[3]) {
                            monto_pagar += ((Integer) data[3]).intValue();
                            porpagar = ((Integer) data[3]).intValue();
                        } else {
                            monto_pagar += valor.intValue();
                            porpagar = valor.intValue();
                        }
                    }
                }


                logger.debug("setDividendos() - instancio detalle de los cargos");
                detalle = cuotaOpcionMin.addNewDetalle();

                if (i + 1 < dividendos.size()) {
                    BhDividendosVw dividendo_max = (BhDividendosVw) dividendos.get(i + 1);
                    if (dividendo_max.getNumOpe().longValue() == dividendo.getNumOpe().longValue() &&
                        dividendo_max.getNumDiv().intValue() == dividendo.getNumDiv().intValue()) {
                        logger.debug("setDividendos() - seteo que se debe mostrar el titulo en la posicion 2 (Max)");
                        tipos_cuotas_sel[2] = true;

                        logger.debug("setDividendos() - CUOTA MAX");
                        ProductosPorPagar.Entrada.Cuota.CuotaOpcion.CuotaMax cuotaOpcionMax =
                            cuotaOpcion.addNewCuotaMax();
                        long valor_cuota_max = Math.round(dividendo_max.getTotMda() * valor_uf.doubleValue());

                        BigDecimal valor_max = new BigDecimal(valor_cuota_max);
                        cuotaOpcionMax.setEnPesos(valor_max.unscaledValue());
                        cuotaOpcionMax.setEnUF(new BigDecimal(dividendo_max.getTotMda().toString()));

                        if (data != null) {
                            logger.info("setDividendos() - Libre Eleccion");
                            if (dividendo.getCodMecanismo() == 4) {
                                if (dividendo_max.getTotMda().doubleValue() == ((Double) data[2]).doubleValue()) {
                                    monto_pagar += valor_max.intValue();
                                    porpagar = valor_max.intValue();
                                    cuotaOpcionMax.setSeleccionado(true);
                                } else if (!cuotaOpcionMin.getSeleccionado()) {
                                    monto_pagar += valor.intValue();
                                    porpagar = valor.intValue();
                                    cuotaOpcionMin.setSeleccionado(true);
                                }
                            }
                        }

                        logger.debug("setDividendos() - Detalle");
                        DetalleCargos detalleMax = cuotaOpcionMax.addNewDetalle();
                        DetalleCargos.Cargo cargoMax;
                        List detalleDividendoMax = dividendo_max.getDetalleDividendo();
                        for (int j = 0; j < detalleDividendoMax.size(); j++) {
                            Object[] det = (Object[]) detalleDividendoMax.get(j);
                            cargoMax = detalleMax.addNewCargo();
                            cargoMax.setDescripcion(det[0].toString());
                            cargoMax.setBigDecimalValue(new BigDecimal(det[1].toString()));
                            logger.debug("setDividendos() - detalle.setCargoArray(0, cargo);");
                        }
                        i = i + 1;
                    }
                }
            }
            logger.debug("setDividendos() - DETALLE DE LOS CARGOS");
            DetalleCargos.Cargo cargo;
            List detalleDividendo = dividendo.getDetalleDividendo();
            for (int j = 0; j < detalleDividendo.size(); j++) {
                Object[] det = (Object[]) detalleDividendo.get(j);
                cargo = detalle.addNewCargo();
                cargo.setDescripcion(det[0].toString());
                cargo.setBigDecimalValue(new BigDecimal(det[1].toString()));
            }

            if (porpagar == 0)
                deuda += valor_cuota;
            else
                deuda += porpagar;
            cuota.setPago(new BigInteger(Integer.toString(porpagar)));
            setEstadoCuotaDividendos(cuota);
        }
        logger.debug("setDividendos() - termino");
        return deuda;

    }

    /**
     * Setea las polizas
     * @param pp
     * @param valor_uf
     * @param polizas
     * @param seleccion
     * @return
     */
    public int SetPolizas(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp, BigDecimal valor_uf,
                          List polizas, List seleccion) {
        logger.debug("SetPolizas() - inicio");
        long num_producto = 0;
        int deuda = 0;
        for (int i = 0; i < polizas.size(); i++) {
            int porpagar = 0;
            ++contador;
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada entrada = pp.addNewEntrada();

            entrada.setContador(new BigDecimal(contador).unscaledValue());

            IndPrimaNormalVw poliza;
            if (polizas.get(i) instanceof IndPrimaNormalVw) {
                poliza = (IndPrimaNormalVw) polizas.get(i);
            } else {
                poliza = ((IndPrimaNormalOfflineVw) polizas.get(i)).toIndPrimaNormalVw();
            }

            entrada.setSeleccionado(false);
            entrada.setDisabled(true);
            Object[] data = null;
            if (seleccion != null) {
                for (int j = 0; j < seleccion.size(); j++) {
                    data = (Object[]) seleccion.get(j);
                    if (((Long) data[0]).longValue() == poliza.getPolizaPol().longValue() &&
                        ((Integer) data[1]).intValue() == poliza.getRamo().intValue() &&
                        ((Integer) data[2]).intValue() == poliza.getFolioRecibo().intValue()) {
                        entrada.setSeleccionado(true);
                        entrada.setDisabled(false);
                        num_producto = 0;
                        break;
                    } else
                        data = null;
                }
            }

            if (data == null && num_producto == 0) {
                num_producto = poliza.getPolizaPol().longValue();
                entrada.setDisabled(false);
            } else if (num_producto != 0 && num_producto != poliza.getPolizaPol().longValue()) {
                num_producto = poliza.getPolizaPol().longValue();
                entrada.setDisabled(false);

            }

            logger.debug("SetPolizas() - INFORMACION DEL PRODUCTO");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Producto producto =
                entrada.addNewProducto();
            producto.setCodigoProducto(new BigDecimal(poliza.getCodProducto()).unscaledValue());
            producto.setDescripcionProducto(poliza.getNombre());
            producto.setNumProducto(new BigDecimal(poliza.getPolizaPol()).unscaledValue());
            producto.setCodEmpresa(poliza.getCodEmpresa());
            producto.setTipoCuota(poliza.getCodMecanismo());

            logger.debug("SetPolizas() - INFORMACION UTIL PARA CAJAS");
            Infocajas.Polizas infopoliza = entrada.addNewInfoCajas().addNewPolizas();
            infopoliza.setFolio(new BigInteger(poliza.getFolioRecibo().toString()));
            infopoliza.setRamo(new BigInteger(poliza.getRamo().toString()));


            logger.debug("SetPolizas() - CUOTA    ");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota cuota = entrada.addNewCuota();

            Calendar calendario = Calendar.getInstance();
            calendario.setTime(poliza.getFInicioRecibo());

            cuota.setFechaVencimiento(calendario);
            cuota.setMedioPago("");

            logger.debug("SetPolizas() - INFORMACION DEL PAGO AUTOMATICO: |" + poliza.getViapago() + "|");

            if (poliza.getViapago().intValue() ==
                Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.pac")) ||
                poliza.getViapago().intValue() ==
                Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.pat"))) {
                ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota.InfoPA infoPA =
                    cuota.addNewInfoPA();
                ValorUf valoruf =
                    DAOFactory.getPersistenciaGeneralDao().findValorUfByDate(cuota.getFechaVencimiento().getTime());
                if (valoruf != null) {
                    String valorn = NumeroUtil.redondear(valoruf.getValorUf(), 2);
                    infoPA.setValorUF(new BigDecimal(valorn));
                } else
                    infoPA.setValorUF(new BigDecimal("0"));
                infoPA.setFechaPago(cuota.getFechaVencimiento());
                //entrada.setSeleccionado(false);
                //entrada.setDisabled(true);
                //num_producto=0;

                //if(poliza.getViapago().intValue()==Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.pac"))) cuota.setMedioPago("PAC");
                //else cuota.setMedioPago("PAT");
            }


            long valor_cuota;
            Double valor_cuotaUF;
            if (poliza.getCodMecanismo() == 2) {
                logger.debug("SetPolizas() - POLIZA NORMAL");


                if (poliza.getReajuste() != null && poliza.getMulta() != null && poliza.getInteres() != null &&
                    poliza.getCostosCobranza() !=
                    null) {
                    // Caso producto APV: Suma de resto de los parametros en caso de APV
                    valor_cuota =
                        poliza.getPrimaBrutaPesosRecibo().longValue() + poliza.getReajuste().longValue() +
                        poliza.getMulta().longValue() + poliza.getInteres().longValue() +
                        poliza.getCostosCobranza().longValue();
                    valor_cuotaUF =
                        poliza.getPrimaBrutaUfRecibo() + (poliza.getReajuste() / poliza.getValorCambioUF()) +
                        (poliza.getMulta() / poliza.getValorCambioUF()) +
                        (poliza.getInteres() / poliza.getValorCambioUF()) +
                        (poliza.getCostosCobranza() / poliza.getValorCambioUF());
                    valor_cuotaUF = NumeroUtil.redondeartodouble(valor_cuotaUF.doubleValue(), 4);
                } else {
                    //Caso poliza normal
                    valor_cuota = poliza.getPrimaBrutaPesosRecibo().longValue();
                    valor_cuotaUF = poliza.getPrimaBrutaUfRecibo();
                }


                MontoCuota cuota_fija = cuota.addNewCuotaFija();

                cuota_fija.setEnUF(new BigDecimal(valor_cuotaUF.toString()));
                BigDecimal valor = new BigDecimal(valor_cuota);
                cuota_fija.setEnPesos(valor.unscaledValue());

                logger.debug("SetPolizas() - aumentando el valor del monto pagado");
                if (data != null) {
                    monto_pagar += valor.intValue();
                    porpagar = valor.intValue();
                }

                logger.debug("SetPolizas() - Detalle de los cargos");
                DetalleCargos detalle = cuota_fija.addNewDetalle();
                DetalleCargos.Cargo cargo;

                if (poliza.getReajuste() != null && poliza.getMulta() != null && poliza.getInteres() != null &&
                    poliza.getCostosCobranza() !=
                    null) {
                    // Caso producto APV: Se agrega el resto de los detalles

                    if (poliza.getViapago().intValue() ==
                        Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.planilla"))) {
                        cargo = detalle.addNewCargo();
                        cargo.setDescripcion("ViaPago");
                        cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                        detalle.setCargoArray(0, cargo);
                        cargo = detalle.addNewCargo();
                        cargo.setDescripcion("Secuencia");
                        cargo.setBigDecimalValue(new BigDecimal(poliza.getSecuencia().toString()));
                        detalle.setCargoArray(1, cargo);
                    } else {
                        cargo = detalle.addNewCargo();
                        cargo.setDescripcion("ViaPago");
                        cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                        detalle.setCargoArray(0, cargo);
                        cargo = detalle.addNewCargo();
                        cargo.setDescripcion("Secuencia");
                        cargo.setBigDecimalValue(new BigDecimal("0"));
                        detalle.setCargoArray(1, cargo);
                    }

                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Prima Neta");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getPrimaNetaUfRecibo().toString()));
                    detalle.setCargoArray(2, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("IVA");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getIvaUfRecibo().toString()));
                    detalle.setCargoArray(3, cargo);

                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Reajuste");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getReajuste().toString()));
                    detalle.setCargoArray(4, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Multa");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getMulta().toString()));
                    detalle.setCargoArray(5, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Interes");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getInteres().toString()));
                    detalle.setCargoArray(6, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Costos De Cobranza");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getCostosCobranza().toString()));
                    detalle.setCargoArray(7, cargo);


                } else {

                    if (poliza.getViapago().intValue() ==
                        Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.planilla"))) {
                        cargo = detalle.addNewCargo();
                        cargo.setDescripcion("ViaPago");
                        cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                        detalle.setCargoArray(0, cargo);
                        cargo = detalle.addNewCargo();
                        cargo.setDescripcion("Secuencia");
                        //cargo.setBigDecimalValue(new BigDecimal(poliza.getSecuencia().toString()));
                        cargo.setBigDecimalValue(new BigDecimal("0"));
                        detalle.setCargoArray(1, cargo);
                    } else {
                        cargo = detalle.addNewCargo();
                        cargo.setDescripcion("ViaPago");
                        cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                        detalle.setCargoArray(0, cargo);
                        cargo = detalle.addNewCargo();
                        cargo.setDescripcion("Secuencia");
                        cargo.setBigDecimalValue(new BigDecimal("0"));
                        detalle.setCargoArray(1, cargo);
                    }

                    //Caso poliza normal
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Prima Neta");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getPrimaNetaUfRecibo().toString()));
                    detalle.setCargoArray(2, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("IVA");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getIvaUfRecibo().toString()));
                    detalle.setCargoArray(3, cargo);

                }

            } else {
                //logger.info("SetPolizas() - POLIZA CON EXCEDENTE");
                logger.debug("SetPolizas() - CUOTA VARIABLE");
                ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota.CuotaVariable cuotaVariable =
                    cuota.addNewCuotaVariable();

                valor_cuota = poliza.getPrimaBrutaPesosRecibo().longValue();

                logger.debug("SetPolizas() - CUOTA FIJA");
                MontoCuota cuota_fija = cuotaVariable.addNewCuotaFija();
                cuota_fija.setEnUF(new BigDecimal(poliza.getPrimaBrutaUfRecibo().toString()));
                BigDecimal valor = new BigDecimal(valor_cuota);
                cuota_fija.setEnPesos(valor.unscaledValue());

                logger.debug("SetPolizas() - Detalle de los cargos");
                DetalleCargos detalle = cuota_fija.addNewDetalle();
                DetalleCargos.Cargo cargo;

                if (poliza.getViapago().intValue() ==
                    Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.planilla"))) {
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("ViaPago");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                    detalle.setCargoArray(0, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Secuencia");
                    //cargo.setBigDecimalValue(new BigDecimal(poliza.getSecuencia().toString()));
                    cargo.setBigDecimalValue(new BigDecimal("0"));
                    detalle.setCargoArray(1, cargo);
                } else {
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("ViaPago");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                    detalle.setCargoArray(0, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Secuencia");
                    cargo.setBigDecimalValue(new BigDecimal("0"));
                    detalle.setCargoArray(1, cargo);
                }


                cargo = detalle.addNewCargo();
                cargo.setDescripcion("Prima Neta");
                cargo.setBigDecimalValue(new BigDecimal(poliza.getPrimaNetaUfRecibo().toString()));
                detalle.setCargoArray(2, cargo);
                cargo = detalle.addNewCargo();
                cargo.setDescripcion("IVA");
                cargo.setBigDecimalValue(new BigDecimal(poliza.getIvaUfRecibo().toString()));
                detalle.setCargoArray(3, cargo);

                logger.debug("SetPolizas() - aumentando el valor del monto pagado segun lo seleccionado en la primera pantalla");
                if (data != null) {
                    monto_pagar += ((Integer) data[3]).intValue();
                    porpagar = ((Integer) data[3]).intValue();
                }

                logger.debug("SetPolizas() - CUOTA VARIABLE");
                /* valor_cuota += valor_excedente
                 * MontoCuota cuotaVFija = cuotaVariable.addNewCuotaVariable();
                 cuotaVFija.setEnPesos(new BigInteger("23"));
                 cuotaVFija.setEnUF(new BigDecimal("2.30"));
                 /*DetalleCargos detalle3 = cuotaVFija.addNewDetalle();
                 DetalleCargos.Cargo cargo3 = detalle3.addNewCargo();
                 cargo3.setDescripcion("desc");
                 detalle3.setCargoArray(0, cargo3);
                 cuotaVFija.setDetalle(detalle3);*/
                //logger.info("SetPolizas() - cuota.setCuotaFija(cuotaVFija);");
                //logger.info("SetPolizas() - cuota.setCuotaVariable(cuotaVariable);");

            }

            if (porpagar == 0)
                deuda += valor_cuota;
            else
                deuda += porpagar;
            cuota.setPago(new BigInteger(Integer.toString(porpagar)));
            setEstadoCuotaPolizas(cuota);
        }
        logger.debug("SetPolizas() - termino");
        return deuda;
    }


    /**
     * Setea las polizas
     * @param pp
     * @param valor_uf
     * @param polizas
     * @param seleccion
     * @return
     */
    public int SetAPVAPT(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp, BigDecimal valor_uf,
                         List polizas, List seleccion) {
        logger.debug("SetPolizas() - inicio");
        long num_producto = 0;
        int deuda = 0;
        for (int i = 0; i < polizas.size(); i++) {
            int porpagar = 0;
            ++contador;
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada entrada = pp.addNewEntrada();
            entrada.setContador(new BigDecimal(contador).unscaledValue());
            IndPrimaNormalVw poliza = (IndPrimaNormalVw) polizas.get(i);

            entrada.setSeleccionado(false);
            entrada.setDisabled(true);
            Object[] data = null;
            if (seleccion != null) {
                for (int j = 0; j < seleccion.size(); j++) {
                    data = (Object[]) seleccion.get(j);
                    if (((Long) data[0]).longValue() == poliza.getPolizaPol().longValue() &&
                        ((Integer) data[1]).intValue() == poliza.getRamo().intValue() &&
                        ((Integer) data[2]).intValue() == poliza.getFolioRecibo().intValue()) {
                        entrada.setSeleccionado(true);
                        entrada.setDisabled(false);
                        num_producto = 0;
                        break;
                    } else
                        data = null;
                }
            }

            if (data == null && num_producto == 0) {
                num_producto = poliza.getPolizaPol().longValue();
                entrada.setDisabled(false);
            } else if (num_producto != 0 && num_producto != poliza.getPolizaPol().longValue()) {
                num_producto = poliza.getPolizaPol().longValue();
                entrada.setDisabled(false);

            }

            logger.debug("SetPolizas() - INFORMACION DEL PRODUCTO");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Producto producto =
                entrada.addNewProducto();
            producto.setCodigoProducto(new BigDecimal(poliza.getCodProducto()).unscaledValue());
            producto.setDescripcionProducto(poliza.getNombre());
            producto.setNumProducto(new BigDecimal(poliza.getPolizaPol()).unscaledValue());
            producto.setCodEmpresa(poliza.getCodEmpresa());
            producto.setTipoCuota(poliza.getCodMecanismo());

            logger.debug("SetPolizas() - INFORMACION UTIL PARA CAJAS");
            Infocajas.Polizas infopoliza = entrada.addNewInfoCajas().addNewPolizas();
            infopoliza.setFolio(new BigInteger(poliza.getFolioRecibo().toString()));
            infopoliza.setRamo(new BigInteger(poliza.getRamo().toString()));


            logger.debug("SetPolizas() - CUOTA    ");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota cuota = entrada.addNewCuota();

            Calendar calendario = Calendar.getInstance();
            calendario.setTime(poliza.getFTerminoRecibo());

            cuota.setFechaVencimiento(calendario);
            cuota.setMedioPago("");

            logger.debug("SetPolizas() - INFORMACION DEL PAGO AUTOMATICO: |" + poliza.getViapago() + "|");


            //PEGA INFO PA
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota.InfoPA infoPA =
                cuota.addNewInfoPA();
            ValorUf valoruf =
                DAOFactory.getPersistenciaGeneralDao().findValorUfByDate(cuota.getFechaVencimiento().getTime());
            if (valoruf != null) {
                String valorn = NumeroUtil.redondear(valoruf.getValorUf(), 2);
                infoPA.setValorUF(new BigDecimal(valorn));
            } else
                infoPA.setValorUF(new BigDecimal("0"));
            infoPA.setFechaPago(cuota.getFechaVencimiento());


            long valor_cuota;
            Double valor_cuotaUF;

            //TODO: NO VA EL CALCULO DE MULTAS E INTERESES


            logger.debug("SetPolizas() - POLIZA NORMAL");
            if (poliza.getReajuste() != null && poliza.getMulta() != null && poliza.getInteres() != null &&
                poliza.getCostosCobranza() !=
                null) {
                // Caso producto APV: Suma de resto de los parametros en caso de APV
                valor_cuota =
                    poliza.getPrimaBrutaPesosRecibo().longValue() + poliza.getReajuste().longValue() +
                    poliza.getMulta().longValue() + poliza.getInteres().longValue() +
                    poliza.getCostosCobranza().longValue();
                valor_cuotaUF =
                    poliza.getPrimaBrutaUfRecibo() + (poliza.getReajuste() / poliza.getValorCambioUF()) +
                    (poliza.getMulta() / poliza.getValorCambioUF()) +
                    (poliza.getInteres() / poliza.getValorCambioUF()) +
                    (poliza.getCostosCobranza() / poliza.getValorCambioUF());
                valor_cuotaUF = NumeroUtil.redondeartodouble(valor_cuotaUF.doubleValue(), 4);
            } else {
                //Caso poliza normal
                valor_cuota = poliza.getPrimaBrutaPesosRecibo().longValue();
                valor_cuotaUF = poliza.getPrimaBrutaUfRecibo();
            }
            MontoCuota cuota_fija = cuota.addNewCuotaFija();
            cuota_fija.setEnUF(new BigDecimal(valor_cuotaUF.toString()));
            BigDecimal valor = new BigDecimal(valor_cuota);
            cuota_fija.setEnPesos(valor.unscaledValue());
            logger.debug("SetPolizas() - aumentando el valor del monto pagado");
            if (data != null) {
                monto_pagar += valor.intValue();
                porpagar = valor.intValue();
            }

            logger.debug("SetPolizas() - Detalle de los cargos");
            DetalleCargos detalle = cuota_fija.addNewDetalle();
            DetalleCargos.Cargo cargo;

            if (poliza.getReajuste() != null && poliza.getMulta() != null && poliza.getInteres() != null &&
                poliza.getCostosCobranza() !=
                null) {
                // Caso producto APV: Se agrega el resto de los detalles

                if (poliza.getViapago().intValue() ==
                    Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.planilla"))) {
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("ViaPago");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                    detalle.setCargoArray(0, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Secuencia");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getSecuencia().toString()));
                    detalle.setCargoArray(1, cargo);
                } else {
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("ViaPago");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                    detalle.setCargoArray(0, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Secuencia");
                    cargo.setBigDecimalValue(new BigDecimal("0"));
                    detalle.setCargoArray(1, cargo);
                }

                cargo = detalle.addNewCargo();
            } else {

                if (poliza.getViapago().intValue() ==
                    Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.planilla"))) {
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("ViaPago");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                    detalle.setCargoArray(0, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Secuencia");
                    //cargo.setBigDecimalValue(new BigDecimal(poliza.getSecuencia().toString()));
                    cargo.setBigDecimalValue(new BigDecimal("0"));
                    detalle.setCargoArray(1, cargo);
                } else {
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("ViaPago");
                    cargo.setBigDecimalValue(new BigDecimal(poliza.getViapago().toString()));
                    detalle.setCargoArray(0, cargo);
                    cargo = detalle.addNewCargo();
                    cargo.setDescripcion("Secuencia");
                    cargo.setBigDecimalValue(new BigDecimal("0"));
                    detalle.setCargoArray(1, cargo);
                }

                //Caso poliza normal

            }

            logger.debug("SetPolizas() - aumentando el valor del monto pagado segun lo seleccionado en la primera pantalla");
            if (data != null) {
                monto_pagar += ((Integer) data[3]).intValue();
                porpagar = ((Integer) data[3]).intValue();
            }

            logger.debug("SetPolizas() - CUOTA VARIABLE");

            //}

            if (porpagar == 0)
                deuda += valor_cuota;
            else
                deuda += porpagar;
            cuota.setPago(new BigInteger(Integer.toString(porpagar)));
            setEstadoCuotaPolizas(cuota);
        }
        logger.debug("SetPolizas() - termino");
        return deuda;
    }

    public void setEstadoCuota(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota cuota) {
    }

    /**
     * Setea el estado de las cuotas
     * @param cuota
     */
    public void setEstadoCuotaPolizas(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota cuota) {
        logger.debug("setEstadoCuota() - inicio");
        Calendar calendario = Calendar.getInstance();
        if (cuota.getFechaVencimiento().getTimeInMillis() >= calendario.getTimeInMillis())
            cuota.setEstado(ResourceBundleUtil.getProperty("polizas.cuota.estado.noatrasada"));
        else {
            if (calendario.get(Calendar.YEAR) == cuota.getFechaVencimiento().get(Calendar.YEAR)) {
                if (calendario.get(Calendar.MONTH) == cuota.getFechaVencimiento().get(Calendar.MONTH)) {
                    if (calendario.get(Calendar.DAY_OF_MONTH) ==
                        cuota.getFechaVencimiento().get(Calendar.DAY_OF_MONTH)) {
                        cuota.setEstado(ResourceBundleUtil.getProperty("polizas.cuota.estado.noatrasada"));
                        logger.debug("setEstadoCuota() - esta pagando en el mismo dia en que vence");
                    } else {
                        cuota.setEstado(ResourceBundleUtil.getProperty("polizas.cuota.estado.unmesatrasada"));
                        logger.debug("setEstadoCuota() - esta pagando en el mismo mes en que vence");
                    }
                } else {
                    logger.info("setEstadoCuota() - cuota.getFechaVencimiento().MONTH <= calendario.MONTH debido al primer IF");
                    cuota.setEstado(ResourceBundleUtil.getProperty("polizas.cuota.estado.masdeunmeatrasada"));
                    logger.info("setEstadoCuota() - " +
                                ResourceBundleUtil.getProperty("polizas.cuota.estado.masdeunmeatrasada"));
                }
            } else {
                logger.info("setEstadoCuota() - " +
                            ResourceBundleUtil.getProperty("polizas.cuota.estado.masdeunmeatrasada"));
                cuota.setEstado(ResourceBundleUtil.getProperty("polizas.cuota.estado.masdeunmeatrasada"));
            }
        }
        logger.debug("setEstadoCuota() - termino");
    }

    /**
     * Setea el estado cuota para dividendos
     * @param cuota
     */
    public void setEstadoCuotaDividendos(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota cuota) {
        logger.debug("setEstadoCuota() - inicio");
        Calendar calendario = Calendar.getInstance();
        if (cuota.getFechaVencimiento().getTimeInMillis() >= calendario.getTimeInMillis())
            cuota.setEstado(ResourceBundleUtil.getProperty("dividendos.cuota.estado.noatrasada"));
        else {
            if (calendario.get(Calendar.YEAR) == cuota.getFechaVencimiento().get(Calendar.YEAR)) {
                if (calendario.get(Calendar.MONTH) == cuota.getFechaVencimiento().get(Calendar.MONTH)) {
                    if (calendario.get(Calendar.DAY_OF_MONTH) ==
                        cuota.getFechaVencimiento().get(Calendar.DAY_OF_MONTH)) {
                        cuota.setEstado(ResourceBundleUtil.getProperty("dividendos.cuota.estado.noatrasada"));
                        logger.debug("setEstadoCuota() - esta pagando en el mismo dia en que vence");
                    } else {
                        cuota.setEstado(ResourceBundleUtil.getProperty("dividendos.cuota.estado.unmesatrasada"));
                        logger.debug("setEstadoCuota() - esta pagando en el mismo mes en que vence");
                    }
                } else {
                    logger.info("setEstadoCuota() - cuota.getFechaVencimiento().MONTH <= calendario.MONTH debido al primer IF");
                    cuota.setEstado(ResourceBundleUtil.getProperty("dividendos.cuota.estado.masdeunmeatrasada"));
                    logger.info("setEstadoCuota() - " +
                                ResourceBundleUtil.getProperty("dividendos.cuota.estado.masdeunmeatrasada"));
                }
            } else {
                logger.info("setEstadoCuota() - " + ResourceBundleUtil.getProperty("cuota.estado.masdeunmeatrasada"));
                cuota.setEstado(ResourceBundleUtil.getProperty("dividendos.cuota.estado.masdeunmeatrasada"));
            }
        }
        logger.debug("setEstadoCuota() - termino");

    }

    public void setProductosPagados(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, int rut) {
    }

    /**
     * Setea productos que ya estan pagados
     * para ser informados y con respectovo link para ser
     * llamadados desde el sitio web.
     * @param pp
     * @param rut
     */
    public void setProductosPagadosPolizas(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, int rut) {
        logger.debug("setProductosPagados() - inicio");
        int deuda = 0;
        List polizas = DAOFactory.getPersistenciaGeneralDao().findPagosPolizasMesByRut(rut);
        if (polizas != null && polizas.size() > 0) {
            logger.debug("setProductosPagadosPolizas() - seteo que se debe mostrar el titulo en la posicion 3 (Prima Contratada)");
            tipos_cuotas_sel[3] = true;
            deuda += SetPolizasMes(pp, polizas);
        }

        pp.setTotalPagado((new BigDecimal(deuda)).unscaledValue());
        logger.debug("setProductosPagadosPolizas() - termino");
    }

    /**
     * Busca los dividendos pagados del mes
     * @param pp
     * @param rut
     */
    public void setProductosPagadosDividendos(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, int rut) {
        logger.debug("setProductosPagados() - inicio");
        int deuda = 0;

        List dividendos = DAOFactory.getPersistenciaGeneralDao().findPagosDividendosMesByRut(rut);
        if (dividendos != null && dividendos.size() > 0) {
            deuda += setDividendosMes(pp, dividendos);
        }
        pp.setTotalPagado((new BigDecimal(deuda)).unscaledValue());
        logger.debug("setProductosPagadosDividendos() - termino");
    }


    /**
     * Setea las polizas del mes
     * @param pp
     * @param polizas
     * @return
     */
    public int SetPolizasMes(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, List polizas) {
        logger.debug("SetPolizasMes() - inicio");
        int deuda = 0;
        CartolaPolizas polizaant = null;
        for (int i = 0; i < polizas.size(); i++) {
            ++contador;
            ResumenConHeaderDocument.ResumenConHeader.ProductosPagados.Entrada entrada = pp.addNewEntrada();

            entrada.setContador(new BigDecimal(contador).unscaledValue());

            CartolaPolizas poliza = (CartolaPolizas) polizas.get(i);

            logger.debug("SetPolizasMes() - INFORMACION DEL PRODUCTO");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPagados.Entrada.Producto producto =
                entrada.addNewProducto();
            producto.setCodigoProducto(new BigInteger(poliza.getCodigoProducto().toString()));
            producto.setDescripcionProducto(poliza.getNombreProducto());
            producto.setNumProducto(new BigInteger(poliza.getPolizaPol().toString()));
            producto.setCodEmpresa(1);
            producto.setTipoCuota(2);

            logger.debug("SetPolizasMes() - CUOTA");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPagados.Entrada.Cuota cuota = entrada.addNewCuota();

            Calendar calendario = Calendar.getInstance();
            calendario.setTime(poliza.getFechaVencimiento());
            cuota.setFechaVencimiento(calendario);

            cuota.setMedioPago(poliza.getCanalPago());

            calendario.setTime(poliza.getFechaPago());
            cuota.setFechaPago(calendario);

            cuota.setValorUF(new BigDecimal(poliza.getValorUf().toString()));
            cuota.setTotalPagado(new BigInteger(poliza.getMontoPagadoPesos().toString()));


            //incremento cantidad pagada
            //incrementar solamente si pago no ha sido anulado posteriormente
            logger.debug("SetPolizasMes() - Chequeo si debo aumentar, disminuir o dejar igual el monto total pagado");
            //if(poliza.getEstado().equals(ResourceProperties.getProperty("poliza.estado.anulada"))) {
            if (poliza.getEstado().equals("9")) {
                if (polizaant != null && poliza.getIdComprobpago() == polizaant.getIdComprobpago()) {
                    logger.debug("SetPolizasMes() - decremento valor");
                    deuda -= poliza.getMontoPagadoPesos().intValue();
                }
            } else {
                logger.debug("SetPolizasMes() - aumento valor");
                deuda += poliza.getMontoPagadoPesos().intValue();
            }

            MontoCuota cuota_fija = cuota.addNewCuotaFija();
            cuota_fija.setEnUF(new BigDecimal(poliza.getPrimaBrutaUf().toString()));
            cuota_fija.setEnPesos(new BigInteger(poliza.getMontoPagadoPesos().toString()));

            //Detalle de los cargos
            List detallepagolist =
                DAOFactory.getConsultasDao().findDetallePagoPolizaByFolioRecibo(poliza.getFolioRecibo());
            if (detallepagolist != null && detallepagolist.size() > 0) {
                DetalleCargos detalle = cuota_fija.addNewDetalle();

                for (int j = 0; j < detallepagolist.size(); j++) {
                    DetallePagoPoliza detallepago = (DetallePagoPoliza) detallepagolist.get(j);
                    DetalleCargos.Cargo cargo = detalle.addNewCargo();
                    cargo.setDescripcion(detallepago.getDescCargo());
                    cargo.setBigDecimalValue(new BigDecimal(detallepago.getMonto().toString()));
                }
            }

            if (poliza.getTipoComprobante() == 2) {
                logger.debug("SetPolizasMes() - PAGO POR CAJA");
                EstadoCaja estado = cuota.addNewEstadoCaja();
                String realEstado = ResourceBundleUtil.getProperty(poliza.getEstado());
                estado.setEstado(realEstado);
                //idcomprobante de tabla bpi_cpp_comprobpagopoliza_tbl
                estado.setComprobantePago(poliza.getIdComprobpago().toString());
            } else {
                logger.debug("SetPolizasMes() - PAGO POR INTERNET");
                EstadoInternet estado = cuota.addNewEstadoInternet();
                String realEstado = ResourceBundleUtil.getProperty(poliza.getEstado());
                estado.setEstado(realEstado);
                //idcomprobante de tabla dna o dpe
                estado.setComprobantePago(poliza.getFolioRecibo().toString());
            }
            polizaant = poliza;
        }
        logger.debug("SetPolizasMes() - termino");
        return deuda;
    }

    /**
     * Metodo para seterar los dividendos que hay que pagar
     * @param pp
     * @param dividendos
     * @return
     */
    public int setDividendosMes(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, List dividendos) {
        logger.debug("setDividendosMes() - inicio");
        int deuda = 0;
        for (int i = 0; i < dividendos.size(); i++) {
            ++contador;
            ResumenConHeaderDocument.ResumenConHeader.ProductosPagados.Entrada entrada = pp.addNewEntrada();
            entrada.setContador(new BigDecimal(contador).unscaledValue());
            BhDividendosHistoricoVw dividendo = (BhDividendosHistoricoVw) dividendos.get(i);

            logger.debug("setDividendosMes() - INFORMACION DEL PRODUCTO");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPagados.Entrada.Producto producto =
                entrada.addNewProducto();
            producto.setCodigoProducto(new BigInteger("1"));
            producto.setDescripcionProducto(dividendo.getProducto());
            producto.setNumProducto(new BigInteger(dividendo.getNumOpe().toString()));
            producto.setCodEmpresa(2);
            producto.setTipoCuota(1);


            logger.debug("setDividendosMes() - CUOTA");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPagados.Entrada.Cuota cuota = entrada.addNewCuota();

            Calendar calendario = Calendar.getInstance();
            calendario.setTime(dividendo.getFecVencimiento());

            cuota.setFechaVencimiento(calendario);
            cuota.setMedioPago(dividendo.getNombreSucursalPago());

            calendario.setTime(dividendo.getFecPago());
            cuota.setFechaPago(calendario);

            cuota.setValorUF(new BigDecimal(dividendo.getValorUf().toString()));


            logger.debug("setDividendosMes() - seteo que se debe mostrar el titulo en la posicion 0 (Nomral)");
            tipos_cuotas_sel[0] = true;
            long valor_cuota = dividendo.getTotPes();
            deuda += valor_cuota;

            MontoCuota cuota_fija = cuota.addNewCuotaFija();
            cuota_fija.setEnUF(new BigDecimal(dividendo.getTotMda().toString()));
            cuota_fija.setEnPesos(new BigInteger(dividendo.getTotPes().toString()));

            //Detalle de los cargos
            DetalleCargos detalle = cuota_fija.addNewDetalle();
            DetalleCargos.Cargo cargo;
            List detalleDividendo = dividendo.getDetalleDividendo();
            for (int j = 0; j < detalleDividendo.size(); j++) {
                Object[] det = (Object[]) detalleDividendo.get(j);
                cargo = detalle.addNewCargo();
                cargo.setDescripcion(det[0].toString());
                cargo.setBigDecimalValue(new BigDecimal(det[1].toString()));
            }

            //Integer totalpagado = dividendo.getDif_boleta() + dividendo.getTotalPagado();
            //cuota.setTotalPagado(new BigInteger(dividendo.getTotPes().toString()));
            Integer totalapagado = dividendo.getTotalPagado() + dividendo.getDif_boleta();
            //cuota.setTotalPagado(new BigInteger(dividendo.getTotPes().toString()));
            cuota.setTotalPagado(new BigInteger(totalapagado.toString()));


            if (dividendo.getSucursalPago() ==
                Integer.parseInt(ResourceBundleUtil.getProperty("dividendo.sucursal.internet"))) {
                logger.info("setDividendosMes() - PAGO POR INTERNET");
                EstadoInternet estado = cuota.addNewEstadoInternet();
                String realEstado = ResourceBundleUtil.getProperty(dividendo.getEstado());
                estado.setEstado(realEstado);
                //estado.setEstado("PAGADO");
                //uso num_dividendo para poder buscar el comprobante asociado + info de numproducto
                estado.setComprobantePago(dividendo.getNumDiv().toString());
            } else {
                logger.info("setDividendosMes() - PAGO POR CAJA");
                EstadoCaja estado = cuota.addNewEstadoCaja();
                String realEstado = ResourceBundleUtil.getProperty(dividendo.getEstado());
                estado.setEstado(realEstado);
                //estado.setEstado("PAGADO");
                //uso num_dividendo para poder buscar el comprobante asociado + info de numproducto
                estado.setComprobantePago(dividendo.getNumDiv().toString());
            }
        }
        logger.debug("setDividendosMes() - termino");
        return deuda;
    }

    /**
     * Establece los convenios con los cuales
     * se pueden realizar los pagos
     * @param newResumenConHeader
     * @param seleccionado
     */
    public void setConvenios(ResumenConHeaderDocument.ResumenConHeader newResumenConHeader,
                             int seleccionado) throws BiceException {
        logger.debug("setConvenios() - inicio");
        List medios;
        medios = DAOFactory.getPersistenciaGeneralDao().findAllMediosVisibles();
        if (medios != null && medios.size() > 0) {
            Convenios newConvenios = newResumenConHeader.addNewConvenios();
            for (int i = 0; i < medios.size(); i++) {
                Convenios.Convenio newConvenio = newResumenConHeader.getConvenios().addNewConvenio();
                MedioPago medio = (MedioPago) medios.get(i);
                newConvenio.setCodigo(new BigInteger(medio.getCodMedio().toString()));
                newConvenio.setDescripcion(medio.getNombre());
                if (seleccionado == medio.getCodMedio().intValue())
                    newConvenio.setSeleccionado(true);
                newConvenios.setConvenioArray(i, newConvenio);
            }
        } else {
            logger.warn("setConvenios() - No se han encontrado convenidos de pago o no existen en la base de datos, revisar..");
            throw new BiceException("No hay convenios disponibles con los cuales se pueda pagar electronicamente.");
        }
        logger.debug("setConvenios() - termino");
    }


    /**
     * Generacion de Logica de resumen y pagos primeras primas
     * @param rut
     * @param nombre
     * @param confirmacion
     * @param idCanal
     * @param numeroPropuesta
     * @return
     */
    public String crearResumenPagosPrimerasPrimas(int rut, String nombre, String confirmacion, int idCanal,
                                                  Integer numeroPropuesta) {
        logger.debug("crearResumenPagosPrimerasPrimas() - inicio");
        String resumenxml =
            "NOK|" + ResourceBundleUtil.getProperty("cl.bice.vida.error.primera.prima.sinpropuesta.bicevida");
        try {
            logger.debug("crearResumenPagosPrimerasPrimas() - verificando confirmacion");
            ConfirmacionDocument conf = null;
            if (confirmacion != null) {
                logger.debug("crearResumenPagosPrimerasPrimas() - confirmacion distinto de null");
                conf = ConfirmacionDocument.Factory.parse(confirmacion);
                logger.debug("crearResumenPagosPrimerasPrimas() - validando xml");
                validateXML(conf);
            }
            logger.debug("crearResumenPagosPrimerasPrimas() - generando resumen y pagos");
            ResumenConHeaderDocument resumen = generaXmlResumenPagosPrimeraPrima(rut, nombre, conf, numeroPropuesta);
            logger.debug("crearResumenPagosPrimerasPrimas() - validando resumen y pago");
            validateXML(resumen);
            logger.debug("crearResumenPagosPrimerasPrimas() - convirtiendo resumen a String");
            resumenxml = resumen.toString();
            logger.info("crearResumenPagosPrimerasPrimas() - creando detalle de navegacion - IDNavegacion["+resumen.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue()+"]Entrada["+resumen.getResumenConHeader().getInfoNavegacion().getEntrada().intValue()+"]");
            DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(resumen.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                           resumen.getResumenConHeader().getInfoNavegacion().getEntrada().intValue(),
                                                                           1,
                                                                           resumen.getResumenConHeader().getTotalPagar().intValue(),
                                                                           resumenxml, idCanal);
        } catch (BiceException e) {
            e.printStackTrace();
            logger.warn("crearResumenPagosPrimerasPrimas() - BiceException : NOK :" + e.getMessage());
            resumenxml = "NOK|" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("crearResumenPagosPrimerasPrimas() - error : NOK :" + e.getMessage(), e);
            resumenxml = "NOK|" + ResourceBundleUtil.getProperty("mensaje.error.resumenypagos.general.bicevida");
            e.printStackTrace();
        } finally {
            logger.debug("crearResumenPagosPrimerasPrimas() - termino");
            return resumenxml;
        }
    }

    /**
     * Metod que genera el XML a enviar
     * pro concepto de resumen y pagos
     * @param rut
     * @param nombre
     * @param conf
     * @return
     */
    public ResumenConHeaderDocument generaXmlResumenPagosPrimeraPrima(int rut, String nombre, ConfirmacionDocument conf,
                                                                      int numeroPropuesta) throws BiceException,
                                                                                                  ParseException {
        logger.debug("generaXmlResumenPagosPrimeraPrima() - inicio");
        Calendar fecha = Calendar.getInstance();
        //SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM");
        //SimpleDateFormat formato1 = new SimpleDateFormat("yyyy-MM-dd");
        //String fechas = formato.format(fecha.getTime());
        //fechas = fechas + "-05";
        //Date fecha_uf = formato1.parse(fechas);
        Date fecha_uf = fecha.getTime();
        logger.debug("generaXmlResumenPagosPrimeraPrima() - recuperando ResumenConHeaderDocument");
        ResumenConHeaderDocument resumen = ResumenConHeaderDocument.Factory.newInstance();
        logger.debug("generaXmlResumenPagosPrimeraPrima() - ejecutando addNewResumenConHeader()");
        ResumenConHeaderDocument.ResumenConHeader newResumenConHeader = resumen.addNewResumenConHeader();
        Long idNavegacion = 0L;
        logger.debug("generaXmlResumenPagosPrimeraPrima() - SETANDO LA INFORMACION GENERAL");
        if (conf != null) {
            logger.debug("generaXmlResumenPagosPrimeraPrima() - confirmacion distinto de null");
            logger.debug("generaXmlResumenPagosPrimeraPrima() - addNewInfoNavegacion");
            newResumenConHeader.addNewInfoNavegacion();
            logger.debug("generaXmlResumenPagosPrimeraPrima() - seteando valores de nevegacion");
            idNavegacion = conf.getConfirmacion().getInfoNavegacion().getIDNavegacion().longValue();
            newResumenConHeader.getInfoNavegacion().setIDNavegacion(conf.getConfirmacion().getInfoNavegacion().getIDNavegacion());
            newResumenConHeader.getInfoNavegacion().setEntrada(conf.getConfirmacion().getInfoNavegacion().getEntrada().add(new BigInteger("1")));
        } else {
            logger.info("generaXmlResumenPagosPrimeraPrima() - confirmacion en null");
            setInfoNavegacion(newResumenConHeader, rut, 1);
        }

        logger.info("generaXmlResumenPagosPrimeraPrima() - IDNavegacion["+idNavegacion+"] - datos del clientes (rut["+rut+"], nombre["+nombre+"], fecha consulta["+fecha+"])");
        newResumenConHeader.setRutCliente(rut);
        newResumenConHeader.setNombreCliente(nombre);
        setIdentificacionMensaje(newResumenConHeader);
        newResumenConHeader.setFechaConsulta(fecha);
        //newResumenConHeader.setNumeroPropuesta(numeroPropuesta);

        BigDecimal valor_uf;
        logger.debug("generaXmlResumenPagosPrimeraPrima() - seteando el valor de la UF");
        if (conf == null ||
            conf.getConfirmacion().getFechaConsulta().DATE != newResumenConHeader.getFechaConsulta().DATE) {
            valor_uf = setValorUF(fecha_uf);
        } else {
            valor_uf = conf.getConfirmacion().getValorUF();
        }

        Calendar fecha_uf_calendar = Calendar.getInstance();
        fecha_uf_calendar.setTime(fecha_uf);
        newResumenConHeader.setFechaUF(fecha_uf_calendar);
        newResumenConHeader.setValorUF(valor_uf);

        logger.debug("generaXmlResumenPagosPrimeraPrima() - buscando si la caja esta habierta para setear el turno");
        AperturaCaja turno = DAOFactory.getPersistenciaGeneralDao().findCajaAbierta();
        if (turno != null) {
            fecha.setTime(turno.getTurno());
            newResumenConHeader.setTurno(fecha);
        }

        logger.debug("generaXmlResumenPagosPrimeraPrima() - Copia ProductosPorPagar");
        ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp;
        pp = newResumenConHeader.addNewProductosPorPagar();
        logger.debug("generaXmlResumenPagosPrimeraPrima() - seteando productos por pagar setProductosPorPagar()");
        //if(conf!=null) setProductosPorPagarPolizas(pp, rut, valor_uf,turno,conf.getConfirmacion().getProductosPorPagar());
        if (conf != null)
            setProductosPorPagarPrimeraPrima(pp, rut, valor_uf, turno, conf.getConfirmacion().getProductosPorPagar(),
                                             numeroPropuesta);
        //else setProductosPorPagarPolizas(pp, rut, valor_uf,turno,null);
        else
            setProductosPorPagarPrimeraPrima(pp, rut, valor_uf, turno, null, numeroPropuesta);

        logger.debug("generaXmlResumenPagosPrimeraPrima() - Copia ProductosPagados");
        ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pPagados;
        pPagados = newResumenConHeader.addNewProductosPagados();

        //setProductosPagadosPolizas(pPagados, rut);
        //setProductosPagadosPrimeraPrima(pPagados, rut, numeroPropuesta);

        logger.debug("generaXmlResumenPagosPrimeraPrima() - titulos que se van a desplegar");
        logger.debug("generaXmlResumenPagosPrimeraPrima() - seteando titulos que se van a desplegar");
        Titulos titulos;
        titulos = newResumenConHeader.addNewTitulos();


        titulos.addTitulo("SVI PRIMERA PRIMA");


        
        int convenio_seleccionado = 0;
        if (conf != null)
            convenio_seleccionado = conf.getConfirmacion().getConvenio().getCodigo().intValue();
        setConvenios(resumen.getResumenConHeader(), convenio_seleccionado);

        logger.info("generaXmlResumenPagosPrimeraPrima() - IDNavegacion["+idNavegacion+"] Convenio seleccionado["+convenio_seleccionado+"]");
        
        
        if (conf == null)
            newResumenConHeader.setTotalPagar(new BigInteger("0"));
        else
            newResumenConHeader.setTotalPagar(conf.getConfirmacion().getProductosPorPagar().getTotalPorPagar().getEnPesos());

        logger.info("generaXmlResumenPagosPrimeraPrima() - IDNavegacion["+idNavegacion+"]Monto total a pagar["+newResumenConHeader.getTotalPagar()+"]");
        
        resumen.setResumenConHeader(newResumenConHeader);
        logger.debug("generaXmlResumenPagosPrimeraPrima() - termino");
        return resumen;
    }

    /**
     * Setea los productos a pagar por conceto de polizas
     * @param pp
     * @param rut
     * @param valor_uf
     * @param turno
     * @param seleccion
     */
    public void setProductosPorPagarPrimeraPrima(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp,
                                                 int rut, BigDecimal valor_uf, AperturaCaja turno,
                                                 ConfirmacionDocument.Confirmacion.ProductosPorPagar seleccion,
                                                 int numeroPropuesta) {
        logger.debug("setProductosPorPagarPrimeraPrima() - inicio");
        int deuda = 0;
        List polizassel = null;
        List dividendossel = null;

        if (seleccion != null) {
            logger.debug("setProductosPorPagarPrimeraPrima() - seleccion distinto de null entra el ciclo");
            polizassel = new ArrayList();
            logger.debug("setProductosPorPagarPrimeraPrima() - iniciando iteracion de productos");
            for (int i = 0; i < seleccion.getEntradaArray().length; i++) {
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = seleccion.getEntradaArray(i);
                Object[] data = new Object[4];
                if (entrada.getProducto().getCodEmpresa() == 1) {
                    data[0] = entrada.getProducto().getNumProducto().longValue();
                    data[1] = entrada.getInfoCajas().getPolizas().getRamo().intValue();
                    data[2] = entrada.getInfoCajas().getPolizas().getFolio().intValue();
                    data[3] = entrada.getCuota().getMonto().getEnPesos().intValue();
                    polizassel.add(data);
                }
            }
            if (polizassel.size() == 0)
                polizassel = null;
        }

        if (turno != null) {
            logger.debug("setProductosPorPagarPrimeraPrima() - turno distinto de null busco polizas findPolizasByRut()");
            //List<IndPrimaNormalVw> polizas = DAOFactory.getPersistenciaGeneralDao().findPolizasByRut(rut);
            List<IndPrimaNormalVw> pprimas =
                DAOFactory.getPersistenciaGeneralDao().findPrimeraPrimaByNumppRut(numeroPropuesta, rut);

            if (pprimas != null && pprimas.size() > 0) {
                logger.debug("setProductosPorPagarPrimeraPrima() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada)");
                logger.debug("setProductosPorPagarPrimeraPrima() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada) - SetPrimeraPrima()");
                tipos_cuotas_sel[3] = true;
                //deuda += SetPolizas(pp, valor_uf, pprimas,polizassel);
                deuda += SetPrimeraPrima(pp, valor_uf, pprimas, polizassel);
            }

            /*List polizaspatpac = DAOFactory.getPersistenciaGeneralDao().findPolizasByRutPatPac(rut);
            if (polizaspatpac != null && polizaspatpac.size() > 0) {
                tipos_cuotas_pat_pac = true;
            }*/

        } else {
            logger.info("setProductosPorPagarPrimeraPrima() - turno es igual a null, buscando polizas Offline - findPolizasOfflineByRut()");
            List polizas = DAOFactory.getPersistenciaGeneralDao().findPolizasOfflineByRut(rut);
            if (polizas != null && polizas.size() > 0) {
                logger.debug("setProductosPorPagarPrimeraPrima() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada)");
                tipos_cuotas_sel[3] = true;
                logger.debug("setProductosPorPagarPrimeraPrima() - seteo que se debe mostrar el titulo en la posicion 0 (Prima Contratada) - SetPrimeraPrima()");
                //deuda += SetPolizas(pp, valor_uf, polizas,polizassel);
                deuda += SetPrimeraPrima(pp, valor_uf, polizas, polizassel);
            }
        }

        pp.setTotalPorPagar(new BigInteger(Integer.toString(monto_pagar)));
        pp.setTotalDeuda(new BigInteger(Integer.toString(deuda)));
        logger.debug("setProductosPorPagarPrimeraPrima() - termino");

    }

    /**
     * Setea productos que ya estan pagados
     * para ser informados y con respectovo link para ser
     * llamadados desde el sitio web.
     * @param pp
     * @param rut
     */
    public void setProductosPagadosPrimeraPrima(ResumenConHeaderDocument.ResumenConHeader.ProductosPagados pp, int rut,
                                                int numeroPropuesta) {
        logger.debug("setProductosPagadosPrimeraPrima() - inicio");
        int deuda = 0;
        List polizas = DAOFactory.getPersistenciaGeneralDao().findPagosPolizasMesByRut(rut);
        if (polizas != null && polizas.size() > 0) {
            logger.debug("setProductosPagadosPrimeraPrima() - seteo que se debe mostrar el titulo en la posicion 3 (Prima Contratada)");
            tipos_cuotas_sel[3] = true;
            deuda += SetPolizasMes(pp, polizas);
        }

        pp.setTotalPagado((new BigDecimal(deuda)).unscaledValue());
        logger.debug("setProductosPagadosPrimeraPrima() - termino");
    }

    /**
     * Setea las polizas
     * @param pp
     * @param valor_uf
     * @param polizas
     * @param seleccion
     * @return
     */
    public int SetPrimeraPrima(ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar pp, BigDecimal valor_uf,
                               List polizas, List seleccion) {
        logger.debug("SetPrimeraPrima() - inicio");
        long num_producto = 0;
        int deuda = 0;
        for (int i = 0; i < polizas.size(); i++) {
            int porpagar = 0;
            ++contador;
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada entrada = pp.addNewEntrada();

            entrada.setContador(new BigDecimal(contador).unscaledValue());

            IndPrimaNormalVw poliza;
            if (polizas.get(i) instanceof IndPrimaNormalVw) {
                poliza = (IndPrimaNormalVw) polizas.get(i);
            } else {
                poliza = ((IndPrimaNormalOfflineVw) polizas.get(i)).toIndPrimaNormalVw();
            }

            entrada.setSeleccionado(false);
            entrada.setDisabled(true);
            Object[] data = null;
            if (seleccion != null) {
                for (int j = 0; j < seleccion.size(); j++) {
                    data = (Object[]) seleccion.get(j);
                    if (((Long) data[0]).longValue() == poliza.getPolizaPol().longValue() &&
                        ((Integer) data[1]).intValue() == poliza.getRamo().intValue() &&
                        ((Integer) data[2]).intValue() == poliza.getFolioRecibo().intValue()) {
                        entrada.setSeleccionado(true);
                        entrada.setDisabled(false);
                        num_producto = 0;
                        break;
                    } else
                        data = null;
                }
            }

            if (data == null && num_producto == 0) {
                num_producto = poliza.getPolizaPol().longValue();
                entrada.setDisabled(false);
            } else if (num_producto != 0 && num_producto != poliza.getPolizaPol().longValue()) {
                num_producto = poliza.getPolizaPol().longValue();
                entrada.setDisabled(false);

            }

            logger.debug("SetPrimeraPrima() - INFORMACION DEL PRODUCTO");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Producto producto =
                entrada.addNewProducto();
            producto.setCodigoProducto(new BigDecimal(poliza.getCodProducto()).unscaledValue());
            producto.setDescripcionProducto(poliza.getNombre());
            producto.setNumProducto(new BigDecimal(poliza.getPolizaPol()).unscaledValue());
            producto.setCodEmpresa(poliza.getCodEmpresa());
            producto.setTipoCuota(poliza.getCodMecanismo());

            logger.debug("SetPrimeraPrima() - INFORMACION UTIL PARA CAJAS");
            Infocajas.Polizas infopoliza = entrada.addNewInfoCajas().addNewPolizas();
            infopoliza.setFolio(new BigInteger(poliza.getFolioRecibo().toString()));
            infopoliza.setRamo(new BigInteger(poliza.getRamo().toString()));


            logger.debug("SetPrimeraPrima() - CUOTA    ");
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota cuota = entrada.addNewCuota();

            Calendar calendario = Calendar.getInstance();
            calendario.setTime(poliza.getFInicioRecibo());

            cuota.setFechaVencimiento(calendario);
            cuota.setMedioPago("");

            logger.debug("SetPrimeraPrima() - INFORMACION DEL PAGO AUTOMATICO: |" + poliza.getViapago() + "|");

            if (poliza.getViapago().intValue() ==
                Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.pac")) ||
                poliza.getViapago().intValue() ==
                Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.pat"))) {
                ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada.Cuota.InfoPA infoPA =
                    cuota.addNewInfoPA();
                ValorUf valoruf =
                    DAOFactory.getPersistenciaGeneralDao().findValorUfByDate(cuota.getFechaVencimiento().getTime());
                if (valoruf != null) {
                    String valorn = NumeroUtil.redondear(valoruf.getValorUf(), 2);
                    infoPA.setValorUF(new BigDecimal(valorn));
                } else
                    infoPA.setValorUF(new BigDecimal("0"));
                infoPA.setFechaPago(cuota.getFechaVencimiento());
                //entrada.setSeleccionado(false);
                //entrada.setDisabled(true);
                //num_producto=0;

                //if(poliza.getViapago().intValue()==Integer.parseInt(ResourceBundleUtil.getProperty("poliza.codigo.pac"))) cuota.setMedioPago("PAC");
                //else cuota.setMedioPago("PAT");
            }

            long valor_cuota = 0;
            Double valor_cuotaUF;


            //TODO: NO VA EL CALCULO DE MULTAS E INTERESES
            logger.debug("SetPolizas() - POLIZA NORMAL");
            if (poliza.getReajuste() != null && poliza.getMulta() != null && poliza.getInteres() != null &&
                poliza.getCostosCobranza() !=
                null) {
                // Caso producto APV: Suma de resto de los parametros en caso de APV
                valor_cuota =
                    poliza.getPrimaBrutaPesosRecibo().longValue() + poliza.getReajuste().longValue() +
                    poliza.getMulta().longValue() + poliza.getInteres().longValue() +
                    poliza.getCostosCobranza().longValue();
                valor_cuotaUF =
                    poliza.getPrimaBrutaUfRecibo() + (poliza.getReajuste() / poliza.getValorCambioUF()) +
                    (poliza.getMulta() / poliza.getValorCambioUF()) +
                    (poliza.getInteres() / poliza.getValorCambioUF()) +
                    (poliza.getCostosCobranza() / poliza.getValorCambioUF());
                valor_cuotaUF = NumeroUtil.redondeartodouble(valor_cuotaUF.doubleValue(), 4);
            } else {
                //Caso poliza normal
                valor_cuota = poliza.getPrimaBrutaPesosRecibo().longValue();
                valor_cuotaUF = poliza.getPrimaBrutaUfRecibo();
            }
            MontoCuota cuota_fija = cuota.addNewCuotaFija();
            cuota_fija.setEnUF(new BigDecimal(valor_cuotaUF.toString()));
            BigDecimal valor = new BigDecimal(valor_cuota);
            cuota_fija.setEnPesos(valor.unscaledValue());
            logger.debug("SetPolizas() - aumentando el valor del monto pagado");
            if (data != null) {
                monto_pagar += valor.intValue();
                porpagar = valor.intValue();
            }

            logger.debug("SetPolizas() - Detalle de los cargos");
            DetalleCargos detalle = cuota_fija.addNewDetalle();
            DetalleCargos.Cargo cargo;


            logger.debug("SetPolizas() - aumentando el valor del monto pagado segun lo seleccionado en la primera pantalla");
            if (data != null) {
                monto_pagar += ((Integer) data[3]).intValue();
                porpagar = ((Integer) data[3]).intValue();
            }

            logger.debug("SetPolizas() - CUOTA VARIABLE");

            //}


            if (porpagar == 0)
                deuda += valor_cuota;
            else
                deuda += porpagar;
            cuota.setPago(new BigInteger(Integer.toString(porpagar)));
            setEstadoCuotaPolizas(cuota);
        }
        logger.debug("SetPrimeraPrima() - termino");
        return deuda;
    }

}


