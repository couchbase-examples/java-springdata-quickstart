[![Try it now!](https://da-demo-images.s3.amazonaws.com/runItNow_outline.png?couchbase-example=java-springdata-quickstart-repo&source=github)](https://gitpod.io/#https://github.com/couchbase-examples/java-springdata-quickstart)

## Overview
This quickstart tutorial will review the basics of using Couchbase by building a simple Spring Data REST API that stores user profiles is used as an example.

## What We'll Cover
- [Cluster Connection Configuration](#cluster-connection-configuration) – Configuring Spring Data to connect to a Couchbase cluster.
- [Database Initialization](#database-initialization) – Creating required database structures upon application startup
- [CRUD operations](#create-or-update-a-profile) – Standard create, update and delete operations.
- [Custom SQL++ queries](#search-profiles-by-text) – Using [SQl++](https://www.couchbase.com/sqlplusplus) with Spring Data.

## Useful Links
- [Spring Data Couchbase - Reference Documentation](https://docs.spring.io/spring-data/couchbase/docs/current/reference/html/)
- [Spring Data Couchbase - JavaDoc](https://docs.spring.io/spring-data/couchbase/docs/current/api/)

## Prerequisites
To run this prebuild project, you will need:
- [Couchbase Capella](https://docs.couchbase.com/cloud/get-started/create-account.html) account or locally installed [Couchbase Server](/tutorial-couchbase-installation-options)
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- Code Editor or an Integrated Development Environment (e.g., [Eclipse](https://www.eclipse.org/ide/))
- [Java SDK v1.8 or higher installed](https://www.oracle.com/java/technologies/ee8-install-guide.html)
- [Gradle Build Tool](https://gradle.org/install/)

## Source Code
The sample source code used in this tutorial is [published on GitHub](https://github.com/couchbase-examples/java-springboot-quickstart).
To obtain it, clone the git repository with your IDE or execute the following command:
```shell
git clone https://github.com/couchbase-examples/java-springdata-quickstart
```
## Dependencies
Gradle dependencies:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'
// spring data couchbase connector
implementation 'org.springframework.boot:spring-boot-starter-data-couchbase'
// swagger ui
implementation 'org.springdoc:springdoc-openapi-ui:1.6.6'
```

Maven dependencies:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-couchbase</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-ui</artifactId>
  <version>1.6.6</version>
</dependency>
```

## Cluster Connection Configuration
Spring Data couchbase connector can be configured by providing a `@Configuration` [bean](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-definition) that extends [`AbstractCouchbaseConfiguration`](https://docs.spring.io/spring-data/couchbase/docs/current/api/org/springframework/data/couchbase/config/AbstractCouchbaseConfiguration.html).
The sample application provides a configuration bean that uses default couchbase login and password:
```java
@Configuration
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {

  @Override
  public String getConnectionString() {
    // capella
    // return "couchbases://cb.jnym5s9gv4ealbe.cloud.couchbase.com";
    
    // localhost
    return "127.0.0.1"
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

  ...
```
> *from config/CouchbaseConfiguration.java*

This default configuration assumes that you have a locally running Couchbae server and uses standard administrative login and password for demonstration purpose.
Applications deployed to production or staging environments should use less privileged credentials created using [Role-Based Access Control](https://docs.couchbase.com/go-sdk/current/concept-docs/rbac.html).
Please refer to [Managing Connections using the Java SDK with Couchbase Server](https://docs.couchbase.com/java-sdk/current/howtos/managing-connections.html) for more information on Capella and local cluster connections.

# Running the Application

To install dependencies and run the application on Linux, Unix or OS X, execute `./gradlew bootRun` (`./gradew.bat bootRun` on Windows).

Once the site is up and running, you can launch your browser and go to the [Swagger Start Page](http://localhost:8080/swagger-ui/) to test the APIs.


## Document Structure
We will be setting up a REST API to manage demo user profiles and store them as documents on a Couchbase Cluster. Every document needs a unique identifier with which it can be addressed in the API. We will use auto-generated UUIDs for this purpose and store in profile documents the first and the last name of the user, their age, and address:

```json
{
  "id": "b181551f-071a-4539-96a5-8a3fe8717faf",
  "firstName": "John",
  "lastName": "Doe",
  "age": "35",
  "address": "123 Main St"
}
```

## Let's Review the Code

### Profile Model
To work with submitted profiles, we first need to model their structure in a Java class, which would define the set of profile fields and their types.
In our sample application, this is done in [`model/Profile.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/trycb/model/Profile.java) class:

```java
@Scope("_default")
@Collection("profile")
public class Profile {
  @id
  @GeneratedValue 
  private UUID id;
  private String firstName, lastName;
  private byte age;
  private String address;

  // ...
}
```
> from `model/Profile.java`

The whole model is annotated with `@Scope` and `@Collection` annotations, which configure Spring Data to store model instances into `profile` collection in the default scope.

It is also worth noting the use of `@Id` and `@GeneratedValue` annotations on `Profile::id` field.

In couchbase, data is stored as JSON documents; each document has a unique identifier that can be used to address that document. 
Every profile instance in our example corresponds to a single document and this annotation is used here to link the document's id to a java field. 
Additionally, the `@GeneratedValue` annotation on the field instructs Spring Data to generate a random UUID if we try to store a profile without one, which will come in handy later.

You can find more information on key generation in the [Connector Documentation](https://docs.spring.io/spring-data/couchbase/docs/current/reference/html/#couchbase.autokeygeneration).

Couchbase Spring Data connector will automatically serialize model instances into JSON when storing them on the cluster.

### Database initialization
Automated database initialization and migration is a common solution that simplifies database management operations.
To keep it simple, our demo uses [DbSetupRunner](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/trycb/config/DbSetupRunner.java) component class that implements `CommandLineRunner` interface and is invoked every time the application starts.
The runner tries to create required structure every startup but ignores any errors if such structure already exists.
For example, this code creates a primary index for configured bucket:
```java
    try {
      // We must create primary index on our bucket in order to query it
      cluster.queryIndexes().createPrimaryIndex(config.getBucketName());
      LOGGER.info("Created primary index {}", config.getBucketName());
    } catch (IndexExistsException iee) {
      LOGGER.info("Primary index {} already exists", config.getBucketName());
    }
```
> From `config/DbSetupRunner.java`

Primary indexes in Couchbase contain all document keys and are used to fetch documents by their unique identifiers. 
Secondary indexes can be used to efficiently query documents by their properties.
For example, `DbSetupRunner` creates additional indexes on the collection that allow querying profiles by first or last names or addresses:
```java
    try {
      final String query = "CREATE INDEX secondary_profile_index ON " + config.getBucketName() + "._default." + CouchbaseConfiguration.PROFILE_COLLECTION + "(firstName, lastName, address)";
      cluster.query(query);
    } catch (IndexExistsException e) {
      LOGGER.info("Secondary index exists on collection {}", CouchbaseConfiguration.PROFILE_COLLECTION);
    }
```
> From `config/DbSetupRunner.java`

More information on working with Couchbase indexes can be found [in our documentation](https://docs.couchbase.com/server/current/learn/services-and-indexes/indexes/global-secondary-indexes.html).

### Create or update a Profile
For CRUD operations, we will extend [`PagingAndSortingRepository`](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html) provided by the framework:

```java
@Repository
public interface ProfileRepository extends PagingAndSortingRepository<Profile, UUID> {
  @Query("#{#n1ql.selectEntity} WHERE firstName LIKE '%' || $1 || '%' OR lastName LIKE '%' || $1 || '%' OR address LIKE '%' || $1 || '%' OFFSET $2 * $3 LIMIT $3")
  List<Profile> findByText(String query, int pageNum, int pageSize);

  Page<Profile> findByAge(byte age, Pageable pageable);
}
```
> From `repository/ProfileRepository.java`

Open the [`ProfileController`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/trycb/controller/ProfileController.java) class located in `controller` package and navigate to the `saveProfile` method.
This method accepts `Profile` objects deserialized by Spring Web from the body of an HTTP request.

```java
  @PostMapping("/profile")
  public ResponseEntity<Profile> saveProfile(@RequestBody Profile profile) {
    // the same endpoint can be used to create and save the object
    profile = profileRepository.save(profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(profile);
  }
```
> *from `saveProfile` method of `controller/ProfileController.java`*

This object can be modified according to business requirements and then saved directly into the database using `ProfileRepository::save` method.
Because we used `@GeneratedValue` annotation on `id` field of our java model, Spring Data will automatically generate a document id when it is missing from the request. This allows clients to use `/profile` endpoint both to update existing profiles and create new records.
To achieve this, a client needs to submit a Profile object without the id field.

### Get Profile by Key
Navigate to the `getProfileById` method in [`ProfileController`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/trycb/controller/ProfileController.java) class.
This method handles client requests to retrieve a single profile by its unique id.
Sent by the client UUID is passed to the standard `findById` method of `ProfileRepository`, which returns an `Optional` with requested profile:

```java
Profile result = profileRepository.findById(id).orElse(null);
```
> *from getProfileById method of controller/ProfileController.java*

### Search profiles by text
Although Couchbase provides [powerful full-text search capabilities out of the box](https://www.couchbase.com/products/full-text-search), in this demo we use classic `LIKE` query for our profile search endpoint.
Navigate to `listProfiles` method of [Profile Controller](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/trycb/controller/ProfileController.java). 
The endpoint uses customized `findByText` method of [Profile Repository](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/trycb/repository/ProfileRepository.java):
```java
result = profileRepository.findByText(query, pageRequest).toList();
```
> *from `listProfiles` method in `controller/ProfileController.java`*

The `ProfileRepository::findByQueryMethod` is generated automatically using provided in `@Query` annotation [SpEL](https://docs.spring.io/spring-integration/docs/5.3.0.RELEASE/reference/html/spel.html) template in SQL++:
```java
  @Query("#{#n1ql.selectEntity} WHERE firstName LIKE '%' || $1 || '%' OR lastName LIKE '%' || $1 || '%' OR address LIKE '%' || $1 || '%'")
  Page<Profile> findByQuery(String query, Pageable pageable);
```
> *definition of `findByQuery` method in `repository/ProfileRepository.java`*

You can find out more about SQL++ in Spring Data in the [connector documentation](https://docs.spring.io/spring-data/couchbase/docs/current/reference/html/#couchbase.repository.querying).

### DELETE Profile
Navigate to the `deleteProfile` method in the [Profile Controller](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/trycb/controller/ProfileController.java).
We only need the `Key` or id from the user to remove a document using a basic key-value operation.

```java
      profileRepository.deleteById(id);
```

> *from `deleteProfile` method of controller/ProfileController.java*


## Conclusion
Setting up a basic REST API in Spring Data with Couchbase is fairly simple.  This project, when run with Couchbase Server 7 installed creates a collection in Couchbase, an index for our parameterized [N1QL query](https://docs.couchbase.com/java-sdk/current/howtos/n1ql-queries-with-sdk.html), and showcases basic CRUD operations needed in most applications.
