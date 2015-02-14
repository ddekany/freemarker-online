package com.kenshoo.freemarker.resources;

import java.util.Map;

import com.kenshoo.freemarker.services.FreeMarkerService;
import com.kenshoo.freemarker.services.FreeMarkerServiceResponse;
import com.kenshoo.freemarker.util.DataModelParser;
import com.kenshoo.freemarker.util.DataModelParsingException;
import com.kenshoo.freemarker.util.ExceptionUtils;
import com.kenshoo.freemarker.view.FreeMarkerOnlineView;
import com.kenshoo.freemarker.view.FreeMarkerOnlineViewResultType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: shlomis
 * Date: 9/1/13
 * Time: 4:35 PM
 */
@Path("/")
@Component
public class FreeMarkerOnlineResource {
    
    @Autowired
    private FreeMarkerService freeMarkerService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public FreeMarkerOnlineView blankForm() {
        return new FreeMarkerOnlineView();
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public FreeMarkerOnlineView formResult(
            @FormParam("template") String templateInput,
            @FormParam("dataModel") String dataModelInput) {
        if (StringUtils.isBlank(templateInput) && StringUtils.isBlank(dataModelInput)) {
            return blankForm();
        }
        
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
