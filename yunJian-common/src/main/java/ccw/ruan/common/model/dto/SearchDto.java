package ccw.ruan.common.model.dto;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;

/**
 * @author 陈翔
 */
@Data
public class SearchDto {

    /**
     * 基本信息检索
     */
    private Basic basic;
    /**
     * 联系方式检索
     */
    private Contact contact;
    /**
     * 工作年限检索
     */
    private WorkYear workYear;
    /**
     * 工作经历检索
     */
    private WorkExperience workExperience;
    /**
     * 其他检索
     */
    private Other other;
    /**
     * 基本信息检索
     */
    private String fullText;
    /**
     * 基本信息检索
     */
    private Integer pageNum;
    /**
     * 基本信息检索
     */
    private Integer pageSize;
    /**
     * 基本信息
     */
    @Data
    public static class Basic {
        /**
         * 姓名
         */
        private String name;
        /**
         * 性别
         */
        private Boolean sex;
        /**
         * 最小年龄
         */
        private Integer minAge;
        /**
         * 最大年龄
         */
        private Integer maxAge;

        /**
         * 专业
         */
        private String major;

        /**
         * 期望工作
         */
        private String expectedJob;

        /**
         * 毕业院校
         */
        private String graduationInstitution;
    }

    /**
     * 联系方式
     */
    @Data
    public static class Contact {
        /**
         * 邮箱
         */
        private String email;
        /**
         * 手机
         */
        private String phone;
    }



    /**
     * 工作时间之和(年为单位)
     */
    @Data
    public static class WorkYear {
        /**
         * 一共最少了工作多长时间
         */
        private Integer lowerLimit;
        /**
         * 一共最多了工作多长时间
         */
        private Integer upperLimit;
    }

    /**
     * 工作经历
     */
    @Data
    public static class WorkExperience {
        /**
         * 公司名
         */
        private String company;
        /**
         * 职位名称
         */
        private String jobName;
        /**
         * 描述
         */
        private String description;
    }

    /**
     * 其他区
     */
    @Data
    public static class Other {
        /**
         * 技能
         */
        private String skillsCertificate;
        /**
         * 项目经历
         */
        private String projectExperiences;

        /**
         * 奖项荣誉
         */
        private String awardsHonors;
    }


}
