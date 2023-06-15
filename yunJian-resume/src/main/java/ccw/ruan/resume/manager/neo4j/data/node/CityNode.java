package ccw.ruan.resume.manager.neo4j.data.node;

import lombok.Data;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;


/**
 * 城市节点
 * @author 陈翔
 */
@NodeEntity(label="城市")
@Data
public class CityNode {

    @Property("name")
    private String name;

    
}
