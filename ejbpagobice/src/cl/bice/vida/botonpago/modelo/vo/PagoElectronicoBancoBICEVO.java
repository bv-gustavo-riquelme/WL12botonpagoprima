package cl.bice.vida.botonpago.modelo.vo;

import cl.bice.vida.botonpago.common.util.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PagoElectronicoBancoBICEVO {
    private String idTransaccion;
    private String idCliente;
    private Double montoTotal;
    private List<ItemPagoElectronicoBancoBICEVO> detalle;
    public PagoElectronicoBancoBICEVO() {
    }
    
    public void addItem(ItemPagoElectronicoBancoBICEVO item) {
        if (detalle == null) detalle = new ArrayList<ItemPagoElectronicoBancoBICEVO>();
        detalle.add(item);
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setDetalle(List<ItemPagoElectronicoBancoBICEVO> detalle) {
        this.detalle = detalle;
    }

    public List<ItemPagoElectronicoBancoBICEVO> getDetalle() {
        return detalle;
    }
    
    public String toXml() {
        String valores = "<BancoBICE>" +
                          "<IdConvenio>" + this.getIdCliente() + "</IdConvenio>" +
                          "<IdTransaccion>" + this.getIdTransaccion() + "</IdTransaccion>" +
                          "<MontoTotal>" + this.getMontoTotal().toString() + "</MontoTotal>" +
                          "<detalle>";
        Iterator itm = detalle.iterator();
        while (itm.hasNext()) {
           ItemPagoElectronicoBancoBICEVO dto = (ItemPagoElectronicoBancoBICEVO) itm.next();
           valores+="<item>" +
                        "<cantidad>" + dto.getCantidad() + "</cantidad>" +
                        "<descripcion>" + StringUtil.quitarAcentos(dto.getDescripcion())  + "</descripcion>" +
                        "<monto>" + dto.getMonto().toString() + "</monto>" +
                      "</item>";
        }
                          
         valores+="</detalle>" +
                "</BancoBICE>";
                
        System.out.println(valores);
        return valores;
    }
}
