package com.kenshoo.freemarker.resources;

import com.kenshoo.freemarker.services.FreeMarkerService;
import com.kenshoo.freemarker.services.FreeMarkerServiceResponse;
import com.kenshoo.freemarker.view.FreeMarkerOnlineView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 9:58 PM
 */
@Path("/result")
@Component
public class FreeMarkerOnlineResultResource {

    @Autowired
    FreeMarkerService freeMarkerService;

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public FreeMarkerOnlineView formResult(@FormParam("template") String templateText, @FormParam("params") String params) {
        Properties properties = parseProperties(params);
        Map<String, String> templateParams = propertiesToMap(properties);
        FreeMarkerServiceResponse freeMarkerServiceResponse = freeMarkerService.calculateFreeMarkerTemplate(templateText, templateParams);
        if (freeMarkerServiceResponse.isSucceed()){
            return new FreeMarkerOnlineView(false,freeMarkerServiceResponse.getResult(),templateText,params);
        }
        else {
            return new FreeMarkerOnlineView(false,freeMarkerServiceResponse.getErrorReason(),templateText,params);
        }

    }

    private Properties parseProperties(String params) {
        try {
            Properties properties = new Properties();
            properties.load(new StringReader(params));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> propertiesToMap(Properties properties) {
        Map<String, String> templateParams = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            templateParams.put(key, properties.getProperty(key));
        }
        return templateParams;
    }
}
