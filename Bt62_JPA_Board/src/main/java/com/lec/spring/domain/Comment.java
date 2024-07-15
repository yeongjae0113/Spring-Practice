package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // PK

    @ToString.Exclude
    @ManyToOne
    private User user;      // 댓글 작성자 (FK)

    @JsonIgnore
    private Long post_id;       // 어느글의 댓글 (FK)

    private String content;     // 댓글 내용


    // Comment : User = N : 1
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User users;

    // Comment : Post = N : 1
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private Post posts;


    // java.time.* 객체 변환을 위한 annotation
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty("regdate")    // 변환하고자 하는 형태 지정 가능
    private LocalDateTime regDate;
}
