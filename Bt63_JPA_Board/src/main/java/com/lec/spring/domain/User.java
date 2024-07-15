package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)    // ToString 어노테이션과 세트
@Entity(name = "t8_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;  // 회원 아이디

    @Column(nullable = false)
    @JsonIgnore
    private String password;  // 회원 비밀번호

    @Transient      // Entity 에 반영안함.   form 데이터를 받기 위한 용도 (검증하려고)
    @ToString.Exclude  // toString() 에서 제외
    @JsonIgnore        // Json 변환에서 빼는 어노테이션
    private String re_password;  // 비밀번호 확인 입력

    @Column(nullable = false)
    private String name;  // 회원 이름
    private String email;   // 이메일

    // fetch 의 기본값
    // @OneToMany, @ManyToMany => FetchType.Lazy    ===> 필요한 데이터만 로드할 때
    // @OneToOne, @ManyToOne => FetchType.EAGER     ===> 모든 데이터를 로드할 때
    @ManyToMany(fetch = FetchType.EAGER)     // N : M
    @ToString.Exclude
    @Builder.Default    // 위 어노테이션을 지정해주지 않으면 NULL 값이 들어감
    @JsonIgnore
    private List<Authority> authorities = new ArrayList<>();

    public void addAuthority(Authority... authorities) {    // Authority 객체를 인수로 받을 수 있게끔 함. ( EX] 호출 시 Authority(auth1, auth2, auth3 ...) )
        Collections.addAll(this.authorities, authorities);  // Authority 객체들을 (가변 인수로 전달된 authorities 객체들) Collections 에 모두 추가
        // Collections => 컬렉션 프레임워크 중 가장 큰 인터페이스 중 하나. ===> Collections 에 저장하여 그때의 상황에 필요한 list, set, map 등의 객체 사용
    }

    // OAuth2 Client
    private String provider;
    private String providerId;

}