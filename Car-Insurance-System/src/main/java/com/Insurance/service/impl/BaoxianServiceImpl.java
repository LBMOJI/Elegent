package com.Insurance.service.impl;

import com.Insurance.entity.Baoxian;
import com.Insurance.entity.BaoxianExample;
import com.Insurance.mapper.BaoxianMapper;
import com.Insurance.service.BaoxianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BaoxianServiceImpl implements BaoxianService {

    @Autowired
    private BaoxianMapper baoxianMapper;

    @Override
    public List<Baoxian> getAllBaoXian() {
        BaoxianExample example = new BaoxianExample();
         return baoxianMapper.selectByExample(example);
    }

    @Override
    public Baoxian selectById(Integer id){
        return baoxianMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Baoxian> searchBaoxian(String name){
        BaoxianExample example = new BaoxianExample();
        BaoxianExample.Criteria criteria = example.createCriteria();
        criteria.andBnameEqualTo(name);
        List<Baoxian> list = new ArrayList<>();
        if (baoxianMapper.selectByExample(example).size() > 0) {
            list.add(baoxianMapper.selectByExample(example).get(0));
        }
        return list;
    }

    @Override
    public void updateBaoxian(Baoxian baoxian){
        baoxianMapper.updateByPrimaryKeySelective(baoxian);
    }

    @Override
    public void deleteBaoxian(String id){
        Integer Bid = Integer.parseInt(id);
        baoxianMapper.deleteByPrimaryKey(Bid);
    }

    @Override
    public void insertBaoxian(Baoxian baoxian){
        Date date = new Date();
        baoxian.setReleasedate(date);
        baoxianMapper.insertSelective(baoxian);
    }
}
