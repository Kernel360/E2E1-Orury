package com.kernel360.orury.domain.notification.controller;


import com.kernel360.orury.domain.notification.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotifyController {
    private final NotifyService notifyService;

    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @PathVariable Long id
    ){
        return notifyService.subscribe(id);
    }

    @PostMapping("/send-data/{id}")
    public void sendData(@PathVariable Long id){
        notifyService.notify(id, "data");
    }

}