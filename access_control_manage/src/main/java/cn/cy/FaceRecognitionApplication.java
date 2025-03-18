package cn.cy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.cy.mapper")
public class FaceRecognitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceRecognitionApplication.class, args);
    }

}
