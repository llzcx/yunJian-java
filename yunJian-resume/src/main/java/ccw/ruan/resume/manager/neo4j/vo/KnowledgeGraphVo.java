package ccw.ruan.resume.manager.neo4j.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
@Data
public class KnowledgeGraphVo implements Serializable {
    private List<SchoolVo> schoolVoList;

}
