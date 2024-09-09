package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.User;
import com.research.agrivision.business.entity.UserRole;

public interface SaveUserPort {
    User createUser(User user);

    UserRole createUserRole(UserRole userRole);

    UserRole updateUserRole(UserRole userRole);

    User updateUser(User user);
}
