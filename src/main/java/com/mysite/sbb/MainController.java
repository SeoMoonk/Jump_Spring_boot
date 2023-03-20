package com.mysite.sbb;


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

    /*
    root메서드를 추가하고, " / " URL를 매핑했음.
    redirect:/question/list는 /question/list URL로 페이지를 리다이렉트 하라는 명령어이다.
    - redirect:<URL>  => URL로 리다이렉트(완전히 새로운 URL로 요청)
    - forward:<URL>   => URL로 포워드(기존 요청 값들이 유지된 채로 URL이 전환)
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
}
