package edu.java.scrapper.repos.jpa;

import edu.java.scrapper.repos.jpa.entities.Link;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    Link findByUrl(String url);

    boolean existsByUrl(String url);

    List<Link> findAllByCheckAtBefore(OffsetDateTime time);
}
