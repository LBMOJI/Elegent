package com.Insurance.service.impl;

import com.Insurance.entity.*;
import com.Insurance.mapper.AdminMapper;
import com.Insurance.mapper.RecordMapper;
import com.Insurance.mapper.UserMapper;
import com.Insurance.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper mapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordMapper recordMapper;


    @Override
    public Admin doLogin(Admin admin) {
        Admin admin1 = mapper.selectByPrimaryKey(admin.getId());
        if (admin1 == null)
            return null;
        if (!admin.getPwd().equals(admin1.getPwd()))
            return null;
        return admin1;
    }

    @Override
    public void updateAdmin(Admin admin) {
        mapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public Admin selectById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteAdmin(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }
}
