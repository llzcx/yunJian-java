package ccw.ruan.common.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 周威宇
 */
@Data
public class ResumeMqMessageVo implements Serializable {
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 简历Id
     */
    private Integer resumeId;
}
