package ccw.ruan.common.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 陈翔
 */
@Data
@EqualsAndHashCode
public class FlowPathNodeVo {
    private Integer id;

    private String name;

    private String color;

    private Integer userId;

    private Integer cnt;

}
