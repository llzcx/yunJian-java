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
     * 岗位职责
     */
    private String responsibility;

    /**
     * 岗位要求
     */
    private String require;
    /**
     * 岗位要求标签集合
     */
    private String professionalLabel;
    /**
     * 学历要求
     */
    private Integer educationalRequirements;
    /**
     * 专业要求
     */
    private String professionalRequirements;
    /**
     * 性别要求
     */
    private Integer sexRequirements;
    /**
     * 工作经验要求
     */
    private Integer workExperienceRequirements;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
