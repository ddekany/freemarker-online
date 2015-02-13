package com.kenshoo.freemarker.resources;

import com.kenshoo.freemarker.services.FreeMarkerService;
import com.kenshoo.freemarker.services.FreeMarkerServiceResponse;
import com.kenshoo.freemarker.util.DataModelParser;
import com.kenshoo.freemarker.util.DataModelParsingException;
import com.kenshoo.freemarker.view.FreeMarkerOnlineView;
import com.kenshoo.freemarker.view.FreeMarkerOnlineViewResultType;

import freemarker.core.ParseException;
import freemarker.template.TemplateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.FaultAction;

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
    private FreeMarkerService freeMarkerService;

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public FreeMarkerOnlineView formResult(
            @FormParam("template") String templateInput,
            @FormParam("dataModel") String dataModelInput) {
        Map<String, Object> dataModel;
        try {
            dataModel = DataModelParser.parse(dataModelInput);
        } catch (DataModelParsingException e) {
            return new FreeMarkerOnlineView(
                    FreeMarkerOnlineViewResultType.DATA_MODEL_ERROR, e.getMessage(),
                    templateInput, dataModelInput);
        }
        FreeMarkerServiceResponse freeMarkerServiceResponse = freeMarkerService.calculateTemplateOutput(
                templateInput, dataModel);
        if (freeMarkerServiceResponse.isSuccesful()){
            return new FreeMarkerOnlineView(
                    FreeMarkerOnlineViewResultType.TEMPLATE_OUTPUT, freeMarkerServiceResponse.getTemplateOutput(),
                    templateInput, dataModelInput);
        } else {
            Throwable failureReason = freeMarkerServiceResponse.getFailureReason();
            return new FreeMarkerOnlineView(
                    FreeMarkerOnlineViewResultType.TEMPLATE_ERROR, getMessageWithCauses(failureReason),
                    templateInput, dataModelInput);
        }
    }

    /**
     * The error message (and sometimes also the class), and then the same with the cause exception, and so on. Doesn't
     * contain the stack trace or other location information.
     */
    private static String getMessageWithCauses(final Throwable exc) {
        StringBuilder sb = new StringBuilder();
        
        Throwable curExc = exc;
        while (curExc != null) {
            if (curExc != exc) {
                sb.append("\n\nCaused by:\n");
            }
            String msg = curExc.getMessage();
            if (msg == null || !(curExc instanceof TemplateException || curExc instanceof ParseException)) {
                sb.append(curExc.getClass().getName()).append(": ");
            }
            sb.append(msg);
            curExc = curExc.getCause();
        }
        return sb.toString();
    }

}
