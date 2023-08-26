package com.feijian.exceptions;

import com.feijian.constant.CommonConstants;

public class WrongUsernameOrPassword extends BaseException {
    public WrongUsernameOrPassword(String msg){
        super(msg, CommonConstants.WRONG_USERNAME_PASSWORD);
    }
}
