package com.feijian.exceptions;

import com.feijian.constant.CommonConstants;

public class ExistException extends BaseException{
    public ExistException(String message) {
        super(message, CommonConstants.EXISTED);
    }
}
