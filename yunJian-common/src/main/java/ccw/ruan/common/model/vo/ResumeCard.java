package ccw.ruan.common.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ResumeCard {
    /**
     * 简历id
     */
    private Integer id;
    /**
     * 完整名字
     */
    private String fullName;
    /**
     * 流程状态
     */
    private Integer processStage;

    public ResumeCard(Integer id, String fullName, Integer processStage) {
        this.id = id;
        this.fullName = fullName;
        this.processStage = processStage;
    }

    public ResumeCard() {
    }
}