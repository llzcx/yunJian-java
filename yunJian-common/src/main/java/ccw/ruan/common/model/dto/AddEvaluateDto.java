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

    @TableField("resume_id")
    private Integer resumeId;

    @TableField("score")
    private Integer score;

    @TableField("text")
    private String text;
}
