package edu.java.scrapper.repos.jpa;

import edu.java.scrapper.repos.jpa.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface JpaChatRepository extends JpaRepository<Chat, Long> {

}
