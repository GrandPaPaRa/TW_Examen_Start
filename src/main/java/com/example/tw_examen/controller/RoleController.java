package com.example.tw_examen.controller;

import com.example.tw_examen.database.users.model.PermissionEntity;
import com.example.tw_examen.database.users.model.RoleEntity;
import com.example.tw_examen.service.PermissionService;
import com.example.tw_examen.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/roles")
public class RoleController {

    private final RoleService roleService;


    private final PermissionService permissionService;

    @Autowired
    public RoleController(RoleService roleService,PermissionService permissionService ){
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping("/{id}/permissions")
    public String editPermissions(@PathVariable Long id, Model model) {
        RoleEntity role = roleService.findById(id);
        model.addAttribute("role", role);
        return "admin/edit-permissions";
    }

    @PostMapping("/{id}/permissions/add")
    public String addPermission(@PathVariable Long id,
                                @RequestParam String httpMethod,
                                @RequestParam String url,
                                RedirectAttributes redirectAttributes) {
        RoleEntity role = roleService.findById(id);
        PermissionEntity permission = new PermissionEntity();
        permission.setHttpMethod(httpMethod);
        permission.setUrl(url);
        permission.setRole(role);

        permissionService.save(permission);
        role.getPermissions().add(permission);
        roleService.save(role);

        redirectAttributes.addFlashAttribute("successMessage", "Permission added successfully.");
        return "redirect:/admin/roles/" + id + "/permissions";
    }

    @PostMapping("/{roleId}/permissions/{permId}/remove")
    public String removePermission(@PathVariable Long roleId, @PathVariable Long permId, RedirectAttributes redirectAttributes) {
        // Find the permission by its ID
        PermissionEntity permission = permissionService.findById(permId);

        if (permission != null) {
            // Remove the permission from the role
            RoleEntity role = permission.getRole();
            if (role != null) {
                role.getPermissions().remove(permission);
            }

            // Now delete the permission from the database
            permissionService.delete(permission);

            redirectAttributes.addFlashAttribute("successMessage", "Permission removed successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Permission not found.");
        }

        return "redirect:/admin/roles/" + roleId + "/permissions";
    }
    // Show Edit Role Form
    @GetMapping("/{id}/edit")
    public String editRole(@PathVariable Long id, Model model) {
        RoleEntity role = roleService.findById(id);
        model.addAttribute("role", role);
        return "admin/roles/edit"; // Thymeleaf template for editing role
    }

    // Process Edit Role
    @PostMapping("/{id}/update")
    public String updateRole(@PathVariable Long id, @RequestParam String name) {
        RoleEntity role = roleService.findById(id);
        role.setName(name);
        roleService.save(role);
        return "redirect:/admin/roles";
    }

    // Delete Role
    @PostMapping("/{id}/delete")
    public String deleteRole(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        RoleEntity admin = roleService.findByName("ADMIN").orElse(null);

        if(roleService.findById(id).equals(admin))
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete ADMIN role!");
        else
            roleService.deleteById(id);

        return "redirect:/admin/roles";
    }

    // Show Add Role Form
    @GetMapping("/add")
    public String showAddRoleForm(Model model) {
        model.addAttribute("role", new RoleEntity());
        return "admin/roles/add";
    }

    // Process Add Role
    @PostMapping("/save")
    public String saveRole(@ModelAttribute RoleEntity role) {
        roleService.save(role);
        return "redirect:/admin/roles";
    }
}
