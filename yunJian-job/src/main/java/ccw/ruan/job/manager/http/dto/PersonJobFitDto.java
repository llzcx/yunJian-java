package ccw.ruan.job.manager.http.dto;


import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.common.model.pojo.Resume;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class PersonJobFitDto implements Serializable {
    private String host;
    private List<String> resumes;

    public PersonJobFitDto(String host, List<String> resumes) {
        this.host = host;
        this.resumes = resumes;
    }


}
