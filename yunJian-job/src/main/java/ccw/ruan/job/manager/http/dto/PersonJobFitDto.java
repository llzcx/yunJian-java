package ccw.ruan.job.manager.http.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class PersonJobFitDto implements Serializable {
    private String post;
    private List<String> resumes;

    public PersonJobFitDto(String post, List<String> resumes) {
        this.post = post;
        this.resumes = resumes;
    }

    public PersonJobFitDto() {
    }
}
