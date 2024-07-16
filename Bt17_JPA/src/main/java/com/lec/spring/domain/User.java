package com.lec.spring.domain;

import com.lec.spring.listener.Auditable;
import com.lec.spring.listener.UserEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor    // NonNull 을 가진 생성자를 만듦
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity  // Entity 객체 지정     => @Entity 와 @Id 는 필수   // 이 객체가 JPA 에서 관리하는 Entity 객체임을 알림.
@Table(
        name = "T_USER"     // DB 테이블 명
        , indexes = {@Index(columnList = "name")}   // 컬럼에 대한 index 생성
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "name"})})  // unique 제약사항 => email 을 UNIQUE 로
@EntityListeners(value = {UserEntityListener.class})
public class User extends BaseEntity {

    @Id     // PK 지정 -> 필수요소
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 마치 MySQL 의 AUTO_INCREMENT 와 같은 동작 수행
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @Column(unique = true)
    private String email;
    // Table 에 not null 을 걸면 복합키일 때에도 not null 이 되고
    // Column 에 not null 을 걸면 그 테이블에서만 not null 이 됌

//    @Column(
//            name = "crtdat",  // 1. created_at 을 crtdat 으로 변경.
//            nullable = false)    // 2. nullable = false 는 crtdat 을 not null 로 설정


//    @Column(insertable = false) // INSERT 동작 시 해당 컬럼은 생략
//    @CreatedDate        // AuditingEntityListener 가 Listener 로 적용 시 사용
//    private LocalDateTime createdAt;    // created_at
//
////    @Column(updatable = false)  // UPDATE 동작 시 해당 컬럼은 생략
//    @LastModifiedDate   // AuditingEntityListener 가 Listener 로 적용 시 사용
//    private LocalDateTime updatedAt;    // updated_at

    // User:Address => 1:N
    //    @OneToMany(fetch = FetchType.EAGER)
    //    private List<Address> address;      // entity 로 선언된 것은 List< ? > 이런 식으로 선언 불가 => 선언해주기 위해서는 릴레이션으로 잡아주어야함 !

    // Eager Fetching => 1:N 관계에서 엔티티를 로드할 때 관련 엔티티들도 함께 로드하는 것
    // Lazy Fetching  => 1:N 관계에서 엔티티를 로드할 때 관련 엔티티들을 함께 가져오지 않고 관련 엔티티가 필요로 할 때에만 가져오는 것

    @Transient  // jakarta.persistence => DB 에 반영 안하는 필드 속성. 영속성을 부여 안한다 !
    private String testData;    // test_data

    @Enumerated(value = EnumType.STRING)    // 이렇게 String 으로 바꿔주면 마지막 확인 단계에서 0 과 1 이 아닌 MALE, FEMALE 로 출력
    private Gender gender;      // enum 타입


    // User : UserHistory = 1 : N 관계
    @OneToMany(fetch = FetchType.EAGER)     // 자식의 Entity 와 부모의 Entity 도 같이 읽어오도록 쿼리 실행해주줌
    @JoinColumn(name = "user_id"            // Entity 가 어떤 컬럼으로 join 하게 될지 지정해준다.
            // name = "user_id" : join 할 컬럼명 지정가능!
            //        UserHistory 의 user_id 란 컬럼으로 join

            , insertable = false, updatable = false
            // User 에서 userHistories 값을 추가, 수정하지 못하도록 하기위해 !
    )
    @ToString.Exclude   // toString 제외
    private List<UserHistory> userHistories = new ArrayList<>();    // ArrayList<>() 를 써서 NullPointException 방지

    // User : Review = 1 : N
    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();   // ArrayList<>() 를 써서 NullPointException 방지 => Review 가 없을 경우 List 로 초기화


//    @PrePersist     // INSERT 직전 호출
//    public void prePersist() {
//        System.out.println(">> prePersist");
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//    @PreUpdate      // UPDATE 직전 호출
//    public void preUpdate() {
//        System.out.println(">>> preUpdate");
//        this.updatedAt = LocalDateTime.now();
//    }
//    @PreRemove      // DELETE 직전 호출
//    public void preRemove() {
//        System.out.println(">>> preRemove");
//    }
//    @PostPersist    // INSERT 직후 호출
//    public void postPersist() {
//        System.out.println(">>> postPersist");
//    }
//    @PostUpdate     // UPDATE 직후 호출
//    public void postUpdate() {
//        System.out.println(">>> postUpdate");
//    }
//    @PostRemove     // DELETE 직후 호출
//    public void postRemove() {
//        System.out.println(">>> postRemove");
//    }
//    @PostLoad       // SELECT 직후 호출
//    public void postLoad() {
//        System.out.println(">>> postLoad");
//    }


    // Embedded 예제
    // Embed 없이 주소 다루기
//    private String city;
//    private String district;
//    private String detail;
//    private String zipCode;

    @Embedded       // Embeddable 클래스 임을 명시
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "home_city")),
            @AttributeOverride(name = "district", column = @Column(name = "home_distirct")),
            @AttributeOverride(name = "detail", column = @Column(name = "home_address_detail")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "home_zip_code")),
    })
    private Address homeAddress;    // 기존의 address

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "company_city")),
            @AttributeOverride(name = "district", column = @Column(name = "company_distirct")),
            @AttributeOverride(name = "detail", column = @Column(name = "company_address_detail")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "company_zip_code")),
    })
    private Address companyAddress; // 바꾼 address
























}
