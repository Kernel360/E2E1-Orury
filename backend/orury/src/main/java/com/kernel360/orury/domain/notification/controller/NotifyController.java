package com.kernel360.orury.domain.notification.controller;


import com.kernel360.orury.domain.notification.service.NotifyService;
import com.kernel360.orury.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotifyController {
    private final NotifyService notifyService;
    private final UserService userService;

    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @PathVariable
            Long userId
    ){
        return notifyService.subscribe(userId);
    }

    @PostMapping("/send-data/{id}")
    public void sendData(@PathVariable Long id){
        notifyService.notify(id, "data");
    }

}