package org.couchbase.quickstart.springdata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;

@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {

  @Value("${spring.couchbase.bootstrap-hosts}")
  private String host;

  @Value("${spring.couchbase.bucket.user}")
  private String username;

  @Value("${spring.couchbase.bucket.password}")
  private String password;

  @Value("${spring.couchbase.bucket.name}")
  private String bucket;


  @Override
  public String getConnectionString() {
    // To connect to capella:
    // - with ssl certificate validation:
    // return "couchbases://cb.jnym5s9gv4ealbe.cloud.couchbase.com"
    // - without ssl validation:
    // return "couchbases://cb.jnym5s9gv4ealbe.cloud.couchbase.com?tls=no_verify"
    // (replace cb.jnym5s9gv4ealbe.cloud.couchbase.com with your Capella cluster
    // address)

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
    return bucket;
  }

  @Override
  public String typeKey() {
    return "type";
  }

  @Bean
  public Bucket getCouchbaseBucket(Cluster cluster){
    // verify that bucket exists
    if (!cluster.buckets().getAllBuckets().containsKey(getBucketName())) {
      // create the bucket if it doesn't
      cluster.buckets().createBucket(
          BucketSettings.create(getBucketName())
              .bucketType(BucketType.COUCHBASE)
              .minimumDurabilityLevel(DurabilityLevel.NONE)
              .ramQuotaMB(128));
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
      }
    } 
    return cluster.bucket(getBucketName());
  }
}