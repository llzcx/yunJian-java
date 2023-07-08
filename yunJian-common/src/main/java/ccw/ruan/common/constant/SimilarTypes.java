package ccw.ruan.common.constant;

import lombok.Getter;

/**
 * 简历相似度标签
 * @author 陈翔
 */
@Getter
public enum SimilarTypes {
    /**
     * 类别
     */
    NAME("姓名相同"),
    PROJECT_EXPERIENCE("项目经历相似"),
    WORK_EXPERIENCE("工作经历相似")
    ;

    private String message;
    SimilarTypes(String message) {
        this.message = message;
    }
}
