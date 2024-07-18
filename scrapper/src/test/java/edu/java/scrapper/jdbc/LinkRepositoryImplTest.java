package edu.java.scrapper.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.repos.jdbc.JdbcLinkRepository;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@DirtiesContext
public class LinkRepositoryImplTest extends IntegrationTest {
    @Autowired private JdbcLinkRepository linkRepository;

    static DTOLink link;

    @BeforeAll static void createDto() {
        link = new DTOLink(
            1L,
            "https://test",
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00"),
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00"),
            "",
            ""
        );
    }

    @Test @Transactional @Rollback void add() {
        assertEquals(0, linkRepository.findAll().size());
        linkRepository.add(link);
        assertEquals("https://test", linkRepository.findAll().getFirst().getUrl());
        assertEquals(1, linkRepository.findAll().size());
    }

    @Test @Transactional @Rollback void remove() {
        linkRepository.add(link);
        assertEquals(1, linkRepository.findAll().size());
        DTOLink curLink =
            new DTOLink(
                linkRepository.findByUrl(link.getUrl()).getLinkId(),
                null,
                null,
                null,
                null,
                null
            );
        linkRepository.remove(curLink);
        assertEquals(0, linkRepository.findAll().size());
    }

    @Test @Transactional @Rollback void findAll() {
        linkRepository.add(link);
        assertEquals(1, linkRepository.findAll().size());
        assertEquals(
            "[DTOLink(linkId=" + linkRepository.findByUrl(link.getUrl()).getLinkId() +
                ", url=https://test, updateAt=2022-01-01T10:30Z, checkAt=2022-01-01T10:30Z, linkType=, data=)]",
            linkRepository.findAll().toString()
        );
    }
}
