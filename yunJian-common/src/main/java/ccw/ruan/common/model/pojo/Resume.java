package ccw.ruan.common.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 简历表
 * </p>
 *
 * @author 陈翔
 * @since 2023-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 简历ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 姓名
     */
    private String fullName;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 年龄
     */
    private String age;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 简历内容
     */
    private String content;

    /**
     * 存储路径
     */
    private String path;

    /**
     * 简历状态
     */
    private Integer resumeStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
