package com.example.demo.Util;

import com.example.demo.DTO.UserDTO;
import com.example.demo.domain.User;

public class UserHolder {
    public static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
