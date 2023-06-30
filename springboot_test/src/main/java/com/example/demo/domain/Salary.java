package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Salary {
    private long id;
    private long employeeId;
    private BigDecimal money;
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date month;
}
