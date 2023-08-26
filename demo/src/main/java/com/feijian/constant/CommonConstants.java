package com.feijian.constant;

public class CommonConstants {
    /**
     * 缺少公司名称，不能添加公司
     */
    public static final Integer EXISTED = 40001;
    /**
     * 货物单位不一致，不能统计
     */

    public static final Integer DIFFERENT_UNIT_CODE = 40002;

    /**
     * 用户名或者密码错误
     */
    public static final Integer WRONG_USERNAME_PASSWORD = 400003;

    /**
     * 用户名已经存在，不能注册
     */
    public static final Integer USERNAME_EXISTED = 400004;
    public static final Integer UN_SUITABLE_TABLE = 400005;
    public static final Integer NOT_FOUND = 404;
    public static final String FILE_STORE_PATH_WIN = "D:\\feijian\\";
    public static final Float TAX_RATE = 0.13f;
}
