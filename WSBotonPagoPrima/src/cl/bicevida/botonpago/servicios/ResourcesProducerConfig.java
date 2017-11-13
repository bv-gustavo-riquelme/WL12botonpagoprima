package cl.bicevida.botonpago.servicios;

import com.sun.jersey.api.core.PackagesResourceConfig;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

public class ResourcesProducerConfig extends PackagesResourceConfig {
    public ResourcesProducerConfig(Map<String, Object> props) {
        super(props);
    }
    
    @Override
    public Map<String, MediaType> getMediaTypeMappings() {
        Map<String, MediaType> mediaTypeMap = new HashMap<String, MediaType>();
        mediaTypeMap.put("json", MediaType.APPLICATION_JSON_TYPE);
        mediaTypeMap.put("xml", MediaType.APPLICATION_XML_TYPE);
        return mediaTypeMap;
    }
}