package ccw.ruan.common.model.vo;

import ccw.ruan.common.model.pojo.Resume;
import lombok.Data;
import org.w3c.dom.ls.LSException;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class SimilarityVo implements Serializable {
    /**
     * 相似度较高的简历对数组
     */
    private List<ResumePair> highSimilarity;
}
