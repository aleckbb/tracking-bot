package edu.java.service.interfaces;

import edu.java.exceptions.NotExistException;
import edu.java.exceptions.RepeatedRegistrationException;

public interface ChatService {
    void register(long chatId, String username) throws RepeatedRegistrationException;

    void unregister(long chatId) throws NotExistException;

    Boolean userExist(long chatId);
}
