package com.lec.spring.controller6;

/**
 * 참고: Http 통신을 위한 자바진영 대표적인 라이브러리들.
 *
 * HttpURLConnection : 자바에서 제공하는 기본 객체
 * Retrofit2  :  안드로이드에서 많이 사용됨.
 * Volley  :  안드로이드에서 많이 사용됨.
 * OkHttp
 * RestTemplate : 스프링 에서 제공
 */


import com.lec.spring.domain.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * RestTemplate
 * Http 통신을 위한 객체  (Synchronous 통신)
 * HttpEntity<T>,  RequestEntity<T>, ResponseEntity<T> 등과 함께 주로 활용됨.
 * <p>
 * 공식 레퍼런스 : https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
 * <p>
 * 주요 메소드
 * 1. request method 별 메소드들
 * .getForEntity(...)  .getForObject(...)
 * .postForEntity(...) .postForObject(...)  .postForLocation(...)
 * .put(...)
 * .patchForObject(...)
 * .delete(...)
 * .optionsForAllow(...)
 * .headForHeaders(...)
 * <p>
 * 2. xxxForEntity() 와 xxxForObject() 의 차이
 * <p>
 * 공통점: response 타입을 지정해야 함.
 * 첫번째 매개변수 url 은 'String' 이거나 'URI'객체
 * <p>
 * 차이점: 리턴타입
 * .getForEntity(url, Class<T> responseType)
 * - 리턴타입 : ResponseEntity<T> .  header. body 등의 상세한 정보가 담겨 있다.
 * <p>
 * .getForObject(url, Class<T> responseType)
 * - 리턴타입 : T.   response body 만 원할 경우
 * <p>
 * 2. exchange() 메소드들
 * 아래 메소드들은 모두 ResponseEntity<T> 를 리턴
 * exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables)
 * exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String,?> uriVariables)
 * exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables)
 * exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String,?> uriVariables)
 * exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType)
 * exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType)
 * exchange(RequestEntity<?> entity, Class<T> responseType)
 * exchange(RequestEntity<?> entity, ParameterizedTypeReference<T> responseType)
 */
@RestController
public class RtController1 {
    public static final String HTTPBIN_URL = "https://httpbin.org";

    @Value("${app.api-key.seoul-data}")
    private String seoul_data_key;
    @Value("${app.api-key.kobis}")
    private String kobis_key;

    @RequestMapping("api/test01")
    public String apiTest01() {
        RestTemplate rt = new RestTemplate();

        // T <- getForObject url, Class<T>)
        String result = rt.getForObject(HTTPBIN_URL + "/get", String.class);

        return result;
    }

    // URI 객체를 사용한 요청
    @RequestMapping("api/test02")
    public String apiTest02() {
        URI uri = UriComponentsBuilder
                .fromUriString(HTTPBIN_URL)
                .path("/get")
                .build()    // UriComponents 리턴
                .toUri();

        RestTemplate rt = new RestTemplate();

        var result = rt.getForObject(uri, String.class);
        return result;
    }

    /*
   RestTemplate 으로 요청시 uri 매개변수에는
   String 보다 가급적 Uri 객체를 사용하세요
   (인코딩 이슈등.. 의 문제)
    */


    // ResponseEntity<T> 로 받아오기
    @RequestMapping("api/test03")
    public String apiTest03() {
        URI uri = UriComponentsBuilder
                .fromUriString(HTTPBIN_URL)
                .path("/get")
                .build()    // UriComponents 리턴
                .toUri();

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> result = rt.getForEntity(uri, String.class);

        printResponseEntity(result);

        return result.getBody();
    }

    private void printResponseEntity(ResponseEntity response) {
        System.out.println("""
                status code: %s
                headers: %s
                body: %s
                """.formatted(
                response.getStatusCode(),   // org.springframework.http.HttpStatusCode 리턴
                response.getHeaders(),      // org.springframework.http.HttpHeaders 리턴
                response.getBody()          // T 리턴
        ));
    }


    // 'JSON' 을 -> 'Java 객체' 로 받아올 수 있다.
    @Data
    public static class HttpBinModel {
        String origin;
        String url;
    }


    @RequestMapping("api/test04")
    public HttpBinModel apiTest04() {
        URI uri = UriComponentsBuilder
                .fromUriString(HTTPBIN_URL)
                .path("/get")
                .build()    // UriComponents 리턴
                .toUri();

        RestTemplate rt = new RestTemplate();

        // getForEntity 사용
        // JSON(text) -> HttpBinModel (Java 객체)
//        ResponseEntity<HttpBinModel> result = rt.getForEntity(uri, HttpBinModel.class);
//        System.out.println(result.getBody());
//        return result.getBody();

        HttpBinModel result = rt.getForObject(uri, HttpBinModel.class);
        System.out.println(result);
        return result;
    }


    // UriComponentsBuilder 를 통해 query string 넘겨주기
    //   https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/UriComponentsBuilder.html
    //   queryParam(String name, Object... values)
    //   queryParam(String name, Collection<?> values)
    //   queryParams(MultiValueMap<String, String> params)
    @RequestMapping("api/test05")
    public String apiTest05() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "superman");
        params.add("pw", "1234");

        URI uri = UriComponentsBuilder
                .fromUriString(HTTPBIN_URL)
//                .path("/get?name=John&age=34")  // 안된다 404에러
                .path("/get")

                // queryParam(name, Object...)
                .queryParam("name", "John")
                .queryParam("age", 23)
                .queryParam("name", "Susan")
                .queryParam("score", 80, 90, 100)

                .queryParam("nick", "삼성").encode()

                .queryParam("family", List.of("Susan", "Cathy"))

                .queryParams(params)


                .build()
                .toUri();

        RestTemplate rt = new RestTemplate();
        return rt.getForObject(uri, String.class);
    }

    //────────────────────────────────────────────────────────────────
    // Path Variable 을 활용한 요청
    // 서울시 공공데이터 - 지하철 승하차 정보
    //   샘플 url : http://openapi.seoul.go.kr:8088/4d46796d7366726f3833774a774955/json/CardSubwayStatsNew/1/5/20181001
    @RequestMapping("api/test06/{start_index}/{end_index}/{date}")
    public ResponseEntity<String> apiTest06(
            @PathVariable("start_index") Integer startIndex,
            @PathVariable("end_index") Integer endIndex,
            @PathVariable("date") String date
    ) {
        //        String uri = String.format("http://openapi.seoul.go.kr:8088/%s/json/CardSubwayStatsNew/%d/%d/%s"
        //                            , seoul_data_key, startIndex, endIndex, date);

        // .build() 의 결과는 UriComponents 다.
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/UriComponents.html


        // UriComponents 의 expand() 메소드를 사용하여
        //   "{ .. }" URI template variable 들을 주어진 값으로 차례대로 치환
        //   .expand(Object... uriVariableValues)
        //   .expand(Map<String,?> uriVariables)
        //   .expand(UriComponents.UriTemplateVariables uriVariables)

        String baseUri = "http://openapi.seoul.go.kr:8088/{key}/json/CardSubwayStatsNew/{startIndex}/{endIndex}/{date}";

        URI uri = UriComponentsBuilder
                .fromUriString(baseUri)
                .build()
                .expand(seoul_data_key, startIndex, endIndex, date)
                .toUri();

        System.out.println("uri: " + uri);


        RestTemplate rt = new RestTemplate();
        String result = rt.getForObject(uri, String.class);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);

    }


    // 영화진흥위원회 - 영화목록조회
    @RequestMapping("api/test07")
    public ResponseEntity<?> apiTest07(String movieNm, Integer openStartDt, Integer itemPerPage, Integer curPage) {
        String baseUrl = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .queryParam("key", kobis_key)
                .queryParam("movieNm", movieNm)
                .queryParam("openStartDt", openStartDt)
                .queryParam("itemPerPage", itemPerPage)
                .queryParam("curPage", curPage)
                .build()
                .encode()
                .toUri();

        String result = new RestTemplate().getForObject(uri, String.class);
        return ResponseEntity.ok()
//                .header("Content-type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    //────────────────────────────────────────────────────────────────
    // POST 요청
    @RequestMapping("api/test10")
    public String apiTest10() {
        URI uri = UriComponentsBuilder
                .fromUriString(HTTPBIN_URL)
                .path("/post")
                // .encode()
                .build()
                .toUri();

        RestTemplate rt = new RestTemplate();
        String result = null;

        // postForObject()!  post 방식으로
        //   postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
        //   postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
        //   postForObject(URI, Object request, Class<T> responseType)

        User user = new User(34, "홍길동");

//        result = rt.postForObject(uri, null, String.class);   // 요청의 Content-type 이 x-www-form-urlencoded 방식으로 전달
        result = rt.postForObject(uri, user, String.class);     // 요청의 Content-type 이 application/json 방식으로 전달

        return result;

    }

    //────────────────────────────────────────────────────────────────
    // exchange 사용
    // exchange(RequestEntity<?> entity, Class<T> responseType) 사용
    @RequestMapping("api/test21")
    public ResponseEntity<String> apiTest21() {
        User user = new User(26, "김봉남");
        RestTemplate rt = new RestTemplate();

        RequestEntity<User> entity = RequestEntity
                .post(HTTPBIN_URL + "/post")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth", "abcd")
                .header("x-key", "1234")
                .body(user);
        ResponseEntity<String> result = rt.exchange(entity, String.class);

        return result;

    }

    //────────────────────────────────────────────────────────────────
    // exchange 사용
    // exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables)
    // post 요청 body 에 name-value 쌍으로 요청하기 - x-www-form-urlencoded
    @RequestMapping("api/test22")
    public ResponseEntity<String> apiTest22() {

        // 1. header 준비
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("user-secret", "xxxx");

        // 2. body 데이터 준비
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("x_auth", "James");
        params.add("x_secret", "1234");

        // 3. header 와 body 를 담은 HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        // 4. exchange() 요청 <- HttpEntity 사용
        ResponseEntity<String> result = new RestTemplate().exchange(
                HTTPBIN_URL + "/{method}",  // url
                HttpMethod.POST,    // request method
                httpEntity,     // HttpEntity (body + header)
                String.class,       // response type
                "post"      // uri variables ...
        );

        return result;
    }



}
