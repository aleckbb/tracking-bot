package edu.java.scrapper.jdbc;

import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.repos.link.LinkRepositoryImpl;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class LinkRepositoryImplTest extends IntegrationTest {
    @Autowired private LinkRepositoryImpl linkRepository;

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
        assertEquals("https://test", linkRepository.findAll().getFirst().url());
        assertEquals(1, linkRepository.findAll().size());
    }

    @Test @Transactional @Rollback void remove() {
        linkRepository.add(link);
        assertEquals(1, linkRepository.findAll().size());
        DTOLink curLink =
            new DTOLink(
                linkRepository.findByUrl(link.url()).linkId(),
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
            "[DTOLink[linkId=" + linkRepository.findByUrl(link.url()).linkId() +
                ", url=https://test, updateAt=2022-01-01T10:30Z, checkAt=2022-01-01T10:30Z, linkType=, data=]]",
            linkRepository.findAll().toString()
        );
    }
}
