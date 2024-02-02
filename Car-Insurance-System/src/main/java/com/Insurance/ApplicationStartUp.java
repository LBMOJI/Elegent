package com.Insurance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan(basePackages = "com.Insurance.mapper")
public class ApplicationStartUp implements CommandLineRunner {

    public static void main(String[] args){
        SpringApplication.run(ApplicationStartUp.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始加载指定页面");
        try{
            Runtime.getRuntime().exec("cmd  /c  start   http://localhost:8080/index");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
