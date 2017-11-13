package cl.bice.vida.botonpago.modelo.dto;

public class PersonaDto {
    private String ciudad;
    private String comuna;
    private String calle;
    private String region;
    private String email;
    private String nombre;

    public PersonaDto() {
        super();
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getComuna() {
        return comuna;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCalle() {
        return calle;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}
