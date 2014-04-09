package com.kenshoo.freemarker.resources;

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
 * To change this template use File | Settings | File Templates.
 */
@Path("/")
@Component
public class MyProjectResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "I'm working!";
    }
}
