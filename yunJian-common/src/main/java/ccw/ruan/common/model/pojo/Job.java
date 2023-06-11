package ccw.ruan.common.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 岗位表
 * </p>
 *
 * @author 陈翔
 * @since 2023-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 岗位名
     */
    private String name;

    /**
     * 岗位描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
