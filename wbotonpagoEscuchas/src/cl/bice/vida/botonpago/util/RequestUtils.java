package cl.bice.vida.botonpago.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class RequestUtils {

    private static final Logger LOGGER = Logger.getLogger(RequestUtils.class);

    private RequestUtils() {

    }


    public static void printRequest(HttpServletRequest request, String nombreObjecto) {
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            LOGGER.info(nombreObjecto + "Nombre Parametro [" + paramName + "], Valor [" +request.getParameter(paramName) + "]");
        }
    }
}
