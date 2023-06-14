package ccw.ruan.resume.manager.neo4j.data.node;


import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * 学科节点
 * @author 陈翔
 */
@NodeEntity(label="学科")
@Data
public class DisciplineNode {
    @Property("name")
    private String name;
}
