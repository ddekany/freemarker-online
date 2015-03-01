package com.kenshoo.freemarker.services;

import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.eclipse.jetty.util.BlockingArrayQueue;
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

    private static final int DEFAULT_MAX_OUTPUT_LENGTH = 100000;
    private static final int DEFAULT_MAX_THREADS = Math.max(2,
            (int) Math.round(Runtime.getRuntime().availableProcessors() * 3.0 / 4));
    /** Not implemented yet, will need 2.3.22, even then a _CoreAPI call. */
    private static final long MAX_TEMPLATE_EXECUTION_TIME = 2000;
    private static final int DEFAULT_MAX_QUEUE_LENGTH = (int) (30000 / MAX_TEMPLATE_EXECUTION_TIME);
    private static final long THREAD_KEEP_ALIVE_TIME = 4 * 1000;
    
    private static final String MAX_OUTPUT_LENGTH_EXCEEDED_TERMINATION = "\n----------\n"
            + "Aborted template processing, as the output length has exceeded the {0} character limit set for "
            + "this service.";
    
    private static final Logger logger = LoggerFactory.getLogger(FreeMarkerService.class);

    private final Configuration freeMarkerConfig;
    
    private ExecutorService templateExecutor;
    
    private int maxOutputLength = DEFAULT_MAX_OUTPUT_LENGTH;
    
    private int maxThreads = DEFAULT_MAX_THREADS;
    private int maxQueueLength = DEFAULT_MAX_QUEUE_LENGTH;

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
     * @throws RejectedExecutionException
     *             If the service is overburden and thus doing the calculation was rejected.
     * @throws FreeMarkerServiceException
     *             If the calculation fails from a reason that's not a mistake in the template and doesn't fit
     *             the meaning of {@link RejectedExecutionException} either.
     */
    public FreeMarkerServiceResponse calculateTemplateOutput(
        String templateSourceCode, Object dataModel) throws RejectedExecutionException {
        Future<FreeMarkerServiceResponse> future;
        Objects.requireNonNull(templateExecutor,
                "templateExecutor was null - may the Spring bean's afterPropertySet wasn't called");
        future = templateExecutor.submit(new CalculateTemplateOutput(templateSourceCode, dataModel));
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new FreeMarkerServiceException("Templat execution task was interrupted", e);
        } catch (ExecutionException e) {
            throw new FreeMarkerServiceException("Templat execution task unexpectedly fauled", e.getCause());
        }
    }
    
    public int getMaxOutputLength() {
        return maxOutputLength;
    }

    public void setMaxOutputLength(int maxOutputLength) {
        this.maxOutputLength = maxOutputLength;
    }

    public int getMaxThreads() {
        return maxThreads;
    }
    
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }
    
    public int getMaxQueueLength() {
        return maxQueueLength;
    }
    
    public void setMaxQueueLength(int maxQueueLength) {
        this.maxQueueLength = maxQueueLength;
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

    @PostConstruct
    public void postConstruct() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                maxThreads, maxThreads,
                THREAD_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                new BlockingArrayQueue<Runnable>(maxQueueLength));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        templateExecutor = threadPoolExecutor;
    }
    
    private class CalculateTemplateOutput implements Callable<FreeMarkerServiceResponse> {
        
        private final String templateSourceCode;
        private final Object dataModel;

        public CalculateTemplateOutput(String templateSourceCode, Object dataModel) {
            this.templateSourceCode = templateSourceCode;
            this.dataModel = dataModel;
        }

        @Override
        public FreeMarkerServiceResponse call() throws Exception {
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
                template.process(dataModel, new LengthLimitedWriter(writer, maxOutputLength));
                resultTruncated = false;
            } catch (LengthLimitExceededException e) {
                // Not really an error, we just cut the output here.
                resultTruncated = true;
                writer.write(new MessageFormat(MAX_OUTPUT_LENGTH_EXCEEDED_TERMINATION, Locale.US)
                        .format(new Object[] { maxOutputLength }));
                // Falls through
            } catch (TemplateException e) {
                // Expected (part of normal operation)
                return createFailureResponse(e);
            } catch (Exception e) {
                // Not expected
                throw new FreeMarkerServiceException("Unexpected exception during template evaluation", e);
            }
            
            return new FreeMarkerServiceResponse.Builder().buildForSuccess(writer.toString(), resultTruncated);
        }
        
    }

}
