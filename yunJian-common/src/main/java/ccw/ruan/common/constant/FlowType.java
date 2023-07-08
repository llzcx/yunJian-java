package ccw.ruan.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 陈翔
 */
@Getter
public enum FlowType {
    /**
     * 活跃
     */
    ACTIVE(1,"活跃"),
    /**
     * 成功
     */
    SUCCESS(2,"成功"),
    /**
     * 失败
     */
    FAIL(3,"失败"),
    ;


    private Integer code;
    private String message;


    FlowType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
