package com.ch.community.module;

import lombok.Data;

@Data
public class User {
    private String accountId;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
}
