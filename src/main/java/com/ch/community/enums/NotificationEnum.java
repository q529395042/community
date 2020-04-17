package com.ch.community.enums;

public enum NotificationEnum {
    REPLY_QUESTION(1,"回复了问题"),
    COMMENT(2,"评论了问题");
    private Integer type;
    private String name;

    NotificationEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
