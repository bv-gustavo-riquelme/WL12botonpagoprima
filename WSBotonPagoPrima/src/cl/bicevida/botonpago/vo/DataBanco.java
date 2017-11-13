package cl.bicevida.botonpago.vo;

public class DataBanco {
    private String value;
    private String name;
    private String type;
    private String actionServlet;
    
    public DataBanco() {
        super();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setActionServlet(String actionServlet) {
        this.actionServlet = actionServlet;
    }

    public String getActionServlet() {
        return actionServlet;
    }
}
