package ccw.ruan.common.model.vo;

import ccw.ruan.common.model.pojo.Resume;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 人岗匹配返回的数据
 * @author 陈翔
 */
@Data
public class PersonJobVo implements Serializable {


    private List<PJResumeVo> list;


    @Data
    public static class PJResumeVo {
        private Resume resume;
        private List<String> skills;
        private Float score;
    }

}
