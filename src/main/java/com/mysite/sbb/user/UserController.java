package com.mysite.sbb.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //GET으로 요청되면 회원 가입을 위한 템플릿을 렌더링함.
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm)
    {
        return "signup_form";
    }

    //POST로 요쳥되면 회원가입이 진행될 수 있도록 함.
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult)
    {
        //폼의 내용이 바인딩 되었을때 오류가 있는가? -> 있다면 폼을 다시 리턴해줌(다시 작성하세요.)
        if(bindingResult.hasErrors())
        {
            return "signup_form";
        }

        //비밀번호와 비밀번호 확인의 내용이 일치하지 않는다면, 바인딩 결과로써 오류 메세지가 출력되고, 폼을 다시 리턴하도록
        //bindingResult.rejectValue(필드명, 오류코드, 에러메시지)
        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2()))
        {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        //try catch를 통해 중복 회원가입이 발생할 시의 오류 메시지를 만들어줌.
        try {
            //위에서 제약조건에 걸리지 않았다면, Controller 가 Service 를 통해서 User 를 생성할 수 있도록 함.
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
        } catch(DataIntegrityViolationException e) {

            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";

        } catch(Exception e){
            //중복 회원가입이 아닌 다른 오류들인 경우
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";

        }
        return "redirect:/";
    }


}
