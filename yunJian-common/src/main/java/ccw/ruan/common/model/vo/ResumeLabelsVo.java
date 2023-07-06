package ccw.ruan.common.model.vo;
import lombok.Data;
import java.util.List;
/**
 * @author 周威宇
 * 简历技能标签以及领域覆盖区域
 */
@Data
public class ResumeLabelsVo {
    /**
     *综合能力表
     */
    private BackgroundIndustry backgroundIndustry;
    /**
     *背景行业表
     */
    private ComprehensiveAbility comprehensiveAbility;
    /**
     *技能标签集合
     */
    private List<String> skillTags;
    /**
     *教育背景标签集合
     */
    private List<String> educationTags;
    /**
     *职业标签集合
     */
     private List<String> jobTags;
}
