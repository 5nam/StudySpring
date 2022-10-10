package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    // 회원을 저장하면 저장한 회원 반환
    Member save(Member member);
    // 아이디로 회원을 찾는 것
    Optional<Member> findById(Long id);
    // 이름으로 회원을 찾는 것
    Optional<Member> findByName(String name);
    // 모든 회원 객체를 찾는 것
    List<Member> findAll();
}
