package com.example.demo.Service;


import com.example.demo.Base.Result;
import com.example.demo.domain.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    Result sendCode(String area, HttpSession session);

    Result login(String phone, String code, HttpSession session);

    Result SelectById(int id);

    Result updateById(User user);
}
