package ccw.ruan.common.model.vo;

import ccw.ruan.common.model.pojo.Resume;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class ResumePair {
    private ResumeCard resume1;
    private ResumeCard resume2;
    private List<String> label;
    private BigDecimal score;

    public ResumePair() {

    }

    public ResumePair(ResumeCard resume1, ResumeCard resume2) {
        this.resume1= resume1;
        this.resume2 = resume2;
    }


}
