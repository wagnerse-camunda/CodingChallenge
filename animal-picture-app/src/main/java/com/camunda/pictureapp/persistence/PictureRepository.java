package com.camunda.pictureapp.persistence;

import com.camunda.pictureapp.model.AnimalType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {
    @Transactional(readOnly = true)
    Optional<PictureEntity> findFirstByTypeOrderByCreatedAtDesc(AnimalType animalType);
}
