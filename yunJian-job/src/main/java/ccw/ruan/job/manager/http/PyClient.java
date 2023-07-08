package ccw.ruan.job.manager.http;


import ccw.ruan.job.manager.http.dto.PersonJobFitDto;
import com.dtflys.forest.annotation.*;
import com.dtflys.forest.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 陈翔
 */
@BaseRequest(
        baseURL = "http://192.168.50.47:7070",     // 默认域名
        headers = {
                "Accept:text/plain",                // 默认请求头
                "Content-Type:application/json",
                "Accept:*/*",
        }
)
public interface PyClient {
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
    @Post("/resume/getScore")
    List<BigDecimal> personJobFit(@JSONBody PersonJobFitDto personJobFitDto);


}
