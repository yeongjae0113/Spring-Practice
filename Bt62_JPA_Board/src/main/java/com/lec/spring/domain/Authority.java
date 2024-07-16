package com.lec.spring.domain;

import  jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "t6_authority")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // PK

    @Column(nullable = false)
    private String name;   // 권한명  ex) "ROLE_MEMBER", "ROLE_ADMIN"

    // Authority : User = N : M
//    @ManyToMany
//    @JoinColumn(name = "authority_id")
//    @ToString.Exclude
//    private List<User> users = new ArrayList<>();
}
