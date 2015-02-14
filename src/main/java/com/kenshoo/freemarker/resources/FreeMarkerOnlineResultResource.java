package com.kenshoo.freemarker.resources;

import com.kenshoo.freemarker.services.FreeMarkerService;
import com.kenshoo.freemarker.services.FreeMarkerServiceResponse;
import com.kenshoo.freemarker.util.DataModelParser;
import com.kenshoo.freemarker.util.DataModelParsingException;
import com.kenshoo.freemarker.util.ExceptionUtils;
import com.kenshoo.freemarker.view.FreeMarkerOnlineView;
import com.kenshoo.freemarker.view.FreeMarkerOnlineViewResultType;

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
                    FreeMarkerOnlineViewResultType.TEMPLATE_ERROR, ExceptionUtils.getMessageWithCauses(failureReason),
                    templateInput, dataModelInput);
        }
    }

}
