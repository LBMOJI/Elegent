package com.Insurance.service.impl;

import com.Insurance.entity.User;
import com.Insurance.entity.UserExample;
import com.Insurance.mapper.UserMapper;
import com.Insurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Override
    public User VerifyUserInLogin(String phone, String password) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneEqualTo(phone);
        criteria.andPwdEqualTo(password);
        List<User> users = userMapper.selectByExample(example);
        if (users.size() != 0){
            return users.get(0);
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public void addUser(User user) {
        DefaultTransactionDefinition definition=new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus=platformTransactionManager.getTransaction(definition);
        try {
            userMapper.insert(user);
            platformTransactionManager.commit(transactionStatus);
        }catch (Exception e){
            platformTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    @Override
    public User selectByPhone(String phone){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneEqualTo(phone);
        List<User> list = userMapper.selectByExample(example);
        if (list.size() != 0){
            return list.get(0);
        }else {
            return null;
        }
    }

    @Override
    public void update(User user){
        DefaultTransactionDefinition definition=new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus=platformTransactionManager.getTransaction(definition);
        try {
            userMapper.updateByPrimaryKeySelective(user);
            platformTransactionManager.commit(transactionStatus);
        }catch (Exception e){
            platformTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    @Override
    public void userDelete(User user){
        user.setName("账号已注销");
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public  User selectById(Integer id){
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> selectListById(Integer id){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        return userMapper.selectByExample(example);
    }

    @Override
    public List<User> selectAll(){
        UserExample example = new UserExample();
        example.setOrderByClause("id desc");
        return userMapper.selectByExample(example);
    }

    @Override
    public void deleteById(Integer id){
        User user = new User();
        user.setId(id);
        user.setName("账号已注销");
        userMapper.updateByPrimaryKey(user);
    }

}
