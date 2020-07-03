package com.zhliang.uid.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.pzy.zhliang.uid.baidu.worker.dao")
@SpringBootApplication
public class SpringbootUidSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootUidSampleApplication.class, args);
    }

}
