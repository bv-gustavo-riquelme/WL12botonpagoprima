package cl.bice.vida.botonpago.modelo.rest;

public class RESTfulParametroDto {
	private String nombre;
	private String valor;
	
	
	
	public RESTfulParametroDto(String nombre, String valor) {
		super();
		this.nombre = nombre;
		this.valor = valor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
}
