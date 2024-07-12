package com.lec.spring.repository;

import com.lec.spring.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // 1번 째 파라미터는 Entity 타입 (Book), 2번째 파라미터는 id 의 타입 (Long)

}
