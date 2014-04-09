/*
* Copyright 2011 Kenshoo.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.kenshoo.freemarker.dropwizard;

import com.berico.fallwizard.SpringService;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.migrations.MigrationsBundle;

/**
 * User: dekely
 * Date: 3/17/13
 * Time: 10:39 AM
 */
public class ApplicationStartup extends SpringService<ApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new ApplicationStartup().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        bootstrap.setName("freemarker-online");
        bootstrap.addBundle(new MigrationsBundle<ApplicationConfiguration>() {
            @Override
            public DatabaseConfiguration getDatabaseConfiguration(ApplicationConfiguration configuration) {
                return configuration.getDatabaseConfiguration();
            }
        });

    }

    @Override
    public void run(ApplicationConfiguration configuration, Environment environment) throws Exception {
        // This is used to allow for overriding/using values from property files
        configuration.setSystemProperties();
        super.run(configuration, environment);
    }
}