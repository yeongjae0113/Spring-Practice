package com.lec.spring.controller2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/board")  // ← 이 컨트롤러는 "/board" 로 시작하는 url 에 매핑된다.
public class BoardController {

    @RequestMapping("/list")
    public void listBoard() {

    }
    @RequestMapping("/write")
    public void writeBoard() {

    }
    @RequestMapping("/detail")
    public void detailBoard() {

    }
    @RequestMapping("/update")
    public void updateBoard() {

    }
    @RequestMapping("/delete")
    public void deleteBoard() {

    }



}
