package com.example.tw_examen.service;

import com.example.tw_examen.database.users.model.PermissionEntity;
import com.example.tw_examen.database.users.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void save(PermissionEntity permissionEntity) {
        this.permissionRepository.save(permissionEntity);
    }
    public void delete(PermissionEntity permissionEntity){
        permissionRepository.delete(permissionEntity);
    }
    public PermissionEntity findById(Long id){
        return permissionRepository.findPermissionEntityById(id).orElse(null);
    }
}
