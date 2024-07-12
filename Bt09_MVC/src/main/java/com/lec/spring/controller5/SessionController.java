package com.lec.spring.controller5;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

@Controller
@RequestMapping("/session")
public class SessionController {

    // HttpSession 객체
    //  현재 request 한 client 에 대한 Session 정보
    @RequestMapping("/list")
    public void list(HttpSession session, Model model) {

        // 세션에 있는 '모든' attr names 들 뽑아내기
        // name: String 타입
        Enumeration<String> enumeration = session.getAttributeNames();

        StringBuffer buff = new StringBuffer();
        int i = 0;
        while(enumeration.hasMoreElements()) {
            // enumeration => 컬렉션의 요소들을 하나씩 순회할 수 있게 해주는 객체
            // ahsMoreElements => 컬렉션에 더이상 순회할 요소가 있는지 확인하는 메소드 있으면 true, 없으면 false
            String sessionName = enumeration.nextElement();
            // session.getAttribute('name')  <-- 특정 세션 attr value 추출. 리턴타입 Object. 해당 name 이 없으면 null 리턴
            String sessionValue = session.getAttribute(sessionName).toString();
            buff.append((i + 1) + "] " + sessionName + " : " + sessionValue + "<br>");
            i++;
        }
        if(i == 0){
            buff.append("세션안에 attribute 가 없다.<br>");
        }
        model.addAttribute("result", buff.toString());
    }


    @RequestMapping("/create")
    public String create(HttpSession session) {
        String sessionName, sessionValue;


        sessionName = "num1";
        sessionValue = "" + (int)(Math.random() * 100);


        // 세션 attr : name-value 생성
        // setAttribute(String name, Object value) 두번째 매개변수는 Object 타입이다
        session.setAttribute(sessionName, sessionValue);

        sessionName = "datetime";
        sessionValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        session.setAttribute(sessionName, sessionValue);

        return "redirect:/session/list";
    }

    @RequestMapping("/delete")
    public String delete(HttpSession session) {
        // removeAttribute(name) 세션 attribute 삭제
        session.removeAttribute("num1");

        return "redirect:/session/list";
    }


    // -------------------------------------------------
    public static final String ADMIN_ID = "admin";
    public static final String ADMIN_PW = "1234";

    @GetMapping("/login")
    public void login(HttpSession session, Model model) {
        if(session.getAttribute("username") != null) {
            model.addAttribute("username", session.getAttribute("username"));
            // 현재 세션에서 가져온 "username" 값
        }
    }
    @PostMapping("/login")
    public String loginOk(String username, String password, HttpSession session, Model model) {

        // 세션 name-value 지정
        String sessionName = "username";
        String sessionValue = username;

        // 제출된 id/ pw 값이 일치하면 로그인 성공 + 세션 attr 생성
        if (ADMIN_ID.equalsIgnoreCase(username) && ADMIN_PW.equals(password)) {
            session.setAttribute(sessionName, sessionValue);
            model.addAttribute("result", true);
        } else {
            session.removeAttribute(sessionName);
        }
        return "session/loginOk";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {

        String sessionName = "username";

        // 세션 삭제
        session.removeAttribute(sessionName);

        return "session/logout";
    }

}   // end controller
