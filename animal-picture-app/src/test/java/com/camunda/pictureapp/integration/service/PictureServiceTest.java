package com.camunda.pictureapp.integration.service;

import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.camunda.pictureapp.TestContainerConfig;
import com.camunda.pictureapp.model.AnimalType;
import com.camunda.pictureapp.persistence.PictureEntity;
import com.camunda.pictureapp.persistence.PictureRepository;
import com.camunda.pictureapp.service.PictureService;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Import(TestContainerConfig.class)
public class PictureServiceTest {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private ResourceLoader resourceLoader;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void cleanUp() {
        pictureRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void givenOnlinePicture_whenFetchPicture_thenPersistPicture() {
        var bearPictureData = Files.readAllBytes(Paths.get(resourceLoader.getResource("classpath:picture/bear.jpeg").getURI()));

        when(restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(bearPictureData, HttpStatus.OK));

        pictureService.fetchPicture(AnimalType.BEAR, "https://bestbearpictures.com/bear.jpeg");

        var retrievedPicture = pictureRepository.findFirstByTypeOrderByCreatedAtDesc(AnimalType.BEAR);
        assertThat(retrievedPicture.isPresent());
        assertThat(retrievedPicture.get().getData()).isEqualTo(bearPictureData);
        assertThat(retrievedPicture.get().getType()).isEqualTo(AnimalType.BEAR);
    }


    @SneakyThrows
    @Test
    void givenPersistedPicture_whenGetPicture_thenReturnPicture() {
        var bearPictureData = Files.readAllBytes(Paths.get(resourceLoader.getResource("classpath:picture/bear.jpeg").getURI()));
        var bear = PictureEntity.builder().data(bearPictureData).type(AnimalType.BEAR).build();
        pictureRepository.save(bear);

        var dogPictureData = Files.readAllBytes(Paths.get(resourceLoader.getResource("classpath:picture/dog.jpeg").getURI()));
        var dog = PictureEntity.builder().data(dogPictureData).type(AnimalType.DOG).build();
        pictureRepository.save(dog);

        var retrievedPicture = pictureService.getPicture(AnimalType.DOG);

        assertThat(retrievedPicture.isPresent());
        assertThat(retrievedPicture.get().getData()).isEqualTo(dogPictureData);
    }

    @SneakyThrows
    @Test
    void givenNoPersistedPicture_whenGetPicture_thenReturnEmpty() {
        var retrievedPicture = pictureService.getPicture(AnimalType.DOG);
        assertThat(retrievedPicture.isEmpty()).isTrue();
    }
}
