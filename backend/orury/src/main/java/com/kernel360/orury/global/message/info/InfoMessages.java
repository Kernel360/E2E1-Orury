package com.kernel360.orury.global.message.info;

import lombok.Getter;

@Getter
public enum InfoMessages {
    BOARD_DELETED("게시판이 삭제되었습니다."),
    POST_CREATED("게시글이 생성되었습니다."),
    POST_DELETED("게시글이 삭제되었습니다."),
    POST_UPDATED("게시글이 수정되었습니다"),
    COMMENT_CREATED("댓글이 생성되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    COMMENT_UPDATED("댓글이 수정되었습니다.")
    ;

    private final String message;

    InfoMessages(String message) {
        this.message = message;
    }

}
