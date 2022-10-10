package hello.hellospring.domain;

import javax.persistence.*;

/*
jpa 는 인터페이스임. 인터페이스로 제공되고, 구현체로 hibernate, 이클립스 등등이 있는 것인데
jpa 는 자바 진영의 표준 인터페이스이고, 구현은 각 업체마다 하는 것!
jpa 는 객체랑 ORM(Object Relational Mapping)  이라는 기술인데, Object 와 관계형(Relational) 테이블을 Mapping 시킨다는 것
매핑하는 방법이 @Entity 인 것!
 */
@Entity
public class Member {
    // 비즈니스 요구사항 참고하면 id, name 데이터 존재한다고 나와있음
    // @Id : PK 라는 뜻
    // @GeneratedValue : 지금 이 PK(id)는 DB 에서 자동생성되도록 설정되어 있음. 그럴 때, 지금 이 id 가 DB 에서 자동 생성된다는 것을 알려주기 위해
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 데이터 구분을 위해 시스템이 정하는 아이디
    // 지금 DB 에 name 이라고 그대로 되어 있어서 상관은 없지만,
    // 만약, DB 에 username 이라고 되어 있는 경우에는 @Column(name = "username") 라고 달아주면 되는 것!
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
