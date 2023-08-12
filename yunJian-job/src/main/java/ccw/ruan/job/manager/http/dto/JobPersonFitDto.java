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
    private List<String> jobs;

    public JobPersonFitDto(String resume, List<String> jobs) {
        this.resume = resume;
        this.jobs = jobs;
    }

    public JobPersonFitDto() {
    }
}
