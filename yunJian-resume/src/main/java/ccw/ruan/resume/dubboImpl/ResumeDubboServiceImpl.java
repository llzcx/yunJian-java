package ccw.ruan.resume.dubboImpl;

import ccw.ruan.service.ResumeDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author 陈翔
 */
@Service
@DubboService(version = "1.0.0", group = "resume",interfaceClass = ResumeDubboService.class)
public class ResumeDubboServiceImpl implements ResumeDubboService {

}
