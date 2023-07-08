package ccw.ruan.common.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 周威宇
 */
@Data
public class JobVo implements Serializable {
    /**
     * 岗位名
     */
    private String name;

    /**
     * 岗位职责
     */
    private String responsibility;

    /**
     * 岗位要求
     */
    private String require;
    /**
     * 岗位要求标签集合
     */
    private List<String> professionalLabel;
    /**
     * 学历要求
     */
    private Integer educationalRequirements;
    /**
     * 专业要求
     */
    private String professionalRequirements;
    /**
     * 性别要求
     */
    private Integer sexRequirements;
    /**
     * 工作经验要求
     */
    private Integer workExperienceRequirements;
}
