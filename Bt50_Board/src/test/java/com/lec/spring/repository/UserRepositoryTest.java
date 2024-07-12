package com.lec.spring.repository;

import com.lec.spring.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private SqlSession sqlSession;

    @Test
    void test1() {
        UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
        AuthorityRepository authorityRepository = sqlSession.getMapper(AuthorityRepository.class);

        User user = userRepository.findById(3L);
        System.out.println("findById(): " + user);
        var list = authorityRepository.findByUser(user);    // 첫번째 선언된 user 가 findByUser(user) 괄호 안의 user 로
        System.out.println("권한들: " + list);

        user = User.builder()
                .username("USER4")
                .password(encoder.encode("1234"))
                .name("회원3")
                .email("user3@mail.com")
                .build();

        System.out.println("save() 전 : " + user);
        userRepository.save(user);
        System.out.println("save() 후 : " + user);   // save 가 되면 id 값 부여

        authorityRepository.addAuthority(user.getId(), authorityRepository.findByName("ROLE_MEMBER").getId());
        // ROLE_MEMBER 의 id 값을 찾아와서 멤버권한을 주려고
        System.out.println("권한들: " + authorityRepository.findByUser(user));
    }

    @Autowired
    PasswordEncoder encoder;

    @Test   // bean 으로 등록되어 있어야 시크리티 동작 가능
    void passwordEncoderTest() {
        String rawPassword = "1234";

        for(int i = 0; i < 10; i++) {
            System.out.println(encoder.encode(rawPassword));
        }
    }



}