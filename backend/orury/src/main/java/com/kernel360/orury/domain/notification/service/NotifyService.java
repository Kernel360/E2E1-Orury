package com.kernel360.orury.domain.user.notification.service;

import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.notification.db.EmitterRepository;
import com.kernel360.orury.domain.user.notification.db.NotifyEntity;
import com.kernel360.orury.domain.user.notification.db.NotifyRepository;
import com.kernel360.orury.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {
    private static final Long DEFAULT_TIMEOUT = 60 * 60 * 1000L;

    private final EmitterRepository emitterRepository;
    private final NotifyRepository notifyRepository;
    private final UserService userService;

    public SseEmitter subscribe(String userEmail, String lastEventId) {
//        String userId = userService.getUserIdByEmail(userEmail).toString();
        String emitterId = makeTimeIncludeId(userEmail);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // prevent 503 error
        String eventId = makeTimeIncludeId(userEmail);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userEmail=" + userEmail+"]");

        // clident가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실 예방
        if(hasLostData(lastEventId)){
            sendLostData(lastEventId, userEmail, emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(String email){
        return email + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        }catch(IOException exception){
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId){
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String userEmail, String emitterId, SseEmitter emitter){
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByEmail(String.valueOf(userEmail));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    public void send(UserEntity user, NotifyEntity.NotificationType notificationType, String content, String url) {
        NotifyEntity notification = notifyRepository.save(createNotification(user, notificationType, content, url));

        String receiverEmail = user.getEmailAddr();
        String eventId = receiverEmail + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByEmail(receiverEmail);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotifyDto.Response.createResponse(notification));
                }
        );
    }
    private NotifyEntity createNotification(UserEntity user, NotifyEntity.NotificationType notificationType, String content, String url) {
        return NotifyEntity.builder()
                .user(user)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false)
                .build();
    }
}
