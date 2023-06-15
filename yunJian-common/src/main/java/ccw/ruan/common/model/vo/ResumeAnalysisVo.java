package ccw.ruan.common.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 周威宇
 */
@Data
public class ResumeAnalysisVo {
    /**
     * id
     */
    private Integer id;
    /**
     * 名字
     */
    private String name;

    /**
     * 出生日期
     */
    private String dateOfBirth;

    /**
     * 毕业院校
     */
    private String graduationInstitution;

    /**
     * 性别
     */
    private String sex;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mailBox;

    /**
     * 学历
     */
    private String education;

    /**
     * 专业
     */
    private String  major;

    /**
     * 意向工作
     */
    private String expectedJob;

    /**
     * 实习经历
     */
    private List<PracticeExperience> practiceExperiences;

    /**
     * 工作经历
     */
    private List<WorkExperience> workExperiences;


    /**
     * 自我评价
     */
    private String selfEvaluation;

}
