package org.couchbase.quickstart.springdata.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotEnvConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        
        try {
            // Load .env file if it exists
            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            // Create a property source from .env entries
            Map<String, Object> envMap = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                
                // Only add if not already set by system environment
                if (System.getenv(key) == null) {
                    envMap.put(key, value);
                    System.out.println("Loaded from .env: " + key);
                }
            });
            
            if (!envMap.isEmpty()) {
                environment.getPropertySources().addFirst(new MapPropertySource("dotenv", envMap));
                System.out.println("Environment variables loaded from .env file: " + envMap.keySet());
            }
            
        } catch (Exception e) {
            System.err.println("Could not load .env file: " + e.getMessage());
        }
    }
}