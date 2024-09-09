package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.User;
import com.research.agrivision.business.entity.UserRole;

import java.util.List;

public interface GetUserPort {
    UserRole getUserRoleById(Long id);

    List<UserRole> getAllUserRoles();

    List<UserRole> getAllUserRolesByType(String roleType);

    User getUserByEmail(String email);

    User getUserById(Long id);

    List<User> getAllUsers();

    List<User> getAllUsersByRole(Long roleId);
}
