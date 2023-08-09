package ccw.ruan.common.model.vo;


import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.pojo.User;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class InterviewerAndNodeVo implements Serializable {
    User user;
    List<FlowPathNode> list;
}
