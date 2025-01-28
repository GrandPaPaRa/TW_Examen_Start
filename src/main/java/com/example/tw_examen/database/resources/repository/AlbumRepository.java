package com.example.tw_examen.database.resources.repository;

import com.example.tw_examen.database.resources.model.AlbumEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {
    Boolean existsAlbumEntityByName(String name);
}
