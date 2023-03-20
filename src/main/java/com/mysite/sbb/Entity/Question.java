package com.mysite.sbb.Entity;


import com.mysite.sbb.Entity.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter     //엔티티에는 Setter 메서드를 구현하지 않고 사용하는게 좋다.(DB와 바로 연결된 상태인데 Setter를 허용하면 변경이 너무 자유로워짐.)
@Entity
public class Question {


    @Id     //기본 키로 지정(동일한 값 불가능(고유번호), 데이터를 구분할 수 있는 유효한 값)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //데이터를 저장할 때 자동으로 1 씩 증가하여 저장
    //strategy = 고유번호를 생성하는 옵션, GenerationType.IDENTITY = 해당 컬럼만의 독립적인 시퀀스를 생성
    private Integer id;

    @Column(length = 200)       //컬럼으로 지정하고, 컬럼의 길이는 200 (엔티티의 속성은 @Column 애너테이션을 사용하지 않더라도
                                //테이블 컬럼으로 인식한다. 테이블 컬럼으로 인식하고 싶지 않은 경우에만 @Transient 를 사용한다.
    private String subject;

    @Column(columnDefinition = "TEXT")      //컬럼으로 지정하고, 글자 수를 제한할 수 없는 겨웅에 columnDefinition = "TEXT" 사용.
    private String content;

    private LocalDateTime createDate;       //대소문자 형태의 컬럼명은 createDate -> create_date로 변경, 구분지어져서 저장된다.


    //1:n관계, Answer 엔티티의 question 속성과  Question 엔티티가 서로 연결된다(Foreign Key, 외래키 관계)
    //하나의 질문에 여러가지 대답이 가능, 이제 Answer객체에서 답변을 참조하려면 question.getAnswerList()를 호출하면 된다.
    //mappedBy = 참조 엔티티의 속성 명 (즉, Answer 엔티티에서 Question 엔티티를 참조한 속성명 question을 mappedBy에 전달해야 한다.)
    //cascade = CacadeType.REMOVE ==> 질문에는 여러 답변일 달릴 수 있는데, 질문이 삭제되면 여러 답변 또한 함께 삭제되어야 하기에 지정.
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

}
