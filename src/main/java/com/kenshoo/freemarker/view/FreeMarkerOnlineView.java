/*
 * Copyright 2014 Kenshoo.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kenshoo.freemarker.view;

import java.nio.charset.Charset;

import org.springframework.util.Assert;

import com.yammer.dropwizard.views.View;

import freemarker.template.Configuration;

/**
 * Created with IntelliJ IDEA. User: nir Date: 4/11/14 Time: 12:23 PM
 */
public class FreeMarkerOnlineView extends View {

    private final String template;
    private final String dataModel;
    private final boolean execute;

    public FreeMarkerOnlineView() {
        this( "", "", false);
    }
    public  FreeMarkerOnlineView(String template, String dataModel, boolean execute) {
        super("/view/freemarker-online.ftl", Charset.forName("utf-8"));
        this.template = template;
        this.dataModel = dataModel;
        this.execute = execute;
    }

    public String getTemplate() {
        return template;
    }

    public String getDataModel() {
        return dataModel;
    }

    public boolean isExecute() { return this.execute; }

    public String getFreeMarkerVersion() {
        return Configuration.getVersion().toString();
    }
}
