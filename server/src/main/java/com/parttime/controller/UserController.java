package com.parttime.controller;

import com.parttime.common.Result;
import com.parttime.entity.User;
import com.parttime.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> params) {
        User user = userService.login(params.get("username"), params.get("password"));
        if (user != null) {
            return Result.success(user);
        }
        return Result.error(401, "用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        User registered = userService.register(user);
        if (registered != null) {
            return Result.success(registered);
        }
        return Result.error(400, "用户名已存在");
    }

    @PutMapping("/update")
    public Result<User> update(@RequestBody User user) {
        return Result.success(userService.updateInfo(user));
    }

    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable Integer id) {
        return Result.success(userService.findById(id));
    }
}
