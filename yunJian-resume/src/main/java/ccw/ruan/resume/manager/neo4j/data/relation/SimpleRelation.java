package ccw.ruan.resume.manager.neo4j.data.relation;

import ccw.ruan.resume.manager.neo4j.data.node.CityNode;
import ccw.ruan.resume.manager.neo4j.data.node.UniversityNode;
import ccw.ruan.resume.manager.neo4j.data.node.UniversitySimpleNameNode;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @author 陈翔
 */
@RelationshipEntity(type="简称")
@Data
public class SimpleRelation {
    @StartNode
    private UniversityNode startNode;

    @EndNode
    private UniversitySimpleNameNode endNode;
}
