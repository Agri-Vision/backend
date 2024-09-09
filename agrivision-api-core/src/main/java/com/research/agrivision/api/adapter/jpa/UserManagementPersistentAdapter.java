package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.jpa.entity.AvUser;
import com.research.agrivision.api.adapter.jpa.entity.AvUserRole;
import com.research.agrivision.api.adapter.jpa.entity.AvUserRoleMap;
import com.research.agrivision.api.adapter.jpa.repository.AvUserRepository;
import com.research.agrivision.api.adapter.jpa.repository.AvUserRoleMapRepository;
import com.research.agrivision.api.adapter.jpa.repository.AvUserRoleRepository;
import com.research.agrivision.business.entity.User;
import com.research.agrivision.business.entity.UserRole;
import com.research.agrivision.business.port.out.GetUserPort;
import com.research.agrivision.business.port.out.SaveUserPort;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class UserManagementPersistentAdapter implements GetUserPort, SaveUserPort {
    private final AvUserRepository userRepository;
    private final AvUserRoleRepository userRoleRepository;
    private final AvUserRoleMapRepository roleMapRepository;

    private ModelMapper mapper = new ModelMapper();

    public UserManagementPersistentAdapter(AvUserRepository userRepository, AvUserRoleRepository userRoleRepository, AvUserRoleMapRepository roleMapRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleMapRepository = roleMapRepository;
    }

    @Override
    public User createUser(User user) {
        if (user == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.AvUser avUser = mapper.map(user, com.research.agrivision.api.adapter.jpa.entity.AvUser.class);
        AvUser createdUser = userRepository.save(avUser);
        UserRole userRole = new UserRole();

        if (user.getRole() != null && user.getRole().getId() != null) {
            AvUserRole role = userRoleRepository.findById(user.getRole().getId()).orElse(null);
            if (role != null) {
                AvUserRoleMap roleMap = new AvUserRoleMap();
                roleMap.setRole(role);
                roleMap.setUser(createdUser);
                AvUserRoleMap createdRoleMap = roleMapRepository.save(roleMap);
                userRole = mapper.map(createdRoleMap.getRole(), UserRole.class);
            }
        }

        User dbUser = mapper.map(createdUser, User.class);
        dbUser.setRole(userRole);
        return dbUser;
    }

    @Override
    public UserRole createUserRole(UserRole userRole) {
        if (userRole == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.AvUserRole avUserRole = mapper.map(userRole, com.research.agrivision.api.adapter.jpa.entity.AvUserRole.class);
        userRoleRepository.save(avUserRole);
        return mapper.map(avUserRole, UserRole.class);
    }

    @Override
    public UserRole updateUserRole(UserRole userRole) {
        if (userRole == null) return null;
        com.research.agrivision.api.adapter.jpa.entity.AvUserRole dbUserRole = userRoleRepository.save(mapper.map(userRole, com.research.agrivision.api.adapter.jpa.entity.AvUserRole.class));
        return mapper.map(dbUserRole, UserRole.class);
    }

    @Override
    public User updateUser(User user) {
        if (user == null) return null;
        Optional<AvUser> optionalAvUser = userRepository.findById(user.getId());
        String password = null;
        if (optionalAvUser.isPresent()) password = optionalAvUser.get().getPassword();
        com.research.agrivision.api.adapter.jpa.entity.AvUser avUser = mapper.map(user, com.research.agrivision.api.adapter.jpa.entity.AvUser.class);
        if (user.getPassword() == null) avUser.setPassword(password);
        AvUser updatedUser = userRepository.save(avUser);
        UserRole userRole = new UserRole();

        if (user.getRole() != null && user.getRole().getId() != null) {
            AvUserRole role = userRoleRepository.findById(user.getRole().getId()).orElse(null);
            if (role != null) {
                AvUserRoleMap roleMap = roleMapRepository.findAvUserRoleMapByUserId(user.getId());
                if (roleMap != null) userRole = mapper.map(roleMap.getRole(), UserRole.class);
                if (roleMap != null && !roleMap.getRole().getId().equals(user.getRole().getId())) {
                    roleMapRepository.delete(roleMap);
                    AvUserRoleMap newRoleMap = new AvUserRoleMap();
                    newRoleMap.setRole(role);
                    newRoleMap.setUser(updatedUser);
                    AvUserRoleMap createdRoleMap = roleMapRepository.save(newRoleMap);
                    userRole = mapper.map(createdRoleMap.getRole(), UserRole.class);
                }
            }
        }

        User dbUser = mapper.map(updatedUser, User.class);
        dbUser.setRole(userRole);
        return dbUser;
    }

    @Override
    public UserRole getUserRoleById(Long id) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.AvUserRole> userRole = userRoleRepository.findById(id);
        if (userRole.isPresent()) {
            return mapper.map(userRole, com.research.agrivision.business.entity.UserRole.class);
        }
        return null;
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll().stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.AvUserRole::getLastModifiedDate).reversed())
                .map(userRole -> mapper.map(userRole, com.research.agrivision.business.entity.UserRole.class))
                .toList();
    }

    @Override
    public List<UserRole> getAllUserRolesByType(String roleType) {
        return userRoleRepository.findAllAvUserRolesByRoleTypeIgnoreCase(roleType).stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.AvUserRole::getLastModifiedDate).reversed())
                .map(userRole -> mapper.map(userRole, com.research.agrivision.business.entity.UserRole.class))
                .toList();
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.AvUser> user = userRepository.findAvUserByEmail(email.toLowerCase());
        UserRole role = new UserRole();
        if (user.isPresent()) {
            AvUserRoleMap roleMap = roleMapRepository.findAvUserRoleMapByUserId(user.get().getId());
            if (roleMap != null) {
                role = mapper.map(roleMap.getRole(), UserRole.class);
            }
            User dbUser = mapper.map(user, com.research.agrivision.business.entity.User.class);
            dbUser.setRole(role);
            return dbUser;
        }
        return null;
    }

    @Override
    public User getUserById(Long id) {
        Optional<com.research.agrivision.api.adapter.jpa.entity.AvUser> user = userRepository.findById(id);
        UserRole role = new UserRole();
        if (user.isPresent()) {
            AvUserRoleMap roleMap = roleMapRepository.findAvUserRoleMapByUserId(user.get().getId());
            if (roleMap != null) {
                role = mapper.map(roleMap.getRole(), UserRole.class);
            }
            User dbUser = mapper.map(user, com.research.agrivision.business.entity.User.class);
            dbUser.setRole(role);
            return dbUser;
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = userRepository.findAll().stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.AvUser::getLastModifiedDate).reversed())
                .map(user -> mapper.map(user, com.research.agrivision.business.entity.User.class))
                .toList();
        for (User user : userList) {
            UserRole role = new UserRole();
            AvUserRoleMap roleMap = roleMapRepository.findAvUserRoleMapByUserId(user.getId());
            if (roleMap != null) {
                role = mapper.map(roleMap.getRole(), UserRole.class);
            }
            User dbUser = mapper.map(user, com.research.agrivision.business.entity.User.class);
            dbUser.setRole(role);
        }
        return userList;
    }

    @Override
    public List<User> getAllUsersByRole(Long roleId) {
        List<AvUserRoleMap> roleMapList = roleMapRepository.findAvUserRoleMapsByRoleId(roleId);
        List<AvUser> users = new ArrayList<>();

        for (AvUserRoleMap roleMap : roleMapList) {
            users.add(roleMap.getUser());
        }

        List<User> userList = users.stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.AvUser::getLastModifiedDate).reversed())
                .map(user -> mapper.map(user, com.research.agrivision.business.entity.User.class))
                .toList();
        for (User user : userList) {
            UserRole role = new UserRole();
            AvUserRoleMap roleMap = roleMapRepository.findAvUserRoleMapByUserId(user.getId());
            if (roleMap != null) {
                role = mapper.map(roleMap.getRole(), UserRole.class);
            }
            User dbUser = mapper.map(user, com.research.agrivision.business.entity.User.class);
            dbUser.setRole(role);
        }
        return userList;
    }
}
