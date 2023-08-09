package ccw.ruan.common.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("evaluate")
public class Evaluate {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("resume_id")
    private Integer resumeId;

    @TableField("score")
    private Integer score;

    @TableField("text")
    private String text;

    @TableField("create_time")
    private LocalDateTime createTime;
    
    // 省略构造方法、getter和setter

    // 可选：您还可以重写 toString 方法以便在打印对象时更友好地显示字段值
    
    // 请确保使用了正确的包引入和注解，例如：import java.util.Date;、import lombok.Data;
}