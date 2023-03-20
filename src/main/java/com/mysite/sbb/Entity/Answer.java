package com.mysite.sbb.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Answer {

    @Id     //기본키(Primary Key) 지정.
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //자동으로 1씩 증가하는 개별적인 타입 지정.
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;


    @ManyToOne  //N:1관계, Answer 엔티티의 question 속성과  Question 엔티티가 서로 연결된다(Foreign Key, 외래키 관계)
    private Question question;


}
