package com.ioter.warehouse.common.exception;


public class ApiException extends BaseException {


    public ApiException(boolean code, String displayMessage) {
        super(code, displayMessage);
    }
}
