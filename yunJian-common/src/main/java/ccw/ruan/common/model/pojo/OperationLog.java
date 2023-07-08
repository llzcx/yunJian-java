package ccw.ruan.common.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 类型
     * 状态修改STATE_CHANGE(1,"应聘人状态修改"),
     * 发送面试邀约INTERVIEW(2,"发送面试邀约"),
     * 发送入职邀约ON_BOARDING(3,"发送入职邀约"),
     */
    private Integer action;
    /**
     * 描述
     */
    private String detail;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    /**
     * 简历id
     */
    private Integer resumeId;
}