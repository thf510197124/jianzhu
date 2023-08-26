package com.feijian.exceptions;

import com.feijian.constant.CommonConstants;

public class DifferentUnitException extends BaseException{
    public DifferentUnitException(String message) {
        super(message, CommonConstants.DIFFERENT_UNIT_CODE);
    }
}
