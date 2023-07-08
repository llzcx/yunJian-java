package ccw.ruan.common.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 陈翔
 */
@Data
@EqualsAndHashCode
public class UpdateFlowPathNodeDto implements Serializable {
    /**
     * 流程名字
     */
    private String name;

    /**
     * 颜色
     */
    private String color;
}
