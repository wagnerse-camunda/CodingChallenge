package com.camunda.pictureapp.api;

import com.camunda.pictureapp.model.AnimalType;
import com.camunda.pictureapp.persistence.PictureEntity;
import com.camunda.pictureapp.service.PictureService;
import com.camunda.pictureapp.workflow.ZeebeClientService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animal-picture")
public class ClientController {

    private final ZeebeClientService camundaClientService;

    private final PictureService pictureService;

    @PostMapping("/{animal}")
    public ResponseEntity<Void> createAnimalPicture(@PathVariable String animal) {
        try {
            var animalType = AnimalType.valueOf(animal.toUpperCase());
            camundaClientService.executeImageGenerationProcess(animalType);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{animal}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getAnimalPicture(@PathVariable String animal) {
        try {
            var animalType = AnimalType.valueOf(animal.toUpperCase());
            Optional<PictureEntity> picture = pictureService.getPicture(animalType);
            return picture.map(p -> ResponseEntity.ok().body(p.getData())).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
