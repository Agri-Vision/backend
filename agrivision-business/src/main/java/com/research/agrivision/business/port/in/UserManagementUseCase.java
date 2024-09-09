package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.User;
import com.research.agrivision.business.entity.UserRole;
import com.research.hexa.core.UseCase;

import java.util.List;

@UseCase
public interface UserManagementUseCase {
    User createUser(User user);

    UserRole createUserRole(UserRole userRole);

    UserRole getUserRoleById(Long id);

    UserRole updateUserRole(UserRole userRole);

    List<UserRole> getAllUserRoles();

    List<UserRole> getAllUserRolesByType(String roleType);

    User getUserByEmail(String email);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(User user);
}
