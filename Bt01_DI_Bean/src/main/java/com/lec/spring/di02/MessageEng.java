package com.lec.spring.di02;

import com.lec.spring.beans.MessageBean;
import org.springframework.stereotype.Component;

@Component
public class MessageEng implements MessageBean {
    String msgEng = "Good Morning";

    // 생성자:
    public MessageEng() {
        System.out.println("MessageEng() 생성");
    }

    @Override
    public void sayHello() {
        System.out.println(msgEng);
    }
}