package ccw.ruan.resume.manager.neo4j.data.relation;


import ccw.ruan.resume.manager.neo4j.data.node.UniversityLevelNode;
import ccw.ruan.resume.manager.neo4j.data.node.UniversityNode;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @author 陈翔
 */
@RelationshipEntity(type="属于")
@Data
public class BelongToRelation {
    @StartNode
    private UniversityNode startNode;

    @EndNode
    private UniversityLevelNode endNode;
}
