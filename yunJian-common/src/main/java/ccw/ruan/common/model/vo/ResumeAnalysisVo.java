package ccw.ruan.common.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 周威宇
 */
@Data
public class ResumeAnalysisVo implements Serializable {
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
     * 年龄
     */
    private String age;
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
     * 项目经历
     */
    private String projectExperiences;

    /**
     * 工作年限
     */
    private Integer workYears;

    /**
     * 工作经历
     */
    private List<WorkExperience> workExperiences;
    /**
     * 技能证书
     */
    private String skillsCertificate;

    /**
     * 奖项荣誉
     */
    private String awardsHonors;
    /**
     * 人才画像解析结果
     */
    private ResumeLabelsVo labelProcessing;
    /**
     * 简历亮点
     * */
    private  String resumeHighlights;
    /**
     * 风险提示
     * */
    private  String riskWarning;
    /**
     * 智能预测
     * */
    private  String intelligentPrediction;

}
