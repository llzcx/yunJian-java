package ccw.ruan.common.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 陈翔
 */
@Data
@EqualsAndHashCode
public class AddTemplateDto implements Serializable {
    /**
     * 模板名字
     */
    private String templateName;

    /**
     * 模板内容
     */
    private String template;

    /**
     * 面试邀约模板：1 入职邀约模板：2
     */
    private Integer type;

}
