package com.kenshoo.freemarker.services;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.core.Environment;
import freemarker.core.ParseException;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:15 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class FreeMarkerServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(FreeMarkerServiceTest.class); 
    
    private static final int MAX_THREADS = 3;
    private static final int MAX_QUEUE_LENGTH = 2;

    private static final int BLOCKING_TEST_TIMEOUT = 5000;
    
    private static final String TRUNCATION_TEST_TEMPLATE = "12345";
    
    @InjectMocks
    private FreeMarkerService freeMarkerService;
    
    @Before
    public void initializeSpringBeans() {
        freeMarkerService.setMaxQueueLength(MAX_QUEUE_LENGTH);
        freeMarkerService.setMaxThreads(MAX_THREADS);
        freeMarkerService.afterPropertiesSet();
    }

    @Test
    public void testCalculationOfATemplateWithNoDataModel() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                "test", Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(true));
        assertThat(serviceResponse.getTemplateOutput(), is("test"));
    }

    @Test
    public void testSimpleTemplate() {
        HashMap<String, Object> dataModel = new HashMap<>();
        dataModel.put("var1", "val1");
        String templateSourceCode = "${var1}";
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                templateSourceCode, dataModel);
        assertThat(serviceResponse.getTemplateOutput(), equalTo("val1"));
    }

    @Test
    public void testTemplateWithFewArgsAndOperators() {
        HashMap<String, Object> dataModel = new HashMap<>();
        dataModel.put("var1", "val1");
        dataModel.put("var2", "val2");
        String template = "${var1?capitalize} ${var2?cap_first}";
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(template, dataModel);
        assertThat(serviceResponse.getTemplateOutput(), equalTo("Val1 Val2"));
    }

    @Test
    public void testTemplateWithSyntaxError() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                "test ${xx", Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(false));
        assertThat(serviceResponse.getFailureReason(), instanceOf(ParseException.class));
    }

    @Test
    public void testTemplateWithEvaluationError() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                "test ${x}", Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(false));
        assertThat(serviceResponse.getFailureReason(), instanceOf(TemplateException.class));
    }

    @Test
    public void testResultAlmostTruncation() {
        freeMarkerService.setOutputLengthLimit(5);
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                TRUNCATION_TEST_TEMPLATE, Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(true));
        assertThat(serviceResponse.isTemplateOutputTruncated(), is(false));
        assertThat(serviceResponse.getTemplateOutput(), equalTo(TRUNCATION_TEST_TEMPLATE));
    }

    @Test
    public void testResultTruncation() {
        freeMarkerService.setOutputLengthLimit(4);
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                TRUNCATION_TEST_TEMPLATE, Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(true));
        assertThat(serviceResponse.isTemplateOutputTruncated(), is(true));
        assertThat(serviceResponse.getTemplateOutput(),
                startsWith(TRUNCATION_TEST_TEMPLATE.substring(0, freeMarkerService.getOutputLengthLimit())));
        assertThat(serviceResponse.getTemplateOutput().charAt(freeMarkerService.getOutputLengthLimit()),
                not(equalTo(TRUNCATION_TEST_TEMPLATE.charAt(freeMarkerService.getOutputLengthLimit()))));
    }
    
    @Test
    public void serviceOverburdenTest() throws InterruptedException {
        final BlockerDirective blocker = new BlockerDirective();
        final Map<String, BlockerDirective> blockerDataModel = Collections.singletonMap("blocker", blocker);
        try {
            // Fill all available task "slots":
            for (int i = 0; i < MAX_THREADS + MAX_QUEUE_LENGTH; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        freeMarkerService.calculateTemplateOutput("<@blocker/>", blockerDataModel);                    
                    }
                }).start();
            }
            
            // Wait until all template executions has started:
            synchronized (blocker) {
                final long startTime = System.currentTimeMillis();
                while (blocker.getEntered() < MAX_THREADS) {
                    // To avoid blocking the CI server forever is something goes wrong:
                    if (System.currentTimeMillis() - startTime > BLOCKING_TEST_TIMEOUT) {
                        fail("JUnit test timed out");
                    }
                    blocker.wait(1000);
                }
            }
            Thread.sleep(200);
            // Because the others are waiting in the queue, and weren't started:
            assertThat(blocker.getEntered(), not(greaterThan(MAX_THREADS)));
            
            // Souldn't accept on more tasks:
            try {
                freeMarkerService.calculateTemplateOutput("<@blocker/>", blockerDataModel);
                fail("Expected RejectedExecutionException, but nothing was thrown.");
            } catch (RejectedExecutionException e) {
                // Expected
            }
        } finally {
            // Ensure that the started threads will end:
            blocker.release();
        }
    }
    
    private static final class BlockerDirective implements TemplateDirectiveModel {
        
        private int entered;
        private boolean released;

        public synchronized void release() {
            released = true;
            notifyAll();
        }
        
        @Override
        public synchronized void execute(Environment env, @SuppressWarnings("rawtypes") Map params,
                TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
            entered++;
            notifyAll();
            final long startTime = System.currentTimeMillis();
            while (!released) {
                // To avoid blocking the CI server forever is something goes wrong:
                if (System.currentTimeMillis() - startTime > BLOCKING_TEST_TIMEOUT) {
                    LOG.error("JUnit test timed out");
                }
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    LOG.error("JUnit test was interrupted");
                }
            }
            LOG.debug("Blocker released");
        }

        public synchronized int getEntered() {
            return entered;
        }
        
    }
    
}
