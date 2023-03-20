package com.mysite.sbb.Repository;

import com.mysite.sbb.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;


//리포지터리로 만들기 위해 JpaRepository 인터페이스를 상속했다.
//JpaRepository를 상속할 때는 제네릭 타입으로 리포지터리의 대상이 되는 엔티티의 타입과
//해당 엔티티의 PK의 속성 타입을 지정해야 한다. 이는 JpaRepository를 생성하기 위한 규칙이다.
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    /*
    리포지터리란?
    -> 엔티티에 의해 생성된 데이터베이스 테이블에 접근하는 메서드들(ex. findAll, save 등)을
    사용하기 위;한 인터페이스이다. 데이터 처리를 위해서는 테이블에 어떤 값을 넣거나 값을 조회하는 등의
    CRUD(Create, Read, Update, Delete)가 필요하다. 이 때 이러한 CRUD를 어떻게 처리할지 정의하는
    계층이 바로 리포지터리이다.
     */



}
