package edu.java.scrapper.service.interfaces;

import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.exceptions.AlreadyExistException;
import edu.java.scrapper.exceptions.NotExistException;
import java.util.List;

public interface LinkService {
    void add(long chatId, String url, String username) throws AlreadyExistException;

    void remove(long chatId, String url) throws NotExistException;

    List<DTOLink> listAll(long chatId);
}
