package com.example.b01.domain;


import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) //컬럼의 길이와 null 허용 여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @OneToMany(mappedBy = "board",
                cascade = {CascadeType.ALL},
                fetch = FetchType.LAZY,
                orphanRemoval = true) //BoardImage 테이블의 board
    @Builder.Default
    @BatchSize(size = 20) //n+1 문제 해결
    private Set<BoardImage> imageSet = new HashSet<>();

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void addImage(String uuid, String fileName) {
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)  //이미지는 이 현재게시글
                .ord(imageSet.size()) //현재 개수 세서 순서부여
                .build();
        imageSet.add(boardImage); //목록에추가
    }
    public void clearImages() {
        imageSet.forEach(boardImage -> boardImage.changeBoard(null)); //값을 null로 만들어서 연결관계 끊어버림
        this.imageSet.clear(); //게시글 이미지를 비움
    }
}
