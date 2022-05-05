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
    // To connect to capella:
    // - with ssl certificate validation:
    // return "couchbases://cb.jnym5s9gv4ealbe.cloud.couchbase.com"
    // - without ssl validation:
    // return "couchbases://cb.jnym5s9gv4ealbe.cloud.couchbase.com?tls=no_verify"
    // (replace cb.jnym5s9gv4ealbe.cloud.couchbase.com with your Capella cluster address)
    //
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

