package ccw.ruan.user.service.impl;

import ccw.ruan.common.model.dto.UpdateFlowPathDto;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.vo.FlowPathVo;
import ccw.ruan.user.RedisConstant;
import ccw.ruan.user.mapper.FlowPathMapper;
import ccw.ruan.user.service.FlowPathService;
import ccw.ruan.user.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ccw.ruan.user.RedisConstant.FLOW_PATH;
import static ccw.ruan.user.RedisConstant.RAS;

/**
 * @author 陈翔
 */
@Service
public class FlowPathServiceImpl extends ServiceImpl<FlowPathMapper, FlowPathNode> implements FlowPathService {

    @Autowired
    FlowPathMapper flowPathMapper;

    @Autowired
    RedisUtil redisUtil;




    private FlowPathVo getFlowPathVo(Integer userId){
        return JSONObject.parseObject(redisUtil.get(RAS+FLOW_PATH+userId), FlowPathVo.class);
    }

    @Override
    public FlowPathVo flowPathService(Integer userId) {
       return getFlowPathVo(userId);
    }

    @Override
    public Boolean updateFlowPath(Integer userId ,UpdateFlowPathDto updateFlowPathDto) {
        redisUtil.set(RAS+ FLOW_PATH+userId,JSONObject.toJSONString(updateFlowPathDto));
        return true;
    }

    @Override
    public FlowPathNode getFirstFlowPathNoe(Integer userId) {
        final FlowPathVo flowPathVo = getFlowPathVo(userId);
        return flowPathVo.getActive().get(0);
    }
}