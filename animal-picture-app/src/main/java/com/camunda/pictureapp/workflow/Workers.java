package com.camunda.pictureapp.workflow;

import com.camunda.pictureapp.model.AnimalType;
import com.camunda.pictureapp.service.PictureService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Workers {

    private final PictureService pictureGenerationService;

    @JobWorker(type = "generate-picture")
    public void generatePicture(final JobClient client, final ActivatedJob job, @Variable String animalType, @Variable String url) {
        pictureGenerationService.fetchPicture(AnimalType.valueOf(animalType.toUpperCase()), url);
    }
}
