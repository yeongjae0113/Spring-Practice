package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class BookReviewInfo extends BaseEntity {

    @Id     // id 에 PK 부여
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1:1 연결하기 BookReviewInfo : Book
//    private Long bookId;    // FK 역할 (포렌키 = 외래키)

    // BookReviewInfo:Book 은 1:1
    @OneToOne(optional = false)     // optional = false <= Book 은 절대 null 을 허용하지 않음)
    private Book book;  // 자식쪽에서 부모 Entity 를 직접 참조할 수 있도록 ! / 원래는 Entity 로 나눈 것은 컬렉션으로 사용 불가

    // NULL 을 허용하면 wrapper 객체 사용
    // NULL 을 허용하지 않을거면 primitive 객체 사용 -> DDL 에 NOT NULL 부여됨
    // 이번예제에서 아래 두개값은 기본값 0 을 사용하기 위해 primitive 를 사용합니다.
    //   --> 굳이 null check 안해도 된다.
    private float averageReviewScore;
    private int reviewCount;    // primitive 는 DDL 에 NOT NULL 이 붙는다



}
