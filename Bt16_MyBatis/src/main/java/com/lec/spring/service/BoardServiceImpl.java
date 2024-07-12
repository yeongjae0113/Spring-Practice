package com.lec.spring.service;

import com.lec.spring.domain.Post;
import com.lec.spring.repository.PostRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    // BoardServiceImpl 이 BoardService 를 구현한다

    private PostRepository postRepository;  // Postrepository 에서의 postRepositroy 멤버변수

    @Autowired
    public BoardServiceImpl(SqlSession sqlSession) {    // MyBatis 가 생성한 SqlSession 빈(bean) 객체 주입
        postRepository = sqlSession.getMapper(PostRepository.class);
        System.out.println("BoardService() 생성");
    }

    @Override
    public int write(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post detail(Long id) {   // 조회수 증가(Update)
        postRepository.incViewCnt(id);
        Post post = postRepository.findById(id);
        return post;
    }

    @Override
    public List<Post> list() {
        return postRepository.findAll();
    }

    @Override
    public Post selectById(Long id) {
        Post post = postRepository.findById(id);
        return post;
    }

    @Override
    public int update(Post post) {
        return postRepository.update(post);
    }

    @Override
    public int deleteById(Long id) {
        int result = 0;

        Post post = postRepository.findById(id);    // 존재하는 데이터인지 확인
        if(post != null) {     // 존재한다면 삭제 진행
            result = postRepository.delete(post);
        }

        return result;
    }


}   // end Service
