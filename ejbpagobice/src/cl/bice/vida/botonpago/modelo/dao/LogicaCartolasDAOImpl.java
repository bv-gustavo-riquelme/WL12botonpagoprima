package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.CartolaPolizas;
import cl.bice.vida.botonpago.common.dto.general.DetallePagoPoliza;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosHistoricoVw;
import cl.bice.vida.botonpago.common.util.NumeroUtil;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import noNamespace.CartolaDocument;
import noNamespace.DetalleCargos;
import noNamespace.EstadoCaja;
import noNamespace.EstadoInternet;
import noNamespace.IdentificacionMensaje;
import noNamespace.MontoCuota;
import noNamespace.Periodo;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;


public class LogicaCartolasDAOImpl extends DataSourceBice implements LogicaCartolasDAO {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(LogicaCartolasDAOImpl.class);


    public String crearCartola(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin, int tipo) {
        return null;
    }

    /**
     * Confecciona una cartola segun
     * los datos solicitados
     * @param rut
     * @param nombre
     * @param email
     * @param rut_usuario
     * @param nombre_usuario
     * @param fechaini
     * @param fechafin
     * @param tipo
     * @return
     * TODO: Testear
     */
    public String crearCartolaPoliza(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin, int tipo) {
        String cartolaxml="NOK|Error: Inesperado";
        try {   
                if(nombre == null) nombre = "";
                if(email == null) email = "";
                if(nombre_usuario == null) nombre_usuario = "";
                
                if(fechaini == null || fechafin == null){
                        cartolaxml="NOK|Error: Debe ingresar intervalo de fechas correctas para hacer la busqueda";
                } else if(tipo != 0 && tipo != 1) cartolaxml="NOK|Error: Tipo de busqueda invalido";
                else{
                        CartolaDocument cartola=generaXmlCartolaPoliza(rut,nombre,email,rut_usuario,nombre_usuario,fechaini,fechafin,tipo);
                        cartolaxml=cartola.toString();
                }
        } catch (Exception e) {
            cartolaxml="NOK|Error: "+e.getMessage();            
        }
        return cartolaxml;
    }

    /**
     * Confecciona una cartola para dividendos
     * @param rut
     * @param nombre
     * @param email
     * @param rut_usuario
     * @param nombre_usuario
     * @param fechaini
     * @param fechafin
     * @param tipo
     * @return
     * TODO: Testear
     */
    public String crearCartolaDividendo(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin, int tipo) {
        String cartolaxml="NOK|Error: Inesperado";
        try {   
                if(nombre == null) nombre = "";
                if(email == null) email = "";
                if(nombre_usuario == null) nombre_usuario = "";                
                if(fechaini == null || fechafin == null){
                        cartolaxml="NOK|Error: Debe ingresar intervalo de fechas correctas para hacer la busqueda";
                }else if(tipo != 0 && tipo != 1) cartolaxml="NOK|Error: Tipo de busqueda invalido";
                else{
                    CartolaDocument cartola=generaXmlCartolaDividendo(rut,nombre,email,rut_usuario,nombre_usuario,fechaini,fechafin,tipo);
                    cartolaxml=cartola.toString();
                }
        } catch (Exception e) {
            cartolaxml="NOK|Error: "+e.getMessage();
        }
        return cartolaxml;
    }

    /**
     * Validacion de XML
     * @param xml
     * @throws Exception
     */
    public void validateXML(XmlObject xml) throws Exception {
        Boolean validar = new Boolean(ResourceBundleUtil.getProperty("xml.validation.enable"));        
        if (validar.booleanValue() == false) return;            
        Collection errorList = new ArrayList();
        XmlOptions xo = new XmlOptions();
        xo.setErrorListener(errorList);
        if (!xml.validate(xo)) {
            System.out.println(xml.toString());
            StringBuffer sb = new StringBuffer();
            for (Iterator it = errorList.iterator(); it.hasNext(); ) {
                sb.append(it.next() + "\n");
            }
            String txtMsg = "XML con errores:" + sb.toString();
            throw new Exception(txtMsg);
        }    
    }

    public CartolaDocument generaXmlCartola(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin, int tipo) {
        return null;
    }

    /**
     * Genera el XML de cartola para poliza
     * @param rut
     * @param nombre
     * @param email
     * @param rut_usuario
     * @param nombre_usuario
     * @param fechaini
     * @param fechafin
     * @param tipo
     * @return
     * TODO: Testear
     */
    public CartolaDocument generaXmlCartolaPoliza(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin, int tipo) throws Exception {        
        CartolaDocument cartola = CartolaDocument.Factory.newInstance();
        CartolaDocument.Cartola newCartola = cartola.addNewCartola();
        Long registro_cartola = null;

        setIdentificacionMensaje(newCartola,tipo);
        
        if(tipo==0){
            //Genera secuencia nueva
            registro_cartola = new Long(Integer.toString(DAOFactory.getPersistenciaGeneralDao().getSecuenceValue("BPI_CAR_IDCARTOLA_SEQ")));
            newCartola.setIdCartola(registro_cartola);
        }
        
        newCartola.setRutCliente(rut);
        newCartola.setNombreCliente(nombre);
        newCartola.setEmailCliente(email);
        
        if(rut_usuario>0) {
           CartolaDocument.Cartola.Usuario usuario = newCartola.addNewUsuario();
           usuario.setRut(rut_usuario);
           usuario.setNombre(nombre_usuario);
        }
        
        //SETEO PERIODO DE LA CONSULTA
        Periodo periodo = newCartola.addNewPeriodoConsulta();
        Calendar cini = Calendar.getInstance();
        cini.setTime(fechaini); 
        periodo.setInicio(cini);
        Calendar cfin = Calendar.getInstance();
        cfin.setTime(fechafin);
        periodo.setFin(cfin);
        
        //Copia ProductosPagados        
        setProductosPagadosPoliza(newCartola,rut,fechaini,fechafin);
        cartola.setCartola(newCartola);
                
        validateXML(cartola); 
        if(registro_cartola!=null){
            // 1 = Bice Vida
            DAOFactory.getPersistenciaGeneralDao().newCartola(registro_cartola,rut,fechaini,fechafin, 1, cartola.toString());
        }
        return cartola;
    }

    /**
     * Genera el XML de Cartola para Dividendos
     * @param rut
     * @param nombre
     * @param email
     * @param rut_usuario
     * @param nombre_usuario
     * @param fechaini
     * @param fechafin
     * @param tipo
     * @return
     * TODO: Testear
     */
    public CartolaDocument generaXmlCartolaDividendo(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin, int tipo) throws Exception {        
        CartolaDocument cartola = CartolaDocument.Factory.newInstance();
        CartolaDocument.Cartola newCartola = cartola.addNewCartola();
        Long registro_cartola = null;
        setIdentificacionMensaje(newCartola,tipo);        
        if(tipo==0){
            //Genera ID
            registro_cartola = new Long(Integer.toString(DAOFactory.getPersistenciaGeneralDao().getSecuenceValue("BPI_CAR_IDCARTOLA_SEQ")));
            newCartola.setIdCartola(registro_cartola);
        }
        
        newCartola.setRutCliente(rut);
        newCartola.setNombreCliente(nombre);
        newCartola.setEmailCliente(email);
        
        if(rut_usuario>0) {
           CartolaDocument.Cartola.Usuario usuario = newCartola.addNewUsuario();
           usuario.setRut(rut_usuario);
           usuario.setNombre(nombre_usuario);
        }
        
        //SETEO PERIODO DE LA CONSULTA
        Periodo periodo = newCartola.addNewPeriodoConsulta();
        Calendar cini = Calendar.getInstance();
        cini.setTime(fechaini); 
        periodo.setInicio(cini);
        Calendar cfin = Calendar.getInstance();
        cfin.setTime(fechafin);
        periodo.setFin(cfin);
        
        //Copia ProductosPagados        
        setProductosPagadosDividendo(newCartola,rut,fechaini,fechafin);
        cartola.setCartola(newCartola);
                
        validateXML(cartola); 
        if(registro_cartola!=null){
            // 2 = Bice Hipotecaria
            DAOFactory.getPersistenciaGeneralDao().newCartola(registro_cartola,rut,fechaini,fechafin,2, cartola.toString());
        }
        return cartola;

    }

    /**
     * Genera identificacion del mensaje de XML
     * @param newCartola
     * @param tipo
     * TODO: Testear
     */
    public void setIdentificacionMensaje(CartolaDocument.Cartola newCartola, int tipo) {
        IdentificacionMensaje idm;        
        idm = newCartola.addNewIdentificacionMensaje();        
        idm.setCodigo("CARTOL");
        idm.setVersion("1.0");
        idm.setDe("PAGWEB");
        idm.setFechaCreacion(Calendar.getInstance());
        if(tipo==0) idm.setAccion("CONSULTA");
        else idm.setAccion("NAVEGACION");    
    }

    public void setProductosPagados(CartolaDocument.Cartola newCartola, int rut, Date fechaini, Date fechafin) {
    }

    /**
     * Establece los productos pagados
     * @param newCartola
     * @param rut
     * @param fechaini
     * @param fechafin
     * TODO: Testear
     */
    public void setProductosPagadosPoliza(CartolaDocument.Cartola newCartola, int rut, Date fechaini, Date fechafin) {
        long deuda = 0;
        double deuda_uf = 0;
        
        CartolaDocument.Cartola.ProductosPagados polizasPagadas = newCartola.addNewProductosPagados();
        polizasPagadas.setEmpresa("Seguros Individuales");
        polizasPagadas.setTotalPagado(new BigInteger("0"));
        polizasPagadas.setTotalPagadoUF(new BigDecimal("0"));
        List polizas = DAOFactory.getConsultasDao().findCartolaPolizasByRutInPeriod(rut,fechaini,fechafin);
        if (polizas != null && polizas.size() > 0) {
            SetPolizas(polizasPagadas, polizas);
            deuda += polizasPagadas.getTotalPagado().longValue();
            deuda_uf += polizasPagadas.getTotalPagadoUF().doubleValue();
        }        
        newCartola.setTotalPagado(new BigInteger(Long.toString(deuda)));
        newCartola.setTotalPagadoUF(new BigDecimal(Double.toString(deuda_uf)));    
    }

    /**
     * Consulta Productos pagados para Dividendos
     * @param newCartola
     * @param rut
     * @param fechaini
     * @param fechafin
     */
    public void setProductosPagadosDividendo(CartolaDocument.Cartola newCartola, int rut, Date fechaini, Date fechafin) {
        long deuda = 0;
        double deuda_uf = 0;        
        CartolaDocument.Cartola.ProductosPagados dividendosPagados = newCartola.addNewProductosPagados();
        dividendosPagados.setEmpresa("Creditos Hipotecarios");        
        dividendosPagados.setTotalPagado(new BigInteger("0"));
        dividendosPagados.setTotalPagadoUF(new BigDecimal("0"));
        List dividendos = DAOFactory.getConsultasDao().findCartolaDividendosByRutInPeriod(Integer.toString(rut),fechaini,fechafin);
        if (dividendos != null && dividendos.size() > 0) {
            setDividendos(dividendosPagados, dividendos);
            deuda += dividendosPagados.getTotalPagado().longValue();
            deuda_uf += dividendosPagados.getTotalPagadoUF().doubleValue();
        }        
        newCartola.setTotalPagado(new BigInteger(Long.toString(deuda)));
        newCartola.setTotalPagadoUF(new BigDecimal(Double.toString(deuda_uf)));    
    }

    /**
     * Setear polizas a respuesta
     * @param pp
     * @param polizas
     */
    public void SetPolizas(CartolaDocument.Cartola.ProductosPagados pp, List polizas) {
        long deuda = 0;
        double deuda_uf = 0;
        int contador = 0;
        CartolaPolizas polizaant = null;
        
        for (int i = 0; i < polizas.size(); i++) {
            ++contador;
            CartolaDocument.Cartola.ProductosPagados.Entrada entrada = pp.addNewEntrada();
            entrada.setContador(new BigDecimal(contador).unscaledValue());
            CartolaPolizas poliza = (CartolaPolizas)polizas.get(i);

            //INFORMACION DEL PRODUCTO
            CartolaDocument.Cartola.ProductosPagados.Entrada.Producto producto = entrada.addNewProducto();
            producto.setCodigoProducto(new BigInteger(poliza.getCodigoProducto().toString()));
            producto.setDescripcionProducto(poliza.getNombreProducto());
            producto.setNumProducto(new BigInteger(poliza.getPolizaPol().toString()));
            producto.setCodEmpresa(1);

            //CUOTA    
            CartolaDocument.Cartola.ProductosPagados.Entrada.Cuota cuota = entrada.addNewCuota();
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(poliza.getFechaVencimiento());
            cuota.setFechaVencimiento(calendario);            
            cuota.setMedioPago(poliza.getCanalPago());
            calendario.setTime(poliza.getFechaPago());
            cuota.setFechaPago(calendario);            
            cuota.setValorUF(new BigDecimal(poliza.getValorUf().toString()));
            cuota.setTotalPagado(new BigInteger(poliza.getMontoPagadoPesos().toString()));
            
            
            //incremento cantidad pagada
            //TODO incrementar solamente si pago no ha sido anulado posteriormente
             //if(poliza.getEstado().equals(ResourceProperties.getProperty("poliza.estado.anulada"))) {
             if(poliza.getEstado().equals("9")) {
                 if(polizaant!=null && poliza.getIdComprobpago()==polizaant.getIdComprobpago()) {
                    deuda -= poliza.getMontoPagadoPesos().intValue();
                    deuda_uf -= poliza.getPrimaBrutaUf();
                 }                 
             }else{
                deuda += poliza.getMontoPagadoPesos().intValue();
                deuda_uf += poliza.getPrimaBrutaUf();
             }
                         
            
            //primero debo buscar si el siguiente resultado corresponde al mismo pago
            MontoCuota cuota_fija = cuota.addNewMonto();
            cuota_fija.setEnUF(new BigDecimal(poliza.getPrimaBrutaUf().toString()));
            cuota_fija.setEnPesos(new BigInteger(poliza.getMontoPagadoPesos().toString()));
            
            //Detalle de los cargos
            List detallepagolist = DAOFactory.getConsultasDao().findDetallePagoPolizaByFolioRecibo(poliza.getFolioRecibo());
            if(detallepagolist!=null && detallepagolist.size()>0){
                DetalleCargos detalle = cuota_fija.addNewDetalle();                
                for(int j=0;j<detallepagolist.size();j++){
                    DetallePagoPoliza detallepago = (DetallePagoPoliza)detallepagolist.get(j);
                    DetalleCargos.Cargo cargo = detalle.addNewCargo();
                    cargo.setDescripcion(detallepago.getDescCargo());
                    cargo.setBigDecimalValue(new BigDecimal(detallepago.getMonto().toString()));
                }
            }
        
             if(poliza.getTipoComprobante() == 2){ 
                 logger.info("SetPolizasMes() - PAGO POR CAJA");
                 EstadoCaja estado = cuota.addNewEstadoCaja();
                 String realEstado = ResourceBundleUtil.getProperty(poliza.getEstado());
                 estado.setEstado(realEstado);  
                 //estado.setEstado(poliza.getEstado());                                 
                 estado.setComprobantePago(poliza.getIdComprobpago().toString());
             }else{
                 logger.info("SetPolizasMes() - PAGO POR INTERNET");
                 EstadoInternet estado = cuota.addNewEstadoInternet();
                 String realEstado = ResourceBundleUtil.getProperty(poliza.getEstado());
                 estado.setEstado(realEstado);  
                 //estado.setEstado(poliza.getEstado());                
                 //idcomprobante de tabla dna o dpe
                 estado.setComprobantePago(poliza.getFolioRecibo().toString());
             }
             
             polizaant = poliza;
        }
         
         pp.setTotalPagado(new BigInteger(Long.toString(deuda)));
         deuda_uf=new Double(NumeroUtil.redondear(deuda_uf,4));
         pp.setTotalPagadoUF(new BigDecimal(Double.toString(deuda_uf)));    
    }

    /**
     * Setea data para dividendos
     * @param pp
     * @param dividendos
     * TODO: Testear
     */
    public void setDividendos(CartolaDocument.Cartola.ProductosPagados pp, List dividendos) {
        long deuda = 0;
        int contador  = 0;
        double deuda_uf = 0;

        for (int i = 0; i < dividendos.size(); i++) {
            ++contador;
            CartolaDocument.Cartola.ProductosPagados.Entrada entrada = pp.addNewEntrada();
            entrada.setContador(new BigDecimal(contador).unscaledValue());
            BhDividendosHistoricoVw dividendo = (BhDividendosHistoricoVw)dividendos.get(i);

            //INFORMACION DEL PRODUCTO
            CartolaDocument.Cartola.ProductosPagados.Entrada.Producto producto = 
                entrada.addNewProducto();
            producto.setCodigoProducto(new BigInteger(dividendo.getCodProducto().toString()));
            producto.setDescripcionProducto(dividendo.getProducto());
            producto.setNumProducto(new BigDecimal(dividendo.getNumOpe()).unscaledValue());
            producto.setCodEmpresa(dividendo.getCodEmpresa());

            //CUOTA    
            CartolaDocument.Cartola.ProductosPagados.Entrada.Cuota cuota = entrada.addNewCuota();
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(dividendo.getFecVencimiento());
            cuota.setFechaVencimiento(calendario);
            if(dividendo.getNombreSucursalPago()!=null)
                cuota.setMedioPago(dividendo.getNombreSucursalPago().toString());
            else cuota.setMedioPago("");
            calendario.setTime(dividendo.getFecPago());
            cuota.setFechaPago(calendario);
            cuota.setValorUF(new BigDecimal("0"));
            
            MontoCuota cuota_fija = cuota.addNewMonto();
            //cuota_fija.setEnUF(new BigDecimal(dividendo.getTotMda().toString()));
             cuota_fija.setEnUF(new BigDecimal(dividendo.getTotalPagado().toString()));
            
            
            //cuota_fija.setEnPesos(new BigInteger(dividendo.getTotalPagado().toString()));
            //Solución pendiente
            Integer totalpagado = new Integer (dividendo.getTotalPagado().intValue() + dividendo.getDif_boleta().intValue());
            cuota_fija.setEnPesos(new BigInteger(totalpagado.toString()));
            
            DetalleCargos detalle = cuota_fija.addNewDetalle();
            DetalleCargos.Cargo cargo;
            List detalleDividendo = dividendo.getDetalleDividendo();
            for(int j=0;j<detalleDividendo.size();j++){
                Object[] det = (Object[])detalleDividendo.get(j);
                cargo = detalle.addNewCargo();
                cargo.setDescripcion(det[0].toString());
                cargo.setBigDecimalValue(new BigDecimal(det[1].toString())); 
            }
        
            Integer totalpagadocuota = new Integer (dividendo.getTotalPagado().intValue() + dividendo.getDif_boleta().intValue());
            cuota.setTotalPagado(new BigInteger(totalpagadocuota.toString()));
            
            if(dividendo.getSucursalPago() == Integer.parseInt(ResourceBundleUtil.getProperty("dividendo.sucursal.internet"))){
                logger.info("setDividendos() - PAGO POR INTERNET");
                 EstadoInternet estado = cuota.addNewEstadoInternet();
                String realEstado = ResourceBundleUtil.getProperty(dividendo.getEstado());
                estado.setEstado(realEstado); 
                 //estado.setEstado("PAGADO");   asdfasdfasdf              
                 //uso num_dividendo para poder buscar el comprobante asociado + info de numproducto
                 estado.setComprobantePago(dividendo.getNumDiv().toString());
            }else{
                logger.info("setDividendos() - PAGO POR CAJA");
                EstadoCaja estado = cuota.addNewEstadoCaja();
                String realEstado = ResourceBundleUtil.getProperty(dividendo.getEstado());
                estado.setEstado(realEstado); 
                //estado.setEstado("PAGADO");
                  //uso num_dividendo para poder buscar el comprobante asociado + info de numproducto
                 estado.setComprobantePago(dividendo.getNumDiv().toString());
            } 
            
            deuda+=dividendo.getTotalPagado() + dividendo.getDif_boleta();
            deuda_uf+=dividendo.getTotMda();
        }
        
        pp.setTotalPagado(new BigInteger(Long.toString(deuda)));
        deuda_uf=new Double(NumeroUtil.redondear(deuda_uf,4));
        pp.setTotalPagadoUF(new BigDecimal(Double.toString(deuda_uf)));
    
    }
}
