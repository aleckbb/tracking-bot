package edu.java.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.clients.StackOverflowClient;
import edu.java.dtoClasses.sof.StackOverflow;
import edu.java.repos.data.SofData;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SofHandler implements Handler<StackOverflow> {
    @Autowired
    private StackOverflowClient stackOverFlowClient;

    @Override
    public String getData(StackOverflow dto) {
        try {
            return Json.mapper().writeValueAsString(
                new SofData(
                    dto.items().getFirst().isAnswered()
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StackOverflow getInfo(String url) {
        String[] splitUrl = url.split("/");
        return stackOverFlowClient.getStackOverflow(splitUrl[splitUrl.length - 2]);
    }
}
