package com.example.b01.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;
    //시작 페이지 번호
    private int start;
    //끝 페이지 번호
    private int end;
    //이전 페이지의 존재 여부
    private boolean prev;
    //다음 페이지의 존재 여부
    private boolean next;
    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, int total, List<E> dtoList) {
        if(total <= 0 ) {
            return;
        }
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        // 현재 페이지가 몇 번째 페이지 묶음에 들어가는지 계산
// 예:
// 1페이지  -> 1~10 묶음
// 7페이지  -> 1~10 묶음
// 13페이지 -> 11~20 묶음
// 그래서 묶음의 마지막 번호를 구함
        this.end = (int)(Math.ceil(this.page / 10.0)) * 10;

// 시작 번호는 끝번호에서 9를 빼면 됨
// 예:
// end가 10이면 start는 1
// end가 20이면 start는 11
        this.start = this.end - 9;

// 진짜 마지막 페이지 번호 구하기
// 전체 글 수를 한 페이지당 개수로 나누고 올림 처리
// 예:
// 글이 95개이고 한 페이지에 10개씩이면 마지막 페이지는 10
        int last = (int)(Math.ceil((total / (double)size)));

// 아까 계산한 end가 너무 크면 마지막 페이지 번호로 고침
// 예:
// end가 20인데 실제 마지막 페이지가 13이면
// 20까지 보여주면 안 되니까 13으로 바꿈
        this.end = end > last ? last : end;

// 이전 버튼이 필요한지 확인
// 시작 페이지가 1보다 크면 앞쪽 페이지 묶음이 있다는 뜻
// 예:
// 1~10은 이전 없음
// 11~20은 이전 있음
        this.prev = this.start > 1;

// 다음 버튼이 필요한지 확인
// 현재 end 페이지까지 보여줄 수 있는 글 수보다
// 전체 글 수가 더 많으면 다음 페이지 묶음이 있음
        this.next = total > this.end * this.size;

    }
}