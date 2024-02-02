package com.Insurance.service.impl;

import com.Insurance.entity.Saler;
import com.Insurance.entity.SalerExample;
import com.Insurance.mapper.SalerMapper;
import com.Insurance.service.SalerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalerServiceImpl implements SalerService {

    @Autowired
    private SalerMapper mapper;

    @Override
    public Saler doLogin(Saler saler){
        Saler saler1 = mapper.selectByPrimaryKey(saler.getId());
        if (saler1 == null)
            return null;
        if (!saler1.getPwd().equals(saler.getPwd()))
            return null;
        if (saler1.getDemission() == 1){
            return null;
        }
        return saler1;
    }

    @Override
    public void pwdRevise(Saler saler){
        mapper.updateByPrimaryKeySelective(saler);
    }

    @Override
    public Saler selectById(Integer id){
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public void revise(Saler saler){
        mapper.updateByPrimaryKeySelective(saler);
    }

    @Override
    public List<Saler> selectAll(){
        SalerExample example = new SalerExample();
        SalerExample.Criteria criteria = example.createCriteria();
        criteria.andDemissionEqualTo(0);
        return mapper.selectByExample(example);
    }

    @Override
    public List<Saler> selectByName(String name){
        SalerExample example = new SalerExample();
        SalerExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        return mapper.selectByExample(example);
    }

    @Override
    public void deleteSaler(int id){
        Saler saler = new Saler();
        saler.setId(id);
        saler.setDemission(1);
        mapper.updateByPrimaryKeySelective(saler);
    }

    @Override
    public void insertSaler(Saler saler){
        saler.setDemission(0);
        mapper.insertSelective(saler);
    }
}
