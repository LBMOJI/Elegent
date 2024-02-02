package com.Insurance.service.impl;

import com.Insurance.entity.*;
import com.Insurance.mapper.*;
import com.Insurance.service.RecordService;
import com.Insurance.tools.Month;
import com.Insurance.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper mapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BaoxianMapper baoxianMapper;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Override
    public List<Record> getRecordByUserId(User user) {
        RecordExample example = new RecordExample();
        RecordExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(user.getId());
        example.setOrderByClause("id desc");
        List<Record> list = mapper.selectByExample(example);
        if (list.size() == 0)
            return null;
        else
            return list;
    }

    @Override
    public BigDecimal compute(String cph, String type, User user) {
        CarExample carExample = new CarExample();
        CarExample.Criteria criteria1 = carExample.createCriteria();
        criteria1.andUidEqualTo(user.getId());
        criteria1.andCphEqualTo(cph);
        criteria1.andCtypeEqualTo(type);
        criteria1.andStatusNotEqualTo("审核中");
        List<Car> carList = carMapper.selectByExampleWithBLOBs(carExample);
        if (carList.size() == 0) {
            return null;
        }
        return carList.get(0).getPrice();
    }

    @Override
    public String addRecord(Record record, User user) throws ParseException {
        record.setPhone(user.getPhone());

        CarExample carExample = new CarExample();
        CarExample.Criteria criteria1 = carExample.createCriteria();
        criteria1.andUidEqualTo(user.getId());
        criteria1.andCphEqualTo(record.getCph());
        criteria1.andCtypeEqualTo(record.getType());
        criteria1.andStatusNotEqualTo("审核中");
        List<Car> carList = carMapper.selectByExampleWithBLOBs(carExample);
        if (carList.size() == 0) {
            return "车辆未上传或审核中，请检查您上传的信息后重新购买";
        }

        Baoxian baoxian = baoxianMapper.selectByPrimaryKey(record.getBid());
        record.setBname(baoxian.getBname());
        record.setDetail(baoxian.getDetail());

        RecordExample example = new RecordExample();
        RecordExample.Criteria criteria = example.createCriteria();
        criteria.andBnameEqualTo(record.getBname());
        criteria.andCphEqualTo(record.getCph());
        criteria.andTypeEqualTo(record.getType());
        example.setOrderByClause("endtime desc");
        List<Record> list = mapper.selectByExample(example);
        if (list.size() > 0) {
            Date date = list.get(0).getEndtime();
            Date nowDate = new Date();
            if (date.after(nowDate)) {
                return "您的保险还未过期";
            }
        }

        Date date = new Date();
        SimpleDateFormat formatId = new SimpleDateFormat("yyyyMMddHHmm");
        record.setStarttime(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, 1);
        record.setEndtime(cal.getTime());
        record.setCname(carList.get(0).getCname());
        record.setId(formatId.format(date) + user.getId().toString());
        mapper.insert(record);
        return "";
    }

    @Override
    public Record checkByCph(String id, String cph, String phone) {
        RecordExample example = new RecordExample();
        RecordExample.Criteria criteria = example.createCriteria();
        criteria.andCphEqualTo(cph);
        criteria.andIdEqualTo(id);
        criteria.andPhoneEqualTo(phone);
        if (mapper.selectByExample(example).size() != 0) {
            return mapper.selectByExample(example).get(0);
        } else {
            return null;
        }
    }

    @Override
    public Record checkByCno(String id, String cno, String person) {
        Record record = mapper.selectByPrimaryKey(id);
        User user = userMapper.selectByPrimaryKey(record.getUid());
        if (!cno.equals(user.getCno())) {
            return null;
        }
        if (!person.equals(user.getName())) {
            return null;
        }
        return record;
    }

    @Override
    public List<Record> selectListById(String id) {
        RecordExample example = new RecordExample();
        RecordExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        return mapper.selectByExample(example);
    }

    @Override
    public List<Record> selectAll() {
        RecordExample example = new RecordExample();
        example.setOrderByClause("id desc");
        return mapper.selectByExample(example);
    }

    @Override
    public void addByAdmin(Record record,String time) throws ParseException {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(definition);
        try {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat idFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            record.setId(idFormat.format(date) + record.getUid());
            record.setStarttime(date);
            Date endtime = format.parse(time);
            record.setEndtime(endtime);
            User user = userMapper.selectByPrimaryKey(record.getUid());
            record.setPhone(user.getPhone());
            record.setPerson(user.getName());
            CarExample carExample = new CarExample();
            CarExample.Criteria carExampleCriteria = carExample.createCriteria();
            carExampleCriteria.andCphEqualTo(record.getCph());
            carExampleCriteria.andCtypeEqualTo(record.getType());
            carExampleCriteria.andUidEqualTo(record.getUid());
            Car car = carMapper.selectByExample(carExample).get(0);
            record.setCname(car.getCname());
            record.setType(car.getCtype());
            BaoxianExample example = new BaoxianExample();
            BaoxianExample.Criteria criteria = example.createCriteria();
            criteria.andBnameEqualTo(record.getBname());
            Baoxian baoxian = baoxianMapper.selectByExample(example).get(0);
            record.setDetail(baoxian.getDetail());
            mapper.insertSelective(record);
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            platformTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    @Override
    public void deleteByAdmin(String id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateRecord(Record record,String start,String end) throws ParseException {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(definition);
        try {
            Car car = carMapper.selectByPrimaryKey(record.getCph(), record.getType());
            record.setCname(car.getCname());
            User user = userMapper.selectByPrimaryKey(record.getUid());
            record.setPhone(user.getPhone());
            record.setPerson(user.getName());
            BaoxianExample example = new BaoxianExample();
            BaoxianExample.Criteria criteria = example.createCriteria();
            criteria.andBnameEqualTo(record.getBname());
            Baoxian baoxian = baoxianMapper.selectByExample(example).get(0);
            record.setDetail(baoxian.getDetail());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            record.setStarttime(format.parse(start));
            record.setEndtime(format.parse(end));
            mapper.updateByPrimaryKeySelective(record);
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            platformTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    @Override
    public Result<Integer> getData() throws ParseException {
        Result<Integer> result = new Result<>();
        UserExample userExample = new UserExample();

        List<String> user = userMapper.selectByExample(userExample).stream().map(User::getName).collect(Collectors.toList());
        int count = 0;
        for (String name : user) {
            if (name.equals("账号已注销")) {
                count += 1;
            }
        }
        result.setUserCount(count);
        result.setUserDeleted(user.size() - count);

        RecordExample example = new RecordExample();
        RecordExample.Criteria criteria = example.createCriteria();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = "2023-01-01 00:00:00";
        String end = "2024-01-01 00:00:00";
        Date startDate = format.parse(start);
        Date endDate = format.parse(end);
        criteria.andStarttimeBetween(startDate,endDate);
        List<String> list = mapper.selectByExample(example).stream().map(Record::getBname).collect(Collectors.toList());

        Map<String, Integer> map = new HashMap<>();
        for (String str : list) {
            int i = 1;
            if (map.get(str) != null) {
                i = map.get(str) + 1;
            }
            map.put(str, i);
        }

        for (String s : map.keySet()) {
            if (map.get(s) > 0) {
                switch (s) {
                    case "第三者责任险":
                        result.setInsurance1(map.get(s));
                        break;
                    case "机动车辆损失险":
                        result.setInsurance2(map.get(s));
                        break;
                    case "车上人员险-司机":
                        result.setInsurance3(map.get(s));
                        break;
                    case "车上人员险-乘客":
                        result.setInsurance4(map.get(s));
                        break;
                    case "机动车盗抢险":
                        result.setInsurance5(map.get(s));
                        break;
                    case "车身划痕损失险":
                        result.setInsurance6(map.get(s));
                        break;
                    case "车轮单独损失险":
                        result.setInsurance7(map.get(s));
                        break;
                    case "修理期间费用补偿险":
                        result.setInsurance8(map.get(s));
                        break;
                }
            }
        }
        return result;
    }

    @Override
    public Month getMonth() throws ParseException {
        Month month = new Month();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = "2023-01-01 00:00:00";
        String end = "2024-01-01 00:00:00";
        Date startDate = format.parse(start);
        Date endDate = format.parse(end);

        RecordExample example = new RecordExample();
        RecordExample.Criteria criteria = example.createCriteria();
        criteria.andStarttimeBetween(startDate,endDate);
        List<Date> timeList = mapper.selectByExample(example).stream().map(Record::getStarttime).collect(Collectors.toList());
        Calendar cal = Calendar.getInstance();
        try {
            for (Date s : timeList) {
                cal.setTime(s);
                int j = cal.get(Calendar.MONTH);
                switch (j) {
                    case 0:
                        month.setJan(month.getJan() + 1);
                        break;
                    case 1:
                        month.setFeb(month.getFeb() + 1);
                        break;
                    case 2:
                        month.setMar(month.getMar() + 1);
                        break;
                    case 3:
                        month.setApr(month.getApr() + 1);
                        break;
                    case 4:
                        month.setMay(month.getMay() + 1);
                        break;
                    case 5:
                        month.setJune(month.getJune() + 1);
                        break;
                    case 6:
                        month.setJuly(month.getJuly() + 1);
                        break;
                    case 7:
                        month.setAug(month.getAug() + 1);
                        break;
                    case 8:
                        month.setSept(month.getSept() + 1);
                        break;
                    case 9:
                        month.setOct(month.getOct() + 1);
                        break;
                    case 10:
                        month.setNov(month.getNov() + 1);
                        break;
                    case 11:
                        month.setDec(month.getDec() + 1);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return month;
    }
}
