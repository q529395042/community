package com.ch.community.dto;

import com.ch.community.exception.CustomizeErrorCode;
import com.ch.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO {
    private Integer code;
    private String message;
    private Object data;

    public ResultDTO() {
    }

    public static ResultDTO errorOf(CustomizeErrorCode type_error) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(type_error.getCode());
        resultDTO.setMessage(type_error.getMessage());
        return  resultDTO;
    }

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO success() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("成功");
        return resultDTO;
    }

    public static ResultDTO success(Object data) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("成功");
        resultDTO.setData(data);
        return resultDTO;
    }



    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }



}

