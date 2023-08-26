package com.feijian.exceptions;

import com.feijian.constant.CommonConstants;

public class UnsuitableTableException extends BaseException{
    public UnsuitableTableException(String message) {
        super(message, CommonConstants.DIFFERENT_UNIT_CODE);
    }
}
