package ccw.ruan.common.constant;

import lombok.Getter;

/**
 * @author 周威宇
 */
@Getter
public enum ResumeState {
    /**
     * 解析完成
     */
    COMPLETE(1,"完成"),
    /**
     * 未解析完成
     */
    INCOMPLETE(0,"未完成"),
    ;


    private Integer code;
    private String message;


    ResumeState(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
