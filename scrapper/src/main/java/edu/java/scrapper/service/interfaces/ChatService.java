package edu.java.scrapper.service.interfaces;

import edu.java.scrapper.exceptions.NotExistException;
import edu.java.scrapper.exceptions.RepeatedRegistrationException;

public interface ChatService {
    void register(long chatId, String username) throws RepeatedRegistrationException;

    void unregister(long chatId) throws NotExistException;

    Boolean userExist(long chatId);
}
