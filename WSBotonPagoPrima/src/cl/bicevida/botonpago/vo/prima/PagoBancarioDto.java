package cl.bicevida.botonpago.vo.prima;

/**
 * Clase DTO para pago bancario
 * @author rhicil
 *
 */
public class PagoBancarioDto {
    
    private String pathBantario;
    private String mensaje; 
    private String pageOut;
    private String idcomprobante;
    private String urlvolver;
    private String enviarCorreo;
    private String contactCenter;
    private String[] idcomprobantes;
    private Boolean mostrarbotonvolver;
    
	public String getUrlvolver() {
		return urlvolver;
	}
	public void setUrlvolver(String urlvolver) {
		this.urlvolver = urlvolver;
	}
	public String getIdcomprobante() {
		return idcomprobante;
	}
	public void setIdcomprobante(String idcomprobante) {
		this.idcomprobante = idcomprobante;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getPathBantario() {
		return pathBantario;
	}
	public void setPathBantario(String pathBantario) {
		this.pathBantario = pathBantario;
	}
	
	public String getPageOut() {
		return pageOut;
	}
	public void setPageOut(String pageOut) {
		this.pageOut = pageOut;
	}
	public String getEnviarCorreo() {
		return enviarCorreo;
	}
	public void setEnviarCorreo(String enviarCorreo) {
		this.enviarCorreo = enviarCorreo;
	}
	public String[] getIdcomprobantes() {
		return idcomprobantes;
	}
	public void setIdcomprobantes(String[] idcomprobantes) {
		this.idcomprobantes = idcomprobantes;
	}
	public String getContactCenter() {
		return contactCenter;
	}
	public void setContactCenter(String contactCenter) {
		this.contactCenter = contactCenter;
	}
	public Boolean isMostrarbotonvolver() {
		return mostrarbotonvolver;
	}
	public void setMostrarbotonvolver(Boolean mostrarbotonvolver) {
		this.mostrarbotonvolver = mostrarbotonvolver;
	}
	
}
