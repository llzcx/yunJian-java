package ccw.ruan.common.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 陈翔
 */
@Data
@TableName("head_node")
public class HeadNode {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("node_id")
    private Integer nodeId;

    @TableField("user_id")
    private Integer userId;
}