package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    // 실무에서는 동시성 문제가 있을 수 있어서 공유되는 변수일 때는 ConcurrentHashMap 을 사용해야 하지만 여기서는 간단하게 사용
    private static Map<Long, Member> store = new HashMap<>();
    // 실무에서는 동시성 문제를 고려해서 어텀롱으로 해야함.
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        // store 에 넣기전에 member 의 id 값을 세팅
        // name 은 고객이 회원가입할 때 적는 이름이 들어가는 것 그래서 여기서는 id 만
        member.setId(++sequence);
        // store 에 저장
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // 결과가 없을 경우(null)에는 Optional 로 감싸서 반환하면 클라이언트에서도 뭔가를 할 수 있게됨
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        // store 의 값들중 member 가 매개변수 name 과 같은 이름인 것을 고르고,
        // 가장 먼저 탐색되는 요소를 리턴
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        // 멤버들 반환
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
