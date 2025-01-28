package com.example.tw_examen.controller;

import com.example.tw_examen.database.users.model.RoleEntity;
import com.example.tw_examen.database.users.model.UserEntity;
import com.example.tw_examen.service.RoleService;
import com.example.tw_examen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/users")
    public String userManagement(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/roles")
    public String roleManagement(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/roles";
    }
    @GetMapping("/users/{id}/roles")
    public String updateRolesForm(@PathVariable Long id, Model model) {
        UserEntity user = userService.findById(id);
        List<RoleEntity> availableRoles = roleService.findAll()
                .stream()
                .filter(role -> !user.getRoles().contains(role)) // Exclude existing roles
                .toList();

        model.addAttribute("user", user);
        model.addAttribute("availableRoles", availableRoles);
        return "admin/update_roles"; // New template for updating roles
    }

    @PostMapping("/users/{id}/roles")
    public String updateRoles(@PathVariable Long id, @RequestParam("roleId") Long roleId) {
        userService.addRoleToUser(id, roleId);
        return "redirect:/admin/users"; // Redirect back to the user list
    }
    @PostMapping("/users/{userId}/roles/{roleId}/remove")
    public String removeRole(@PathVariable Long userId, @PathVariable Long roleId, RedirectAttributes redirectAttributes) {
        UserEntity user = userService.findById(userId);
        RoleEntity role = roleService.findById(roleId);
        UserEntity currentUser = userService.getCurrentAuthenticatedUser();

        // Check if the user is trying to remove their own admin role
        if (currentUser != null && currentUser.equals(user) && role != null && role.equals(roleService.findByName("ADMIN").orElse(null))) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot remove your own ADMIN role.");
            return "redirect:/admin/users";
        }

        if (user != null && role != null) {
            user.getRoles().remove(role);
            userService.save(user); // Save the updated user
        }

        return "redirect:/admin/users";
    }
}