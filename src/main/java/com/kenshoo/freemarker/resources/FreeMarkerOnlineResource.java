package com.kenshoo.freemarker.resources;

import com.kenshoo.freemarker.view.FreeMarkerOnlineView;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
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

    @GET
    @Produces(MediaType.TEXT_HTML)
    public FreeMarkerOnlineView index() {
        return new FreeMarkerOnlineView();
    }
}
