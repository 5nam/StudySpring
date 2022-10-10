package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// jdbctemplate 의 사용법은 검색해서 찾아볼 수 있으므로 넘어감.
public class JdbcTemplateMemberRepository implements MemberRepository{

    // 얘는 injection 을 받을 수 있는 것은 아님.
    // 그래서 생성자를 만들고, 그 생성자에 매개변수로 DataSource 를 넣어서
    // new JdbcTemplate(dataSource) 로 주입해주면 됨.
    private final JdbcTemplate jdbcTemplate;

    /*
    참고) 생성자가 딱 하나만 있으면 스프링 빈에 등록되고 나서 Autowired 를 생략할 수 있음.
    dataSource 자동으로 인젝션 됨.
     */
    // @Autowired
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        // SimpleJdbcInsert 가 있는데 이게 jdbcTemplate 를 넘겨서 만드는 건데,
        // 아래 4줄 코드를 활용하면 사실 id, name, 테이블 이름 member 있을 때 쿼리를 짜던 걸 안짜도 됨.
        // 그냥 insert 쿼리를 만들어줌
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        // executeAndReturnKey 를 통해서 키를 받아서 멤버에다가 setId 해서 만들어주는 것
        Number key = jdbcInsert.executeAndReturnKey(new
                MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }


    @Override
    public Optional<Member> findById(Long id) {
        /*
        결과가 나오는 것을 RowMapper 로 맵핑을 해줘야 함
         */
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    /* 람다로 바꾸기 가능 RowMapper<Member>() 에 커서 올려놓고 option+enter 누르면 람다로 바꿀 수 있음.
    private RowMapper<Member> memberRowMapper() {
        return new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return member;
            }
        };
    }
    */

    private RowMapper<Member> memberRowMapper() {
        // 객체 생성에 관한건 여기서 생성되는 것
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }

}
