package edu.java.repos.link;

import edu.java.dtoClasses.jdbc.DTOLink;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository {
    void add(DTOLink chat);

    void remove(DTOLink chat);

    List<DTOLink> findAll();

    DTOLink findByUrl(String url);

    void updateUpdateTime(long linkId, OffsetDateTime time);

    void updateCheckTime(long linkId, OffsetDateTime time);

    List<DTOLink> findOldLinksToCheck(OffsetDateTime time);
}
