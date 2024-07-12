package com.lec.spring.controller1;


import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *  org.springframework.http 패키지의 주요 객체
 *
 *  HttpEntity<T> :
 *      │          HTTP request, 혹은 HTTP response 표현객체. 'headers' 와 'body' 로 구성됨
 *      │          request 와 response 를 보다 세밀하게 제어할수 있다.
 *      │                  └ ① header 설정
 *      │                  └ ② body 설정
 *      │
 *      │          SpringMVC @Controller 메소드의 return 값으로도 사용
 *      │          RestTemplate 에서도 활용
 *      │          https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html
 *      │
 *      └─ RequestEntity<T>
 *      │          HTTP request 표현객체
 *      │          SpringMVC @Controller 메소드의 '입력값', RestTemplate 에서 활용
 *      │          https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/RequestEntity.html
 *      │
 *      └─ ResponseEntity<T>
 *                 HTTP response 표현객체
 *                 Status Code 도 설정 가능
 *                 SpringMVC @Controller 메소드의 '리턴값', RestTemplate 에서 활용
 *                 https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html
 *
 *  MultiValueMap (I)
 *      └─ HttpHeaders
 *          Http header(들) 표현 객체
 *          https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpHeaders.html
 *
 *  MediaType
 *       Http 요청과 응답의 Content type 표현 객체
 *       https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/MediaType.html
 */
@Controller
public class HomeController2 {

    public final String str1 =  "<h1>안녕하세요 Hello!</h1>";

    String[] jsonArr = {
            // [0]
            """
        {
            "status": "성공",
            "code": 34200,
            "response_time": "2023-07-03 13:09:06"
        }
        """,
    };

    @RequestMapping("http01")
    @ResponseBody
    public HttpEntity<String> http01(){
        String body = str1;
        // HttpEntity<T> : T 는 response 할 body 의 타입
        HttpEntity<String> entity = new HttpEntity<>(body);
        return entity;
        // 기본적으로 Content type 은 text/html;Charset=UTF-8 로 response 된다.
    }

    @RequestMapping("http02")
    @ResponseBody
        public HttpEntity<String> http02() throws UnsupportedEncodingException {
            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.TEXT_PLAIN);   // text/plain 으로 response

            headers.add("Content-type", "text/plain;charset=UTF-8");
            headers.add("user-secret", "xxxx");
//            headers.add("my-team", "삼성개못함");    // 에러 헤더에 한글 불가
            headers.add("my-team", URLEncoder.encode("삼성개못함", "utf-8"));

            String body = str1;
            HttpEntity<String> entity = new HttpEntity<>(body, headers);     // HttpEntity(body, headers)
            return entity;
    }

    // 일반적으로 response 는 ResponseEntity 사용
    @RequestMapping("http03")
    @ResponseBody
    public ResponseEntity<String> http03() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //  응답 콘텐츠 타입이 제이슨.  / application/json

        String body = jsonArr[0];
        ResponseEntity<String> entity = new ResponseEntity<>(body, headers, HttpStatus.OK); // (body, headers, statusCode)
        return entity;
    }

    // body 의 어떠한 Java 객체도 가능
    @RequestMapping("http04")
    @ResponseBody
    public ResponseEntity<HomeController.Product> http04() {
        HomeController.Product product = new HomeController.Product(10, "신성태", false);
        ResponseEntity<HomeController.Product> entity = new ResponseEntity<>(product, HttpStatus.OK);
        return entity;
    }

    //  <T> 타입 명시하기 귀찮다면 ?
    // type parameter 를 ? (혹은 Object) 로 작성해두면 편리하다 (일반적으로 많이 쓰임)
    // ※ 개발단계에서 response body 의 타입은 유동적으로 변할수 있기 때문이다.

    @RequestMapping("http05")
    @ResponseBody
    public ResponseEntity<?> http05() {
        var product = new HomeController.Product(11, "바이크", true);
        ResponseEntity<?> entity = new ResponseEntity<>(product, HttpStatus.OK);
        return entity;
    }

    @RequestMapping("http06")
    @ResponseBody
    public ResponseEntity<?> http06() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 에러
        //  new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // 404
        //  new ResponseEntity<>(null, HttpStatus.CONFLICT);   // 409

    }

    @RequestMapping("http07")
    @ResponseBody
    public void http07() {
        // TODO
    }



}