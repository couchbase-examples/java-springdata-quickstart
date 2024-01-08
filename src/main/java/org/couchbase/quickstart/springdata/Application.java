package org.couchbase.quickstart.springdata;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
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
@OpenAPIDefinition(info = @Info(title = "Quickstart in Couchbase with Spring Data", version = "2.0", description = "<html><body><h2>A quickstart API using Java and Spring Data with Couchbase and travel-sample data</h2><p>We have a visual representation of the API documentation using Swagger which allows you to interact with the API's endpoints directly through the browser. It provides a clear view of the API including endpoints, HTTP methods, request parameters, and response objects.</p><p>Click on an individual endpoint to expand it and see detailed information. This includes the endpoint's description, possible response status codes, and the request parameters it accepts.</p><p><strong>Trying Out the API</strong></p><p>You can try out an API by clicking on the \"Try it out\" button next to the endpoints.</p><ul><li><strong>Parameters:</strong> If an endpoint requires parameters, Swagger UI provides input boxes for you to fill in. This could include path parameters, query strings, headers, or the body of a POST/PUT request.</li><li><strong>Execution:</strong> Once you've inputted all the necessary parameters, you can click the \"Execute\" button to make a live API call. Swagger UI will send the request to the API and display the response directly in the documentation. This includes the response code, response headers, and response body.</li></ul><p><strong>Models</strong></p><p>Swagger documents the structure of request and response bodies using models. These models define the expected data structure using JSON schema and are extremely helpful in understanding what data to send and expect.</p><p>For details on the API, please check the tutorial on the Couchbase Developer Portal: <a href=\"https://developer.couchbase.com/tutorial-quickstart-java-spring-boot\">https://developer.couchbase.com/tutorial-quickstart-java-spring-boot</a></p></body></html>"))
@EnableCouchbaseRepositories(basePackages = "org.couchbase.quickstart.springdata.repository")
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
