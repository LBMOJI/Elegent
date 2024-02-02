package com.Insurance.service.impl;

import com.Insurance.entity.Car;
import com.Insurance.entity.CarExample;
import com.Insurance.entity.User;
import com.Insurance.mapper.CarMapper;
import com.Insurance.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarMapper mapper;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Override
    public List<Car> getListByUserId(User user) {
        CarExample example = new CarExample();
        CarExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(user.getId());
        criteria.andStatusEqualTo("审核完毕");
        List<Car> list = mapper.selectByExampleWithBLOBs(example);
        if (list.size() == 0)
            return null;
        else
            return list;
    }

    @Override
    public void insertCar(Car car) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(definition);
        try {
            car.setStatus("审核中");
            mapper.insert(car);
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            platformTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    @Override
    public List<Car> NotAudited(User user) {
        CarExample example = new CarExample();
        CarExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("审核中");
        criteria.andUidEqualTo(user.getId());
        return mapper.selectByExample(example);
    }

    @Override
    public List<Car> AuditedFailed(User user) {
        CarExample example = new CarExample();
        CarExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("审核未通过");
        criteria.andUidEqualTo(user.getId());
        return mapper.selectByExample(example);
    }

    @Override
    public List<Car> selectCarAndUser() {
        return mapper.selectCarAndUser();
    }

    @Override
    public List<Car> searchCph(String cph) {
        return mapper.selectCarAndUserByCph(cph);
    }

    @Override
    public List<Car> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public List<Car> selectByStatus(String status) {
        return mapper.selectByStatus(status);
    }

    @Override
    public void refuseCar(String cph, String ctype) {
        Car car = new Car();
        car.setCph(cph);
        car.setCtype(ctype);
        car.setStatus("审核未通过");
        mapper.updateByPrimaryKeySelective(car);
    }

    @Override
    public void passCar(String cph, String ctype) {
        Car car = new Car();
        car.setCph(cph);
        car.setCtype(ctype);
        car.setStatus("审核完毕");
        mapper.updateByPrimaryKeySelective(car);
    }

    @Override
    public void backStatus(String cph, String status, String ctype) {
        Car car = new Car();
        car.setCph(cph);
        car.setCtype(ctype);
        if (status.equals("审核完毕") || status.equals("审核未通过")) {
            car.setStatus("审核中");
        }
        mapper.updateByPrimaryKeySelective(car);
    }

    @Override
    public void deleteCar(String cph, String ctype) {
        mapper.deleteByPrimaryKey(cph, ctype);
    }

    @Override
    public List<Car> selectByCph(String cph) {
        return mapper.selectByCph(cph);
    }

}
