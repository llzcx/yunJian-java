package ccw.ruan.user;


import ccw.ruan.common.model.dto.AddEvaluateDto;
import ccw.ruan.common.model.pojo.Evaluate;
import ccw.ruan.common.model.pojo.FlowPathNode;
import ccw.ruan.user.service.EvaluateService;
import ccw.ruan.user.service.IFlowPathService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluateTest {

    @Autowired
    EvaluateService evaluateService;


    @Test
    public void getEvaluateList(){
        final List<Evaluate> evaluateList = evaluateService.getEvaluateList(22);
        for (Evaluate evaluate : evaluateList) {
            System.out.println(evaluate.toString());
        }
    }

    @Test
    public void saveEvaluate(){
        for (int i = 1; i <= 2; i++) {
            final AddEvaluateDto addEvaluateDto = new AddEvaluateDto();
            addEvaluateDto.setText("还不错"+i);
            addEvaluateDto.setScore(i);
            addEvaluateDto.setResumeId(22+(i-1));
            evaluateService.saveEvaluate(2, addEvaluateDto);
        }
    }

}
