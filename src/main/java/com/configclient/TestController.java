package com.configclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RefreshScope
@RestController
public class TestController {


    @Autowired
    private Environment environment;

    @GetMapping("/profiles")
    public String getActiveProfiles() {


        return "key1 = " +environment.getProperty("key1") +" key2 = " +environment.getProperty("key2");
    }
}
