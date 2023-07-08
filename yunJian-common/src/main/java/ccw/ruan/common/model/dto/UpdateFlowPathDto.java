package ccw.ruan.common.model.dto;

import ccw.ruan.common.model.pojo.FlowPathNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
@EqualsAndHashCode
public class UpdateFlowPathDto implements Serializable {
    private List<FlowPathNode> active;
    private List<FlowPathNode> success;
    private List<FlowPathNode> fail;

    public Integer getTotal(){
        return active.size() + success.size() + fail.size();
    }
}
