/**
* author: Shin Jongmin
* date: 2023/11/14
* description: sse 알림을 위한 서비스
* */

package com.kernel360.orury.domain.notification.service;

import com.kernel360.orury.domain.notification.db.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {
    private static final Long DEFAULT_TIMEOUT = 60 * 60 * 1000L;
    private final EmitterRepository emitterRepository;

    /**
     * description: 클라이언트가 구독을 위해 호출하는 메서드.
     *
     * @param userId - 구독하는 클라이언트의 사용자 아이디.
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = createEmitter(userId);

//        sendToClient(userId, "EventStream Created. [userId=" + userId + "]", "new comment");

        return emitter;
    }

    public void notify(Long userId, Object event, String eventName){
        sendToClient(userId, event, eventName);
    }

    /**
     * description: 클라이언트에게 데이터를 전송
     *
     * @param id   - 데이터를 받을 사용자의 아이디.
     * @param data - 전송할 데이터.
     */
    private void sendToClient(Long id, Object data, String eventName) {
        SseEmitter emitter = emitterRepository.get(id);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(id)).name(eventName).data(data));
            } catch (IOException exception) {
                emitterRepository.deleteById(id);
                emitter.completeWithError(exception);
            }
        }
    }

    /**
     * description: 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     *
     * @param id - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    private SseEmitter createEmitter(Long id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        return emitter;
    }
}
