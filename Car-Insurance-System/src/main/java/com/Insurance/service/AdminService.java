package com.Insurance.service;

import com.Insurance.entity.Admin;
import com.Insurance.tools.Month;
import com.Insurance.tools.Result;

import java.text.ParseException;
import java.util.List;

public interface AdminService {
    Admin doLogin(Admin admin);

    void updateAdmin(Admin admin);

    Admin selectById(Integer id);

    void deleteAdmin(Integer id);
}
