package ccw.ruan.common.model.vo;

import ccw.ruan.common.model.pojo.Resume;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class SimilarityVo implements Serializable {
    private List<ResumePair> resumes;
}
