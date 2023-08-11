package ccw.ruan.resume.manager.http;

import ccw.ruan.common.config.PyClient;
import ccw.ruan.resume.manager.http.dto.CalculateSimilarityDto;
import com.dtflys.forest.annotation.*;

import java.math.BigDecimal;

/**
 * @author 陈翔
 */
@BaseRequest(
        baseURL = PyClient.CX,     // 默认域名
        headers = {
                "Accept:text/plain",                // 默认请求头
                "Content-Type:application/json",
                "Accept:*/*",
        }
)
public interface SimilarityClient {
    /**
     * 测试
     * @param name
     * @return
     */
    @Get("/hello")
    String send(@Query("name") String name);

    /**
     * 计算简历相似度
     * @param calculateSimilarityDto
     * @return
     */
    @Post(value = "/resume/calculateSimilarity",timeout = 1000000000)
    BigDecimal calculateSimilarity(@JSONBody CalculateSimilarityDto calculateSimilarityDto);

}
