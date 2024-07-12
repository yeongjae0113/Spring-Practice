package com.lec.spring.repository;

import com.lec.spring.domain.Gender;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserHistory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;

@SpringBootTest
class UserRepositoryTest {

    @Autowired      // Autowired 해야 주입받을 수 있음
    private UserRepository userRepository;

    @Autowired
            private UserHistoryRepository userHistoryRepository;

//    @Test
    void crud() {
        System.out.println("\n-- TEST#crud() ---------------------------------------------");
//        System.out.println(userRepository.findAll());       // (User 테이블에 있는) SELECT 전체 조회 > SELECT * FROM T_USER; => 리턴타입 List
//        User user = new User();     // Java 객체
//        System.out.println("user: " + user);
//
//        userRepository.save(user);   // INSERT, 저장하기 -> 영속화된 객체!
//        userRepository.findAll().forEach(System.out::println);  // SELECT
//        System.out.println("user:" + user);
        // 테스트에 사용할 변수들
        List<User> users = null;
        User user1 = null, user2 = null, user3 = null, user4 = null, user5 = null;
        List<Long> ids = null;

        // #002 findAll()
        users = userRepository.findAll();
        users.forEach(System.out::println);

        System.out.println();

        // #003 findXX() + Sort.by() 사용
        users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));   // name 프로퍼티를 역순으로
        users.forEach(System.out::println);

        // #004 findXXByXX(Iterable)
        ids = List.of(1L, 3L, 5L);
        users = userRepository.findAllById(ids);
        users.forEach(System.out::println);

        // #005 save(entity) 저장하기
        user1 = new User("jack", "jack@redknight.com");     // user1 => 자바 객체
        userRepository.save(user1);     // INSERT

        userRepository.findAll().forEach(System.out::println);

        // #006 saveAll(Iterable)
        user1 = new User("jack", "jack@redknight.com");     // user1, user2 => 자바 객체
        user2 = new User("steve", "steve@redknight.com");
        userRepository.saveAll(List.of(user1, user2));
        userRepository.findAll().forEach(System.out::println);

        // #007 findById(id)
        // 리턴타입은 Optional<Entity> => Optional 이란> null 값(nullpointexception 을 방지하기 위해 사용 => Entity 안에 값이 없다면 empty 로 !
        Optional<User> user;
        user = userRepository.findById(1L);
        System.out.println(user);   // 현재 user 는 Optional 객체
        System.out.println(user.get());

        user = userRepository.findById(10L);    // 10번 id 가 없다면 ?
        System.out.println(user);   // Optional.empty

        user1 = userRepository.findById(10L).orElse(null);
        System.out.println(user1);  // null

        // #008 flush()
        // flush() 는 SQL 쿼리의 변화를 주는게 아니라 DB 반영 시점을 조정한다. 로그 출력으로는 변화를 확인하기 힘들다
        // flush 메소드란? insert 를 하여도 바로 디비에 저장이(반영) 안되는데 위 메소드를 사용하면 데이터베이스에 즉시 반영
        userRepository.save(new User("new martin", "newmartin@redknight.com"));     // save 메소드를 사용하면 INSERT 실행
        userRepository.flush();

        // saveAndFlush() = save() + flush()
        userRepository.saveAndFlush(new User("베리베리", "베리@berry.com"));
        userRepository.findAll().forEach(System.out::println);

        // #009 count()
        Long count = userRepository.count();    // Long 타입 리턴
        System.out.println(count);

        // #010 existsById()
        boolean exists = userRepository.existsById(1L);     // id 값을 where 절로 받음
        System.out.println(exists);     // 존재하는지 여부는 count 함수를 사용하여 true, false 로 확인

        // #011 delete(entity)
        // 삭제하기 => 영속성 객체에서 가져와 삭제 (영속성 객체란? 영구 저장소에 저장되어 있는 데이터)
//        userRepository.delete(userRepository.findById(1L).orElse(null));
        userRepository.findAll().forEach(System.out::println);
        // 순서 !
        // 1. findById 에서 select
        // 2. delete 를 하기 전에 해당 entity 가 있는지 select 먼저 수행
        // 2. delete
        // 3. findAll()

        // delete 메소드는 null 값 허용 안함
//        userRepository.delete(userRepository.findById(1L).orElse(null));
        // 차라리 예외 발생하고 처리하도록 하는게 좋다 !
//        userRepository.delete(userRepository.findById(1L).orElseThrow(RuntimeException::new));

        // #012 deleteById(id)
//        userRepository.deleteById(1L);
//        userRepository.deleteById(2L);
        // 순서 !
        // 1번 id 가 있는지 select 쿼리로 확인 한 후
        // delete 쿼리 작동 => 1번이 없다면 select 쿼리에서 끝나고 1번이 있다면 delete 쿼리 작동

        // #013 deleteAll()
        System.out.println(new String("베리").repeat(10));
//        userRepository.deleteAll();     // SELECT 1회 + DELETE x n 회

        // #014 deleteAll(Iterable) => iterable 객체란? 반복 가능한 객체. forEach 와 사용 가능
//        userRepository.deleteAll(userRepository.findAllById(List.of(1L, 3L)));
        // id 값이 없기 때문에 in 연산자로 SELECT 1번하고 끝
        // 값이 있었다면 SELECT n회 + DELETE n회

        // deleteAll() 은 성능이슈 발생 !
        // #015 deleteInBatch()
//        userRepository.deleteInBatch(userRepository.findAllById(List.of(3L, 4L, 5L)));

        // #016
//        userRepository.deleteAllInBatch();

        // Batch 가 없는 delete 는 ? -> DELETE x n회
        // Batch 가 있는 delete 는 ? -> 한 번의 DELETE

        // #017 findXXX(Pageable)  페이징
        // PageRequest 는 Pageable 의 구현체  org.springframework.data.domain.PageRequest
        // 리턴값은 Page<T>  org.springframework.data.domain.Page
        // 주의: page 값은 0-base 다 => 0 베이스 이기 때문에 첫 번째 페이지는 0 두 번째 페이지는 1
        Page<User> page = userRepository.findAll(PageRequest.of(3, 3));     // page1 (두 번째 페이지)

        // Page<T> 의 메소드들
        System.out.println("page: " + page);
        System.out.println("total");
        System.out.println("totalElements: " + page.getTotalElements());
        System.out.println("totalPages: " + page.getTotalPages());
        System.out.println("numberOfElements: " + page.getNumberOfElements());
        System.out.println("sort: " + page.getSort());
        System.out.println("size: " + page.getSize());  // 페이징 할때 나누는 size

        page.getContent().forEach(System.out::println); // 해당 페이지 내부의 데이터 (들)

        // #019 QueryByExample 사용
        ExampleMatcher matcher = ExampleMatcher.matching()       // 검색 조건을 담는 객체
//                .withIgnorePaths("name")            // name 컬럼은 매칭하지 않음
                .withMatcher("email", endsWith());   // email 컬럼은 뒷 부분으로 매칭하여 검색

        //Example.of(probe, ExampleMatcher)  <-- 여기서 probe 란 실제 Entity 는 아니란 말입니다.  match 를 위해서 쓰인 객체
        Example<User> example = Example.of(new User("ma", "knight.com"), matcher);      // probe

        userRepository.findAll(example).forEach(System.out::println);


        // #020
        user1 = new User();
        user1.setEmail("blue");

        // email 필드에서 주어진 문자열을 담고 있는 것을 검색
        matcher = ExampleMatcher.matching().withMatcher("email", contains());
        example = Example.of(user1, matcher);
        userRepository.findAll(example).forEach(System.out::println);

        // UPDATE !!
        // save() 는 INSERT 뿐 아니라 UPDATE 도 수행. 오잉!?

        userRepository.save(new User("유인아", "You-in-Ah@베리베리.com"));  // INSERT : @Id 가 null 인 경우
//
        user1 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user1.setEmail("마우스가@움직여요.com");
        userRepository.save(user1);   // SELECT + UPDATE, id=1L 인 User 를 수정

        userRepository.findAll().forEach(System.out::println);


        System.out.println("------------------------------------------------------------\n");
    }



    // 테스트에 사용할 변수들
    List<User> users;
    User user, user1, user2, user3, user4, user5;
    List<Long> ids;
    Optional<User> optUser;


    // @BeforeEach : 매 테스트 메소드가 실행되기 직전에 실행
    // @BeforeEach 메소드의 매개변수에 TestInfo 객체를 지정하면
    // JUnit Jupiter 에선 '현재 실행할 test' 의 정보가 담긴 TestInfo 객체를 주입해준다
    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("─".repeat(40));


        users = null;
        user = user1 = user2 = user3 = user4 = user5 = null;
        ids = null;
        optUser = null;
    }

    // @AfterEach : 매 테스트 메소드가 종료된 직후에 실행
    @AfterEach
    void afterEach() {
        System.out.println("-".repeat(40) + "\n");
    }


    /***********************************************************************
     * Query Method
     */


    // 다양한 Query Return Types
    //   https://docs.spring.io/spring-data/jpa/reference/repositories/query-return-types-reference.html
    //   => void, Primitive, Wrapper, T, Iterator<T>, Collection<T>, List<T>, Optional<T>, Option<T>, Stream<T> ...
    // 쿼리 메소드의 리턴타입은 SELECT 결과가  '1개인지' 혹은 '복수개인지' 에 따라, 위에서 가용한 범위내에서 설정해서 선언


    @Test
    void qryMethod01() {
        System.out.println(userRepository.findByName("dennis"));
        // 리턴타입이 User 이면 에러다.  <- 여러개를 리턴하는 경우. (UserRepository.java 에서 선언)
//        System.out.println(userRepository.findByName("martin"));
//        userRepository.findByName("martin").forEach(System.out::println);
    }

    // 쿼리 메소드의 naming
    //  https://docs.spring.io/spring-data/jpa/reference/repositories/query-keywords-reference.html
    //     find…By, read…By, get…By, query…By, search…By, stream…By
    @Test
    void qryMethod002() {
        String email = "martin@redknight.com";
        System.out.println("findByEmail : " + userRepository.findByEmail(email));
        System.out.println("getByEmail : " + userRepository.getByEmail(email));
        System.out.println("readByEmail : " + userRepository.readByEmail(email));
        System.out.println("queryByEmail : " + userRepository.queryByEmail(email));
        System.out.println("searchByEmail : " + userRepository.searchByEmail(email));
        System.out.println("streamByEmail : " + userRepository.streamByEmail(email));
        System.out.println("findUserByEmail : " + userRepository.findUsersByEmail(email));
    }


    @Test
    void qryMethod03() {
        System.out.println(userRepository.findYouinahByEmail("martin@redknight.com"));
    }

    // First, Top
    //   First 와 Top 은 둘다 동일 (가독성 차원에서 제공되는 것임)
    @Test
    void qryMethod005() {
        String name = "martin";
        System.out.println("findTop1ByName : " + userRepository.findTop1ByName(name));
        System.out.println("findTop2ByName : " + userRepository.findTop2ByName(name));
        System.out.println("findFirst1ByName : " + userRepository.findFirst1ByName(name));
        System.out.println("findFirst2ByName : " + userRepository.findFirst2ByName(name));
    }

    // And, Or
    @Test
    void qryMethod007() {
        String email = "martin@redknight.com";
        System.out.println("findByEmailAndName : " + userRepository.findByEmailAndName("martin@redknight.com", "martin"));
        System.out.println("findByEmailAndName : " + userRepository.findByEmailAndName("martin@redknight.com", "dennis"));
        System.out.println("findByEmailOrName : " + userRepository.findByEmailOrName("martin@redknight.com", "dennis"));    // 이메일이 찾는 조건이거나 이름이 찾는 조건일 때
    }

    // After, Before
    // After, Before 는 주로 '시간'에 대해서 쓰이는 조건절에 쓰인다.  (가독성 측면)
    // 그러나, 꼭 '시간'만을 위해서 쓰이지는 않는다 .   '숫자', '문자열' 등에서도 쓰일수 있다.
    @Test
    void qryMethod008() {
        System.out.println("findByCreatedAtAfter : " + userRepository.findByCreatedAtAfter(
                LocalDateTime.now().minusDays(1L)
        ));

        System.out.println("findByIdAfter : " + userRepository.findByIdAfter(4L));
        System.out.println("findByNameBefore : " + userRepository.findByNameBefore("martin"));
    }

    // GreaterThan, GreaterThanEqual, LessThan, LessThanEqual
    @Test
    void qryMethod009() {
        System.out.println("findByCreatedAtGreaterThan : " + userRepository.findByCreatedAtGreaterThan(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByNameGreaterThanEqual : " + userRepository.findByNameGreaterThanEqual("martin"));
        // GraterThan       -------- [  > ]
        // GreaterThanEqual -------- [ >= ]
        // LessThan         -------- [  < ]
        // LessThanEqual    -------- [ <= ]
    }


    // Between
    @Test
    void qryMethod010() {
        System.out.println("findByCreatedAtBetween : "          // between A and B 로 쿼리문 작성됌
                + userRepository.findByCreatedAtBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L)));
        System.out.println("findByIdBetween : "                 // between A and B 로 쿼리문 작성됌
                + userRepository.findByIdBetween(1L, 3L));
        System.out.println("findByIdGreaterThanEqualAndIdLessThanEqual : "      // where 절 쿼리로 and 추가
                + userRepository.findByIdGreaterThanEqualAndIdLessThanEqual(1L, 3L));
    }


    // Empty 와 Null
    //   - IsEmpty, Empty
    //   - IsNotEmpty, NotEmpty,
    //   - NotNull, IsNotNull
    //   - Null, IsNull
    @Test
    void qryMethod011() {
        System.out.println("findByIdIsNotNull : "
                + userRepository.findByIdIsNotNull());      // WHERE id IS NOT NULL


//        System.out.println("findByIdIsNotEmpty : "
//                + userRepository.findByAddressIsNotEmpty());    // Empty 이지 않은 것을 SELECT


//      System.out.println("findByAddressIsNotEmpty : " + userRepository);
    }


    // In, NotIn
    @Test
    void qryMethod012() {
        System.out.println("findByNameIn : "
                + userRepository.findByNameIn(List.of("martin", "dennis")));    // List.of 는 null 을 허용하지 않고 간결하고 안전한 방식으로 List 를 생성하기 위해 사용
        // WHERE name IN (?, ?)
    }


    // StartingWith, EndingWith, Contains
    // 문자열에 대한 검색쿼리, LIKE 사용
    @Test
    void qryMethod013() {
        System.out.println("findByNameStartingWith : "
                + userRepository.findByNameStartingWith("mar"));    // name 컬럼이 "mar" 로 시작하는 것을 SELECT
        System.out.println("findByNameEndingWith : "
                + userRepository.findByNameEndingWith("s"));        // name 컬럼이 "s" 로 끝나는 것을 SELECT
        System.out.println("findByEmailContains : "
                + userRepository.findByEmailContains("red"));       // email 컬럼이 "red" 가 있는 것을 SELECT
    }


    // Like
    // 사실 위의 키워드는 Like 를 한번 더 wrapping 한거다.
    // Like 검색 시 %, _ 와 같은 와일드 카드 사용
    @Test
    void qryMethod014() {
        System.out.println("findByEmailLike : "
                + userRepository.findByEmailLike("%" + "dragon" + "%"));
    }


    // Is, Equals
    // 특별한 역할은 하지 않는다. 생략가능. 가독성 차원에서 남겨진 키워드입니다.
    @Test
    void qryMethod015() {
        System.out.println(userRepository);
    }


    /***********************************************************************
     * Query Method - Sorting & Paging
     */


    // Naming 기반 정렬
    // Query method 에 OrderBy 를 붙임
    @Test
    void qryMethod016() {
        System.out.println("findTop1ByName : "
                + userRepository.findTop1ByName("martin"));
        System.out.println("findLast1ByName : "
                + userRepository.findLast1ByName("martin"));    // Last1 는 무시


        System.out.println("findTopByNameOrderByIdDesc : "
                + userRepository.findTopByNameOrderByIdDesc("martin"));
    }


    // 정렬기준 추가
    @Test
    void qryMethod017() {
        System.out.println("findFirstByNameOrderByIdDesc : "
                + userRepository.findFirstByNameOrderByIdDesc("martin"));           // order by 조건절 1개 => id
        System.out.println("findFirstByNameOrderByIdDescEmailDesc : "
                + userRepository.findFirstByNameOrderByIdDescEmailDesc("martin"));  // order by 조건절 2개 => id 와 email
    }


    // 매개변수(Sort) 기반 정렬
    // Query method 에 Sort 매개변수로 정렬옵션 제공
    @Test
    void qryMethod018() {
        System.out.println("findFirstByName + Sort : "
                + userRepository.findFirstByName("martin", Sort.by(Sort.Order.desc("id"))));
        // 이름이 "martin"인 사용자들을 id 필드 기준으로 내림차순 정렬한 후, 첫 번째 사용자를 반환
        System.out.println("findFirstByName + Sort : "
                + userRepository.findFirstByName("martin", Sort.by(Sort.Order.desc("id"), Sort.Order.asc("email"))));
        // id 필드 기준으로 내림차순 정렬하고, id 값이 동일한 경우 email 필드 기준으로 오름차순 정렬된 사용자 중 첫 번째 사용자를 반환
    }

    // ↑ 무엇이 더 낳을까?
    //   Naming 기반 정렬 vs. 매개변수(Sort) 기반 정렬
    //   'Naming 기반 정렬' 은 유연성이 떨어지고..
    //      정렬 조건이 많아지면 길어지면 메소드도 많이 생성해야 하고 메소드 이름이 길어지니까 가독성이 안좋다.
    //   '매개변수 기반 정렬' 은 메소드 하나로 여러 정렬 조건을 다룰수 있다.
    //     메소드 하나만 정의해놓고, 사용하는 쪽에서 정렬조건을 제공할수 있다
    //     유연성, 자유도, 가독성 좋다.



    @Test
    void qryMethod018_2() {
        System.out.println("findFirstByName + Sort : "
                + userRepository);
        System.out.println("findFirstByName + Sort : "
                + userRepository);
        System.out.println("findFirstByName + Sort : "
                + userRepository);
        System.out.println("findFirstByName + Sort : "
                + userRepository);
    }
    private Sort getSort() {
        return Sort.by(
                Sort.Order.desc("id"),
                Sort.Order.asc("email").ignoreCase(),
                Sort.Order.desc("createdAt"),
                Sort.Order.asc("updatedAt")
        );
    }

    @Test
    void qryMethod018_3() {
        System.out.println("findFirstByName + Sort : "
                + userRepository.findFirstByName("martin", getSort()));
    }


    // 19 Paging + Sort
    // PageRequest.of(page, size, Sort) page는 0-base
    // PageRequest 는 Pageable의 구현체
    @Test
    void qryMethod019() {
        Page<User> userPage = userRepository.findByName("martin", PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id"))));
        // pageNumber 1이 진짜 페이지 1이 아니라 제로베이스이기 때문에 => 2

        System.out.println("page: " + userPage); // Page 를 함 찍어보자                // 2 페이지 중 2 페이지
        System.out.println("totalElements: " + userPage.getTotalElements());        // 2 => 전체 요소의 수
        System.out.println("totalPages: " + userPage.getTotalPages());              // 2 => 전체 페이지의 수
        System.out.println("numberOfElements: " + userPage.getNumberOfElements());  // 1 => 현재 페이지에 포함된 요소의 수
        System.out.println("sort: " + userPage.getSort());                          // id 가 역순 (DESC)
        System.out.println("size: " + userPage.getSize());                          // 1 => 페이징 할 때 나누는 size
        userPage.getContent().forEach(System.out::println);  // 페이지 내의 데이터 List<>
    }


    @Test
    void insertAndUpdateTest() {
        System.out.println("\n-- TEST#insertAndUpdateTest() ---------------------------------------------");

        user = User.builder()   // 새로운 user 를 만들기 위해 user 객체를 만듦
                .name("martin")
                .email("martin2@blueknight.com")
                .build();

        userRepository.save(user);  // INSERT

        user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("U E NA");
        userRepository.save(user2); // UPDATE


        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    void enumTest() {
        System.out.println("\n-- TEST#enumTest() ---------------------------------------------");
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setGender(Gender.MALE);    // id = 1 에게 Gender => MALE 값 부여
        userRepository.save(user);      // UPDATE
        userRepository.findAll().forEach(System.out::println);

        // enum 타입이 실제 어떤 값으로 DB 에 저장되어 있는지 확인
        System.out.println(userRepository.findRowRecord().get("gender"));
        // enumTest 를 실행시켜보면 FEMALE = 0 추론 가능, MALE = 1 => Gender 도메인에서의 순서에 따라 첫 번째에 있는게 0, 그 다음으로 있는게 1


        System.out.println("\n------------------------------------------------------------\n");
    }


    @Test
    void listenerTest() {

        user = new User();
        user.setEmail("베리베리@mail.com");
        user.setName("유인아");

        userRepository.save(user);  // INSERT 동작

        // SELECT 동작
        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);

        user2.setName("marrrrrtin");
        userRepository.save(user2);  // 새롭게 SELECT 동작 후 , UPDATE 동작

        userRepository.deleteById(4L);  // 새롭게 SELECT 동작 후 , DELETE 동작

        System.out.println("\n------------------------------------------------------------\n");
    }


    @Test
    void prePersistTest() {
        System.out.println("\n-- TEST#prePersistTest() ---------------------------------------------");
        User user = new User();
        user.setEmail("martin2@redknight.com");
        user.setName("martin2");

//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);  // INSERT

        System.out.println(userRepository.findByEmail("martin2@redknight.com"));

        System.out.println("\n------------------------------------------------------------\n");
    }


    @Test
    void preUpdateTest() throws InterruptedException{
        Thread.sleep(1000);     // 1초 뒤에 UPDATE 시도
        System.out.println("\n-- TEST#preUpdateTest() ---------------------------------------------");

        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        System.out.println("as-is : " + user);   // 수정전
        user.setName("martin2");

        userRepository.save(user);  // UPDATE
        System.out.println("to-be :" + userRepository.findAll().get(0));


        System.out.println("\n------------------------------------------------------------\n");
    }


    @Test
    void userHistoryTest() {
        System.out.println("\n-- TEST#userHistoryTest() ---------------------------------------------");

        User user = new User();
        user.setEmail("martin-new@greendragon.com");
        user.setName("martin-new");

        userRepository.save(user);  // INSERT

        user.setName("U E NA");
        userRepository.save(user);  // UPDATE.    User update 전에 UserHistory 에 INSERT 발생

        userHistoryRepository.findAll().forEach(System.out::println);


        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    void userRelationTest() {
        System.out.println("\n-- TEST#userRelationTest() ---------------------------------------------");

        User user = new User();
        user.setName("David");
        user.setEmail("david@reddragon.com");
        user.setGender(Gender.MALE);

        userRepository.save(user);      // User 에 INSERT, UserHistory 에 INSERT.

        user.setName("베리냥");
        userRepository.save(user);      // User 에 UPDATE, UserHistory 에 INSERT  => UPDATE 하기 전에 SELECT 하고 시작

        System.out.println("😀".repeat(30));

        user.setEmail("berry@mail.com");
        userRepository.save(user);      // 이 부분에서도 User 에 SELECT + UPDATE, UserHistory 에 INSERT !


//        userHistoryRepository.findAll().forEach(System.out::println);

        System.out.println("🧡".repeat(30));
        // 특정 userId 로 UserHistory 조회
//        Long userId = userRepository.findByEmail("berry@mail.com").getId();
//        List<UserHistory> result = userHistoryRepository.findByUserId(userId);
//        result.forEach(System.out::println);

        List<UserHistory> result = userRepository.findByEmail("berry@mail.com").getUserHistories();
        result.forEach(System.out::println);    // LazyInitializationException 발생 !
        // LazyInitializationException 이 발생하는 주요 원인은 JPA 에서 관리하는 세션이 종료 된 후(정확하게는 persistence context 가 종료 된 후) 관계가 설정된 엔티티를 참조하려고 할 때 발생한다.
        // 이것에서 착안해 DAO 레이어(Spring data 에서 Repository) 상위에서 세션을 시작해 DAO 계층 밖에서도 세션이 종료되지 않도록 트랜젝션을 거는 방법이다.

        System.out.println("👩".repeat(30));
        System.out.println(userHistoryRepository.findAll().get(0).getUser());


        System.out.println("\n------------------------------------------------------------\n");
    }







}