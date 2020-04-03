package com.qty.util;

public class ConstantParameter {

    //qiniuACCESSKEY
    public static final String ACCESSKEY  = "VEBYoHGhadF8Zlk7LmaG38u9XO5v2AGwDVLr_BLV";

    //qiniuSECRETKEY
    public static final String SECRETKEY  = "tpX-sNQ8aubgsAo9TXyh3jzeB11IVmJFdxvZOgXO";

    //Redis过期时间
    public static final String FIVEMIN  = "FIVEMIN";

    //Redis过期时间
    public static final String FIVESEC = "FIVESEC";

    //超级管理员Id
    public static final Long SUPER_ADMIN = 1L;

    //当前第1页
    public static final Long CURPAGE= 1L;

    //页容量10页
    public static final Long LIMIT_SIZE=10L;

    //检索条件
    public static final String SEARCH="search";

    //父分类ID
    public static final String PARENTSORTID="parentSortId";

    //当前页码
    public static final String PAGE = "page";

    //每页显示记录数
    public static final String LIMIT = "limit";

    //排序字段
    public static final String ORDER_FIELD = "sidx";

    //排序方式
    public static final String ORDER = "order";

    //升序
    public static final String ASC = "asc";

    //降序
    public static final String DESC = "desc";

    //密匙
    public static final String JWT_SECRET = "8677df7fc3a34e26a61c034d5ec8245d";

    //token失效的时间：ms为单位
    public static final Long ACCESS_TOKEN_EXPIRE=6000000L;

    //redis存的用户token的键
    public static final String JWT_TOKEN_REDIS_KEY_PREFIX="UserAuth:JWT:Key:";


    //加密算法
    public final static String HASHALGORITHMNAME = "SHA-256";

    //循环次数(加密迭代次数)
    public final static int HASHITERATIONS = 20;

}
