package com.example.tw_examen.database.resources.repository;

import com.example.tw_examen.database.resources.model.ImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    Optional<List<ImageEntity>> findImageEntitiesByAlbum_Id(Long id);
}
