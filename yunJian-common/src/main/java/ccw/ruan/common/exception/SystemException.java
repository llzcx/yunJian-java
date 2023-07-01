package ccw.ruan.common.exception;



import ccw.ruan.common.request.ResultCode;
import lombok.Data;

/**
 * 自定义业务异常类
 * @author 陈翔
 */

@Data
public class SystemException extends RuntimeException {

    private ResultCode resultCode;
    private Integer code;
    private String msg;

    public SystemException() {
        super();
        this.resultCode = ResultCode.COMMON_FAIL;
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();

    }

    public SystemException(ResultCode resultCode) {
        super("{code:" + resultCode.getCode() + ",Msg:" + resultCode.getMessage() + "}");
        this.resultCode = resultCode;
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
    }

    public SystemException(Integer code, String msg) {
        super("{code:" + code + ",Msg:" + msg + "}");
        this.code = code;
        this.msg = msg;
    }

    public SystemException(Integer code, String msg, Object... args) {
        super("{code:" + code + ",Msg:" + String.format(msg, args) + "}");
        this.code = code;
        this.msg = String.format(msg, args);
    }

}
