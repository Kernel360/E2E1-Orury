package com.kernel360.orury.global.domain;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostRemove;


@Slf4j
public class Listener {

    @PostRemove
    public void preRemove(Object entity) {
        log.info("삭제되었습니다. : {}", entity.toString());
    }
}
