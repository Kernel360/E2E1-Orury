package com.kernel360.orury.global.message.info;

public enum InfoMessages {
    BOARD_DELETED("게시판이 삭제되었습니다."),
    POST_DELETED("게시글이 삭제되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다.");

    private final String message;

    InfoMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
