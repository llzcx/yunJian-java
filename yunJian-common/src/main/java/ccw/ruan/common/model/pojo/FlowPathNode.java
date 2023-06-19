package ccw.ruan.common.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 面试流程
 * @author 陈翔
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FlowPathNode {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String color;

    private Integer userId;

    public FlowPathNode() {

    }

    public FlowPathNode(String name, String color, Integer userId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.userId = userId;
    }
}
