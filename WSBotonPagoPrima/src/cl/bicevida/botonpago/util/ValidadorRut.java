package cl.bicevida.botonpago.util;

import org.apache.log4j.Logger;

/*import cl.bice.vida.botonpago.util.RutUtil;

import cl.bice.vida.botonpago.util.FacesUtil;

import cl.bice.vida.botonpago.util.StringUtil;*/


import java.util.StringTokenizer;




public class ValidadorRut  {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ValidadorRut.class);
    
    private String noformato;
    private String noingresado;
    private String novalido;
    
    /**
     * Constructor incial
     */
    public ValidadorRut() {
    }

    
    
    /**
     * Valida RUT
     * @param valor
     */
    public void validaRut(String valor) throws  Exception {
        if (valor != null || valor.length() > 0) {
            StringTokenizer token = new StringTokenizer(valor, "-");
            if (token.countTokens() == 2) {
                String primeraParte = token.nextToken();
                primeraParte = StringUtil.replaceString(primeraParte,".","");
                primeraParte = StringUtil.replaceString(primeraParte,",","");
                String digito = token.nextToken();

                try {
                    int prueba = Integer.parseInt(primeraParte);
                } catch (Exception e) {
                    noformato = ResourceMessageUtil.getProperty("jsf.converter.convertidorrut.noformato");        
                    throw new Exception(noformato);
                }
        
                //Valida
                String validador = RutUtil.calculaDv(primeraParte);
                if (!validador.equalsIgnoreCase(digito)) {
                    novalido = ResourceMessageUtil.getProperty("jsf.converter.convertidorrut.novalidorut");
                    throw new Exception(novalido);
                }
            } else {
                noformato = ResourceMessageUtil.getProperty("jsf.converter.convertidorrut.noformato");        
                throw new Exception(noformato);
            }
        } else {
            noingresado = ResourceMessageUtil.getProperty("jsf.converter.convertidorrut.noingresado");
            
            throw new Exception(noingresado);
        }
    }    
}
