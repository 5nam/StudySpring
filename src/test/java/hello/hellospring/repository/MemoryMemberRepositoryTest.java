package hello.hellospring.repository;

import hello.hellospring.domain.Member;
// import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    // 테스트가 실행되고 종료될 때마다 한번씩 실행됨
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        // 회원 객체를 생성
        Member member = new Member();
        member.setName("spring");
        // 생성한 회원 객체 저장
        repository.save(member);
        // 회원 객체가 잘 저장되었는지 결과값을 result 에 저장
        Member result = repository.findById(member.getId()).get();

        // 검증 : new 해서 저장한 member 와 DB 에서 꺼내온 result 가 똑같으면 됨
        /*
        println 방식
        System.out.println("result = " + (result == member));
         */

        // Assertion 방법
        /* Assertions.assertEquals(기대하는 값, 결과값) : org.junit.jupiter.api.Assertions;
        Assertions.assertEquals(member, result);
         */
        // member 는 isEqualTo result 와 똑같다
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName() {
        // member1 객체 생성
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        /*
        단축키 팁 : fn+shift+f6 하면 변수 이름 변경 한번에 가능
         */

        // member2 객체 생성
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        // repository 에서 이름이 spring1 인 객체를 get 으로 받아오기
        Member result = repository.findByName("spring1").get();

        // result 가 member1 이랑 같은지 체크
        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        // 생성된 개수와 기대했던 값이 같은지 체크
        assertThat(result.size()).isEqualTo(2);
    }
}
