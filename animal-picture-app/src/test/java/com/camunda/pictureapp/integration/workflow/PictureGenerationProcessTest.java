package com.camunda.pictureapp.integration.workflow;

import static io.camunda.zeebe.spring.test.ZeebeTestThreadSupport.waitForProcessInstanceCompleted;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.camunda.pictureapp.service.PictureService;
import com.camunda.pictureapp.TestContainerConfig;
import com.camunda.pictureapp.model.AnimalType;
import com.camunda.pictureapp.workflow.ProcessInputVariable;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;


@SpringBootTest
@Import(TestContainerConfig.class)
@ZeebeSpringTest
public class PictureGenerationProcessTest {

    @Autowired
    private ZeebeClient zeebeClient;

    @MockBean
    private PictureService pictureService;


    @Test
    public void testPictureGeneration() {
        var inputVar = ProcessInputVariable.builder().animal(AnimalType.CAT.getType()).build();
        var processInstance =
            zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId("AnimalPictureProcess")
                .latestVersion()
                .variables(inputVar)
                .send()
                .join();

        waitForProcessInstanceCompleted(processInstance);

        // verify that the picture service was called from the worker with the correct arguments
        ArgumentCaptor<AnimalType> animalTypeCapture = ArgumentCaptor.forClass(AnimalType.class);
        ArgumentCaptor<String> urlCapture = ArgumentCaptor.forClass(String.class);
        verify(pictureService, times(1)).fetchPicture(animalTypeCapture.capture(), urlCapture.capture());
        assertThat(animalTypeCapture.getValue()).isEqualTo(AnimalType.CAT);
        assertThat(urlCapture.getValue()).isEqualTo("https://placekitten.com/200/300");
    }

}
