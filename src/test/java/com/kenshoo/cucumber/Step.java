package com.kenshoo.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: shlomis
 * Date: 9/11/13
 * Time: 3:27 PM
 * Cucu step for the hell of it
 */

public class Step extends BaseCucuStep {

    @Given("^I am a given step$")
    public void given() {
        System.out.println("******************* Given was executed ! ****************");
    }

    @When("^I am a when step$")
    public void when() {
        System.out.println("******************* When was executed ! ****************");
    }

    @Then("^I am a then step$")
    public void then() {
        System.out.println("******************* Then was executed ! ****************");
    }

    @Then("^I am a failing then step$")
    public void failingThen() {
        System.out.println("******************* failingThen was executed ! ****************");
//        fail("Fail this biaaatch !");
    }
}
