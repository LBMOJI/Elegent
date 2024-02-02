package com.Insurance.service;

import com.Insurance.entity.Baoxian;

import java.util.List;

public interface BaoxianService {
    public List<Baoxian> getAllBaoXian();

    Baoxian selectById(Integer id);

    List<Baoxian> searchBaoxian(String name);

    void updateBaoxian(Baoxian baoxian);

    void deleteBaoxian(String id);

    void insertBaoxian(Baoxian baoxian);
}
