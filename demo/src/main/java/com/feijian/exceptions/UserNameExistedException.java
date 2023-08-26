package com.feijian.exceptions;

import com.feijian.constant.CommonConstants;

public class UserNameExistedException extends BaseException{
    public UserNameExistedException(String msg) {
        super(msg, CommonConstants.USERNAME_EXISTED);
    }
}
