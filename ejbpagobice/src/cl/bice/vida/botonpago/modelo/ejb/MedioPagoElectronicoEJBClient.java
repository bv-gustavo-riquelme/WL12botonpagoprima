package cl.bice.vida.botonpago.modelo.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class MedioPagoElectronicoEJBClient {
    private static Context getInitialContext() throws NamingException {
        Hashtable h = new Hashtable();
        h.put(Context.INITIAL_CONTEXT_FACTORY, "oracle.j2ee.rmi.RMIInitialContextFactory");
        h.put(Context.SECURITY_PRINCIPAL, "oc4jadmin" );
        h.put(Context.SECURITY_CREDENTIALS, "oc4j" );
        h.put(Context.PROVIDER_URL, "ormi://127.0.0.1:23891/current-workspace-app");
        InitialContext returnInitialContext = new InitialContext(h);
        return returnInitialContext;    
    }
    
    public static void crearPEC() {
      try {
          final Context context = getInitialContext();
          MedioPagoElectronicoEJB ejb = (MedioPagoElectronicoEJB)context.lookup("MedioPagoElectronicoEJB");
         
         String xmlcons = "<ConsultaPagosPendientes>\n" + 
         "      <IdentificacionMensaje>\n" + 
         "              <Codigo>CONPPC</Codigo>\n" + 
         "              <Version>1.0</Version>\n" + 
         //"              <De>SERVIP</De>\n" + 
         "              <De>BECAJV</De>\n" + 
         "              <FechaCreacion>2006-08-13</FechaCreacion>\n" + 
         "              <Accion>CONSULTA</Accion>\n" + 
         "      </IdentificacionMensaje>\n" + 
         "      <Consulta>\n" + 
         //"              <Cliente>76007078</Cliente>\n" + //bicevida Servipag
         //"              <Cliente>6346426</Cliente>\n" +  //hipotecaria Servipag
         "              <Cliente>4391060</Cliente>\n" + //bicevida banco estado
         "         <NombreCliente>NN</NombreCliente>\n" + 
         "              <Empresa>1</Empresa>\n" + 
         "      </Consulta>\n" + 
         "</ConsultaPagosPendientes>\n";
         
         String xmlresp = ejb.crearRespuestaPagosPendientes(xmlcons);         
         System.out.println("xmlresp = " +xmlresp);
         
      } catch (Exception ex) {
          ex.printStackTrace();
      }
    
    }
    
    public static void pagoPECServipag() {
          try {
              final Context context = getInitialContext();
              MedioPagoElectronicoEJB ejb = (MedioPagoElectronicoEJB)context.lookup("MedioPagoElectronicoEJB");
    /*
     <RespuestaServipagVida>
             <ListaProductos>
                     <Entrada contador="1">
                             <IdTransaccion>15041</IdTransaccion>
                             <Producto>
                                     <CodigoProducto>2014</CodigoProducto>
                                     <DescripcionProducto>Seguro Oncologico</DescripcionProducto>
                                     <NumProducto>104107</NumProducto>
                                     <CodEmpresa>1</CodEmpresa>
                             </Producto>
                             <Cuota>
                                     <FechaRecibo>2008-10-01-04:00</FechaRecibo>
                                     <Folio>1623485</Folio>
                                     <Ramo>14</Ramo>
                                     <MontoCuota>
                                             <EnPesos>4288</EnPesos>
                                             <EnUF>0.2017</EnUF>
                                     </MontoCuota>
                             </Cuota>
                     </Entrada>
                     <Entrada contador="2">
                             <IdTransaccion>15042</IdTransaccion>
                             <Producto>
                                     <CodigoProducto>2013</CodigoProducto>
                                     <DescripcionProducto>Accidentes Personales</DescripcionProducto>
                                     <NumProducto>1682407</NumProducto>
                                     <CodEmpresa>1</CodEmpresa>
                             </Producto>
                             <Cuota>
                                     <FechaRecibo>2008-10-01-04:00</FechaRecibo>
                                     <Folio>1616370</Folio>
                                     <Ramo>13</Ramo>
                                     <MontoCuota>
                                             <EnPesos>5526</EnPesos>
                                             <EnUF>0.2599</EnUF>
                                     </MontoCuota>
                             </Cuota>
                     </Entrada>
             </ListaProductos>
     </RespuestaServipagVida>
    */
             String xmlpago = 
             "<InformePagoServipag>\n" + 
             "      <IdentificacionMensaje>\n" + 
             "              <Codigo>INFPPC</Codigo>\n" + 
             "              <Version>1.0</Version>\n" + 
             "              <De>SERVIP</De>\n" + 
             "              <FechaCreacion>2007-02-13</FechaCreacion>\n" + 
             "              <Accion>OK</Accion>\n" + 
             "      </IdentificacionMensaje>\n" + 
             "      <IdTransaccion>15201</IdTransaccion>\n" + 
             "      <NumTransaccionServipag>1234567</NumTransaccionServipag>\n" + 
             "      <DetallePago>\n" + 
             "              <CodMedioPago>016</CodMedioPago>\n" + 
             "              <NumTransaccionMedio>1234567</NumTransaccionMedio>\n" + 
             "              <FormaPago>1</FormaPago>\n" + 
             "              <FechaPago>2008-12-16</FechaPago>\n" + 
             "              <FechaPagoContable>2008-12-16</FechaPagoContable>\n" + 
             "      </DetallePago>\n" + 
             "      <ProductosPagados>\n" + 
             "          <Entrada>1</Entrada>\n" + 
             "      </ProductosPagados>\n" + 
             "      <MontoTotalPagado>77495</MontoTotalPagado>\n" + 
             "</InformePagoServipag>";
             
             String xmlresp = ejb.procesarInformePagoPendientes(xmlpago);         
             System.out.println("xmlresp = " +xmlresp);
             
          } catch (Exception ex) {
              ex.printStackTrace();
          }
        
    }
    
    public static void pagoPECBEstado() {
          try {
              final Context context = getInitialContext();
              MedioPagoElectronicoEJB ejb = (MedioPagoElectronicoEJB)context.lookup("MedioPagoElectronicoEJB");
    /*
     <RespuestaPagosPendientes>
             <IdTransaccion>15223</IdTransaccion>
             <Producto>
                     <CodigoProducto>2034</CodigoProducto>
                     <DescripcionProducto>Seguros Bice Vida</DescripcionProducto>
                     <NumProducto>98503</NumProducto>
                     <CodEmpresa>1</CodEmpresa>
             </Producto>
             <Cuota>
                     <EnPesos>3652</EnPesos>
                     <EnUF>0.1718</EnUF>
             </Cuota>
     </RespuestaPagosPendientes>


    */
             String xmlpago = 
             "<InformePagoPendiente>\n" + 
             "	<IdentificacionMensaje>\n" + 
             "		<Codigo>INFPPC</Codigo>\n" + 
             "		<Version>1.0</Version>\n" + 
             "		<De>BECAJV</De>\n" + 
             "		<FechaCreacion>2008-12-10</FechaCreacion>\n" + 
             "		<Accion>OK</Accion>\n" + 
             "	</IdentificacionMensaje>\n" + 
             "	<IdTransaccion>15223</IdTransaccion>\n" + 
             "	<NumTransaccionMedio>123456</NumTransaccionMedio>\n" + 
             "  <MontoPagado>3652</MontoPagado>\n" + 
             "<CodError>2</CodError>\n" + 
             "</InformePagoPendiente>\n";
             
             String xmlresp = ejb.procesarInformePagoPendientes(xmlpago);         
             System.out.println("xmlresp = " +xmlresp);
             
          } catch (Exception ex) {
              ex.printStackTrace();
          }
        
    }
        
    public static void main(String [] args) {
        try {
            MedioPagoElectronicoEJBClient test = new MedioPagoElectronicoEJBClient();
            //test.crearPEC();
            //test.pagoPECServipag();
            //test.pagoPECBEstado();            
            test.ResumenPagoPimeraPrima();
            /*final Context context = getInitialContext();
            MedioPagoElectronicoEJB ejb = (MedioPagoElectronicoEJB)context.lookup("MedioPagoElectronicoEJB");
            SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
            dto.setIdTrx(Long.parseLong("12300"));
            dto.setCodRet(1); 
            dto.setNropPagos(1); 
            dto.setMontoPago(Integer.parseInt("1738"));
            dto.setDescRet("Rechazo de tx. en B24, No autorizada"); 
            dto.setIdCom(Long.parseLong("597026016975")); 
            dto.setIdTrxBanco("7024253292");
            dto.setIndPago("N");            
            System.out.println(ejb.actualizaTransaccionSP(dto));*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    public void ResumenPagoPimeraPrima () {
    try{
            final Context context = getInitialContext();
            MedioPagoElectronicoEJB ejb = (MedioPagoElectronicoEJB)context.lookup("MedioPagoElectronicoEJB");
            String xmlresp = ejb.crearResumenPagosPrimerasPrimas(6486533,"PRUEBA", null, ejb.CANAL_BOTON_PAGO_PUBLICO_PRIMERA_PRIMA , 307080);
            //String xmlresp = ejb.crearResumenPagosAPVAPT(10551069, "PRUEBA", null, ejb.CANAL_BOTON_PAGO_APV_EXTRAORDINARIO);
            System.out.println("xmlresp = " +xmlresp);
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
}
