package ccw.ruan.resume.manager.neo4j.vo;

import ccw.ruan.resume.manager.neo4j.data.node.*;
import ccw.ruan.resume.manager.neo4j.data.relation.SimpleRelation;
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
     * 大学基本信息
     */
    private UniversityNode universityNode;
    /**
     * 各个学科
     */
    private List<DisciplineNode> disciplineList;
    /**
     * 所在城市
     */
    private CityNode cityNode;
    /**
     * 简写
     */
    private SimpleRelation simpleRelation;
    /**
     * 主管单位
     */
    private SponsorNode sponsorNode;
    /**
     * 学校层次
     */
    private UniversityLevelNode universityLevelNode;
}
