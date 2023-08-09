package ccw.ruan.common.request;

/**
 *
 * @author cx
 */
public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),

    ERROR_UNKNOWN(400,"未知错误"),

    ERROR_404(404,"网页或文件未找到"),

    ERROR_505(500,"出错了"),
    PARAM_ERROR(501,"参数错误"),

    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    PASSWORD_ERROR(2001,"密码错误"),
    CODE_ERROR(2002,"验证码错误"),
    NOT_LOGIN(2003,"未登录"),
    TOKEN_TIME_OUT(2004,"token已过期"),
    TOKEN_EXCEPTION(2005,"token异常"),
    AUTHENTICATION_EXCEPTION(2006,"身份验证异常"),
    ACCESS_WAS_DENIED(2007,"访问被拒绝"),
    OPERATION_FAIL(2008,"操作失败"),
    UNAUTHORIZED_ACCESS(2009,"无权访问"),
    USERNAME_HAS_ALREADY_BEEN_USED(2010,"用户名已经被使用"),
    CODE_TIME_OUT(2011,"验证码过期"),
    USER_NOT_AUTHENTICATED(2012,"用户未认证"),
    USER_GENDER_MISMATCH(2013,"用户性别不符合"),
    TOKEN_IS_EMPTY(2014,"token未携带"),
    REDIS_IS_EMPTY(2015,"redis中为空")

    ;
    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
