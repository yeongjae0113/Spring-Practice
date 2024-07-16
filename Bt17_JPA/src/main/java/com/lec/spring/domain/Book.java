package com.lec.spring.domain;

import com.lec.spring.domain.converter.BookStatusConverter;
import com.lec.spring.listener.Auditable;
import com.lec.spring.repository.dto.BookStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
//@EntityListeners(value = AuditingEntityListener.class)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;    // null 허용
    private Long authorId;      // null 허용
//    private Long publisherId;

    @OneToOne(mappedBy = "book")    // 해당 Entity 의 테이블에선 연관키를 가지지 않는다.
    @ToString.Exclude       // lombok 의 toString 에서 배제 (양방향에서의 순환참조 때문에)
    private BookReviewInfo bookReviewInfo;


    // Book : Review = 1 : N
    @OneToMany
    @JoinColumn(name = "book_id")   // review 쪽 book_id 컬럼을 이용해 사용하겠다.
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();


    // Book : Publisher = N : 1
    @ManyToOne
    @ToString.Exclude
    private Publisher publisher;

//    // Book : Author = N : M
//    @ManyToMany
//    @ToString.Exclude
//    private List<Author> authors = new ArrayList<>();
//
//    public void addAuthor(Author... authors) {
//        Collections.addAll(this.authors, authors);
//    }

    // Book : Writing = 1 : M
    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<Writing> writings = new ArrayList<>();
    public void addWritings(Writing... writings) {
        Collections.addAll(this.writings, writings);
    }



//    @Column(updatable = false)
//    @CreatedDate
//    private LocalDateTime createdAt;
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }


    //---------------------------------------------------
    // Converter 예제
//    private int status;   // 판매상태 (정수값으로 표현)
//
//    public boolean isDisplayed(){
//        return status == 200;
//    }

    // Entity 가 아닌 BookStatus 타입으로 선언
//    @Convert(converter = BookStatusConverter.class)     // 어떠한 Converter 를 적용할 지 명시 !
    private BookStatus status;
}
