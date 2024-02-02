package com.Insurance.service.impl;

import com.Insurance.entity.*;
import com.Insurance.mapper.*;
import com.Insurance.service.AcceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AcceptionServiceImpl implements AcceptionService {
    @Autowired
    private AcceptionMapper mapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private SalerMapper salerMapper;
    @Autowired
    private BaoxianMapper baoxianMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Override
    public List<Acception> getAll(Integer uid) {
        AcceptionExample example = new AcceptionExample();
        AcceptionExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        example.setOrderByClause("id desc");
        List<Acception> list = mapper.selectByExample(example);
        if (list.size() != 0) {
            return list;
        } else
            return null;
    }

    @Override
    public Acception checkByCph(Acception acception) {
        AcceptionExample example = new AcceptionExample();
        AcceptionExample.Criteria criteria = example.createCriteria();
        criteria.andCphEqualTo(acception.getCph());
        criteria.andIdEqualTo(acception.getId());
        criteria.andPhoneEqualTo(acception.getPhone());
        if (mapper.selectByExample(example).size() != 0) {
            return mapper.selectByExample(example).get(0);
        } else {
            return null;
        }
    }

    @Override
    public Acception checkByCno(String id, String cno, String name) {
        Acception acception = mapper.selectByPrimaryKey(id);
        User user = userMapper.selectByPrimaryKey(acception.getUid());
        if (!cno.equals(user.getCno())) {
            return null;
        }
        if (!name.equals(user.getName())) {
            return null;
        }
        return acception;
    }

    @Override
    public void addAcception(String id) {
        Record record = recordMapper.selectByPrimaryKey(id);
        Acception acception = new Acception();
        acception.setId(record.getId());
        acception.setBname(record.getBname());
        Date date = new Date();
        acception.setApplytime(date);
        acception.setPhone(record.getPhone());
        acception.setPerson(record.getPerson());
        acception.setUid(record.getUid());
        acception.setCph(record.getCph());
        acception.setType(record.getType());
        acception.setCname(record.getCname());
        acception.setImg(carMapper.selectByPrimaryKey(record.getCph(), record.getType()).getCimg());
        acception.setStatus("未受理");
        acception.setSid(getSid());
        acception.setSname(salerMapper.selectByPrimaryKey(getSid()).getName());
        acception.setSphone(salerMapper.selectByPrimaryKey(getSid()).getPhone());
        mapper.insert(acception);
    }

    @Override
    public List<Acception> getBySalerId(Integer id) {
        AcceptionExample example = new AcceptionExample();
        AcceptionExample.Criteria criteria = example.createCriteria();
        criteria.andSidEqualTo(id);
        example.setOrderByClause("id desc");
        return mapper.selectByExample(example);
    }

    @Override
    public void startAcception(String id) {
        Acception acception = new Acception();
        acception.setStatus("受理中");
        AcceptionExample example = new AcceptionExample();
        AcceptionExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        mapper.updateByExampleSelective(acception, example);
    }

    @Override
    public void finishAcception(String id, BigDecimal payout) {
        Acception acception = new Acception();
        acception.setPayout(payout);
        acception.setStatus("已完成");
        AcceptionExample example = new AcceptionExample();
        AcceptionExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        mapper.updateByExampleSelective(acception, example);
    }

    @Override
    public void refuseAcception(String id) {
        Acception acception = new Acception();
        acception.setStatus("已拒绝");
        AcceptionExample example = new AcceptionExample();
        AcceptionExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        mapper.updateByExampleSelective(acception, example);
    }

    @Override
    public List<Acception> search(String id) {
        AcceptionExample example = new AcceptionExample();
        AcceptionExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        return mapper.selectByExample(example);
    }

    @Override
    public List<Acception> selectAllByAdmin() {
        AcceptionExample example = new AcceptionExample();
        example.setOrderByClause("id desc");
        return mapper.selectByExample(example);
    }

    @Override
    public void addByAdmin(Acception acception) throws Exception {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(definition);
        try {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            acception.setId(format.format(date) + acception.getUid());
            setAcception(acception);
            acception.setApplytime(date);
            mapper.insertSelective(acception);
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            platformTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    @Override
    public void deleteAcception(String id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateAcception(Acception acception) throws Exception {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(definition);
        try {
            setAcception(acception);
            mapper.updateByPrimaryKeySelective(acception);
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            platformTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    @Override
    public boolean backStatus(String id) {
        Acception acception = mapper.selectByPrimaryKey(id);
        switch (acception.getStatus()) {
            case "已完成":
                acception.setStatus("受理中");
                acception.setPayout(null);
                break;
            case "已拒绝":
            case "受理中":
                acception.setStatus("未受理");
                break;
            default:
                return false;
        }
        mapper.updateByPrimaryKeySelective(acception);
        return true;
    }

    private Integer getSid() {
        SalerExample example = new SalerExample();
        SalerExample.Criteria criteria = example.createCriteria();
        criteria.andDemissionEqualTo(0);
        List<Integer> sidList = salerMapper.selectByExample(example).stream().map(Saler::getId).collect(Collectors.toList());
        Random random = new Random();
        int index = random.nextInt(sidList.size() - 1);
        return sidList.get(index);
    }

    private void setAcception(Acception acception) throws Exception {
        User user = userMapper.selectByPrimaryKey(acception.getUid());
        acception.setPerson(user.getName());
        acception.setPhone(user.getPhone());
        Car car = carMapper.selectByPrimaryKey(acception.getCph(), acception.getType());
        acception.setCname(car.getCname());
        acception.setImg(car.getCimg());
        if (car.getUid().compareTo(user.getId()) != 0) {
            throw new Exception("该车信息不在用户账号中");
        }
        BaoxianExample baoxianExample = new BaoxianExample();
        BaoxianExample.Criteria criteria = baoxianExample.createCriteria();
        criteria.andBnameEqualTo(acception.getBname());
        Baoxian baoxian = baoxianMapper.selectByExample(baoxianExample).get(0);
        if (baoxian == null) {
            throw new Exception("未找到该保险");
        }
        acception.setSid(getSid());
        Saler saler = salerMapper.selectByPrimaryKey(acception.getSid());
        acception.setSname(saler.getName());
        acception.setSphone(saler.getPhone());
    }
}
