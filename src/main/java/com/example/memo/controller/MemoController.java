package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto dto) {  // 파라미터로 바로 바인딩 하는 방법 : @RequestBody, 메모를 생성하는 API에서 상태 코드를 따로 반환할 수 있게 만들어주기 위해 ResponseEntity를 사용한다.

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
        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.CREATED);
        /*
        MemoResponseDto만 넣어주는 것이 아니라 실제로 응답할 HTTP Status 코드도 함께 넣어준다.
        HTTP Status를 확인해보면 1xx, 2xx, 3xx, 4xx, 5xx까지 이미 Enum 값으로 다 만들어져있다.
        사용할 응답에 적절한 상태 코드를 찾아서 사용하면 된다. 만들어진 것을 사용하는 것이 가장 바람직하다.
         */
    }

    @GetMapping  // 뒤에 아무것도 적지 않고, 괄호도 적지 않으면 상단의 @RequestMapping이 Mapping된다.
    public ResponseEntity<List<MemoResponseDto>> findAllMemos() {  // 전체 조회이기 때문에 파라미터가 필요없다.

        // init List 초기화
        List<MemoResponseDto> responseList = new ArrayList<>();  // List는 인터페이스이기 때문에 구현체를 사용해서 초기화해줘야 한다. 인터페이스는 new 해서 인스턴스화 할 수 없다.


        /*
        HashMap<Memo> -> List<MemoResponseDto>
        데이터베이스가 Map으로 만들어져 있다. -> HashMap에 Memo가 저장된다.
        전체 조회해서 List<MemoResponseDto> 형태로 만들어준다.
         */
        for (Memo memo : memoList.values()) {  // memoList의 모든 Memo를 꺼낸다. 꺼내진 Memo만큼 memo에 들어간다. 하나씩 들어가면서 반복문이 실행된다.
            MemoResponseDto responseDto = new MemoResponseDto(memo);  // memo가 MemoResponseDto 형태로 바뀐다.

            responseList.add(responseDto);  // responseList에 생성된 MemoResponseDto를 하나씩 추가한다.
        }

        /*
        Map To List
        스트림 사용해서 좀 더 간단하게 구현
        위에 있는 코드와 결국 결과는 똑같다.
        stream을 사용하는 것에 익숙해진다면 그때 사용할 것!
         */
        //responseList = memoList.values().stream().map(MemoResponseDto::new).toList();

        return new ResponseEntity<>(responseList, HttpStatus.OK);  // 모든 응답값을 ResponseEntity로 통일하는 것이 좋다!

    }

    /**
     * 메모를 조회하는 메서드
     * @param id - 식별자
     * @return MemoResponseDto(memo) - 저장된 데이터를 MemoResponseDto 형태로 바꿔서 응답해준다.
     */
    @GetMapping("/{id}")  // 조회이기 때문에 @GetMapping 사용한다.
    public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id) {  // 식별자를 파라미터로 바인딩할 때는 @PathVariable을 사용한다.

        // memoList라는 데이터베이스에서 get()을 통해서 key값을 전달해주면 저장되어 있는 memo를 조회할 수 있다.
        Memo memo = memoList.get(id);

        // ResponseEntity를 사용하면 다음과 같이 동적으로 응답해줄 수 있다.
        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 저장된 데이터를 MemoResponseDto 형태로 바꿔서 응답해준다.
        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    @PutMapping("/{id}")  // 전체 수정을 하기 위해서 @PutMapping을 사용한다. 단건을 수정할 것이기 때문에 경로 변수가 필요하다.
    public ResponseEntity<MemoResponseDto> updateMemoById(
            @PathVariable Long id,  // @PathVariable로 id 바인딩
            @RequestBody MemoRequestDto dto  // 어떤 데이터로 수정할 지도 요청을 받아야 한다. 제목, 내용을 모두 수정할 수 있다.
    ) {
        // 실제로 동작할 로직
        Memo memo = memoList.get(id);

        // ResponseEntity를 사용하면 다음과 같이 동적으로 응답해줄 수 있다.
        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 필수 값을 검증해준다.
        if (dto.getTitle() == null || dto.getContents() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // 필수 값을 클라이언트가 전달해주는 것이기 때문에 BAD_REQUEST를 반환해준다.
        }

        // memo 수정
        memo.update(dto);

        // 바뀐 Memo를 확인하기 위해서 MemoResponseDto 형태로 반환한다. 업데이트된 Memo를 전달한다.
        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    @PatchMapping("/{id}")  // 일부 수정해야 하기 때문에 @PatchMapping이 필요하다.
    public ResponseEntity<MemoResponseDto> updateTitle(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto
    ) {
        Memo memo = memoList.get(id);

        // NullPointerException 방지
        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 필수 값을 검증해준다. 제목은 필수지만 내용은 필수가 아니다.
        if (dto.getTitle() == null || dto.getContents() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // 필수 값을 클라이언트가 전달해주는 것이기 때문에 BAD_REQUEST를 반환해준다.
        }

        // 제목만 수정한다.
        memo.updateTitle(dto);

        // 바뀐 Memo를 확인하기 위해서 MemoResponseDto 형태로 반환한다. 업데이트된 Memo를 전달한다.
        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")  // 식별자로 id가 필요하다.
    public void deleteMemo(@PathVariable Long id) {

        // memoList에 저장되어 있는 값 삭제 -> id와 같은 데이터 삭제
        memoList.remove(id);
    }
}
