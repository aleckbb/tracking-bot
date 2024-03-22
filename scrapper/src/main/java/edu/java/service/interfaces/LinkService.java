package edu.java.service.interfaces;

import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.exceptions.AlreadyExistException;
import edu.java.exceptions.NotExistException;
import java.util.List;

public interface LinkService {
    void add(long chatId, String url, String username) throws AlreadyExistException;

    void remove(long chatId, String url) throws NotExistException;

    List<DTOLink> listAll(long chatId);
}
