package edu.java.scrapper.repos.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "chat")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Chat {
    public Chat(long chatId, OffsetDateTime createdAt, String name) {
        this.chatId = chatId;
        this.name = name;
        this.createdAt = createdAt;
    }

    @Id
    private long chatId;
    private String name;
    private OffsetDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "chat_link",
               joinColumns = @JoinColumn(name = "chat_id"),
               inverseJoinColumns = @JoinColumn(name = "link_id"))
    private Set<Link> links = new HashSet<>();
}
