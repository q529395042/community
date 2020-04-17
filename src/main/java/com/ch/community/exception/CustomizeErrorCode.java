package com.ch.community.exception;

public enum  CustomizeErrorCode implements ICustomizeErrorCode {
    Parent_Id_NOT_Found(2003,"回复评论未找到"),
    SYS_ERROR(2004, "服务冒烟了，要不然你稍后再试试！！！"),
    Type_Error(2005,"回复类型错误"),
    NO_LOGIN (2002,"用户未登录"),
    PAGE_NO_FOUND(2000,"页面未找到"),
    QUESTION_NO_FOUND(2006,"你的问题找不到要不换一个试试"),
    CONTENT_IS_EMPTY(2007,"回复内容不能为空"),
    FILE_UPLOAD_FAIL(2008,"图片上传失败"),
    NOT_USERNAME(2009,"用户名、密码或昵称为空"),
    USERNAME_EXIST(2010,"该用户名已存在"),
    USERNAME_ERROR(2011,"用户名或密码错误");

    private  Integer code;
   private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
