package edu.java.repos.jdbc;

import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.repos.interfaces.LinkRepository;
import edu.java.repos.mappers.LinkMapper;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("MagicNumber") @Repository public class JdbcLinkRepository implements LinkRepository {
    @Autowired private JdbcClient jdbcClient;

    @Transactional @Override public void add(DTOLink link) {
        jdbcClient.sql("INSERT INTO link VALUES(DEFAULT, ?, ?, ?, ?, ?)")
            .params(link.getUrl(), link.getUpdateAt(), link.getCheckAt(), link.getLinkType(), link.getData())
            .update();
    }

    @Transactional @Override public void remove(DTOLink link) {
        jdbcClient.sql("DELETE FROM link WHERE link_id=?").param(link.getLinkId()).update();
    }

    @Transactional @Override public List<DTOLink> findAll() {
        return jdbcClient.sql("SELECT * FROM link").query(new LinkMapper()).list();
    }

    @Transactional @Override public DTOLink findByUrl(String url) {
        try {
            return jdbcClient.sql("SELECT * FROM link WHERE url=?").param(url).query(new LinkMapper()).single();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional @Override public void updateData(long linkId, OffsetDateTime time, String data) {
        jdbcClient.sql("UPDATE link SET update_at=?, data=? WHERE link_id=?").param(time).param(data).param(linkId)
            .update();
    }

    @Transactional @Override public void updateCheckTime(long linkId, OffsetDateTime time) {
        jdbcClient.sql("UPDATE link SET check_at=? WHERE link_id=?").param(time).param(linkId).update();
    }

    @Transactional @Override public List<DTOLink> findOldLinksToCheck(OffsetDateTime time) {
        return jdbcClient.sql("SELECT * FROM link WHERE  check_at<?").param(time.minusMinutes(5))
            .query(new LinkMapper()).list();
    }
}
