package cl.bice.vida.botonpago.modelo.vo;

public class ItemPagoElectronicoBancoBICEVO {
    private Long cantidad;
    private String descripcion;
    private Double monto;
    public ItemPagoElectronicoBancoBICEVO() {
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getMonto() {
        return monto;
    }
}
