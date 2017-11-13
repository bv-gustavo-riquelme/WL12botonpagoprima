package cl.bice.vida.botonpago.common.vo;

import cl.bice.vida.botonpago.common.dto.general.BpiDitribFondosAPVTbl;
import cl.bice.vida.botonpago.common.util.FechaUtil;

import cl.bice.vida.botonpago.common.util.NumeroUtil;
import cl.bice.vida.botonpago.common.util.RutUtil;
import cl.bice.vida.botonpago.common.util.StringUtil;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PagoElectronicoVO implements Serializable {
    private int idTransaccion;
    private Date fechaTransaccion;
    private String xmloriginal;
    private String xmlpagopublico;
    private String xmlconfirmacion;
    private String xmlconfirmacionDataBancos;
    private PagoBancoInfoVO banco = new PagoBancoInfoVO();
    private Date   ultimoVencimientoDeuda;
    private String estadoDeuda = "AL DIA";
    private Double totalPagar;
    private Double valorUF;
    private PagoDatosClienteVO datosCliente;
    private List cuotas;
    private int idBancoPago;
    private String urlPdfDownload;
    private String urlPdfEnEsperaDownload;
    private String absoluteServerPathPdfDownload;
    private String absoluteServerPathPdfEnEsperaDownload;    
    private String formaPago;
    private String pdfUrlVolantes = null;
    private ResultadoConsultaVO resultadoPagoBanco;
    private int tipoTransaccion;
    private Double cargo;
    private double porcentajeCobroTarjeta;
    
    /**
     * Constructor inicial
     */
    public PagoElectronicoVO() {
        cuotas = new ArrayList();
        resultadoPagoBanco = new ResultadoConsultaVO();
    }
    
    /**
     * Recupera el XML para renderizado
     * de pdf
     * @return
     */
    public String getXMLForPDF() {
        String xml;
        
        if(getTipoTransaccion() == 2){
            //CABECERA
            String xmlcabecera = "" +
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>"+
            "<comprobante>" + 
            " <header>" + 
            "    <formadepago>" + getFormaPago() + "</formadepago>" + 
            "    <numerotransaccion>" + getBanco().getIdTransaccionBanco() + "</numerotransaccion>" + 
            "    <fechaoperacion>" +FechaUtil.getFechaFormateoStandar(getFechaTransaccion()) + "</fechaoperacion>" + 
            "    <nombre>" + StringUtil.replaceBadCharsFOPDF(getDatosCliente().getNombreCliente()) + "</nombre>" + 
            "    <rut>" + NumeroUtil.formatMilesNoDecimal(getDatosCliente().getRutCliente()) + "-" + RutUtil.calculaDv(Integer.toString(getDatosCliente().getRutCliente())) + "</rut>" + 
            "    <direccion>" + StringUtil.replaceBadCharsFOPDF(getDatosCliente().getDireccion()) + "</direccion>" + 
            "    <comuna>" + StringUtil.replaceBadCharsFOPDF(getDatosCliente().getComuna()) + "</comuna>" + 
            "    <ciudad>" + StringUtil.replaceBadCharsFOPDF(getDatosCliente().getRegion()) + "</ciudad>" + 
            " </header>";        
            
            //DETALLE Y DISTRIBUCION
            String xmldetalle01 = "" +
            " <detalle>";
            String xmldetalle02 = "";
            
            String xmldistribucion01 = "" +
            " <distribucion visible=\"false\">";
            String xmldistribucion02 = "";
            
            //Iteracion
            Iterator items = getCuotas().iterator();
            while (items.hasNext()) {
                PagoDetalleCuotaVO dto = (PagoDetalleCuotaVO) items.next();
            
                String tipoProd = dto.getTipoProducto().toUpperCase();
                if (dto.getCodigoProducto() == 1060 || dto.getCodigoProducto() == 1063 || dto.getCodigoProducto() == 1064) { //APV   
                    String tipox = dto.getTipoProducto().toUpperCase();
                    if (tipox != null && tipox.trim().length() > 0) {
                        tipoProd = "R&#xE9;gimen Tributario " +dto.getTipoProducto().toUpperCase();
                    } else {
                        tipoProd = "R&#xE9;gimen Tributario A";
                    }
                }
                if (dto.getCodigoProducto() == 1061) { //57BICE
                    tipoProd = "57 Bis";
                }
                if (dto.getCodigoProducto() == 1062) { //FLEXIBLE MUTUO
                    tipoProd = "Flexible Mutuo";
                }
                
                    //Generacion dekl detalle
                    xmldetalle02 = xmldetalle02 + 
                    "   <row>" + 
                    "     <producto>" + StringUtil.replaceBadCharsFOPDF(dto.getDescripcionProducto()) + "</producto>" + 
                    "     <poliza>N&#xB0;" + dto.getNumeroProducto() + "</poliza>" + 
                    "     <tipo> "+ StringUtil.replaceBadCharsFOPDF(tipoProd) +"</tipo> " + 
                    "     <periodocobertura>" + FechaUtil.getFechaFormateoCustom(dto.getFechaVencimiento(),"MMMM yyyy") + "</periodocobertura>" + 
                    "     <montopagado>" + NumeroUtil.formatMilesNoDecimal(dto.getTotalCuota()) + "</montopagado>" + 
                    "   </row>" ;              
                   
                   if(dto.getDistribucionFondos() != null && dto.getDistribucionFondos().size() > 0){
                        xmldistribucion01 = "" +
                        " <distribucion visible=\"true\">";
                        
                        // cabecera detalle poliza
                        String xmlpoliza = "" +
                        " <poliza>" +
                        "   <producto>" + dto.getDescripcionProducto() + "</producto>" +
                        "   <nropoliza>" + dto.getNumeroProducto() + "</nropoliza>";
                        
                        //detalle de la distribucion de fondos
                        String xmldetalledist01 = "" +
                        " <detalle>";
                        String xmldetalledist02 = "";                   
                        for(int i=0; i<dto.getDistribucionFondos().size(); i++ ){
                            BpiDitribFondosAPVTbl detalledist = dto.getDistribucionFondos().get(i); 
                            if(detalledist.getMonto() > 0.0){
                                xmldetalledist02 = xmldetalledist02 + "" +
                                "   <row>" +
                                "       <colorfondo>" + detalledist.getColor().intValue() + "</colorfondo>" +
                                "       <nombrefondo>" + detalledist.getNombreFondo() + "</nombrefondo>" +
                                "       <montofondo>" + NumeroUtil.formatMilesNoDecimal(detalledist.getMonto()) +  "</montofondo>" +
                                "       <porcentajefondo>" + detalledist.getPorcentaje().intValue() + "</porcentajefondo>" +
                                "   </row>";
                            }
                            
                        }                    
                        
                        String xmltotaldist = "<total>" + NumeroUtil.formatMilesNoDecimal(dto.getTotalCuota()) + "</total>";
                        xmldistribucion02 = xmldistribucion02 + xmlpoliza + xmldetalledist01 + xmldetalledist02 + "</detalle>" + xmltotaldist + "</poliza>";
                   }
                   
                
            }
            
            String xmldetalle = xmldetalle01 + xmldetalle02 + " </detalle>";
            String xmldistribucion = xmldistribucion01 + xmldistribucion02 + "</distribucion>";
            
            //CARGO SI ES PAGO WEBPAY
            String xmlcargo = "<cargo visible=\"false\">";
            if(getIdBancoPago() == 11){
                String pocentcobro = String.valueOf(getPorcentajeCobroTarjeta());
                pocentcobro = StringUtil.replaceString(pocentcobro, ".", ",");
                xmlcargo = "" +
                " <cargo visible=\"true\">" +
                "   <total>" + NumeroUtil.formatMilesNoDecimal(getCargo()) + "</total>" +
                "   <mensaje>(*) Toda Operaci&#xF3;n con Tarjeta de Cr&#xE9;dito tiene un cargo de un " + pocentcobro + "%</mensaje>";            
            }
            
            xmlcargo= xmlcargo + "" +
            " </cargo>";
              
            //FOOTER                   
            String xmlfooter01 = 
            " <footer>" + 
            "   <fechavencimiento>" + FechaUtil.getFechaFormateoCustom(getUltimoVencimientoDeuda(),"MMMM yyyy") + "</fechavencimiento>" + 
            "   <totalapagar>" + NumeroUtil.formatMilesNoDecimal((getTotalPagar() + getCargo() )) + "</totalapagar>";
            String xmlfooter02 = "";
            if ( getResultadoPagoBanco() != null) {
                xmlfooter02 = xmlfooter02 +
                "   <numerooperacion>" + getResultadoPagoBanco().getNumeroTransaccionBanco() + "</numerooperacion>" + 
                "   <numerocuotas>" + getResultadoPagoBanco().getNumeroCuotasBanco() + "</numerocuotas>" + 
                "   <codigoautorizacion>" + getResultadoPagoBanco().getCodigoAutorizacion() + "</codigoautorizacion>" + 
                "   <numeroordencompra>" + getResultadoPagoBanco().getNumeroOrdenCompra() + "</numeroordencompra>" + 
                "   <numerotarjeta>" + getResultadoPagoBanco().getNumeroTarjeta() + "</numerotarjeta>" + 
                "   <fechabanco>" + getResultadoPagoBanco().getFechaBanco() + "</fechabanco>" + 
                "   <tipocuota>" + StringUtil.replaceBadCharsFOPDF(getResultadoPagoBanco().getTipoCuotas()) + "</tipocuota>" + 
                "   <moneda>" + getResultadoPagoBanco().getMonedaPago() + "</moneda>" + 
                "   <horabanco>" + getResultadoPagoBanco().getHoraBanco() + "</horabanco>";
            }
            
            String xmlfooter = xmlfooter01 + xmlfooter02 + " </footer></comprobante>";     
            xml = xmlcabecera + xmldetalle + xmlcargo + xmldistribucion + xmlfooter;  
            
            xml = xml.replaceAll("null", "-");
        }else{
        
            if (getDatosCliente() != null) {
                xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>"+
                "<comprobante>" + 
                " <header>" + 
                "    <formadepago>" + getFormaPago() + "</formadepago>" + 
                "    <numerotransaccion>" + getIdTransaccion() + "</numerotransaccion>" + 
                "    <fechaoperacion>" + FechaUtil.getFechaFormateoStandar(getFechaTransaccion()) + "</fechaoperacion>" + 
                "    <nombre>" +  StringUtil.replaceBadCharsFOPDF(getDatosCliente().getNombreCliente()) + "</nombre>" + 
                "    <rut>" + NumeroUtil.formatMilesNoDecimal(getDatosCliente().getRutCliente()) + "-" + RutUtil.calculaDv(Integer.toString(getDatosCliente().getRutCliente())) + "</rut>" + 
                "    <direccion>" + StringUtil.replaceBadCharsFOPDF(getDatosCliente().getDireccion()) + "</direccion>" + 
                "    <comuna>" + StringUtil.replaceBadCharsFOPDF(getDatosCliente().getComuna()) + "</comuna>" + 
                "    <ciudad>" + StringUtil.replaceBadCharsFOPDF(getDatosCliente().getRegion()) + "</ciudad>" + 
                " </header>" +
                " <detalle>";
            } else {
                xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>"+
                "<comprobante>" + 
                " <header>" + 
                "    <formadepago>" + getFormaPago() + "</formadepago>" + 
                "    <numerotransaccion>" + getIdTransaccion() + "</numerotransaccion>" + 
                "    <fechaoperacion>" + FechaUtil.getFechaFormateoStandar(getFechaTransaccion()) + "</fechaoperacion>" + 
                "    <nombre>null</nombre>" + 
                "    <rut>null</rut>" + 
                "    <direccion>null</direccion>" + 
                "    <comuna>null</comuna>" + 
                "    <ciudad>null</ciudad>" + 
                " </header>" +
                " <detalle>";
            }
            
            //Iteracion
            Iterator items = getCuotas().iterator();
            while (items.hasNext()) {
                PagoDetalleCuotaVO dto = (PagoDetalleCuotaVO) items.next();
                xml = xml + 
                "   <row>" + 
                "     <producto>" + StringUtil.replaceBadCharsFOPDF(dto.getDescripcionProducto()) + "</producto>" + 
                "     <tipo>R&#xE9;gimen "+ StringUtil.replaceBadCharsFOPDF(dto.getTipoProducto()) +"</tipo> " + 
                "     <numeropoliza>N&#xB0;" + dto.getNumeroProducto() + "</numeropoliza>" +                
                "     <periodocobertura>" + FechaUtil.getFechaFormateoCustom(dto.getFechaVencimiento(),"MMMM yyyy") + "</periodocobertura>" + 
                "     <primapagouf>"+ NumeroUtil.formatMilesSiDecimal(dto.getValorEnUF()) +"</primapagouf> " + 
                "     <primapagopesos>"+ NumeroUtil.formatMilesNoDecimal(dto.getValorEnPesos()) +"</primapagopesos> " + 
                "     <saldofavor>0</saldofavor>" + 
                "     <montopagado>" + NumeroUtil.formatMilesNoDecimal(dto.getTotalCuota()) + "</montopagado>" + 
                "   </row>" ;
            }
            
            xml = xml +
            " </detalle>" + 
            " <footer>" + 
            "   <fechavencimiento>" + FechaUtil.getFechaFormateoCustom(getUltimoVencimientoDeuda(),"MMMM yyyy") + "</fechavencimiento>" + 
            "   <totalapagar>" + NumeroUtil.formatMilesNoDecimal(getTotalPagar()) + "</totalapagar>";
            
            if ( getResultadoPagoBanco() != null) {
                xml = xml +
                "   <numerooperacion>" + getResultadoPagoBanco().getNumeroTransaccionBanco() + "</numerooperacion>" + 
                "   <numerocuotas>" + getResultadoPagoBanco().getNumeroCuotasBanco() + "</numerocuotas>" + 
                "   <codigoautorizacion>" + getResultadoPagoBanco().getCodigoAutorizacion() + "</codigoautorizacion>" + 
                "   <numeroordencompra>" + getResultadoPagoBanco().getNumeroOrdenCompra() + "</numeroordencompra>" + 
                "   <numerotarjeta>" + getResultadoPagoBanco().getNumeroTarjeta() + "</numerotarjeta>" + 
                "   <fechabanco>" + getResultadoPagoBanco().getFechaBanco() + "</fechabanco>" + 
                "   <tipocuota>" + StringUtil.replaceBadCharsFOPDF(getResultadoPagoBanco().getTipoCuotas()) + "</tipocuota>" + 
                "   <moneda>" + getResultadoPagoBanco().getMonedaPago() + "</moneda>" + 
                "   <horabanco>" + getResultadoPagoBanco().getHoraBanco() + "</horabanco>";
            }
            xml = xml + " </footer></comprobante>";      
            xml = xml.replaceAll("null", "-");
        }
    
        System.out.println(xml);
        return xml;
    }
    
    public void setUltimoVencimientoDeuda(Date ultimoVencimientoDeuda) {
        this.ultimoVencimientoDeuda = ultimoVencimientoDeuda;
    }

    public Date getUltimoVencimientoDeuda() {
        return ultimoVencimientoDeuda;
    }

    public void setEstadoDeuda(String estadoDeuda) {
        this.estadoDeuda = estadoDeuda;
    }

    public String getEstadoDeuda() {
        return estadoDeuda;
    }

    public void setTotalPagar(Double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public Double getTotalPagar() {
        return totalPagar;
    }

    public void setXmloriginal(String xmloriginal) {
        this.xmloriginal = xmloriginal;
    }

    public String getXmloriginal() {
        return xmloriginal;
    }

    public void setXmlpagopublico(String xmlpagopublico) {
        this.xmlpagopublico = xmlpagopublico;
    }

    public String getXmlpagopublico() {
        return xmlpagopublico;
    }

    public void setDatosCliente(PagoDatosClienteVO datosCliente) {
        this.datosCliente = datosCliente;
    }

    public PagoDatosClienteVO getDatosCliente() {
        return datosCliente;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setValorUF(Double valorUF) {
        this.valorUF = valorUF;
    }

    public Double getValorUF() {
        return valorUF;
    }

    public void setCuotas(List cuotas) {
        this.cuotas = cuotas;
    }

    public List getCuotas() {
        return cuotas;
    }
    
    public void addCuotaDetalle(PagoDetalleCuotaVO cuota) {
        cuotas.add(cuota);
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setIdBancoPago(int idBancoPago) {
        this.idBancoPago = idBancoPago;
    }

    public int getIdBancoPago() {
        return idBancoPago;
    }
    
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getFormaPago() {
        return formaPago;
    }   

    public void setUrlPdfDownload(String urlPdfDownload) {
        this.urlPdfDownload = urlPdfDownload;
    }

    public String getUrlPdfDownload() {
        return urlPdfDownload;
    }

    public void setUrlPdfEnEsperaDownload(String urlPdfEnEsperaDownload) {
        this.urlPdfEnEsperaDownload = urlPdfEnEsperaDownload;
    }

    public String getUrlPdfEnEsperaDownload() {
        return urlPdfEnEsperaDownload;
    }

    public void setAbsoluteServerPathPdfDownload(String absoluteServerPathPdfDownload) {
        this.absoluteServerPathPdfDownload = absoluteServerPathPdfDownload;
    }

    public String getAbsoluteServerPathPdfDownload() {
        return absoluteServerPathPdfDownload;
    }

    public void setAbsoluteServerPathPdfEnEsperaDownload(String absoluteServerPathPdfEnEsperaDownload) {
        this.absoluteServerPathPdfEnEsperaDownload = absoluteServerPathPdfEnEsperaDownload;
    }

    public String getAbsoluteServerPathPdfEnEsperaDownload() {
        return absoluteServerPathPdfEnEsperaDownload;
    }

    public void setXmlconfirmacion(String xmlconfirmacion) {
        this.xmlconfirmacion = xmlconfirmacion;
    }

    public String getXmlconfirmacion() {
        return xmlconfirmacion;
    }

    public void setXmlconfirmacionDataBancos(String xmlconfirmacionDataBancos) {
        this.xmlconfirmacionDataBancos = xmlconfirmacionDataBancos;
    }

    public String getXmlconfirmacionDataBancos() {
        return xmlconfirmacionDataBancos;
    }

    public void setBanco(PagoBancoInfoVO banco) {
        this.banco = banco;
    }

    public PagoBancoInfoVO getBanco() {
        return banco;
    }

    public void setPdfUrlVolantes(String pdfUrlVolantes) {
        this.pdfUrlVolantes = pdfUrlVolantes;
    }

    public String getPdfUrlVolantes() {
        return pdfUrlVolantes;
    }

    public void setResultadoPagoBanco(ResultadoConsultaVO resultadoPagoBanco) {
        this.resultadoPagoBanco = resultadoPagoBanco;
    }

    public ResultadoConsultaVO getResultadoPagoBanco() {
        return resultadoPagoBanco;
    }

    public void setTipoTransaccion(int tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public int getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setCargo(Double cargo) {
        this.cargo = cargo;
    }

    public Double getCargo() {
        return cargo;
    }

    public void setPorcentajeCobroTarjeta(double porcentajeCobroTarjeta) {
        this.porcentajeCobroTarjeta = porcentajeCobroTarjeta;
    }

    public double getPorcentajeCobroTarjeta() {
        return porcentajeCobroTarjeta;
    }
}
