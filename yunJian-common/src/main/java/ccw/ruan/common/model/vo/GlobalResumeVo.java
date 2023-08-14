package ccw.ruan.common.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Data
public class GlobalResumeVo implements Serializable{
    /**
     * 人才库简历总数
     */
    private Integer num1;
    /**
     * 现有岗位总数据
     */
    private Integer num2;
    /**
     * 公司需求数
     */
    private Integer num3;
    /**
     * 完成率
     */
    private String num4;
    /**
     * 男性人数
     */
    private Integer num5;

    /**
     * 女性人数
     */
    private Integer num6;

    /**
     * 状态节点
     */
    private List<Stage> stages;

    /**
     *  按照后面条件的年龄分布["26以下", "26-30", "30-34", "34-38", "38-42", "42以上"]
     */
    private List<Integer> ages;

    /**
     * 按照后面条件顺序的学历分布情况 ["博士","硕士","本科","大专","高中"]
     */
    private List<Integer> educations;

    /**
     * 按照后面条件的工作经验时长分布["0经验", "(0,3]年", "(3,5]年经验", "(5,10]年经验", "(10,~]年经验"]
     */
    private List<Integer> experiences;


    @Data
    public static class Stage implements Serializable {
        /**
         * 流程名
         */
        private String name;
        /**
         * 简历数量
         */
        private Integer value;

        public Stage(String name, Integer value) {
            this.name = name;
            this.value = value;
        }
    }
}
