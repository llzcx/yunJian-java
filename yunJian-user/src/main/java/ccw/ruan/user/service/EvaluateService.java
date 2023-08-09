package ccw.ruan.user.service;

import ccw.ruan.common.model.dto.AddEvaluateDto;
import ccw.ruan.common.model.pojo.Evaluate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 陈翔
 */
public interface EvaluateService extends IService<Evaluate> {

    /**
     * 添加一条面评
     * @param userId
     * @param addEvaluateDto
     */
    Evaluate saveEvaluate(Integer userId, AddEvaluateDto addEvaluateDto);


    /**
     * 获取面评列表
     * @param resumeId
     * @return
     */
    List<Evaluate> getEvaluateList(Integer resumeId);
}