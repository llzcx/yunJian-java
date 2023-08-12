package ccw.ruan.common.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 陈翔
 */
@Data

public class AddEvaluateDto implements Serializable {


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
}
