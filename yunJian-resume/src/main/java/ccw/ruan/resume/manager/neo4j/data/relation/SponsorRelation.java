package ccw.ruan.resume.manager.neo4j.data.relation;

import ccw.ruan.resume.manager.neo4j.data.node.SponsorNode;
import ccw.ruan.resume.manager.neo4j.data.node.UniversityNode;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @author 陈翔
 */
@RelationshipEntity(type="主管单位")
@Data
public class SponsorRelation {

    @StartNode
    private SponsorNode startNode;

    @EndNode
    private UniversityNode endNode;

}
