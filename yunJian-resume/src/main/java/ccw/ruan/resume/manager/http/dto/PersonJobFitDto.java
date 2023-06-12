package ccw.ruan.resume.manager.http.dto;


import ccw.ruan.common.model.pojo.Job;
import ccw.ruan.common.model.pojo.Resume;
import lombok.Data;

import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class PersonJobFitDto {
    private Job job;
    private List<Resume> resumes;
}
