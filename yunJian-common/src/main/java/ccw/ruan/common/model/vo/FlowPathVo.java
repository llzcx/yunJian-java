package ccw.ruan.common.model.vo;

import ccw.ruan.common.model.pojo.FlowPathNode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class FlowPathVo implements Serializable {
    private List<FlowPathNode> active;
    private List<FlowPathNode> success;
    private List<FlowPathNode> fail;

    public FlowPathVo() {
    }

    public FlowPathVo(List<FlowPathNode> active, List<FlowPathNode> success, List<FlowPathNode> fail) {
        this.active = active;
        this.success = success;
        this.fail = fail;
    }
}
