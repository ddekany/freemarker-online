package com.kenshoo.freemarker.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 10:15 AM
 */
@Service
public class FreeMarkerService {

    private static final String ERROR_IN_TEMPLATE_PARSING = "Error in Template Parsing";
    private static final String ERROR_IN_TEMPLATE_EVALUATION = "Error in Template evaluation";
    private final Logger logger = LoggerFactory.getLogger(FreeMarkerService.class);
    private final FreeMarkerServiceResponseBuilder freeMarkerServiceResponseBuilder = new FreeMarkerServiceResponseBuilder();

    public FreeMarkerServiceResponse calculateFreeMarkerTemplate(String templateText, Map<String, String> params) {
        Configuration freeMarkerConfig = new Configuration();
        StringWriter writer = new StringWriter();
        Template template;

        try {
            template = new Template(UUID.randomUUID().toString(), templateText, freeMarkerConfig);
        } catch (IOException e) {
            return createExceptionalResponse(e, ERROR_IN_TEMPLATE_PARSING);
        }
        try {
            template.process(params, writer);
        } catch (TemplateException e) {
            return createExceptionalResponse(e, ERROR_IN_TEMPLATE_EVALUATION);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String result = writer.toString();
        return freeMarkerServiceResponseBuilder.successfulResponse(result);
    }

    private FreeMarkerServiceResponse createExceptionalResponse(Exception e, String msg) {
        logger.info(msg);
        logger.debug(msg, e);
        return freeMarkerServiceResponseBuilder.errorResponse(e.getMessage());
    }

}
