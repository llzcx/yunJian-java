package ccw.ruan.resume.manager.neo4j.data.node;

import lombok.Data;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;


/**
 * 大学节点
 * @author 陈翔
 */
@NodeEntity(label="大学")
@Data
public class UniversityNode {
    @Property("中文名")
    private String chineseName;
    @Property("校训")
    private String schoolMotto;
    @Property("code")
    private Long code;
    @Property("校歌")
    private String schoolSong;
    @Property("外文名")
    private String foreignName;
    @Property("学校类别")
    private String schoolType;
    @Property("创办人")
    private String founder;
    @Property("院系设置")
    private String departmentSetting;
    @Property("院校代码")
    private String departmentCode;
    @Property("现任领导")
    private String leader;
    @Property("校庆日")
    private String schoolDay;
    @Property("创办时间")
    private String creatTime;
    @Property("高职专业")
    private String vocationalMajor;
    @Property("主要奖项")
    private String awards;
    @Property("主管部门")
    private String sponsor;
    @Property("办学性质")
    private String level;
    @Property("name")
    private String name;
    @Property("地址")
    private String address;
    @Property("类型")
    private String type;
    @Property("简称")
    private String simpleName;
    @Property("desc")
    private String desc;
}
