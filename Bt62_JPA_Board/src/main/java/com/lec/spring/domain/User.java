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
@EqualsAndHashCode(callSuper = true)
@Entity(name = "t6_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;  // 회원 아이디

    @Column(nullable = false)
    @JsonIgnore
    private String password;  // 회원 비밀번호

    @Transient
    @ToString.Exclude  // toString() 에서 제외
    @JsonIgnore
    private String re_password;  // 비밀번호 확인 입력

    @Column(nullable = false)
    private String name;  // 회원 이름
    private String email;   // 이메일

    // OAuth2 Client
    private String provider;
    private String providerId;


    // User : Authority = N : M
    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    @JsonIgnore
    private List<Authority> authorities = new ArrayList<>();

    public void addAuthority(Authority... authorities) {
        Collections.addAll(this.authorities, authorities);
    }


    // User : Comment = 1 : N
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

}