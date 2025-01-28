package com.example.tw_examen;

import com.example.tw_examen.database.users.model.PermissionEntity;
import com.example.tw_examen.database.users.model.Role;
import com.example.tw_examen.database.users.model.RoleEntity;
import com.example.tw_examen.database.users.model.UserEntity;
import com.example.tw_examen.service.PermissionService;
import com.example.tw_examen.service.RoleService;
import com.example.tw_examen.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {
    private final List<String> CRUD_METHODS = List.of("POST", "GET", "PATCH", "DELETE");
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public DataInitializer(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        /* ROLES */
        RoleEntity admin = new RoleEntity();
        admin.setRole(Role.ADMIN);

        RoleEntity user = new RoleEntity();
        user.setRole(Role.USER);

        RoleEntity defaultRole = new RoleEntity();
        defaultRole.setRole(Role.DEFAULT);

        Boolean isRoleAdminInDb = roleService.existsByName(admin.getName());
        if(!isRoleAdminInDb) {
            roleService.save(admin);
            for (String crudMethod : CRUD_METHODS) {
                PermissionEntity permissionEntity = new PermissionEntity();
                permissionEntity.setHttpMethod(crudMethod);
                permissionEntity.setUrl("/**");
                permissionEntity.setRole(admin);
                permissionService.save(permissionEntity);
            }
        }

        Boolean isRoleUserInDb = roleService.existsByName(user.getName());
        if(!isRoleUserInDb) {
            roleService.save(user);
            PermissionEntity permissionEntity = new PermissionEntity();
            //add default user permissions if necessary
//            permissionEntity.setHttpMethod("GET");
//            permissionEntity.setUrl("/home");
//            permissionEntity.setRole(user);
//            permissionService.save(permissionEntity);
        }

        Boolean isRoleDefaultInDb = roleService.existsByName(defaultRole.getName());
        if(!isRoleDefaultInDb)
            roleService.save(defaultRole);

        UserEntity adminUser = new UserEntity();
        adminUser.setUsername("Administrator");
        adminUser.setEmail("admin@admin.com");
        adminUser.setPassword("admin");
        adminUser.addRole(admin);

        Boolean isUserPresentInDatabase = this.userService.existsByEmail(adminUser.getEmail());

        if(!isUserPresentInDatabase)
            this.userService.save(adminUser);

    }
}
