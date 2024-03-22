package edu.java.service.jdbc;

import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.dtoClasses.jdbc.DTOSub;
import edu.java.repos.chatLink.ChatLinkRepository;
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
    @Autowired
    private ChatLinkRepository chatLinkRepository;

    @Override
    public void update(long linkId, OffsetDateTime time, String data) {
        linkRepository.updateData(linkId, time, data);
    }

    @Override
    public void check(long linkId, OffsetDateTime time) {
        linkRepository.updateCheckTime(linkId, time);
    }

    @Override
    public List<DTOLink> findOldLinksToUpdate(OffsetDateTime time) {
        return linkRepository.findOldLinksToCheck(time);
    }

    @Override
    public long[] allChatIdsByLinkId(long linkId) {
        List<Long> chatIdsList = chatLinkRepository.findByLinkId(linkId)
            .stream().map(DTOSub::chatId).toList();
        long[] chatIds = new long[chatIdsList.size()];
        int i = 0;
        for (long val : chatIdsList) {
            chatIds[i] = val;
            i++;
        }
        return chatIds;
    }
}
