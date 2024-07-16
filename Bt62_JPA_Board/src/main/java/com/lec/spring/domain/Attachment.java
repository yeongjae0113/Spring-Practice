package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "t6_attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourcename;   // 원본 파일명
    private String filename;  // 저장된 파일명 (rename 된 파일명)

    private boolean isImage;   // 이미지 여부
}
