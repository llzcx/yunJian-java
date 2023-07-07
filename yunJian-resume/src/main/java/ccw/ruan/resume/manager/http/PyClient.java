package ccw.ruan.resume.manager.http;

import ccw.ruan.resume.manager.http.dto.CalculateSimilarityDto;
import com.dtflys.forest.annotation.*;

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
     * 用作向python发送请求处理简历文件
     * @param fileName
     * @param fileFormat
     * @return resumeFile
     */
    @Get(value = "/ResumeFile",timeout = 1000000000)
    String resumeFile(@Query("fileName") String fileName, @Query("fileFormat") String fileFormat);
    /**
     * 计算简历相似度
     * @param calculateSimilarityDto
     * @return
     */
    @Post(value = "/resume/calculateSimilarity",timeout = 1000000000)
    String calculateSimilarity(@JSONBody CalculateSimilarityDto calculateSimilarityDto);

}
