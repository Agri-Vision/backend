package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.User;
import com.research.agrivision.business.entity.UserRole;
import com.research.agrivision.business.port.in.UserManagementUseCase;
import com.research.agrivision.business.port.out.FilePort;
import com.research.agrivision.business.port.out.GetUserPort;
import com.research.agrivision.business.port.out.SaveUserPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserManagementUseCaseImpl implements UserManagementUseCase {
    @Autowired
    private SaveUserPort saveUserPort;

    @Autowired
    private GetUserPort getUserPort;

    @Autowired
    private FilePort filePort;

    @Override
    public User createUser(User user) {
        String email = user.getEmail().toLowerCase();
        user.setEmail(email);
        user.setIdentityId(String.valueOf(UUID.randomUUID()));
        User createdUser = saveUserPort.createUser(user);
        generateSignedUrl(createdUser);
        return createdUser;
    }

    @Override
    public UserRole createUserRole(UserRole userRole) {
        return saveUserPort.createUserRole(userRole);
    }

    @Override
    public UserRole getUserRoleById(Long id) {
        return getUserPort.getUserRoleById(id);
    }

    @Override
    public UserRole updateUserRole(UserRole userRole) {
        if (getUserPort.getUserRoleById(userRole.getId()) == null) return null;
        return saveUserPort.updateUserRole(userRole);
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        return getUserPort.getAllUserRoles();
    }

    @Override
    public List<UserRole> getAllUserRolesByType(String roleType) {
        return getUserPort.getAllUserRolesByType(roleType);
    }

    @Override
    public User getUserByEmail(String email) {
        if (email == null || email.isEmpty()) return null;
        User user = getUserPort.getUserByEmail(email);
        if (user == null) return null;
        generateSignedUrl(user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        User user = getUserPort.getUserById(id);
        if (user == null) return null;
        user.setPassword(null);
        generateSignedUrl(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = getUserPort.getAllUsers();
        for (User user : users) {
            user.setPassword(null);
            generateSignedUrl(user);
        }
        return users;
    }

    @Override
    public User updateUser(User user) {
        if (getUserPort.getUserById(user.getId()) == null) return null;
        User updateduser = saveUserPort.updateUser(user);
        updateduser.setPassword(null);
        generateSignedUrl(updateduser);
        return updateduser;
    }

    @Override
    public List<User> getAllUsersByRole(Long roleId) {
        List<User> users = getUserPort.getAllUsersByRole(roleId);
        for (User user : users) {
            user.setPassword(null);
            generateSignedUrl(user);
        }
        return users;
    }

    private void generateSignedUrl(User user) {
        if(user.getProfileImg() != null) {
            String imgName = user.getProfileImg();
            user.setProfileImgUrl(filePort.generateSignedUrl(imgName));
        }
    }
}
