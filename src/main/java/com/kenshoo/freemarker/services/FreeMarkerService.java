package com.kenshoo.freemarker.services;

import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kenshoo.freemarker.util.LengthLimitExceededException;
import com.kenshoo.freemarker.util.LengthLimitedWriter;

import freemarker.core.ParseException;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 10:15 AM
 */
@Service
public class FreeMarkerService {

    private static final int DEFAULT_OUTPUT_LENGTH_LIMIT = 100000;
    
    private static final String OUTPUT_LENGTH_LIMIT_EXCEEDED_TERMINATION = "\n----------\n"
            + "Aborted template processing, as the output length has exceeded the {0} character limit set for "
            + "this service.";
    
    private static final Logger logger = LoggerFactory.getLogger(FreeMarkerService.class);

    private final Configuration freeMarkerConfig;
    
    private int outputLengthLimit = DEFAULT_OUTPUT_LENGTH_LIMIT;
    
    public FreeMarkerService() {
        freeMarkerConfig = new Configuration(Configuration.getVersion());
        freeMarkerConfig.setNewBuiltinClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);
        freeMarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freeMarkerConfig.setLocale(Locale.US);
        freeMarkerConfig.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        freeMarkerConfig.setOutputEncoding("UTF-8");
    }
    
    /**
     * @return The result of the template parsing and evaluation. The method won't throw exception if that fails due to
     *         errors in the template provided, instead it indicates this fact in the response object. That's because
     *         this is a service for trying out the template language, so such errors are part of the normal operation.
     * 
     * @throws FreeMarkerServiceException
     *             If the calculation fails from a reason that's not a mistake in the template.
     */
    public FreeMarkerServiceResponse calculateTemplateOutput(
            String templateSourceCode, Map<String, Object> dataModel) {
        Template template;
        try {
            template = new Template(null, templateSourceCode, freeMarkerConfig);
        } catch (ParseException e) {
            // Expected (part of normal operation)
            return createFailureResponse(e);
        } catch (Exception e) {
            // Not expected
            throw new FreeMarkerServiceException("Unexpected exception during template parsing", e);
        }
        
        boolean resultTruncated;
        StringWriter writer = new StringWriter();
        try {
            template.process(dataModel, new LengthLimitedWriter(writer, outputLengthLimit));
            resultTruncated = false;
        } catch (LengthLimitExceededException e) {
            resultTruncated = true;
            writer.write(new MessageFormat(OUTPUT_LENGTH_LIMIT_EXCEEDED_TERMINATION, Locale.US)
                    .format(new Object[] { outputLengthLimit }));
            // Falls through (not an error)
        } catch (TemplateException e) {
            // Expected (part of normal operation)
            return createFailureResponse(e);
        } catch (Exception e) {
            // Not expected
            throw new FreeMarkerServiceException("Unexpected exception during template evaluation", e);
        }
        
        return new FreeMarkerServiceResponse.Builder().buildForSuccess(writer.toString(), resultTruncated);
    }
    
    public int getOutputLengthLimit() {
        return outputLengthLimit;
    }

    public void setOutputLengthLimit(int outputLengthLimit) {
        this.outputLengthLimit = outputLengthLimit;
    }
    
    /**
     * Returns the time zone used by the FreeMarker templates.
     */
    public TimeZone getFreeMarkerTimeZone() {
        return freeMarkerConfig.getTimeZone();
    }
    
    private FreeMarkerServiceResponse createFailureResponse(Throwable e) {
        logger.debug("The template had error(s)", e);
        return new FreeMarkerServiceResponse.Builder().buildForFailure(e);
    }

}
