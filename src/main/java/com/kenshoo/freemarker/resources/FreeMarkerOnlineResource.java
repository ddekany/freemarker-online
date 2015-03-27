/*
 * Copyright 2014 Kenshoo.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kenshoo.freemarker.resources;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

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

    private static final int MAX_TEMPLATE_INPUT_LENGTH = 10000;
    
    private static final int MAX_DATA_MODEL_INPUT_LENGTH = 10000;

    private static final String MAX_TEMPLATE_INPUT_LENGTH_EXCEEDED_ERROR_MESSAGE
            = "The template length has exceeded the {0} character limit set for this service.";
    
    private static final String MAX_DATA_MODEL_INPUT_LENGTH_EXCEEDED_ERROR_MESSAGE
            = "The data model length has exceeded the {0} character limit set for this service.";

    private static final String SERVICE_OVERBURDEN_ERROR_MESSAGE
            = "Sorry, the service is overburden and couldn't handle your request now. Try again later.";
    
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
        
        if (dataModelInput.length() > MAX_DATA_MODEL_INPUT_LENGTH) {
            return new FreeMarkerOnlineView(
                    FreeMarkerOnlineViewResultType.DATA_MODEL_ERROR,
                    new MessageFormat(MAX_DATA_MODEL_INPUT_LENGTH_EXCEEDED_ERROR_MESSAGE, Locale.US)
                            .format(new Object[] { MAX_DATA_MODEL_INPUT_LENGTH }),
                    templateInput, dataModelInput);
        }
        Map<String, Object> dataModel;
        try {
            dataModel = DataModelParser.parse(dataModelInput, freeMarkerService.getFreeMarkerTimeZone());
        } catch (DataModelParsingException e) {
            return new FreeMarkerOnlineView(
                    FreeMarkerOnlineViewResultType.DATA_MODEL_ERROR, e.getMessage(),
                    templateInput, dataModelInput);
        }
        
        if (templateInput.length() > MAX_TEMPLATE_INPUT_LENGTH) {
            return new FreeMarkerOnlineView(
                    FreeMarkerOnlineViewResultType.TEMPLATE_ERROR,
                    new MessageFormat(MAX_TEMPLATE_INPUT_LENGTH_EXCEEDED_ERROR_MESSAGE, Locale.US)
                            .format(new Object[] { MAX_TEMPLATE_INPUT_LENGTH }),
                    templateInput, dataModelInput);
        }
        FreeMarkerServiceResponse freeMarkerServiceResponse;
        try {
            freeMarkerServiceResponse = freeMarkerService.calculateTemplateOutput(templateInput, dataModel);
        } catch (RejectedExecutionException e) {
            return new FreeMarkerOnlineView(
                    FreeMarkerOnlineViewResultType.TEMPLATE_ERROR, SERVICE_OVERBURDEN_ERROR_MESSAGE,
                    templateInput, dataModelInput);
        }
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
