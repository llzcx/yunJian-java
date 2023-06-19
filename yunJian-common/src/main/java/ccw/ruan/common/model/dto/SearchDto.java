package ccw.ruan.common.model.dto;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;

/**
 * @author 陈翔
 */
@Data
public class SearchDto {

    private Basic basic;
    private Contact contact;
    private WorkYear workYear;
    private WorkExperience workExperience;
    private Other other;
    private String fullText;
    private Integer pageNum;
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
         * 自我描述
         */
        private String selfEvaluation;
    }


}