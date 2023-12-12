package org.couchbase.quickstart.springdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * This example application demonstrates using
 * Spring Data with Couchbase. 
 **/
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

  @Bean
  ForwardedHeaderFilter forwardedHeaderFilter() {
     return new ForwardedHeaderFilter();
  }
}
