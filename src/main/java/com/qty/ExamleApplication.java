package com.qty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qty
 * date 2020-02-05
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.qty.mapper"})
public class ExamleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamleApplication.class,args);
    }
}
