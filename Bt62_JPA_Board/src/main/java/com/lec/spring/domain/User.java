package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;  // 회원 아이디
    @JsonIgnore
    private String password;  // 회원 비밀번호

    @ToString.Exclude  // toString() 에서 제외
    @JsonIgnore
    private String re_password;  // 비밀번호 확인 입력

    private String name;  // 회원 이름
    private String email;   // 이메일

    @JsonIgnore
    private LocalDateTime regDate;

    // OAuth2 Client
    private String provider;
    private String providerId;


    // User : Authority = N : M
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Authority> authorities = new ArrayList<>();

    // User : Post = 1 : N
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    // User : Comment = 1 : N
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

}