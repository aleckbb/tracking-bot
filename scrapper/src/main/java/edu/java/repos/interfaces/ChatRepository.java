package edu.java.repos.interfaces;

import edu.java.dtoClasses.jdbc.DTOChat;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository {
    void add(DTOChat chat);

    void remove(DTOChat chat);

    List<DTOChat> findAll();
}
