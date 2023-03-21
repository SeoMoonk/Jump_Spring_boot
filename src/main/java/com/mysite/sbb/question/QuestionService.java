package com.mysite.sbb.question;


/*
서비스가 필요한 이유 (Controller -> Service -> Repository)
1. 모듈화
    => 어떤 Controller 가 여러 Repository 를 사용하여 데이터를 조회한 후 가공하여 리턴한다고 하면,
    이러한 기능을 서비스로 만들어 둔다면 Controller 는 해당 Service 를 호출하여서 사용하면 된다.
    하지만 Service 로 만들지 않고 Controller 에서 구현하려고 한다면 해당 기능을 필요로 하는
    모든 Controller 가 동일한 기능을 중복으로 구현해야 하기 때문에, Service 는 모듈화를 위해서 필요하다.

2. 보안
    => Controller 는 Repository 없이 Service 를 통해서만 데이터베이스에 접근하도록 구현하는 것이 보안상
    안전하다, 이렇게 하면 어떤 해커가 해킹을 통해 Controller 를 제어할 수 있게 되더라도 Repository 에 직접
    접근 할 수는 없게 된다.

3. 엔티티 객체와 DTO 객체의 반환
    => 우리가 작성한 Question, Answer 클래스는 엔티티(Entity) 클래스이다. Entity 클래스는 데이터베이스와
    직접 맞닿아 있는 클래스이기 때문에 Controller 나 Thymeleaf 같은 템플릿 엔진에 전달하여 사용하는 것은
    좋지 않다, Controller 나 Thymeleaf 에서 사용하는 데이터 객체는 속성을 변경하여 비즈니스적인 요구를
    처리해야 하는 경우가 많은데, Entity 를 직접 사용하여 속성을 변경한다면 테이블 Column 이 변경되어
    엉망이 될 수도 있기 때문이다.

    이러한 이유로 Question, Answer 같은 Entity 클래스는 컨트롤러에서 사용할 수 없게끔 설계하는 것이 좋다.
    그러기 위해서는 Question, Answer 대신 사용할 DTO(Data Transfer Object) 클래스가 필요하다.
    그리고 Question, Answer 등의 Entity 객체를 DTO 객체로 변환하는 작업도 필요하다.
    그렇다면 Entity 객체를 DTO 객체로 변환하는 일은 어디서 처리하는 것이 좋을까?
    그것이 바로 Service 이다. Service 는 Controller 와 Repository 의 중간의 입장에서 Entity 객체와
    DTO 객체를 서로 변환하여 양방향에 전달하는 역할을 한다.
    -> 단, 이 책에서는 그냥 그대로 사용함. (실제로는 DTO를 권장)
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mysite.sbb.DataNotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service        //스프링의 서비스로 지정
public class QuestionService {

    //생성자 방식으로, DI(Dependency Injection) 규칙에 의해 "의존성이 주입" 된다.
    private final QuestionRepository questionRepository;


    //질문 목록을 조회하여 리턴하는 getList 메서드. (컨트롤러에서 리포지터리를 사용하던 것 옮겨온 것)
    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    //데이터의 실제 제목과 내용을 출력
    public Question getQuestion(Integer id)
    {
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent())
        {
            return question.get();
        }
        else
        {
            throw new DataNotFoundException("question not found");
        }
    }



}
