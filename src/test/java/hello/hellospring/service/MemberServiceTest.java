package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        // 여기서 생성해서 객체를 전달하므로 같은 객체로 테스트 가능
        // MemberService 생성자를 통해 memberRepository 를 하나로 통일해서 테스트 하는 것!
        // 각각 memberRepository 를 생성하지 않는 것!
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        // given : 회원 객체를 생성
        Member member = new Member();
        member.setName("hello");

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

    @Test
    void findOne() {
    }
}