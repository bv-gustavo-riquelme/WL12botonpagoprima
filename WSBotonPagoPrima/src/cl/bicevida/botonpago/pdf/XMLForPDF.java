package cl.bicevida.botonpago.pdf;

import cl.bicevida.botonpago.util.FechaUtil;
import cl.bicevida.botonpago.util.NumeroUtil;
import cl.bicevida.botonpago.util.RutUtil;
import cl.bicevida.botonpago.util.StringUtil;
import cl.bicevida.botonpago.vo.prima.PagoDetalleCuotaVO;
import cl.bicevida.botonpago.vo.prima.PagoPublicoVO;

import java.util.Hashtable;

import java.util.LinkedHashMap;

import org.apache.batik.dom.util.HashTable;

import org.codehaus.jackson.map.ObjectMapper;

public class XMLForPDF {
    public XMLForPDF() {
        super();
    }
    
    /**
     * Recupera el XML para renderizado
     * de pdf
     * @return
     */
    public String getXMLForPDF(PagoPublicoVO pagoConsolidado) {
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>"+
        "<comprobante>" + 
        " <header>" + 
        "    <formadepago>" + pagoConsolidado.getFormaPago() + "</formadepago>" + 
        "    <numerotransaccion>" + pagoConsolidado.getBanco().getIdTransaccionBanco() + "</numerotransaccion>" + 
        "    <fechaoperacion>" +FechaUtil.getFechaFormateoStandar(pagoConsolidado.getFechaTransaccion()) + "</fechaoperacion>" + 
        "    <nombre>" + StringUtil.replaceBadCharsFOPDF(pagoConsolidado.getDatosCliente().getNombreCliente()) + "</nombre>" + 
        "    <rut>" + NumeroUtil.formatMilesNoDecimal(pagoConsolidado.getDatosCliente().getRutCliente()) + "-" + RutUtil.calculaDv(Integer.toString(pagoConsolidado.getDatosCliente().getRutCliente())) + "</rut>" + 
        "    <direccion>" + StringUtil.replaceBadCharsFOPDF(pagoConsolidado.getDatosCliente().getDireccion()) + "</direccion>" + 
        "    <comuna>" + StringUtil.replaceBadCharsFOPDF(pagoConsolidado.getDatosCliente().getComuna()) + "</comuna>" + 
        "    <ciudad>" + StringUtil.replaceBadCharsFOPDF(pagoConsolidado.getDatosCliente().getRegion()) + "</ciudad>" + 
        " </header>" +
        " <detalle>";

        //Iteracion
        int items = pagoConsolidado.getHashcuotas().size();
        ObjectMapper mapper = new ObjectMapper();
       // Hashtable hash = pagoConsolidado.getHashcuotas();
        LinkedHashMap hash = pagoConsolidado.getHashcuotasl();
        for(int counter = 1; counter <= items; counter++){
        //while (items.hasNext()) {
            //PagoDetalleCuotaVO dto = (PagoDetalleCuotaVO)pagoConsolidado.getHashcuotas().get(counter);
            //PagoDetalleCuotaVO dto = mapper.convertValue(hash.get(""+counter), PagoDetalleCuotaVO.class);
            PagoDetalleCuotaVO dto = mapper.convertValue(hash.get(counter), PagoDetalleCuotaVO.class);
            if(dto.isSelecionado()){
            
                String tipoProd = dto.getTipoProducto().toUpperCase();
                if (dto.getCodigoProducto() == 1060 || dto.getCodigoProducto() == 1063 || dto.getCodigoProducto() == 1064) { //APV     
                    tipoProd = dto.getTipoProducto().toUpperCase();
                    if (tipoProd == null || tipoProd.trim().length() == 0) {
                        tipoProd = "Regimen Tributario A";
                    }
                }
                if (dto.getCodigoProducto() == 1061) { //57BICE
                    tipoProd = "57 Bis";
                }
                if (dto.getCodigoProducto() == 1062) { //FLEXIBLE MUTUO
                    tipoProd = "Flexible Mutuo";
                }
                
                xml = xml + 
                "   <row>" + 
                "     <producto>" + StringUtil.replaceBadCharsFOPDF(dto.getDescripcionProducto()) + "</producto>" + 
                "     <tipo>"+ StringUtil.replaceBadCharsFOPDF(tipoProd) +"</tipo> " + 
                "     <numeropoliza>N&#xB0;" + dto.getNumeroProducto() + "</numeropoliza>" +                
                "     <periodocobertura>" + FechaUtil.getFechaFormateoCustom(dto.getFechaVencimiento(),"MMMM yyyy") + "</periodocobertura>" + 
                "     <primapagouf>"+ NumeroUtil.formatMilesSiDecimal(dto.getValorEnUF()) +"</primapagouf> " + 
                "     <primapagopesos>"+ NumeroUtil.formatMilesNoDecimal(dto.getValorEnPesos()) +"</primapagopesos> " + 
                "     <saldofavor>0</saldofavor>" + 
                "     <montopagado>" + NumeroUtil.formatMilesNoDecimal(dto.getTotalCuota()) + "</montopagado>" + 
                "   </row>" ;
            }
        }
        
        xml = xml +
        " </detalle>" + 
        " <footer>" + 
        "   <fechavencimiento>" + FechaUtil.getFechaFormateoCustom(pagoConsolidado.getUltimoVencimientoDeuda(),"MMMM yyyy") + "</fechavencimiento>" + 
        "   <totalapagar>" + NumeroUtil.formatMilesNoDecimal(pagoConsolidado.getTotalPagar()) + "</totalapagar>";
        if ( pagoConsolidado.getResultadoPagoBanco() != null) {
            xml = xml +
            "   <numerooperacion>" + pagoConsolidado.getResultadoPagoBanco().getNumeroTransaccionBanco() + "</numerooperacion>" + 
            "   <numerocuotas>" + pagoConsolidado.getResultadoPagoBanco().getNumeroCuotasBanco() + "</numerocuotas>" + 
            "   <codigoautorizacion>" + pagoConsolidado.getResultadoPagoBanco().getCodigoAutorizacion() + "</codigoautorizacion>" + 
            "   <numeroordencompra>" + pagoConsolidado.getResultadoPagoBanco().getNumeroOrdenCompra() + "</numeroordencompra>" + 
            "   <numerotarjeta>" +pagoConsolidado.getResultadoPagoBanco().getNumeroTarjeta() + "</numerotarjeta>" + 
            "   <fechabanco>" + pagoConsolidado.getResultadoPagoBanco().getFechaBanco() + "</fechabanco>" + 
            "   <tipocuota>" + StringUtil.replaceBadCharsFOPDF(pagoConsolidado.getResultadoPagoBanco().getTipoCuotas()) + "</tipocuota>" + 
            "   <moneda>" + pagoConsolidado.getResultadoPagoBanco().getMonedaPago() + "</moneda>" + 
            "   <horabanco>" + pagoConsolidado.getResultadoPagoBanco().getHoraBanco() + "</horabanco>";
        }
        xml = xml + " </footer></comprobante>";        
        xml = xml.replaceAll("null", "-");
        
        return xml;
    }
}
