package ccw.ruan.user;

import ccw.ruan.common.model.dto.UpdateFlowPathDto;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.common.model.vo.FlowPathVo;
import ccw.ruan.common.model.vo.InterviewerAndNodeVo;
import ccw.ruan.common.util.JsonUtil;
import ccw.ruan.user.service.IFlowPathService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowNodeTest {

    @Autowired
    IFlowPathService flowPathService;


    @Test
    public void flowPathService(){
        final FlowPathVo flowPathVo = flowPathService.flowPathService(1);
        System.out.println(JsonUtil.object2StringSlice(flowPathVo));
    }


    @Test
    public void updateFlowPath(){
        UpdateFlowPathDto updateFlowPathDto = new UpdateFlowPathDto();
//        updateFlowPathDto.setActive();
       flowPathService.updateFlowPath(1,updateFlowPathDto);
    }

    @Test
    public void getFirstFlowPathNode(){
        final FlowPathNode firstFlowPathNode = flowPathService.getFirstFlowPathNode(1);
        System.out.println(firstFlowPathNode);
    }

    @Test
    public void listInterviewerSituation(){
        final List<InterviewerAndNodeVo> list = flowPathService.listInterviewerSituation(1);
        System.out.println(JsonUtil.object2StringSlice(list));
    }

}
