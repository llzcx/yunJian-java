package ccw.ruan.common.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ResumeCard {
    private Integer id;
    /**
     * z
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