package com.example.b01.service;

import com.example.b01.dto.BoardDTO;
import com.example.b01.dto.PageRequestDTO;
import com.example.b01.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno); // 화면에 보여줘야하기때문에 BoardDTO를 리턴
    void modify(BoardDTO boardDTO); //리다리엑트 하기떄문에 값 화면에 전달 X 그래서 void
    void remove(Long bno);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
}
