package cl.bicevida.botonpago.util;

import org.apache.log4j.Logger;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Clase de utilidad para manejo de objetos de jsf
 * @author rhicil
 *
 */
public class FacesUtil {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(FacesUtil.class);

    /*
     * Constantes
     */
    private static final String X_FORWARDED_HOST = "x-forwarded-host";
    private static final String VH_HEADER = "nuxeo-virtual-host";


    /**
     * Setter
     *
     * @param nameMBean
     * @param mBean
     */
    public static void putMBeanInRequest(String nameMBean, Object mBean) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getRequestMap().put(nameMBean, mBean);
    }

    /**
	 * Getter
	 * @param nameMBean
	 * @return Object
	 */
    public static Object getMBeanInRequest(String nameMBean) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object returnObject = facesContext.getExternalContext().getRequestMap().get(nameMBean);
        return returnObject;
    }

    /**
	 * Recupera un Valor de ADF en Bean, solo a partier del nombre del bean
	 *
	 * @param el
	 * @return Object
	 */
    public static Object getValueBindingEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{" + el + "}");
        return valueBinding;
    }

    /**
     * get the value binding
     * 
     * @param bindingName
     * @return
     */
    private static ValueBinding getValueBinding(String bindingName) {
        ValueBinding returnValueBinding = getApplication().createValueBinding(bindingName);
        return returnValueBinding;
    }

    /**
     * get the application
     * 
     * @return the application
     */
    private static Application getApplication() {
        ApplicationFactory appFactory = getApplicationFactory();
        Application returnApplication = appFactory.getApplication();
        return returnApplication;
    }

    /**
     * Get the application factory
     * 
     * @return
     */
    public static ApplicationFactory getApplicationFactory() {
        ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        return appFactory;
    }

    public static void setLocale(Locale locale) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(java.util.Locale.ENGLISH);
    }

    public static Locale getLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale returnLocale = context.getViewRoot().getLocale();
        return returnLocale;
    }


    /**
     * Crea un ValueBinding a partir de un atributo de un bean dado, solo recibiendo como argumento el valor interno
     * @param el
     * @return ValueBinding
     */
    public static ValueBinding createValueBindingEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding("#{" + el + "}");
        return valueBinding;
    }

    /**
	 * Crea un ValueBinding a partir de un atributo de un bean dado
	 * @param el
	 * @return ValueBinding
	 */
    public static ValueBinding createValueBinding(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding(el);
        return valueBinding;
    }

    /**
	 * Setea un mensaje de error en jsf
	 * @param mensaje
	 */
    public static void setError(String mensaje) {
        FacesUtil.setMessage(mensaje, FacesMessage.SEVERITY_ERROR);
    }

    /**
	 * Setea un mensaje de informacion en jsf
	 * @param mensaje
	 */
    public static void setInformacion(String mensaje) {
        FacesUtil.setMessage(mensaje, FacesMessage.SEVERITY_INFO);
    }

    /**
	 * Setea un mensaje de advertencia en jsf
	 * @param mensaje
	 */
    public static void setAdvertencia(String mensaje) {
        FacesUtil.setMessage(mensaje, FacesMessage.SEVERITY_WARN);
    }

    /**
	 * Setea un mensaje jsf
	 * @param mensaje
	 * @param tipo
	 */
    private static void setMessage(String mensaje, FacesMessage.Severity tipo) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(mensaje);
        msg.setSeverity(tipo);
        ctx.addMessage(null, msg);
    }


    /**
	 * Setea un atributo en sesi�n
	 *
	 * @param nombre
	 * @param valor
	 */
    public static void setAttributeInSession(String nombre, String valor) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)ctx.getExternalContext().getRequest();
        request.getSession().setAttribute(nombre, valor);
    }

    /**
	 * Setea un atributo en sesi�n
	 *
	 * @param nombre
	 * @param valor
	 */
    public static void setAttributeInSessionObject(String nombre, Object valor) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)ctx.getExternalContext().getRequest();
        request.getSession().setAttribute(nombre, valor);
    }

    /**
	 * Recupera un atributo en sesi�n
	 *
	 * @param nombre
	 */
    public static String getAttributeInSession(String nombre) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)ctx.getExternalContext().getRequest();
        String returnString = (String) request.getSession().getAttribute(nombre);
        return returnString;
    }

    public static Object getAttributeInSessionObject(String nombre) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)ctx.getExternalContext().getRequest();
        Object returnObject = request.getSession().getAttribute(nombre);
        return returnObject;
    }

    /**
	 * Elimina un atributo de session
	 *
	 * @param nombre
	 */
    public static void removeAttributeInSession(String nombre) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)ctx.getExternalContext().getRequest();
        request.getSession().removeAttribute(nombre);
    }


    /**
     * get resource message
     *
     * @param msg
     * @return
     */
    public static String getMessage(String msg) {
        String returnString = getMessage(msg, new Object[] {});
        return returnString;
    }


    /**
     * get resource message
     *
     * @param msg
     * @param params
     * @return
     */
    public static String getMessage(String msg, Object[] params) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        String bundle = application.getMessageBundle();
        String returnString = getMessage(bundle, msg, params);
        return returnString;
    }

    /**
     * get resource message
     *
     * @param bundle
     * @param msg
     * @param params
     * @return
     */
    public static String getMessage(String bundle, String msg, Object[] params) {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = context.getViewRoot().getLocale();
        String returnString = getMessage(bundle, locale, msg, params);
        return returnString;
    }


    /**
     * get resource message
     *
     * @param bundle
     * @param locale
     * @param msg
     * @param params
     * @return
     */
    public static String getMessage(String bundle, Locale locale, String msg, Object[] params) {
        String result = msg;
        try {
            ResourceBundle rb = ResourceBundle.getBundle(bundle, locale);
            String msgPattern = rb.getString(msg);
            result = msgPattern;

            result = MessageFormat.format(msgPattern, params);
        } catch (Exception e) {
            result = msg;
        }
        return result;
    }

    /**
     * Add information message.
     *
     * @param msg
     *            the information message
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    /**
     * Add information message.
     *
     * @param msg
     * @param useBundle
     */
    public static void addInfoMessage(String msg, boolean useBundle) {
        addInfoMessage(msg, useBundle, null);
    }

    /**
     * Add information message.
     *
     * @param msg
     * @param useBundle
     * @param prams
     */
    public static void addInfoMessage(String msg, boolean useBundle, Object[] prams) {
        addInfoMessage(null, msg, useBundle, prams);
    }

    /**
     * Add information message to a sepcific client.
     *
     * @param clientId
     * @param msg
     */
    public static void addInfoMessage(String clientId, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    /**
     * Add information message to a sepcific client.
     *
     * @param clientId
     * @param msg
     * @param useBundle
     * @param params
     */
    public static void addInfoMessage(String clientId, String msg, boolean useBundle, Object[] params) {
        FacesContext context = FacesContext.getCurrentInstance();
        String msgs = msg;

        if (useBundle) {
            Application application = context.getApplication();
            String messageBundleName = application.getMessageBundle();
            Locale locale = context.getViewRoot().getLocale();
            ResourceBundle rb = 
                ResourceBundle.getBundle(messageBundleName, locale);
            String msgPattern = rb.getString(msg);
            msgs = msgPattern;

            if (params != null && params.length > 0) {
                msgs = MessageFormat.format(msgPattern, params);
            }
        }

        context.addMessage(clientId, 
                           new FacesMessage(FacesMessage.SEVERITY_INFO, msgs, 
                                            msgs));
    }

    /**
     * Add error message.
     *
     * @param msg
     *            the error message
     */
    public static FacesMessage addErrorMessage(String msg) {
        FacesMessage returnFacesMessage = addErrorMessage(msg, false);
        return returnFacesMessage;
    }

    /**
             * add error message
             *
             *
             * @param msg
             * @param param
             * @return
             */
    public static FacesMessage addErrorMessage(String msg, Object param) {
        FacesMessage returnFacesMessage = addErrorMessageById(null, msg, param);
        return returnFacesMessage;
    }

    /**
             * add error message
             *
             * @param msg
             * @param params
             * @return
             */
    public static FacesMessage addErrorMessage(String msg, Object[] params) {
        FacesMessage returnFacesMessage = addErrorMessageById(null, msg, params);
        return returnFacesMessage;
    }

    /**
             * add error message
             *
             *
             * @param msg
             * @param useBundle
             * @return
             */
    public static FacesMessage addErrorMessage(String msg, boolean useBundle) {
        FacesMessage returnFacesMessage = addErrorMessage(msg, useBundle, null);
        return returnFacesMessage;
    }

    /**
     * add error message
     *
     * @param msg
     * @param useBundle
     * @param param
     * @return
     */
    public static FacesMessage addErrorMessage(String msg, boolean useBundle, Object param) {
        if (param != null) {
            FacesMessage returnFacesMessage = addErrorMessage(msg, useBundle, null);
            return returnFacesMessage;
        } else {
            FacesMessage returnFacesMessage = addErrorMessage(msg, useBundle, new Object[] { param });
            return returnFacesMessage;
        }
    }

    /**
     * add error message
     *
     * @param msg
     * @param useBundle
     * @param params
     * @return
     */
    public static FacesMessage addErrorMessage(String msg, boolean useBundle, 
                                               Object[] params) {
        FacesMessage returnFacesMessage = addErrorMessageById(null, msg, useBundle, params);
        return returnFacesMessage;
    }

    /**
     * Add error message to a sepcific client.
     *
     * @param clientId
     *            the client id
     * @param msg
     *            the error message
     */
    public static FacesMessage addErrorMessageById(String clientId, String msg) {
        FacesMessage fMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(clientId, fMsg);
        return fMsg;
    }

    /**
     * add error message
     *
     * @param clientId
     * @param msg
     * @param useBundle
     * @param params
     * @return
     */
    public static FacesMessage addErrorMessageById(String clientId, String msg, boolean useBundle, Object[] params) {
        FacesContext context = FacesContext.getCurrentInstance();
        String msgs = msg;
        if (useBundle) {
            Application application = context.getApplication();
            String messageBundleName = application.getMessageBundle();
            Locale locale = context.getViewRoot().getLocale();
            ResourceBundle rb = 
                ResourceBundle.getBundle(messageBundleName, locale);
            String msgPattern = rb.getString(msg);
            msgs = msgPattern;

            if (params != null && params.length > 0) {
                msgs = MessageFormat.format(msgPattern, params);
            }
        }

        FacesMessage fMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msgs, msgs);
        context.addMessage(clientId, fMsg);
        return fMsg;
    }

    /**
     * add error message
     *
     * @param clientId
     * @param msg
     * @param param
     * @return
     */
    public static FacesMessage addErrorMessageById(String clientId, String msg, Object param) {
        FacesMessage returnFacesMessage = addErrorMessageById(clientId, msg, new Object[] { param });
        return returnFacesMessage;
    }

    /**
     * Add an error message
     *
     * @param clientId
     * @param msg
     * @param params
     * @return
     */
    public static FacesMessage addErrorMessageById(String clientId, String msg, Object[] params) {
        FacesContext context = FacesContext.getCurrentInstance();
        String msgs = msg;
        Application application = context.getApplication();
        String messageBundleName = application.getMessageBundle();
        Locale locale = context.getViewRoot().getLocale();
        ResourceBundle rb = ResourceBundle.getBundle(messageBundleName, locale);
        String msgPattern = rb.getString(msg);
        msgs = msgPattern;
        msgs = MessageFormat.format(msgPattern, params);
        FacesMessage fMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msgs, msgs);
        context.addMessage(clientId, fMsg);
        return fMsg;
    }


    /**
     * Get servlet context.
     * 
     * @return the servlet context
     */
    public static ServletContext getServletContext() {
        ServletContext returnServletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return returnServletContext;
    }

    public static String getApplicationRealPath(String s) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        ServletContext scontext = (ServletContext) econtext.getContext();
        String returnString = scontext.getRealPath(s);
        return returnString;
    }
    
    /**
     * Recupera el Contextpath
     * @return
     */
    public static String getContextPath() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest resquest = (HttpServletRequest) econtext.getRequest();
        String url = resquest.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - resquest.getRequestURI().length()) + resquest.getContextPath() + "/";
        
        logger.info("BaseURL -> " + baseURL);
        
        return resquest.getContextPath();
    }    

    /**
     * Get managed bean based on the bean name.
     * 
     * @param beanName
     *            the bean name
     * @return the managed bean associated with the bean name
     */
    public static Object getManagedBean(String beanName) {
        Object o = getValueBinding(getJsfEl(beanName)).getValue(FacesContext.getCurrentInstance());
        return o;
    }
    
    /**
     * get the JSF-EL style value
     * 
     * @param value
     * @return
     */
    private static String getJsfEl(String value) {
        String returnString = "#{" + value + "}";
        return returnString;
    }

    public static String getRemoteUser() {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext econtext = context.getExternalContext();
		String returnString = econtext.getRemoteUser();
		logger.info("getRemoteUser() - termina");
            return returnString;
    }
    

    public static boolean hasRole(String role) {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext econtext = context.getExternalContext();
            boolean returnboolean = econtext.isUserInRole(role);
            return returnboolean;
    }
    
    /**
     * get the HttpSession
     * 
     * @return HttpSession
     */
    public static HttpSession getSession() {
            HttpSession returnHttpSession = getSession(false);
            return returnHttpSession;
    }

    /**
     * get the HttpSession
     * 
     * @param create
     *            create a new HttpSession
     * @return HttpSession
     */
    public static HttpSession getSession(boolean create) {
        HttpSession returnHttpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
        return returnHttpSession;
    }

    /**
     * Recupera URL real
     * del server activo donde
     * el website esta corriendo
     * @return
     */
    public static String getServerURL() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();    
        String baseURL = null;
        StringBuffer sbaseURL = new StringBuffer();
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String serverName = httpRequest.getServerName();
            int serverPort = httpRequest.getServerPort();

            // Detect Nuxeo specific header for VH
            String nuxeoVH = httpRequest.getHeader(VH_HEADER);
            if ((nuxeoVH != null) && (nuxeoVH.contains("http"))) {
				logger.info("getServerURL() - termina");
                return nuxeoVH;
            }

            // Detect virtual hosting based in standard header
            String forwardedHost = httpRequest.getHeader(X_FORWARDED_HOST);

            // :XXX: Need to test out with different proxy configurations
            // especially with several proxies routing the request.
            if (forwardedHost != null) {
                if (forwardedHost.contains(":")) {
                    serverName = forwardedHost.split(":")[0];
                    serverPort = Integer.valueOf(forwardedHost.split(":")[1]);
                } else {
                    serverPort = 80; // fallback
                    serverName = forwardedHost;
                }
            }

            // :XXX: To be tested in case of virtual hosting...
            String protocol = httpRequest.getScheme();

            sbaseURL.append(protocol);
            sbaseURL.append("://");
            sbaseURL.append(serverName);
            if (serverPort != 0) {
                if (("http".equals(protocol) && serverPort != 80)
                        || ("https".equals(protocol) && serverPort != 443)) {
                    sbaseURL.append(':');
                    sbaseURL.append(serverPort);
                }
            }
            baseURL = sbaseURL.toString();
        }
        return baseURL;
    }

    /**
     * Dispara una navegacion
     * @param navigation
     */
    public static void goToNavigation(String navigation){
        FacesContext ctx = FacesContext.getCurrentInstance();
        NavigationHandler nh = ctx.getApplication().getNavigationHandler();
        nh.handleNavigation(ctx, null, navigation);
    }      
    
    /**
     * Agrega mensajes
     * @param facesmessages
     */
    public static void addFacesMessages(FacesMessage facesmessages) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, facesmessages);        
    }

    /**
     * Recupera parametros
     * @return
     */
    public static Map getRequestParameter() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    }
    
    /**
         * Setea un atributo en sesi�n
         *
         * @param nombre
         * @param valor
         */
    public static  boolean isInternetExplorer7() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)ctx.getExternalContext().getRequest();
        String agent = request.getHeader("User-Agent").toLowerCase();
        if (agent.indexOf("msie 7") != -1) return true;
        return false;
    }   
}
