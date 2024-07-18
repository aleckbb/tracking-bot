package edu.java.scrapper.api;

import edu.java.models.Request.AddLinkRequest;
import edu.java.models.Request.RemoveLinkRequest;
import edu.java.models.Response.ApiErrorResponse;
import edu.java.models.Response.LinkResponse;
import edu.java.models.Response.ListLinksResponse;
import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.exceptions.AlreadyExistException;
import edu.java.scrapper.exceptions.NotExistException;
import edu.java.scrapper.exceptions.RepeatedRegistrationException;
import edu.java.scrapper.service.interfaces.ChatService;
import edu.java.scrapper.service.interfaces.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("RegexpSinglelineJava")
@RequiredArgsConstructor
@RestController
public class ScrapperController {
    private final ChatService chatService;
    private final LinkService linkService;

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Чат зарегистрирован",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })

    @PostMapping("/tg-chat/{id}")
    public void chatReg(
        @PathVariable long id,
        @RequestBody String username
    ) throws RepeatedRegistrationException {
        chatService.register(id, username);
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Чат успешно удалён",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/tg-chat/{id}")
    public void chatDel(@PathVariable long id) throws NotExistException {
        chatService.unregister(id);
    }

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылки успешно получены",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ListLinksResponse.class)
            )}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )}
        )
    })
    @GetMapping("/links")
    public ListLinksResponse getLinks(@RequestHeader(name = "Tg-Chat-Id") long id) throws NotExistException {
        List<DTOLink> links = linkService.listAll(id);
        if (links.isEmpty()) {
            throw new NotExistException("Вы не отслеживаете ни одной ссылки!");
        }
        LinkResponse[] res = new LinkResponse[links.size()];
        int i = 0;
        for (DTOLink link : links) {
            res[i] = new LinkResponse(id, link.getUrl());
            i++;
        }
        return new ListLinksResponse(res, res.length);
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно добавлена",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LinkResponse.class)
            )}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )}
        )
    })
    @PostMapping("/links")
    public LinkResponse addLink(
        @RequestHeader(name = "Tg-Chat-Id") long id,
        @RequestHeader(name = "Username") String username,
        @RequestBody AddLinkRequest addLinkRequest
    ) throws AlreadyExistException {
        linkService.add(id, addLinkRequest.link(), username);
        return new LinkResponse(id, addLinkRequest.link());
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно убрана",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LinkResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ссылка не найдена",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/links")
    public LinkResponse delLink(
        @RequestHeader(name = "Tg-Chat-Id") long id,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) throws NotExistException {
        linkService.remove(id, removeLinkRequest.link());
        return new LinkResponse(id, removeLinkRequest.link());
    }

    @Operation(summary = "Проверить, что пользователь зарегистрирован")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Пользователь уже зарегистрирован"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )}
        )
    })
    @GetMapping("/tg-chat/{id}")
    public Boolean hasUser(@PathVariable long id) {
        return chatService.userExist(id);
    }
}
