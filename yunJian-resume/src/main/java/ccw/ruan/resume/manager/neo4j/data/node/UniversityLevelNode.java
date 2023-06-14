package ccw.ruan.resume.manager.neo4j.data.node;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * 学校层次
 * @author 陈翔
 */
@NodeEntity(label="学校层次")
@Data
public class UniversityLevelNode {
    @Property("name")
    private String name;
}
