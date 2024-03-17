package edu.java.api;

import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.exceptions.AlreadyExistException;
import edu.java.exceptions.NotExistException;
import edu.java.exceptions.RepeatedRegistrationException;
import edu.java.models.Request.AddLinkRequest;
import edu.java.models.Request.RemoveLinkRequest;
import edu.java.models.Response.ApiErrorResponse;
import edu.java.models.Response.LinkResponse;
import edu.java.models.Response.ListLinksResponse;
import edu.java.service.interfaces.ChatService;
import edu.java.service.interfaces.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("RegexpSinglelineJava")
@AllArgsConstructor
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
    public void chatReg(@PathVariable long id, String username) {
        try {
            chatService.register(id, username);
        } catch (RepeatedRegistrationException e) {
            System.out.println(e.getMessage());
        }
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
    public void chatDel(@PathVariable long id) {
        try {
            chatService.unregister(id);
        } catch (NotExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылки успешно получены",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ListLinksResponse.class)
            )
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
    @GetMapping("/links")
    public List<DTOLink> getLinks(@RequestHeader(name = "Tg-Chat-Id") long id) {
        return linkService.listAll(id);
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно добавлена",
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
        )
    })
    @PostMapping("/links")
    public void addLink(
        @RequestHeader(name = "Tg-Chat-Id") long id,
        @RequestParam/*@RequestBody(required = true)*/ AddLinkRequest addLinkRequest,
        String username
    ) {
        try {
            linkService.add(id, addLinkRequest.link(), username);
        } catch (AlreadyExistException e) {
            System.out.println(e.getMessage());
        }
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
    public void delLink(
        @RequestHeader(name = "Tg-Chat-Id") long id,
        @RequestBody(required = true) RemoveLinkRequest removeLinkRequest
    ) {
        try {
            linkService.remove(id, removeLinkRequest.link());
        } catch (NotExistException e) {
            System.out.println(e.getMessage());
        }
    }
}
