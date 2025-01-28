package com.example.tw_examen.database.users.repository;

import com.example.tw_examen.database.users.model.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //@Query(value = "SELECT user FROM UserEntity user WHERE user.username=:username")
    //@Query(value = "SELECT * FROM app_user WHERE username=:username", nativeQuery = true)
    List<UserEntity> findAllByUsername(String username);

    @Modifying
    @Query(value = "update app_user set username=:username, email=:email, password=:password WHERE id=:id", nativeQuery = true)
    void updateUserEntity(Long id, String username, String email, String password);

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findUserEntityById(Long id);
    Boolean existsByEmail(String email);
    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.id IN :roleIds")
    Optional<List<UserEntity>> findUsersByRoleIds(@Param("roleIds") List<Long> roleIds);
    boolean existsByUsername(String username);
}
