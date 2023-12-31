package ccw.ruan.job.manager.http;


import ccw.ruan.common.config.PyClient;
import ccw.ruan.job.manager.http.dto.JobPersonFitDto;
import ccw.ruan.job.manager.http.dto.PersonJobFitDto;
import com.dtflys.forest.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 陈翔
 */
@BaseRequest(
        baseURL = PyClient.R9000P,     // 默认域名
        headers = {
                "Accept:text/plain",                // 默认请求头
                "Content-Type:application/json",
                "Accept:*/*",
        }
)
public interface PersonJobClient {
    /**
     * 测试
     * @param name
     * @return
     */
    @Get("/hello")
    String send(@Query("name") String name);
    /**
     * 计算人岗匹配度
     * @param
     * @return
     */
    @Post("/resume/personJob")
    List<BigDecimal> personJobFit(@JSONBody PersonJobFitDto personJobFitDto);
    /**
     * 计算岗人匹配度
     * @param
     * @return
     */
    @Post("/resume/jobPerson")
    List<BigDecimal> jobPersonFit(@JSONBody JobPersonFitDto jobPersonFitDto);
}
