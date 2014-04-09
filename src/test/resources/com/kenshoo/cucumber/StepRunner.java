package com.kenshoo.cucumber;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: shlomis
 * Date: 9/11/13
 * Time: 3:49 PM
 */
@RunWith(Cucumber.class)
@Cucumber.Options(features = "src/test/resources/com/kenshoo/cucumber/")
public class StepRunner {
}
