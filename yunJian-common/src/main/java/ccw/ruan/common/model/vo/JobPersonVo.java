package ccw.ruan.common.model.vo;

import ccw.ruan.common.model.pojo.Job;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗人匹配
 * @author 陈翔
 */
@Data
@EqualsAndHashCode
public class JobPersonVo implements Serializable {
    private List<JPJobVo> list;
    @Data
    public static class JPJobVo {
        /**
         * 岗位
         */
        private Job job;
        /**
         * 岗位要求技能标签
         */
        private List<String> skills;
        /**
         * 得分
         */
        private BigDecimal score;
    }

    public void sortListByScore() {
        list = list.stream().filter(item -> item.getScore().compareTo(new BigDecimal("0.75")) > 0).collect(Collectors.toList());
        list.sort((o1, o2) -> {
            // 根据 score 进行比较
            return o2.getScore().compareTo(o1.getScore());
        });
    }
}
