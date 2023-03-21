package com.mysite.sbb.answer;

import com.mysite.sbb.answer.Answer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


//이렇게 하면 이제 QuestionRepository, AnswerRepository를 이용하여
// question, answer 테이블에 데이터를 저장하거나 조회할 수 있다.
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE answer AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();


}
