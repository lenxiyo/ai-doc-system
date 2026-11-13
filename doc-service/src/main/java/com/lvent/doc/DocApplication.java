package com.lvent.doc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lvent.doc.mapper")
public class DocApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocApplication.class,args);
    }
}
