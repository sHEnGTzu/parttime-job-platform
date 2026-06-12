package com.parttime.service;

import com.parttime.entity.User;

public interface UserService {
    User login(String username, String password);
    User register(User user);
    User updateInfo(User user);
    User findById(Integer id);
}
