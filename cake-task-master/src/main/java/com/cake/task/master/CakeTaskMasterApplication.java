package com.cake.task.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CakeTaskMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CakeTaskMasterApplication.class, args);
    }

}
