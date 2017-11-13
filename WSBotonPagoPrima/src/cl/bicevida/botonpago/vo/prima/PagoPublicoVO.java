package cl.bicevida.botonpago.vo.prima;

import cl.bice.vida.botonpago.common.vo.PagoBancoInfoVO;
import cl.bice.vida.botonpago.common.vo.ResultadoConsultaVO;
import cl.bicevida.botonpago.util.RutUtil;
import cl.bicevida.botonpago.util.FechaUtil;

import cl.bicevida.botonpago.util.NumeroUtil;

import cl.bicevida.botonpago.util.StringUtil;

import cl.bicevida.botonpago.vo.out.OutDatosPdf;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class PagoPublicoVO implements Serializable {
    private int idTransaccion;
    private int idNavegacion;
    private Date fechaTransaccion;
    private String xmloriginal;
    private String xmlpagopublico;
    private String xmlconfirmacion;
    private String xmlconfirmacionDataBancos;
    private PagoBancoInfoVo banco = new PagoBancoInfoVo();
    private Date   ultimoVencimientoDeuda;
    private String estadoDeuda = "";//"AL DIA";
    private Double totalPagar;
    private Double valorUF;
    private PagoDatosClienteVO datosCliente;
    private List cuotas;
    private Hashtable hashcuotas = new Hashtable();
    private LinkedHashMap hashcuotasl = new LinkedHashMap();
    private int idBancoPago;
    private String urlPdfDownload;
    private String urlPdfEnEsperaDownload;
    private String absoluteServerPathPdfDownload;
    private String absoluteServerPathPdfEnEsperaDownload;    
    private String formaPago;
    private String pdfUrlVolantes = null;
    private ResultadoConsultaVO resultadoPagoBanco;
    private int tipoTransaccion;
    
    /*****************************************************************
     * GR - Atributos utilizados para el nuevo servicio web(REST)
     * */
    private boolean estado;
    private String mensajeRespuesta;
    private Integer codRespuesta;
    private String fechaAccesoUsuario;
    private int medioPago;
    private List<OutDatosPdf> pdfPolizas;
    private OutDatosPdf pdfConsolidado;
    private String tbkURLExitoError;//para el medio de pago 4,9,10 (webpay,BCI)
    private String urlcgi;
    private Integer bancoId;
    private String methodSend;
    private String accion;
    private List<PagoDetalleCuotaVO> hashList = new ArrayList<PagoDetalleCuotaVO>();
    /****************************************************************/
        
        
        
    
    /**
     * Constructor inicial
     */
    public PagoPublicoVO() {
        cuotas = new ArrayList();
        resultadoPagoBanco = new ResultadoConsultaVO();
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
    
    public void addCuotaDetalleHash(Integer idcuota, PagoDetalleCuotaVO cuota) {
        hashcuotas.put(idcuota, cuota);
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

    public void setBanco(PagoBancoInfoVo banco) {
        this.banco = banco;
    }

    public PagoBancoInfoVo getBanco() {
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

    public void setIdNavegacion(int idNavegacion) {
        this.idNavegacion = idNavegacion;
    }

    public int getIdNavegacion() {
        return idNavegacion;
    }
   
    public void setHashcuotas(Hashtable hashcuotas) {
        this.hashcuotas = hashcuotas;
    }

    public Hashtable getHashcuotas() {
        return hashcuotas;
    }

    public void setTipoTransaccion(int tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public int getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setCodRespuesta(Integer codRespuesta) {
        this.codRespuesta = codRespuesta;
    }

    public Integer getCodRespuesta() {
        return codRespuesta;
    }

    public void setFechaAccesoUsuario(String fechaAccesoUsuario) {
        this.fechaAccesoUsuario = fechaAccesoUsuario;
    }

    public String getFechaAccesoUsuario() {
        return fechaAccesoUsuario;
    }
    public void setMedioPago(int medioPago) {
        this.medioPago = medioPago;
    }

    public int getMedioPago() {
        return medioPago;
    }
    
    public void setTbkURLExitoError(String tbkURLExitoError) {
        this.tbkURLExitoError = tbkURLExitoError;
    }

    public String getTbkURLExitoError() {
        return tbkURLExitoError;
    }

    public void setUrlcgi(String urlcgi) {
        this.urlcgi = urlcgi;
    }

    public String getUrlcgi() {
        return urlcgi;
    }

    public void setBancoId(Integer bancoId) {
        this.bancoId = bancoId;
    }

    public Integer getBancoId() {
        return bancoId;
    }

    public void setMethodSend(String methodSend) {
        this.methodSend = methodSend;
    }

    public String getMethodSend() {
        return methodSend;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getAccion() {
        return accion;
    }

    public void setPdfPolizas(List<OutDatosPdf> pdfPolizas) {
        this.pdfPolizas = pdfPolizas;
    }

    public List<OutDatosPdf> getPdfPolizas() {
        return pdfPolizas;
    }

    public void setPdfConsolidado(OutDatosPdf pdfConsolidado) {
        this.pdfConsolidado = pdfConsolidado;
    }

    public OutDatosPdf getPdfConsolidado() {
        return pdfConsolidado;
    }

    public void setHashList(List<PagoDetalleCuotaVO> hashList) {
        this.hashList = hashList;
    }

    public List<PagoDetalleCuotaVO> getHashList() {
        return hashList;
    }

    public void setHashcuotasl(LinkedHashMap hashcuotasl) {
        this.hashcuotasl = hashcuotasl;
    }

    public LinkedHashMap getHashcuotasl() {
        return hashcuotasl;
    }

}
