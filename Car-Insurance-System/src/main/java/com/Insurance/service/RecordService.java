package com.Insurance.service;

import com.Insurance.entity.Record;
import com.Insurance.entity.User;
import com.Insurance.tools.Month;
import com.Insurance.tools.Result;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface RecordService {
    List<Record> getRecordByUserId(User user);

    BigDecimal compute(String cph, String type, User user);

    String addRecord(Record record, User user) throws ParseException;

    Record checkByCph(String id, String cph, String phone);

    Record checkByCno(String id, String cno, String person);

    List<Record> selectListById(String id);

    List<Record> selectAll();

    void addByAdmin(Record record,String time) throws ParseException;

    void deleteByAdmin(String id);

    void updateRecord(Record record,String start,String end) throws ParseException;

    Result<Integer> getData() throws ParseException;

    Month getMonth() throws ParseException;
}
