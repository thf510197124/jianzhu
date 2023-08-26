package com.feijian.utils;

import com.feijian.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    public static User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static String getName(){
        User user = getUser();
        return user.getEmployee().getName();
    }
}
