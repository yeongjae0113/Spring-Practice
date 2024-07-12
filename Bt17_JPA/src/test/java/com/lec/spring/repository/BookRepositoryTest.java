package com.lec.spring.repository;

import com.lec.spring.domain.Book;
import com.lec.spring.domain.Publisher;
import com.lec.spring.domain.Review;
import com.lec.spring.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void bookTest() {
        System.out.println("\n-- TEST#bookTest() ---------------------------------------------");
        Book book = new Book();
        book.setName("JPA 스터디");
        book.setAuthorId(1L);
//        book.setPublisherId(1L);
        bookRepository.save(book);  // INSERT

        System.out.println(bookRepository.findAll());
        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    @Transactional  //
    void bookRelationTest() {
        System.out.println("\n-- TEST#bookRelationTest() ---------------------------------------------");

        // 테스트용 데이터 입력
        givenBookAndReview();

        // 특정 User 가져오기
        User user = userRepository.findByEmail("martin@redknight.com");

        // 특정 User 가 남긴 review 정보들 가져오기
        System.out.println("Review : " + user.getReviews());

        // 특정 User 가 남긴 Review 중 첫 번째 Review 의 Book 정보 가져오기
        System.out.println("Book : " + user.getReviews().get(0).getBook());

        // 특정 User 가 남긴 Review 중 첫 번째 Review 의 Book 의 Publisher 정보 가져오기
        System.out.println("Publisher : " + user.getReviews().get(0).getBook().getPublisher());

        System.out.println("\n------------------------------------------------------------\n");
    }

    private Book givenBook(Publisher publisher) {
        Book book = new Book();
        book.setName("JPA 완전정복");
        book.setPublisher(publisher);   // FK (외래키) 설정

        return bookRepository.save(book);
    }

    private Publisher givenPublisher() {
        Publisher publisher = new Publisher();
        publisher.setName("K-출판사");

        return publisherRepository.save(publisher);
    }

    private void givenReview(User user, Book book) {
        Review review = new Review();
        review.setTitle("내 인생을 바꾼 책");
        review.setContent("너무너무 재미있고 즐거운 책이었어요");
        review.setScore(5.0f);
        review.setUser(user);   // FK (외래키) 설정
        review.setBook(book);   // FK (외래키) 설정

        reviewRepository.save(review);
    }

    private User givenUser() {
        // 1번 User 값 리턴
        return userRepository.findByEmail("martin@redknight.com");
    }
    private void givenBookAndReview() {
        // 리뷰 저장
        givenReview(givenUser(), givenBook(givenPublisher()));
    }






}