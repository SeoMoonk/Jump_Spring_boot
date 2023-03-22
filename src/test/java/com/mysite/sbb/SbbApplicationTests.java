package com.mysite.sbb;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	/*
	//@Autowired = 객체를 주입하기 위해 사용하는 스프링의 애너테이션,
	객체를 주입하는 방식에는 @Autowired 외에 Setter 혹은 생성자를 사용하는 방식이 있는데,
	순환참조 문제와 같은 이유로 @Autowired 보다는 생성자를 통한 객체 주입 방식이 권장된다.
	하지만 테스트 코드의 경우에는 생성자를 통한 객체의 주입이 불가능하므로 테스트 코드 작성시에만
	@Autowired 를 사용하고, 실제 코드 작성시에는 생성자를 통한 객체 주입 방식을 사용하는것이 좋다.
	*/

	//@Autowired 애너테이션은 스프링의 DI 기능으로 questionRepository 객체를 스프링이 자동으로 생성해 준다.
	@Autowired
	private QuestionRepository questionRepository;
	//테스트 환경에서는 리포지터리를 이용한 통신만 가능하다.

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;


//	@BeforeEach
//		// 아래 메서드는 각 테스트케이스가 실행되기 전에 실행된다.
//	void beforeEach() {
//		// 모든 데이터 삭제
//		questionRepository.deleteAll();
//
//		// 흔적삭제(다음번 INSERT 때 id가 1번으로 설정되도록)
//		questionRepository.clearAutoIncrement();
//
//		// 질문 1개 생성
//		Question q1 = new Question();
//		q1.setSubject("sbb가 무엇인가요?");
//		q1.setContent("sbb에 대해서 알고 싶습니다.");
//		q1.setCreateDate(LocalDateTime.now());
//		questionRepository.save(q1);  // 첫번째 질문 저장
//
//		// 질문 1개 생성
//		Question q2 = new Question();
//		q2.setSubject("스프링부트 모델 질문입니다.");
//		q2.setContent("id는 자동으로 생성되나요?");
//		q2.setCreateDate(LocalDateTime.now());
//		questionRepository.save(q2);  // 두번째 질문 저장
//
//		// 모든 데이터 삭제
//		answerRepository.deleteAll();
//
//		// 흔적삭제(다음번 INSERT 때 id가 1번으로 설정되도록)
//		answerRepository.clearAutoIncrement();
//	}


	@DisplayName("질문 객체 생성")
	@Test
	void testJpa1() {

		// q라는 Question 엔티티 객체를 생성하고, QuestionRepository 를 이용하여 그 값을 데이터베이스에 저장하는 코드.
		// 질문 1개 생성
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);  // 첫번째 질문 저장

		// 질문 1개 생성
		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);  // 두번째 질문 저장

		assertEquals("sbb가 무엇인가요?", questionRepository.findById(1).get().getSubject());
	}


	 /*
    SQL
    SELECT * FROM question
    */
	 @DisplayName("전부 조회하기")
	@Test
	void testJpa2() {
		List<Question> all = this.questionRepository.findAll();		//findAll 은 데이터를 모두 조회할때 사용하는 메서드이다.
		assertEquals(2, all.size());		//모두 조회했을 때의 데이터 수가 기대치(expected)와 일치한다면 통과한다.

		Question q = all.get(0);		//데이터의 첫번쨰 항목에 대한 내용
		assertEquals("sbb가 무엇인가요?", q.getSubject());		//첫번째 데이터(index=0) 의 getSubject 값이 기대값과 일치한다면 통과.
	}


	/*
    SQL
    SELECT *
    FROM question
    WHERE id = 1
    */
	@DisplayName("ID로 검색하기")
	@Test
	void testJpa3() {

		//!!주의!! => findById의 리턴 타입은 Question 이 아닌 Optional 이다.
		// (Optional 은 null 처리를 유연하게 처리하기 위해 사용하는 클래스로, 위와 같이 isPresent 로 null 이 아닌지를 확인한 후에
		// get 으로 실제 Question 객체 값을 얻어야 한다. (isPresent는 값이 있다면 true, 값이 없다면(null 이라면) false를 반환함.
		Optional<Question> oq = this.questionRepository.findById(1);	//아이디를 사용하여 조회하는 findById
		if(oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}



	/*
    SQL
    SELECT *
    FROM question
    WHERE subject = 'sbb가 무엇인가요?'
    */
	@DisplayName("제목으로 검색하기")
	@Test
	void testJpa4() {
		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());

	}



	/*
    SQL
    SELECT *
    FROM question
    WHERE subject = 'sbb가 무엇인가요?'
    AND content = 'sbb에 대해서 알고 싶습니다.'
    */
	@DisplayName("AND (제목 + 내용) 검색하기")
	@Test
	void testJpa5() {
		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1, q.getId());
	}



	/*
    SQL
    SELECT *
    FROM question
    WHERE subject LIKE 'sbb%'
    */
	//LIKE (sbb%) 검색하기
	@DisplayName("LIKE_(sbb%) 검색하기")
	@Test
	void testJpa6() {
		/*
		sbb% => "sbb"로 시작하는 문자열
		%sbb => "sbb"로 끝나는 문자열
		%sbb% => "sbb"를 포함하는 문자열
		 */

		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}




	/*
    SQL
    UPDATE
        question
    SET
        content = ?,
        create_date = ?,
        subject = ?
    WHERE
        id = ?
    */

	@DisplayName("데이터 수정하기")
	@Test
	void testJpa7() {
		Optional<Question> oq = this.questionRepository.findById(1);		//id가 1인 객체를 repos에서 가져와서 저장.
		assertTrue(oq.isPresent());		//Optional 객체가 값을 가지고 있다면 true, 값이 없다면 false 리턴
		Question q = oq.get();			//가져온 id가 1인 객체를 q에 대입.
		q.setSubject("수정된 제목");		//제목을 수정.
		this.questionRepository.save(q);	//수정된 내용을 저장.
	}



	/*
    SQL
    DELETE
    FROM
        question
    WHERE
        id = ?
    */
	@DisplayName("데이터 삭제하기")
	@Test
	void testJpa8() {
		assertEquals(2, this.questionRepository.count());		//현재 객체에 있는 데이터의 총 갯수가 기대치와 일치하는가?
		Optional<Question> oq = this.questionRepository.findById(1);	//id가 1번인 내용을 가져옴.
		assertTrue(oq.isPresent());		//객체에 내용 잘 들어갔지? (값 존재 하지?)
		Question q = oq.get();			//내용 대입
		this.questionRepository.delete(q);		//내용 삭제
		assertEquals(1, this.questionRepository.count());		//전에서 1개 삭제되었는데, 갯수가 일치 하는가?
	}


	@Test
	@DisplayName("답변 생성 후 저장하기")
	void testJpa9() {
		Optional<Question> oq = this.questionRepository.findById(2);	//id가 2인 내용을 가져온다.
		assertTrue(oq.isPresent());			//id가 2인 내용을 잘 가져왔는가? (null 이 아닌거 맞지?)
		Question q = oq.get();

		Answer a = new Answer();			//답변 객체를 생성하고, 답변의 내용들을 Setter 로 세팅.
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);		//어떤 질문의 답변인지 알기 위해서 Question 객체가 필요하다.
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);		//답변 저장
	}

	@Test
	@DisplayName("답변 조회하기")
	void testJpa10() {
		//해당 Answer 의 아이디는 1이지만, Answer 과 Question 이 서로 연결되있도록 했기 때문에
		//1번 Answer 에 대한 내용이 아이디가 2인 Question 에 대한 답변이다 라는 것까지 얻어서 확인할 수 있음.

		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());		//ID가 1인 Answer => ID가 2인 Question 에 대한 답변 맞지?
	}

	//답변에 연결된 질문 찾기 vs 질문에 달린 답변 찾기
	/*
	오류가 발생한다.
	=> 왜냐하면 Question 리포지터리가 findById를 호출하여 Question 객체를 조회하고 나면 DB세션이 끊어지기 때문이다.
	그 이후에 실행되는 q.getAnswerList() 메서드는 세션이 종료되어 오류가 발생한다.
	답변 데이터 리스트는 q 객체를 조회할때 가져오지 않고 q.getAnswerList() 메서드를 호출하는 시점에 가져오기 때문이다.
	이렇게 필요한 시점에 데이터를 가져오는 방식을 Lazy 방식이라고 한다.
	이와 반대로 q 객체를 조회할때 답변 리스트를 모두 가져오는 방식은 Eager 방식이라고 한다.
	@OneToMany, @ManyToOne 애너테이션의 옵션으로 fetch=FetchType.LAZY 또는 fetch=FetchType.EAGER 처럼 가져오는 방식을
	설정할 수 있는데 이 책에서는 따로 지정하지 않고 항상 디폴트 값을 사용할 것이다.
	사실 이 문제는 테스트 코드에서만 발생한다.
	실제 서버에서 JPA 프로그램들을 실행할 때는 DB 세션이 종료되지 않기 때문에 위와 같은 오류가 발생하지 않는다.
	테스트 코드를 수행할 때 위와 같은 오류를 방지할 수 있는 가장 간단한 방법은 다음처럼 @Transactional 애너테이션을 사용하는 것이다.
	@Transactional 애너테이션을 사용하면 메서드가 종료될 때까지 DB 세션이 유지된다.
	 */

	@Transactional	//이 애너테이션을 사용하면 메서드가 종료될 때까지 DB 세션이 유지되어 실행 가능해짐.
	@Test
	@DisplayName("답변 조회하기2")
	void testJpa11() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}

	@Test
	@DisplayName("테스트데이터 생성")
	void testJpa12() {
		for(int i=1; i<=50; i++)
		{
			String subject = String.format("테스트 데이터 입니다. : [%03d]", i);
			String content = "내용 x";
			this.questionService.create(subject, content);
		}
	}
}
