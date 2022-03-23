package trycb.config;

import com.couchbase.client.core.error.CollectionExistsException;
import com.couchbase.client.core.error.IndexExistsException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.collection.CollectionManager;
import com.couchbase.client.java.manager.collection.CollectionSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public final class DbSetupRunner implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DbSetupRunner.class);

  @Autowired
  private Bucket bucket;
  @Autowired
  private Cluster cluster;
  @Autowired
  private CouchbaseConfiguration config;

  @Override
  public void run(String... args) {
    try {
      // We must create primary index on our bucket in order to query it
      cluster.queryIndexes().createPrimaryIndex(config.getBucketName());
      LOGGER.info("Created primary index {}", config.getBucketName());
    } catch (IndexExistsException iee) {
      LOGGER.info("Primary index {} already exists", config.getBucketName());
    } catch (Exception e) {
      LOGGER.error("Failed to create primary index {}", config.getBucketName(), e);
      System.exit(1);
    }

    CollectionManager collectionManager = bucket.collections();
    try {
      // Making sure that profile collection exists
      CollectionSpec colspec = CollectionSpec.create(CouchbaseConfiguration.PROFILE_COLLECTION, "_default");
      collectionManager.createCollection(colspec);
      LOGGER.info("Created collection {}", CouchbaseConfiguration.PROFILE_COLLECTION);
    } catch (CollectionExistsException e) {
      LOGGER.info("Collection {} already exists", CouchbaseConfiguration.PROFILE_COLLECTION);
    } catch (Exception e) {
      LOGGER.error("Failed to create collection {}", CouchbaseConfiguration.PROFILE_COLLECTION, e);
      System.exit(1);
    }

    try {
      // primary index for querying profiles by id
      final String query = "CREATE PRIMARY INDEX default_profile_index ON " + config.getBucketName() + "._default." + CouchbaseConfiguration.PROFILE_COLLECTION;
      LOGGER.info("Creating default_profile_index: {}", query);
      cluster.query(query);
      Thread.sleep(1000);
      LOGGER.info("Created primary index on collection {}", CouchbaseConfiguration.PROFILE_COLLECTION);
    } catch (IndexExistsException e) {
      LOGGER.info("Primary index exists on collection {}", CouchbaseConfiguration.PROFILE_COLLECTION);
    } catch (Exception e) {
      LOGGER.error("Failed to create primary index on collection {}", CouchbaseConfiguration.PROFILE_COLLECTION, e);
    }

    try {
      // secondary index for querying profiles by fields
      final String query = "CREATE INDEX secondary_profile_index ON " + config.getBucketName() + "._default." + CouchbaseConfiguration.PROFILE_COLLECTION + "(firstName, lastName, address)";
      LOGGER.info("Creating secondary_profile_index: {}", query);
      cluster.query(query);
      Thread.sleep(1000);
      LOGGER.info("Created secondary index on collection {}", CouchbaseConfiguration.PROFILE_COLLECTION);
    } catch (IndexExistsException e) {
      LOGGER.info("Secondary index exists on collection {}", CouchbaseConfiguration.PROFILE_COLLECTION);
    } catch (Exception e) {
      LOGGER.error("Failed to create secondary index on collection {}", CouchbaseConfiguration.PROFILE_COLLECTION, e);
    }
  }
}
