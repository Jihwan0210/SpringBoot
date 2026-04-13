package com.example.b01.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name="Reply" , indexes = {
        @Index(name = "idx_reply_board_bno" , columnList = "board_bno")})
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "board") //board는 toString을 적용하면 안되기 떄문에 exclude해줌
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name ="bno")//FK 컬럼명 ,생략가능함
    private Board board;

    private String replyText;

    private String replyer;



    public void changeText(String text) {
        this.replyText = text;

    }
}
