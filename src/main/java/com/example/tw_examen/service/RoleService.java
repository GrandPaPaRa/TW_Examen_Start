package com.example.tw_examen.service;

import com.example.tw_examen.database.users.model.PermissionEntity;
import com.example.tw_examen.database.users.model.RoleEntity;
import com.example.tw_examen.database.users.model.UserEntity;
import com.example.tw_examen.database.users.repository.PermissionRepository;
import com.example.tw_examen.database.users.repository.RoleRepository;
import com.example.tw_examen.database.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository, UserRepository userRepository,  UserService userService) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void save(RoleEntity role) {
        this.roleRepository.save(role);
    }

    public Boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public Optional<RoleEntity> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    public RoleEntity findById(Long id){
        return roleRepository.findRoleEntityById(id).orElse(null);
    }
    public void deleteById(Long id) {
        RoleEntity role = roleRepository.findById(id).orElse(null);

        if (role != null) {
            // Remove the role from all associated users
            for (UserEntity user : new ArrayList<>(role.getUsers())) {
                user.getRoles().remove(role);
            }
            // Save updated users to persist the changes
            userRepository.saveAll(role.getUsers());

            // Delete the role after it has been removed from all users
            roleRepository.deleteById(id);
        }
    }
    public void delete(RoleEntity role){
        roleRepository.delete(role);
    }
    public void addPermissionToRole(RoleEntity role, String httpMethod, String url) {
        PermissionEntity permission = new PermissionEntity();
        permission.setHttpMethod(httpMethod);
        permission.setUrl(url);
        permission.setRole(role); // Link the permission to the role

        permissionRepository.save(permission); // Save the permission
    }
    public boolean hasPermission(String requestUrl, String requestMethod) {
        UserEntity currentUser = userService.getCurrentAuthenticatedUser();

        if (currentUser == null) {
            return false;
        }

        // Get all roles of the current user
        for (RoleEntity role : currentUser.getRoles()) {
            // Check if any role has a permission that matches the request
            boolean hasPermission = role.getPermissions().stream()
                    .anyMatch(permission ->
                            pathMatcher.match(permission.getUrl(), requestUrl) &&
                                    permission.getHttpMethod().equalsIgnoreCase(requestMethod)
                    );

            if (hasPermission) {
                return true;
            }
        }

        return false;
    }
    // Delete role if no users are assigned to it
    public void deleteRoleIfUnused(String roleName) {
        RoleEntity role = roleRepository.findByName(roleName).orElse(null);
        if (role != null && role.getUsers().isEmpty()) { // No users with the role
            roleRepository.delete(role);
        }
    }
}
