package com.parttime.service.impl;

import com.parttime.entity.User;
import com.parttime.mapper.UserMapper;
import com.parttime.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            user.setPassword(null);
            return user;
        }
        return null;
    }

    @Override
    public User register(User user) {
        User existing = userMapper.findByUsername(user.getUsername());
        if (existing != null) {
            return null;
        }
        if (user.getRole() == null) {
            user.setRole("student");
        }
        userMapper.insert(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public User updateInfo(User user) {
        userMapper.update(user);
        return userMapper.findById(user.getId());
    }

    @Override
    public User findById(Integer id) {
        User user = userMapper.findById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
}
