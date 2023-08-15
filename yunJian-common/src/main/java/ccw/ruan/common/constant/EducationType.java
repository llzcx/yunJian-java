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
    POLYTECHNIC_SCHOOL(1,"中专"),
    /**
     * 高中
     */
    HIGH_SCHOOL(2,"高中"),
    /**
     * 大专
     */
    JUNIOR_COLLEGE(3,"大专"),
    /**
     * 本科
     */
    UNDERGRADUATE_COURSE(4,"本科"),
    /**
     * 硕士
     */
    MASTER(5,"硕士"),
    /**
     * 博士
     */
    DOCTOR(6,"博士"),
    ;


    private Integer code;
    private String message;


    EducationType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
