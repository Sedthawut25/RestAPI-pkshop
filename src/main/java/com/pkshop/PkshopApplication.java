package com.pkshop;

import com.pkshop.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableScheduling
public class  PkshopApplication {
    public static void main(String[] args) {
        SpringApplication.run(PkshopApplication.class, args);
    }
}

// แก้ไข stripe ไม่บันทึก payment_Intent  \
//หาโค้ดที่สร้างการอัปเดตสถานะ หลังจ่ายเงินเพื่อแก้ไขปัญหานี้