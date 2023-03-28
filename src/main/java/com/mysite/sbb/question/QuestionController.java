package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
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

/*    //template에 question_list.html을 추가함으롤써 ResponseBody 어노테이션은 필요 없어지고,
    //html 파일의 내용으로 return 이 대체 되어지게된다.
    @GetMapping("/old_list")
    public String list_old() {
        return "old_question_list";
    }*/


    private final UserService userService;

    //(Controller -> Service -> Repository)
    //페이징으로 대체됨.
//    @GetMapping("/list")
//    public String list(Model model) {
//
//        /*
//        QuestionRepository의 findAll 메서드를 사용하여 질문 목록 데이터인 quetionList를 생성하고,
//        Model 객체에 "questionList" 라는 이름으로 값을 저장. (Model 객체는 자바 클래스와 템플리 간의 연결고리 역할을 한다.)
//        Model 객체에 값을 담아두면 템플릿에서 그 값을 사용할 수 있다.
//        Model 객체는 따로 생성할 필요 없이 컨트롤러 메서드의 매개변수로 지정하기만 하면
//        SpringBoot가 자동으로 Model 객체를 생성한다.
//        */
//        List<Question> questionList = this.questionService.getList();
//
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }

    //GET 방식으로 요청된 URL 에서 page 값을 가져오기 위해
    //@RequestParam(value="page", defaultValue="0") int page 매개변수가 list 메서드에 추가 (default 는 0)
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue ="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw)
    {                           //검색어에 해당하는 kw 파라미터를 추가하고, 디폴트 값으로 빈 문자열을 설정함.

        Page<Question> paging = this.questionService.getList(page, kw);

        //템플릿에 Page<Question> 객체인 paging 을 전달.
        model.addAttribute("paging", paging);

        //화면에서 입력한 검색어를 화면에 유지하기 위해 kw 값을 저장.
        model.addAttribute("kw", kw);

        //kw 값이 파라미터로 들어오면 해당 값으로 질문 목록이 검색되어 조회됨.
        return "question_list";
    }

    //변하는 id 값을 얻을 때에는 이와 같이 @PathVariable 애너테이션을 사용해야 한다.
    //이 때, @GetMapping 에서 사용한 {id} 와 @PathVariable("id")의 매개변수 일름이 동일해야 한다.
    //URL 매핑시 value 매개변수는 생략할 수 있는데, 위 메서드와 아래의 이 메서드 둘 다
    //  /question 으로 시작되는 URL 을 매핑하고있다, 이런 경우 @RequestMapping("/question") 애너테이션을
    // 클래스에 추가하고, 메서드 단위에서는 /question 을 생략한 그 뒷부분만을 적으면 된다.
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        //Q Controller 에서 Q Service 의 getQuestion 메서드를 호출하여 Question 객체를 템플릿에 전달할 수 있게 함.
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question_detail";
    }

    /*
    아래와 매핑 주소 명이 같고, 메소드 명도 같다
    매핑 URL 이 같은 이유 -> GET 일 경우엔 위로 이동해 폼으로 유인,
    POST 일 경우엔 아래로 이동해 입력된 내용을 저장
    메소드 명이 같은 이유 -> 위와 아래가 받는 Parameter(인자) 수가 다르다. => 메소드 오버로딩

    QuestionController 의 GetMapping 으로 매핑한 메서드도 다음과 같이 변경해야 한다.
    왜냐하면 question_form.html 템플릿은 "질문 등록하기" 버튼을 통해 GET 방식으로 요청되더라도
    th:object 에 의해 QuestionForm 객체가 필요하기 때문이다.
    */
    @PreAuthorize("isAuthenticated()")      //로그인 했을 경우에만 가능
    @GetMapping(value = "/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }


    // !! 주의할 점 !! => 여기서 받는 RequestParam 의 매개변수 명은 템플리셍서 받아온 이름과 동일해야 한다. (구형 형식)
    // => Form 형식으로 변경항 후에는, subject, content 항목을 지닌 폼이 전송되면
    // QuestionForm 의 subject, content 속성이 자동으로 바인딩 된다. (스프링의 바인딩 기능)
    // @Valid => @NotEmpty, @Size 등의 Form 검증 애노테이션이 동작.
    // @BindingResult => @Valid 로 인해 검증이 수행된 결과를 의미하는 객체. (항상 Valid 와 함께 사용되어야 함.)
    @PreAuthorize("isAuthenticated()")      //로그인 했을 경우에만 가능
    @PostMapping(value = "/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal)
    {
        //입력이 잘못되었을 경우(비어있을 경우 등) => 다시 폼을 작성하는 화면으로 이어지도록.
        if(bindingResult.hasErrors())
        {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";      //질문 저장 후 질문 목록으로 이동
    }

    //질문 수정하기(GET)
    @PreAuthorize("isAuthenticated()")       //로그인 되어있는 상태입니까?
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal)
    {
        //게시물 id를 통해 질문을 가져옴
        Question question = this.questionService.getQuestion(id);

        //글쓴이와 로그인한 회원이 같은지 검사
        if(!question.getAuthor().getUsername().equals(principal.getName()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        //현재 질문에 대한 내용이 일단은 form 으로 넘어갔을때 띄워질 수 있도록 넘겨주기 위한 세팅.
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        return "question_form";
    }

    //질문 수정하기(POST)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal,
                                 @PathVariable("id") Integer id)
    {
        //데이터 검증
        if(bindingResult.hasErrors())
        {
            return "question_form";
        }

        //질문 id번호를 통해 질문을 가져옴
        Question question = this.questionService.getQuestion(id);

        //질문의 작성자와 로그인한 사용자가 동일한지 검증
        if(!question.getAuthor().getUsername().equals(principal.getName()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        //검증을 다 통과했다면 form 의 input 을 통해 POST 된 내용들을 기반으로 수정함.
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

        //수정이 완료되면 질문 상세 화면을 다시 호출
        return String.format("redirect:/question/detail/%s", id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id)
    {
        //URL로 전달받은 id 값을 사용하여 질문을 조회함.
        Question question = this.questionService.getQuestion(id);

        //사용자와 질문 작성자가 동일한 경우 Service 의 delete 메서드로 질문을 삭제함.
        if(!question.getAuthor().getUsername().equals(principal.getName()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        this.questionService.delete(question);

        //삭제 후에는 질문 목록 화면으로 돌아갈 수 있도록 루트 페이지로 리다이렉트함.
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }



}
