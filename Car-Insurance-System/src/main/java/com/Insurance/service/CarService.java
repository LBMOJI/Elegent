package com.Insurance.service;

import com.Insurance.entity.Car;
import com.Insurance.entity.User;

import java.util.List;

public interface CarService {
    List<Car> getListByUserId(User user);

    void insertCar(Car car);

    List<Car> NotAudited(User user);

    List<Car> AuditedFailed(User user);

    List<Car> selectCarAndUser();

    List<Car> searchCph(String cph);

    List<Car> selectAll();

    List<Car> selectByStatus(String status);

    void refuseCar(String cph, String ctype);

    void passCar(String cph, String ctype);

    void backStatus(String cph, String status, String ctype);

    void deleteCar(String cph, String ctype);

    List<Car> selectByCph(String cph);
}
