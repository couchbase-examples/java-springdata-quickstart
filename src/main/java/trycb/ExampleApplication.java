package trycb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * This example application demonstrates using
 * Spring Data with Couchbase. 
 **/
@SpringBootApplication
public class ExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleApplication.class, args);
	}

  @Bean
  ForwardedHeaderFilter forwardedHeaderFilter() {
     return new ForwardedHeaderFilter();
  }
}
