package com.ch.community.enums;

public enum  NotificationStatusEnum {
    NO_READ(0),
    READ(1);
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    NotificationStatusEnum(Integer status) {
        this.status = status;
    }

}
