package com.example.memo.dto;

import lombok.Getter;

@Getter
public class MemoRequestDto {

    /*
    요청받을 데이터는 title, contents 입니다. 라고 하는 것과 같다.
     */
    // 제목 (title)
    private String title;
    // 내용 (contents)
    private String contents;

}
