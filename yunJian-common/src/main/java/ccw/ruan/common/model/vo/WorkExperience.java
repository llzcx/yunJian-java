package ccw.ruan.common.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 陈翔
 */
@Data
public class WorkExperience implements Serializable {
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 工作名称
     */
    private String jobName;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 工作描述
     */
    private String description;


}
