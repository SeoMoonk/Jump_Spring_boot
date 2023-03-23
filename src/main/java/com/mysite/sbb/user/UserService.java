package com.mysite.sbb.user;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password)
    {
        //User 리퐁지터리를 사용하여 User 데이터를 생성하는 create 메서드.

        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);

        //비밀번호는 보안을 위해 반드시 암호화 하여 저장해야 한다. (시큐리티의 BCryptPasswordEncoder 클래스)
        // -> SecurityConfig 파일에 BCryptPasswordEncoder 를 Bean 으로 추가 해두었음. (Bcrypt 해싱 함수)
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);

        return user;
    }

}
