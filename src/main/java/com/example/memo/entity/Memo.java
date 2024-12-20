package com.example.memo.entity;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class Memo {

    // 속성 (식별자)
    /*
    식별자 (id)
    Long 타입을 쓰는 이유 : int보다 훨씬 더 크고, 래퍼 클래스이기 때문에 null 값을 포함할 수 있다.
    보통 식별자는 안전하게 다루기 위해서 Long 타입을 많이 사용한다.
    id는 서버에서 관리하면 된다.
     */
    private Long id;

    /*
    클라이언트로부터 전달받아야 하는 데이터 :title, contents
     */
    // 제목 (title)
    private String title;
    // 내용 (contents)
    private String contents;

    // update 메서드
    public void update(MemoRequestDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
    }

    // updateTitle 메서드
    public void updateTitle(MemoRequestDto dto) {
        this.title = dto.getTitle();  // 제목만 수정하면 된다.
    }

}
