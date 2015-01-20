package com.kenshoo.freemarker.view;

import com.yammer.dropwizard.views.View;

import freemarker.template.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/11/14
 * Time: 12:23 PM
 */
public class FreeMarkerOnlineView extends View {

    private final boolean errorInTemplateEvaluation;
    private final String template;
    private final String params;
    private String result;
    public static final String ERROR_IN_EVAL = "Error in template Evaluation: ";
    public static final String NO_RESULTS = "No Results Yet ";


    public FreeMarkerOnlineView() {
                this(false,NO_RESULTS,"","");
        }

    public FreeMarkerOnlineView(boolean errorInTemplateEvaluation,String result, String template, String params) {
        super("/view/freemarker-online.mustache");
        this.errorInTemplateEvaluation = errorInTemplateEvaluation;
        this.result = result;
        this.template = template;
        this.params = params;

    }

    public String getResult(){
        if (errorInTemplateEvaluation)
            return  ERROR_IN_EVAL + result;
        return result;
    }

    public String getTemplate(){
        return template;
    }

    public String getParams(){
            return params;
        }
    
    public String getFreeMarkerVersion() {
        return Configuration.getVersion().toString();
    }
}
