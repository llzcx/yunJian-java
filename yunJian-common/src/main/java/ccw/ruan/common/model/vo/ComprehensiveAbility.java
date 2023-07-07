package ccw.ruan.common.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 周威宇
 * 综合能力表判断数据
 */
@Data
public class ComprehensiveAbility implements Serializable {
    /**
     * 所获荣誉
     */
    private Integer honorsReceived;
    /**
     * 教育背景
     */
    private Integer educationalBackground;
    /**
     * 语言能力
     */
    private Integer languageAbility;
    /**
     * 语言能力
     */
    private Integer leadership;
    /**
     * 工作年限
     */
    private Integer serviceYears;
    /**
     * 技能
     */
    private Integer skill;
}
