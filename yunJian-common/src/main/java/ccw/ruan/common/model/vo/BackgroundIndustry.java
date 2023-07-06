package ccw.ruan.common.model.vo;

import lombok.Data;

/**
 * @author 周威宇
 * 背景行业表判断数据
 */
@Data
public class BackgroundIndustry {
    /**
     * 产品领域标签数量
     */
    private Integer product;
    /**
     * 工程师领域标签数量
     */
    private Integer engineer;
    /**
     * 广告领域标签数量
     */
    private Integer advertisement;
    /**
     * 互联网领域标签数量
     */
    private Integer internet;
    /**
     * 建筑房地产领域标签数量
     */
    private Integer build;
    /**
     * 教育翻译服务业领域标签数量
     */
    private Integer educationTranslate;
    /**
     * 金融领域标签数量
     */
    private Integer finance;
    /**
     * 媒体领域标签数量
     */
    private Integer medium;
    /**
     *生产采购物流领域标签数量
     */
    private Integer logisticsProcure;
    /**
     *生物制药医疗护理领域标签数量
     */
    private Integer treatPharmacy;
    /**
     *市场运营领域标签数量
     */
    private Integer marketOperations;
    /**
     *行政管理领域标签数量
     */
    private Integer administration;
    /**
     *咨询法律公务员领域标签数量
     */
    private Integer legalAdvice;
}
