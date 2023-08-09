package ccw.ruan.job.manager.http;


import ccw.ruan.job.manager.http.dto.PersonJobFitDto;
import com.dtflys.forest.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 陈翔
 */
@BaseRequest(
        baseURL = "http://{py.jobAnalysis}",     // 默认域名
        headers = {
                "Accept:text/plain",                // 默认请求头
                "Content-Type:application/json",
                "Accept:*/*",
        }
)
public interface PyClient1 {
    /**
     * 岗位解析
     * @param jobContent
     * @return
     */
    @Get(value = "/JobAnalysis",timeout = 1000000000)
    String jobAnalysis(@Query("JobContent") String jobContent);

}
