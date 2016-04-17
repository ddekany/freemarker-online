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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.RejectedExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kenshoo.freemarker.model.ErrorCode;
import com.kenshoo.freemarker.model.ErrorResponse;
import com.kenshoo.freemarker.model.ExecuteRequest;
import com.kenshoo.freemarker.model.ExecuteResourceFields;
import com.kenshoo.freemarker.model.ExecuteResponse;
import com.kenshoo.freemarker.services.AllowedSettingValuesMaps;
import com.kenshoo.freemarker.services.FreeMarkerService;
import com.kenshoo.freemarker.services.FreeMarkerServiceResponse;
import com.kenshoo.freemarker.util.DataModelParser;
import com.kenshoo.freemarker.util.DataModelParsingException;
import com.kenshoo.freemarker.util.ExceptionUtils;

import freemarker.core.OutputFormat;

/**
 * Created by pradeep on 8/28/2015.
 */
@Path("/api/execute")
@Component
public class FreeMarkerOnlineExecuteResource {
    private static final int MAX_TEMPLATE_INPUT_LENGTH = 10000;

    private static final int MAX_DATA_MODEL_INPUT_LENGTH = 10000;

    private static final String MAX_TEMPLATE_INPUT_LENGTH_EXCEEDED_ERROR_MESSAGE
            = "The template length has exceeded the {0} character limit set for this service.";

    private static final String MAX_DATA_MODEL_INPUT_LENGTH_EXCEEDED_ERROR_MESSAGE
            = "The data model length has exceeded the {0} character limit set for this service.";

    private static final String UNKNOWN_OUTPUT_FORMAT_ERROR_MESSAGE = "Unknown output format: {0}";
    private static final String UNKNOWN_LOCALE_ERROR_MESSAGE = "Unknown locale: {0}";
    private static final String UNKNOWN_TIME_ZONE_ERROR_MESSAGE = "Unknown time zone: {0}";

    private static final String SERVICE_OVERBURDEN_ERROR_MESSAGE
            = "Sorry, the service is overburden and couldn't handle your request now. Try again later.";

    static final String DATA_MODEL_ERROR_MESSAGE_HEADING = "Failed to parse data model:";
    static final String DATA_MODEL_ERROR_MESSAGE_FOOTER = "Note: This is NOT a FreeMarker error message. "
            + "The data model syntax is specific to this online service.";

    @Autowired
    private FreeMarkerService freeMarkerService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response formResult(
            ExecuteRequest payload) {
        Map<ExecuteResourceFields, String> problems = new HashMap<ExecuteResourceFields, String>();
        ExecuteResponse executeResponse = new ExecuteResponse();
        if (StringUtils.isBlank(payload.getTemplate()) && StringUtils.isBlank(payload.getDataModel())) {
            return Response.status(400).entity("Empty Template & data").build();
        }

        if (payload.getDataModel().length() > MAX_DATA_MODEL_INPUT_LENGTH) {
            String error = formatMessage(
                    MAX_DATA_MODEL_INPUT_LENGTH_EXCEEDED_ERROR_MESSAGE, MAX_DATA_MODEL_INPUT_LENGTH);
            problems.put(ExecuteResourceFields.DATA_MODEL, error);
            executeResponse.setProblems(problems);
            return buildFreeMarkerResponse(executeResponse);
        }
        final Map<String, Object> dataModel;
        try {
            dataModel = DataModelParser.parse(payload.getDataModel(), freeMarkerService.getFreeMarkerTimeZone());
        } catch (DataModelParsingException e) {
            String error = e.getMessage();
            problems.put(ExecuteResourceFields.DATA_MODEL, decorateResultText(error));
            executeResponse.setProblems(problems);
            return buildFreeMarkerResponse(executeResponse);
        }

        if (payload.getTemplate().length() > MAX_TEMPLATE_INPUT_LENGTH) {
            String error = formatMessage(MAX_TEMPLATE_INPUT_LENGTH_EXCEEDED_ERROR_MESSAGE, MAX_TEMPLATE_INPUT_LENGTH);
            problems.put(ExecuteResourceFields.TEMPLATE, error);
            executeResponse.setProblems(problems);
            return buildFreeMarkerResponse(executeResponse);
        }
        
        final OutputFormat outputFormat;
        {
            String outputFormatStr = payload.getOutputFormat();
            if (StringUtils.isBlank(outputFormatStr)) {
                outputFormat = AllowedSettingValuesMaps.DEFAULT_OUTPUT_FORMAT;
            } else {
                outputFormat = AllowedSettingValuesMaps.OUTPUT_FORMAT_MAP.get(outputFormatStr);
                if (outputFormat == null) {
                    problems.put(
                            ExecuteResourceFields.OUTPUT_FORMAT,
                            formatMessage(UNKNOWN_OUTPUT_FORMAT_ERROR_MESSAGE, outputFormatStr));
                }
            }
        }
        
        final Locale locale;
        {
            String localeStr = payload.getLocale();
            if (StringUtils.isBlank(localeStr)) {
                locale = AllowedSettingValuesMaps.DEFAULT_LOCALE;
            } else {
                locale = AllowedSettingValuesMaps.LOCALE_MAP.get(localeStr);
                if (locale == null) {
                    problems.put(
                            ExecuteResourceFields.LOCALE,
                            formatMessage(UNKNOWN_LOCALE_ERROR_MESSAGE, localeStr));
                }
            }
        }
        
        final TimeZone timeZone;
        {
            String timeZoneStr = payload.getTimeZone();
            if (StringUtils.isBlank(timeZoneStr)) {
                timeZone = AllowedSettingValuesMaps.DEFAULT_TIME_ZONE;
            } else {
                timeZone = AllowedSettingValuesMaps.TIME_ZONE_MAP.get(timeZoneStr);
                if (timeZone == null) {
                    problems.put(
                            ExecuteResourceFields.LOCALE,
                            formatMessage(UNKNOWN_TIME_ZONE_ERROR_MESSAGE, timeZoneStr));
                }
            }
        }
        
        FreeMarkerServiceResponse freeMarkerServiceResponse;
        try {
            freeMarkerServiceResponse = freeMarkerService.calculateTemplateOutput(
                    payload.getTemplate(), dataModel,
                    outputFormat, locale, timeZone);
        } catch (RejectedExecutionException e) {
            String error = SERVICE_OVERBURDEN_ERROR_MESSAGE;
            return Response.serverError().entity(new ErrorResponse(ErrorCode.FREEMARKER_SERVICE_TIMEOUT, error)).build();
        }
        if (freeMarkerServiceResponse.isSuccesful()){
            String result = freeMarkerServiceResponse.getTemplateOutput();
            executeResponse.setResult(result);
            executeResponse.setTruncatedResult(freeMarkerServiceResponse.isTemplateOutputTruncated());
            return buildFreeMarkerResponse(executeResponse);
        } else {
            Throwable failureReason = freeMarkerServiceResponse.getFailureReason();
            String error = ExceptionUtils.getMessageWithCauses(failureReason);
            problems.put(ExecuteResourceFields.TEMPLATE, error);
            executeResponse.setProblems(problems);
            return buildFreeMarkerResponse(executeResponse);
        }

    }
    
    private Response buildFreeMarkerResponse(ExecuteResponse executeResponse){
        return Response.ok().entity(executeResponse).build();
    }
    
    private String decorateResultText(String resultText) {
        return DATA_MODEL_ERROR_MESSAGE_HEADING + "\n\n" + resultText + "\n\n" + DATA_MODEL_ERROR_MESSAGE_FOOTER;
    }
    
    private String formatMessage(String key, Object... params) {
        return new MessageFormat(key, Locale.US).format(params);
    }
    
}
