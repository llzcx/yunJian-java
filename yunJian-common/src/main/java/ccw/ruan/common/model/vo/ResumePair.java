package ccw.ruan.common.model.vo;

import ccw.ruan.common.model.pojo.Resume;
import lombok.Data;

/**
 * @author 陈翔
 */
@Data
public class ResumePair {
    private Resume resume1;
    private Resume resume2;
    private Float score;

    public ResumePair(Resume resume1, Resume resume2) {
        this.resume1 = resume1;
        this.resume2 = resume2;
    }
}
