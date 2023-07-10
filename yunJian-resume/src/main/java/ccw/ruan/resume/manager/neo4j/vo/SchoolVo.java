package ccw.ruan.resume.manager.neo4j.vo;

import ccw.ruan.resume.manager.neo4j.data.node.*;
import ccw.ruan.resume.manager.neo4j.data.relation.SimpleRelation;
import com.sun.el.parser.SimpleNode;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 单个学校知识图谱vo
 * @author 陈翔
 */
@Data
public class SchoolVo implements Serializable {
    /**
     * 待替换的词
     */
    private String replaceName;
    /**
     * 大学基本信息
     */
    private List<UniversityNode> universityNode;
    /**
     * 各个学科
     */
    private List<DisciplineNode> disciplineList;
    /**
     * 所在城市
     */
    private List<CityNode> cityNode;
    /**
     * 简写
     */
    private List<UniversitySimpleNameNode> simpleNodes;
    /**
     * 主管单位
     */
    private List<SponsorNode> sponsorNode;
    /**
     * 学校层次
     */
    private List<UniversityLevelNode> universityLevelNode;
}
