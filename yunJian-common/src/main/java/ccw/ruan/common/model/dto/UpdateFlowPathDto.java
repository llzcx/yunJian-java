package ccw.ruan.common.model.dto;

import ccw.ruan.common.model.pojo.FlowPathNode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class UpdateFlowPathDto implements Serializable {
    private List<FlowPathNode> active;
    private List<FlowPathNode> success;
    private List<FlowPathNode> fail;
}
