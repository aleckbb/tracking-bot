package edu.java.scrapper.service.interfaces;

import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater {
    void update(long linkId, OffsetDateTime time, String data);

    void check(long linkId, OffsetDateTime time);

    List<DTOLink> findOldLinksToUpdate(OffsetDateTime time);

    long[] allChatIdsByLinkId(long linkId);
}
