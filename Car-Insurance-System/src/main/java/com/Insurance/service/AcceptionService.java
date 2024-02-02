package com.Insurance.service;

import com.Insurance.entity.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AcceptionService {
    List<Acception> getAll(Integer uid);

    Acception checkByCph(Acception acception);

    Acception checkByCno(String id,String cno,String name);

    void addAcception(String id);

    List<Acception> getBySalerId(Integer id);

    void startAcception(String id);

    void finishAcception(String id, BigDecimal payout);

    void refuseAcception(String id);

    List<Acception> search(String id);

    List<Acception> selectAllByAdmin();

    void addByAdmin(Acception acception) throws Exception;

    void deleteAcception(String id);

    void updateAcception(Acception acception) throws Exception;

    boolean backStatus(String id);

}
