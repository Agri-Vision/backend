package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.request.LoginRequest;
import com.research.agrivision.business.entity.User;
import com.research.agrivision.business.entity.UserRole;
import com.research.agrivision.business.port.in.UserManagementUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/identity")
public class UserManagementController {
    private final UserManagementUseCase userService;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserManagementController(UserManagementUseCase userService) {
        this.userService = userService;
    }

    @PostMapping("/role")
    public ResponseEntity<UserRole> createUserRole(@RequestBody final UserRole userRole) {
        if (userRole == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        UserRole createdRole = userService.createUserRole(userRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/role")
    public ResponseEntity<UserRole> updateUserRole(@RequestBody final UserRole userRole) {
        if (userRole == null || userRole.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UserRole role = userService.getUserRoleById(userRole.getId());

        if (role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserRole updatedRole = userService.updateUserRole(userRole);
        return ResponseEntity.ok(updatedRole);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<UserRole> getUserRoleById(@PathVariable Long id) {
        UserRole userRole = userService.getUserRoleById(id);
        if (userRole == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userRole);
    }

    @GetMapping("/role")
    public ResponseEntity<List<UserRole>> getAllUserRoles() {
        List<UserRole> roleList = userService.getAllUserRoles();
        if (roleList == null || roleList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(roleList);
    }

    @GetMapping("/role/type/{roleType}")
    public ResponseEntity<List<UserRole>> getAllUserRolesByType(@PathVariable String roleType) {
        List<UserRole> roleList = userService.getAllUserRolesByType(roleType);
        if (roleList == null || roleList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(roleList);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody final User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User createdUser = userService.createUser(user);
        createdUser.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> userLogin(@RequestBody final LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user = userService.getUserByEmail(request.getEmail());
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if (!user.getPassword().equals(request.getPassword()) || !user.isStatus()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users == null || users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(users);
    }

    @PutMapping("/user")
    public ResponseEntity<User> updateUser(@RequestBody final User user) {
        if (user == null || user.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User dbuser = userService.getUserById(user.getId());

        if (dbuser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }
}
