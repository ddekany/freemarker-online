package com.kenshoo.freemarker.platform;

import com.google.common.io.Resources;
import com.kenshoo.freemarker.dropwizard.ApplicationStartup;
import com.yammer.dropwizard.testing.junit.DropwizardServiceRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

/**
 * Created with IntelliJ IDEA.
 * User: shlomis
 * Date: 9/9/13
 * Time: 10:43 AM
 */
public class DropWizardServiceTest {
    @ClassRule
    public static TestRule testRule = new DropwizardServiceRule<>(ApplicationStartup.class,
            Resources.getResource("freemarker-online.yml").getPath());


    @Test
    public void testServerIsUp() throws Exception {
        ((DropwizardServiceRule)testRule).getService();
    }
}
