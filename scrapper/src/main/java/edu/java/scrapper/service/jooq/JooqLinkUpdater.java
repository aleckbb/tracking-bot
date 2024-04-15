package edu.java.scrapper.service.jooq;

import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.dtoClasses.jdbc.DTOSub;
import edu.java.scrapper.repos.jooq.JooqChatLinkRepository;
import edu.java.scrapper.repos.jooq.JooqLinkRepository;
import edu.java.scrapper.service.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JooqLinkUpdater implements LinkUpdater {
    private final JooqChatLinkRepository jooqChatLinkRepository;
    private final JooqLinkRepository jooqLinkRepository;

    @Override
    public void update(long linkId, OffsetDateTime time, String data) {
        jooqLinkRepository.updateData(linkId, time, data);
    }

    @Override
    public void check(long linkId, OffsetDateTime time) {
        jooqLinkRepository.updateCheckTime(linkId, time);
    }

    @Override
    public List<DTOLink> findOldLinksToUpdate(OffsetDateTime time) {
        return jooqLinkRepository.findOldLinksToCheck(time);
    }

    @Override
    public long[] allChatIdsByLinkId(long linkId) {
        List<Long> chatIdsList = jooqChatLinkRepository.findByLinkId(linkId)
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
