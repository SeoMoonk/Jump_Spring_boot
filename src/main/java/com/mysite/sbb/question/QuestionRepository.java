package com.mysite.sbb.question;

import com.mysite.sbb.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


//리포지터리로 만들기 위해 JpaRepository 인터페이스를 상속했다.
//JpaRepository 를 상속할 때는 제네릭 타입으로 리포지터리의 대상이 되는 엔티티의 타입과
//해당 엔티티의 PK의 속성 타입을 지정해야 한다. 이는 JpaRepository 를 생성하기 위한 규칙이다.
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    /*
    리포지터리란?
    -> 엔티티에 의해 생성된 데이터베이스 테이블에 접근하는 메서드들(ex. findAll, save 등)을
    사용하기 위한 인터페이스이다. 데이터 처리를 위해서는 테이블에 어떤 값을 넣거나 값을 조회하는 등의
    CRUD(Create, Read, Update, Delete)가 필요하다. 이 때 이러한 CRUD 를 어떻게 처리할지 정의하는
    계층이 바로 리포지터리이다.
     */

    /*
    인터페이스에 단순하게 findBySubject 라는 메서드를 선언만 하고 구현은 하지 않았는데
    도대체 어떻게 실행이 되는 거지?
    => JpaRepository 를 상속한 QuestionRepository 객체가 생성되고, 리포지터리 객체의 메서드가 실행될 때
    JPA 가 해당 메서드 명을 분석하여 쿼리를 만들고 실행한다.
    즉, findBy + "엔티티의 속성 명" 과 같은 리포지터리의 메서드를 작성하는 것만으로도
    해당 속성의 값을로 데이터를 조회할 수 있다.
     */

    Question findBySubject(String subject);


    //제목과 내용을 And 조건을 통해 함께 조회.
    /*
    And 처럼 사용할 수 있는 조건 조합들
    - Or => 여러 컬럼을 or 로 검색
    - Between => 컬럼을 between 으로 검색
    - LessThan => 작은 항목 검색
    - GreaterThanEqual => 크거나 같은 항목 검색
    - Like => Like 검색
    - In => 여러 값 중에 하나인 항목 검색
    - OrderBy => 검색 결과를 정렬하여 전달
     */
    Question findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String subject);

    //Pageable 객체를 입력으로 받아 Page<Question> 타입 객체를 리턴하는 findAll 메서드
    Page<Question> findAll(Pageable pageable);


    //id 자동증가 내역 삭제
    @Query(value = "ALTER TABLE answer AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();


}
