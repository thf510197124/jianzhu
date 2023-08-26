package com.feijian.controller;

import com.feijian.exceptions.BaseException;
import com.feijian.exceptions.ExistException;
import com.feijian.exceptions.NotFountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionController {
    private final Logger logger = LoggerFactory.getLogger(ExceptionController.class);
    @ExceptionHandler( UsernameNotFoundException.class)
    public String exceptionController(Exception ex){
        logger.error("用户不存在");
        return "用不不存在";
    }
    @ExceptionHandler( ExistException.class)
    public String existExceptionController(ExistException ex){
        logger.error(ex.getMessage());
        return ex.getMessage() + ":错误代码:" + ex.getStatus();
    }
    @ExceptionHandler(BaseException.class)
    public int exceptionController(BaseException ex){
        logger.error(ex.getMessage());
        return ex.getStatus();
    }
    @ExceptionHandler(NotFountException.class)
    public int exceptionController(NotFountException ex){
        logger.error(ex.getMessage());
        return ex.getStatus();
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleTypeMissMatch(MethodArgumentTypeMismatchException ex){
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        String message = String.format("'%s' should be a valid '%s' and '%s' isn't",
                name, type, value);
        ex.printStackTrace();
        System.out.println(message);
        // Do the graceful handling
    }
    @ExceptionHandler(BindException.class)
    public String bindException(BindException ex){
        ex.printStackTrace();
        return ex.getMessage();
    }

    /**
     * 用来处理之前是post的页面，出现错误，这时返回index
     * @return
     */
    @ExceptionHandler({NumberFormatException.class})
    public String missArgument(){
        return "/index";
    }
}
