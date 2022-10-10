package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

    // jpa 는 entityManager 라는 걸로 모든 게 동작이 됨 : 이건 아까 jpa 사용을 위해 gradle, 설정을 바꿀 때 다 스트링 부트가 알아서 만들어줌
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        // 이렇게 하면 jpa 가 insert 쿼리 다 만들어서 set id 까지 다 해줌
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    // 아래 findByName, findAll 같은 PK 기준으로 찾는 것이 아닌 것들은 jpgl 이라는 객체지향 쿼리를 사용해줘야 함!
    @Override
    public Optional<Member> findByName(String name) {
        // 여기서는 조금 특별한 jpql 이라는 객체지향 쿼리 언어를 써야 함. 거의 sql 이랑 동일함.
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        return result.stream().findAny();

    }

    @Override
    public List<Member> findAll() {
        // command + option + n : 한줄로 만들어줌
        // select m from Member m
        // : 이게 jpql 이라는 쿼리 언어인데 우리는 보통 테이블 대상으로 쿼리를 날리는데, 이건 객체 대상으로 쿼리를 날리는 것!
        // 그럼 이게 sql 로 변역이 됨
        // 정확히 말하면 Entity 를 대상으로 쿼리를 날리는 것인데 Member Entity 를 조회해, 그리고 여기서는 객체 자체를 select 하는 것
        // 보통은 '*' 로 select 하는데 이렇게 해서 여러가지 찾고 그래야 하는데 이건 그냥 이 한 줄로 가능!
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /*
    근데 이제 스프링 데이터 JPA 를 사용하면 jpql 도 사용 안해도 됨!
     */
}
