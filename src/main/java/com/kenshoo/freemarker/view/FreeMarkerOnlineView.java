package com.kenshoo.freemarker.view;

import java.nio.charset.Charset;

import org.springframework.util.Assert;

import com.yammer.dropwizard.views.View;

import freemarker.template.Configuration;

/**
 * Created with IntelliJ IDEA. User: nir Date: 4/11/14 Time: 12:23 PM
 */
public class FreeMarkerOnlineView extends View {

    private final FreeMarkerOnlineViewResultType resultType;
    private final String template;
    private final String dataModel;
    private String result;

    static final String DATA_MODEL_ERROR_MESSAGE_HEADING = "Failed to parse data model:";
    static final String DATA_MODEL_ERROR_MESSAGE_FOOTER = "Note: This is NOT a FreeMarker error message. "
                + "The data model syntax is specific to this online service.";

    public static final String NO_RESULTS = "No Results Yet ";

    public FreeMarkerOnlineView() {
        this(FreeMarkerOnlineViewResultType.BLANK, null, "", "");
    }

    public FreeMarkerOnlineView(FreeMarkerOnlineViewResultType resultType, String resultText, String template, String dataModel) {
        super("/view/freemarker-online.ftl", Charset.forName("utf-8"));
        Assert.notNull(resultType);
        this.resultType = resultType;
        this.result = decorateResultText(resultText, resultType);
        this.template = template;
        this.dataModel = dataModel;
    }

    private String decorateResultText(String resultText, FreeMarkerOnlineViewResultType resultType) {
        switch (resultType) {
        case DATA_MODEL_ERROR:
            return DATA_MODEL_ERROR_MESSAGE_HEADING + "\n\n" + resultText + "\n\n" + DATA_MODEL_ERROR_MESSAGE_FOOTER;
        default:
            // We don't decorate in the other cases at the moment.
            return resultText;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResultText(String resultText) {
        this.result = resultText;
    }

    public String getTemplate() {
        return template;
    }

    public String getDataModel() {
        return dataModel;
    }
    
    public boolean getHasResult() {
        return resultType != FreeMarkerOnlineViewResultType.BLANK;
    }
    
    public boolean isErrorResult() {
        return resultType.isError();
    }

    public FreeMarkerOnlineViewResultType getResultType() {
        return resultType;
    }

    public String getFreeMarkerVersion() {
        return Configuration.getVersion().toString();
    }
}
