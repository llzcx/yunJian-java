package ccw.ruan.common.model.vo;

import lombok.Data;

/**
 * @author 周威宇
 */
@Data
public class ResumeAnalysisVo {
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
     * 工作经历开始时间
     */
    private String workStartTime;
    /**
     * 工作经历结束时间
     */
    private String workEndTime;
    /**
     * 年龄
     */
    private String ago;
    /**
     * 学历
     */
    private String education;
    /**
     * 自我评价
     */
    private String selfEvaluation;
    /**
     * 工作过的第一个公司
     */
    private String firstCompany;
    /**
     * 工作过的最后一个公司
     */
    private String lastCompany;
}
