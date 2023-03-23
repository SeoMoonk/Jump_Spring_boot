package com.mysite.sbb.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //Primary Key, Auto Increment
    private Long id;

    @Column(unique = true)      //Unique Key(유일 키) -> 유일한 값만이 저장될 수 있다.
    private String username;

    private String password;

    @Column(unique = true)      //Unique Key(유일 키) -> 유일한 값만이 저장될 수 있다.
    private String email;


}
