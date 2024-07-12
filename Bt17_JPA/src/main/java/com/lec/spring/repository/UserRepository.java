package com.lec.spring.repository;

// Repository 생성
// JpaRepository<Entity 타입, Id 타입> 상속 ← 바로 이게 Spring Data JPA 가 지원해주는 영역!
//   상속받은 것만으로도 많은 JPA 메소드를 편리하게 사용 가능하게 된다.
//   상속받은 것만으로도 이미 Spring Context 에 생성된다. → 주입 가능!

import com.lec.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    // 1.
//    User findByName(String name);       // 단일 객체를 리턴하는 경우만 가능
//    List<User> findByName(String name);   // 여러 객체가 SELECT 되는 경우
    Optional<User> findByName(String name);


    // 2.
    User findByEmail(String email);

    User getByEmail(String email);

    User readByEmail(String email);

    User queryByEmail(String email);

    User searchByEmail(String email);

    User streamByEmail(String email);

    User findUsersByEmail(String email);


    // 3. find 아무말... 가능
    User findYouinahByEmail(String email);


    // 4. 잘못된 네이밍
//    User findByByName(String name);     // <- 'ByName' 이란 property 를 찾는다! -> 가동 자체가 안된다.


    // 5. First, Top
    List<User> findFirst1ByName(String name);

    List<User> findFirst2ByName(String name);

    List<User> findTop1ByName(String name);

    List<User> findTop2ByName(String name);


    // 6. Last 는 없는 키워드! -> find 와 By 사이의 키워드 아닌 것 들은 무시된다.
    List<User> findLast1ByName(String name);    // findByName() 과 동일 => findLastByName 이란건 따로 없음


    // 7. And, Or
    List<User> findByEmailAndName(String email, String name);

    List<User> findByEmailOrName(String email, String name);


    // 8. After, Before
    List<User> findByCreatedAtAfter(LocalDateTime dateTime);

    List<User> findByIdAfter(Long id);      // id 값이 주어진 매개변수보다 큰 것

    List<User> findByNameBefore(String name);


    // 9. GreaterThan, GreaterThanEqual, LessThan, LessThanEqual    => 숫자를 비교할 땐 위 비교연산자? 가 더 좋음
    //      >, >=, <, <=
    List<User> findByCreatedAtGreaterThan(LocalDateTime datetime);  // 매개변수로 주어진 DateTime 보다 큰 것을 select

    List<User> findByNameGreaterThanEqual(String name);


    // 10. Between
    List<User> findByCreatedAtBetween(LocalDateTime datetime1, LocalDateTime datetime2);

    // 매개변수 2개, datetime1 보다 같거나 크고 datetime2 보다 작거나 같은 범위를 SELECT
    List<User> findByIdBetween(Long id1, Long id2);
    // id1 보다 같거나 크고 id2 보다 같거나 작은 범위를 SELECT

    // BETWEEN a AND b =>   id >= a AND id <= b
    List<User> findByIdGreaterThanEqualAndIdLessThanEqual(Long id1, Long id2);


    // 11. Null, Empty
    List<User> findByIdIsNotNull();     // id 값이 Null 이 아닌 것

    // isEmpty 와 IsNotEmpty 는 collection properties 에만 사용할 수 있다.
    //    List<User> findByIdIsNotEmpty();    // 스프링 가동 시 에러
    // Query method 의 Empty 는 문자열의 Empty 와 다르다 !
    // Query method 의 Empty 는 collection 에서의 Empty, not empty 를 체크한다.   ( list 의 원소가 있는지. 없는지.)

//    List<User> findByAddressIsNotEmpty();


    // 12. In, NotIn
    List<User> findByNameIn(List<String> names);


    // 13. StartingWith, EndingWith, Contains
    // 문자열에 대한 검색 쿼리, LIKE 사용
    List<User> findByNameStartingWith(String name);

    List<User> findByNameEndingWith(String name);

    List<User> findByEmailContains(String email);


    // 14. Like
    List<User> findByEmailLike(String email);


    // 15. Is, Equals
    // 특별한 역할 안함.   생략 가능.  가독성 차원에서 남겨진 키워드들.
    Set<User> findByNameIs(String name);    // findByName 과 동일

    // 아래 4개는 동일하게 동작하는 메소드다.   => find 와 By 사이에는 보통 Entity 명을 많이 사용
    // Set<User> findByName(String name);
    // Set<User> findUserByName(String name);
    // Set<User> findUserByNameIs(String name);
    // Set<User> findUserByNameEquals(String name);


    // 16. OrderBy
    List<User> findTopByNameOrderByIdDesc(String name);
    List<User> findFirstByNameOrderByIdDesc(String name);


    // 17. 정렬기준 추가
    List<User> findFirstByNameOrderByIdDescEmailDesc(String name);


    // 18. Sort 객체를 매개변수로 사용한 정렬
    List<User> findFirstByName(String name, Sort sort);     // => sort 객체 지정해주는 것


    // 19. Paging
    Page<User> findByName(String name, Pageable pageable);     // Pageable 의 리턴타입은 Page 객체


    // 20. Enum 처리
    @Query(value = "SELECT * FROM t_user LIMIT 1", nativeQuery = true)
    Map<String, Object> findRowRecord();
    // key 값 = String, value = Object => 하나의 행이 Map 으로 담겨옴. (여러개가 있기 때문에 List 로 -> 썻다가 지웠음)

}
