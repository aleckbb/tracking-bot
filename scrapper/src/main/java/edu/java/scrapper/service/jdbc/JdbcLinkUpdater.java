package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.dtoClasses.jdbc.DTOSub;
import edu.java.scrapper.repos.jdbc.JdbcChatLinkRepository;
import edu.java.scrapper.repos.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

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
