package com.Insurance.service;

import com.Insurance.entity.User;

import java.util.List;

public interface UserService {
    User VerifyUserInLogin(String phone, String password);

    void addUser(User user) throws Exception;

    User selectByPhone(String phone);

    void update(User user);

    void userDelete(User user);

    User selectById(Integer id);

    List<User> selectListById(Integer id);

    List<User> selectAll();

    void deleteById(Integer id);
}
