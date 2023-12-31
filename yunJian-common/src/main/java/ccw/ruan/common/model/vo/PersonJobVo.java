package ccw.ruan.common.model.vo;

import ccw.ruan.common.model.pojo.Resume;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 人岗匹配返回的数据
 * @author 陈翔
 */
@Data
public class PersonJobVo implements Serializable {


    private List<PJResumeVo> list;

    private static BigDecimal base = new BigDecimal("0.75");

    @Data
    public static class PJResumeVo {
        private Resume resume;
        private List<String> skills;
        private BigDecimal score;
    }

    public void sortListByScore() {
        list = list.stream().filter(item->item.getScore().compareTo(base)>0).collect(Collectors.toList());
        // 使用 Collections.sort() 方法，传入自定义的 Comparator 对象
        Collections.sort(list, new Comparator<PJResumeVo>() {
            @Override
            public int compare(PJResumeVo o1, PJResumeVo o2) {
                // 根据 score 进行比较
                return o2.getScore().compareTo(o1.getScore());
            }
        });
    }

}
