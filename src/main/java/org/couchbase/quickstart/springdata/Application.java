package org.couchbase.quickstart.springdata;

import static org.couchbase.quickstart.springdata.config.SpringDocConstants.DESCRIPTION;
import static org.couchbase.quickstart.springdata.config.SpringDocConstants.TITLE;
import static org.couchbase.quickstart.springdata.config.SpringDocConstants.VERSION;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;

/**
 * This example application demonstrates using
 * Spring Data with Couchbase.
 **/

@Slf4j
@SpringBootApplication(exclude = SecurityAutoConfiguration.class, proxyBeanMethods = false)
@OpenAPIDefinition(info = @Info(title = TITLE, version = VERSION, description = DESCRIPTION))
public class Application implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("Application started successfully");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

}
