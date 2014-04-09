package com.kenshoo.freemarker.platform;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: shlomis
 * Date: 9/2/13
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:spring/*-context.xml"
        ,"classpath:spring/bootstrap-security.xml"
        ,"classpath:spring/test-bootstrap-datasource-context.xml"
})
@Transactional
public class DatabaseBaseTest {

    @Autowired
    JdbcTemplate template;

    @Test
    public void testName() throws Exception {
        System.out.println(template.queryForObject("select DATABASE()", String.class));
    }

    @Test
    public void testContextOrder() throws Exception {
        System.out.println(System.getenv("database.dbName"));
    }
}
