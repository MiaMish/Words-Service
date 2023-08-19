package com.mia.decool.service.words;

import com.mia.decool.service.words.util.StartupHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class SpringBootTemplateApplication {

    public static void main(String[] args) {
        Environment env = new SpringApplication(SpringBootTemplateApplication.class)
                .run(args)
                .getEnvironment();
        StartupHelper.logApplicationStartup(env);
    }
}
