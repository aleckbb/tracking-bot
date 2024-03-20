package edu.java.service.interfaces;

import edu.java.dtoClasses.jdbc.DTOLink;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater {
    void update(long linkId, OffsetDateTime time, String data);

    void check(long id, OffsetDateTime timestamp);

    List<DTOLink> findOldLinksToUpdate(OffsetDateTime timestamp);

    long[] allChatIdsByLinkId(long linkId);
}
