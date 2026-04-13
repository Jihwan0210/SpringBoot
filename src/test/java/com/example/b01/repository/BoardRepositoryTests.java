package com.example.b01.repository;


import com.example.b01.domain.Board;
import com.example.b01.domain.Reply;
import com.example.b01.dto.BoardListReplyCountDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.print.attribute.standard.PageRanges;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1,100).forEach( i -> {  // 1부터 100까지 반복
            Board board = Board.builder()
                    .title("title....." + i) // 제목: title.....1 ~ title.....100
                    .content("content..." + i)  // 내용: content...1 ~ content...100
                    .writer("user" + (i % 10)) // 작성자: user0 ~ user9 반복
                    .build();

            Board result = boardRepository.save(board); // DB에 저장
            log.info("BNO:"  + result.getBno());  // 저장된 게시글 번호 로그 출력
        });
    }

    @Test
    public void testSelect() {
        Long bno = 100L; //조회할 게시글 번호 = 100번 L은 Long타입
        Optional<Board> result = boardRepository.findById(bno);
        // DB에서 100번 게시글을 찾아서 Optional에 담음
        // Optional = 값이 있을 수도, 없을 수도 있는 껍데기
        Board board = result.orElseThrow();
        // 값이 있으면 꺼내고
        // 없으면 예외(에러) 던짐
        log.info(board);
    }


    @Test
    public void testUpdate() {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        board.change("update..title 100" , "update content 100");
        // 제목, 내용 수정
        // change() 메서드는 Board.java에 따로 정의되어 있어야 함
        boardRepository.save(board);
    }

    @Test
    public void testDelete() {
        Long bno = 1L;
        boardRepository.deleteById(bno);
    }

        @Test
        public void testPaging() {
            //1 page order by bno desc
            Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
            Page<Board> result = boardRepository.findAll(pageable);
            log.info("total count : " + result.getTotalElements());
            log.info("total pages : " + result.getTotalPages());
            log.info("page number : " + result.getNumber());
            log.info("page size : " + result.getSize());
            List<Board> todosList = result.getContent();
            todosList.forEach(board -> log.info(board));

        }

        @Test
        public void testSearch1() {
            //2 page order by bno desc
            Pageable pageable = PageRequest.of(1,10,Sort.by("bno").descending());
            boardRepository.search1(pageable);
        }

        @Test
        public void testSearchAll() {
            String[] types = {"t","c","m"};
            String keyword = "1";
            Pageable pageable = PageRequest.of(0,10,
                    Sort.by("bno").descending());
            Page<Board> result = boardRepository.searchAll(types,keyword,pageable);
            //total pages
            log.info(result.getTotalPages());
            //pag size
            log.info(result.getSize());
            //pageNumber
            log.info(result.getNumber());
            //prev next
            log.info(result.hasPrevious() + ": " + result.hasNext());
            result.getContent().forEach(board -> log.info(board));
        }



    @Test
    public void testSearchReplyCount() {
        String[] types = {"t", "c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types,keyword,pageable);
        //total pages
        log.info(result.getTotalPages());
        //page size
        log.info(result.getSize());
        //pageNumber
        log.info(result.getNumber());
        log.info(result.hasPrevious() + " : "  + result.hasNext());
        result.getContent().forEach(board -> log.info(board));
    }
}
