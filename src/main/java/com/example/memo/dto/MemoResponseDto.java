package com.example.memo.dto;

import com.example.memo.entity.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {

    // 속성 (필드)
    /*
    이 부분은 정의하기 나름이다. 요구사항에 따라 달라진다!
     */
    // 식별자 (id)
    private Long id;
    // 제목 (title)
    private String title;
    // 내용 (contents)
    private String contents;

    // 생성자
    // Memo 객체가 그대로 반환되는 것이 아니라 MemoResponseDto 형태로 바껴서 응답이 되어야 한다.
    public MemoResponseDto(Memo memo) {
        this.id = memo.getId();
        this.title = memo.getTitle();
        this.contents = memo.getContents();
    }
}
