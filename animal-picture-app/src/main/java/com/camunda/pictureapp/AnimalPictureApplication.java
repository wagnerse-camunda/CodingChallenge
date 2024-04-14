package com.camunda.pictureapp;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = "classpath:process/animal-picture-process.bpmn")
public class AnimalPictureApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalPictureApplication.class, args);
    }

}
