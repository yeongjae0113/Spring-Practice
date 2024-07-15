package com.lec.spring.repository;

import com.lec.spring.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userTest() {
        System.out.println("\n-- Test# userTest() ------------------------------");

        User user1 = User.builder()
                .username("jack")
                .password("as00252")
                .re_password("as00252")
                .name("jackson")
                .email("jack123@naver.com")
                .regDate(LocalDateTime.now())
                .provider("local")
                .providerId("1")
                .build();
        userRepository.save(user1);

        Optional<User> saveUser = userRepository.findById(1L);
        saveUser.ifPresentOrElse(
                user -> System.out.println("Save User: " + user),
                () -> System.out.println("User not found!")
        );

    }


}