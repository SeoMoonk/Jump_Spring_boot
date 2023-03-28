package com.mysite.sbb.answer;


import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

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
        Answer answer = this.answerService.create(question, answerForm.getContent(), siteUser);

        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal)
    {
        //URL 의 답변 아이디를 통해 조회한 답변 데이터의 내용을 AnswerForm 객체에 대입하여
        //answer_form.html 템플릿에서 사용할 수 있도록 하였다.
        Answer answer = this.answerService.getAnswer(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        //답변 수정 시 기존의 내용이 필요하므로 AnswerForm 객체에 조회한 값을 저장해야 한다.
        answerForm.setContent(answer.getContent());

        //answer_form.html 은 답변을 수정하기 위한 템플릿으로 신규로 작성해야 한다.
        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("id") Integer id,
                               Principal principal)
    {
        if(bindingResult.hasErrors())
        {
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());

        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id)
    {
        Answer answer = this.answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.vote(answer, siteUser);

        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }
}
