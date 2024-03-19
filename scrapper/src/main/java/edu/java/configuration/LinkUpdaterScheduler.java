package edu.java.configuration;

import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.service.interfaces.LinkUpdater;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@EnableScheduling
public class LinkUpdaterScheduler {
    @Autowired
    LinkUpdater linkUpdater;
    static final Logger LOGGER = Logger.getLogger(LinkUpdaterScheduler.class.getName());

    @Scheduled(fixedDelayString = "#{scheduler.interval}")
    public void update() {
        LOGGER.info("I'm updating!");
        OffsetDateTime time = OffsetDateTime.now();
        List<DTOLink> oldLinks = linkUpdater.findOldLinksToUpdate(time);
        for(DTOLink link : oldLinks) {
            linkUpdater.check(link.linkId(), time);

        }
    }
}
