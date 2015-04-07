package com.example;

import com.example.core.User;
import com.example.resources.UserResource;

import com.example.security.ExampleAuthenticator;
import com.example.security.ExampleSecurityProvider;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ExampleService extends Application<ExampleConfiguration> {

    public static void main(String[] args) throws Exception
    {
        new com.example.ExampleService().run(args);
    }

    @Override
    public void initialize(Bootstrap bootstrap) {
        //bootstrap.setName("dropwizard-security");
    }

    @Override
    public void run(ExampleConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new UserResource());

        // Adds security provider so resource methods decorated with auth attribute will use this authenticator
        environment.jersey().register(new ExampleSecurityProvider<User>(new ExampleAuthenticator()));
    }
}
