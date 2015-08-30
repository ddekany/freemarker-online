package com.kenshoo.freemarker.model;

/**
 * Created by Pmuruge on 8/28/2015.
 */
public class FreeMarkerPayload {
    private String template;
    private String dataModel;

    public FreeMarkerPayload() {
    }

    public FreeMarkerPayload(String template, String dataModel) {
        this.template = template;
        this.dataModel = dataModel;
    }

    public String getDataModel() {
        return dataModel;
    }

    public void setDataModel(String dataModel) {
        this.dataModel = dataModel;
    }

    public String getTemplate() {

        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
