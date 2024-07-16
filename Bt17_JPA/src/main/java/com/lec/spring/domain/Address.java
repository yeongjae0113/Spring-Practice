package com.lec.spring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable     // Entity 에서 Embed 할 수 있는 클래스 임을 선언 -> Entity 안에 선언 가능해진다 !
public class Address {      // 최소한으로 필요한 것 => @Entity, @Id => 프라이머리키를 지정해주는 것임
//    @Id
//    private Long id;

    // Embed 예제
    private String city;
    private String district;
    @Column(name = "address_detail")
    private String detail;
    private String zipCode;
}
