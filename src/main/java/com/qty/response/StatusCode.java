package com.qty.response;

public enum StatusCode {

    Success(200,"success"),
    Fail(500,"fail"),

    RepeatAdd(500,"请勿重复添加"),

    InvalidParams(201,"非法的参数!"),
    UserNotLogin(202,"用户没登录"),

    UnknownError(500,"未知异常，请联系管理员!"),
    InvalidCode(501,"验证码不正确!"),
    AccountPasswordNotMatch(502,"账号密码不匹配!"),
    AccountHasBeenLocked(503,"账号已被锁定,请联系管理员!"),
    AccountValidateFail(504,"账户验证失败!"),
    TokenValidateExpireToken(60001,"Token已过期"),
    TokenValidateCheckFail(60002,"Token验证失败"),
    UserNamePasswordNotBlank(50000,"账户密码不能为空!"),
    AccessTokenInvalidate(70002,"无效的Token!"),
    AccessTokenNotExistRedis(80001,"Token不存在或已经过期-请重新登录!"),
    NOAUTH(402,"权限不足"),
    SQLEXCEPTION(250,"数据库异常，检查你的数据库操作"),
    TIMEOUTEXCEPTION(260,"连接超时"),
    PostCodeHasExist(4001,"岗位编码已存在!"),

    CurrUserHasNotPermission(505,"当前用户没有权限访问该资源或者操作！"),
    DeptHasSubDeptCanNotBeDelete(2001,"该部门下包含有子部门，请先删除子部门!"),

    PasswordCanNotBlank(1000,"密码不能为空!"),
    OldPasswordNotMatch(1001,"原密码不正确!"),
    UpdatePasswordFail(1002,"修改密码失败~请联系管理员!");

    private  Integer code;
    private  String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
