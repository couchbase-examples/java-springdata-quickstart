# Quickstart in Couchbase with Spring Data and Java

#### REST API using Couchbase Capella in Java using Spring Data

Often, the first step developers take after creating their database is to create a REST API that can perform Create, Read, Update, and Delete (CRUD) operations for that database. This repo is designed to teach you and give you a starter project (in Java using Spring Data) to generate such a REST API. After you have installed the travel-sample bucket in your database, you can run this application which is a REST API with Swagger documentation so that you can learn:

1. How to create, read, update, and delete documents using Key-Value[ operations](https://docs.couchbase.com/java-sdk/current/howtos/kv-operations.html) (KV operations). KV operations are unique to Couchbase and provide super fast (think microseconds) queries.
2. How to write simple parametrized [N1QL queries](https://docs.couchbase.com/java-sdk/current/howtos/n1ql-queries-with-sdk.html) using the built-in travel-sample bucket.

Full documentation for the tutorial can be found on the [Couchbase Developer Portal](https://developer.couchbase.com/tutorial-quickstart-spring-data-java/).

## Prerequisites

To run this prebuilt project, you will need:

- [Couchbase Capella](https://www.couchbase.com/products/capella/) cluster with [travel-sample](https://docs.couchbase.com/java-sdk/current/ref/travel-app-data-model.html) bucket loaded.
  - To run this tutorial using a self-managed Couchbase cluster, please refer to the [appendix](#running-self-managed-couchbase-cluster).
- [Java 1.8 or higher](https://www.oracle.com/java/technologies/javase-downloads.html)
  - Ensure that the Java version is compatible with the Couchbase SDK.
- Loading Travel Sample Bucket
  If travel-sample is not loaded in your Capella cluster, you can load it by following the instructions for your Capella Cluster:
  - [Load travel-sample bucket in Couchbase Capella](https://docs.couchbase.com/cloud/clusters/data-service/import-data-documents.html#import-sample-data)
- Gradle
  - You can install Gradle using the [instructions](https://gradle.org/install/).
## App Setup

We will walk through the different steps required to get the application running.

### Cloning Repo

```shell
git clone https://github.com/couchbase-examples/java-springboot-quickstart
```

### Install Dependencies

The dependencies for the application are specified in the `build.gradle` file in the source folder. Dependencies can be installed through `gradle` the default package manager for Java.

```
gradle build -x test
```

Note: The `-x test` option is used to skip the tests. The tests require the application to be running.

Note: The application is tested with Java 17. If you are using a different version of Java, please update the `build.gradle` file accordingly.

### Setup Database Configuration

To learn more about connecting to your Capella cluster, please follow the [instructions](https://docs.couchbase.com/cloud/get-started/connect.html).

Specifically, you need to do the following:

- Create the [database credentials](https://docs.couchbase.com/cloud/clusters/manage-database-users.html) to access the travel-sample bucket (Read and Write) used in the application.
- [Allow access](https://docs.couchbase.com/cloud/clusters/allow-ip-address.html) to the Cluster from the IP on which the application is running.

All configuration for communication with the database is read from the environment variables. We have provided a convenience feature in this quickstart to read the environment variables from a local file, `application.properties` in the `src/main/resources` folder.

```properties
server.use-forward-headers=true
server.forward-headers-strategy=framework
spring.couchbase.bootstrap-hosts=DB_CONN_STR
spring.couchbase.bucket.name=travel-sample
spring.couchbase.bucket.user=DB_USERNAME
spring.couchbase.bucket.password=DB_PASSWORD
spring.couchbase.scope.name=inventory
spring.mvc.pathmatch.matching-strategy=ANT_PATH_MATCHER
```

Instead of the hash symbols, you need to add the values for the Couchbase connection.

> Note: The connection string expects the `couchbases://` or `couchbase://` part.


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

## Let's Review the Code

### Airline Model

The `Airline` model is a simple POJO (Plain Old Java Object) that is used to represent the airline document in the travel-sample bucket. The `@Id` annotation is used to specify the document ID in the bucket. The `@Field` annotation is used to specify the field name in the document. The `@TypeAlias` annotation is used to specify the type of the document in the bucket.

```java
@Document
@Scope("inventory")
@Collection("airline")
public class Airline {

  @Id
  private String id;

  @Field("callsign")
  private String callsign;

  @Field("country")
  private String country;

  @Field("iata")
  private String iata;

  @Field("icao")
  private String icao;

  @Field("name")
  private String name;

  @Field("type")
  private String type;

  @Field("active")
  private boolean active;

  ...
```
> *from model/Airline.java*

The `@Document` annotation is used to specify that this class is a document in the bucket. The `@Scope` annotation is used to specify the scope of the document. The `@Collection` annotation is used to specify the collection of the document.

The `@Id` annotation is used to specify the document ID in the bucket. The `@Field` annotation is used to specify the field name in the document. The `@TypeAlias` annotation is used to specify the type of the document in the bucket.

You can find more information on key generation in the [Connector Documentation](https://docs.spring.io/spring-data/couchbase/docs/current/reference/html/#couchbase.autokeygeneration).

Couchbase Spring Data connector will automatically serialize model instances into JSON when storing them on the cluster.

## Document Structure

The `Airline` document is stored in the `airline` collection in the `travel-sample` bucket. The document has the following structure:

```json
{
  "callsign": "AMERICAN",
  "country": "United States",
  "iata": "AA",
  "icao": "AAL",
  "id": 10,
  "name": "American Airlines",
  "type": "airline",
  "active": true
}
```


## Running The Application

### Directly on Machine

At this point, we have installed the dependencies, loaded the travel-sample data and configured the application with the credentials. The application is now ready and you can run it.

```sh
gradle bootRun
```

Note: If you're using Windows, you can run the application using the `gradle.bat` executable.

```sh
./gradew.bat bootRun
```

### Using Docker

Build the Docker image

```sh
docker build -t java-springdata-quickstart .
```

Run the Docker image

```sh
docker run -d --name springdata-container -p 8080:8080 java-springdata-quickstart
```

Note: The `application.properties` file has the connection information to connect to your Capella cluster. These will be part of the environment variables in the Docker container.

### Verifying the Application

Once the application starts, you can see the details of the application on the logs.

![Application Startup](app_startup.png)

The application will run on port 8080 of your local machine (http://localhost:8080). You will find the interactive Swagger documentation of the API if you go to the URL in your browser. Swagger documentation is used in this demo to showcase the different API endpoints and how they can be invoked. More details on the Swagger documentation can be found in the [appendix](#swagger-documentation).

![Swagger Documentation](swagger_documentation.png)

## Running Tests

To run the integration tests, use the following commands:

```sh
gradle test
```

## Appendix

### Data Model

For this quickstart, we use three collections, `airport`, `airline` and `routes` that contain sample airports, airlines and airline routes respectively. The routes collection connects the airports and airlines as seen in the figure below. We use these connections in the quickstart to generate airports that are directly connected and airlines connecting to a destination airport. Note that these are just examples to highlight how you can use SQL++ queries to join the collections.

![travel-sample data model](travel_sample_data_model.png)

### Extending API by Adding New Entity

If you would like to add another entity to the APIs, these are the steps to follow:

- Create the new entity (collection) in the Couchbase bucket. You can create the collection using the [SDK](https://docs.couchbase.com/sdk-api/couchbase-java-client-3.5.2/com/couchbase/client/java/Collection.html#createScope-java.lang.String-) or via the [Couchbase Server interface](https://docs.couchbase.com/cloud/n1ql/n1ql-language-reference/createcollection.html).
- Define the routes in a new file in the `controllers` folder similar to the existing routes like `AirportController.java`.
- Define the service in a new file in the `services` folder similar to the existing services like `AirportService.java`. 
- Define the repository in a new file in the `repositories` folder similar to the existing repositories like `AirportRepository.java`.

### Running Self-Managed Couchbase Cluster

If you are running this quickstart with a self-managed Couchbase cluster, you need to [load](https://docs.couchbase.com/server/current/manage/manage-settings/install-sample-buckets.html) the travel-sample data bucket in your cluster and generate the credentials for the bucket.

You need to update the connection string and the credentials in the `application.properties` file in the `src/main/resources` folder.

Note: Couchbase Server version 7 or higher must be installed and running before running the Spring Boot Java app.

### Swagger Documentation

Swagger documentation provides a clear view of the API including endpoints, HTTP methods, request parameters, and response objects.

Click on an individual endpoint to expand it and see detailed information. This includes the endpoint's description, possible response status codes, and the request parameters it accepts.

#### Trying Out the API

You can try out an API by clicking on the "Try it out" button next to the endpoints.

- Parameters: If an endpoint requires parameters, Swagger UI provides input boxes for you to fill in. This could include path parameters, query strings, headers, or the body of a POST/PUT request.

- Execution: Once you've inputted all the necessary parameters, you can click the "Execute" button to make a live API call. Swagger UI will send the request to the API and display the response directly in the documentation. This includes the response code, response headers, and response body.

#### Models

Swagger documents the structure of request and response bodies using models. These models define the expected data structure using JSON schema and are extremely helpful in understanding what data to send and expect.

## Conclusion
Setting up a basic REST API in Spring Data with Couchbase is fairly simple.  This project, when run with Couchbase Server 7 installed creates a collection in Couchbase, an index for our parameterized [N1QL query](https://docs.couchbase.com/java-sdk/current/howtos/n1ql-queries-with-sdk.html), and showcases basic CRUD operations needed in most applications.
