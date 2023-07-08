package ccw.ruan.common.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 陈翔
 */
@Data
public class AddFlowPathNodeDto implements Serializable {

    /**
     * 流程名字
     */
    private String name;

    /**
     * 颜色
     */
    private String color;
    /**
     * 追加到哪个流程后面 active:1 success:2 fail:3
     */
    private Integer flowType;
}
