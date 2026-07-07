package com.system.wallet.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String message;
    private boolean status;
    private int code;
    private Object data;


    public ApiResponse(String message, boolean status, int code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public ApiResponse(String message, boolean status, int code, Object data) {
        this.message = message;
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
