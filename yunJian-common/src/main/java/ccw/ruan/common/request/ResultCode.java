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
