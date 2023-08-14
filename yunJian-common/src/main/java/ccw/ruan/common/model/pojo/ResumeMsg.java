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
@TableName("resume_msg")
@Data
public class ResumeMsg {

    @TableId(type = IdType.AUTO,value="id")
    private Integer id;

    private Boolean isRead;

    private Integer userId;
    
    private String msg;

    private Integer resumeId;
    
    private LocalDateTime createTime;


    public ResumeMsg() {
    }

    public ResumeMsg(Integer id, Boolean read) {
        this.id = id;
        this.isRead = read;
    }
}
