package hello.hellospring.service;

import hello.hellospring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    // 스프링 데이터 jpa 방식
    private final MemberRepository memberRepository;

    // 이런 경우에는 스프링 컨테이너에서 MemberRepository 를 찾는 지금 구현해놓은게 없는거 아닌가?
    // interface 로 SpringDataJpaMemberRepository extends JpaRepository 를 해놓았기 때문에
    // 스프링 데이터 jpa 가 작성해놓은 interface 에 대한 구현체를 만듦 그리고 스프링 빈에 등록해 놓음
    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /* jpa 방식
    // 이 방식으로 받아올 수도 있고
    // 원래 스펙에서는 @PersistenceContext 로 EntityManager 을 받아옴
    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }
    */
    /* jdbc Template
    private DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    */

    @Bean
    public MemberService memberService() {
        // jpa 방식
//        return new MemberService(memberRepository());
        return new MemberService(memberRepository); // 스프링 데이터 jpa 방식

    }

    /* 스프링 데이터 jpa 버전은 이게 필요 없음
    @Bean
    public MemberRepository memberRepository() {
        // 순수 jdbc 버전
        //return new JdbcMemberRepository(dataSource);
        // jdbcTemplate 버전
//        return new JdbcTemplateMemberRepository(dataSource);
        // jpa 버전
//        return new JpaMemberRepository(em);
    }

     */
}
