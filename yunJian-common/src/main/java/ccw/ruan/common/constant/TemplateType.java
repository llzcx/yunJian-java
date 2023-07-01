package ccw.ruan.common.constant;

/**
 * 邀约模板类型
 * @author 陈翔
 */

public enum TemplateType {
    /**
     * 面试
     */
    INTERVIEW(1),
    /**
     * 入职
     */
    ON_BOARDING(2),
    ;


    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;

    }


    TemplateType(Integer code) {
        this.code = code;
    }
    public static TemplateType getEnum(Integer code) {
        for (TemplateType item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
