package ccw.ruan.resume.manager.neo4j.data.node;

import lombok.Data;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;


/**
 * 主管单位节点
 * @author 陈翔
 */
@NodeEntity(label="主管单位")
@Data
public class SponsorNode {

    @Property("name")
    private String name;

}
