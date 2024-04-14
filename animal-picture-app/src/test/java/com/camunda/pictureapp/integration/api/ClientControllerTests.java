package com.camunda.pictureapp.integration.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.camunda.pictureapp.TestContainerConfig;
import com.camunda.pictureapp.model.AnimalType;
import com.camunda.pictureapp.persistence.PictureEntity;
import com.camunda.pictureapp.persistence.PictureRepository;
import com.camunda.pictureapp.workflow.ZeebeClientService;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestContainerConfig.class)
public class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ZeebeClientService zeebeeClientService;

    @Autowired
    private ResourceLoader resourceLoader;

    @MockBean
    private PictureRepository pictureRepository;

    @Test
    void shouldExecuteProcessWithAnimalType() throws Exception {
        mockMvc.perform(post("/animal-picture/cat")).andExpect(status().isCreated());

        ArgumentCaptor<AnimalType> animalTypeCapture = ArgumentCaptor.forClass(AnimalType.class);
        verify(zeebeeClientService, times(1)).executeImageGenerationProcess(animalTypeCapture.capture());
        assertThat(animalTypeCapture.getValue()).isEqualTo(AnimalType.CAT);
    }

    @Test
    void shouldNotExecuteProcessWithInvalidAnimalType() throws Exception {
        mockMvc.perform(post("/animal-picture/elephant"))
            .andExpect(status().isBadRequest());

        verify(zeebeeClientService, times(0)).executeImageGenerationProcess(any());
    }

    @Test
    void shouldRetrieveAnimalPicture() throws Exception {
        byte[] expectedContent = Files.readAllBytes(Paths.get(resourceLoader.getResource("classpath:picture/bear.jpeg").getURI()));
        var bear = PictureEntity.builder().data(expectedContent).type(AnimalType.BEAR).build();

        when(pictureRepository.findFirstByTypeOrderByCreatedAtDesc(any())).thenReturn(Optional.of(bear));

        // Perform the request and capture the response
        byte[] responseContent = mockMvc.perform(get("/animal-picture/bear"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("image/jpeg"))
            .andReturn()
            .getResponse()
            .getContentAsByteArray();

        // Assert that the response matches the expected content
        assertThat(responseContent).isEqualTo(expectedContent);
    }

    @Test
    void shouldReturnNotFoundForNonExistingPicture() throws Exception {
        when(pictureRepository.findById(any())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/animal-pictures/cat"))
            .andExpect(status().isNotFound());
    }
}
