package ccw.ruan.common.constant;

/**
 * 日志枚举
 * @author 陈翔
 */
public enum LogTypeEnum {

    /**
     * 状态修改
     */
    STATE_CHANGE(1,"应聘人状态修改"),
    /**
     * 面试
     */
    INTERVIEW(2,"发送面试邀约"),
    /**
     * 入职
     */
    ON_BOARDING(3,"发送入职邀约"),
    ;


    private Integer code;
    private String message;

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

    LogTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public static LogTypeEnum getEnum(Integer code) {
        for (LogTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
