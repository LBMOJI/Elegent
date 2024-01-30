package com.example.demo.Controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.Base.Result;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Service.UserService;
import com.example.demo.Util.UserHolder;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("sendCode")
    public Result sendCode(HttpSession session) {
        return userService.sendCode("3198835313@qq.com", session);
    }

    @RequestMapping("userLogin")
    public Result userLogin(HttpSession session) {
        String code = "000000";
        String to = "3198835313@qq.com";
        return userService.login(to,code,session);
    }

    @GetMapping("me")
    public Result me(){
        UserDTO user = UserHolder.getUser();
        return Result.success(user);
    }

    @GetMapping("getUser")
    public Result getUser(){
        return userService.SelectById(10000);
    }

    @PutMapping("updateUser")
    public Result updateUser(){
        Result result=  userService.SelectById(10000);
        User user = BeanUtil.toBean(result.getData(),User.class);
        user.setSex("女");
        return userService.updateById(user);
    }

}
