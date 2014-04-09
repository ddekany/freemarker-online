package com.kenshoo.cucumber;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: shlomis
 * Date: 9/11/13
 * Time: 4:05 PM
 */
public class DBStep extends BaseCucuStep{

    @Autowired
    JdbcTemplate template;


    @Given("^I have a value$")
    public void given() {
        template.execute("INSERT INTO dropwizard_bootstrap SET id=1, name='shlomi'"); // this will also fail on "garbage" from other tests
    }

    @When("^I set a value$")
    public void when() {
        template.execute("UPDATE dropwizard_bootstrap SET name='shlomi_dropwizard' WHERE id=1");
    }

    @Then("^I expect a value$")
    public void then() {
        assertEquals(1, template.queryForInt("SELECT id FROM dropwizard_bootstrap WHERE  name='shlomi_dropwizard'"));
    }

    @After
    public void tearDown(){
        template.execute("DELETE FROM dropwizard_bootstrap"); // this will also fail on "garbage" from other tests
    }

}
