package no.kristiania.database;

import no.kristiania.database.objects.Member;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;


import static org.assertj.core.api.Assertions.assertThat;

class MemberDaoTest {

    private MemberDao memberDao;
    private final Random random = new Random();

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        memberDao = new MemberDao(dataSource);
    }

    @Test
    void shouldListInsertedProjectMembers() throws SQLException {
        Member member1 = exampleProjectMember();
        Member member2 = exampleProjectMember();
        memberDao.insert(member1);
        memberDao.insert(member2);
        assertThat(memberDao.listAllElements())
                .extracting(Member::getFirstName)
                .contains(member1.getFirstName(), member2.getFirstName());
    }

    private Member exampleProjectMember() {
        Member member = new Member();
        member.setFirstName(exampleProjectMemberFirstName());
        member.setLastName(exampleProjectMemberLastName());
        member.setEmail(exampleProjectMemberEmail());

        return member;
    }

    private String exampleProjectMemberFirstName() {
        String[] options = {"Ola", "Kari", "Harry", "Henriette"};
        return options[random.nextInt(options.length)];
    }

    private String exampleProjectMemberLastName() {
        String[] options = {"Halvorsen", "Haraldsen", "Jensen", "Svendsen"};
        return options[random.nextInt(options.length)];
    }

    private String exampleProjectMemberEmail() {
        String[] options = {"minmail@mailenmin.no", "test@tester.se", "hei@hade.com", "hallo@hello.co.uk"};
        return options[random.nextInt(options.length)];
    }
}