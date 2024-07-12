package com.lec.spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Address {      // 최소한으로 필요한 것 => @Entity, @Id => 프라이머리키를 지정해주는 것임
    @Id
    private Long id;
}
