package edu.java.service.interfaces;

import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.exceptions.NotExistException;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater {
    void update(long id, OffsetDateTime timestamp) throws NotExistException;

    void check(long id, OffsetDateTime timestamp);

    List<DTOLink> findOldLinksToUpdate(OffsetDateTime timestamp);
}
