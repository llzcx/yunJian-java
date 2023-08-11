package ccw.ruan.common.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 陈翔
 */
@Data
@TableName("evaluate")
public class Evaluate {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;


    /**
     * 简历id
     */
    private Integer resumeId;

    /**
     * 技能评估
     */
    private String skill;

    /**
     * 总结与建议
     */
    private String summarize;

    /**
     * 综合评价
     */
    private String composite;


    /**
     * 评价时间
     */
    private LocalDateTime createTime;
}