package ccw.ruan.job.manager.http.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
@EqualsAndHashCode
public class JobPersonFitDto implements Serializable {
    private String resume;
    private List<String> job;

    public JobPersonFitDto(String resume, List<String> job) {
        this.resume = resume;
        this.job = job;
    }

    public JobPersonFitDto() {
    }
}
