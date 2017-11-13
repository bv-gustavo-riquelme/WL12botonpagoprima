package cl.bice.vida.botonpago.modelo.dao;

import java.util.Date;
import java.util.List;

import noNamespace.CartolaDocument;

import org.apache.xmlbeans.XmlObject;


public interface LogicaCartolasDAO {
    public String crearCartola(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin,int tipo);
    public String crearCartolaPoliza(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin,int tipo);
    public String crearCartolaDividendo(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin,int tipo);
    public void validateXML(XmlObject xml) throws Exception;
    public CartolaDocument generaXmlCartola(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin,int tipo) throws Exception;
    public CartolaDocument generaXmlCartolaPoliza(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin,int tipo) throws Exception;
    public CartolaDocument generaXmlCartolaDividendo(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin,int tipo) throws Exception;
    public void setIdentificacionMensaje(CartolaDocument.Cartola newCartola,int tipo);
    public void setProductosPagados(CartolaDocument.Cartola newCartola, int rut,Date fechaini,Date fechafin);
    public void setProductosPagadosPoliza(CartolaDocument.Cartola newCartola, int rut,Date fechaini,Date fechafin);
    public void setProductosPagadosDividendo(CartolaDocument.Cartola newCartola, int rut,Date fechaini,Date fechafin);
    public void SetPolizas(CartolaDocument.Cartola.ProductosPagados pp, List polizas);
    public void setDividendos(CartolaDocument.Cartola.ProductosPagados pp, List dividendos);
}
