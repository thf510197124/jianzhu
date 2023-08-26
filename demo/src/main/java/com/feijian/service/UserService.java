package com.feijian.service;

import com.feijian.domain.Role;
import com.feijian.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User register(User user);
    User get(int id);
    User updateUser(User user);
    void addUserRole(User user, Role role);
    List<User> allUsers();
    List<User> needEnabled(boolean enabled);
}
