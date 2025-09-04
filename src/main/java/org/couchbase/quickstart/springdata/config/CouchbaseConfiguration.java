package org.couchbase.quickstart.springdata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import com.couchbase.client.core.error.BucketNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.env.ClusterEnvironment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {

  @Value("#{systemEnvironment['DB_CONN_STR'] ?: '${spring.couchbase.connection-string:localhost}'}")
  private String host;

  @Value("#{systemEnvironment['DB_USERNAME'] ?: '${spring.couchbase.username:Administrator}'}")
  private String username;

  @Value("#{systemEnvironment['DB_PASSWORD'] ?: '${spring.couchbase.password:password}'}")
  private String password;

  // Since bucket auto-configuration is removed, we'll hardcode the travel-sample bucket name
  private String bucketName = "travel-sample";

  @Override
  public String getConnectionString() {
    return host;
  }

  @Override
  public String getUserName() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getBucketName() {
    return bucketName;
  }

  @Override
  public String typeKey() {
    return "type";
  }

  @Override
  @Bean(destroyMethod = "disconnect")
  public Cluster couchbaseCluster(ClusterEnvironment couchbaseClusterEnvironment) {
    try {
      log.debug("Connecting to Couchbase cluster at " + host);
      return Cluster.connect(getConnectionString(), getUserName(), getPassword());
    } catch (Exception e) {
      log.error("Error connecting to Couchbase cluster", e);
      throw e;
    }
  }

  @Bean
  public Bucket getCouchbaseBucket(Cluster cluster) {
    try {
      if (!cluster.buckets().getAllBuckets().containsKey(getBucketName())) {
        log.error("Bucket with name {} does not exist. Creating it now", getBucketName());
        throw new BucketNotFoundException(bucketName);
      }
      return cluster.bucket(getBucketName());
    } catch (Exception e) {
      log.error("Error getting bucket", e);
      throw e;
    }
  }

}