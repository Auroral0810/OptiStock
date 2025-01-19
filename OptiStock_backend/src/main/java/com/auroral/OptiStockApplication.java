package com.auroral;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.auroral.mapper")
public class OptiStockApplication
{

    public static void main( String[] args )
    {
        SpringApplication.run(OptiStockApplication.class, args);
    }
}
