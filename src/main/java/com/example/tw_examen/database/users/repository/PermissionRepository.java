package com.example.tw_examen.database.users.repository;

import com.example.tw_examen.database.users.model.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findPermissionEntityById(Long id);
}
