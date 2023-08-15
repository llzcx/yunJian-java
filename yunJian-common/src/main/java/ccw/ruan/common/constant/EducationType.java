package ccw.ruan.common.constant;

import lombok.Getter;

/**
 * @author 周威宇
 */
@Getter
public enum EducationType {
    /**
     * 高中
     */
    HIGH_SCHOOL(1,"高中"),
    /**
     * 大专
     */
    JUNIOR_COLLEGE(2,"大专"),
    /**
     * 本科
     */
    UNDERGRADUATE_COURSE(3,"本科"),
    /**
     * 硕士
     */
    MASTER(4,"硕士"),
    /**
     * 博士
     */
    DOCTOR(5,"博士"),
    ;


    private Integer code;
    private String message;


    EducationType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Integer getEnum(String msg) {
        for (EducationType item : values()) {
            if (item.getMessage().equals(msg)) {
                return item.getCode();
            }
        }
        return 0;
    }
}
