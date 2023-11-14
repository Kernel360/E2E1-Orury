package com.kernel360.orury.domain.user.notification.db;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String emitterId, Object event);
    Map<String, SseEmitter> findAllEmitterStartWithByEmail(String email);
    Map<String, Object> findAllEventCacheStartWithByEmail(String email);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(Long userId);
    void deleteAllEventCacheStartWithId(Long userId);
}
