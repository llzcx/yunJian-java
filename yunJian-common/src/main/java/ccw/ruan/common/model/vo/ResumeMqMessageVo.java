package ccw.ruan.common.model.vo;

import lombok.Data;

/**
 * @author 周威宇
 */
@Data
public class ResumeMqMessageVo {
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 简历Id
     */
    private Integer resumeId;
}
