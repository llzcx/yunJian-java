package ccw.ruan.job.dubboimpl;

import ccw.ruan.service.JobDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author 陈翔
 */
@Service
@DubboService(version = "1.0.0", group = "job",interfaceClass = JobDubboService.class)
public class JobDubboServiceImpl implements JobDubboService {

    @Override
    public String get(){
        return "123";
    }

}
