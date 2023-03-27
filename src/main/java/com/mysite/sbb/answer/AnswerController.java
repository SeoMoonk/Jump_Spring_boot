package com.mysite.sbb.answer;


import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.security.Principal;

//템플릿에서 답변으로 입력한 내용(content)을 얻기 위해 추가.
//템플릿의 답변 내용에 해당하는 textarea 의 name 속성명이 content 이기 때문에
//여기서도 변수명을 content 로 사용해야 한다. 만약 content 대신 다른 이름으로 사용하면 오류가 발생.
@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;


//    // /answer/create/{id} 와 같은 URL 요청시 createAnswer 메서드가 호출되도록 @PostMapping 으로 매핑.
//    @PostMapping("/create/{id}")
//    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam String content)
//    {
//        Question question = this.questionService.getQuestion(id);
//        this.answerService.create(question, content);
//        return String.format("redirect:/question/detail/%s", id);
//    }

    @PreAuthorize("isAuthenticated()")      //로그인 했을 경우에만 가능
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal)
    {                                                                     //Principal => 현재 로그인한 나에 대한 정보
        Question question = this.questionService.getQuestion(id);

        //principal 객체를 통해 사용자 명을 얻은 후에, 사용자 명을 통해 SiteUser 객체를 얻어서
        //답변을 등록하는 AnswerService 의 create 메서드에 전달하여 답변을 저장하도록 함.
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if(bindingResult.hasErrors())
        {
            model.addAttribute("question", question);
            return "question_detail";
        }

        this.answerService.create(question, answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
