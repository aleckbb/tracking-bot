package edu.java.scrapper.repos.jooq;

import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.repos.interfaces.LinkRepository;
import edu.java.scrapper.repos.jooq.generated.tables.Link;
import java.time.OffsetDateTime;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqLinkRepository implements LinkRepository {
    @Autowired DSLContext context;

    @Transactional
    @Override
    public void add(DTOLink link) {
        context.insertInto(Link.LINK)
            .set(Link.LINK.URL, link.getUrl())
            .set(Link.LINK.UPDATE_AT, link.getUpdateAt())
            .set(Link.LINK.CHECK_AT, link.getCheckAt())
            .set(Link.LINK.LINK_TYPE, link.getLinkType())
            .set(Link.LINK.DATA, link.getData())
            .execute();
    }

    @Transactional
    @Override
    public void remove(DTOLink link) {
        context.deleteFrom(Link.LINK)
            .where(Link.LINK.LINK_ID.eq(link.getLinkId()))
            .execute();
    }

    @Transactional
    @Override
    public List<DTOLink> findAll() {
        return context.selectFrom(Link.LINK)
            .fetchInto(DTOLink.class);
    }

    @Transactional
    @Override
    public DTOLink findByUrl(String url) {
        return context.selectFrom(Link.LINK)
            .where(Link.LINK.URL.eq(url))
            .fetchInto(DTOLink.class).getFirst();
    }

    @Transactional
    @Override
    public void updateData(long linkId, OffsetDateTime time, String data) {
        context.update(Link.LINK)
            .set(Link.LINK.DATA, data)
            .set(Link.LINK.UPDATE_AT, time)
            .where(Link.LINK.LINK_ID.eq(linkId))
            .execute();
    }

    @Transactional
    @Override
    public void updateCheckTime(long linkId, OffsetDateTime time) {
        context.update(Link.LINK)
            .set(Link.LINK.UPDATE_AT, time)
            .where(Link.LINK.LINK_ID.eq(linkId))
            .execute();
    }

    @Transactional
    @Override
    @SuppressWarnings("MagicNumber")
    public List<DTOLink> findOldLinksToCheck(OffsetDateTime time) {
        return context.selectFrom(Link.LINK)
            .where(Link.LINK.CHECK_AT.lt(time.minusMinutes(5)))
            .fetchInto(DTOLink.class);
    }
}
