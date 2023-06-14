package ccw.ruan.resume.manager.neo4j.data.node;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;


/**
 * 大学简称
 * @author 陈翔
 */
@NodeEntity(label="大学简称")
@Data
public class UniversitySimpleNameNode {
    @Property("name")
    private String name;
}
