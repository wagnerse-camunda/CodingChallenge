package com.camunda.pictureapp.workflow;

import com.camunda.pictureapp.model.AnimalType;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ZeebeClientService {

    private final ZeebeClient client;

    public void executeImageGenerationProcess(AnimalType animal) {
        log.info("Starting process instance ...");
        var variables = ProcessInputVariable.builder().animal(animal.getType()).build();
        var processInstanceResult =
            client
                .newCreateInstanceCommand()
                .bpmnProcessId("AnimalPictureProcess")
                .latestVersion()
                .variables(variables)
                .withResult()
                .send()
                .join();

        log.info("Process instance {} completed successfully.", processInstanceResult.getProcessInstanceKey());
    }
}
