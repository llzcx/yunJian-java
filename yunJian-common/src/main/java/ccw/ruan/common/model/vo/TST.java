package ccw.ruan.common.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TST implements Serializable {
    int id;
    /**
     * 名字
     */
    private String name;
    /**
     * 毕业院校
     */
    private String graduationInstitution;
    /**
     * 年龄
     */
    private String age;
    /**
     * 学历
     */
    private String education;
    /**
     * 工作年限
     */
    private Integer workYears;
    /**
     * 匹配岗位
     */
    private String positionMatching;
}
