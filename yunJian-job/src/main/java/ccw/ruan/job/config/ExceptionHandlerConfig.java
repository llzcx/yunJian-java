package ccw.ruan.job.config;


import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.request.ApiResp;
import ccw.ruan.common.request.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;

/**
 * RestControllerAdvice，统一异常处理
 * @author 陈翔
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerConfig {

    /**
     * Exception出错的栈信息转成字符串
     * 用于打印到日志中
     */
    public static String errorInfoToString(Throwable e) {
        //try-with-resource语法糖 处理机制
        try(StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)){
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            return sw.toString();
        }catch (Exception ignored){
            throw new RuntimeException(ignored.getMessage(),ignored);
        }
    }

    /**
     * 业务异常处理
     *
     * @param e 业务异常
     * @return
     */
    @ExceptionHandler(value = SystemException.class)
    @ResponseBody
    public ApiResp exceptionHandler(SystemException e) {
        log.error(errorInfoToString(e));
        return ApiResp.fail(e.getResultCode());
    }


    /**
     * 未知异常处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResp exceptionHandler(Exception e) {
        // 把错误信息输入到日志中
        log.error(errorInfoToString(e));
        return ApiResp.fail(ResultCode.ERROR_UNKNOWN);
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ApiResp exceptionHandler(NullPointerException e) {
        log.error(errorInfoToString(e));
        return ApiResp.fail(ResultCode.COMMON_FAIL);
    }


    /**
     * 文件上传异常
     * @param ex
     * @return
     */
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ApiResp ioException(IOException ex) {
        log.error(errorInfoToString(ex));
        return ApiResp.fail(ResultCode.COMMON_FAIL);
    }


    /**
     * py
     * @param ex
     * @return
     */
    @ExceptionHandler(ConnectException.class)
    @ResponseBody
    public ApiResp connectException(ConnectException ex) {
        log.error(errorInfoToString(ex));
        return ApiResp.fail(ResultCode.PY_APPLICATION_ERROR);
    }
}
