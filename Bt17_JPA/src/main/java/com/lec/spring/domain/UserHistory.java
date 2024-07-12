package com.lec.spring.domain;

import com.lec.spring.listener.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// User 의 히스토리 정보 저장
// '수정하기 전의 데이터' 가 아니라
// '수정 될 내용'을 History 에 담은 예제
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
//@EntityListeners(value = AuditingEntityListener.class)
public class UserHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "user_id", insertable = false, updatable = false)
//    private Long userId;    // User 의 id
    private String name;    // User 의 name
    private String email;   // User 의 email

    @ManyToOne
    private User user;      // user_id 로 실행될 것임. / Entity 는 Entity 를 필드로 가질 수 없기 때문에 @관계 어노테이션 필요

//    @CreatedDate
//    private LocalDateTime createdAt;
//    @LastModifiedDate
//    private LocalDateTime updatedAt;
}
