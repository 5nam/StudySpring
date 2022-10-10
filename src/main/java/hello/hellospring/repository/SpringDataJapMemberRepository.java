package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// interface 가 interface 를 받을 때는 extends 라고 함
// interface 는 상속을 2개 이상 받을 수 있음
public interface SpringDataJapMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    @Override
    Optional<Member> findByName(String name);
}
