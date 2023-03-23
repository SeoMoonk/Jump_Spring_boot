package com.mysite.sbb;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration           //스프링의 환경설정 파일임을 의미
@EnableWebSecurity       //모든 요청 URL이 스피링 시큐리티의 제어를 받도록 만듦.
public class SecurityConfig {
    /*
    스프링 시큐리티를 적용하면 CSRF(Cross Site Request Forgery) 기능이 동작한다.
    => 웹 사이트 취약점 공격을 방지하기 위해 사용하는 기술로, 스프링 시큐리티가
    CSRF 토큰 값을 세션을 통해 발행 하고, 웹 페이지 에서는 폼 전송시에 해당 토큰을
    함께 전송하여 실제 웹 페이지에서 작성된 데이터가 전달되는지를 검증하는 기술이다.
    */
    //스프링 시큐리티의 세부 설정은 SecurityFilterChain 빈을 생성하여 설정할 수 있다.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //모든 인증되지 않은 요청을 허락한다는 의미.
        http.authorizeHttpRequests().requestMatchers(
                new AntPathRequestMatcher("/**")).permitAll()
                .and()
                    .csrf().ignoringRequestMatchers(
                            new AntPathRequestMatcher("/maria-db/**"))
                .and()
                    .headers()
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()  //스프링 시큐리티의 로그인 설정을 담당하는 부분으로, 로그인 페이지의 URL은 다음과 같고, 성공시엔 root 로 이동한다
                    .formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/");
                //스프링 시큐리티가 CSRF 처리시 maria db는 예외로 처리할 수 있도록 수정.

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
