package ccw.ruan.user.mapper;

import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.pojo.HeadNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 陈翔
 */
@Mapper
public interface HeadNodeMapper extends BaseMapper<HeadNode> {
    /**
     * 获取面试官管理的所有流程节点
     * @param userId
     * @return
     */
    List<FlowPathNode> selectInterviewerNode(@Param("userId") Integer userId);
}