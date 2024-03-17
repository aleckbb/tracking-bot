package edu.java.repos.chatLink;

import edu.java.dtoClasses.jdbc.DTOSub;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLinkRepository {
    void add(DTOSub sub);

    void remove(DTOSub sub);

    List<DTOSub> findAll();

    List<DTOSub> findByChatId(long chatId);

    List<DTOSub> findByLinkId(long linkId);
}
