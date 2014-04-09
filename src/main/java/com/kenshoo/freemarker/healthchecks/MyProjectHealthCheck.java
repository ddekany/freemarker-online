package com.kenshoo.freemarker.healthchecks;

import com.yammer.metrics.core.HealthCheck;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: tzachz
 * Date: 5/23/13
 */
@Component
public class MyProjectHealthCheck extends HealthCheck {

    // note that this is due to the default spring CTR
    public MyProjectHealthCheck() {
        super("MyProjectHealthCheck");
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy(); // we're always healthy!
    }
}
