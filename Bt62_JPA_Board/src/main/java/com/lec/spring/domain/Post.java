package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder  // builder pattern 사용 가능
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "t6_post")
@DynamicInsert
@DynamicUpdate
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;        // 제목

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;        // 내용

    @ColumnDefault(value = "0")
    @Column(insertable = false)
    private Long viewCnt;          // 조회수

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    // Post : Comment = 1 : N
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();
    public void addComments(Comment... comments) {
        Collections.addAll(commentList, comments);
    }

    // Post : Attachment = 1 : N
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    @Builder.Default
    private List<Attachment> fileList = new ArrayList<>();

    public void addFiles(Attachment... files) {
        Collections.addAll(fileList, files);
    }


}
