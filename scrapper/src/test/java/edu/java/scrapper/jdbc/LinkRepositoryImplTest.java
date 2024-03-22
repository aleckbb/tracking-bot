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
            OffsetDateTime.of(2024, 3, 17, 18, 31, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2024, 3, 17, 19, 11, 0, 0, ZoneOffset.UTC),
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
                ", url=https://test, updateAt=2024-03-17T21:31Z, checkAt=2024-03-17T22:11Z, linkType=, data=]]",
            linkRepository.findAll().toString()
        );
    }
}
