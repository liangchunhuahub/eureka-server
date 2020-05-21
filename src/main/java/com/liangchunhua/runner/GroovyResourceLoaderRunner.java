package com.liangchunhua.runner;

import com.liangchunhua.groovy.GroovyResourceLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class GroovyResourceLoaderRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        String groovyPath = System.getProperty("user.dir");
        System.out.println("groovyPath = " + groovyPath);
        GroovyResourceLoader.loadResource(groovyPath + "/groovyScript");
    }
}
