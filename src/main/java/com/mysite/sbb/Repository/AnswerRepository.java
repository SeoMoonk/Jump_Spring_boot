package com.mysite.sbb.Repository;

import com.mysite.sbb.Entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;


//이렇게 하면 이제 QuestionRepository, AnswerRepository를 이용하여
// question, answer 테이블에 데이터를 저장하거나 조회할 수 있다.
public interface AnswerRepository extends JpaRepository<Answer, Integer> {


}
