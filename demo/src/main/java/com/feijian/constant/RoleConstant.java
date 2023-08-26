package com.feijian.constant;

import com.feijian.domain.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleConstant {
    public static final String ROOT = "ROLE_root";
    public static final String ROOT_SIMPLE = "root";
    public static final String ROOT_ZN = "根";
    public static final int ROOT_CODE = 100;

    public static final String ADMIN = "ROLE_admin";
    public static final String ADMIN_SIMPLE = "admin";
    public static final String ADMIN_ZN = "管理员";
    public static final int ADMIN_CODE = 80;
    public static final String MANAGER = "ROLE_manager";
    public static final String MANAGER_SIMPLE = "manager";
    public static final String MANAGER_ZN = "管理者";
    public static final int MANAGER_CODE = 50;


    public static final String USER = "ROLE_user";
    public static final String User_SIMPLE = "user";
    public static final String USER_ZN = "普通用户";
    public static final int USER_CODE = 0;
    public static final String WARE = "ROLE_ware";
    public static final String WARE_SIMPLE = "ware";
    public static final String WARE_ZN = "库管";

    public static final int WARE_CODE = 0;

    public static int getRoleCode(Role role){
        if (role.getName().equalsIgnoreCase(ROOT_SIMPLE) || role.getName().equalsIgnoreCase(ROOT)){
            return ROOT_CODE;
        }else if (role.getName().equalsIgnoreCase(ADMIN_SIMPLE) || role.getName().equalsIgnoreCase(ADMIN)){
            return ADMIN_CODE;
        }else if (role.getName().equalsIgnoreCase(MANAGER_SIMPLE) || role.getName().equalsIgnoreCase(MANAGER)){
            return MANAGER_CODE;
        }else{
            return 0;
        }
    }
    public static String getRoleZN(Role role){
        if (role.getName().equalsIgnoreCase(ROOT)){
            return ROOT_ZN;
        }else if (role.getName().equalsIgnoreCase(ADMIN_SIMPLE) || role.getName().equalsIgnoreCase(ADMIN)){
            return ADMIN_ZN;
        }else if (role.getName().equalsIgnoreCase(MANAGER_SIMPLE) || role.getName().equalsIgnoreCase(MANAGER)){
            return MANAGER_ZN;
        }else{
            return WARE_ZN;
        }
    }
    public static int getRoleCode(List<Role> roleList){
        int code = 0;
        for (Role role : roleList){
            if (getRoleCode(role) > code){
                code = getRoleCode(role);
            }
        }
        return code;
    }
    public static List<String> getSubRole(List<Role> roleList){
        int code = getRoleCode(roleList);
        List<String> roles = new ArrayList<>();
        if (code == ROOT_CODE){
            roles.add(ADMIN_ZN);
            roles.add(MANAGER_ZN);
            roles.add(USER_ZN);
            roles.add(WARE_ZN);
        }else if (code == ADMIN_CODE){
            roles.add(MANAGER_ZN);
            roles.add(USER_ZN);
            roles.add(WARE_ZN);
        }else if (code == MANAGER_CODE){
            roles.add(USER_ZN);
            roles.add(WARE_ZN);
        }else{
            return null;
        }
        return roles;
    }
    public static Role getRole(String name){
        Role role = new Role();
        if (name.equalsIgnoreCase(ADMIN) || name.equalsIgnoreCase(ADMIN_SIMPLE) || name.equalsIgnoreCase(ADMIN_ZN)){
            role.setName(ADMIN);role.setNameZN(ADMIN_ZN);
            role.setNameSimple(ADMIN_SIMPLE);
        }else if (name.equalsIgnoreCase(USER) || name.equalsIgnoreCase(User_SIMPLE) || name.equalsIgnoreCase(USER_ZN)){
            role.setNameZN(USER_ZN);
            role.setName(USER);
            role.setNameSimple(User_SIMPLE);
        }else if (name.equalsIgnoreCase(WARE) || name.equalsIgnoreCase(WARE_SIMPLE) || name.equalsIgnoreCase(WARE_ZN)){
            role.setName(WARE);
            role.setNameZN(WARE_ZN);
            role.setNameSimple(WARE_SIMPLE);
        }else if (name.equalsIgnoreCase(ROOT) || name.equalsIgnoreCase(ROOT_SIMPLE) || name.equalsIgnoreCase(ROOT_ZN)){
            role.setName(ROOT);
            role.setNameZN(ROOT_ZN);
            role.setNameSimple(ROOT_SIMPLE);
        }else if (name.equalsIgnoreCase(MANAGER) || name.equalsIgnoreCase(MANAGER_SIMPLE) || name.equalsIgnoreCase(MANAGER_ZN)){
            role.setName(MANAGER);
            role.setNameZN(MANAGER_ZN);
            role.setNameSimple(MANAGER_SIMPLE);
        }else{
            return null;
        }
        return role;
    }
}
