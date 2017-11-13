package cl.bicevida.botonpago.util;

import cl.bice.vida.botonpago.common.util.URLUtil;

import cl.bicevida.botonpago.servicios.Servicios;
import cl.bicevida.botonpago.vo.in.PDFPoliza;
import cl.bicevida.botonpago.vo.out.OutDatosPdf;
import cl.bicevida.botonpago.vo.prima.PagoDetalleCuotaVO;
import cl.bicevida.botonpago.vo.prima.PagoPublicoVO;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Vector;

import org.apache.log4j.Logger;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Gustavo Riquelme G.
 * @version 15000000000000000000000V
 * Clase que retorna las URL de Pdf tanto para polizas como cupones consolidados
 */
public class LinkPDF {
    
    private static final Logger logger = Logger.getLogger(LinkPDF.class);
    
    public LinkPDF() {
        super();
    }
    
    //NUEVA IMPLEMENTACION PARA EL SERVICIO
    public static OutDatosPdf getHtmlPDFs(PagoPublicoVO pagoPublico){
        
        
        List<String> itemspintar = new ArrayList<String>();
        OutDatosPdf outPDF = new OutDatosPdf();
        
        String[] itemsdata = pagoPublico.getDatosCliente().getIdsPDFs();
        for(int i = 0; i < itemsdata.length; i++){
            if(itemsdata[i] != null ){
                String datanumeropdf = itemsdata[i];
                if(datanumeropdf.indexOf("-") != -1 ){
                    String[] data = datanumeropdf.split("-");
                    String data_idpdf = data[0];
                    String data_poliza = data[1];
                    String data_ramo = data[2];
                    if (data_idpdf != null && data_idpdf.length() > 0 && data_poliza.trim().length() == 0 && data_ramo.trim().length() == 0) {
                        itemspintar.add(data_idpdf);
                    }
                }
            }
        }
        if (itemspintar.size() > 0 ){
            //GENERA EL HTML A PEGAR POR LOS CUPONES.
            outPDF = getHtmlImagePdfConsolidado(itemspintar, pagoPublico);
        }else{
            outPDF.setUrl(ResourceMessageUtil.getProperty("jsf.pdf.nocomprobante"));
        }          
        logger.info("getHtmlPDFs() - termina");   
        
        return outPDF;
    }
    
    private static List<OutDatosPdf> getHtmlImagePDFsByPoliza(int poliza, int ramo,PagoPublicoVO pagoPublico) {
        logger.info("getHtmlImagePDFsByPoliza() - iniciando");
        List<String> itemspintar = new ArrayList<String>();
        String[] itemsdata = pagoPublico.getDatosCliente().getIdsPDFs();
        for(int i = 0; i < itemsdata.length; i++){
            if(itemsdata[i] != null ){
                String datanumeropdf = itemsdata[i];
                if(datanumeropdf.indexOf("-") != -1 ){
                    String[] data = datanumeropdf.split("-");
                    String data_idpdf = data[0];
                    String data_poliza = data[1];
                    String data_ramo = data[2];

                    if (((data_poliza != null && data_poliza.trim().length() > 0) &&
                         (data_ramo != null && data_ramo.trim().length() > 0))) {
                        if (poliza == Integer.valueOf(data_poliza).intValue()) {
                            if (ramo == Integer.valueOf(data_ramo).intValue()) {
                                itemspintar.add(data_idpdf);
                            }
                        }
                    }
                }
            }
        }
        logger.info("getHtmlImagePDFsByPoliza() - iniciando");
        return getHtmlImagePDFs(itemspintar, pagoPublico);   
    }
    
    
    private static List<OutDatosPdf> getHtmlImagePDFs(List<String> itemsdata, PagoPublicoVO pagoPublico) {
            logger.info("getHtmlImagePDFs() - iniciando");
            
            List<OutDatosPdf> listOudPDF = new ArrayList<OutDatosPdf>();
            OutDatosPdf outPDF = new OutDatosPdf();
            
            if (itemsdata.size() > 1 ) {            
                for (int x = 0; x < itemsdata.size() ; x++) {
                    String numeropdf = itemsdata.get(x);                    
                    String urlgo = pagoPublico.getDatosCliente().getUrlPDFs() + pagoPublico.getDatosCliente().getRutCliente()+ "-" + numeropdf + ".pdf";                
                    if(URLUtil.exists(urlgo)){
                        outPDF.setPoliza(numeropdf);
                        outPDF.setUrl(urlgo);
                        listOudPDF.add(outPDF);
                    }else{
                        outPDF.setUrl(ResourceMessageUtil.getProperty("jsf.pdf.nocomprobante"));
                        listOudPDF.add(outPDF);
                   }
                }    
            } 
            logger.info("getHtmlImagePDFs() - terminando");
            return listOudPDF;
        }
    private static OutDatosPdf getHtmlImagePdfConsolidado(List<String> itemsdata, PagoPublicoVO pagoPublico){
        OutDatosPdf outPDF = new OutDatosPdf();
        
        if (itemsdata.size() == 0) {
            outPDF.setUrl(ResourceMessageUtil.getProperty("jsf.pdf.nocomprobante"));
            return outPDF;
        }
        if (itemsdata.size() == 1) {
            String numeropdf = itemsdata.get(0);
            String urlgo = pagoPublico.getDatosCliente().getUrlPDFs() + pagoPublico.getDatosCliente().getRutCliente() + "-" + numeropdf + ".pdf";
            if (URLUtil.exists(urlgo)) {
                outPDF.setPoliza(numeropdf);
                outPDF.setUrl(urlgo);
            } else {
                outPDF.setUrl(ResourceMessageUtil.getProperty("jsf.pdf.nocomprobante"));
                return outPDF;    
            }
        }
        return outPDF;
    }
    
    public static List<OutDatosPdf> obtieneDataHashTable(PagoPublicoVO pagoPublico){
        
            ObjectMapper mapper = new ObjectMapper();
           // Hashtable hash = pagoPublico.getHashcuotas();
        LinkedHashMap hash = pagoPublico.getHashcuotasl();
            List<OutDatosPdf> listDatoPdf = new ArrayList<OutDatosPdf>();
            if(hash.size() > 0 && hash != null){
                for ( int i = 1; i <= hash.size(); i++){
                    //PagoDetalleCuotaVO vCuota = mapper.convertValue(hash.get(""+i), PagoDetalleCuotaVO.class);
                    PagoDetalleCuotaVO vCuota = mapper.convertValue(hash.get(i), PagoDetalleCuotaVO.class);
                    listDatoPdf = getHtmlImagePDFsByPoliza(vCuota.getNumeroProducto(),vCuota.getNumeroRamo(),pagoPublico);
                }
            }
        return listDatoPdf;
    }


}
