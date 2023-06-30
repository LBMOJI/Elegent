package com.example.demo.Controller;

import com.example.demo.domain.Employee;
import com.example.demo.domain.Salary;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@RequestMapping("employees")
@RestController
public class EmployeeController {

    @GetMapping
    public List<Employee> findAll() {
        ArrayList<Employee> list = new ArrayList<>();
        list.add(new Employee(1L, "dave"));
        list.add(new Employee(2L, "lucy"));
        return list;
    }

    @GetMapping("{id}")
    public Employee findOne(@PathVariable(name = "id") Long id) {
        Map<Long, Employee> employeeMap = new HashMap<>();
        employeeMap.put(1L, new Employee(1L, "dave"));
        employeeMap.put(2L, new Employee(2L, "lucy"));
        return employeeMap.get(id);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable(name = "id") Long id, HttpServletResponse res) {
        System.out.println("删除id为" + id + "的员工");
        res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @PostMapping("{employeeId}/Salaries")
    public Salary saveSalary(Salary salary) {
        salary.setId(1L);
        return salary;
    }

    @GetMapping("{employeeId}/Salaries/{month}")
    public Salary findOne(@PathVariable(name = "employeeId") Long employeeId,
                          @PathVariable(name = "month") @DateTimeFormat(pattern = "yyyy-MM") Date month) {
        return new Salary(1L, employeeId, BigDecimal.TEN, month);
    }
}
