package edu.java.scrapper.dtoClasses.jdbc;

import edu.java.scrapper.repos.jpa.entities.Link;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
public class DTOLink {
    private Long linkId;
    private String url;
    private OffsetDateTime updateAt;
    private OffsetDateTime checkAt;
    private String linkType;
    private String data;

    public DTOLink(Link link) {
        this.linkId = link.getLinkId();
        this.url = link.getUrl();
        this.updateAt = link.getUpdateAt();
        this.checkAt = link.getCheckAt();
        this.linkType = link.getLinkType();
        this.data = link.getData();
    }
}
