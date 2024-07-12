package com.lec.spring.repository;

import com.lec.spring.domain.Post;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest  // 스프링 context 를 로딩하여 테스트에 사용
class PostRepositoryTest {

    // MyBatis 가 생성한 구현체들이 담겨 있는 SqlSession 객체
    // 기본적으로 스프링에 빈 생성되어있어서 주입 받을수 있다

    @Autowired
    private SqlSession sqlSession;

    @Test
    void test0(){          // = 오른쪽 부분의 property 값을 postRepository 변수에 저장
        PostRepository postRepository = sqlSession.getMapper(PostRepository.class);

        System.out.println("[최초]");
        postRepository.findAll().forEach(System.out::println);

        Post post = Post.builder()  // Post 타입을 가진 post 변수명
                .user("김다현")
                .subject("늦게 일어났어요")
                .content("ㅋㅋ")
                .build();
        System.out.println("[생성전] " + post);
        int result = postRepository.save(post);
        System.out.println("[생성후] " + post);
        System.out.println("save() 결과:" + result);

        System.out.println("[신규 생성후]");
        postRepository.findAll().forEach(System.out::println);

        Long id = post.getId();
        for(int i = 0; i < 5; i++){
            postRepository.incViewCnt(id);
        }
        post = postRepository.findById(id);
        System.out.println("[조회수 증가후]" + post);

        post.setContent("감기 조심하세요");
        post.setSubject("에러 메세지 보세요");
        postRepository.update(post);
        post = postRepository.findById(id);
        System.out.println("[수정후]" + post);

        postRepository.delete(post);
        System.out.println("[삭제후]");
        postRepository.findAll().forEach(System.out::println);

    }

}