package com.kernel360.orury.domain.user.notification.controller;


import com.kernel360.orury.domain.user.notification.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotifyController {
    private final NotifyService notifyService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(
            @AuthenticationPrincipal User principal,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ){
        return notifyService.subscribe(principal.getUsername(), lastEventId);
    }

}