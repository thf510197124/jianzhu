package com.feijian.service.impl;

import com.feijian.dao.UserRepository;
import com.feijian.domain.Role;
import com.feijian.domain.User;
import com.feijian.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User get(int id) {
        return userRepository.getOne(id);
    }

    @Override
    public User updateUser(User user) {
        userRepository.save(user);
        return user;
    }


    @Override
    public void addUserRole(User user,Role role) {
        user.addRole(role);
        userRepository.save(user);
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> needEnabled(boolean enabled) {
        return userRepository.getUsersByEnabled(enabled);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }
}
