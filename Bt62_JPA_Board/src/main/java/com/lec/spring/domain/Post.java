package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder  // builder pattern 사용 가능
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;        // 제목
    private String content;        // 내용
    private LocalDateTime regDate; // 작성일
    private Long viewCnt;          // 조회수

    @Transient
    private User user;             // 글 작성자 (FK)

    // Post : User = N : 1
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User users;

    // Post : Comment = 1 : N
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    // Post : Attachment = 1 : N
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private List<Attachment> attachments = new ArrayList<>();


    // 첨부파일
    @ToString.Exclude
    @Builder.Default   // builder 제공안함
    @Transient
    private List<Attachment> fileList = new ArrayList<>();

}
