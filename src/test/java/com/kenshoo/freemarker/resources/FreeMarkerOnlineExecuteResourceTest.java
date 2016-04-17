package com.kenshoo.freemarker.resources;

import com.kenshoo.freemarker.model.ExecuteRequest;
import com.kenshoo.freemarker.model.ExecuteResourceFields;
import com.kenshoo.freemarker.model.ExecuteResponse;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Created by Pmuruge on 8/29/2015.
 */
public class FreeMarkerOnlineExecuteResourceTest extends JerseyTest {
    private static final String DATA_MODEL = "user=John";
    private static final String TEMPLATE_WITH_VARIABLE = "Welcome ${user}";
    private static final String TEMPLATE_PLAIN = "Welcome John";
    private static final String MALFORMED_DATA_MODEL = "userJohn";
    private static final String EXECUTE_API = "api/execute";
    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("com.kenshoo.freemarker.resources")
                        .contextPath("/")
                        .contextListenerClass(ContextLoaderListener.class)
                        .contextParam("contextConfigLocation", "classpath:spring/bootstrap-context.xml")
                        .servletClass(SpringServlet.class)
                        .requestListenerClass(RequestContextListener.class)
                        .build();
    }

    @Test
    public void testSuccessRequest() throws Exception {
        ExecuteRequest payload = new ExecuteRequest(TEMPLATE_WITH_VARIABLE, DATA_MODEL);
        ClientResponse resp = client().resource(getBaseURI().toString() + EXECUTE_API).header("Content-Type", "application/json").entity(payload).post(ClientResponse.class);
        assertEquals(200, resp.getStatus());
        ExecuteResponse response = resp.getEntity(ExecuteResponse.class);
        assertNull(response.getProblems());
    }

    @Test
    public void testMalformedDataModel() throws Exception {
        ExecuteRequest payload = new ExecuteRequest(TEMPLATE_PLAIN, MALFORMED_DATA_MODEL);
        ClientResponse resp = client().resource(getBaseURI().toString() + EXECUTE_API).header("Content-Type", "application/json").entity(payload).post(ClientResponse.class);
        assertEquals(200, resp.getStatus());
        ExecuteResponse response = resp.getEntity(ExecuteResponse.class);
        assertNotNull(response.getProblems());
        assertTrue(response.getProblems().containsKey(ExecuteResourceFields.DATA_MODEL));
    }

    @Test
    public void testLongDataModel() throws Exception {
        String longDataModel = create30KString();
        ExecuteRequest payload = new ExecuteRequest(TEMPLATE_PLAIN, longDataModel);
        ClientResponse resp = client().resource(getBaseURI().toString() + EXECUTE_API).header("Content-Type", "application/json").entity(payload).post(ClientResponse.class);
        assertEquals(200, resp.getStatus());
        ExecuteResponse response = resp.getEntity(ExecuteResponse.class);
        assertNotNull(response.getProblems());
        assertTrue(response.getProblems().containsKey(ExecuteResourceFields.DATA_MODEL));
        String error = response.getProblems().get(ExecuteResourceFields.DATA_MODEL);
        assertThat(error, containsString("data model"));
        assertThat(error, containsString("limit"));
    }
    @Test
    public void testLongTemplate() throws Exception {
        String longTemplate = create30KString();
        ExecuteRequest payload = new ExecuteRequest(longTemplate, DATA_MODEL);
        ClientResponse resp = client().resource(getBaseURI().toString() + EXECUTE_API).header("Content-Type", "application/json").entity(payload).post(ClientResponse.class);
        assertEquals(200, resp.getStatus());
        ExecuteResponse response = resp.getEntity(ExecuteResponse.class);
        assertNotNull(response.getProblems());
        assertTrue(response.getProblems().containsKey(ExecuteResourceFields.TEMPLATE));
        String error = response.getProblems().get(ExecuteResourceFields.TEMPLATE);
        assertThat(error, containsString("template"));
        assertThat(error, containsString("limit"));
    }

    private String create30KString() {
        final String veryLongString;
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 30000 / 10; i++) {
                sb.append("0123456789");
            }
            veryLongString = sb.toString();
        }
        return veryLongString;
    }



}
