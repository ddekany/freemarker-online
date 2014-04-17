package com.kenshoo.freemarker.webdriver;

import com.google.common.io.Resources;
import com.kenshoo.freemarker.dropwizard.ApplicationStartup;
import com.yammer.dropwizard.testing.junit.DropwizardServiceRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/14/14
 * Time: 11:29 AM

 */
public class WebDriverTest {

    private static final String TEMPLATE_INPUT_NAME = "template";
    private static final String PARAMS_FIELD_NAME = "params";
    private WebDriver driver;

    @ClassRule
    public static TestRule testRule = new DropwizardServiceRule<>(ApplicationStartup.class,
            Resources.getResource("freemarker-online.yml").getPath());

    @Before
    public void initDriver(){
        driver = new HtmlUnitDriver();
    }

    @After
    public void closeDriver(){
        driver.quit();
    }

    @Test
    public void testBasicUIFlow() throws Exception {
        driver.get("http://localhost:8080");
        fillInputField(TEMPLATE_INPUT_NAME, "${var1}");
        fillInputField(PARAMS_FIELD_NAME,"var1=val1");
        submitForm();
        WebElement result = driver.findElement(By.id("result"));
        assertEquals(result.getText(), "val1");
    }

    private void submitForm() {
        driver.findElement(By.name(TEMPLATE_INPUT_NAME)).submit();
    }

    private void fillInputField(String fieldName, String value) {
        WebElement template = driver.findElement(By.name(fieldName));
        template.sendKeys(value);
    }


}
