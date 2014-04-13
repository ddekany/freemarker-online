package com.kenshoo.freemarker.services;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/13/14
 * Time: 7:10 AM
 */
public class FreeMarkerServiceResponseBuilder {

    public FreeMarkerServiceResponseBuilder(){}

    public FreeMarkerServiceResponse successfulResponse(String result){
        return new FreeMarkerServiceResponse(result,true,"");
    }

    public FreeMarkerServiceResponse errorResponse(String errorReason){
        return new FreeMarkerServiceResponse("",false,errorReason);
    }
}
