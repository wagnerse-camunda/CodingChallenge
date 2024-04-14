package com.camunda.pictureapp.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.camunda.pictureapp.TestContainerConfig;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Import(TestContainerConfig.class)
@ZeebeSpringTest
@AutoConfigureMockMvc
public class AcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void testCatPictureGeneration() {
        mockMvc.perform(post("/animal-picture/cat")).andExpect(status().isCreated());

        var responseContent = retrievePicture("/animal-picture/cat");
        assertThat(responseContent).isNotEmpty();
    }

    @Test
    @SneakyThrows
    public void testBearPictureGeneration() {
        mockMvc.perform(post("/animal-picture/bear")).andExpect(status().isCreated());

        var responseContent = retrievePicture("/animal-picture/bear");
        assertThat(responseContent).isNotEmpty();
    }

    @Test
    @SneakyThrows
    public void testDogPictureGeneration() {
        mockMvc.perform(post("/animal-picture/dog")).andExpect(status().isCreated());

        var responseContent = retrievePicture("/animal-picture/dog");
        assertThat(responseContent).isNotEmpty();
    }

    private byte[] retrievePicture(String urlTemplate) throws Exception {
        return mockMvc.perform(get(urlTemplate))
            .andExpect(status().isOk())
            .andExpect(content().contentType("image/jpeg"))
            .andReturn()
            .getResponse()
            .getContentAsByteArray();
    }

}
