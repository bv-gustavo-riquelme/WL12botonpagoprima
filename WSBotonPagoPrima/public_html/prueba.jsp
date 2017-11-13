<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="application/json; charset=UTF-8"/>
        <title>prueba</title>
         </head>
    <body>
        <!-- WEBPAY-->
        <!--<form id="formularioEnvio" target="_self" action="http://aplicaciones-desa.bicevida.cl/TbnkBicevida/cgi-bin/tbk_bp_pago.cgi" method="POST">
                    
                <input type="hidden" name="TBK_ID_SESION" value="1A530637289A03B07199A44E8D5314271A530637289A03B07199A44E8D531427"  />
                <input type="hidden" name="TBK_MONTO" value="268350000"/> 
                <input type="hidden" name="TBK_TIPO_TRANSACCION" value="TR_NORMAL" />
                <input type="hidden" name="TBK_ORDEN_COMPRA" value="01749313001" />
                <input type="hidden" name="TBK_URL_EXITO" value="untitled1.jsp"  /> 
                <input type="hidden" name="TBK_URL_FRACASO" value="untitled1.jsp" /> -->    
                    
        <!--BANCO DE CHILE-->
        <form id="formularioEnvio"  action="http://aplicaciones-desa.bicevida.cl/bp12/webpagobicepublico/cambiocontextobchile" method="POST">
        <!--<form id="formularioEnvio"  action="http://aplicaciones-desa.bicevida.cl/bchBicevida/cgi-bin/cgicomer" method="POST">-->
            <!--<MPINI><IDCOM>2966564105</IDCOM><IDTRX>2966564105749339</IDTRX><TOTAL>26835</TOTAL><NROPAGOS>1</NROPAGOS><DETALLE><SRVREC>005218</SRVREC><MONTO>26835</MONTO><GLOSA>Seguro de Vida Individual</GLOSA><CANTIDAD>0001</CANTIDAD><PRECIO>26835</PRECIO><DATOADIC>2966564105749339072109652966564105</DATOADIC></DETALLE></MPINI>-->

            <input type="hidden" name="URLCGI" value="http://aplicaciones-desa.bicevida.cl/bchBicevida/cgi-bin/cgicomer" />
            <input type="hidden" name="data" value="<MPINI><IDCOM>2966564105</IDCOM><IDTRX>2966564105749370</IDTRX><TOTAL>26835</TOTAL><NROPAGOS>1</NROPAGOS><DETALLE><SRVREC>005218</SRVREC><MONTO>26835</MONTO><GLOSA>Seguro de Vida Individual</GLOSA><CANTIDAD>0001</CANTIDAD><PRECIO>26835</PRECIO><DATOADIC>2966564105749370072109652966564105</DATOADIC></DETALLE></MPINI>"/>
            <!--<input type="hidden" name="data" value="<![CDATA[<MPINI><IDCOM>2966564105</IDCOM><IDTRX>2966564105749339</IDTRX><TOTAL>26835</TOTAL><NROPAGOS>1</NROPAGOS><DETALLE><SRVREC>005218</SRVREC><MONTO>26835</MONTO><GLOSA>Seguro de Vida Individual</GLOSA><CANTIDAD>0001</CANTIDAD><PRECIO>26835</PRECIO><DATOADIC>2966564105749339072109652966564105</DATOADIC></DETALLE></MPINI>]]>"/>-->
            <input type="submit" value="enviar"  />
        </form>
    
    </body>
</html>