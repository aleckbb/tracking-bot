package edu.java.scrapper.repos.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "link")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Link {
    public Link(
        String url,
        OffsetDateTime updateAt,
        OffsetDateTime checkAt,
        String linkType,
        String data,
        Set<Chat> chats
    ) {
        this.url = url;
        this.updateAt = updateAt;
        this.checkAt = checkAt;
        this.linkType = linkType;
        this.data = data;
        this.chats = chats;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long linkId;
    private String url;
    private OffsetDateTime updateAt;
    private OffsetDateTime checkAt;
    private String linkType;
    private String data;

    @ManyToMany(mappedBy = "links")
    private Set<Chat> chats = new HashSet<>();
}
