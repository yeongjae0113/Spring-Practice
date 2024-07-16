package com.lec.spring.domain.converter;

import com.lec.spring.repository.dto.BookStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


/**
 * AttributeConverter<X, Y> 을 implement 하여 Converter 생성
 *     X: 엔티티의  attribute 타입
 *     Y: DB column 타입
 */
//@Converter  // JPA 에서 사용하는 Converter 임을 지정해주는 어노테이션
    @Converter(autoApply = true)    // 자주 사용하게 되는 Converter 는 autoApply 를 지정해서 사용 가능 !
public class BookStatusConverter implements AttributeConverter<BookStatus, Integer> {
    // ↑ 엔티티는 BookStatus, 테이블의 컬럼은 정수타입으로
    // 2개의 type 파리미터를 받음 => BookStatus 타입을 정수로 변환시켜줌


    // 데이터베이스 컬럼으로 변환시키기 위한 메소드 [BookStatus -> DB 컬럼값으로 변환]
    @Override
    public Integer convertToDatabaseColumn(BookStatus bookStatus) {
        return bookStatus.getCode();
//        return null;    // 개발자가 만들어 주지 않으면?
//        => 이렇게 하면 status 의 값을 가져오지 못하고 null 로 인식하여 UPDATE 쿼리문 실행됌 ! 주 의 !
    }

    // 엔티티로 변환시키기 위한 메소드 [DB 컬럼값 -> BookStatus 로 변환]
    @Override
    public BookStatus convertToEntityAttribute(Integer dbData) {    // dbData 라는 매개변수
        // Book 에서 BookStatus 속성은 not null 을 지정해주지도 않은 상태다
        // 따라서, 여기서 Integer 값이 null 이 넘어올수도 있다.
        // Converter 는 DB에 대해 거의 직접적으로 동작하기 때문에
        // 예외등이 발생하면 추적하기 곤란한다.  가급적 예외가 발생안하도록 해야 한다

        return dbData != null ? new BookStatus(dbData) : null;      // Mapper 타입으로 왔기 때문에 반드시 null 인지 확인 !
    }
}
