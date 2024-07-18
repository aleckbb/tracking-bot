package edu.java.bot.api;

import edu.java.bot.service.UpdateSender;
import edu.java.models.Request.LinkUpdate;
import edu.java.models.Response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/updates")
public class BotController {
    @Autowired
    public UpdateSender updateSender;

    @Operation(summary = "Отправить обновление")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Обновление обработано",
            content = {
                @Content()
            }
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class)
                )
            }
        )
    })
    @PostMapping
    public void sendUpdate(@RequestBody LinkUpdate linkUpdate) {
        updateSender.sendUpdate(linkUpdate);
    }
}
