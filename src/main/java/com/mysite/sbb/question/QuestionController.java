package com.mysite.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/*
스프링의 의존성 주입(Dependency Injection) 방식 3가지
1. @Autowired 속성 - 속성에 @Autowired 애너테이션을 적용하여 객체를 주입하는 방식
2. 생성자 - 생성자를 작성하여 객체를 주입하는 방식(**권장하는 방식**)
3. Setter - Setter 메서드를 작성하여 객체를 주입하는 방식 (메서드에 @Autowired 애너테이션 적용이 필요하다.)
 */

//questionRepository 속성을 포함하는 생성자를 생성시킴.
// ( final이 붙은 속성을 포함하는 생성자를 자동으로 생성하는 역할)
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    //생성자 방식으로, DI(Dependency Injection) 규칙에 의해 "의존성이 주입" 된다.
    private final QuestionService questionService;

    //template에 question_list.html을 추가함으롤써 ResponseBody 어노테이션은 필요 없어지고,
    //html 파일의 내용으로 return 이 대체 되어지게된다.
    @GetMapping("/old_list")
    public String list_old() {
        return "old_question_list";
    }


    //(Controller -> Service -> Repository)
    @GetMapping("/list")
    public String list(Model model) {

        /*
        QuestionRepository의 findAll 메서드를 사용하여 질문 목록 데이터인 quetionList를 생성하고,
        Model 객체에 "questionList" 라는 이름으로 값을 저장. (Model 객체는 자바 클래스와 템플리 간의 연결고리 역할을 한다.)
        Model 객체에 값을 담아두면 템플릿에서 그 값을 사용할 수 있다.
        Model 객체는 따로 생성할 필요 없이 컨트롤러 메서드의 매개변수로 지정하기만 하면
        SpringBoot가 자동으로 Model 객체를 생성한다.
        */
        List<Question> questionList = this.questionService.getList();

        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    //변하는 id 값을 얻을 때에는 이와 같이 @PathVariable 애너테이션을 사용해야 한다.
    //이 때, @GetMapping 에서 사용한 {id} 와 @PathVariable("id")의 매개변수 일름이 동일해야 한다.
    //URL 매핑시 value 매개변수는 생략할 수 있는데, 위 메서드와 아래의 이 메서드 둘 다
    //  /question 으로 시작되는 URL 을 매핑하고있다, 이런 경우 @RequestMapping("/question") 애너테이션을
    // 클래스에 추가하고, 메서드 단위에서는 /question 을 생략한 그 뒷부분만을 적으면 된다.
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id)
    {
        //Q Controller 에서 Q Service 의 getQuestion 메서드를 호출하여 Question 객체를 템플릿에 전달할 수 있게 함.
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question_detail";
    }
}
