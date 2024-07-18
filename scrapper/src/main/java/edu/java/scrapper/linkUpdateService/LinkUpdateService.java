package edu.java.scrapper.linkUpdateService;

import edu.java.models.Request.LinkUpdate;

public interface LinkUpdateService {
    void sendUpdate(LinkUpdate linkUpdate);
}
