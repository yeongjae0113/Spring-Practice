package com.lec.spring.di03;

import com.lec.spring.beans.Score;
import com.lec.spring.beans.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//JAVA 를 이용한 DI 설정
//클래스 이름앞에 반드시 아래 어노테이션 명시 필요
//@Configuration --> 이 클래스는 '스프링 설정'에 사용되는 클래스 입니다.
//결국 IOC 컨테이너에 생성되는 bean 들을 기술하는 클래스 --> @Bean 사용

@Configuration
public class Config03 {

    public Config03(){
        System.out.println("Config03() 생성");
    }

    @Bean  // 리턴타입의 bean 객체 생성. (기본적으로) bean 의 이름은 메소드 이름으로 설정
    public Score score1(){   // Score 타입의 bean 생성. bean 의 이름은 score1
        return new Score(100, 80, 75, "좋아요");
    }

    @Bean
    public Student stu1(){  // Student 타입의 bean 생성.  bean 의 이름은 stu1
        return new Student("홍길동", 34, score1());
    }
}














