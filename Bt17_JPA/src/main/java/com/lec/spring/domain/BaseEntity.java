package com.lec.spring.domain;

import com.lec.spring.listener.Auditable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass   // 이 클래스의 속성을 상속받는 쪽에 포함시켜준다
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity implements Auditable {  // get 과 같은 것을 상속받기 위해

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
