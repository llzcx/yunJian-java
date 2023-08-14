package ccw.ruan.common.model.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 周威宇
 */
@Data
public class TalentPortrait implements Serializable {
     /**
     * ID
     * */
     private  int id;
    /**
     * 名字
     * */
     private  String name;
     /**
     * 简历亮点
     * */
     private  String sparkle;
     /**
     * 风险提示
     * */
     private  String riskWarning;
    /**
     * 智能预测
     * */
     private  String intelligentPrediction;

}
