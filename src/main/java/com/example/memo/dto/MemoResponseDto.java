package com.example.memo.dto;

import lombok.Getter;

@Getter
public class MemoResponseDto {

    /*
    이 부분은 정의하기 나름이다. 요구사항에 따라 달라진다!
     */
    // 식별자 (id)
    private Long id;
    // 제목 (title)
    private String title;
    // 내용 (contents)
    private String contents;

}
