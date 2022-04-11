package trycb.config;

import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

@Configuration
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {
  public static final String PROFILE_COLLECTION = "profile";

  @Override
  public String getConnectionString() {
    return "couchbase://127.0.0.1";
  }

  @Override
  public String getUserName() {
    return "Administrator";
  }

  @Override
  public String getPassword() {
    return "password";
  }

  @Override
  public String getBucketName() {
    return "springdata_quickstart";
  }

  @Bean
  public Bucket getCouchbaseBucket(Cluster cluster) throws Exception {
    // verify that bucket exists
    if (!cluster.buckets().getAllBuckets().containsKey(getBucketName())) {
      // create the bucket if it doesn't
      cluster.buckets().createBucket(
        BucketSettings.create(getBucketName())
          .bucketType(BucketType.COUCHBASE)
          .minimumDurabilityLevel(DurabilityLevel.NONE)
          .ramQuotaMB(128)
      );
      Thread.sleep(1000);
    } 

    return cluster.bucket(getBucketName());
  }
}

