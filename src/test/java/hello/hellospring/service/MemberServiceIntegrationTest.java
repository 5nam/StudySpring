package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
/*
테스트는 반복할 수 있어야 하는 것이 중요한 특성인데, 테스트 시에 데이터가 남아있으면 반복 불가
그래서 이전에 afterEach, beforeEach 등을 해주면서 처리 해주었던 것.
지금도 이 방식으로 할 수는 있지만 스프링에서 트랜잭션이라는 기능을 제공함.
데이터베이스는 기본적으로 트랜잭션이라는 개념이 있는데 DB 에 데이터를 insert 쿼리 할 때 커밋을 해줘야 디비에 반영이 됨.
그럼 커밋을 하기 전에는 DB 에 반영이 안됨. (모드가 auto commit 모드일때는 무조건 반영이 되는 모드도 있음.)
테스트 끝나고 롤하면 DB insert 다 날리고 롤백하는 것. 그럼 DB 에 반영이 안되고 다 날라감.
이렇게 해서 검즐할 수 있는 방법이 @Transactional 를 테스트 클래스에 달아주면
테스트 실행 시에 트랜잭션을 먼저 실행하고 그리고 DB 의 데이터를 insert 쿼리를 통해 넣어주고, 롤백을 해서 DB 에 데이터가 반영이 안되도록 하는 것!
 */
@Transactional
class MemberServiceIntegrationTest {

    // 이전에는 객체를 직접 생성해서 넣는 방식으로 했는데, 이제는 스프링 컨테이너한테 멤버 서비스, 멤버 리포지토리를 요청해야 함.
    // 테스트 코드는 말 그대로 테스트이기 때문에 생성자를 통합 주입 방식으로 안해도 됨. 제일 편한 방식을 선택하면 됨.
    @Autowired MemberService memberService;
    // MemoryMemberRepository 가 아닌 MemberRepository 로 해주면 됨. - @Transactional 때문에
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {
        // given : 회원 객체를 생성
        Member member = new Member();
        // 오류 발생 : DB 에 데이터가 남아있기 때문에 spring 이 중복된 데이터로 오류 발생!
        member.setName("spring");

        // when : 회원 객체를 회원가입 할 때
        Long saveId = memberService.join(member);

        // then : 멤버 리포지토리에 찾는 멤버가 있는지(회원가입이 잘 되었는지)
        Member findMember = memberService.findOne(saveId).get();
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());


    }

    @Test
    void 중복_회원_예외() {
        // given : 중복된 이름의 회원이 가입했을 때
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);
        // member2 회원가입할 때 exception 이 발생하면 람다식 실행
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // then
        // 중복 익셉션 체크 : e 로 받아온 exception 메시지가 같은지 테스트
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        /* try-catch 로 잡을 수도 있음.
        try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.12");
        }
        */
    }

}