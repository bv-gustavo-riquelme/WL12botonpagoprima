package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

public class ResumenRequest implements Serializable {
    Integer[] productosSeleccionados;
    Integer[] opciones;
    Integer[] totales;
    int total_pagar;
    int banco;
    
    public ResumenRequest() {
    }

    public void setProductosSeleccionados(Integer[] productosSeleccionados) {
        this.productosSeleccionados = productosSeleccionados;
    }

    public Integer[] getProductosSeleccionados() {
        return productosSeleccionados;
    }
    
    public int getProductosSeleccionado(int position) {
        return productosSeleccionados[position].intValue();
    }

    public void setBanco(int banco) {
        this.banco = banco;
    }

    public int getBanco() {
        return banco;
    }

    public void setOpciones(Integer[] opciones) {
        this.opciones = opciones;
    }

    public Integer[] getOpciones() {
        return opciones;
    }
    
    public int getOpcion(int position) {
        return opciones[position].intValue();
    }
    
    public void setTotales(Integer[] totales) {
        this.totales = totales;
    }

    public Integer[] getTotales() {
        return totales;
    }
    
    public int getTotal(int position) {
        return totales[position].intValue();
    }
    
    public void setTotal_pagar(int total_pagar) {
        this.total_pagar = total_pagar;
    }

    public int getTotal_pagar() {
        return total_pagar;
    }
}
