package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.Comprobantes;
import cl.bice.vida.botonpago.common.dto.general.DetalleNavegacion;
import cl.bice.vida.botonpago.common.dto.general.DetalleTransaccionByEmp;
import cl.bice.vida.botonpago.common.dto.general.Navegacion;
import cl.bice.vida.botonpago.common.dto.general.ResumenRequest;
import cl.bice.vida.botonpago.common.dto.general.Transacciones;
import cl.bice.vida.botonpago.common.dto.parametros.CodEmpByMedio;
import cl.bice.vida.botonpago.common.util.EncryptSeguridadBiceUtil;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.common.util.NumeroUtil;
import cl.bice.vida.botonpago.common.util.RutUtil;
import cl.bice.vida.botonpago.common.util.StringUtil;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;
import cl.bice.vida.botonpago.modelo.vo.ItemPagoElectronicoBancoBICEVO;
import cl.bice.vida.botonpago.modelo.vo.PagoElectronicoBancoBICEVO;
import cl.bice.xmlbeans.servipag.pec.Documentos;
import cl.bice.xmlbeans.servipag.pec.Header;
import cl.bice.xmlbeans.servipag.pec.Servipag;
import cl.bice.xmlbeans.util.JAXBUtil;

import java.io.IOException;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import noNamespace.ConfirmacionDocument;
import noNamespace.DETALLEDocument;
import noNamespace.DetalleCargos;
import noNamespace.MPINIDocument;
import noNamespace.ResumenConHeaderDocument;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.dom4j.DocumentException;


public class LogicaConfirmacionDAOImpl extends DataSourceBice implements LogicaConfirmacionDAO {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(LogicaConfirmacionDAOImpl.class);

    /*
     * Variables
     */
    private List detallesByEmp = null;
    private List comprobantes = null;

    /*
     * Constantes
     */
    private final int CODE_MEDIOPAGO_BANCOCHILE = 1;
    private final int CODE_MEDIOPAGO_SANTANDER = 2;
    private final int CODE_MEDIOPAGO_SERVIPAG = 3;
    private final int CODE_MEDIOPAGO_SERVIPAG2 = 16;
    private final int CODE_MEDIOPAGO_WEBPAY = 4;
    private final int CODE_MEDIOPAGO_BANCOESTADO = 8;
    private final int CODE_MEDIOPAGO_BANCOBCI = 9;
    private final int CODE_MEDIOPAGO_BANCOTBANC = 10;
    private final int CODE_MEDIOPAGO_WEBPAY_CONVENIO = 11;
    private final int CODE_MEDIOPAGO_BANCOBICE = 12;

    /**
     * Variable utilizada para servipag
     */
    private String email;

    public String crearConfirmacion(String xmlResumenPagos, ResumenRequest req) {
        return null;
    }

    /**
     * Crea la confirmacion con los productos
     * ya listos y seleccionados.
     * @param xmlResumenSeleccionado
     * @return
     */
    public String crearConfirmacionConXmlSeleccionado(String origenTransaccion, String xmlResumenSeleccionado,
                                                      int idCanal, String email) {
        logger.info("crearConfirmacionConXmlSeleccionado() - inicio");
        detallesByEmp = null;
        comprobantes = null;
        this.email = email;
        ConfirmacionDocument confirmacion;
        String code = "NOK|Error: Inesperado";
        try {
            ResumenConHeaderDocument res = ResumenConHeaderDocument.Factory.parse(xmlResumenSeleccionado);
            validateXML(res);
            logger.info("crearConfirmacionConXmlSeleccionado() - busco el medio de pago seleccionado");
            int medio = 0;
            for (int i = 0; i < res.getResumenConHeader().getConvenios().getConvenioArray().length; i++) {
                if (res.getResumenConHeader().getConvenios().getConvenioArray(i).getSeleccionado()) {
                    medio = res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue();
                }
            }
            logger.info("crearConfirmacionConXmlSeleccionado() - creo la transaccion");
            Transacciones transaccion;
            if (res.getResumenConHeader().getIdTransaccion() == null) {
                //CONSULTO LA NAVEGACION PARA SABER A QUE EMPRESA LE PERTENECENE LA CONFIRMACION
                String empresa = "BV";
                Navegacion navDto =
                    DAOFactory.getPersistenciaGeneralDao().findNavegacionById(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue());
                if (navDto != null) {
                    if (navDto.getEmpresa() == 1)
                        empresa = "BV";
                    if (navDto.getEmpresa() == 2)
                        empresa = "BH";
                }
                transaccion =
                    DAOFactory.getPersistenciaGeneralDao().newTransaccion(origenTransaccion, empresa, medio,
                                                                          res.getResumenConHeader().getTurno().getTime(),
                                                                          res.getResumenConHeader().getTotalPagar().intValue(),
                                                                          res.getResumenConHeader().getFechaConsulta().getTime(),
                                                                          res.getResumenConHeader().getRutCliente(),
                                                                          res.getResumenConHeader().getNombreCliente());

            } else {
                transaccion =
                    DAOFactory.getPersistenciaGeneralDao().updateTransaccion(res.getResumenConHeader().getIdTransaccion().intValue(),
                                                                             medio,
                                                                             res.getResumenConHeader().getTotalPagar().intValue(),
                                                                             res.getResumenConHeader().getTurno().getTime());
            }
            logger.info("crearConfirmacionConXmlSeleccionado() - actualiza la informacion de la transaccion en la tabla de navegacion");
            DAOFactory.getPersistenciaGeneralDao().updateTransaccionInNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                                 transaccion.getIdTransaccion(),
                                                                                 transaccion.getMontoTotal(), medio);

            logger.info("crearConfirmacionConXmlSeleccionado() - creo el xml de confirmacion y lo almaceno en la base de datos");
            confirmacion = generaXmlConfirmacion(res, transaccion.getIdTransaccion());
            validateXML(confirmacion);

            DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(confirmacion.getConfirmacion().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                           confirmacion.getConfirmacion().getInfoNavegacion().getEntrada().intValue(),
                                                                           3,
                                                                           confirmacion.getConfirmacion().getProductosPorPagar().getTotalPorPagar().getEnPesos().intValue(),
                                                                           confirmacion.toString(), idCanal);
            code = confirmacion.toString();

            logger.info("crearConfirmacionConXmlSeleccionado() - lleno el detalle de la transaccion");
            //TODO: Revisar SQL
            DAOFactory.getPersistenciaGeneralDao().updateDetalleTransaccion(transaccion, detallesByEmp, comprobantes);
        } catch (Exception e) {
            code = "NOK|Error: " + e.getMessage();
            logger.error("crearConfirmacionConXmlSeleccionado() - NOK|Error: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {        
            logger.info("crearConfirmacionConXmlSeleccionado() - termino");
            return code;
        }
    }

    /**
     * Crea la confirmacion con los productos
     * ya listos y seleccionados.
     * @param xmlResumenSeleccionado
     * @return
     */
    public String crearConfirmacionConXmlSeleccionado(String origenTransaccion, String xmlResumenSeleccionado,
                                                      int idCanal, int tipoTransaccion, String email) {
        logger.info("crearConfirmacionConXmlSeleccionado() - inicio");
        this.email = email;
        detallesByEmp = null;
        comprobantes = null;

        ConfirmacionDocument confirmacion;
        String code = "NOK|Error: Inesperado";
        try {
            ResumenConHeaderDocument res = ResumenConHeaderDocument.Factory.parse(xmlResumenSeleccionado);
            validateXML(res);
            logger.info("crearConfirmacionConXmlSeleccionado() - busco el medio de pago seleccionado");
            int medio = 0;
            for (int i = 0; i < res.getResumenConHeader().getConvenios().getConvenioArray().length; i++) {
                if (res.getResumenConHeader().getConvenios().getConvenioArray(i).getSeleccionado()) {
                    medio = res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue();
                }
            }
            logger.info("crearConfirmacionConXmlSeleccionado() - creo la transaccion");
            Transacciones transaccion;
            if (res.getResumenConHeader().getIdTransaccion() == null) {

                //CONSULTO LA NAVEGACION PARA SABER A QUE EMPRESA LE PERTENECENE LA CONFIRMACION
                String empresa = "BV";
                Navegacion navDto =
                    DAOFactory.getPersistenciaGeneralDao().findNavegacionById(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue());
                if (navDto != null) {
                    if (navDto.getEmpresa() == 1)
                        empresa = "BV";
                    if (navDto.getEmpresa() == 2)
                        empresa = "BH";
                }
                transaccion =
                    DAOFactory.getPersistenciaGeneralDao().newTransaccion(origenTransaccion, empresa, medio,
                                                                          res.getResumenConHeader().getTurno().getTime(),
                                                                          res.getResumenConHeader().getTotalPagar().intValue(),
                                                                          res.getResumenConHeader().getFechaConsulta().getTime(),
                                                                          res.getResumenConHeader().getRutCliente(),
                                                                          res.getResumenConHeader().getNombreCliente());
                DAOFactory.getPersistenciaGeneralDao().updateTipoTransaccion(transaccion.getIdTransaccion().intValue(),
                                                                             tipoTransaccion);
            } else {
                transaccion =
                    DAOFactory.getPersistenciaGeneralDao().updateTransaccion(res.getResumenConHeader().getIdTransaccion().intValue(),
                                                                             medio,
                                                                             res.getResumenConHeader().getTotalPagar().intValue(),
                                                                             res.getResumenConHeader().getTurno().getTime());
            }
            logger.info("crearConfirmacionConXmlSeleccionado() - actualiza la informacion de la transaccion en la tabla de navegacion");
            DAOFactory.getPersistenciaGeneralDao().updateTransaccionInNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                                 transaccion.getIdTransaccion(),
                                                                                 transaccion.getMontoTotal(), medio);

            logger.info("crearConfirmacionConXmlSeleccionado() - creo el xml de confirmacion y lo almaceno en la base de datos");
            confirmacion = generaXmlConfirmacion(res, transaccion.getIdTransaccion());
            validateXML(confirmacion);

            DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(confirmacion.getConfirmacion().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                           confirmacion.getConfirmacion().getInfoNavegacion().getEntrada().intValue(),
                                                                           3,
                                                                           confirmacion.getConfirmacion().getProductosPorPagar().getTotalPorPagar().getEnPesos().intValue(),
                                                                           confirmacion.toString(), idCanal);
            code = confirmacion.toString();

            logger.info("crearConfirmacionConXmlSeleccionado() - lleno el detalle de la transaccion");
            //TODO: Revisar SQL
            DAOFactory.getPersistenciaGeneralDao().updateDetalleTransaccion(transaccion, detallesByEmp, comprobantes);
        } catch (Exception e) {
            code = "NOK|Error: " + e.getMessage();
            logger.error("crearConfirmacionConXmlSeleccionado() - NOK|Error: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            logger.info("crearConfirmacionConXmlSeleccionado() - termino");
            return code;
        }

    }

    /**
     * Crea la confirmacion con los productos
     * ya listos y seleccionados.
     * @param xmlResumenSeleccionado
     * @return
     */
    public String crearConfirmacionConXmlSeleccionadoAPT(String origenTransaccion, String xmlResumenSeleccionado,
                                                         int idCanal, int tipoTransaccion, int cargoExtra, String email) {
        logger.info("crearConfirmacionConXmlSeleccionado() - inicio");
        this.email = email;
        detallesByEmp = null;
        comprobantes = null;

        ConfirmacionDocument confirmacion;
        String code = "NOK|Error: Inesperado";
        try {
            ResumenConHeaderDocument res = ResumenConHeaderDocument.Factory.parse(xmlResumenSeleccionado);
            validateXML(res);
            logger.info("crearConfirmacionConXmlSeleccionado() - busco el medio de pago seleccionado");
            int medio = 0;
            for (int i = 0; i < res.getResumenConHeader().getConvenios().getConvenioArray().length; i++) {
                if (res.getResumenConHeader().getConvenios().getConvenioArray(i).getSeleccionado()) {
                    medio = res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue();
                }
            }
            logger.info("crearConfirmacionConXmlSeleccionado() - creo la transaccion");
            Transacciones transaccion;
            if (res.getResumenConHeader().getIdTransaccion() == null) {
                String empresa = "BV";
                Navegacion navDto =
                    DAOFactory.getPersistenciaGeneralDao().findNavegacionById(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue());
                if (navDto != null) {
                    if (navDto.getEmpresa() == 1)
                        empresa = "BV";
                    if (navDto.getEmpresa() == 2)
                        empresa = "BH";
                }
                transaccion =
                    DAOFactory.getPersistenciaGeneralDao().newTransaccion(origenTransaccion, empresa, medio,
                                                                          res.getResumenConHeader().getTurno().getTime(),
                                                                          res.getResumenConHeader().getTotalPagar().intValue(),
                                                                          res.getResumenConHeader().getFechaConsulta().getTime(),
                                                                          res.getResumenConHeader().getRutCliente(),
                                                                          res.getResumenConHeader().getNombreCliente());
                DAOFactory.getPersistenciaGeneralDao().updateTipoTransaccionAPT(transaccion.getIdTransaccion().intValue(),
                                                                                tipoTransaccion, cargoExtra);

            } else {
                transaccion =
                    DAOFactory.getPersistenciaGeneralDao().updateTransaccion(res.getResumenConHeader().getIdTransaccion().intValue(),
                                                                             medio,
                                                                             res.getResumenConHeader().getTotalPagar().intValue(),
                                                                             res.getResumenConHeader().getTurno().getTime());
                DAOFactory.getPersistenciaGeneralDao().updateTipoTransaccionAPT(transaccion.getIdTransaccion().intValue(),
                                                                                tipoTransaccion, cargoExtra);
            }
            logger.info("crearConfirmacionConXmlSeleccionado() - actualiza la informacion de la transaccion en la tabla de navegacion");
            DAOFactory.getPersistenciaGeneralDao().updateTransaccionInNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                                 transaccion.getIdTransaccion(),
                                                                                 transaccion.getMontoTotal(), medio);

            logger.info("crearConfirmacionConXmlSeleccionado() - creo el xml de confirmacion y lo almaceno en la base de datos");
            confirmacion = generaXmlConfirmacionAPT(res, transaccion.getIdTransaccion(), cargoExtra);
            validateXML(confirmacion);

            DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(confirmacion.getConfirmacion().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                           confirmacion.getConfirmacion().getInfoNavegacion().getEntrada().intValue(),
                                                                           3,
                                                                           confirmacion.getConfirmacion().getProductosPorPagar().getTotalPorPagar().getEnPesos().intValue(),
                                                                           confirmacion.toString(), idCanal);
            code = confirmacion.toString();

            logger.info("crearConfirmacionConXmlSeleccionado() - lleno el detalle de la transaccion");
            //TODO: Revisar SQL
            DAOFactory.getPersistenciaGeneralDao().updateDetalleTransaccion(transaccion, detallesByEmp, comprobantes);
        } catch (Exception e) {
            code = "NOK|Error: " + e.getMessage();
            logger.error("crearConfirmacionConXmlSeleccionado() - NOK|Error: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            logger.info("crearConfirmacionConXmlSeleccionado() - termino");
            return code;
        }

    }

    /**
     * Confecciona el resumen y pago de confirmacion
     * @param xmlResumenPagos
     * @param req
     * @return
     */
    public String generaXmlResumenPagosConProductosSeleccionados(String xmlResumenPagos, ResumenRequest req,
                                                                 int idCanal) {
        String resp = null;
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - inicio");
        ResumenConHeaderDocument res = null;
        try {
            res = ResumenConHeaderDocument.Factory.parse(xmlResumenPagos);
            validateXML(res);
            generaXmlResumenPagosConProductosSeleccionados(res, req);
            validateXML(res);

            //============================================================================
            // VALIDA QUE LOS PRODUCTOS SELECCIONADOS TENGAN UNA SECUENCIA DE PAGO DESEADA
            //============================================================================
            logger.info("EJB: PRODUCTO ===> INICIALIZANDO PRODUCTO");
            noNamespace.ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada[] selvericar =
                res.getResumenConHeader().getProductosPorPagar().getEntradaArray();
            if (selvericar != null && selvericar.length > 0) {
                for (int pu = 0; pu < selvericar.length; pu++) {

                    //SOLO DIVIDENDOS HIPOTECARIOS
                    if (selvericar[pu].getInfoCajas().getCreditos() != null &&
                        selvericar[pu].getInfoCajas().getCreditos().getNumDividendo() != null) {

                        Integer operacion = selvericar[pu].getProducto().getNumProducto().intValue();
                        Integer dividendo = selvericar[pu].getInfoCajas().getCreditos().getNumDividendo().intValue();

                        //PRODUCTO SELECCIONADO ANALIZO QUE ESTE OK DENTRO DE LOS DIVIDENDOS MAS ANTIGUOS A PAGAR
                        if (selvericar[pu].getSeleccionado()) {
                            logger.info("EJB: PRODUCTO ===> SELECCIONADO NUM PRODCUTO:" + operacion + " DIVIDENDO:" +
                                         dividendo);

                            //VERIFICA QUE HAYA UNO MENOR O NADA SELECCIONADO AL QUE SE DESE UTILIZAR
                            for (int yaetan = 0; yaetan < selvericar.length; yaetan++) {

                                //SOLO OPERO CON LA OPERACION DE CREDITO EXISTENTE
                                if (selvericar[yaetan].getProducto().getNumProducto().intValue() == operacion) {
                                    if (selvericar[yaetan].getInfoCajas().getCreditos().getNumDividendo().intValue() <
                                        dividendo) {
                                        if (selvericar[yaetan].getSeleccionado() ==
                                            false) {
                                            //ERROR UN DIVIDENDO MENOR SIN ESTAR SELECCIONADO
                                            resp =
        "NOK|El numero de operacion " + operacion + " posee pago de un dividendo anticipado (" + dividendo +
        ") no de puede pagar, debe pagar del mas antiguo al mas reciente.";
                                            return resp;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }

            DetalleNavegacion detallenavegacion =
                DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                               res.getResumenConHeader().getInfoNavegacion().getEntrada().intValue(),
                                                                               2,
                                                                               res.getResumenConHeader().getTotalPagar().intValue(),
                                                                               res.toString(), idCanal);
            resp = res.toString();
            /**
             * TODO: Revisar cuando no graba ya que el dato
             * puede que ya exista
             * if(detallenavegacion==null) {
                resp ="NOK|No se ha podido almacenar en la base de datos xml seleccionado";
            } else {
                resp = res.toString();
            }*/
        } catch (Exception e) {
            resp = "NOK|Error al generar xml de resumen con los productos seleccionados :" + e.getMessage();
            logger.error("generaXmlResumenPagosConProductosSeleccionados() - Excepcion :" + e.getMessage(), e);
        }
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - termino");
        return resp;

    }

    /**
     * Confecciona el resumen y pago de confirmacion
     * @param xmlResumenPagos
     * @param req
     * @return
     */
    public String generaXmlResumenPagosConProductosSeleccionadosAPVAPT(String xmlResumenPagos, ResumenRequest req,
                                                                       int idCanal, int cargoTarjetaCredito) {
        String resp = null;
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - inicio");
        ResumenConHeaderDocument res = null;
        try {
            res = ResumenConHeaderDocument.Factory.parse(xmlResumenPagos);
            validateXML(res);
            generaXmlResumenPagosConProductosSeleccionadosAPVAPT(res, req, cargoTarjetaCredito);
            validateXML(res);
            DetalleNavegacion detallenavegacion =
                DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                               res.getResumenConHeader().getInfoNavegacion().getEntrada().intValue(),
                                                                               2,
                                                                               res.getResumenConHeader().getTotalPagar().intValue(),
                                                                               res.toString(), idCanal);
            resp = res.toString();
            /**
             * TODO: Revisar cuando no graba ya que el dato
             * puede que ya exista
             * if(detallenavegacion==null) {
                resp ="NOK|No se ha podido almacenar en la base de datos xml seleccionado";
            } else {
                resp = res.toString();
            }*/
        } catch (Exception e) {
            resp = "NOK|Error al generar xml de resumen con los productos seleccionados :" + e.getMessage();
            logger.error("generaXmlResumenPagosConProductosSeleccionados() - Excepcion :" + e.getMessage(), e);
        }
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - termino");
        return resp;

    }

    public void validateXML(XmlObject xml) throws Exception {
        Boolean validar = new Boolean(ResourceBundleUtil.getProperty("xml.validation.enable"));
        if (validar.booleanValue() == false)
            return;
        logger.info("validateXML() - inicio");
        Collection errorList = new ArrayList();
        XmlOptions xo = new XmlOptions();
        logger.info("validateXML() - seteando lista de error");
        xo.setErrorListener(errorList);
        if (!xml.validate(xo)) {
            System.out.println(xml.toString());
            StringBuffer sb = new StringBuffer();
            for (Iterator it = errorList.iterator(); it.hasNext();) {
                sb.append(it.next() + "\n");
            }
            logger.info("validateXML() - XML con errores : " + sb.toString());
            String txtMsg = "XML con errores:" + sb.toString();
            logger.info("validateXML() - termino con error");
            throw new Exception(txtMsg);
        }
        logger.info("validateXML() - termino");
    }

    /**
     * Genera xml de confirmacion
     * @param res
     * @param req
     */
    public void generaXmlResumenPagosConProductosSeleccionados(ResumenConHeaderDocument res, ResumenRequest req) {
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - inicio");
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - cambio la entrada en infonavegacion");
        res.getResumenConHeader().getInfoNavegacion().setEntrada(res.getResumenConHeader().getInfoNavegacion().getEntrada().add(new BigInteger("1")));
        int contador = 0;
        boolean habilito = false;
        int totalapagarsumarizado = 0;
        for (int i = 0; i < res.getResumenConHeader().getProductosPorPagar().getEntradaArray().length; i++) {
            boolean flagprimerproducto = true; //Falg que indica que es el promer producto diferente
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada entrada =
                res.getResumenConHeader().getProductosPorPagar().getEntradaArray(i);
            if (req.getProductosSeleccionados() != null && contador < req.getProductosSeleccionados().length && 
                entrada.getContador().intValue() == req.getProductosSeleccionado(contador)) {
                
                entrada.setSeleccionado(true);
                entrada.setDisabled(false);
                habilito = true;

                int tipoCuota = entrada.getProducto().getTipoCuota();
                int pago = 0;
                if (tipoCuota == 1 || tipoCuota == 2) {
                    logger.info("generaXmlResumenPagosConProductosSeleccionados() - CUOTA FIJA y PRIMA FIJA");
                    pago = entrada.getCuota().getCuotaFija().getEnPesos().intValue();

                } else if (tipoCuota == 4) {
                    logger.info("generaXmlResumenPagosConProductosSeleccionados() - OPCION ENTRE MONTO MIN Y MAX");
                    pago = req.getOpcion(contador);
                    int cuotamin = entrada.getCuota().getCuotaOpcion().getCuotaMin().getEnPesos().intValue();
                    if (cuotamin == pago)
                        entrada.getCuota().getCuotaOpcion().getCuotaMin().setSeleccionado(true);
                    else
                        entrada.getCuota().getCuotaOpcion().getCuotaMax().setSeleccionado(true);
                } else {
                    logger.info("generaXmlResumenPagosConProductosSeleccionados() - OPCION ENTRE UN INTERVALO [MIN,MAX]");
                    pago = req.getTotal(contador);
                }

                entrada.getCuota().setPago(new BigInteger(Integer.toString(pago)));
                totalapagarsumarizado = totalapagarsumarizado + entrada.getCuota().getPago().intValue();
                contador++;
            } else {
                if (habilito) {
                    logger.info("generaXmlResumenPagosConProductosSeleccionados() - la entrada anterior fue seleccionada");
                    entrada.setDisabled(false);
                    habilito = false;
                } else if (i > 0) {
                    ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada entrada_ant =
                        res.getResumenConHeader().getProductosPorPagar().getEntradaArray(i - 1);
                    if (entrada_ant.getProducto().getCodigoProducto().intValue() ==
                        entrada.getProducto().getCodigoProducto().intValue()) {
                        logger.info("generaXmlResumenPagosConProductosSeleccionados() - es el mismo tipo de producto");
                        if (entrada_ant.getInfoCajas().getCreditos() != null) {
                            logger.info("generaXmlResumenPagosConProductosSeleccionados() - ambos son credito");
                            if (entrada_ant.getProducto().getNumProducto().intValue() ==
                                entrada.getProducto().getNumProducto().intValue()) {
                                logger.info("generaXmlResumenPagosConProductosSeleccionados() - es el mismo producto");
                                entrada.setDisabled(true);
                            } else {
                                flagprimerproducto = true; // primer producto de la secuencia que es distinto
                                if (entrada.getSeleccionado() == true) {
                                    logger.info("generaXmlResumenPagosConProductosSeleccionados() - fue anteriormente seleccionado pero ya no lo es, por lo tento se puede habilitar");
                                    entrada.setDisabled(false);
                                    flagprimerproducto = false;
                                } else {
                                    if (flagprimerproducto) {
                                        entrada.setDisabled(false);
                                    } else {
                                        entrada.setDisabled(true);
                                    }
                                }
                            }
                        } else {
                            logger.info("generaXmlResumenPagosConProductosSeleccionados() - ambos son polizas");
                            if (entrada_ant.getProducto().getNumProducto().intValue() ==
                                entrada.getProducto().getNumProducto().intValue()) {
                                logger.info("generaXmlResumenPagosConProductosSeleccionados() - es el mismo producto");
                                entrada.setDisabled(true);
                            } else {
                                flagprimerproducto = true; // primer producto de la secuencia que es distinto
                                if (entrada.getSeleccionado() == true) {
                                    logger.info("generaXmlResumenPagosConProductosSeleccionados() - fue anteriormente seleccionado pero ya no lo es, por lo tento se puede habilitar");
                                    entrada.setDisabled(false);
                                    flagprimerproducto = false;
                                } else {
                                    if (flagprimerproducto) {
                                        entrada.setDisabled(false);
                                    } else {
                                        entrada.setDisabled(true);
                                    }

                                }
                            }

                        }
                    } else if (entrada.getSeleccionado() == true) {
                        logger.info("generaXmlResumenPagosConProductosSeleccionados() - fue anteriormente seleccionado pero ya no lo es, por lo tento se puede habilitar");
                        entrada.setDisabled(false);
                    }
                }
                entrada.setSeleccionado(false);
            }
            res.getResumenConHeader().getProductosPorPagar().setEntradaArray(i, entrada);
            entrada = null;
        }


        //AQUI NO HAY QUE PASAR EL MONTO QUE OBTUVO PRODUCTO DE LA LLAMADA, SINO
        //LA SUMATORIA DE LAS LUCAS QUE CORRESPONDE CON EL PAGO
        res.getResumenConHeader().getProductosPorPagar().setTotalPorPagar(new BigInteger("" + totalapagarsumarizado));
        //res.getResumenConHeader().getProductosPorPagar().setTotalPorPagar(new BigInteger(Integer.toString(req.getTotal_pagar())));

        logger.info("generaXmlResumenPagosConProductosSeleccionados() - Limpio los bancos--------");
        /**
          * Limpiar todos y setear 1 solo convenio seleccionado
          */
        int largo = res.getResumenConHeader().getConvenios().getConvenioArray().length;
        for (int banquitos = 0; banquitos < largo; banquitos++) {
            res.getResumenConHeader().getConvenios().getConvenioArray(banquitos).setSeleccionado(false);
            if (res.getResumenConHeader().getConvenios().getConvenioArray(banquitos).getCodigo().intValue() ==
                req.getBanco()) {
                res.getResumenConHeader().getConvenios().getConvenioArray(banquitos).setSeleccionado(true);
            }
        }

        res.getResumenConHeader().setTotalPagar(new BigInteger(Integer.toString(req.getTotal_pagar())));
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - termino");

    }


    /**
     * Genera xml de confirmacion
     * @param res
     * @param req
     */
    public void generaXmlResumenPagosConProductosSeleccionadosAPVAPT(ResumenConHeaderDocument res, ResumenRequest req,
                                                                     int cargoTarjetaCredito) {
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - inicio");
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - cambio la entrada en infonavegacion");
        res.getResumenConHeader().getInfoNavegacion().setEntrada(res.getResumenConHeader().getInfoNavegacion().getEntrada().add(new BigInteger("1")));
        int contador = 0;
        boolean habilito = false;
        for (int i = 0; i < res.getResumenConHeader().getProductosPorPagar().getEntradaArray().length; i++) {
            boolean flagprimerproducto = true; //Falg que indica que es el promer producto diferente
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada entrada =
                res.getResumenConHeader().getProductosPorPagar().getEntradaArray(i);
            if (req.getProductosSeleccionados() != null && contador < req.getProductosSeleccionados().length &&
                entrada.getContador().intValue() == req.getProductosSeleccionado(contador)) {
                entrada.setSeleccionado(true);
                entrada.setDisabled(false);
                habilito = true;

                int tipoCuota = entrada.getProducto().getTipoCuota();
                int pago = 0;
                /*if (tipoCuota==1 || tipoCuota==2){
                     logger.info("generaXmlResumenPagosConProductosSeleccionados() - CUOTA FIJA y PRIMA FIJA");
                     pago=entrada.getCuota().getCuotaFija().getEnPesos().intValue();

                 }else if (tipoCuota==4){
                     logger.info("generaXmlResumenPagosConProductosSeleccionados() - OPCION ENTRE MONTO MIN Y MAX");
                     pago=req.getOpcion(contador);
                     int cuotamin = entrada.getCuota().getCuotaOpcion().getCuotaMin().getEnPesos().intValue();
                     if(cuotamin==pago)
                         entrada.getCuota().getCuotaOpcion().getCuotaMin().setSeleccionado(true);
                     else entrada.getCuota().getCuotaOpcion().getCuotaMax().setSeleccionado(true);
                  }else{
                     logger.info("generaXmlResumenPagosConProductosSeleccionados() - OPCION ENTRE UN INTERVALO [MIN,MAX]");
                     pago=req.getTotal(contador);
                  } */

                logger.info("generaXmlResumenPagosConProductosSeleccionados() - OPCION ENTRE UN INTERVALO [MIN,MAX]");
                pago = req.getTotal(contador);

                entrada.getCuota().setPago(new BigInteger(Integer.toString(pago)));
                contador++;
            } else {
                if (habilito) {
                    logger.info("generaXmlResumenPagosConProductosSeleccionados() - la entrada anterior fue seleccionada");
                    entrada.setDisabled(false);
                    habilito = false;
                } else if (i > 0) {
                    ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada entrada_ant =
                        res.getResumenConHeader().getProductosPorPagar().getEntradaArray(i - 1);
                    if (entrada_ant.getProducto().getCodigoProducto().intValue() ==
                        entrada.getProducto().getCodigoProducto().intValue()) {
                        logger.info("generaXmlResumenPagosConProductosSeleccionados() - es el mismo tipo de producto");
                        if (entrada_ant.getInfoCajas().getCreditos() != null) {
                            logger.info("generaXmlResumenPagosConProductosSeleccionados() - ambos son credito");
                            if (entrada_ant.getProducto().getNumProducto().intValue() ==
                                entrada.getProducto().getNumProducto().intValue()) {
                                logger.info("generaXmlResumenPagosConProductosSeleccionados() - es el mismo producto");
                                entrada.setDisabled(true);
                            } else {
                                flagprimerproducto = true; // primer producto de la secuencia que es distinto
                                if (entrada.getSeleccionado() == true) {
                                    logger.info("generaXmlResumenPagosConProductosSeleccionados() - fue anteriormente seleccionado pero ya no lo es, por lo tento se puede habilitar");
                                    entrada.setDisabled(false);
                                    flagprimerproducto = false;
                                } else {
                                    if (flagprimerproducto) {
                                        entrada.setDisabled(false);
                                    } else {
                                        entrada.setDisabled(true);
                                    }
                                }
                            }
                        } else {
                            logger.info("generaXmlResumenPagosConProductosSeleccionados() - ambos son polizas");
                            if (entrada_ant.getProducto().getNumProducto().intValue() ==
                                entrada.getProducto().getNumProducto().intValue()) {
                                logger.info("generaXmlResumenPagosConProductosSeleccionados() - es el mismo producto");
                                entrada.setDisabled(true);
                            } else {
                                flagprimerproducto = true; // primer producto de la secuencia que es distinto
                                if (entrada.getSeleccionado() == true) {
                                    logger.info("generaXmlResumenPagosConProductosSeleccionados() - fue anteriormente seleccionado pero ya no lo es, por lo tento se puede habilitar");
                                    entrada.setDisabled(false);
                                    flagprimerproducto = false;
                                } else {
                                    if (flagprimerproducto) {
                                        entrada.setDisabled(false);
                                    } else {
                                        entrada.setDisabled(true);
                                    }

                                }
                            }

                        }
                    } else if (entrada.getSeleccionado() == true) {
                        logger.info("generaXmlResumenPagosConProductosSeleccionados() - fue anteriormente seleccionado pero ya no lo es, por lo tento se puede habilitar");
                        entrada.setDisabled(false);
                    }
                }
                entrada.setSeleccionado(false);
            }
            res.getResumenConHeader().getProductosPorPagar().setEntradaArray(i, entrada);
            entrada = null;
        }

        res.getResumenConHeader().getProductosPorPagar().setTotalPorPagar(new BigInteger(Integer.toString(req.getTotal_pagar())));
        res.getResumenConHeader().getProductosPorPagar().setTotalDeuda(new BigInteger(Integer.toString(cargoTarjetaCredito)));

        logger.info("generaXmlResumenPagosConProductosSeleccionados() - Limpio los bancos--------");
        /**
          * Limpiar todos y setear 1 solo convenio seleccionado
          */
        int largo = res.getResumenConHeader().getConvenios().getConvenioArray().length;
        for (int banquitos = 0; banquitos < largo; banquitos++) {
            res.getResumenConHeader().getConvenios().getConvenioArray(banquitos).setSeleccionado(false);
            if (res.getResumenConHeader().getConvenios().getConvenioArray(banquitos).getCodigo().intValue() ==
                req.getBanco()) {
                res.getResumenConHeader().getConvenios().getConvenioArray(banquitos).setSeleccionado(true);
            }
        }

        res.getResumenConHeader().setTotalPagar(new BigInteger(Integer.toString(req.getTotal_pagar())));


        logger.info("generaXmlResumenPagosConProductosSeleccionados() - termino");

    }

    /**
     * Generacion del XML de confirmacion de pago
     * @param res
     * @param idTransaccion
     * @return
     */
    public ConfirmacionDocument generaXmlConfirmacion(ResumenConHeaderDocument res, long idTransaccion) {

        String XMLPagoConvenio = "";
        logger.info("generaXmlConfirmacion() - inicio");
        ConfirmacionDocument conf = ConfirmacionDocument.Factory.newInstance();
        ConfirmacionDocument.Confirmacion newConfirmacion = conf.addNewConfirmacion();

        logger.info("generaXmlConfirmacion() - Copia IdentificacionMensaje  ");
        ConfirmacionDocument.Confirmacion.IdentificacionMensaje idm;
        idm = newConfirmacion.addNewIdentificacionMensaje();
        idm.setCodigo("CONFIR");
        idm.setVersion("1.0");
        idm.setDe("PAGWEB");
        idm.setFechaCreacion(java.util.Calendar.getInstance());
        idm.setAccion("CONFIRMAR");

        logger.info("generaXmlConfirmacion() - Informacion de la navegacion");
        ConfirmacionDocument.Confirmacion.InfoNavegacion in = newConfirmacion.addNewInfoNavegacion();
        in.setIDNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion());
        in.setEntrada(res.getResumenConHeader().getInfoNavegacion().getEntrada().add(new BigInteger("1")));

        newConfirmacion.setIdTransaccion(new BigInteger(Long.toString(idTransaccion)));
        newConfirmacion.setRutCliente(res.getResumenConHeader().getRutCliente());
        newConfirmacion.setNombreCliente(res.getResumenConHeader().getNombreCliente());
        newConfirmacion.setFechaConsulta(res.getResumenConHeader().getFechaConsulta());
        newConfirmacion.setFechaUF(res.getResumenConHeader().getFechaUF());
        newConfirmacion.setValorUF(res.getResumenConHeader().getValorUF());

        logger.info("generaXmlConfirmacion() - Copia ProductosPorPagar");
        ConfirmacionDocument.Confirmacion.ProductosPorPagar productos = newConfirmacion.addNewProductosPorPagar();
        noNamespace.ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada[] listEntradas =
            res.getResumenConHeader().getProductosPorPagar().getEntradaArray();
        int contador = 1;

        //DETALLE PARA POLIZAS
        DetalleTransaccionByEmp detallePoliza = new DetalleTransaccionByEmp();
        detallePoliza.setCodEmpresa(1);
        detallePoliza.setFechahora(res.getResumenConHeader().getFechaConsulta().getTime());
        detallePoliza.setIdTransaccion(idTransaccion);
        detallePoliza.setMontoTotal(0);

        //DETALLE PARA DIVIDENDOS
        DetalleTransaccionByEmp detalleDiv = new DetalleTransaccionByEmp();
        detalleDiv.setCodEmpresa(2);
        detalleDiv.setFechahora(res.getResumenConHeader().getFechaConsulta().getTime());
        detalleDiv.setIdTransaccion(idTransaccion);
        detalleDiv.setMontoTotal(0);

        comprobantes = new ArrayList();
        List info_comprobantes = new ArrayList();
        int cod_empresa = 0;
        try {
            //SETEO DE INFORMACION GLOBAL PARA DIVIDENDOS Y POLIZAS
            for (int i = 0; i < listEntradas.length; i++) {
                if (listEntradas[i].getSeleccionado()) {
                    ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = productos.addNewEntrada();
                    entrada.setContador(new BigInteger(Integer.toString(contador)));
                    contador++;


                    logger.info("generaXmlConfirmacion() - datos del producto");
                    ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Producto prod =
                        entrada.addNewProducto();
                    prod.setCodigoProducto(listEntradas[i].getProducto().getCodigoProducto());
                    prod.setDescripcionProducto(listEntradas[i].getProducto().getDescripcionProducto());
                    prod.setNumProducto(listEntradas[i].getProducto().getNumProducto());
                    prod.setCodEmpresa(listEntradas[i].getProducto().getCodEmpresa());

                    entrada.setInfoCajas(listEntradas[i].getInfoCajas());


                    Comprobantes comprobante = new Comprobantes();
                    comprobante.setCodProducto(prod.getCodigoProducto().intValue());
                    comprobante.setIdTransaccion(idTransaccion);
                    comprobante.setNumProducto(prod.getNumProducto().longValue());

                    if (entrada.getInfoCajas().getCreditos() != null)
                        comprobante.setCuota(entrada.getInfoCajas().getCreditos().getNumDividendo().intValue());
                    else {
                        comprobante.setCuota(entrada.getInfoCajas().getPolizas().getFolio().intValue());
                    }

                    logger.info("generaXmlConfirmacion() - Informacion sobre la cuota");
                    ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Cuota newCuota = entrada.addNewCuota();
                    newCuota.setFechaVencimiento(listEntradas[i].getCuota().getFechaVencimiento());

                    ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Cuota.Monto newMonto =
                        newCuota.addNewMonto();
                    int tipoCuota = listEntradas[i].getProducto().getTipoCuota();

                    newMonto.setEnPesos(listEntradas[i].getCuota().getPago());
                    comprobante.setMontoBase(newMonto.getEnPesos().intValue());
                    comprobante.setMontoExcedente(0);
                    comprobante.setMontoTotal(newMonto.getEnPesos().intValue());


                    //MONTO TOTAL PARA VIDA
                    if (prod.getCodEmpresa() == 1) {
                        detallePoliza.setMontoTotal(detallePoliza.getMontoTotal() + newMonto.getEnPesos().intValue());
                    } else {
                        //MONTO TOTAL PARA HIPOTECARIA
                        detalleDiv.setMontoTotal(detalleDiv.getMontoTotal() + newMonto.getEnPesos().intValue());
                    }

                    logger.info("generaXmlConfirmacion() - seteando el valor en uf y el detalle");
                    noNamespace.DetalleCargos newDetalle = newMonto.addNewDetalle();

                    if (tipoCuota == 1 || tipoCuota == 2) {
                        newMonto.setEnUF(listEntradas[i].getCuota().getCuotaFija().getEnUF());
                        logger.info("generaXmlConfirmacion() - Copiar Detalle");
                        newDetalle = listEntradas[i].getCuota().getCuotaFija().getDetalle();
                        newMonto.setDetalle(newDetalle);
                    } else if (tipoCuota == 3) {
                        BigInteger montopp;

                        if (listEntradas[i].getCuota().getCuotaVariable().getCuotaVariable() != null) {
                            newMonto.setEnUF(listEntradas[i].getCuota().getCuotaVariable().getCuotaVariable().getEnUF());
                            montopp =
                                listEntradas[i].getCuota().getCuotaVariable().getCuotaVariable().getEnPesos().add(listEntradas[i].getCuota().getCuotaVariable().getCuotaFija().getEnPesos());
                        } else {
                            newMonto.setEnUF(listEntradas[i].getCuota().getCuotaVariable().getCuotaFija().getEnUF());
                            montopp = listEntradas[i].getCuota().getCuotaVariable().getCuotaFija().getEnPesos();
                        }

                        //System.out.println("MONTO PAGADO "+montopp+" MONTO TOTAL "+listEntradas[i].getCuota().getPago().intValue());
                        if (montopp.intValue() == listEntradas[i].getCuota().getPago().intValue()) {
                            logger.info("generaXmlConfirmacion() - Copiar Detalle");
                            newDetalle = listEntradas[i].getCuota().getCuotaVariable().getCuotaFija().getDetalle();
                            newMonto.setDetalle(newDetalle);
                        } else {
                            noNamespace.DetalleCargos.Cargo cargo = newDetalle.addNewCargo();
                            cargo.setDescripcion("Abono");
                            String detalleuf =
                                NumeroUtil.redondear(listEntradas[i].getCuota().getPago().intValue() /
                                                     res.getResumenConHeader().getValorUF().doubleValue(), 4);
                            cargo.setBigDecimalValue(new BigDecimal(detalleuf));
                            comprobante.setMontoBase(0);
                            comprobante.setMontoExcedente(newMonto.getEnPesos().intValue());
                            newMonto.setDetalle(newDetalle);
                        }
                    } else {
                        if (listEntradas[i].getCuota().getPago().intValue() ==
                            listEntradas[i].getCuota().getCuotaOpcion().getCuotaMin().getEnPesos().intValue()) {
                            newMonto.setEnUF(listEntradas[i].getCuota().getCuotaOpcion().getCuotaMin().getEnUF());
                            newDetalle =
                                (DetalleCargos) listEntradas[i].getCuota().getCuotaOpcion().getCuotaMin().getDetalle().copy();
                            newMonto.setDetalle(newDetalle);

                        } else if (listEntradas[i].getCuota().getPago().intValue() ==
                                   listEntradas[i].getCuota().getCuotaOpcion().getCuotaMax().getEnPesos().intValue()) {
                            newMonto.setEnUF(listEntradas[i].getCuota().getCuotaOpcion().getCuotaMax().getEnUF());
                            newDetalle = listEntradas[i].getCuota().getCuotaOpcion().getCuotaMax().getDetalle();
                            newMonto.setDetalle(newDetalle);
                        } else if (tipoCuota == 5) {
                            //newMonto.setEnUF(listEntradas[i].getCuota().getCuotaOpcion().getCuotaMin().getEnUF());
                            newDetalle = listEntradas[i].getCuota().getCuotaOpcion().getCuotaMin().getDetalle();
                            noNamespace.DetalleCargos.Cargo cargo = newDetalle.addNewCargo();
                            cargo.setDescripcion("Cuenta Excedente");
                            int excedente =
                                (listEntradas[i].getCuota().getPago().intValue() -
                                 listEntradas[i].getCuota().getCuotaOpcion().getCuotaMin().getEnPesos().intValue());
                            String detalleuf =
                                NumeroUtil.redondear(excedente / res.getResumenConHeader().getValorUF().doubleValue(),
                                                     4);
                            Double valorUFDg = new Double(detalleuf);
                            cargo.setBigDecimalValue(new BigDecimal(detalleuf));

                            comprobante.setMontoBase(listEntradas[i].getCuota().getCuotaOpcion().getCuotaMin().getEnPesos().intValue());
                            comprobante.setMontoExcedente(excedente);


                            Double montominuf =
                                new Double(listEntradas[i].getCuota().getCuotaOpcion().getCuotaMin().getEnUF().toString());
                            String totaluf = NumeroUtil.redondear(new Double(montominuf + valorUFDg), 4);

                            newMonto.setEnUF(new BigDecimal(totaluf));

                            newMonto.setDetalle(newDetalle);
                        } else {
                            throw new Exception("Ha habido un error al procesar el detalle de la cuota");
                        }
                    }

                    java.util.Date fechavenci = new java.util.Date(newCuota.getFechaVencimiento().getTimeInMillis());
                    String fechavencistr = FechaUtil.getFechaFormateoCustom(fechavenci, "yyyyMMdd");

                    String codigo_boleta;
                    codigo_boleta = "" + prod.getNumProducto() + comprobante.getCuota() + idTransaccion;

                    String[] info_prodpagado = new String[6];
                    info_prodpagado[0] = Integer.toString(prod.getCodEmpresa());
                    info_prodpagado[1] = comprobante.getMontoTotal().toString();
                    info_prodpagado[2] = prod.getDescripcionProducto();
                    //-------------datos adicionales-----------
                    info_prodpagado[3] = prod.getCodigoProducto().toString();
                    info_prodpagado[4] = fechavencistr;
                    info_prodpagado[5] = codigo_boleta;
                    cod_empresa = prod.getCodEmpresa();


                    info_comprobantes.add(info_prodpagado);
                    comprobantes.add(comprobante);
                }
            }
            contador--;
            logger.info("generaXmlConfirmacion() - decremento contador ya que partimos con 1");

            ConfirmacionDocument.Confirmacion.ProductosPorPagar.TotalPorPagar newTotalPorPagar =
                productos.addNewTotalPorPagar();
            newTotalPorPagar.setEnPesos(res.getResumenConHeader().getTotalPagar());

            String total =
                NumeroUtil.redondear(res.getResumenConHeader().getTotalPagar().intValue() /
                                     res.getResumenConHeader().getValorUF().doubleValue(), 4);
            newTotalPorPagar.setEnUF(new BigDecimal(total));


            ConfirmacionDocument.Confirmacion.Convenio newConvenio = conf.getConfirmacion().addNewConvenio();
            for (int i = 0; i < res.getResumenConHeader().getConvenios().getConvenioArray().length; i++) {
                if (res.getResumenConHeader().getConvenios().getConvenioArray(i).getSeleccionado()) {
                    newConvenio.setCodigo(res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo());
                    newConvenio.setDescripcion(res.getResumenConHeader().getConvenios().getConvenioArray(i).getDescripcion());

                    logger.info("generaXmlConfirmacion() - seteo detalle de transacciones por empresa");
                    detallesByEmp = new ArrayList();
                    if (detalleDiv.getMontoTotal() == 0) {
                        detalleDiv = null;
                    } else {
                        detalleDiv.setCodMedio(res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                        detallesByEmp.add(detalleDiv);
                    }
                    if (detallePoliza.getMontoTotal() == 0) {
                        detallePoliza = null;
                    } else {
                        detallePoliza.setCodMedio(res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                        detallesByEmp.add(detallePoliza);
                    }

                    /**
                   * Este codigo determina cual es el medio de pago
                   * que el cliente escogio para cancelar y se adjunta
                   * la informacion de redireccionamiento web para lanzar
                   * la pantalla de pago al cliente.
                   */
                    CodEmpByMedio codpolizas = null;
                    CodEmpByMedio coddividendos = null;
                    CodEmpByMedio cod_bicevida = null;
                    if (detallePoliza != null) {
                        cod_bicevida =
                            codpolizas =
                            DAOFactory.getPersistenciaGeneralDao().findCodByEmpInMedio(1,
                                                                                       res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                    }
                    if (detalleDiv != null) {
                        cod_bicevida =
                            coddividendos =
                            DAOFactory.getPersistenciaGeneralDao().findCodByEmpInMedio(2,
                                                                                       res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                    }

                    newConvenio.setURL(cod_bicevida.getUrl());


                    /**
                   * Generaci�n de XML a ser enviados por metodo Post
                   */
                    java.util.Date fechapago = new java.util.Date(System.currentTimeMillis());


                    /**
                   * Codigo que genera XML para Banco Chile y Santander Santiago
                   */
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOCHILE ||
                        newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_SANTANDER) {
                        MPINIDocument docMpini = MPINIDocument.Factory.newInstance();
                        MPINIDocument.MPINI mpini = docMpini.addNewMPINI();
                        mpini.setIDCOM(cod_bicevida.getIDCOM());
                        String idtrx = Long.toString(idTransaccion);
                        while (idtrx.length() < 6) {
                            idtrx = "0" + idtrx;
                        }
                        idtrx = cod_bicevida.getIDCOM().toString() + idtrx.substring(idtrx.length() - 6);
                        mpini.setIDTRX(idtrx);
                        mpini.setTOTAL(newTotalPorPagar.getEnPesos().longValue());
                        mpini.setNROPAGOS(contador);

                        for (int j = 0; j < info_comprobantes.size(); j++) {
                            String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                            DETALLEDocument.DETALLE det = mpini.addNewDETALLE();
                            if (info_prodpagado[0].compareTo("1") == 0)
                                det.setSRVREC(codpolizas.getSRVREC().intValue());
                            else
                                det.setSRVREC(coddividendos.getSRVREC().intValue());
                            det.setMONTO(info_prodpagado[1]);
                            String glosa = info_prodpagado[2];
                            logger.info("generaXmlConfirmacion() - glosa MPINI: " + glosa);
                            glosa = glosa.replaceAll("�", "a");
                            glosa = glosa.replaceAll("�", "e");
                            glosa = glosa.replaceAll("�", "i");
                            glosa = glosa.replaceAll("�", "o");
                            glosa = glosa.replaceAll("�", "u");
                            logger.info("generaXmlConfirmacion() - glosa MPINI truncada: " + glosa);
                            if (glosa.length() > 30)
                                glosa = glosa.substring(0, 29);
                            det.setGLOSA(glosa);
                            det.setCANTIDAD("0001");
                            det.setPRECIO(info_prodpagado[1]);
                            String datos_adic = idtrx;
                            String rut = Integer.toString(newConfirmacion.getRutCliente());
                            while (rut.length() < 8) {
                                rut = "0" + rut;
                            }
                            datos_adic += rut;
                            if (info_prodpagado[0].compareTo("1") == 0)
                                datos_adic += codpolizas.getIDCOM();
                            else
                                datos_adic += coddividendos.getIDCOM();
                            det.setDATOADIC(datos_adic);
                        }
                        XMLPagoConvenio = docMpini.xmlText();


                        if (XMLPagoConvenio.indexOf("<SRVREC>5218</SRVREC>") > 0) {
                            XMLPagoConvenio =
                                StringUtil.replaceString(XMLPagoConvenio, "<SRVREC>5218</SRVREC>",
                                                         "<SRVREC>005218</SRVREC>");
                        } else {
                            XMLPagoConvenio =
                                StringUtil.replaceString(XMLPagoConvenio, "<SRVREC>3028</SRVREC>",
                                                         "<SRVREC>003028</SRVREC>");
                        }


                        //TODO: INSERTAR EN TABLA DE RELACION DE LLAVES IDCOMERCIO+ID6DIGITOS CON IDTRANSACCION REAL
                        java.util.Date fecha = new java.util.Date();
                        DAOFactory.getPersistenciaGeneralDao().insertarHomologacionPKConvenio(newConvenio.getCodigo().longValue(),
                                                                                              idTransaccion,
                                                                                              mpini.getIDTRX(), fecha);
                        
                        if(newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOCHILE) {
                            //generamos nueva logica de envio Banco Chile.
                            if(cod_bicevida.getCodEmpresa() == 2) {//Bice Hipotecaria
                                XMLPagoConvenio = generaNuevoXMLBChile(XMLPagoConvenio
                                                                       , ResourceBundleUtil.getProperty("bcochile.hipotecaria.firma.url")
                                                                       , ResourceBundleUtil.getProperty("bice.mediopago.cgi.bice.bancochile.hipotecaria.path.key"));
                            } else {//BIcevida
                                XMLPagoConvenio = generaNuevoXMLBChile(XMLPagoConvenio
                                                                       , ResourceBundleUtil.getProperty("bcochile.vida.firma.url")
                                                                       , ResourceBundleUtil.getProperty("bice.mediopago.cgi.bice.bancochile.vida.path.key"));
                            }
                            
                        }

                    }

                    /**
                   * Codigo que genera XML para WebPay
                   */
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_WEBPAY) {
                        String idtrx = Long.toString(idTransaccion);
                        while (idtrx.length() < 6) {
                            idtrx = "0" + idtrx;
                        }
                        XMLPagoConvenio =
                            "<WebPay>" + "<IdTrx>" + idtrx + "</IdTrx>" + "<IdCom>" + cod_bicevida.getIDCOM() +
                            "</IdCom>" + "<Monto>" + newTotalPorPagar.getEnPesos().longValue() + "</Monto>" +
                            "<NumProductos>" + info_comprobantes.size() + "</NumProductos>" + "</WebPay>";
                    }


                    /**
                   * Codigo que genera XML para BANCO BICE
                   */
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBICE) {

                        PagoElectronicoBancoBICEVO voBice = new PagoElectronicoBancoBICEVO();
                        voBice.setIdCliente(cod_bicevida.getIDCOM().toString());
                        voBice.setIdTransaccion(Long.toString(idTransaccion));
                        voBice.setMontoTotal(newTotalPorPagar.getEnPesos().doubleValue());

                        //AGREGA DETALLE DE PAGO
                        for (int j = 0; j < info_comprobantes.size(); j++) {
                            String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                            String tipoVia = info_prodpagado[0]; //Tivo Dividendo o Poliza
                            String monprod = info_prodpagado[1]; //Monto Producto
                            String descripprod = info_prodpagado[2]; //Monto Producto
                            String codigotipoProducto = info_prodpagado[3]; //1060, 1061, etc
                            String numContraOperacion = info_prodpagado[5]; // numero contrato operacion

                            //BUSCA EL NOMBRE DE PRODUCTO SIN ACENTOS
                            /*String descripcion = "";
                          String descripcionBD =  DAOFactory.getConsultasDao().getNombreProductoSinAcentos(new Long(codigotipoProducto));
                          descripcion = descripcionBD;
                          if (descripprod.length()> descripcionBD.length()) descripcion+= descripprod.substring(descripcionBD.length());
                          */

                            //AGREGA DETALLE
                            ItemPagoElectronicoBancoBICEVO det = new ItemPagoElectronicoBancoBICEVO();
                            det.setCantidad(new Long(1));

                            if (tipoVia.equals("1"))
                                det.setDescripcion("Pago Seguro BICE Vida " + numContraOperacion); //VIDA
                            if (tipoVia.equals("2"))
                                det.setDescripcion("Pago Credito BICE Hipotecaria " + numContraOperacion); //HIPOTECARIA
                            det.setMonto(new Double(monprod));
                            voBice.addItem(det);
                        }

                        XMLPagoConvenio = voBice.toXml();

                    }


                    /**
                    * Codigo que genera XML para banco BCI y TBANC
                    */
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBCI ||
                        newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOTBANC) {
                        String identBanco = "";
                        String idenUrlRetorno = "";
                        String codigoBancoCGI = "";
                        String urlConfeccion = "";

                        //Identificacion del TAG
                        if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBCI)
                            identBanco = "BCI";
                        if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOTBANC)
                            identBanco = "TBanc";

                        //Identificacion de URL de Respuesta de Pago (Termino de Pago)
                        idenUrlRetorno = "$WEBCONTEXT$/faces/ReceptorPago/ReceptorComprobanteBancoBCI";

                        //Identificacion de URL de Respuesta de Pago (Termino de Pago)
                        if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBCI)
                            codigoBancoCGI = "bci";
                        if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOTBANC)
                            codigoBancoCGI = "tbc";

                        String idtrx = Long.toString(idTransaccion);
                        XMLPagoConvenio =
                            "<" + identBanco + ">" + "<IdTrx>" + idtrx + "</IdTrx>" + "<IdCom>" +
                            cod_bicevida.getIDCOM() + "</IdCom>" + "<Productos>";


                        String productosAdd = "";
                        urlConfeccion = "";
                        long monto = 0;
                        for (int j = 0; j < info_comprobantes.size(); j++) {
                            String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                            String monprod = info_prodpagado[1]; //Monto Producto
                            String numprod = info_prodpagado[3]; //Numero >Producto
                            productosAdd =
                                productosAdd + "<Identificador>" + numprod + "</Identificador><Monto>" + monprod +
                                "</Monto><Cantidad>1</Cantidad>";
                            monto = monto + Long.parseLong(monprod);
                        }
                        urlConfeccion = urlConfeccion + "PROD1|" + monto + "|1";

                        XMLPagoConvenio =
                            XMLPagoConvenio + productosAdd + "</Productos>" + "<Total>" +
                            newTotalPorPagar.getEnPesos().longValue() + "</Total>" + "<NumProductos>" +
                            info_comprobantes.size() + "</NumProductos>" + "<UrlRetorno>" + idenUrlRetorno +
                            "</UrlRetorno>" + "<CostoEnvio>0</CostoEnvio>" + "<Banco>" + codigoBancoCGI + "</Banco>" +
                            "<Action>inicio</Action>" + "<Compra>" + urlConfeccion + "</Compra>" + "</" + identBanco +
                            ">";

                        //Adaptaciones de URL para TBANC Y BCI ya que en su metodo de envio utiliza via GET y toda la informacion en la URL
                        String urlBase = newConvenio.getURL();
                        String datosAdic =
                            urlConfeccion + "&cstenv=0&pagret=" + idenUrlRetorno + "&bco=" + codigoBancoCGI + "&trx=" +
                            idtrx +
                            StringUtil.rellenarTextoIzquierda(Integer.toString(info_comprobantes.size()), 2, "0");

                        //RESETEA LA URL EN BASE DE LA INFORMACION DE LA BASE DE DATOS
                        newConvenio.setURL(urlBase + "?" + datosAdic);
                    }


                    /**
                   * Codigo que genera XML para Banco Estado
                   */
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOESTADO) {
                        String idtrx = Long.toString(idTransaccion);
                        String producto = "";
                        String rutEmpresa = "";
                        if (detallePoliza != null) {
                            producto = "SEGUROS BICE VIDA";
                            rutEmpresa = "966564105"; //RUT DE VIDA
                        }
                        if (detalleDiv != null) {
                            producto = "BICE HIPOTECARIA";
                            rutEmpresa = "967770604"; //RUT DE HIPOTECARIA
                        }


                        String rut = Integer.toString(newConfirmacion.getRutCliente());
                        XMLPagoConvenio =
                            "<INICIO>" + "<ENCABEZADO>" + " <ID_SESSION>" + idtrx + "</ID_SESSION>" + " <RUT_DV_CON>" +
                            rutEmpresa + "</RUT_DV_CON>" + " <CONV_CON>" + cod_bicevida.getIDCOM() + "</CONV_CON>" +
                            " <SERVICIO>" + producto + "</SERVICIO>" + " <RUT_DV_CLIENTE>" + rut +
                            RutUtil.calculaDv(rut) + "</RUT_DV_CLIENTE>" + " <PAG_RET>" +
                            ResourceBundleUtil.getProperty("cl.bice.vida.bancoestado.contexto.url.pago.forward") +
                            "/jsp/closewindow.jsp</PAG_RET>" + " <TIPO_CONF>nomq</TIPO_CONF>" +
                            " <METODO_REND>POST</METODO_REND>" + " <PAG_REND>" +
                            ResourceBundleUtil.getProperty("cl.bice.vida.bancoestado.contexto.url.pago.forward") +
                            "/faces/ReceptorPago/ReceptorComprobanteBancoEstado</PAG_REND>" + " <BANCO>0012</BANCO>" + " <CANT_MPAGO>" +
                            info_comprobantes.size() + "</CANT_MPAGO>" + " <TOTAL>" +
                            newTotalPorPagar.getEnPesos().longValue() + "</TOTAL>" + "</ENCABEZADO>" + "<MULTIPAGO>" +
                            "  <GLOSA_MPAGO>" + producto + "</GLOSA_MPAGO>" + "  <ID_MPAGO>" + idtrx + "</ID_MPAGO>" +
                            "  <PAGO>" + "    <RUT_DV_EMP>" + rutEmpresa + "</RUT_DV_EMP>" + "    <NUM_CONV>" +
                            cod_bicevida.getIDCOM() + "</NUM_CONV>" + "    <FEC_TRX>" +
                            FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()),
                                                             "yyyyMMdd") + "</FEC_TRX>" + "    <HOR_TRX>" +
                            FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()), "HHmmss") +
                            "</HOR_TRX>" + "    <FEC_VENC>" +
                            FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()),
                                                             "yyyyMMdd") + "</FEC_VENC>" + "    <GLOSA>" + producto +
                            "</GLOSA>" + "    <COD_PAGO/>" + "    <MONTO>" + newTotalPorPagar.getEnPesos().longValue() +
                            "</MONTO>" + "  </PAGO>" + "</MULTIPAGO>" + "</INICIO>";
                    }


                    /**
                   * Codigo que genera XML para Servipag
                   */
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_SERVIPAG ||
                        newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_SERVIPAG2) {

                        /**
                       * Este indicar genera que el resultado de la operacion a enviar
                       * a servipag sea un XML con un solo detalle por el total del monto
                       * a enviar de pendiendo de su valor
                       *  en variable todoenuno.
                       *  1) valor true = Uno solo detalle del pago
                       *  2) valor false = Detalles separados por cada producto a pagar
                       */
                        boolean todoenuno = true;

                        /**
                       * Codigo para generar documento XML de servipag
                       */
                        logger.info("generaXmlConfirmacion() - Genero xml Servipag");
                        String codcanalpago = "000";
                        logger.info("generaXmlConfirmacion() - Instancia ServipagDocument");

                        Servipag docServipag = new Servipag();

                        logger.info("idTransaccion -> " + idTransaccion);
                        //Genera ID
                        String idtrx = Long.toString(idTransaccion);
                        logger.info("idTransaccion");
                        /**
                      * Caso Bicevida canal de pago 23 para servipag
                      */
                        if (cod_empresa == 1) {
                            //codcanalpago = "023";
                            codcanalpago = ResourceBundleUtil.getProperty("servipag.bicevida.codcanalpago");
                            /**
                      * Caso Bicehipotecaria canal de pago 24 para servipag
                      */
                        } else {
                            //codcanalpago = "024";
                            codcanalpago = ResourceBundleUtil.getProperty("servipag.hipotecaria.codcanalpago");
                        }


                        // EncryptSeguridadBiceUtil.crearClaves();

                        Header header = new Header();

                        String rut = Integer.toString(newConfirmacion.getRutCliente());
                        while (rut.length() < 8)
                            rut = "0" + rut;

                        //Seteo boletas a pagar
                        int comprobantes = 0;
                        long montototal = 0;
                        for (int j = 0; j < info_comprobantes.size(); j++) {
                            String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                            //Seteo Data
                            if (todoenuno == false) {
                                Documentos documentopago = new Documentos();
                                documentopago.setIdSubTx((new Long(comprobantes + 1)).toString());
                                //documentopago.setIdentificador(rut); //Rut
                                documentopago.setIdentificador(info_prodpagado[3]); //Numero Producto
                                documentopago.setBoleta(info_prodpagado[5]); //codigo_boleta = "" + prod.getNumProducto() + comprobante.getCuota()+ idTransaccion;
                                documentopago.setMonto((new Long(info_prodpagado[1])).longValue());
                                documentopago.setFechaVencimiento(info_prodpagado[4]);
                            }
                            montototal = montototal + (new Long(info_prodpagado[1])).longValue();
                            comprobantes++;
                        }
                        header.setNumeroBoletas(comprobantes);

                        /**
                       * Determina si todo sale en un solo detalle
                       */
                        String boleta = "";
                        String fechaVencimientoPago = "";
                        if (todoenuno == true) {
                            String pagos;
                            if ((comprobantes + 1) >= 10) {
                                pagos = "" + (comprobantes);
                            } else {
                                pagos = "0" + (comprobantes);
                            }

                            String[] info_prodpagado = (String[]) info_comprobantes.get(0);
                            Documentos documentopago = new Documentos();
                            documentopago.setIdSubTx((new Long(1)).toString());
                            documentopago.setIdentificador(rut); //Rut
                            //documentopago.setIdentificador(info_prodpagado[3]+pagos); //Numero Producto + nro pagos
                            //documentopago.setBoleta("1234"); //Numero Poliza
                            boleta = info_prodpagado[5] + pagos;
                            documentopago.setBoleta(boleta); //codigo_boleta + pagos = "" + prod.getNumProducto() + comprobante.getCuota()+ idTransaccion + pagos;
                            documentopago.setMonto(montototal);
                            //documentopago.setFechaVencimiento(datetool.formatFecha("yyyyMMdd", fechapago));
                            fechaVencimientoPago = info_prodpagado[4];
                            documentopago.setFechaVencimiento(fechaVencimientoPago);
                            docServipag.getDocumentos().add(documentopago);
                            header.setNumeroBoletas(1);
                        }


                        // encriptacion de los datos
                        //String datafirmar = idtrx + FechaUtil.getFechaFormateoCustom(fechapago, "yyyyMMdd") +  newTotalPorPagar.getEnPesos().toString();

                        String datafirmar =
                            codcanalpago + FechaUtil.getFechaFormateoCustom(fechapago, "yyyyMMdd") +
                            newTotalPorPagar.getEnPesos().toString();

                        datafirmar += "1" +idtrx + boleta + montototal + fechaVencimientoPago;


                        logger.info("generaXmlConfirmacion() - Data a firmar: " + datafirmar);

                        logger.info("generaXmlConfirmacion() - Inicializa algoritmo de encriptacion");
                        Signature sig = Signature.getInstance("MD5WithRSA");
                        logger.info("generaXmlConfirmacion() - Construye claves a partir de base 64");

                        String publicKey = ResourceBundleUtil.getProperty("servipag.signature.clave.publica");
                        String privateKey = ResourceBundleUtil.getProperty("servipag.signature.clave.privada");

                        logger.info("===========================================================================");
                        logger.info("Properties Public Key -> " + publicKey);
                        logger.info("Properties Private Key -> " + privateKey);
                        logger.info("===========================================================================");

                        PublicKey keyPubl = EncryptSeguridadBiceUtil.getClavePublicaByBase64(publicKey);
                        PrivateKey keyPriv = EncryptSeguridadBiceUtil.getClavePrivadaByBase64(privateKey);
                        String firmado64 =
                            EncryptSeguridadBiceUtil.firmarDataBase64(sig, keyPriv, datafirmar.getBytes());
                        logger.info("generaXmlConfirmacion() - Dato a firmado en base 64: " + firmado64);

                        logger.info("generaXmlConfirmacion() - Finaliza algoritmo de encriptacion");

                        //Comprobaci�n de de los datos encriptados en foema correcta1
                        logger.info("generaXmlConfirmacion() - Inicializa Proceso de comprobaci�n de encripataci�n");
                        byte[] firmadecode = EncryptSeguridadBiceUtil.base64decode(firmado64);
                        logger.info("generaXmlConfirmacion() - Se decodifica la firma en base 64");
                        boolean resultado =
                            EncryptSeguridadBiceUtil.verisingDataSignature(sig, keyPubl, datafirmar.getBytes(),
                                                                           firmadecode);
                        if (resultado) {
                            logger.info("generaXmlConfirmacion() - se codific� y encript� correctamente, resultado: " +
                                         resultado);
                        } else {
                            logger.error("generaXmlConfirmacion() - se codific� y encript� incorrectamente, resultado: " +
                                         resultado);
                        }

                        //Seteao Datos de Header
                        logger.info("generaXmlConfirmacion() - Seteao Datos de Header: HeaderDocumentPec.Header header = servipag.addNewHeader() ");


                        header.setFirmaEPS(firmado64);
                        //header.setFirmaEPS(" ");
                        header.setCodigoCanalPago(codcanalpago);
                        header.setIdTxPago(idtrx);
                        header.setFechaPago(FechaUtil.getFechaFormateoCustom(fechapago, "yyyyMMdd"));
                        header.setMontoTotalDeuda(newTotalPorPagar.getEnPesos().longValue());

                        /*CARGAMOS NUEVOS PARAMETROS SERVIPAG*/
                        cargaNuevosParametros(header, newConfirmacion);


                        docServipag.setHeader(header);
                        //XMLPagoConvenio = docServipag.toString();
                        XMLPagoConvenio = JAXBUtil.convertObjectTOXmlString(docServipag);
                    }


                    /**
                  * AQUI SETEAR PARAMETRO XML AL CONVENIO
                  */
                    System.out.println("========================== XML CONVENIO PARA PAGO ========================");
                    logger.info("XML BANCO ===>" + XMLPagoConvenio);
                    System.out.println("========================== XML CONVENIO PARA PAGO ========================");
                    newConvenio.setXML(XMLPagoConvenio);

                    break;
                }
            }

            conf.setConfirmacion(newConfirmacion);
            logger.info("generaXmlConfirmacion() - termino");
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
        return conf;
    }

    /**
     * Generacion del XML de confirmacion de pago
     * @param res
     * @param idTransaccion
     * @return
     */
    public ConfirmacionDocument generaXmlConfirmacionAPT(ResumenConHeaderDocument res, long idTransaccion,
                                                         int cargoExtra) throws Exception {
        String XMLPagoConvenio = "";
        logger.info("generaXmlConfirmacion() - inicio");
        logger.info("generaXmlConfirmacion() - validaciones de negocio");
        logger.info("generaXmlConfirmacion() - ejemplos de obtencion de campos");
        ConfirmacionDocument conf = ConfirmacionDocument.Factory.newInstance();
        ConfirmacionDocument.Confirmacion newConfirmacion = conf.addNewConfirmacion();

        logger.info("generaXmlConfirmacion() - Copia IdentificacionMensaje  ");
        ConfirmacionDocument.Confirmacion.IdentificacionMensaje idm;
        idm = newConfirmacion.addNewIdentificacionMensaje();
        idm.setCodigo("CONFIR");
        idm.setVersion("1.0");
        idm.setDe("PAGWEB");
        idm.setFechaCreacion(java.util.Calendar.getInstance());
        idm.setAccion("CONFIRMAR");

        logger.info("generaXmlConfirmacion() - Informacion de la navegacion");
        ConfirmacionDocument.Confirmacion.InfoNavegacion in = newConfirmacion.addNewInfoNavegacion();
        in.setIDNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion());
        in.setEntrada(res.getResumenConHeader().getInfoNavegacion().getEntrada().add(new BigInteger("1")));

        newConfirmacion.setIdTransaccion(new BigInteger(Long.toString(idTransaccion)));
        newConfirmacion.setRutCliente(res.getResumenConHeader().getRutCliente());
        newConfirmacion.setNombreCliente(res.getResumenConHeader().getNombreCliente());
        newConfirmacion.setFechaConsulta(res.getResumenConHeader().getFechaConsulta());
        newConfirmacion.setFechaUF(res.getResumenConHeader().getFechaUF());
        newConfirmacion.setValorUF(res.getResumenConHeader().getValorUF());

        logger.info("generaXmlConfirmacion() - Copia ProductosPorPagar");
        ConfirmacionDocument.Confirmacion.ProductosPorPagar productos = newConfirmacion.addNewProductosPorPagar();
        noNamespace.ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada[] listEntradas =
            res.getResumenConHeader().getProductosPorPagar().getEntradaArray();
        int contador = 1;

        DetalleTransaccionByEmp detallePoliza = new DetalleTransaccionByEmp();
        detallePoliza.setCodEmpresa(1);
        detallePoliza.setFechahora(res.getResumenConHeader().getFechaConsulta().getTime());
        detallePoliza.setIdTransaccion(idTransaccion);
        detallePoliza.setMontoTotal(0);

        DetalleTransaccionByEmp detalleDiv = new DetalleTransaccionByEmp();
        detalleDiv.setCodEmpresa(2);
        detalleDiv.setFechahora(res.getResumenConHeader().getFechaConsulta().getTime());
        detalleDiv.setIdTransaccion(idTransaccion);
        detalleDiv.setMontoTotal(0);

        comprobantes = new ArrayList();
        List info_comprobantes = new ArrayList();
        int cod_empresa = 0;

        for (int i = 0; i < listEntradas.length; i++) {
            if (listEntradas[i].getSeleccionado()) {
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = productos.addNewEntrada();
                entrada.setContador(new BigInteger(Integer.toString(contador)));
                contador++;


                logger.info("generaXmlConfirmacion() - datos del producto");
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Producto prod = entrada.addNewProducto();
                prod.setCodigoProducto(listEntradas[i].getProducto().getCodigoProducto());
                prod.setDescripcionProducto(listEntradas[i].getProducto().getDescripcionProducto());
                prod.setNumProducto(listEntradas[i].getProducto().getNumProducto());
                prod.setCodEmpresa(listEntradas[i].getProducto().getCodEmpresa());

                entrada.setInfoCajas(listEntradas[i].getInfoCajas());


                Comprobantes comprobante = new Comprobantes();
                comprobante.setCodProducto(prod.getCodigoProducto().intValue());
                comprobante.setIdTransaccion(idTransaccion);
                comprobante.setNumProducto(prod.getNumProducto().longValue());

                if (entrada.getInfoCajas().getCreditos() != null)
                    comprobante.setCuota(entrada.getInfoCajas().getCreditos().getNumDividendo().intValue());
                else {
                    comprobante.setCuota(entrada.getInfoCajas().getPolizas().getFolio().intValue());
                }

                logger.info("generaXmlConfirmacion() - Informacion sobre la cuota");
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Cuota newCuota = entrada.addNewCuota();
                newCuota.setFechaVencimiento(listEntradas[i].getCuota().getFechaVencimiento());

                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Cuota.Monto newMonto =
                    newCuota.addNewMonto();
                int tipoCuota = listEntradas[i].getProducto().getTipoCuota();

                newMonto.setEnPesos(listEntradas[i].getCuota().getPago());
                comprobante.setMontoBase(newMonto.getEnPesos().intValue());
                comprobante.setMontoExcedente(0);
                comprobante.setMontoTotal(newMonto.getEnPesos().intValue());

                if (prod.getCodEmpresa() == 1) {
                    detallePoliza.setMontoTotal(detallePoliza.getMontoTotal() + newMonto.getEnPesos().intValue());
                } else {
                    detalleDiv.setMontoTotal(detalleDiv.getMontoTotal() + newMonto.getEnPesos().intValue());
                }

                logger.info("generaXmlConfirmacion() - seteando el valor en uf y el detalle");
                noNamespace.DetalleCargos newDetalle = newMonto.addNewDetalle();

                //SOLO PROCESA CUOTA FIJA PARA APT
                newMonto.setEnUF(listEntradas[i].getCuota().getCuotaFija().getEnUF());
                logger.info("generaXmlConfirmacion() - Copiar Detalle");
                newDetalle = listEntradas[i].getCuota().getCuotaFija().getDetalle();
                newMonto.setDetalle(newDetalle);

                java.util.Date fechavenci = new java.util.Date(newCuota.getFechaVencimiento().getTimeInMillis());
                String fechavencistr = FechaUtil.getFechaFormateoCustom(fechavenci, "yyyyMMdd");

                String codigo_boleta;
                codigo_boleta = "" + prod.getNumProducto() + comprobante.getCuota() + idTransaccion;

                String[] info_prodpagado = new String[6];
                info_prodpagado[0] = Integer.toString(prod.getCodEmpresa());
                info_prodpagado[1] = comprobante.getMontoTotal().toString();
                info_prodpagado[2] = prod.getDescripcionProducto();
                //-------------datos adicionales-----------
                info_prodpagado[3] = prod.getCodigoProducto().toString();
                info_prodpagado[4] = fechavencistr;
                info_prodpagado[5] = codigo_boleta;
                cod_empresa = prod.getCodEmpresa();


                info_comprobantes.add(info_prodpagado);
                comprobantes.add(comprobante);
            }
        }
        contador--;
        logger.info("generaXmlConfirmacion() - decremento contador ya que partimos con 1");

        ConfirmacionDocument.Confirmacion.ProductosPorPagar.TotalPorPagar newTotalPorPagar =
            productos.addNewTotalPorPagar();
        newTotalPorPagar.setEnPesos(res.getResumenConHeader().getTotalPagar());

        String total =
            NumeroUtil.redondear(res.getResumenConHeader().getTotalPagar().intValue() /
                                 res.getResumenConHeader().getValorUF().doubleValue(), 4);
        newTotalPorPagar.setEnUF(new BigDecimal(total));


        ConfirmacionDocument.Confirmacion.Convenio newConvenio = conf.getConfirmacion().addNewConvenio();
        for (int i = 0; i < res.getResumenConHeader().getConvenios().getConvenioArray().length; i++) {
            if (res.getResumenConHeader().getConvenios().getConvenioArray(i).getSeleccionado()) {
                newConvenio.setCodigo(res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo());
                newConvenio.setDescripcion(res.getResumenConHeader().getConvenios().getConvenioArray(i).getDescripcion());

                logger.info("generaXmlConfirmacion() - seteo detalle de transacciones por empresa");
                detallesByEmp = new ArrayList();
                if (detalleDiv.getMontoTotal() == 0) {
                    detalleDiv = null;
                } else {
                    detalleDiv.setCodMedio(res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                    detallesByEmp.add(detalleDiv);
                }
                if (detallePoliza.getMontoTotal() == 0) {
                    detallePoliza = null;
                } else {
                    detallePoliza.setCodMedio(res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                    detallesByEmp.add(detallePoliza);
                }

                /**
                   * Este codigo determina cual es el medio de pago
                   * que el cliente escogio para cancelar y se adjunta
                   * la informacion de redireccionamiento web para lanzar
                   * la pantalla de pago al cliente.
                   */
                CodEmpByMedio codpolizas = null;
                CodEmpByMedio coddividendos = null;
                CodEmpByMedio cod_bicevida = null;
                if (detallePoliza != null) {
                    cod_bicevida =
                        codpolizas =
                        DAOFactory.getPersistenciaGeneralDao().findCodByEmpInMedio(1,
                                                                                   res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                }
                if (detalleDiv != null) {
                    cod_bicevida =
                        coddividendos =
                        DAOFactory.getPersistenciaGeneralDao().findCodByEmpInMedio(2,
                                                                                   res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                }

                newConvenio.setURL(cod_bicevida.getUrl());


                /**
                   * Generaci�n de XML a ser enviados por metodo Post
                   */
                java.util.Date fechapago = new java.util.Date(System.currentTimeMillis());


                /**
                   * Codigo que genera XML para Banco Chile y Santander Santiago
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOCHILE ||
                    newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_SANTANDER) {
                    MPINIDocument docMpini = MPINIDocument.Factory.newInstance();
                    MPINIDocument.MPINI mpini = docMpini.addNewMPINI();
                    mpini.setIDCOM(cod_bicevida.getIDCOM());
                    String idtrx = Long.toString(idTransaccion);
                    while (idtrx.length() < 6) {
                        idtrx = "0" + idtrx;
                    }
                    idtrx = cod_bicevida.getIDCOM().toString() + idtrx.substring(idtrx.length() - 6);
                    mpini.setIDTRX(idtrx);
                    mpini.setTOTAL(newTotalPorPagar.getEnPesos().longValue());
                    mpini.setNROPAGOS(contador);

                    for (int j = 0; j < info_comprobantes.size(); j++) {
                        String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                        DETALLEDocument.DETALLE det = mpini.addNewDETALLE();
                        if (info_prodpagado[0].compareTo("1") == 0)
                            det.setSRVREC(codpolizas.getSRVREC().intValue());
                        else
                            det.setSRVREC(coddividendos.getSRVREC().intValue());
                        det.setMONTO(info_prodpagado[1]);
                        String glosa = info_prodpagado[2];
                        logger.info("generaXmlConfirmacion() - glosa MPINI: " + glosa);
                        glosa = glosa.replaceAll("�", "a");
                        glosa = glosa.replaceAll("�", "e");
                        glosa = glosa.replaceAll("�", "i");
                        glosa = glosa.replaceAll("�", "o");
                        glosa = glosa.replaceAll("�", "u");
                        logger.info("generaXmlConfirmacion() - glosa MPINI truncada: " + glosa);
                        if (glosa.length() > 30)
                            glosa = glosa.substring(0, 29);
                        det.setGLOSA(glosa);
                        det.setCANTIDAD("0001");
                        det.setPRECIO(info_prodpagado[1]);
                        String datos_adic = idtrx;
                        String rut = Integer.toString(newConfirmacion.getRutCliente());
                        while (rut.length() < 8) {
                            rut = "0" + rut;
                        }
                        datos_adic += rut;
                        if (info_prodpagado[0].compareTo("1") == 0)
                            datos_adic += codpolizas.getIDCOM();
                        else
                            datos_adic += coddividendos.getIDCOM();
                        det.setDATOADIC(datos_adic);
                    }
                    XMLPagoConvenio = docMpini.xmlText();

                    if (XMLPagoConvenio.indexOf("<SRVREC>5218</SRVREC>") > 0) {
                        XMLPagoConvenio =
                            StringUtil.replaceString(XMLPagoConvenio, "<SRVREC>5218</SRVREC>",
                                                     "<SRVREC>005218</SRVREC>");
                    } else {
                        XMLPagoConvenio =
                            StringUtil.replaceString(XMLPagoConvenio, "<SRVREC>3028</SRVREC>",
                                                     "<SRVREC>003028</SRVREC>");
                    }
                    if(newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOCHILE) {
                        //generamos nueva logica de envio Banco Chile.
                        if(cod_bicevida.getCodEmpresa() == 2) {//Bice Hipotecaria
                            XMLPagoConvenio = generaNuevoXMLBChile(XMLPagoConvenio
                                                                   , ResourceBundleUtil.getProperty("bcochile.hipotecaria.firma.url")
                                                                   , ResourceBundleUtil.getProperty("bice.mediopago.cgi.bice.bancochile.hipotecaria.path.key"));
                        } else {//BIcevida
                            XMLPagoConvenio = generaNuevoXMLBChile(XMLPagoConvenio
                                                                   , ResourceBundleUtil.getProperty("bcochile.vida.firma.url")
                                                                   , ResourceBundleUtil.getProperty("bice.mediopago.cgi.bice.bancochile.vida.path.key"));
                        }
                        
                    }
                    //TODO: INSERTAR EN TABLA DE RELACION DE LLAVES IDCOMERCIO+ID6DIGITOS CON IDTRANSACCION REAL
                    java.util.Date fecha = new java.util.Date();
                    DAOFactory.getPersistenciaGeneralDao().insertarHomologacionPKConvenio(newConvenio.getCodigo().longValue(),
                                                                                          idTransaccion,
                                                                                          mpini.getIDTRX(), fecha);

                }

                /**
                   * Codigo que genera XML para WebPay
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_WEBPAY) {
                    String idtrx = Long.toString(idTransaccion);
                    while (idtrx.length() < 6) {
                        idtrx = "0" + idtrx;
                    }
                    Long totalpagoCargo = newTotalPorPagar.getEnPesos().longValue() + new Long(cargoExtra);
                    XMLPagoConvenio =
                        "<WebPay>" + "<IdTrx>" + idtrx + "</IdTrx>" + "<IdCom>" + cod_bicevida.getIDCOM() + "</IdCom>" +
                        "<Monto>" + totalpagoCargo.longValue() + "</Monto>" + "<NumProductos>" +
                        info_comprobantes.size() + "</NumProductos>" + "</WebPay>";
                }

                /**
                   * Codigo que genera XML para WebPay
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_WEBPAY_CONVENIO) {
                    String idtrx = Long.toString(idTransaccion);
                    while (idtrx.length() < 6) {
                        idtrx = "0" + idtrx;
                    }
                    Long totalpagoCargo = newTotalPorPagar.getEnPesos().longValue() + new Long(cargoExtra);
                    XMLPagoConvenio =
                        "<WebPay>" + "<IdTrx>" + idtrx + "</IdTrx>" + "<IdCom>" + cod_bicevida.getIDCOM() + "</IdCom>" +
                        "<Monto>" + totalpagoCargo.longValue() + "</Monto>" + "<NumProductos>" +
                        info_comprobantes.size() + "</NumProductos>" + "</WebPay>";
                }


                /**
                    * Codigo que genera XML para banco BCI y TBANC
                    */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBCI ||
                    newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOTBANC) {
                    String identBanco = "";
                    String idenUrlRetorno = "";
                    String codigoBancoCGI = "";
                    String urlConfeccion = "";

                    //Identificacion del TAG
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBCI)
                        identBanco = "BCI";
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOTBANC)
                        identBanco = "TBanc";

                    //Identificacion de URL de Respuesta de Pago (Termino de Pago)
                    idenUrlRetorno = "$WEBCONTEXT$/faces/ReceptorPago/ReceptorComprobanteBancoBCI";

                    //Identificacion de URL de Respuesta de Pago (Termino de Pago)
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBCI)
                        codigoBancoCGI = "bci";
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOTBANC)
                        codigoBancoCGI = "tbc";

                    String idtrx = Long.toString(idTransaccion);
                    XMLPagoConvenio =
                        "<" + identBanco + ">" + "<IdTrx>" + idtrx + "</IdTrx>" + "<IdCom>" + cod_bicevida.getIDCOM() +
                        "</IdCom>" + "<Productos>";


                    String productosAdd = "";
                    urlConfeccion = "";
                    long monto = 0;
                    for (int j = 0; j < info_comprobantes.size(); j++) {
                        String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                        String monprod = info_prodpagado[1]; //Monto Producto
                        String numprod = info_prodpagado[3]; //Numero >Producto
                        productosAdd =
                            productosAdd + "<Identificador>" + numprod + "</Identificador><Monto>" + monprod +
                            "</Monto><Cantidad>1</Cantidad>";
                        monto = monto + Long.parseLong(monprod);
                    }
                    urlConfeccion = urlConfeccion + "PROD1|" + monto + "|1";

                    XMLPagoConvenio =
                        XMLPagoConvenio + productosAdd + "</Productos>" + "<Total>" +
                        newTotalPorPagar.getEnPesos().longValue() + "</Total>" + "<NumProductos>" +
                        info_comprobantes.size() + "</NumProductos>" + "<UrlRetorno>" + idenUrlRetorno +
                        "</UrlRetorno>" + "<CostoEnvio>0</CostoEnvio>" + "<Banco>" + codigoBancoCGI + "</Banco>" +
                        "<Action>inicio</Action>" + "<Compra>" + urlConfeccion + "</Compra>" + "</" + identBanco + ">";

                    //Adaptaciones de URL para TBANC Y BCI ya que en su metodo de envio utiliza via GET y toda la informacion en la URL
                    String urlBase = newConvenio.getURL();
                    String datosAdic =
                        urlConfeccion + "&cstenv=0&pagret=" + idenUrlRetorno + "&bco=" + codigoBancoCGI + "&trx=" +
                        idtrx + StringUtil.rellenarTextoIzquierda(Integer.toString(info_comprobantes.size()), 2, "0");

                    //RESETEA LA URL EN BASE DE LA INFORMACION DE LA BASE DE DATOS
                    newConvenio.setURL(urlBase + "?" + datosAdic);
                }


                /**
                   * Codigo que genera XML para BANCO BICE
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBICE) {
                    PagoElectronicoBancoBICEVO voBice = new PagoElectronicoBancoBICEVO();
                    voBice.setIdCliente(cod_bicevida.getIDCOM().toString());
                    voBice.setIdTransaccion(Long.toString(idTransaccion));
                    voBice.setMontoTotal(newTotalPorPagar.getEnPesos().doubleValue());

                    //AGREGA DETALLE DE PAGO
                    for (int j = 0; j < info_comprobantes.size(); j++) {
                        String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                        String tipoVia = info_prodpagado[0]; //Tivo Dividendo o Poliza
                        String monprod = info_prodpagado[1]; //Monto Producto
                        String descripprod = info_prodpagado[2]; //Monto Producto
                        String codigotipoProducto = info_prodpagado[3]; //1060, 1061, etc
                        String numContraOperacion = info_prodpagado[5]; // numero contrato operacion

                        //BUSCA EL NOMBRE DE PRODUCTO SIN ACENTOS
                        /*String descripcion = "";
                           String descripcionBD =  DAOFactory.getConsultasDao().getNombreProductoSinAcentos(new Long(codigotipoProducto));
                           descripcion = descripcionBD;
                           if (descripprod.length()> descripcionBD.length()) descripcion+= descripprod.substring(descripcionBD.length());
                           */

                        //AGREGA DETALLE
                        ItemPagoElectronicoBancoBICEVO det = new ItemPagoElectronicoBancoBICEVO();
                        det.setCantidad(new Long(1));

                        if (tipoVia.equals("1"))
                            det.setDescripcion("Aporte Extraordinario Bice Vida " + numContraOperacion); //VIDA
                        if (tipoVia.equals("2"))
                            det.setDescripcion("Aporte Extraordinario Bice Hipotecaria " +
                                               numContraOperacion); //HIPOTECARIA
                        det.setMonto(new Double(monprod));
                        voBice.addItem(det);
                    }

                    XMLPagoConvenio = voBice.toXml();
                }


                /**
                   * Codigo que genera XML para Banco Estado
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOESTADO) {
                    String idtrx = Long.toString(idTransaccion);
                    String producto = "";
                    String rutEmpresa = "";
                    if (detallePoliza != null) {
                        producto = "SEGUROS BICE VIDA";
                        rutEmpresa = "966564105"; //RUT DE VIDA
                    }
                    if (detalleDiv != null) {
                        producto = "BICE HIPOTECARIA";
                        rutEmpresa = "967770604"; //RUT DE HIPOTECARIA
                    }


                    String rut = Integer.toString(newConfirmacion.getRutCliente());
                    XMLPagoConvenio =
                        "<INICIO>" + "<ENCABEZADO>" + " <ID_SESSION>" + idtrx + "</ID_SESSION>" + " <RUT_DV_CON>" +
                        rutEmpresa + "</RUT_DV_CON>" + " <CONV_CON>" + cod_bicevida.getIDCOM() + "</CONV_CON>" +
                        " <SERVICIO>" + producto + "</SERVICIO>" + " <RUT_DV_CLIENTE>" + rut + RutUtil.calculaDv(rut) +
                        "</RUT_DV_CLIENTE>" + " <PAG_RET>" +
                        ResourceBundleUtil.getProperty("cl.bice.vida.bancoestado.contexto.url.pago.forward") +
                        "/jsp/closewindow.jsp</PAG_RET>" + " <TIPO_CONF>nomq</TIPO_CONF>" +
                        " <METODO_REND>POST</METODO_REND>" + " <PAG_REND>" +
                        ResourceBundleUtil.getProperty("cl.bice.vida.bancoestado.contexto.url.pago.forward") +
                        "/faces/ReceptorPago/ReceptorComprobanteBancoEstado</PAG_REND>" + " <BANCO>0012</BANCO>" + " <CANT_MPAGO>" +
                        info_comprobantes.size() + "</CANT_MPAGO>" + " <TOTAL>" +
                        newTotalPorPagar.getEnPesos().longValue() + "</TOTAL>" + "</ENCABEZADO>" + "<MULTIPAGO>" +
                        "  <GLOSA_MPAGO>" + producto + "</GLOSA_MPAGO>" + "  <ID_MPAGO>" + idtrx + "</ID_MPAGO>" +
                        "  <PAGO>" + "    <RUT_DV_EMP>" + rutEmpresa + "</RUT_DV_EMP>" + "    <NUM_CONV>" +
                        cod_bicevida.getIDCOM() + "</NUM_CONV>" + "    <FEC_TRX>" +
                        FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()), "yyyyMMdd") +
                        "</FEC_TRX>" + "    <HOR_TRX>" +
                        FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()), "HHmmss") +
                        "</HOR_TRX>" + "    <FEC_VENC>" +
                        FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()), "yyyyMMdd") +
                        "</FEC_VENC>" + "    <GLOSA>" + producto + "</GLOSA>" + "    <COD_PAGO/>" + "    <MONTO>" +
                        newTotalPorPagar.getEnPesos().longValue() + "</MONTO>" + "  </PAGO>" + "</MULTIPAGO>" +
                        "</INICIO>";
                }


                /**
                   * Codigo que genera XML para Servipag
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_SERVIPAG ||
                    newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_SERVIPAG2) {

                    /**
                       * Este indicar genera que el resultado de la operacion a enviar
                       * a servipag sea un XML con un solo detalle por el total del monto
                       * a enviar de pendiendo de su valor
                       *  en variable todoenuno.
                       *  1) valor true = Uno solo detalle del pago
                       *  2) valor false = Detalles separados por cada producto a pagar
                       */
                    boolean todoenuno = true;

                    /**
                       * Codigo para generar documento XML de servipag
                       */
                    logger.info("generaXmlConfirmacion() - Genero xml Servipag");
                    String codcanalpago = "000";
                    logger.info("generaXmlConfirmacion() - Instancia ServipagDocumentPec");
                    Servipag docServipag = new Servipag();
                    logger.info("generaXmlConfirmacion() - Agrega nuevo documento addNewServipag()");
                    //Servipag servipag = docServipag.addNewServipag();

                    //Genera ID
                    String idtrx = Long.toString(idTransaccion);

                    if (cod_empresa == 1) {
                        //codcanalpago = "023";
                        codcanalpago = ResourceBundleUtil.getProperty("servipag.bicevida.codcanalpago");
                        /**
                    * Caso Bicehipotecaria canal de pago 24 para servipag
                    */
                    } else {
                        //codcanalpago = "024";
                        codcanalpago = ResourceBundleUtil.getProperty("servipag.hipotecaria.codcanalpago");
                    }


                    // EncryptSeguridadBiceUtil.crearClaves();

                    

                    //Seteao Datos de Header
                    logger.info("generaXmlConfirmacion() - Seteao Datos de Header: HeaderDocumentPec.Header header = servipag.addNewHeader() ");
                    Header header = new Header();
                    
                    //header.setFirmaEPS(" ");
                    header.setCodigoCanalPago(codcanalpago);
                    header.setIdTxPago(idtrx);
                    header.setFechaPago(FechaUtil.getFechaFormateoCustom(fechapago, "yyyyMMdd"));
                    header.setMontoTotalDeuda(newTotalPorPagar.getEnPesos().longValue());

                    /*CARGAMOS NUEVOS PARAMETROS SERVIPAG*/
                    cargaNuevosParametros(header, newConfirmacion);


                    docServipag.setHeader(header);
                    String rut = Integer.toString(newConfirmacion.getRutCliente());
                    while (rut.length() < 8)
                        rut = "0" + rut;

                    //Seteo boletas a pagar
                    int comprobantes = 0;
                    long montototal = 0;
                    for (int j = 0; j < info_comprobantes.size(); j++) {
                        String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                        //Seteo Data
                        if (todoenuno == false) {
                            Documentos documentopago = new Documentos();
                            documentopago.setIdSubTx((new Long(comprobantes + 1)).toString());
                            //documentopago.setIdentificador(rut); //Rut
                            documentopago.setIdentificador(info_prodpagado[3]); //Numero Producto
                            documentopago.setBoleta(info_prodpagado[5]); //codigo_boleta = "" + prod.getNumProducto() + comprobante.getCuota()+ idTransaccion;
                            documentopago.setMonto((new Long(info_prodpagado[1])).longValue());
                            documentopago.setFechaVencimiento(info_prodpagado[4]);
                            docServipag.getDocumentos().add(documentopago);
                        }
                        montototal = montototal + (new Long(info_prodpagado[1])).longValue();
                        comprobantes++;

                    }
                    header.setNumeroBoletas(comprobantes);
                        
                    /**
                       * Determina si todo sale en un solo detalle
                       */
                    String boleta = "";
                    String fechaVencimientoPago = "";
                    
                    if (todoenuno == true) {
                        String pagos;
                        if ((comprobantes + 1) >= 10) {
                            pagos = "" + (comprobantes);
                        } else {
                            pagos = "0" + (comprobantes);
                        }

                        String[] info_prodpagado = (String[]) info_comprobantes.get(0);
                        Documentos documentopago = new Documentos();
                        documentopago.setIdSubTx((new Long(1)).toString());
                        documentopago.setIdentificador(rut); //Rut
                        //documentopago.setIdentificador(info_prodpagado[3]+pagos); //Numero Producto + nro pagos
                        //documentopago.setBoleta("1234"); //Numero Poliza
                        boleta = info_prodpagado[5] + pagos;
                        documentopago.setBoleta(boleta); //codigo_boleta + pagos = "" + prod.getNumProducto() + comprobante.getCuota()+ idTransaccion + pagos;
                        documentopago.setMonto(montototal);
                        //documentopago.setFechaVencimiento(datetool.formatFecha("yyyyMMdd", fechapago));
                        fechaVencimientoPago = info_prodpagado[4];
                        documentopago.setFechaVencimiento(fechaVencimientoPago);
                        docServipag.getDocumentos().add(documentopago);
                        header.setNumeroBoletas(1);
                    }
                    
                    
                    
                    // encriptacion de los datos
                    String datafirmar =
                        codcanalpago + FechaUtil.getFechaFormateoCustom(fechapago, "yyyyMMdd") +
                        newTotalPorPagar.getEnPesos().toString();

                    datafirmar += "1" +idtrx + boleta + montototal + fechaVencimientoPago;
                    logger.info("generaXmlConfirmacion() - Dato a firmar \"IdTxPagoFECHAPAGOMONTOTOTALDEUDA\": " +
                                 datafirmar);

                    logger.info("generaXmlConfirmacion() - Inicializa algoritmo de encriptacion");
                    Signature sig = Signature.getInstance("MD5WithRSA");
                    logger.info("generaXmlConfirmacion() - Construye claves a partir de base 64");
                    PublicKey keyPubl =
                        EncryptSeguridadBiceUtil.getClavePublicaByBase64(ResourceBundleUtil.getProperty("servipag.signature.clave.publica"));
                    PrivateKey keyPriv =
                        EncryptSeguridadBiceUtil.getClavePrivadaByBase64(ResourceBundleUtil.getProperty("servipag.signature.clave.privada"));

                    String firmado64 = EncryptSeguridadBiceUtil.firmarDataBase64(sig, keyPriv, datafirmar.getBytes());
                    logger.info("generaXmlConfirmacion() - Dato a firmado en base 64: " + firmado64);

                    logger.info("generaXmlConfirmacion() - Finaliza algoritmo de encriptacion");

                    //Comprobaci�n de de los datos encriptados en foema correcta1
                    logger.info("generaXmlConfirmacion() - Inicializa Proceso de comprobaci�n de encripataci�n");
                    byte[] firmadecode = EncryptSeguridadBiceUtil.base64decode(firmado64);
                    logger.info("generaXmlConfirmacion() - Se decodifica la firma en base 64");
                    boolean resultado =
                        EncryptSeguridadBiceUtil.verisingDataSignature(sig, keyPubl, datafirmar.getBytes(),
                                                                       firmadecode);
                    if (resultado) {
                        logger.info("generaXmlConfirmacion() - se codific� y encript� correctamente, resultado: " +
                                     resultado);
                    } else {
                        logger.error("generaXmlConfirmacion() - se codific� y encript� incorrectamente, resultado: " +
                                     resultado);
                    }
                    
                    
                    header.setFirmaEPS(firmado64);
                    

                    docServipag.setHeader(header);
                    //XMLPagoConvenio = docServipag.xmlText();
                    XMLPagoConvenio = JAXBUtil.convertObjectTOXmlString(docServipag);
                }


                /**
                  * AQUI SETEAR PARAMETRO XML AL CONVENIO
                  */
                logger.info("XML BANCO ===>" + XMLPagoConvenio);
                System.out.println("XML BANCO ===>" + XMLPagoConvenio);
                newConvenio.setXML(XMLPagoConvenio);

                break;
            }
        }
        logger.info("generaXmlConfirmacion() - termino");
        return conf;
    }


    //============= P R I M E R A   P R I M A  ==================

    /**
     * Confecciona el resumen y pago de confirmacion
     * @param xmlResumenPagos
     * @param req
     * @return
     */
    public String generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima(String xmlResumenPagos, ResumenRequest req,
                                                                             int idCanal) {
        String resp = null;
        logger.info("generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima() - inicio");
        ResumenConHeaderDocument res = null;
        try {
            res = ResumenConHeaderDocument.Factory.parse(xmlResumenPagos);
            validateXML(res);
            generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima(res, req);
            validateXML(res);
            DetalleNavegacion detallenavegacion =
                DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                               res.getResumenConHeader().getInfoNavegacion().getEntrada().intValue(),
                                                                               2,
                                                                               res.getResumenConHeader().getTotalPagar().intValue(),
                                                                               res.toString(), idCanal);
            resp = res.toString();
            /**
             * TODO: Revisar cuando no graba ya que el dato
             * puede que ya exista
             * if(detallenavegacion==null) {
                resp ="NOK|No se ha podido almacenar en la base de datos xml seleccionado";
            } else {
                resp = res.toString();
            }*/
        } catch (Exception e) {
            resp = "NOK|Error al generar xml de resumen con los productos seleccionados :" + e.getMessage();
            logger.error("generaXmlResumenPagosConProductosSeleccionados() - Excepcion :" + e.getMessage(), e);
        }
        logger.info("generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima() - termino");
        return resp;

    }

    /**
     * Genera xml de confirmacion
     * @param res
     * @param req
     */
    public void generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima(ResumenConHeaderDocument res,
                                                                           ResumenRequest req) {
        logger.info("generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima() - inicio");
        logger.info("generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima() - cambio la entrada en infonavegacion");
        res.getResumenConHeader().getInfoNavegacion().setEntrada(res.getResumenConHeader().getInfoNavegacion().getEntrada().add(new BigInteger("1")));
        int contador = 0;
        boolean habilito = false;
        for (int i = 0; i < res.getResumenConHeader().getProductosPorPagar().getEntradaArray().length; i++) {
            boolean flagprimerproducto = true; //Falg que indica que es el promer producto diferente
            ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada entrada =
                res.getResumenConHeader().getProductosPorPagar().getEntradaArray(i);

            if (req.getProductosSeleccionados() != null && contador < req.getProductosSeleccionados().length &&
                entrada.getContador().intValue() == req.getProductosSeleccionado(contador)) {
                entrada.setSeleccionado(true);
                entrada.setDisabled(false);
                habilito = true;

                int idCuota = entrada.getContador().intValue(); //ID CUOTA
                int pago = 0;

                for (int prodSel = 0; prodSel < req.getProductosSeleccionados().length; prodSel++) {
                    if (req.getProductosSeleccionados()[prodSel] == idCuota) {
                        pago = req.getTotales()[prodSel];
                        break;
                    }
                }

                entrada.getCuota().getCuotaFija().setEnPesos(new BigInteger("" + pago));
                entrada.getCuota().setPago(new BigInteger(Integer.toString(pago)));
                contador++;
            }

            //SETEA LA ACTUALIZACION DEL DATO
            res.getResumenConHeader().getProductosPorPagar().setEntradaArray(i, entrada);
            entrada = null;
        }

        res.getResumenConHeader().getProductosPorPagar().setTotalPorPagar(new BigInteger(Integer.toString(req.getTotal_pagar())));
        res.getResumenConHeader().getProductosPorPagar().setTotalDeuda(new BigInteger(Integer.toString(req.getTotal_pagar())));

        logger.info("generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima() - Limpio los bancos--------");
        /**
          * Limpiar todos y setear 1 solo convenio seleccionado
          */
        int largo = res.getResumenConHeader().getConvenios().getConvenioArray().length;
        for (int banquitos = 0; banquitos < largo; banquitos++) {
            res.getResumenConHeader().getConvenios().getConvenioArray(banquitos).setSeleccionado(false);
            if (res.getResumenConHeader().getConvenios().getConvenioArray(banquitos).getCodigo().intValue() ==
                req.getBanco()) {
                res.getResumenConHeader().getConvenios().getConvenioArray(banquitos).setSeleccionado(true);
            }
        }

        res.getResumenConHeader().setTotalPagar(new BigInteger(Integer.toString(req.getTotal_pagar())));
        logger.info("generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima() - termino");

    }


    /**
     * Crea la confirmacion con los productos
     * ya listos y seleccionados.
     * @param xmlResumenSeleccionado
     * @return
     */
    public String crearConfirmacionConXmlSeleccionadoPrimeraPrima(String origenTransaccion,
                                                                  String xmlResumenSeleccionado, int idCanal, String email) {
        logger.info("crearConfirmacionConXmlSeleccionado() - inicio");
        this.email = email;
        detallesByEmp = null;
        comprobantes = null;

        ConfirmacionDocument confirmacion;
        String code = "NOK|Error: Inesperado";
        try {
            ResumenConHeaderDocument res = ResumenConHeaderDocument.Factory.parse(xmlResumenSeleccionado);
            validateXML(res);
            logger.info("crearConfirmacionConXmlSeleccionadoPrimeraPrima() - busco el medio de pago seleccionado");
            int medio = 0;
            for (int i = 0; i < res.getResumenConHeader().getConvenios().getConvenioArray().length; i++) {
                if (res.getResumenConHeader().getConvenios().getConvenioArray(i).getSeleccionado()) {
                    medio = res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue();
                }
            }
            logger.info("crearConfirmacionConXmlSeleccionadoPrimeraPrima() - creo la transaccion");
            Transacciones transaccion;
            if (res.getResumenConHeader().getIdTransaccion() == null) {
                //CONSULTO LA NAVEGACION PARA SABER A QUE EMPRESA LE PERTENECENE LA CONFIRMACION
                String empresa = "BV";
                Navegacion navDto =
                    DAOFactory.getPersistenciaGeneralDao().findNavegacionById(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue());
                if (navDto != null) {
                    if (navDto.getEmpresa() == 1)
                        empresa = "BV";
                    if (navDto.getEmpresa() == 2)
                        empresa = "BH";
                }
                transaccion =
                    DAOFactory.getPersistenciaGeneralDao().newTransaccion(origenTransaccion, empresa, medio,
                                                                          res.getResumenConHeader().getTurno().getTime(),
                                                                          res.getResumenConHeader().getTotalPagar().intValue(),
                                                                          res.getResumenConHeader().getFechaConsulta().getTime(),
                                                                          res.getResumenConHeader().getRutCliente(),
                                                                          res.getResumenConHeader().getNombreCliente());

            } else {
                transaccion =
                    DAOFactory.getPersistenciaGeneralDao().updateTransaccion(res.getResumenConHeader().getIdTransaccion().intValue(),
                                                                             medio,
                                                                             res.getResumenConHeader().getTotalPagar().intValue(),
                                                                             res.getResumenConHeader().getTurno().getTime());
            }
            logger.info("crearConfirmacionConXmlSeleccionado() - actualiza la informacion de la transaccion en la tabla de navegacion");
            DAOFactory.getPersistenciaGeneralDao().updateTransaccionInNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                                 transaccion.getIdTransaccion(),
                                                                                 transaccion.getMontoTotal(), medio);

            logger.info("crearConfirmacionConXmlSeleccionado() - creo el xml de confirmacion y lo almaceno en la base de datos");
            confirmacion = generaXmlConfirmacionPrimeraPrima(res, transaccion.getIdTransaccion());
            validateXML(confirmacion);

            DAOFactory.getPersistenciaGeneralDao().createDetalleNavegacion(confirmacion.getConfirmacion().getInfoNavegacion().getIDNavegacion().longValue(),
                                                                           confirmacion.getConfirmacion().getInfoNavegacion().getEntrada().intValue(),
                                                                           3,
                                                                           confirmacion.getConfirmacion().getProductosPorPagar().getTotalPorPagar().getEnPesos().intValue(),
                                                                           confirmacion.toString(), idCanal);
            code = confirmacion.toString();

            logger.info("crearConfirmacionConXmlSeleccionado() - lleno el detalle de la transaccion");
            //TODO: Revisar SQL
            DAOFactory.getPersistenciaGeneralDao().updateDetalleTransaccion(transaccion, detallesByEmp, comprobantes);
        } catch (Exception e) {
            code = "NOK|Error: " + e.getMessage();
            logger.error("crearConfirmacionConXmlSeleccionado() - NOK|Error: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            logger.info("crearConfirmacionConXmlSeleccionado() - termino");
            return code;
        }

    }


    /**
     * Generacion del XML de confirmacion de pago
     * @param res
     * @param idTransaccion
     * @return
     */
    private ConfirmacionDocument generaXmlConfirmacionPrimeraPrima(ResumenConHeaderDocument res,
                                                                   long idTransaccion) throws Exception {
        String XMLPagoConvenio = "";
        logger.info("generaXmlConfirmacionPrimeraPrima() - inicio");
        logger.info("generaXmlConfirmacionPrimeraPrima() - validaciones de negocio");
        logger.info("generaXmlConfirmacionPrimeraPrima() - ejemplos de obtencion de campos");
        ConfirmacionDocument conf = ConfirmacionDocument.Factory.newInstance();
        ConfirmacionDocument.Confirmacion newConfirmacion = conf.addNewConfirmacion();

        logger.info("generaXmlConfirmacionPrimeraPrima() - Copia IdentificacionMensaje  ");
        ConfirmacionDocument.Confirmacion.IdentificacionMensaje idm;
        idm = newConfirmacion.addNewIdentificacionMensaje();
        idm.setCodigo("CONFIR");
        idm.setVersion("1.0");
        idm.setDe("PAGWEB");
        idm.setFechaCreacion(java.util.Calendar.getInstance());
        idm.setAccion("CONFIRMAR");

        logger.info("generaXmlConfirmacionPrimeraPrima() - Informacion de la navegacion");
        ConfirmacionDocument.Confirmacion.InfoNavegacion in = newConfirmacion.addNewInfoNavegacion();
        in.setIDNavegacion(res.getResumenConHeader().getInfoNavegacion().getIDNavegacion());
        in.setEntrada(res.getResumenConHeader().getInfoNavegacion().getEntrada().add(new BigInteger("1")));

        newConfirmacion.setIdTransaccion(new BigInteger(Long.toString(idTransaccion)));
        newConfirmacion.setRutCliente(res.getResumenConHeader().getRutCliente());
        newConfirmacion.setNombreCliente(res.getResumenConHeader().getNombreCliente());
        newConfirmacion.setFechaConsulta(res.getResumenConHeader().getFechaConsulta());
        newConfirmacion.setFechaUF(res.getResumenConHeader().getFechaUF());
        newConfirmacion.setValorUF(res.getResumenConHeader().getValorUF());

        logger.info("generaXmlConfirmacionPrimeraPrima() - Copia ProductosPorPagar");
        ConfirmacionDocument.Confirmacion.ProductosPorPagar productos = newConfirmacion.addNewProductosPorPagar();
        noNamespace.ResumenConHeaderDocument.ResumenConHeader.ProductosPorPagar.Entrada[] listEntradas =
            res.getResumenConHeader().getProductosPorPagar().getEntradaArray();
        int contador = 1;

        DetalleTransaccionByEmp detallePoliza = new DetalleTransaccionByEmp();
        detallePoliza.setCodEmpresa(1);
        detallePoliza.setFechahora(res.getResumenConHeader().getFechaConsulta().getTime());
        detallePoliza.setIdTransaccion(idTransaccion);
        detallePoliza.setMontoTotal(0);

        DetalleTransaccionByEmp detalleDiv = new DetalleTransaccionByEmp();
        detalleDiv.setCodEmpresa(2);
        detalleDiv.setFechahora(res.getResumenConHeader().getFechaConsulta().getTime());
        detalleDiv.setIdTransaccion(idTransaccion);
        detalleDiv.setMontoTotal(0);

        comprobantes = new ArrayList();
        List info_comprobantes = new ArrayList();
        int cod_empresa = 0;

        for (int i = 0; i < listEntradas.length; i++) {
            if (listEntradas[i].getSeleccionado()) {
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = productos.addNewEntrada();
                entrada.setContador(new BigInteger(Integer.toString(contador)));
                contador++;


                logger.info("generaXmlConfirmacionPrimeraPrima() - datos del producto");
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Producto prod = entrada.addNewProducto();
                prod.setCodigoProducto(listEntradas[i].getProducto().getCodigoProducto());
                prod.setDescripcionProducto(listEntradas[i].getProducto().getDescripcionProducto());
                prod.setNumProducto(listEntradas[i].getProducto().getNumProducto());
                prod.setCodEmpresa(listEntradas[i].getProducto().getCodEmpresa());

                entrada.setInfoCajas(listEntradas[i].getInfoCajas());


                Comprobantes comprobante = new Comprobantes();
                comprobante.setCodProducto(prod.getCodigoProducto().intValue());
                comprobante.setIdTransaccion(idTransaccion);
                comprobante.setNumProducto(prod.getNumProducto().longValue());

                if (entrada.getInfoCajas().getCreditos() != null)
                    comprobante.setCuota(entrada.getInfoCajas().getCreditos().getNumDividendo().intValue());
                else {
                    comprobante.setCuota(entrada.getInfoCajas().getPolizas().getFolio().intValue());
                }

                logger.info("generaXmlConfirmacionPrimeraPrima() - Informacion sobre la cuota");
                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Cuota newCuota = entrada.addNewCuota();
                newCuota.setFechaVencimiento(listEntradas[i].getCuota().getFechaVencimiento());

                ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Cuota.Monto newMonto =
                    newCuota.addNewMonto();
                int tipoCuota = listEntradas[i].getProducto().getTipoCuota();

                newMonto.setEnPesos(listEntradas[i].getCuota().getPago());
                comprobante.setMontoBase(newMonto.getEnPesos().intValue());
                comprobante.setMontoExcedente(0);
                comprobante.setMontoTotal(newMonto.getEnPesos().intValue());

                if (prod.getCodEmpresa() == 1) {
                    detallePoliza.setMontoTotal(detallePoliza.getMontoTotal() + newMonto.getEnPesos().intValue());
                } else {
                    detalleDiv.setMontoTotal(detalleDiv.getMontoTotal() + newMonto.getEnPesos().intValue());
                }

                logger.info("generaXmlConfirmacionPrimeraPrima() - seteando el valor en uf y el detalle");
                noNamespace.DetalleCargos newDetalle = newMonto.addNewDetalle();

                //SOLO PROCESA CUOTA FIJA PARA APT
                newMonto.setEnUF(listEntradas[i].getCuota().getCuotaFija().getEnUF());
                logger.info("generaXmlConfirmacionPrimeraPrima() - Copiar Detalle");
                newDetalle = listEntradas[i].getCuota().getCuotaFija().getDetalle();
                newMonto.setDetalle(newDetalle);

                java.util.Date fechavenci = new java.util.Date(newCuota.getFechaVencimiento().getTimeInMillis());
                String fechavencistr = FechaUtil.getFechaFormateoCustom(fechavenci, "yyyyMMdd");

                String codigo_boleta;
                codigo_boleta = "" + prod.getNumProducto() + comprobante.getCuota() + idTransaccion;

                String[] info_prodpagado = new String[6];
                info_prodpagado[0] = Integer.toString(prod.getCodEmpresa());
                info_prodpagado[1] = comprobante.getMontoTotal().toString();
                info_prodpagado[2] = prod.getDescripcionProducto();
                //-------------datos adicionales-----------
                info_prodpagado[3] = prod.getCodigoProducto().toString();
                info_prodpagado[4] = fechavencistr;
                info_prodpagado[5] = codigo_boleta;
                cod_empresa = prod.getCodEmpresa();


                info_comprobantes.add(info_prodpagado);
                comprobantes.add(comprobante);
            }
        }
        contador--;
        logger.info("generaXmlConfirmacionPrimeraPrima() - decremento contador ya que partimos con 1");

        ConfirmacionDocument.Confirmacion.ProductosPorPagar.TotalPorPagar newTotalPorPagar =
            productos.addNewTotalPorPagar();
        newTotalPorPagar.setEnPesos(res.getResumenConHeader().getTotalPagar());

        String total =
            NumeroUtil.redondear(res.getResumenConHeader().getTotalPagar().intValue() /
                                 res.getResumenConHeader().getValorUF().doubleValue(), 4);
        newTotalPorPagar.setEnUF(new BigDecimal(total));


        ConfirmacionDocument.Confirmacion.Convenio newConvenio = conf.getConfirmacion().addNewConvenio();
        for (int i = 0; i < res.getResumenConHeader().getConvenios().getConvenioArray().length; i++) {
            if (res.getResumenConHeader().getConvenios().getConvenioArray(i).getSeleccionado()) {
                newConvenio.setCodigo(res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo());
                newConvenio.setDescripcion(res.getResumenConHeader().getConvenios().getConvenioArray(i).getDescripcion());

                logger.info("generaXmlConfirmacionPrimeraPrima() - seteo detalle de transacciones por empresa");
                detallesByEmp = new ArrayList();
                if (detalleDiv.getMontoTotal() == 0) {
                    detalleDiv = null;
                } else {
                    detalleDiv.setCodMedio(res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                    detallesByEmp.add(detalleDiv);
                }
                if (detallePoliza.getMontoTotal() == 0) {
                    detallePoliza = null;
                } else {
                    detallePoliza.setCodMedio(res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                    detallesByEmp.add(detallePoliza);
                }

                /**
                   * Este codigo determina cual es el medio de pago
                   * que el cliente escogio para cancelar y se adjunta
                   * la informacion de redireccionamiento web para lanzar
                   * la pantalla de pago al cliente.
                   */
                CodEmpByMedio codpolizas = null;
                CodEmpByMedio coddividendos = null;
                CodEmpByMedio cod_bicevida = null;
                if (detallePoliza != null) {
                    cod_bicevida =
                        codpolizas =
                        DAOFactory.getPersistenciaGeneralDao().findCodByEmpInMedio(1,
                                                                                   res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                }
                if (detalleDiv != null) {
                    cod_bicevida =
                        coddividendos =
                        DAOFactory.getPersistenciaGeneralDao().findCodByEmpInMedio(2,
                                                                                   res.getResumenConHeader().getConvenios().getConvenioArray(i).getCodigo().intValue());
                }

                newConvenio.setURL(cod_bicevida.getUrl());


                /**
                   * Generaci�n de XML a ser enviados por metodo Post
                   */
                java.util.Date fechapago = new java.util.Date(System.currentTimeMillis());


                /**
                   * Codigo que genera XML para Banco Chile y Santander Santiago
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOCHILE ||
                    newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_SANTANDER) {
                    MPINIDocument docMpini = MPINIDocument.Factory.newInstance();
                    MPINIDocument.MPINI mpini = docMpini.addNewMPINI();
                    mpini.setIDCOM(cod_bicevida.getIDCOM());
                    String idtrx = Long.toString(idTransaccion);
                    while (idtrx.length() < 6) {
                        idtrx = "0" + idtrx;
                    }
                    idtrx = cod_bicevida.getIDCOM().toString() + idtrx.substring(idtrx.length() - 6);
                    mpini.setIDTRX(idtrx);
                    mpini.setTOTAL(newTotalPorPagar.getEnPesos().longValue());
                    mpini.setNROPAGOS(contador);

                    for (int j = 0; j < info_comprobantes.size(); j++) {
                        String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                        DETALLEDocument.DETALLE det = mpini.addNewDETALLE();
                        if (info_prodpagado[0].compareTo("1") == 0)
                            det.setSRVREC(codpolizas.getSRVREC().intValue());
                        else
                            det.setSRVREC(coddividendos.getSRVREC().intValue());
                        det.setMONTO(info_prodpagado[1]);
                        String glosa = info_prodpagado[2];
                        logger.info("generaXmlConfirmacion() - glosa MPINI: " + glosa);
                        glosa = glosa.replaceAll("�", "a");
                        glosa = glosa.replaceAll("�", "e");
                        glosa = glosa.replaceAll("�", "i");
                        glosa = glosa.replaceAll("�", "o");
                        glosa = glosa.replaceAll("�", "u");
                        logger.info("generaXmlConfirmacion() - glosa MPINI truncada: " + glosa);
                        if (glosa.length() > 30)
                            glosa = glosa.substring(0, 29);
                        det.setGLOSA(glosa);
                        det.setCANTIDAD("0001");
                        det.setPRECIO(info_prodpagado[1]);
                        String datos_adic = idtrx;
                        String rut = Integer.toString(newConfirmacion.getRutCliente());
                        while (rut.length() < 8) {
                            rut = "0" + rut;
                        }
                        datos_adic += rut;
                        if (info_prodpagado[0].compareTo("1") == 0)
                            datos_adic += codpolizas.getIDCOM();
                        else
                            datos_adic += coddividendos.getIDCOM();
                        det.setDATOADIC(datos_adic);
                    }
                    XMLPagoConvenio = docMpini.xmlText();

                    if (XMLPagoConvenio.indexOf("<SRVREC>5218</SRVREC>") > 0) {
                        XMLPagoConvenio =
                            StringUtil.replaceString(XMLPagoConvenio, "<SRVREC>5218</SRVREC>",
                                                     "<SRVREC>005218</SRVREC>");
                    } else {
                        XMLPagoConvenio =
                            StringUtil.replaceString(XMLPagoConvenio, "<SRVREC>3028</SRVREC>",
                                                     "<SRVREC>003028</SRVREC>");
                    }

                    //TODO: INSERTAR EN TABLA DE RELACION DE LLAVES IDCOMERCIO+ID6DIGITOS CON IDTRANSACCION REAL
                    java.util.Date fecha = new java.util.Date();
                    DAOFactory.getPersistenciaGeneralDao().insertarHomologacionPKConvenio(newConvenio.getCodigo().longValue(),
                                                                                          idTransaccion,
                                                                                          mpini.getIDTRX(), fecha);
                    
                    
                    if(newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOCHILE) {
                        //generamos nueva logica de envio Banco Chile.
                        if(cod_bicevida.getCodEmpresa() == 2) {//Bice Hipotecaria
                            XMLPagoConvenio = generaNuevoXMLBChile(XMLPagoConvenio
                                                                   , ResourceBundleUtil.getProperty("bcochile.hipotecaria.firma.url")
                                                                   , ResourceBundleUtil.getProperty("bice.mediopago.cgi.bice.bancochile.hipotecaria.path.key"));
                        } else {//BIcevida
                            XMLPagoConvenio = generaNuevoXMLBChile(XMLPagoConvenio
                                                                   , ResourceBundleUtil.getProperty("bcochile.vida.firma.url")
                                                                   , ResourceBundleUtil.getProperty("bice.mediopago.cgi.bice.bancochile.vida.path.key"));
                        }
                        
                    }

                }

                /**
                   * Codigo que genera XML para WebPay
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_WEBPAY) {
                    String idtrx = Long.toString(idTransaccion);
                    while (idtrx.length() < 6) {
                        idtrx = "0" + idtrx;
                    }
                    Long totalpagoCargo = newTotalPorPagar.getEnPesos().longValue();
                    XMLPagoConvenio =
                        "<WebPay>" + "<IdTrx>" + idtrx + "</IdTrx>" + "<IdCom>" + cod_bicevida.getIDCOM() + "</IdCom>" +
                        "<Monto>" + totalpagoCargo.longValue() + "</Monto>" + "<NumProductos>" +
                        info_comprobantes.size() + "</NumProductos>" + "</WebPay>";
                }

                /**
                   * Codigo que genera XML para WebPay
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_WEBPAY_CONVENIO) {
                    String idtrx = Long.toString(idTransaccion);
                    while (idtrx.length() < 6) {
                        idtrx = "0" + idtrx;
                    }
                    Long totalpagoCargo = newTotalPorPagar.getEnPesos().longValue();
                    XMLPagoConvenio =
                        "<WebPay>" + "<IdTrx>" + idtrx + "</IdTrx>" + "<IdCom>" + cod_bicevida.getIDCOM() + "</IdCom>" +
                        "<Monto>" + totalpagoCargo.longValue() + "</Monto>" + "<NumProductos>" +
                        info_comprobantes.size() + "</NumProductos>" + "</WebPay>";
                }


                /**
                    * Codigo que genera XML para banco BCI y TBANC
                    */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBCI ||
                    newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOTBANC) {
                    String identBanco = "";
                    String idenUrlRetorno = "";
                    String codigoBancoCGI = "";
                    String urlConfeccion = "";

                    //Identificacion del TAG
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBCI)
                        identBanco = "BCI";
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOTBANC)
                        identBanco = "TBanc";

                    //Identificacion de URL de Respuesta de Pago (Termino de Pago)
                    idenUrlRetorno = "$WEBCONTEXT$/faces/ReceptorPago/ReceptorComprobanteBancoBCI";

                    //Identificacion de URL de Respuesta de Pago (Termino de Pago)
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBCI)
                        codigoBancoCGI = "bci";
                    if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOTBANC)
                        codigoBancoCGI = "tbc";

                    String idtrx = Long.toString(idTransaccion);
                    XMLPagoConvenio =
                        "<" + identBanco + ">" + "<IdTrx>" + idtrx + "</IdTrx>" + "<IdCom>" + cod_bicevida.getIDCOM() +
                        "</IdCom>" + "<Productos>";


                    String productosAdd = "";
                    urlConfeccion = "";
                    long monto = 0;
                    for (int j = 0; j < info_comprobantes.size(); j++) {
                        String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                        String monprod = info_prodpagado[1]; //Monto Producto
                        String numprod = info_prodpagado[3]; //Numero >Producto
                        productosAdd =
                            productosAdd + "<Identificador>" + numprod + "</Identificador><Monto>" + monprod +
                            "</Monto><Cantidad>1</Cantidad>";
                        monto = monto + Long.parseLong(monprod);
                    }
                    urlConfeccion = urlConfeccion + "PROD1|" + monto + "|1";

                    XMLPagoConvenio =
                        XMLPagoConvenio + productosAdd + "</Productos>" + "<Total>" +
                        newTotalPorPagar.getEnPesos().longValue() + "</Total>" + "<NumProductos>" +
                        info_comprobantes.size() + "</NumProductos>" + "<UrlRetorno>" + idenUrlRetorno +
                        "</UrlRetorno>" + "<CostoEnvio>0</CostoEnvio>" + "<Banco>" + codigoBancoCGI + "</Banco>" +
                        "<Action>inicio</Action>" + "<Compra>" + urlConfeccion + "</Compra>" + "</" + identBanco + ">";

                    //Adaptaciones de URL para TBANC Y BCI ya que en su metodo de envio utiliza via GET y toda la informacion en la URL
                    String urlBase = newConvenio.getURL();
                    String datosAdic =
                        urlConfeccion + "&cstenv=0&pagret=" + idenUrlRetorno + "&bco=" + codigoBancoCGI + "&trx=" +
                        idtrx + StringUtil.rellenarTextoIzquierda(Integer.toString(info_comprobantes.size()), 2, "0");

                    //RESETEA LA URL EN BASE DE LA INFORMACION DE LA BASE DE DATOS
                    newConvenio.setURL(urlBase + "?" + datosAdic);
                }


                /**
                   * Codigo que genera XML para BANCO BICE
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOBICE) {
                    PagoElectronicoBancoBICEVO voBice = new PagoElectronicoBancoBICEVO();
                    voBice.setIdCliente(cod_bicevida.getIDCOM().toString());
                    voBice.setIdTransaccion(Long.toString(idTransaccion));
                    voBice.setMontoTotal(newTotalPorPagar.getEnPesos().doubleValue());

                    //AGREGA DETALLE DE PAGO
                    for (int j = 0; j < info_comprobantes.size(); j++) {
                        String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                        String tipoVia = info_prodpagado[0]; //Tivo Dividendo o Poliza
                        String monprod = info_prodpagado[1]; //Monto Producto
                        String descripprod = info_prodpagado[2]; //Monto Producto
                        String codigotipoProducto = info_prodpagado[3]; //1060, 1061, etc
                        String numContraOperacion = info_prodpagado[5]; // numero contrato operacion

                        //BUSCA EL NOMBRE DE PRODUCTO SIN ACENTOS
                        /*String descripcion = "";
                           String descripcionBD =  DAOFactory.getConsultasDao().getNombreProductoSinAcentos(new Long(codigotipoProducto));
                           descripcion = descripcionBD;
                           if (descripprod.length()> descripcionBD.length()) descripcion+= descripprod.substring(descripcionBD.length());
                           */

                        //AGREGA DETALLE
                        ItemPagoElectronicoBancoBICEVO det = new ItemPagoElectronicoBancoBICEVO();
                        det.setCantidad(new Long(1));

                        if (tipoVia.equals("1"))
                            det.setDescripcion("SVI Primera Prima Bice Vida " + numContraOperacion); //VIDA
                        if (tipoVia.equals("2"))
                            det.setDescripcion("SVI Primera Prima Bice Hipotecaria " +
                                               numContraOperacion); //HIPOTECARIA
                        det.setMonto(new Double(monprod));
                        voBice.addItem(det);
                    }

                    XMLPagoConvenio = voBice.toXml();
                }


                /**
                   * Codigo que genera XML para Banco Estado
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_BANCOESTADO) {
                    String idtrx = Long.toString(idTransaccion);
                    String producto = "";
                    String rutEmpresa = "";
                    if (detallePoliza != null) {
                        producto = "SEGUROS BICE VIDA";
                        rutEmpresa = "966564105"; //RUT DE VIDA
                    }
                    if (detalleDiv != null) {
                        producto = "BICE HIPOTECARIA";
                        rutEmpresa = "967770604"; //RUT DE HIPOTECARIA
                    }


                    String rut = Integer.toString(newConfirmacion.getRutCliente());
                    XMLPagoConvenio =
                        "<INICIO>" + "<ENCABEZADO>" + " <ID_SESSION>" + idtrx + "</ID_SESSION>" + " <RUT_DV_CON>" +
                        rutEmpresa + "</RUT_DV_CON>" + " <CONV_CON>" + cod_bicevida.getIDCOM() + "</CONV_CON>" +
                        " <SERVICIO>" + producto + "</SERVICIO>" + " <RUT_DV_CLIENTE>" + rut + RutUtil.calculaDv(rut) +
                        "</RUT_DV_CLIENTE>" + " <PAG_RET>" +
                        ResourceBundleUtil.getProperty("cl.bice.vida.bancoestado.contexto.url.pago.forward") +
                        "/jsp/closewindow.jsp</PAG_RET>" + " <TIPO_CONF>nomq</TIPO_CONF>" +
                        " <METODO_REND>POST</METODO_REND>" + " <PAG_REND>" +
                        ResourceBundleUtil.getProperty("cl.bice.vida.bancoestado.contexto.url.pago.forward") +
                        "/faces/ReceptorPago/ReceptorComprobanteBancoEstado</PAG_REND>" + " <BANCO>0012</BANCO>" + " <CANT_MPAGO>" +
                        info_comprobantes.size() + "</CANT_MPAGO>" + " <TOTAL>" +
                        newTotalPorPagar.getEnPesos().longValue() + "</TOTAL>" + "</ENCABEZADO>" + "<MULTIPAGO>" +
                        "  <GLOSA_MPAGO>" + producto + "</GLOSA_MPAGO>" + "  <ID_MPAGO>" + idtrx + "</ID_MPAGO>" +
                        "  <PAGO>" + "    <RUT_DV_EMP>" + rutEmpresa + "</RUT_DV_EMP>" + "    <NUM_CONV>" +
                        cod_bicevida.getIDCOM() + "</NUM_CONV>" + "    <FEC_TRX>" +
                        FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()), "yyyyMMdd") +
                        "</FEC_TRX>" + "    <HOR_TRX>" +
                        FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()), "HHmmss") +
                        "</HOR_TRX>" + "    <FEC_VENC>" +
                        FechaUtil.getFechaFormateoCustom(new java.util.Date(System.currentTimeMillis()), "yyyyMMdd") +
                        "</FEC_VENC>" + "    <GLOSA>" + producto + "</GLOSA>" + "    <COD_PAGO/>" + "    <MONTO>" +
                        newTotalPorPagar.getEnPesos().longValue() + "</MONTO>" + "  </PAGO>" + "</MULTIPAGO>" +
                        "</INICIO>";
                }


                /**
                   * Codigo que genera XML para Servipag
                   */
                if (newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_SERVIPAG ||
                    newConvenio.getCodigo().intValue() == CODE_MEDIOPAGO_SERVIPAG2) {

                    /**
                       * Este indicar genera que el resultado de la operacion a enviar
                       * a servipag sea un XML con un solo detalle por el total del monto
                       * a enviar de pendiendo de su valor
                       *  en variable todoenuno.
                       *  1) valor true = Uno solo detalle del pago
                       *  2) valor false = Detalles separados por cada producto a pagar
                       */
                    boolean todoenuno = true;

                    /**
                       * Codigo para generar documento XML de servipag
                       */
                    logger.info("generaXmlConfirmacionPrimeraPrima() - Genero xml Servipag");
                    String codcanalpago = "000";
                    logger.info("generaXmlConfirmacionPrimeraPrima() - Instancia ServipagDocumentPec");
                    Servipag docServipag = new Servipag(); //ServipagDocumentPec.Factory.newInstance();
                    logger.info("generaXmlConfirmacionPrimeraPrima() - Agrega nuevo documento addNewServipag()");
                    //ServipagDocumentPec.Servipag servipag = docServipag.addNewServipag();

                    //Genera ID
                    String idtrx = Long.toString(idTransaccion);

                    if (cod_empresa == 1) {
                        //codcanalpago = "023";
                        codcanalpago = ResourceBundleUtil.getProperty("servipag.bicevida.codcanalpago");
                        /**
                    * Caso Bicehipotecaria canal de pago 24 para servipag
                    */
                    } else {
                        //codcanalpago = "024";
                        codcanalpago = ResourceBundleUtil.getProperty("servipag.hipotecaria.codcanalpago");
                    }


                    // EncryptSeguridadBiceUtil.crearClaves();

                    

                    //Seteao Datos de Header
                    logger.info("generaXmlConfirmacionPrimeraPrima() - Seteao Datos de Header: HeaderDocumentPec.Header header = servipag.addNewHeader() ");
                    Header header = new Header();
                    header.setCodigoCanalPago(codcanalpago);
                    header.setIdTxPago(idtrx);
                    header.setFechaPago(FechaUtil.getFechaFormateoCustom(fechapago, "yyyyMMdd"));
                    header.setMontoTotalDeuda(newTotalPorPagar.getEnPesos().longValue());

                    String rut = Integer.toString(newConfirmacion.getRutCliente());
                    while (rut.length() < 8)
                        rut = "0" + rut;

                    /*CARGAMOS NUEVOS PARAMETROS SERVIPAG*/
                    cargaNuevosParametros(header, newConfirmacion);

                    //Seteo boletas a pagar
                    int comprobantes = 0;
                    long montototal = 0;
                    for (int j = 0; j < info_comprobantes.size(); j++) {
                        String[] info_prodpagado = (String[]) info_comprobantes.get(j);
                        //Seteo Data
                        if (todoenuno == false) {
                            Documentos documentopago = new Documentos();
                            documentopago.setIdSubTx((new Long(comprobantes + 1)).toString());
                            //documentopago.setIdentificador(rut); //Rut
                            documentopago.setIdentificador(info_prodpagado[3]); //Numero Producto
                            documentopago.setBoleta(info_prodpagado[5]); //codigo_boleta = "" + prod.getNumProducto() + comprobante.getCuota()+ idTransaccion;
                            documentopago.setMonto((new Long(info_prodpagado[1])).longValue());
                            documentopago.setFechaVencimiento(info_prodpagado[4]);

                            docServipag.getDocumentos().add(documentopago);
                        }
                        montototal = montototal + (new Long(info_prodpagado[1])).longValue();
                        comprobantes++;
                    }
                    header.setNumeroBoletas(comprobantes);

                    /**
                       * Determina si todo sale en un solo detalle
                       */
                    String boleta = "";
                    String fechaVencimiento = "";
                    if (todoenuno == true) {
                        String pagos;
                        if ((comprobantes + 1) >= 10) {
                            pagos = "" + (comprobantes);
                        } else {
                            pagos = "0" + (comprobantes);
                        }

                        String[] info_prodpagado = (String[]) info_comprobantes.get(0);
                        Documentos documentopago = new Documentos();
                        documentopago.setIdSubTx((new Long(1)).toString());
                        documentopago.setIdentificador(rut); //Rut
                        //documentopago.setIdentificador(info_prodpagado[3]+pagos); //Numero Producto + nro pagos
                        //documentopago.setBoleta("1234"); //Numero Poliza
                        boleta = info_prodpagado[5] + pagos;
                        documentopago.setBoleta(boleta); //codigo_boleta + pagos = "" + prod.getNumProducto() + comprobante.getCuota()+ idTransaccion + pagos;
                        documentopago.setMonto(montototal);
                        //documentopago.setFechaVencimiento(datetool.formatFecha("yyyyMMdd", fechapago));
                        fechaVencimiento = info_prodpagado[4];
                        documentopago.setFechaVencimiento(fechaVencimiento);
                        header.setNumeroBoletas(1);

                        docServipag.getDocumentos().add(documentopago);
                    }

                    // encriptacion de los datos
                    String datafirmar =
                        codcanalpago + FechaUtil.getFechaFormateoCustom(fechapago, "yyyyMMdd") +
                        newTotalPorPagar.getEnPesos().toString();

                    datafirmar += "1" +idtrx + boleta + montototal + fechaVencimiento;
                    logger.info("generaXmlConfirmacion() - Dato a firmar \"IdTxPagoFECHAPAGOMONTOTOTALDEUDA\": " +
                                 datafirmar);

                    logger.info("generaXmlConfirmacion() - Inicializa algoritmo de encriptacion");
                    Signature sig = Signature.getInstance("MD5WithRSA");
                    logger.info("generaXmlConfirmacion() - Construye claves a partir de base 64");
                    PublicKey keyPubl =
                        EncryptSeguridadBiceUtil.getClavePublicaByBase64(ResourceBundleUtil.getProperty("servipag.signature.clave.publica"));
                    PrivateKey keyPriv =
                        EncryptSeguridadBiceUtil.getClavePrivadaByBase64(ResourceBundleUtil.getProperty("servipag.signature.clave.privada"));

                    String firmado64 = EncryptSeguridadBiceUtil.firmarDataBase64(sig, keyPriv, datafirmar.getBytes());
                    logger.info("generaXmlConfirmacion() - Dato a firmado en base 64: " + firmado64);

                    logger.info("generaXmlConfirmacion() - Finaliza algoritmo de encriptacion");

                    //Comprobaci�n de de los datos encriptados en foema correcta1
                    logger.info("generaXmlConfirmacion() - Inicializa Proceso de comprobaci�n de encripataci�n");
                    byte[] firmadecode = EncryptSeguridadBiceUtil.base64decode(firmado64);
                    logger.info("generaXmlConfirmacion() - Se decodifica la firma en base 64");
                    boolean resultado =
                        EncryptSeguridadBiceUtil.verisingDataSignature(sig, keyPubl, datafirmar.getBytes(),
                                                                       firmadecode);
                    if (resultado) {
                        logger.info("generaXmlConfirmacion() - se codific� y encript� correctamente, resultado: " +
                                     resultado);
                    } else {
                        logger.error("generaXmlConfirmacion() - se codific� y encript� incorrectamente, resultado: " +
                                     resultado);
                    }
                    
                    
                    header.setFirmaEPS(firmado64);    
                    
                    docServipag.setHeader(header);
                    //XMLPagoConvenio = docServipag.xmlText();
                    XMLPagoConvenio = JAXBUtil.convertObjectTOXmlString(docServipag);
                }


                /**
                  * AQUI SETEAR PARAMETRO XML AL CONVENIO
                  */
                logger.info("XML BANCO ===>" + XMLPagoConvenio);
                System.out.println("XML BANCO ===>" + XMLPagoConvenio);
                newConvenio.setXML(XMLPagoConvenio);

                break;
            }
        }
        logger.info("generaXmlConfirmacionPrimeraPrima() - termino");
        return conf;
    }
    
    @Override
    public String generaXmlResumenPagosConProductosSeleccionados(String xmlResumenPagos, ResumenRequest req,
                                                                 int idCanal, String email) {
        this.email = email;
        return generaXmlResumenPagosConProductosSeleccionados(xmlResumenPagos, req, idCanal);
    }

    private void cargaNuevosParametros(Header header, ConfirmacionDocument.Confirmacion newConfirmacion) {
        /**
         * NUEVOS DATOS SERVIPAG
         * NOMBRECLIENTE
         * RUTCLIENTE
         * EMAILCLIENTE
         * VERSION
         * */
        header.setNombreCliente(newConfirmacion.getNombreCliente());
        header.setRutCliente(newConfirmacion.getRutCliente() + "" +
                             RutUtil.calculaDv(Integer.toString(newConfirmacion.getRutCliente())));
        header.setEmailCliente((email == null) ? "" : email);
        header.setVersion(ResourceBundleUtil.getProperty("servipag.version"));
        /**
         * FIN NUEVOS PARAMETROS DE ENVIO
         */

    }
    
    private String generaNuevoXMLBChile(String xmlMPINI, String url, String pathKey) throws IOException,
                                                                                            DocumentException {
        /*
        String xml2 = "<MensajeE><MPINIComer>";
        xml2 += xmlMPINI;
        xml2 += "</MPINIComer>";
        xml2 += "<DirLlavePriv>" 
                    + pathKey
                +"</DirLlavePriv>";
        xml2 += "</MensajeE>";
        logger.info("BChile XML2 -> " + xml2);
        
        String xmlClaveEncriptada = URLUtil.callUrlPost(url, xml2, "GET");

        Document document = DocumentHelper.parseText(xmlClaveEncriptada);
        Node node = document.selectSingleNode("//MensajeS/MPINIFirma");   
        
        
        logger.info("Clave Obtenida -> " + node.getText());
        String xmlFinal = "<MPINISecure><MPINIComer>";
        xmlFinal += xmlMPINI;
        xmlFinal += "</MPINIComer>";
        xmlFinal += "<MPINIFirma>" + node.getText() + "</MPINIFirma>";
        xmlFinal += "</MPINISecure>";
        
        logger.info("XML FINAL sera enviado a BChile -> " + xmlFinal);
        return xmlFinal;
        */
        return xmlMPINI;
    }

    
}
