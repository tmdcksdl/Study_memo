package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController  // 데이터를 항상 JSON 형태로 통신을 하기로 했기 때문에 @RestController를 사용하면 된다.
@RequestMapping ("/memos") // prefix URL을 만들 때 사용한다.
public class MemoController {
    /*
    실제로 데이터베이스에 데이터를 저장하는 것이 아니라 자료구조를 사용해서 임시로 데이터를 저장한다.
    초기화를 해주면 memoList라는 빈 map 자로구조가 생성된다.
     */
    private final Map<Long, Memo> memoList = new HashMap<>();

    /*
    실제 호출해서 사용할 Controller를 만들어줘야 한다.
    반환 타입은 설정하기 나름이다. 생성할 때 데이터를 주지 않아도 되고 줘도 되는데, 그때 ResponseDto 형태로 전달하면 된다.
    그리고 "우리는 MemoResponseDto를 전달할 것이다."라는 의미이다.
     */
    @PostMapping  // 생성이기 떄문에 사용한다.
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto dto) {  // 파라미터로 바로 바인딩 하는 방법 : @RequestBody

        // 식별자가 1씩 증가하도록 만들어야 한다.
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;  // MemoList.keySet() 중에서 최대값을 반환해주는 것이다. 최대값에서 1씩 증가하도록 설정한다.

        /*
        요청 받은 데이터로 Memo 객체를 생성한다.
        MemoRequestDto 형태로 받았기 때문에 Memo 객체로 바꿔주어야 한다.
         */
        Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

        // Inmemory(Map) DB에 Memo를 저장한다.
        memoList.put(memoId, memo);

        /*
        저장된 데이터를 MemoResponseDto 형태로 바꿔서 응답해준다.
        응답하는 데이터 : MemoResponseDto 형태
        요청하는 데이터 : MemoRequestDto 형태
         */
        return new MemoResponseDto(memo);
    }
}
