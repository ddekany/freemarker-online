package com.kenshoo.freemarker.healthchecks;

import com.yammer.metrics.core.HealthCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: shlomis
 * Date: 9/2/13
 * Time: 10:48 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DatabaseHealthCheck extends HealthCheck {

    @Autowired
    JdbcTemplate template;

    protected DatabaseHealthCheck() {
        super("Database health check");
    }

    @Override
    protected Result check() throws Exception {
        String dbName = template.queryForObject("select DATABASE()",String.class);
        return Result.healthy("Connected to database:" + dbName);
    }
}
