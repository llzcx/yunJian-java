package ccw.ruan.resume.manager.http;

import ccw.ruan.resume.manager.http.dto.CalculateSimilarityDto;
import com.dtflys.forest.annotation.*;

/**
 * @author 陈翔
 */
@BaseRequest(
        baseURL = "http://127.0.0.1:7070",     // 默认域名
        headers = {
                "Accept:text/plain",                // 默认请求头
                "Content-Type:application/json",
                "Accept:*/*",
        }
)
public interface ResumeHandleClient {

    /**
     * 用作向python发送请求处理简历文件
     * @param fileName
     * @param fileFormat
     * @return resumeFile
     */
    @Get(value = "/ResumeFile",timeout = 1000000000)
    String resumeFile(@Query("fileName") String fileName, @Query("fileFormat") String fileFormat);



}
