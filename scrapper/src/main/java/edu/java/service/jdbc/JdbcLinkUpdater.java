package edu.java.service.jdbc;

import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.repos.link.LinkRepositoryImpl;
import edu.java.service.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdater implements LinkUpdater {
    @Autowired
    private LinkRepositoryImpl linkRepository;

    @Override
    public void update(long linkId, OffsetDateTime time) {
        linkRepository.updateUpdateTime(linkId, time);
    }

    @Override
    public void check(long linkId, OffsetDateTime time) {
        linkRepository.updateCheckTime(linkId, time);
    }

    @Override
    public List<DTOLink> findOldLinksToUpdate(OffsetDateTime time) {
        return linkRepository.findOldLinksToCheck(time);
    }
}
