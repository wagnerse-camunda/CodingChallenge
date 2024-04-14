package com.camunda.pictureapp.service;

import com.camunda.pictureapp.model.AnimalType;
import com.camunda.pictureapp.persistence.PictureEntity;
import com.camunda.pictureapp.persistence.PictureRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;

    private final RestTemplate restTemplate;

    public void fetchPicture(AnimalType type, String url) {
        log.info("Generating picture for animal type {} and url {}", type.getType(), url);
        // Using synchronous RestTemplate for simplicity, WebClient could be used for non-blocking calls
        var response = restTemplate.getForEntity(url, byte[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            byte[] pictureData = response.getBody();
            var picture = PictureEntity.builder().data(pictureData).type(type).build();
            pictureRepository.save(picture);
        } else {
            throw new RuntimeException(String.format("Failed to retrieve picture from %s, status %d returned.", url, response.getStatusCode()));
        }
    }

    public Optional<PictureEntity> getPicture(AnimalType type) {
        return pictureRepository.findFirstByTypeOrderByCreatedAtDesc(type);
    }
}
