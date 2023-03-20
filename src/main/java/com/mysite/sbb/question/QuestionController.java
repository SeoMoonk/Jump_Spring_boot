package com.mysite.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/*
스프링의 의존성 주입(Dependency Injection) 방식 3가지
1. @Autowired 속성 - 속성에 @Autowired 애너테이션을 적용하여 객체를 주입하는 방식
2. 생성자 - 생성자를 작성하여 객체를 주입하는 방식(**권장하는 방식**)
3. Setter - Setter 메서드를 작성하여 객체를 주입하는 방식 (메서드에 @Autowired 애너테이션 적용이 필요하다.)
 */

//questionRepository 속성을 포함하는 생성자를 생성시킴.
// ( final이 붙은 속성을 포함하는 생성자를 자동으로 생성하는 역할)
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionRepository questionRepository;

    //template에 question_list.html을 추가함으롤써 ResponseBody 어노테이션은 필요 없어지고,
    //html 파일의 내용으로 return 이 대체 되어지게된다.
    @GetMapping("/question/old_list")
    public String list_old() {
        return "old_question_list";
    }

    @GetMapping("/question/list")
    public String list(Model model) {

        /*
        QuestionRepository의 findAll 메서드를 사용하여 질문 목록 데이터인 quetionList를 생성하고,
        Model 객체에 "questionList" 라는 이름으로 값을 저장. (Model 객체는 자바 클래스와 템플리 간의 연결고리 역할을 한다.)
        Model 객체에 값을 담아두면 템플릿에서 그 값을 사용할 수 있다.
        Model 객체는 따로 생성할 필요 없이 컨트롤러 메서드의 매개변수로 지정하기만 하면
        SpringBoot가 자동으로 Model 객체를 생성한다.
        */
        List<Question> questionList = this.questionRepository.findAll();

        model.addAttribute("questionList", questionList);
        return "question_list";
    }
}
