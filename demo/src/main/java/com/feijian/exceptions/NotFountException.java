package com.feijian.exceptions;

import com.feijian.constant.CommonConstants;

public class NotFountException extends BaseException{
    public NotFountException(String msg){
        super(msg, CommonConstants.NOT_FOUND);
    }

    public NotFountException(Throwable cause) {
        super(cause);
    }
}
