package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "t8_authority")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // PK

    @Column(length = 40, nullable = false, unique = true)   // null 허용 X, unique 로 사용 !
    private String name;   // 권한명  ex) "ROLE_MEMBER", "ROLE_ADMIN"
}
