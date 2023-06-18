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

    private String name;

    private String color;
}
