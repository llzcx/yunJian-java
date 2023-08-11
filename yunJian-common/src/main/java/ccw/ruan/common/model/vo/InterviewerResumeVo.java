package ccw.ruan.common.model.vo;

import ccw.ruan.common.model.pojo.Evaluate;
import ccw.ruan.common.model.pojo.Resume;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 陈翔
 */
@Data
@EqualsAndHashCode
public class InterviewerResumeVo {
    /**
     * 简历
     */
    Resume resume;
    /**
     * 所有评价
     */
    List<Evaluate> evaluateList;
}
