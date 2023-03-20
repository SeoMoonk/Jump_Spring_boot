package com.mysite.sbb.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/sbb")       //요청된 URL과의 매핑을 담당.
    @ResponseBody               //URL요청에 대한 응답으로 문자열을 리턴하라는 의미(생략시 "index.html" 이라는 템플릿 파일으로 넘어가게됨.
    public String index() {
        return "index";
    }
}
