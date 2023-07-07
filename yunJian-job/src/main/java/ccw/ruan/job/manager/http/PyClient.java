package ccw.ruan.job.manager.http;


import ccw.ruan.job.manager.http.dto.PersonJobFitDto;
import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Query;

import java.util.List;

/**
 * @author 陈翔
 */
@BaseRequest(
        baseURL = "http://localhost:7070",     // 默认域名
        headers = {
                "Accept:text/plain"                // 默认请求头
        }
)
public interface PyClient {
    /**
     * 计算人岗匹配度
     * @param personJobFitDto
     * @return
     */
    @Get("/resume/personJob")
    List<Float> personJobFit(@JSONBody PersonJobFitDto personJobFitDto);

}
