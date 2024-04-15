# Animal Picture Generator Application Deployment Guide

## Overview

This guide outlines the steps necessary to deploy and run the Animal Picture Generator application, which provides REST endpoints for generating and retrieving animal pictures. 
The application leverages a BPMN (Business Process Model and Notation) process within a Camunda Zeebe cluster, demonstrating an integration between a Spring Boot application and a BPMN workflow engine.

Hence, you need access to a Camunda Zeebe cluster. 
You can create an account [here](https://camunda.com). 
To set up a cluster follow the instructions in the [Camunda Docs](https://docs.camunda.io/docs/guides/create-cluster/).

You can run the application as a standalone Spring Boot application, using Docker Compose, or deploy it to a Kubernetes cluster using Helm.

A short API description for generating and retrieving images can be found [here](#generating-and-retrieving-animal-pictures).

## Running the Application

### Run as a Standalone Spring Boot application

- JDK 21 installed.
- Maven installed.
- A running PostgreSQL database.

#### Configuration

Once you have access to a Camunda Zeebe cluster, configure the application to connect to it by setting the following properties in the `src/main/resources/application.properties` file within the `animal-picture-app` module:

- `zeebe.client.cloud.region`
- `zeebe.client.cloud.clusterId`
- `zeebe.client.cloud.clientId`
- `zeebe.client.cloud.clientSecret`

Additionally, set the database connection properties:

- `spring.datasource.url` - URL to your PostgreSQL database.
- `spring.datasource.username` and `spring.datasource.password` - Credentials for the database.

Example `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/animaldb
spring.datasource.username=animaluser
spring.datasource.password=animalpass
zeebe.client.cloud.region=bru-2
zeebe.client.cloud.clusterId=c7e5c022-6f75-491c-8f2b-997f2ec7b58c
zeebe.client.cloud.clientId=Y9A4bLqx26uFFMXpkoWPR1Kyl4MINaHQ
zeebe.client.cloud.clientSecret=xZTGeKHN4xmYoQ_SdiIOLplnDH~JaI9nXNqJaiDtdDl3vC2ehfDeNA9owaSX2a9S
```

#### Build and Run
Navigate to the `animal-picture-app` module and run the application:
   ```bash
   mvn spring-boot:run
   ```

### Run the Application with Docker Compose
You can also run the application using Docker Compose. 
Ensure you have Docker installed on your machine.
The Docker Compose configuration includes the application and a PostgreSQL database.

#### Configuration
Open the `docker-compose.yml` file in the root directory of the project and set the following environment variables:

- `ZEEBE_REGION`
- `ZEEBE_CLUSTER_ID` 
- `ZEEBE_CLIENT_ID`
- `ZEEBE_CLIENT_SECRET`

You can also set the database connection properties, however, the default values should work for the provided Docker Compose configuration.

#### Run the Application

Simply run the following command in the root directory of the project to start the application and database containers:

```bash
docker-compose up
```


### Run the Application on Kubernetes with Helm
You can deploy the application to a Kubernetes cluster using Helm. 
Ensure you have Helm [installed](https://helm.sh/docs/intro/install/) on your machine and a Kubernetes cluster or a local Kubernetes engine such as Minikube available.

#### Configuration
Open the `values.yaml` file in the `helm/animal-picture-app` directory and set the following values in the `env` section:

- `ZEEBE_REGION`
- `ZEEBE_CLUSTER_ID`
- `ZEEBE_CLIENT_ID`
- `ZEEBE_CLIENT_SECRET`

You can also set the database connection properties, however, the default values should work for the provided Helm chart.

#### Deploy and Run the Application
Run the following commands in the `animal-picture-app` folderto deploy the application to your Kubernetes cluster:

```bash
  helm install animal-picture-app-release ./helm
```

## Generating and Retrieving Animal Pictures
To generate the animal pictures perform a POST request to `http://localhost:8080/animal-picture/{animalType}`.
Where `animalType` can be `bear`, `cat`, or `dog`.

To retrieve the animal pictures perform a GET request to `http://localhost:8080/animal-picture/{animalType}`.

Please replace the base URL `http://localhost:8080` with your application's host and port.
Note, for Spring standalone applications and Docker Compose, the base URL is by default `http://localhost:8080`.

## Build the Docker Image
If you updated the application, you need to rebuild the Docker image:

```bash
/mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=wagerse/animal-picture-app
```

You can push the image to the Docker registry with the following command:

```bash
docker push wagerse/animal-picture-app
```



