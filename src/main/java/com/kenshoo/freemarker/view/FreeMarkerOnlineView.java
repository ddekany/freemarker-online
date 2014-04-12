package com.kenshoo.freemarker.view;

import com.yammer.dropwizard.views.View;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/11/14
 * Time: 12:23 PM
 */
public class FreeMarkerOnlineView extends View{

    public FreeMarkerOnlineView() {
        super("/view/freemarker-online.mustache");
    }
}
