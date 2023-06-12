package ccw.ruan.resume.manager.http;

import ccw.ruan.resume.manager.http.dto.CalculateSimilarityDto;
import ccw.ruan.resume.manager.http.dto.PersonJobFitDto;
import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Query;

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
    @Get("/resume/similarity")
    String calculateSimilarity(@JSONBody CalculateSimilarityDto calculateSimilarityDto);

}
