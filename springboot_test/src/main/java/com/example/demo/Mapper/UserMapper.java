package com.example.demo.Mapper;

import com.example.demo.domain.User;

public interface UserMapper {
    User selectById(Integer id);

    void updateById(User user);
}
