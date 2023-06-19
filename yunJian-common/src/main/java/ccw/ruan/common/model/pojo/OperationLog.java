package ccw.ruan.common.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 简历日志信息
 * @author 陈翔
 */
@Data
@TableName("operation_log")
public class OperationLog {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 类型
     */
    private Integer action;
    /**
     * 描述
     */
    private String detail;
    /**
     * 创建时间
     */
    private LocalDateTime time;
    /**
     * 简历id
     */
    private Integer resumeId;
}