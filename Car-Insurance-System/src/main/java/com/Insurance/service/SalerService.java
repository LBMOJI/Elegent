package com.Insurance.service;

import com.Insurance.entity.Saler;

import java.util.List;

public interface SalerService {

    Saler doLogin(Saler saler);

    void pwdRevise(Saler saler);

    Saler selectById(Integer id);

    void revise(Saler saler);

    List<Saler> selectAll();

    List<Saler> selectByName(String name);

    void deleteSaler(int id);

    void insertSaler(Saler saler);
}
