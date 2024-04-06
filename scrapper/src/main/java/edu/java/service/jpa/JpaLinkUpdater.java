package edu.java.service.jpa;

import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.repos.jpa.JpaLinkRepository;
import edu.java.repos.jpa.entities.Chat;
import edu.java.repos.jpa.entities.Link;
import edu.java.service.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class JpaLinkUpdater implements LinkUpdater {
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    public void update(long linkId, OffsetDateTime time, String data) {
        Link link = jpaLinkRepository.findById(linkId).orElseThrow();
        link.setUpdateAt(time);
        link.setData(data);
        jpaLinkRepository.saveAndFlush(link);
    }

    @Override
    public void check(long linkId, OffsetDateTime time) {
        Link link = jpaLinkRepository.findById(linkId).orElseThrow();
        link.setUpdateAt(time);
        jpaLinkRepository.saveAndFlush(link);
    }

    @Override
    public List<DTOLink> findOldLinksToUpdate(OffsetDateTime time) {
        return jpaLinkRepository.findAllByCheckAtBefore(time).stream().map(DTOLink::new).toList();
    }

    @Override
    public long[] allChatIdsByLinkId(long linkId) {
        List<Long> chatIds = jpaLinkRepository.findById(linkId).orElseThrow().getChats()
            .stream().map(Chat::getChatId).toList();
        long[] longChatIds = new long[chatIds.size()];
        int i = 0;
        for (long chatId : chatIds) {
            longChatIds[i] = chatId;
            i++;
        }
        return longChatIds;
    }
}
